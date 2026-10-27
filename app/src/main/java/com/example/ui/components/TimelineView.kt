package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ComplaintStatus
import com.example.ui.theme.ResolveXPrimary
import com.example.ui.theme.StatusResolved

@Composable
fun ComplaintTimelineView(
    currentStatus: ComplaintStatus,
    modifier: Modifier = Modifier
) {
    val steps = listOf(
        ComplaintStatus.REGISTERED,
        ComplaintStatus.ASSIGNED,
        ComplaintStatus.IN_PROGRESS,
        ComplaintStatus.RESOLVED,
        ComplaintStatus.CLOSED
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag("complaint_timeline_view")
    ) {
        Text(
            text = "LIFECYCLE TIMELINE",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            steps.forEachIndexed { index, status ->
                val isCompleted = status.stepOrder < currentStatus.stepOrder
                val isCurrent = status == currentStatus
                val isUpcoming = status.stepOrder > currentStatus.stepOrder

                val nodeColor = when {
                    isCompleted -> StatusResolved
                    isCurrent -> ResolveXPrimary
                    else -> MaterialTheme.colorScheme.outlineVariant
                }

                // Step Node
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                if (isCurrent || isCompleted) nodeColor else Color.Transparent
                            )
                            .border(
                                width = if (isUpcoming) 2.dp else 0.dp,
                                color = if (isUpcoming) MaterialTheme.colorScheme.outlineVariant else Color.Transparent,
                                shape = CircleShape
                            )
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Completed",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Text(
                                text = "${index + 1}",
                                color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = status.displayName,
                        fontSize = 10.sp,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                        color = when {
                            isCurrent -> ResolveXPrimary
                            isCompleted -> MaterialTheme.colorScheme.onSurface
                            else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        },
                        maxLines = 1
                    )
                }

                // Connecting Line between nodes
                if (index < steps.lastIndex) {
                    Box(
                        modifier = Modifier
                            .height(3.dp)
                            .width(14.dp)
                            .background(
                                if (status.stepOrder < currentStatus.stepOrder) StatusResolved
                                else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                    )
                }
            }
        }
    }
}
