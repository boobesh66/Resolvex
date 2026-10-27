package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Complaint
import com.example.model.ComplaintCategory
import com.example.model.ComplaintStatus
import com.example.model.Priority
import com.example.ui.components.CategoryChip
import com.example.ui.components.PriorityBadge
import com.example.ui.components.StatusBadge
import com.example.ui.theme.ResolveXPrimary
import com.example.ui.theme.StatusAssigned
import com.example.ui.theme.StatusInProgress
import com.example.ui.theme.StatusResolved
import com.example.viewmodel.ComplaintViewModel

@Composable
fun AllComplaintsScreen(
    viewModel: ComplaintViewModel,
    modifier: Modifier = Modifier
) {
    val complaints by viewModel.filteredComplaints.collectAsState()
    val allCount by viewModel.allComplaints.collectAsState()
    val staffList by viewModel.staffList.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedPriority by viewModel.filterPriority.collectAsState()
    val selectedStatus by viewModel.filterStatus.collectAsState()
    val selectedCategory by viewModel.filterCategory.collectAsState()
    val selectedStaffId by viewModel.filterStaffId.collectAsState()

    var showFiltersExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("all_complaints_screen")
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))

            // Header & Register Action
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "All Complaints",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Complete database (${complaints.size} of ${allCount.size} records)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = { viewModel.setRegisterDialogVisible(true) },
                    colors = ButtonDefaults.buttonColors(containerColor = ResolveXPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("all_complaints_register_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Register", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.searchQuery.value = it },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                placeholder = { Text("Search by Complaint ID (e.g. C1001) or Customer Name...") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_complaints_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Toggle Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { showFiltersExpanded = !showFiltersExpanded }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filters",
                        tint = ResolveXPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (showFiltersExpanded) "Hide Filters ▲" else "Show Filters ▼",
                        color = ResolveXPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                val hasActiveFilters = selectedPriority != null || selectedStatus != null ||
                        selectedCategory != null || selectedStaffId != null || searchQuery.isNotBlank()

                if (hasActiveFilters) {
                    Text(
                        text = "Reset Filters",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable { viewModel.clearFilters() }
                            .padding(4.dp)
                    )
                }
            }

            // Expanded Filter Section
            if (showFiltersExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // Priority Filters
                        Text("Priority:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.outline)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FilterChip(
                                selected = selectedPriority == null,
                                onClick = { viewModel.filterPriority.value = null },
                                label = { Text("All") }
                            )
                            Priority.entries.forEach { p ->
                                FilterChip(
                                    selected = selectedPriority == p,
                                    onClick = {
                                        viewModel.filterPriority.value = if (selectedPriority == p) null else p
                                    },
                                    label = { Text("${p.emoji} ${p.displayName}") }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Status Filters
                        Text("Status:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.outline)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FilterChip(
                                selected = selectedStatus == null,
                                onClick = { viewModel.filterStatus.value = null },
                                label = { Text("All") }
                            )
                            ComplaintStatus.entries.forEach { s ->
                                FilterChip(
                                    selected = selectedStatus == s,
                                    onClick = {
                                        viewModel.filterStatus.value = if (selectedStatus == s) null else s
                                    },
                                    label = { Text(s.displayName) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Category Filters
                        Text("Category:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.outline)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FilterChip(
                                selected = selectedCategory == null,
                                onClick = { viewModel.filterCategory.value = null },
                                label = { Text("All Categories") }
                            )
                            ComplaintCategory.entries.forEach { cat ->
                                FilterChip(
                                    selected = selectedCategory == cat,
                                    onClick = {
                                        viewModel.filterCategory.value = if (selectedCategory == cat) null else cat
                                    },
                                    label = { Text(cat.displayName) }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        if (complaints.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(24.dp)
                    ) {
                        Text("No matching complaints found", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("Try resetting your filters or search keywords.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        } else {
            items(complaints, key = { it.id }) { complaint ->
                ComplaintManagementCard(
                    complaint = complaint,
                    onView = { viewModel.selectComplaint(complaint) },
                    onAssign = { viewModel.openAssignDialog(complaint) },
                    onUpdateStatus = {
                        val nextStatus = when (complaint.status) {
                            ComplaintStatus.REGISTERED -> ComplaintStatus.ASSIGNED
                            ComplaintStatus.ASSIGNED -> ComplaintStatus.IN_PROGRESS
                            ComplaintStatus.IN_PROGRESS -> ComplaintStatus.RESOLVED
                            ComplaintStatus.RESOLVED -> ComplaintStatus.CLOSED
                            ComplaintStatus.CLOSED -> ComplaintStatus.REGISTERED
                        }
                        if (nextStatus == ComplaintStatus.RESOLVED) {
                            viewModel.openResolveDialog(complaint)
                        } else {
                            viewModel.updateStatus(complaint.id, nextStatus)
                        }
                    },
                    onResolve = { viewModel.openResolveDialog(complaint) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun ComplaintManagementCard(
    complaint: Complaint,
    onView: () -> Unit,
    onAssign: () -> Unit,
    onUpdateStatus: () -> Unit,
    onResolve: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
            .clickable { onView() }
            .testTag("manage_complaint_${complaint.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // ID + Badges
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = complaint.id,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = ResolveXPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    PriorityBadge(priority = complaint.priority)
                    Spacer(modifier = Modifier.width(6.dp))
                    StatusBadge(status = complaint.status)
                }

                Text(
                    text = complaint.registeredDateFormatted.substringBefore(","),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = complaint.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = complaint.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CategoryChip(category = complaint.category)
                Text(
                    text = "Customer: ${complaint.customerName} (${complaint.customerId})",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (complaint.assignedStaffName != null) "Assigned: ${complaint.assignedStaffName}" else "Unassigned",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (complaint.assignedStaffName != null) StatusAssigned else MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 4 Required Buttons: View | Assign | Update | Resolve
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(
                    onClick = onView,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).testTag("btn_view_${complaint.id}")
                ) {
                    Text("View", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = onAssign,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).testTag("btn_assign_${complaint.id}")
                ) {
                    Text("Assign", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = onUpdateStatus,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1.1f).testTag("btn_update_${complaint.id}")
                ) {
                    Text("Update", fontSize = 11.sp)
                }

                Button(
                    onClick = onResolve,
                    enabled = complaint.status.isOpen,
                    colors = ButtonDefaults.buttonColors(containerColor = StatusResolved),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1.1f).testTag("btn_resolve_${complaint.id}")
                ) {
                    Text("Resolve", fontSize = 11.sp)
                }
            }
        }
    }
}
