package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ComplaintRepository
import com.example.model.Complaint
import com.example.model.ComplaintCategory
import com.example.model.ComplaintStatus
import com.example.model.Customer
import com.example.model.Priority
import com.example.model.SupportStaff
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen(val title: String, val iconName: String) {
    DASHBOARD("Dashboard", "dashboard"),
    REGISTER("Register Complaint", "add_circle"),
    ALL_COMPLAINTS("All Complaints", "list_alt"),
    PRIORITY_QUEUE("Priority Queue", "low_priority"),
    SUPPORT_STAFF("Support Staff", "groups"),
    CUSTOMER_HISTORY("Customer History", "history"),
    DATA_STRUCTURES("Data Structures", "account_tree"),
    REPORTS("Reports & Analytics", "bar_chart")
}

data class DashboardMetrics(
    val totalComplaints: Int,
    val openComplaints: Int,
    val criticalComplaints: Int,
    val inProgressComplaints: Int,
    val resolvedComplaints: Int,
    val availableStaff: Int,
    val resolutionRatePercentage: Int,
    val avgResolutionTimeHours: String
)

class ComplaintViewModel(
    private val repository: ComplaintRepository = ComplaintRepository()
) : ViewModel() {

    val allComplaints: StateFlow<List<Complaint>> = repository.allComplaintsFlow
    val pendingQueue: StateFlow<List<Complaint>> = repository.pendingQueueFlow
    val staffList: StateFlow<List<SupportStaff>> = repository.staffListFlow
    val customersList: StateFlow<List<Customer>> = repository.customersFlow

    // Navigation
    private val _currentScreen = MutableStateFlow(Screen.DASHBOARD)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Screen navigation stack for proper BackHandler support
    private val navigationBackStack = mutableListOf(Screen.DASHBOARD)

    // Notification / Toast
    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    // Dialog & Modal States
    private val _selectedComplaint = MutableStateFlow<Complaint?>(null)
    val selectedComplaint: StateFlow<Complaint?> = _selectedComplaint.asStateFlow()

    private val _assignDialogComplaint = MutableStateFlow<Complaint?>(null)
    val assignDialogComplaint: StateFlow<Complaint?> = _assignDialogComplaint.asStateFlow()

    private val _resolveDialogComplaint = MutableStateFlow<Complaint?>(null)
    val resolveDialogComplaint: StateFlow<Complaint?> = _resolveDialogComplaint.asStateFlow()

    private val _showRegisterDialog = MutableStateFlow(false)
    val showRegisterDialog: StateFlow<Boolean> = _showRegisterDialog.asStateFlow()

    private val _showAddStaffDialog = MutableStateFlow(false)
    val showAddStaffDialog: StateFlow<Boolean> = _showAddStaffDialog.asStateFlow()

    data class ComplaintFilters(
        val query: String = "",
        val priority: Priority? = null,
        val status: ComplaintStatus? = null,
        val category: ComplaintCategory? = null,
        val staffId: String? = null
    )

    // Filter states for All Complaints
    val searchQuery = MutableStateFlow("")
    val filterPriority = MutableStateFlow<Priority?>(null)
    val filterStatus = MutableStateFlow<ComplaintStatus?>(null)
    val filterCategory = MutableStateFlow<ComplaintCategory?>(null)
    val filterStaffId = MutableStateFlow<String?>(null)

    private val filtersFlow: StateFlow<ComplaintFilters> = combine(
        searchQuery,
        filterPriority,
        filterStatus,
        filterCategory,
        filterStaffId
    ) { query, priority, status, category, staffId ->
        ComplaintFilters(query, priority, status, category, staffId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ComplaintFilters())

    // Filtered Complaints
    val filteredComplaints: StateFlow<List<Complaint>> = combine(
        allComplaints,
        filtersFlow
    ) { list, filters ->
        list.filter { c ->
            val matchesQuery = filters.query.isBlank() ||
                c.id.contains(filters.query, ignoreCase = true) ||
                c.customerName.contains(filters.query, ignoreCase = true) ||
                c.title.contains(filters.query, ignoreCase = true)
            val matchesPriority = filters.priority == null || c.priority == filters.priority
            val matchesStatus = filters.status == null || c.status == filters.status
            val matchesCategory = filters.category == null || c.category == filters.category
            val matchesStaff = filters.staffId == null || c.assignedStaffId == filters.staffId
            matchesQuery && matchesPriority && matchesStatus && matchesCategory && matchesStaff
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Metrics for Dashboard
    val dashboardMetrics: StateFlow<DashboardMetrics> = combine(
        allComplaints,
        staffList
    ) { complaints, staff ->
        val total = complaints.size
        val open = complaints.count { it.status.isOpen }
        val critical = complaints.count { it.priority == Priority.CRITICAL && it.status.isOpen }
        val inProgress = complaints.count { it.status == ComplaintStatus.IN_PROGRESS }
        val resolved = complaints.count { it.status == ComplaintStatus.RESOLVED || it.status == ComplaintStatus.CLOSED }
        val available = staff.count { it.isAvailable }
        val resolutionRate = if (total > 0) (resolved * 100) / total else 0

        DashboardMetrics(
            totalComplaints = total,
            openComplaints = open,
            criticalComplaints = critical,
            inProgressComplaints = inProgress,
            resolvedComplaints = resolved,
            availableStaff = available,
            resolutionRatePercentage = resolutionRate,
            avgResolutionTimeHours = "4.2 hrs"
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DashboardMetrics(0, 0, 0, 0, 0, 0, 0, "0 hrs")
    )

    // Customer History Selected Customer ID
    private val _selectedCustomerId = MutableStateFlow("CUST-101")
    val selectedCustomerId: StateFlow<String> = _selectedCustomerId.asStateFlow()

    // Diagnostic state for HashMap exploration
    private val _diagnosticResult = MutableStateFlow<ComplaintRepository.LookupDiagnostic?>(null)
    val diagnosticResult: StateFlow<ComplaintRepository.LookupDiagnostic?> = _diagnosticResult.asStateFlow()

    init {
        // Initial lookup test
        testLookup("C1002")
    }

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            navigationBackStack.add(screen)
            _currentScreen.value = screen
        }
    }

    fun handleBack(): Boolean {
        return if (navigationBackStack.size > 1) {
            navigationBackStack.removeAt(navigationBackStack.lastIndex)
            _currentScreen.value = navigationBackStack.last()
            true
        } else if (_currentScreen.value != Screen.DASHBOARD) {
            _currentScreen.value = Screen.DASHBOARD
            navigationBackStack.clear()
            navigationBackStack.add(Screen.DASHBOARD)
            true
        } else {
            false
        }
    }

    fun selectComplaint(complaint: Complaint?) {
        _selectedComplaint.value = complaint
    }

    fun openAssignDialog(complaint: Complaint) {
        _assignDialogComplaint.value = complaint
    }

    fun closeAssignDialog() {
        _assignDialogComplaint.value = null
    }

    fun openResolveDialog(complaint: Complaint) {
        _resolveDialogComplaint.value = complaint
    }

    fun closeResolveDialog() {
        _resolveDialogComplaint.value = null
    }

    fun setRegisterDialogVisible(visible: Boolean) {
        _showRegisterDialog.value = visible
    }

    fun setAddStaffDialogVisible(visible: Boolean) {
        _showAddStaffDialog.value = visible
    }

    fun selectCustomer(customerId: String) {
        _selectedCustomerId.value = customerId
    }

    // ACTIONS
    fun registerComplaint(
        name: String,
        customerId: String,
        phone: String,
        email: String,
        title: String,
        description: String,
        category: ComplaintCategory,
        priority: Priority
    ) {
        val created = repository.registerComplaint(
            customerName = name,
            customerId = customerId,
            phone = phone,
            email = email,
            title = title,
            description = description,
            category = category,
            priority = priority
        )
        viewModelScope.launch {
            _toastEvent.emit("Complaint registered successfully. ID: ${created.id}")
        }
    }

    fun processNextComplaint() {
        val processed = repository.pollNextComplaint()
        viewModelScope.launch {
            if (processed != null) {
                _toastEvent.emit("Processed [${processed.id}]: PriorityQueue dispatched highest priority (${processed.priority.displayName}).")
                _selectedComplaint.value = repository.complaintMap[processed.id]
            } else {
                _toastEvent.emit("PriorityQueue is currently empty! All complaints resolved.")
            }
        }
    }

    fun assignStaff(complaintId: String, staffId: String) {
        val success = repository.assignComplaint(complaintId, staffId)
        if (success) {
            val staff = repository.staffMap[staffId]
            viewModelScope.launch {
                _toastEvent.emit("Complaint $complaintId assigned to ${staff?.name ?: "staff"}.")
            }
            closeAssignDialog()
            _selectedComplaint.value = repository.complaintMap[complaintId]
        }
    }

    fun autoAssignStaff(complaintId: String) {
        val staff = repository.autoAssignComplaint(complaintId)
        viewModelScope.launch {
            if (staff != null) {
                _toastEvent.emit("Auto-assigned to ${staff.name} (${staff.specialization.displayName} specialist).")
                _selectedComplaint.value = repository.complaintMap[complaintId]
            } else {
                _toastEvent.emit("No staff available for assignment.")
            }
        }
    }

    fun resolveComplaint(complaintId: String, remarks: String) {
        val success = repository.resolveComplaint(complaintId, remarks)
        if (success) {
            viewModelScope.launch {
                _toastEvent.emit("Complaint $complaintId marked as RESOLVED.")
            }
            closeResolveDialog()
            _selectedComplaint.value = repository.complaintMap[complaintId]
        }
    }

    fun updateStatus(complaintId: String, newStatus: ComplaintStatus) {
        val success = repository.updateStatus(complaintId, newStatus)
        if (success) {
            viewModelScope.launch {
                _toastEvent.emit("Status updated to ${newStatus.displayName}.")
            }
            _selectedComplaint.value = repository.complaintMap[complaintId]
        }
    }

    fun addSupportStaff(name: String, dept: String, specialization: ComplaintCategory) {
        val staff = repository.addSupportStaff(name, dept, specialization)
        viewModelScope.launch {
            _toastEvent.emit("Staff member ${staff.name} added successfully (ID: ${staff.id}).")
        }
        setAddStaffDialogVisible(false)
    }

    fun toggleStaffAvailability(staffId: String) {
        repository.toggleStaffAvailability(staffId)
    }

    fun testLookup(complaintId: String) {
        _diagnosticResult.value = repository.diagnoseLookup(complaintId.trim())
    }

    fun clearFilters() {
        searchQuery.value = ""
        filterPriority.value = null
        filterStatus.value = null
        filterCategory.value = null
        filterStaffId.value = null
    }

    fun getRepositoryRef(): ComplaintRepository = repository
}
