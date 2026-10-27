package com.example

import com.example.data.ComplaintRepository
import com.example.model.ComplaintCategory
import com.example.model.ComplaintStatus
import com.example.model.Priority
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ComplaintDataStructuresTest {

    private lateinit var repository: ComplaintRepository

    @Before
    fun setUp() {
        repository = ComplaintRepository()
    }

    @Test
    fun testPriorityQueueOrdering() {
        // Register a LOW priority complaint first
        val low = repository.registerComplaint(
            customerName = "Test Low",
            customerId = "CUST-991",
            phone = "111",
            email = "low@test.com",
            title = "Minor UI Typo",
            description = "Spelling mistake",
            category = ComplaintCategory.GENERAL_ENQUIRY,
            priority = Priority.LOW
        )

        // Register a CRITICAL priority complaint second
        val critical = repository.registerComplaint(
            customerName = "Test Critical",
            customerId = "CUST-992",
            phone = "222",
            email = "crit@test.com",
            title = "Database Server Crash",
            description = "Outage on prod",
            category = ComplaintCategory.TECHNICAL_ISSUE,
            priority = Priority.CRITICAL
        )

        // PriorityQueue must return highest priority complaint first
        val polled = repository.pollNextComplaint()
        assertNotNull(polled)
        assertEquals(Priority.CRITICAL, polled?.priority)
    }

    @Test
    fun testArrayListHistorySequentialRecords() {
        val initialSize = repository.complaintHistory.size
        val newComplaint = repository.registerComplaint(
            customerName = "Alice",
            customerId = "CUST-801",
            phone = "555-1234",
            email = "alice@example.com",
            title = "Network Loss",
            description = "Disconnected",
            category = ComplaintCategory.TECHNICAL_ISSUE,
            priority = Priority.HIGH
        )

        assertEquals(initialSize + 1, repository.complaintHistory.size)
        // Check indexed access [last index]
        val lastInList = repository.complaintHistory[repository.complaintHistory.lastIndex]
        assertEquals(newComplaint.id, lastInList.id)
    }

    @Test
    fun testHashMapFastLookup() {
        val complaint = repository.registerComplaint(
            customerName = "Bob",
            customerId = "CUST-802",
            phone = "555-4321",
            email = "bob@example.com",
            title = "Billing Discrepancy",
            description = "Charged $10 extra",
            category = ComplaintCategory.PAYMENT_BILLING,
            priority = Priority.MEDIUM
        )

        // O(1) instant lookup by key
        val found = repository.complaintMap[complaint.id]
        assertNotNull(found)
        assertEquals(complaint.id, found?.id)
        assertEquals("Bob", found?.customerName)
    }

    @Test
    fun testAutoAssignStaffMatchingSpecialization() {
        val complaint = repository.registerComplaint(
            customerName = "Charlie",
            customerId = "CUST-803",
            phone = "555-9876",
            email = "charlie@example.com",
            title = "Invoice Not Received",
            description = "Missing PDF invoice",
            category = ComplaintCategory.PAYMENT_BILLING,
            priority = Priority.MEDIUM
        )

        val assignedStaff = repository.autoAssignComplaint(complaint.id)
        assertNotNull(assignedStaff)
        // Should match Finance & Billing specialization (Elena Rostova STF-202)
        assertEquals(ComplaintCategory.PAYMENT_BILLING, assignedStaff?.specialization)
    }

    @Test
    fun testResolveComplaintRemovesFromPriorityQueue() {
        val complaint = repository.registerComplaint(
            customerName = "Diana",
            customerId = "CUST-804",
            phone = "555-6543",
            email = "diana@example.com",
            title = "Wrong Delivery Item",
            description = "Received incorrect box",
            category = ComplaintCategory.DELIVERY_ISSUE,
            priority = Priority.CRITICAL
        )

        val resolved = repository.resolveComplaint(complaint.id, "Replacement dispatched overnight")
        assertTrue(resolved)

        val updated = repository.complaintMap[complaint.id]
        assertEquals(ComplaintStatus.RESOLVED, updated?.status)
        assertNotNull(updated?.resolutionTimestamp)

        // Must no longer exist in the active PriorityQueue
        val foundInQueue = repository.complaintQueue.any { it.id == complaint.id }
        assertTrue(!foundInQueue)
    }
}
