package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.model.ComplaintCategory
import com.example.model.ComplaintStatus
import com.example.model.Priority
import com.example.ui.components.StatCard
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

@Composable
fun ReportsScreen(
    viewModel: ComplaintViewModel,
    modifier: Modifier = Modifier
) {
    val allComplaints by viewModel.allComplaints.collectAsState()
    val staffList by viewModel.staffList.collectAsState()
    val metrics by viewModel.dashboardMetrics.collectAsState()

    val total = allComplaints.size

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("reports_screen")
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))

            Column {
                Text(
                    text = "Reports & Analytics",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Operational performance, resolution rates, and workload distribution",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4 Key KPI Metrics Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Total Complaints",
                    value = "$total",
                    icon = Icons.Default.BarChart,
                    iconTint = ResolveXPrimary,
                    iconBgColor = ResolveXPrimaryContainer,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Resolution Rate",
                    value = "${metrics.resolutionRatePercentage}%",
                    icon = Icons.Default.CheckCircle,
                    iconTint = StatusResolved,
                    iconBgColor = StatusResolved.copy(alpha = 0.15f),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Critical Tickets",
                    value = "${metrics.criticalComplaints}",
                    icon = Icons.Default.ReportProblem,
                    iconTint = PriorityCritical,
                    iconBgColor = PriorityCriticalBg,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Avg Resolution Time",
                    value = metrics.avgResolutionTimeHours,
                    icon = Icons.Default.Speed,
                    iconTint = ResolveXSecondary,
                    iconBgColor = ResolveXSecondaryContainer,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Chart 1: Resolved vs Pending Complaints
            ReportCard(
                title = "Resolved vs Pending Complaints",
                icon = Icons.Default.PieChart
            ) {
                val resolved = allComplaints.count { it.status == ComplaintStatus.RESOLVED || it.status == ComplaintStatus.CLOSED }
                val pending = total - resolved
                val resolvedFraction = if (total > 0) resolved.toFloat() / total else 0f
                val pendingFraction = if (total > 0) pending.toFloat() / total else 0f

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Resolved: $resolved (${(resolvedFraction * 100).toInt()}%)", color = StatusResolved, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("Pending: $pending (${(pendingFraction * 100).toInt()}%)", color = PriorityCritical, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    if (resolved > 0) {
                        Box(
                            modifier = Modifier
                                .weight(resolved.toFloat())
                                .fillMaxSize()
                                .background(StatusResolved)
                        )
                    }
                    if (pending > 0) {
                        Box(
                            modifier = Modifier
                                .weight(pending.toFloat())
                                .fillMaxSize()
                                .background(PriorityCritical)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Chart 2: Complaints by Priority
            ReportCard(
                title = "Complaints by Priority (Severity)",
                icon = Icons.Default.ReportProblem
            ) {
                Priority.entries.forEach { priority ->
                    val count = allComplaints.count { it.priority == priority }
                    val fraction = if (total > 0) count.toFloat() / total else 0f

                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${priority.emoji} ${priority.displayName}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "$count (${(fraction * 100).toInt()}%)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = priority.color
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { fraction },
                            color = priority.color,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Chart 3: Complaints by Status
            ReportCard(
                title = "Complaints by Lifecycle Status",
                icon = Icons.Default.BarChart
            ) {
                ComplaintStatus.entries.forEach { status ->
                    val count = allComplaints.count { it.status == status }
                    val fraction = if (total > 0) count.toFloat() / total else 0f

                    val color = when (status) {
                        ComplaintStatus.REGISTERED -> ResolveXPrimary
                        ComplaintStatus.ASSIGNED -> StatusAssigned
                        ComplaintStatus.IN_PROGRESS -> StatusInProgress
                        ComplaintStatus.RESOLVED -> StatusResolved
                        ComplaintStatus.CLOSED -> MaterialTheme.colorScheme.outline
                    }

                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(status.displayName, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Text("$count", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { fraction },
                            color = color,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Chart 4: Complaints by Category
            ReportCard(
                title = "Complaints by Department Category",
                icon = Icons.Default.BarChart
            ) {
                ComplaintCategory.entries.forEach { cat ->
                    val count = allComplaints.count { it.category == cat }
                    val fraction = if (total > 0) count.toFloat() / total else 0f

                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(cat.displayName, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Text("$count", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { fraction },
                            color = ResolveXPrimary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(4.dp))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Chart 5: Support Staff Workload
            ReportCard(
                title = "Support Staff Workload & Productivity",
                icon = Icons.Default.BarChart
            ) {
                val maxWorkload = maxOf(1, staffList.maxOfOrNull { it.assignedComplaints + it.resolvedComplaints } ?: 1)

                staffList.forEach { staff ->
                    val active = staff.assignedComplaints
                    val resolved = staff.resolvedComplaints
                    val totalHandled = active + resolved
                    val fraction = totalHandled.toFloat() / maxWorkload

                    Column(modifier = Modifier.padding(vertical = 5.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${staff.name} (${staff.department})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Active: $active • Resolved: $resolved",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { fraction },
                            color = if (staff.isAvailable) ResolveXSecondary else MaterialTheme.colorScheme.outline,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun ReportCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = ResolveXPrimary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}
