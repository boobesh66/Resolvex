package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Complaint
import com.example.ui.components.CategoryChip
import com.example.ui.components.PriorityBadge
import com.example.ui.components.StatCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.PriorityCritical
import com.example.ui.theme.PriorityCriticalBg
import com.example.ui.theme.ResolveXPrimary
import com.example.ui.theme.ResolveXPrimaryContainer
import com.example.ui.theme.ResolveXSecondary
import com.example.ui.theme.ResolveXSecondaryContainer
import com.example.ui.theme.StatusAssigned
import com.example.ui.theme.StatusInProgress
import com.example.ui.theme.StatusResolved
import com.example.viewmodel.ComplaintViewModel
import com.example.viewmodel.Screen

@Composable
fun DashboardScreen(
    viewModel: ComplaintViewModel,
    modifier: Modifier = Modifier
) {
    val metrics by viewModel.dashboardMetrics.collectAsState()
    val allComplaints by viewModel.allComplaints.collectAsState()
    val pendingQueue by viewModel.pendingQueue.collectAsState()

    val recentComplaints = allComplaints.takeLast(7).reversed()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("dashboard_screen")
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle & Header
            Column {
                Text(
                    text = "Dashboard Overview",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Smart Complaint Prioritization, Assignment & Resolution",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Quick-Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { viewModel.setRegisterDialogVisible(true) },
                    colors = ButtonDefaults.buttonColors(containerColor = ResolveXPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("dashboard_register_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Register Complaint", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Button(
                    onClick = { viewModel.processNextComplaint() },
                    colors = ButtonDefaults.buttonColors(containerColor = PriorityCritical),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("dashboard_process_next_btn")
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Process Next (Queue)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Live PriorityQueue Quick Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = ResolveXPrimaryContainer.copy(alpha = 0.6f)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.navigateTo(Screen.PRIORITY_QUEUE) }
                    .testTag("dashboard_pq_banner")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(14.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(ResolveXPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Queue",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "PriorityQueue Status • ${pendingQueue.size} Pending",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = ResolveXPrimary
                        )
                        if (pendingQueue.isNotEmpty()) {
                            val head = pendingQueue.first()
                            Text(
                                text = "Head: [${head.id}] ${head.title} (${head.priority.displayName})",
                                fontSize = 11.sp,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        } else {
                            Text(
                                text = "All priority tickets resolved! Queue is clear.",
                                fontSize = 11.sp,
                                color = StatusResolved
                            )
                        }
                    }

                    Text(
                        text = "View Queue →",
                        color = ResolveXPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6 Metric Cards
            Text(
                text = "Key Metrics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // Grid Row 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Total Complaints",
                    value = "${metrics.totalComplaints}",
                    icon = Icons.Default.Layers,
                    iconTint = ResolveXPrimary,
                    iconBgColor = ResolveXPrimaryContainer,
                    subtitle = "In ArrayList",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Open Complaints",
                    value = "${metrics.openComplaints}",
                    icon = Icons.Default.HourglassTop,
                    iconTint = StatusInProgress,
                    iconBgColor = StatusInProgress.copy(alpha = 0.15f),
                    subtitle = "In PriorityQueue",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Grid Row 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Critical Complaints",
                    value = "${metrics.criticalComplaints}",
                    icon = Icons.Default.ReportProblem,
                    iconTint = PriorityCritical,
                    iconBgColor = PriorityCriticalBg,
                    subtitle = "Highest Heap Priority",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "In Progress",
                    value = "${metrics.inProgressComplaints}",
                    icon = Icons.Default.AssignmentTurnedIn,
                    iconTint = StatusAssigned,
                    iconBgColor = StatusAssigned.copy(alpha = 0.15f),
                    subtitle = "Assigned to staff",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Grid Row 3
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Resolved",
                    value = "${metrics.resolvedComplaints}",
                    icon = Icons.Default.CheckCircle,
                    iconTint = StatusResolved,
                    iconBgColor = StatusResolved.copy(alpha = 0.15f),
                    subtitle = "${metrics.resolutionRatePercentage}% success rate",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Support Staff",
                    value = "${metrics.availableStaff} Active",
                    icon = Icons.Default.SupervisorAccount,
                    iconTint = ResolveXSecondary,
                    iconBgColor = ResolveXSecondaryContainer,
                    subtitle = "Ready in HashMap",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Recent Complaints Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Recent Complaints",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Complaint ID • Customer • Priority • Status",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                OutlinedButton(
                    onClick = { viewModel.navigateTo(Screen.ALL_COMPLAINTS) },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("dashboard_view_all_btn")
                ) {
                    Text("View All (${allComplaints.size})", fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        items(recentComplaints) { complaint ->
            ComplaintCard(
                complaint = complaint,
                onView = { viewModel.selectComplaint(complaint) },
                onAssign = { viewModel.openAssignDialog(complaint) },
                onResolve = { viewModel.openResolveDialog(complaint) },
                onAutoAssign = { viewModel.autoAssignStaff(complaint.id) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun ComplaintCard(
    complaint: Complaint,
    onView: () -> Unit,
    onAssign: () -> Unit,
    onResolve: () -> Unit,
    onAutoAssign: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                RoundedCornerShape(14.dp)
            )
            .clickable { onView() }
            .testTag("complaint_card_${complaint.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: ID, Badges, Date
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

            // Title & Description preview
            Text(
                text = complaint.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = complaint.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Customer & Assigned Staff metadata
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CategoryChip(category = complaint.category)

                Text(
                    text = "Customer: ${complaint.customerName}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (complaint.assignedStaffName != null) "Staff: ${complaint.assignedStaffName}" else "Staff: Unassigned",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (complaint.assignedStaffName != null) StatusAssigned else MaterialTheme.colorScheme.error
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

            // Action Buttons Row: View | Assign | Resolve
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(
                    onClick = onView,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).testTag("action_view_${complaint.id}")
                ) {
                    Text("View", fontSize = 11.sp)
                }

                if (complaint.status.isOpen) {
                    OutlinedButton(
                        onClick = onAssign,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1.2f).testTag("action_assign_${complaint.id}")
                    ) {
                        Text("Assign", fontSize = 11.sp)
                    }

                    Button(
                        onClick = onResolve,
                        colors = ButtonDefaults.buttonColors(containerColor = StatusResolved),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1.2f).testTag("action_resolve_${complaint.id}")
                    ) {
                        Text("Resolve", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
