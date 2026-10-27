package com.example.data

import com.example.model.Complaint
import com.example.model.ComplaintCategory
import com.example.model.ComplaintStatus
import com.example.model.Customer
import com.example.model.Priority
import com.example.model.SupportStaff
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.Locale
import java.util.PriorityQueue

/**
 * Data repository powered directly by real Java Data Structures:
 * 1. PriorityQueue<Complaint> - High-efficiency binary heap for priority dispatch
 * 2. ArrayList<Complaint>     - Dynamic array preserving sequential registration history
 * 3. HashMap<String, Complaint> - O(1) key-value hash table for rapid ID lookup
 * 4. HashMap<String, Customer>  - O(1) customer directory
 * 5. HashMap<String, SupportStaff> - O(1) staff directory
 */
class ComplaintRepository {

    // === JAVA DATA STRUCTURES INSTANCES ===
    val complaintQueue: PriorityQueue<Complaint> = PriorityQueue<Complaint>()
    val complaintHistory: ArrayList<Complaint> = ArrayList<Complaint>()
    val complaintMap: HashMap<String, Complaint> = HashMap<String, Complaint>()
    val customerMap: HashMap<String, Customer> = HashMap<String, Customer>()
    val staffMap: HashMap<String, SupportStaff> = HashMap<String, SupportStaff>()

    // === REACTIVE STATE FLOWS FOR JETPACK COMPOSE ===
    private val _allComplaintsFlow = MutableStateFlow<List<Complaint>>(emptyList())
    val allComplaintsFlow: StateFlow<List<Complaint>> = _allComplaintsFlow.asStateFlow()

    private val _pendingQueueFlow = MutableStateFlow<List<Complaint>>(emptyList())
    val pendingQueueFlow: StateFlow<List<Complaint>> = _pendingQueueFlow.asStateFlow()

    private val _staffListFlow = MutableStateFlow<List<SupportStaff>>(emptyList())
    val staffListFlow: StateFlow<List<SupportStaff>> = _staffListFlow.asStateFlow()

    private val _customersFlow = MutableStateFlow<List<Customer>>(emptyList())
    val customersFlow: StateFlow<List<Customer>> = _customersFlow.asStateFlow()

    private var nextComplaintNumber = 1001

    private val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())

    init {
        preloadData()
    }

    private fun preloadData() {
        // Preload Support Staff
        val initialStaff = listOf(
            SupportStaff("STF-201", "Alex Morgan", "Engineering", ComplaintCategory.TECHNICAL_ISSUE, isAvailable = true, assignedComplaints = 2, resolvedComplaints = 14),
            SupportStaff("STF-202", "Elena Rostova", "Finance & Billing", ComplaintCategory.PAYMENT_BILLING, isAvailable = true, assignedComplaints = 1, resolvedComplaints = 19),
            SupportStaff("STF-203", "David Chen", "Security & Accounts", ComplaintCategory.ACCOUNT_ISSUE, isAvailable = false, assignedComplaints = 3, resolvedComplaints = 11),
            SupportStaff("STF-204", "Priya Sharma", "Logistics & Supply", ComplaintCategory.DELIVERY_ISSUE, isAvailable = true, assignedComplaints = 1, resolvedComplaints = 16),
            SupportStaff("STF-205", "Marcus Wright", "Product Quality", ComplaintCategory.PRODUCT_ISSUE, isAvailable = true, assignedComplaints = 0, resolvedComplaints = 8),
            SupportStaff("STF-206", "Sophia Martinez", "Customer Success", ComplaintCategory.SERVICE_ISSUE, isAvailable = true, assignedComplaints = 1, resolvedComplaints = 22)
        )
        for (staff in initialStaff) {
            staffMap[staff.id] = staff
        }

        // Preload Customers
        val initialCustomers = listOf(
            Customer("CUST-101", "James Wilson", "+1 (555) 234-5678", "j.wilson@corp.com", 3),
            Customer("CUST-102", "Samantha Green", "+1 (555) 345-6789", "sam.green@tech.io", 2),
            Customer("CUST-103", "Carlos Mendes", "+1 (555) 456-7890", "cmendes@enterprise.org", 2),
            Customer("CUST-104", "Amina Al-Mansoor", "+1 (555) 567-8901", "amina.mansoor@global.net", 1),
            Customer("CUST-105", "Liam O'Connor", "+1 (555) 678-9012", "liam.oconnor@apex.com", 2),
            Customer("CUST-106", "Yuki Tanaka", "+1 (555) 789-0123", "ytanaka@cloudnet.jp", 1),
            Customer("CUST-107", "Rachel Adams", "+1 (555) 890-1234", "radams@innovate.co", 1)
        )
        for (c in initialCustomers) {
            customerMap[c.id] = c
        }

        val baseTime = System.currentTimeMillis() - 86400000L * 3 // 3 days ago

        val preloadedComplaints = listOf(
            Complaint(
                id = "C1001",
                customerId = "CUST-101",
                customerName = "James Wilson",
                customerPhone = "+1 (555) 234-5678",
                customerEmail = "j.wilson@corp.com",
                title = "Payment Deducted Twice for Subscription",
                description = "Account debited $240 twice during the annual renewal checkout on the portal.",
                category = ComplaintCategory.PAYMENT_BILLING,
                priority = Priority.HIGH,
                status = ComplaintStatus.RESOLVED,
                assignedStaffId = "STF-202",
                assignedStaffName = "Elena Rostova",
                registeredTimestamp = baseTime + 10000,
                registeredDateFormatted = dateFormat.format(Date(baseTime + 10000)),
                resolutionTimestamp = baseTime + 45000000,
                resolutionDateFormatted = dateFormat.format(Date(baseTime + 45000000)),
                resolutionRemarks = "Initiated automatic refund for duplicate gateway transaction. Customer confirmed receipt."
            ),
            Complaint(
                id = "C1002",
                customerId = "CUST-102",
                customerName = "Samantha Green",
                customerPhone = "+1 (555) 345-6789",
                customerEmail = "sam.green@tech.io",
                title = "Production Database API Outage",
                description = "REST API cluster throwing HTTP 503 Service Unavailable across all us-east instances.",
                category = ComplaintCategory.TECHNICAL_ISSUE,
                priority = Priority.CRITICAL,
                status = ComplaintStatus.IN_PROGRESS,
                assignedStaffId = "STF-201",
                assignedStaffName = "Alex Morgan",
                registeredTimestamp = baseTime + 80000,
                registeredDateFormatted = dateFormat.format(Date(baseTime + 80000))
            ),
            Complaint(
                id = "C1003",
                customerId = "CUST-103",
                customerName = "Carlos Mendes",
                customerPhone = "+1 (555) 456-7890",
                customerEmail = "cmendes@enterprise.org",
                title = "Critical Cargo Shipment Delay",
                description = "Shipment #TRK-8891 stranded at regional transit hub for over 48 hours without status change.",
                category = ComplaintCategory.DELIVERY_ISSUE,
                priority = Priority.HIGH,
                status = ComplaintStatus.ASSIGNED,
                assignedStaffId = "STF-204",
                assignedStaffName = "Priya Sharma",
                registeredTimestamp = baseTime + 120000,
                registeredDateFormatted = dateFormat.format(Date(baseTime + 120000))
            ),
            Complaint(
                id = "C1004",
                customerId = "CUST-104",
                customerName = "Amina Al-Mansoor",
                customerPhone = "+1 (555) 567-8901",
                customerEmail = "amina.mansoor@global.net",
                title = "Two-Factor Auth Lockout",
                description = "Hardware security key lost while traveling. Cannot authenticate to company console.",
                category = ComplaintCategory.ACCOUNT_ISSUE,
                priority = Priority.CRITICAL,
                status = ComplaintStatus.REGISTERED,
                assignedStaffId = null,
                assignedStaffName = null,
                registeredTimestamp = baseTime + 200000,
                registeredDateFormatted = dateFormat.format(Date(baseTime + 200000))
            ),
            Complaint(
                id = "C1005",
                customerId = "CUST-105",
                customerName = "Liam O'Connor",
                customerPhone = "+1 (555) 678-9012",
                customerEmail = "liam.oconnor@apex.com",
                title = "Damaged Enterprise Server Rack",
                description = "Side chassis panel dented during unloading. Fans vibrating excessively on initial test.",
                category = ComplaintCategory.PRODUCT_ISSUE,
                priority = Priority.MEDIUM,
                status = ComplaintStatus.REGISTERED,
                assignedStaffId = null,
                assignedStaffName = null,
                registeredTimestamp = baseTime + 300000,
                registeredDateFormatted = dateFormat.format(Date(baseTime + 300000))
            ),
            Complaint(
                id = "C1006",
                customerId = "CUST-106",
                customerName = "Yuki Tanaka",
                customerPhone = "+1 (555) 789-0123",
                customerEmail = "ytanaka@cloudnet.jp",
                title = "Query on Cloud SLA Penalties",
                description = "Requesting clarification on section 4.2 tier rebate calculations for uptime below 99.9%.",
                category = ComplaintCategory.GENERAL_ENQUIRY,
                priority = Priority.LOW,
                status = ComplaintStatus.REGISTERED,
                assignedStaffId = null,
                assignedStaffName = null,
                registeredTimestamp = baseTime + 400000,
                registeredDateFormatted = dateFormat.format(Date(baseTime + 400000))
            ),
            Complaint(
                id = "C1007",
                customerId = "CUST-101",
                customerName = "James Wilson",
                customerPhone = "+1 (555) 234-5678",
                customerEmail = "j.wilson@corp.com",
                title = "Support Agent Unresponsive During Outage",
                description = "Live chat agent abruptly disconnected without transfer or resolution.",
                category = ComplaintCategory.SERVICE_ISSUE,
                priority = Priority.MEDIUM,
                status = ComplaintStatus.IN_PROGRESS,
                assignedStaffId = "STF-206",
                assignedStaffName = "Sophia Martinez",
                registeredTimestamp = baseTime + 500000,
                registeredDateFormatted = dateFormat.format(Date(baseTime + 500000))
            ),
            Complaint(
                id = "C1008",
                customerId = "CUST-107",
                customerName = "Rachel Adams",
                customerPhone = "+1 (555) 890-1234",
                customerEmail = "radams@innovate.co",
                title = "Security Vulnerability in Webhook Endpoint",
                description = "Missing HMAC header verification allowing spoofed callbacks.",
                category = ComplaintCategory.TECHNICAL_ISSUE,
                priority = Priority.CRITICAL,
                status = ComplaintStatus.REGISTERED,
                assignedStaffId = null,
                assignedStaffName = null,
                registeredTimestamp = baseTime + 600000,
                registeredDateFormatted = dateFormat.format(Date(baseTime + 600000))
            ),
            Complaint(
                id = "C1009",
                customerId = "CUST-102",
                customerName = "Samantha Green",
                customerPhone = "+1 (555) 345-6789",
                customerEmail = "sam.green@tech.io",
                title = "VAT Invoice Missing Tax ID",
                description = "Automated tax PDF generated without the corporate VAT exemption number.",
                category = ComplaintCategory.PAYMENT_BILLING,
                priority = Priority.LOW,
                status = ComplaintStatus.CLOSED,
                assignedStaffId = "STF-202",
                assignedStaffName = "Elena Rostova",
                registeredTimestamp = baseTime + 700000,
                registeredDateFormatted = dateFormat.format(Date(baseTime + 700000)),
                resolutionTimestamp = baseTime + 90000000,
                resolutionDateFormatted = dateFormat.format(Date(baseTime + 90000000)),
                resolutionRemarks = "Re-issued corporate tax compliant invoice with VAT ID."
            ),
            Complaint(
                id = "C1010",
                customerId = "CUST-103",
                customerName = "Carlos Mendes",
                customerPhone = "+1 (555) 456-7890",
                customerEmail = "cmendes@enterprise.org",
                title = "SSO SAML Certificate Expiration",
                description = "Okta authentication failing due to expired IdP metadata signature.",
                category = ComplaintCategory.ACCOUNT_ISSUE,
                priority = Priority.HIGH,
                status = ComplaintStatus.IN_PROGRESS,
                assignedStaffId = "STF-203",
                assignedStaffName = "David Chen",
                registeredTimestamp = baseTime + 800000,
                registeredDateFormatted = dateFormat.format(Date(baseTime + 800000))
            ),
            Complaint(
                id = "C1011",
                customerId = "CUST-105",
                customerName = "Liam O'Connor",
                customerPhone = "+1 (555) 678-9012",
                customerEmail = "liam.oconnor@apex.com",
                title = "SDK Documentation Discrepancy",
                description = "Android SDK v3.2 method signatures mismatch public developer guide.",
                category = ComplaintCategory.TECHNICAL_ISSUE,
                priority = Priority.MEDIUM,
                status = ComplaintStatus.REGISTERED,
                assignedStaffId = null,
                assignedStaffName = null,
                registeredTimestamp = baseTime + 900000,
                registeredDateFormatted = dateFormat.format(Date(baseTime + 900000))
            )
        )

        for (complaint in preloadedComplaints) {
            // Add to ArrayList (Sequential History)
            complaintHistory.add(complaint)
            // Add to HashMap (O(1) lookup)
            complaintMap[complaint.id] = complaint
            // If complaint is Open/Unresolved, add to PriorityQueue
            if (complaint.status.isOpen) {
                complaintQueue.add(complaint)
            }
        }
        nextComplaintNumber = 1012
        syncStateFlows()
    }

    private fun syncStateFlows() {
        // Sync ArrayList to immutable flow
        _allComplaintsFlow.value = ArrayList(complaintHistory)

        // Sync PriorityQueue by polling into a sorted display list (heap copy)
        val copyQueue = PriorityQueue(complaintQueue)
        val sortedPending = ArrayList<Complaint>()
        while (!copyQueue.isEmpty()) {
            sortedPending.add(copyQueue.poll())
        }
        _pendingQueueFlow.value = sortedPending

        _staffListFlow.value = staffMap.values.toList()
        _customersFlow.value = customerMap.values.toList()
    }

    /**
     * Register a new complaint.
     * Inserts into PriorityQueue, ArrayList, and HashMap simultaneously!
     */
    fun registerComplaint(
        customerName: String,
        customerId: String,
        phone: String,
        email: String,
        title: String,
        description: String,
        category: ComplaintCategory,
        priority: Priority
    ): Complaint {
        val id = "C$nextComplaintNumber"
        nextComplaintNumber++

        val now = System.currentTimeMillis()
        val formattedDate = dateFormat.format(Date(now))

        val complaint = Complaint(
            id = id,
            customerId = customerId.ifBlank { "CUST-${(100..999).random()}" },
            customerName = customerName,
            customerPhone = phone,
            customerEmail = email,
            title = title,
            description = description,
            category = category,
            priority = priority,
            status = ComplaintStatus.REGISTERED,
            registeredTimestamp = now,
            registeredDateFormatted = formattedDate
        )

        // 1. HashMap Put: O(1)
        complaintMap[complaint.id] = complaint

        // 2. ArrayList Add: O(1) amortized
        complaintHistory.add(complaint)

        // 3. PriorityQueue Offer: O(log N)
        complaintQueue.add(complaint)

        // Update or insert Customer in customerMap
        val existingCustomer = customerMap[complaint.customerId]
        if (existingCustomer != null) {
            customerMap[complaint.customerId] = existingCustomer.copy(
                totalComplaints = existingCustomer.totalComplaints + 1
            )
        } else {
            customerMap[complaint.customerId] = Customer(
                id = complaint.customerId,
                name = customerName,
                phone = phone,
                email = email,
                totalComplaints = 1
            )
        }

        syncStateFlows()
        return complaint
    }

    /**
     * PriorityQueue Poll: Extracts the highest priority complaint from the heap.
     */
    fun pollNextComplaint(): Complaint? {
        if (complaintQueue.isEmpty()) return null
        val next = complaintQueue.poll() ?: return null

        // If complaint was REGISTERED, advance it to ASSIGNED or IN_PROGRESS
        val updated = if (next.status == ComplaintStatus.REGISTERED) {
            next.copy(status = ComplaintStatus.IN_PROGRESS)
        } else {
            next
        }

        // Update HashMap & ArrayList
        complaintMap[updated.id] = updated
        val index = complaintHistory.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            complaintHistory[index] = updated
        }

        // Put back in queue if still open so it can be managed until resolved
        if (updated.status.isOpen) {
            complaintQueue.add(updated)
        }

        syncStateFlows()
        return updated
    }

    /**
     * Assign complaint to a staff member.
     */
    fun assignComplaint(complaintId: String, staffId: String): Boolean {
        val complaint = complaintMap[complaintId] ?: return false
        val staff = staffMap[staffId] ?: return false

        val updated = complaint.copy(
            assignedStaffId = staff.id,
            assignedStaffName = staff.name,
            status = if (complaint.status == ComplaintStatus.REGISTERED) ComplaintStatus.ASSIGNED else complaint.status
        )

        // Update staff workload count
        staffMap[staff.id] = staff.copy(
            assignedComplaints = staff.assignedComplaints + 1
        )

        updateComplaintInCollections(updated)
        return true
    }

    /**
     * Automatic Assignment Algorithm:
     * Selects an available support staff member whose specialization best matches
     * the complaint category. If multiple or none available, chooses staff with lowest active load.
     */
    fun autoAssignComplaint(complaintId: String): SupportStaff? {
        val complaint = complaintMap[complaintId] ?: return null

        // 1. Exact specialization match & available
        val matchedStaff = staffMap.values.filter { it.isAvailable && it.specialization == complaint.category }
            .minByOrNull { it.assignedComplaints }

        // 2. Fallback to any available staff with lowest workload
        val assignedStaff = matchedStaff
            ?: staffMap.values.filter { it.isAvailable }
                .minByOrNull { it.assignedComplaints }
            ?: staffMap.values.minByOrNull { it.assignedComplaints }

        if (assignedStaff != null) {
            assignComplaint(complaintId, assignedStaff.id)
        }
        return assignedStaff
    }

    /**
     * Resolve Complaint:
     * Records remarks, resolution date, transitions to RESOLVED,
     * removes from pending PriorityQueue, retains in ArrayList history and HashMap.
     */
    fun resolveComplaint(complaintId: String, remarks: String): Boolean {
        val complaint = complaintMap[complaintId] ?: return false
        val now = System.currentTimeMillis()
        val formattedDate = dateFormat.format(Date(now))

        val updated = complaint.copy(
            status = ComplaintStatus.RESOLVED,
            resolutionTimestamp = now,
            resolutionDateFormatted = formattedDate,
            resolutionRemarks = remarks.ifBlank { "Issue investigated and resolved to customer satisfaction." }
        )

        // If staff was assigned, update staff resolved count & decrease active
        if (complaint.assignedStaffId != null) {
            val staff = staffMap[complaint.assignedStaffId]
            if (staff != null) {
                staffMap[staff.id] = staff.copy(
                    assignedComplaints = maxOf(0, staff.assignedComplaints - 1),
                    resolvedComplaints = staff.resolvedComplaints + 1
                )
            }
        }

        // Remove from pending PriorityQueue since it's resolved!
        complaintQueue.removeIf { it.id == complaintId }

        updateComplaintInCollections(updated)
        return true
    }

    /**
     * Update Complaint Status (e.g. In Progress, Closed)
     */
    fun updateStatus(complaintId: String, newStatus: ComplaintStatus): Boolean {
        val complaint = complaintMap[complaintId] ?: return false
        val updated = complaint.copy(status = newStatus)

        if (!newStatus.isOpen) {
            complaintQueue.removeIf { it.id == complaintId }
        } else {
            // Re-order in queue
            complaintQueue.removeIf { it.id == complaintId }
            complaintQueue.add(updated)
        }

        updateComplaintInCollections(updated)
        return true
    }

    private fun updateComplaintInCollections(updated: Complaint) {
        // Update HashMap
        complaintMap[updated.id] = updated

        // Update ArrayList
        val index = complaintHistory.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            complaintHistory[index] = updated
        }

        // If open, ensure updated version is in PriorityQueue
        if (updated.status.isOpen) {
            complaintQueue.removeIf { it.id == updated.id }
            complaintQueue.add(updated)
        } else {
            complaintQueue.removeIf { it.id == updated.id }
        }

        syncStateFlows()
    }

    /**
     * Add new Support Staff
     */
    fun addSupportStaff(name: String, department: String, specialization: ComplaintCategory): SupportStaff {
        val newId = "STF-${staffMap.size + 201}"
        val newStaff = SupportStaff(
            id = newId,
            name = name,
            department = department,
            specialization = specialization,
            isAvailable = true,
            assignedComplaints = 0,
            resolvedComplaints = 0
        )
        staffMap[newStaff.id] = newStaff
        syncStateFlows()
        return newStaff
    }

    /**
     * Toggle staff availability
     */
    fun toggleStaffAvailability(staffId: String): Boolean {
        val staff = staffMap[staffId] ?: return false
        staffMap[staffId] = staff.copy(isAvailable = !staff.isAvailable)
        syncStateFlows()
        return true
    }

    /**
     * Customer History Lookup using Customer ID
     * Returns Customer details + list of complaints from ArrayList
     */
    fun getCustomerHistory(customerId: String): Pair<Customer?, List<Complaint>> {
        val customer = customerMap[customerId]
        val customerComplaints = complaintHistory.filter { it.customerId.equals(customerId, ignoreCase = true) }
        return Pair(customer, customerComplaints)
    }

    /**
     * HashMap Lookup Benchmark and Diagnostic Info
     */
    data class LookupDiagnostic(
        val complaint: Complaint?,
        val key: String,
        val hashCode: Int,
        val bucketIndex: Int,
        val lookupDurationNanos: Long,
        val arrayListScanDurationNanos: Long
    )

    fun diagnoseLookup(complaintId: String): LookupDiagnostic {
        val startHash = System.nanoTime()
        val complaint = complaintMap[complaintId]
        val endHash = System.nanoTime()

        val startArray = System.nanoTime()
        val linearFound = complaintHistory.find { it.id.equals(complaintId, ignoreCase = true) }
        val endArray = System.nanoTime()

        val hash = complaintId.hashCode()
        val bucket = kotlin.math.abs(hash) % 16

        return LookupDiagnostic(
            complaint = complaint ?: linearFound,
            key = complaintId,
            hashCode = hash,
            bucketIndex = bucket,
            lookupDurationNanos = maxOf(12L, endHash - startHash),
            arrayListScanDurationNanos = maxOf(65L, endArray - startArray)
        )
    }
}
