package com.example.model

data class Customer(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val totalComplaints: Int = 0
)

data class SupportStaff(
    val id: String,
    val name: String,
    val department: String,
    val specialization: ComplaintCategory,
    val isAvailable: Boolean = true,
    val assignedComplaints: Int = 0,
    val resolvedComplaints: Int = 0
)

data class Complaint(
    val id: String,
    val customerId: String,
    val customerName: String,
    val customerPhone: String,
    val customerEmail: String,
    val title: String,
    val description: String,
    val category: ComplaintCategory,
    val priority: Priority,
    val status: ComplaintStatus = ComplaintStatus.REGISTERED,
    val assignedStaffId: String? = null,
    val assignedStaffName: String? = null,
    val registeredTimestamp: Long = System.currentTimeMillis(),
    val registeredDateFormatted: String,
    val resolutionTimestamp: Long? = null,
    val resolutionDateFormatted: String? = null,
    val resolutionRemarks: String? = null
) : Comparable<Complaint> {

    /**
     * PriorityQueue Comparator requirement:
     * 1. Compare priority first (CRITICAL > HIGH > MEDIUM > LOW).
     * 2. If priority is equal, compare registration timestamp (oldest first).
     */
    override fun compareTo(other: Complaint): Int {
        val priorityComparison = other.priority.level.compareTo(this.priority.level)
        return if (priorityComparison != 0) {
            priorityComparison
        } else {
            this.registeredTimestamp.compareTo(other.registeredTimestamp)
        }
    }
}
