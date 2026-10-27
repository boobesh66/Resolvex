package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LowPriority
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AddStaffDialog
import com.example.ui.components.AssignStaffDialog
import com.example.ui.components.ComplaintDetailModal
import com.example.ui.components.RegisterComplaintDialog
import com.example.ui.components.ResolveComplaintDialog
import com.example.ui.screens.AllComplaintsScreen
import com.example.ui.screens.CustomerHistoryScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DataStructureVisualizerScreen
import com.example.ui.screens.PriorityQueueScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.SupportStaffScreen
import com.example.ui.theme.PriorityCritical
import com.example.ui.theme.ResolveXPrimary
import com.example.ui.theme.ResolveXPrimaryContainer
import com.example.viewmodel.ComplaintViewModel
import com.example.viewmodel.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResolveXApp(
    viewModel: ComplaintViewModel = remember { ComplaintViewModel() }
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val currentScreen by viewModel.currentScreen.collectAsState()
    val pendingQueue by viewModel.pendingQueue.collectAsState()
    val allComplaints by viewModel.allComplaints.collectAsState()
    val staffList by viewModel.staffList.collectAsState()

    // Modals
    val selectedComplaint by viewModel.selectedComplaint.collectAsState()
    val assignDialogComplaint by viewModel.assignDialogComplaint.collectAsState()
    val resolveDialogComplaint by viewModel.resolveDialogComplaint.collectAsState()
    val showRegisterDialog by viewModel.showRegisterDialog.collectAsState()
    val showAddStaffDialog by viewModel.showAddStaffDialog.collectAsState()

    // Listen to toasts
    LaunchedEffect(Unit) {
        viewModel.toastEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Hardware/Gesture Back Handler
    BackHandler(enabled = drawerState.isOpen || currentScreen != Screen.DASHBOARD || selectedComplaint != null) {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        } else if (selectedComplaint != null) {
            viewModel.selectComplaint(null)
        } else {
            viewModel.handleBack()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
                    .testTag("app_navigation_drawer")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    // Header Branding
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 8.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(ResolveXPrimary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "ResolveX",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "ResolveX",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = ResolveXPrimary
                            )
                            Text(
                                text = "Complaint Management",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Text(
                        text = "Smart Prioritization, Assignment & Resolution",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                    // Navigation Items
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                        label = { Text("Dashboard") },
                        selected = currentScreen == Screen.DASHBOARD,
                        onClick = {
                            viewModel.navigateTo(Screen.DASHBOARD)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        label = { Text("Register Complaint") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            viewModel.setRegisterDialogVisible(true)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.ListAlt, contentDescription = null) },
                        label = { Text("All Complaints") },
                        badge = {
                            Text(
                                text = "${allComplaints.size}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.outline
                            )
                        },
                        selected = currentScreen == Screen.ALL_COMPLAINTS,
                        onClick = {
                            viewModel.navigateTo(Screen.ALL_COMPLAINTS)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.LowPriority, contentDescription = null) },
                        label = { Text("Priority Queue") },
                        badge = {
                            if (pendingQueue.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(PriorityCritical)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${pendingQueue.size}",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        },
                        selected = currentScreen == Screen.PRIORITY_QUEUE,
                        onClick = {
                            viewModel.navigateTo(Screen.PRIORITY_QUEUE)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Groups, contentDescription = null) },
                        label = { Text("Support Staff") },
                        badge = {
                            Text(
                                text = "${staffList.size}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.outline
                            )
                        },
                        selected = currentScreen == Screen.SUPPORT_STAFF,
                        onClick = {
                            viewModel.navigateTo(Screen.SUPPORT_STAFF)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.History, contentDescription = null) },
                        label = { Text("Customer History") },
                        selected = currentScreen == Screen.CUSTOMER_HISTORY,
                        onClick = {
                            viewModel.navigateTo(Screen.CUSTOMER_HISTORY)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.AccountTree, contentDescription = null) },
                        label = { Text("Data Structures") },
                        selected = currentScreen == Screen.DATA_STRUCTURES,
                        onClick = {
                            viewModel.navigateTo(Screen.DATA_STRUCTURES)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                        label = { Text("Reports") },
                        selected = currentScreen == Screen.REPORTS,
                        onClick = {
                            viewModel.navigateTo(Screen.REPORTS)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Footer Project Badge
                    Card(
                        colors = CardDefaults.cardColors(containerColor = ResolveXPrimaryContainer.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Data Structures Project",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = ResolveXPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Demonstrating PriorityQueue, ArrayList & HashMap in Java/Android.",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "ResolveX",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 17.sp,
                                    color = ResolveXPrimary
                                )
                                Text(
                                    text = " • ${currentScreen.title}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier.testTag("app_bar_menu_button")
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        // Quick Priority Queue status icon
                        BadgedBox(
                            badge = {
                                if (pendingQueue.isNotEmpty()) {
                                    Badge(containerColor = PriorityCritical) {
                                        Text("${pendingQueue.size}")
                                    }
                                }
                            },
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            IconButton(
                                onClick = { viewModel.navigateTo(Screen.PRIORITY_QUEUE) },
                                modifier = Modifier.testTag("app_bar_queue_icon")
                            ) {
                                Icon(Icons.Default.Bolt, contentDescription = "Queue", tint = PriorityCritical)
                            }
                        }

                        // Quick Register action
                        IconButton(
                            onClick = { viewModel.setRegisterDialogVisible(true) },
                            modifier = Modifier.testTag("app_bar_register_action")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Register", tint = ResolveXPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentScreen) {
                    Screen.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                    Screen.REGISTER -> {
                        // In case navigated via screen enum, render register view or launch dialog
                        LaunchedEffect(Unit) {
                            viewModel.setRegisterDialogVisible(true)
                            viewModel.navigateTo(Screen.DASHBOARD)
                        }
                        DashboardScreen(viewModel = viewModel)
                    }
                    Screen.ALL_COMPLAINTS -> AllComplaintsScreen(viewModel = viewModel)
                    Screen.PRIORITY_QUEUE -> PriorityQueueScreen(viewModel = viewModel)
                    Screen.SUPPORT_STAFF -> SupportStaffScreen(viewModel = viewModel)
                    Screen.CUSTOMER_HISTORY -> CustomerHistoryScreen(viewModel = viewModel)
                    Screen.DATA_STRUCTURES -> DataStructureVisualizerScreen(viewModel = viewModel)
                    Screen.REPORTS -> ReportsScreen(viewModel = viewModel)
                }
            }
        }
    }

    // --- MODAL DIALOGS ---

    if (showRegisterDialog) {
        RegisterComplaintDialog(
            onDismiss = { viewModel.setRegisterDialogVisible(false) },
            onSubmit = { name, custId, phone, email, title, desc, cat, priority ->
                viewModel.registerComplaint(name, custId, phone, email, title, desc, cat, priority)
            }
        )
    }

    if (showAddStaffDialog) {
        AddStaffDialog(
            onDismiss = { viewModel.setAddStaffDialogVisible(false) },
            onAdd = { name, dept, category ->
                viewModel.addSupportStaff(name, dept, category)
            }
        )
    }

    if (assignDialogComplaint != null) {
        AssignStaffDialog(
            complaint = assignDialogComplaint!!,
            staffList = staffList,
            onDismiss = { viewModel.closeAssignDialog() },
            onAssign = { staffId ->
                viewModel.assignStaff(assignDialogComplaint!!.id, staffId)
            },
            onAutoAssign = {
                viewModel.autoAssignStaff(assignDialogComplaint!!.id)
            }
        )
    }

    if (resolveDialogComplaint != null) {
        ResolveComplaintDialog(
            complaint = resolveDialogComplaint!!,
            onDismiss = { viewModel.closeResolveDialog() },
            onConfirm = { remarks ->
                viewModel.resolveComplaint(resolveDialogComplaint!!.id, remarks)
            }
        )
    }

    if (selectedComplaint != null) {
        ComplaintDetailModal(
            complaint = selectedComplaint!!,
            onDismiss = { viewModel.selectComplaint(null) },
            onAssignClicked = {
                val current = selectedComplaint!!
                viewModel.selectComplaint(null)
                viewModel.openAssignDialog(current)
            },
            onResolveClicked = {
                val current = selectedComplaint!!
                viewModel.selectComplaint(null)
                viewModel.openResolveDialog(current)
            },
            onUpdateStatus = { newStatus ->
                viewModel.updateStatus(selectedComplaint!!.id, newStatus)
            }
        )
    }
}
