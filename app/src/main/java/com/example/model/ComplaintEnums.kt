package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.PriorityCritical
import com.example.ui.theme.PriorityCriticalBg
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.PriorityHighBg
import com.example.ui.theme.PriorityLow
import com.example.ui.theme.PriorityLowBg
import com.example.ui.theme.PriorityMedium
import com.example.ui.theme.PriorityMediumBg

enum class Priority(
    val level: Int,
    val displayName: String,
    val emoji: String,
    val color: Color,
    val bgColor: Color
) {
    CRITICAL(4, "Critical", "🔴", PriorityCritical, PriorityCriticalBg),
    HIGH(3, "High", "🟠", PriorityHigh, PriorityHighBg),
    MEDIUM(2, "Medium", "🟡", PriorityMedium, PriorityMediumBg),
    LOW(1, "Low", "🟢", PriorityLow, PriorityLowBg);

    companion object {
        fun fromLevel(level: Int): Priority = entries.firstOrNull { it.level == level } ?: MEDIUM
    }
}

enum class ComplaintCategory(val displayName: String) {
    TECHNICAL_ISSUE("Technical Issue"),
    PAYMENT_BILLING("Payment/Billing"),
    ACCOUNT_ISSUE("Account Issue"),
    PRODUCT_ISSUE("Product Issue"),
    DELIVERY_ISSUE("Delivery Issue"),
    SERVICE_ISSUE("Service Issue"),
    GENERAL_ENQUIRY("General Enquiry");

    companion object {
        fun fromDisplayName(name: String): ComplaintCategory =
            entries.firstOrNull { it.displayName.equals(name, ignoreCase = true) } ?: GENERAL_ENQUIRY
    }
}

enum class ComplaintStatus(val displayName: String, val stepOrder: Int) {
    REGISTERED("Registered", 1),
    ASSIGNED("Assigned", 2),
    IN_PROGRESS("In Progress", 3),
    RESOLVED("Resolved", 4),
    CLOSED("Closed", 5);

    val isOpen: Boolean get() = this == REGISTERED || this == ASSIGNED || this == IN_PROGRESS
}
