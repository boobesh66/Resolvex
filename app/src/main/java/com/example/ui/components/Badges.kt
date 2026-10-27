package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.model.ComplaintCategory
import com.example.model.ComplaintStatus
import com.example.model.Priority
import com.example.ui.theme.StatusAssigned
import com.example.ui.theme.StatusAssignedBg
import com.example.ui.theme.StatusClosed
import com.example.ui.theme.StatusClosedBg
import com.example.ui.theme.StatusInProgress
import com.example.ui.theme.StatusInProgressBg
import com.example.ui.theme.StatusRegistered
import com.example.ui.theme.StatusRegisteredBg
import com.example.ui.theme.StatusResolved
import com.example.ui.theme.StatusResolvedBg

@Composable
fun PriorityBadge(
    priority: Priority,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(priority.bgColor)
            .border(1.dp, priority.color.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag("priority_badge_${priority.name.lowercase()}")
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(priority.color)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = priority.displayName.uppercase(),
            color = priority.color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun StatusBadge(
    status: ComplaintStatus,
    modifier: Modifier = Modifier
) {
    val (color, bgColor) = when (status) {
        ComplaintStatus.REGISTERED -> StatusRegistered to StatusRegisteredBg
        ComplaintStatus.ASSIGNED -> StatusAssigned to StatusAssignedBg
        ComplaintStatus.IN_PROGRESS -> StatusInProgress to StatusInProgressBg
        ComplaintStatus.RESOLVED -> StatusResolved to StatusResolvedBg
        ComplaintStatus.CLOSED -> StatusClosed to StatusClosedBg
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag("status_badge_${status.name.lowercase()}")
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = status.displayName,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun CategoryChip(
    category: ComplaintCategory,
    modifier: Modifier = Modifier
) {
    Text(
        text = category.displayName,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 7.dp, vertical = 3.dp)
    )
}
