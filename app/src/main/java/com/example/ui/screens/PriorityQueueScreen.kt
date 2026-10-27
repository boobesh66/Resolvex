package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Complaint
import com.example.model.ComplaintStatus
import com.example.model.Priority
import com.example.ui.components.CategoryChip
import com.example.ui.components.PriorityBadge
import com.example.ui.components.StatusBadge
import com.example.ui.theme.PriorityCritical
import com.example.ui.theme.ResolveXPrimary
import com.example.ui.theme.ResolveXPrimaryContainer
import com.example.ui.theme.StatusAssigned
import com.example.ui.theme.StatusResolved
import com.example.viewmodel.ComplaintViewModel

@Composable
fun PriorityQueueScreen(
    viewModel: ComplaintViewModel,
    modifier: Modifier = Modifier
) {
    val pendingQueue by viewModel.pendingQueue.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("priority_queue_screen")
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))

            // Page Header
            Column {
                Text(
                    text = "Priority Queue",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Live Binary Max-Heap Priority Dispatch System",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Mandatory Faculty Explanation Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = ResolveXPrimaryContainer),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("pq_explanation_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(ResolveXPrimary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Algorithm Info",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Data Structures Concept",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = ResolveXPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "PriorityQueue is used because the most important complaint must be processed first.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Ordering Rule: 1. Priority: CRITICAL (4) → HIGH (3) → MEDIUM (2) → LOW (1)\n" +
                               "2. Tie-breaker: If priorities are equal, oldest complaint (lowest timestamp) is dispatched first (FIFO fairness).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dispatch Action Card with "Process Next Complaint"
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Next In Queue (Head)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.Bold
                        )
                        if (pendingQueue.isNotEmpty()) {
                            val head = pendingQueue.first()
                            Text(
                                text = "${head.id} • ${head.priority.displayName}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = head.priority.color
                            )
                            Text(
                                text = head.title,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Text(
                                text = "Queue is empty",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = StatusResolved
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = { viewModel.processNextComplaint() },
                        enabled = pendingQueue.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = PriorityCritical),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("btn_process_next_complaint")
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Process Next",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Queue stats row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "PENDING QUEUE ORDER (${pendingQueue.size})",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "Comparator: Priority DESC, Time ASC",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        if (pendingQueue.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = StatusResolved,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("All Complaints Processed!", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(
                            "Register new complaints from the dashboard to repopulate the PriorityQueue.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            itemsIndexed(pendingQueue, key = { _, item -> item.id }) { index, complaint ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(300)),
                    exit = fadeOut(animationSpec = tween(300))
                ) {
                    QueueItemCard(
                        rank = index + 1,
                        complaint = complaint,
                        isHead = index == 0,
                        onView = { viewModel.selectComplaint(complaint) },
                        onAssign = { viewModel.openAssignDialog(complaint) },
                        onResolve = { viewModel.openResolveDialog(complaint) },
                        onAutoAssign = { viewModel.autoAssignStaff(complaint.id) }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun QueueItemCard(
    rank: Int,
    complaint: Complaint,
    isHead: Boolean,
    onView: () -> Unit,
    onAssign: () -> Unit,
    onResolve: () -> Unit,
    onAutoAssign: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isHead) PriorityCritical else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
        label = "border"
    )

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isHead) MaterialTheme.colorScheme.surface
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isHead) 3.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(if (isHead) 2.dp else 1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable { onView() }
            .testTag("queue_item_${complaint.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Queue Rank Badge
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                if (isHead) PriorityCritical else MaterialTheme.colorScheme.surfaceVariant
                            )
                    ) {
                        Text(
                            text = "$rank",
                            color = if (isHead) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = complaint.id,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = ResolveXPrimary
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    PriorityBadge(priority = complaint.priority)

                    if (isHead) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "POLL NEXT",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PriorityCritical,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(PriorityCritical.copy(alpha = 0.15f))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }

                StatusBadge(status = complaint.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title & Description
            Text(
                text = "${complaint.title}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = complaint.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Category & Customer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CategoryChip(category = complaint.category)
                Text(
                    text = "Customer: ${complaint.customerName}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Staff & Queue timestamp metadata
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (complaint.assignedStaffName != null) "Assigned: ${complaint.assignedStaffName}" else "Unassigned",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (complaint.assignedStaffName != null) StatusAssigned else MaterialTheme.colorScheme.error
                )

                Text(
                    text = "Reg: ${complaint.registeredDateFormatted}",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(
                    onClick = onView,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = onAssign,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1.2f)
                ) {
                    Text("Assign", fontSize = 11.sp)
                }

                Button(
                    onClick = onResolve,
                    colors = ButtonDefaults.buttonColors(containerColor = StatusResolved),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1.2f)
                ) {
                    Text("Resolve", fontSize = 11.sp)
                }
            }
        }
    }
}
