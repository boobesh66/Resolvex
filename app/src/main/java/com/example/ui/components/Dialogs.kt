package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.Complaint
import com.example.model.ComplaintCategory
import com.example.model.ComplaintStatus
import com.example.model.Priority
import com.example.model.SupportStaff
import com.example.ui.theme.ResolveXPrimary
import com.example.ui.theme.ResolveXPrimaryContainer
import com.example.ui.theme.StatusAssigned
import com.example.ui.theme.StatusInProgress
import com.example.ui.theme.StatusResolved
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterComplaintDialog(
    onDismiss: () -> Unit,
    onSubmit: (
        name: String,
        customerId: String,
        phone: String,
        email: String,
        title: String,
        description: String,
        category: ComplaintCategory,
        priority: Priority
    ) -> Unit
) {
    var customerName by remember { mutableStateOf("") }
    var customerId by remember { mutableStateOf("CUST-${(101..109).random()}") }
    var phone by remember { mutableStateOf("+1 (555) ") }
    var email by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ComplaintCategory.TECHNICAL_ISSUE) }
    var selectedPriority by remember { mutableStateOf(Priority.CRITICAL) }
    var categoryExpanded by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 24.dp)
                .testTag("register_complaint_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Register New Complaint",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Inserts into PriorityQueue, ArrayList, and HashMap",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_register_dialog")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                // Customer Info Section
                Text(
                    text = "Customer Information",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = ResolveXPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text("Customer Name *") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1.2f)
                            .testTag("input_customer_name")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = customerId,
                        onValueChange = { customerId = it },
                        label = { Text("Customer ID") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(0.8f)
                            .testTag("input_customer_id")
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_customer_phone")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_customer_email")
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Complaint Details Section
                Text(
                    text = "Complaint Details",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = ResolveXPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Complaint Title *") },
                    placeholder = { Text("e.g., Server Cluster Downtime") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_complaint_title")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Detailed Description *") },
                    placeholder = { Text("Describe the exact problem and impact...") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_complaint_desc")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedCategory.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("select_category_dropdown")
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        ComplaintCategory.entries.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.displayName) },
                                onClick = {
                                    selectedCategory = category
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Priority Selection
                Text(
                    text = "Severity / Priority *",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Priority.entries.forEach { priority ->
                        val isSelected = selectedPriority == priority
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) priority.bgColor else MaterialTheme.colorScheme.surfaceVariant)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) priority.color else Color.Transparent,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedPriority = priority }
                                .padding(vertical = 10.dp)
                                .testTag("select_priority_${priority.name.lowercase()}")
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = priority.emoji,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = priority.displayName,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) priority.color else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("cancel_register_button")
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (customerName.isBlank() || title.isBlank() || description.isBlank()) {
                                errorMessage = "Please fill in Customer Name, Title, and Description."
                            } else {
                                onSubmit(
                                    customerName.trim(),
                                    customerId.trim(),
                                    phone.trim(),
                                    if (email.isBlank()) "customer@example.com" else email.trim(),
                                    title.trim(),
                                    description.trim(),
                                    selectedCategory,
                                    selectedPriority
                                )
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ResolveXPrimary),
                        modifier = Modifier.testTag("submit_register_button")
                    ) {
                        Text("Register Complaint")
                    }
                }
            }
        }
    }
}

@Composable
fun AssignStaffDialog(
    complaint: Complaint,
    staffList: List<SupportStaff>,
    onDismiss: () -> Unit,
    onAssign: (staffId: String) -> Unit,
    onAutoAssign: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("assign_staff_dialog")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Assign Support Staff",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${complaint.id} • ${complaint.category.displayName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = ResolveXPrimary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Auto-Assign Smart Button
                Button(
                    onClick = {
                        onAutoAssign()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ResolveXPrimaryContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_smart_auto_assign")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Auto Match",
                        tint = ResolveXPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Smart Auto-Assign (Best Match)",
                        color = ResolveXPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Or Select Available Staff Manually:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(260.dp)
                ) {
                    items(staffList) { staff ->
                        val isSpecialist = staff.specialization == complaint.category
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSpecialist) ResolveXPrimaryContainer.copy(alpha = 0.4f)
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onAssign(staff.id)
                                    onDismiss()
                                }
                                .testTag("staff_item_${staff.id}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (staff.isAvailable) StatusResolved else MaterialTheme.colorScheme.outline)
                                ) {
                                    Text(
                                        text = staff.name.take(2).uppercase(),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = staff.name,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp
                                        )
                                        if (isSpecialist) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "MATCH",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ResolveXPrimary,
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(ResolveXPrimary.copy(alpha = 0.15f))
                                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "${staff.department} • ${staff.specialization.displayName}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Active: ${staff.assignedComplaints} • Resolved: ${staff.resolvedComplaints}",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }

                                Text(
                                    text = if (staff.isAvailable) "Available" else "Busy",
                                    color = if (staff.isAvailable) StatusResolved else MaterialTheme.colorScheme.error,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResolveComplaintDialog(
    complaint: Complaint,
    onDismiss: () -> Unit,
    onConfirm: (remarks: String) -> Unit
) {
    var remarks by remember { mutableStateOf("") }
    val currentDate = remember {
        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Resolve Complaint ${complaint.id}",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "Customer: ${complaint.customerName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Resolution Date: $currentDate",
                    style = MaterialTheme.typography.bodySmall,
                    color = ResolveXPrimary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = remarks,
                    onValueChange = { remarks = it },
                    label = { Text("Resolution Remarks *") },
                    placeholder = { Text("Explain root cause and resolution applied...") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_resolution_remarks")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(remarks.ifBlank { "Issue resolved and validated with the client." })
                },
                colors = ButtonDefaults.buttonColors(containerColor = StatusResolved),
                modifier = Modifier.testTag("btn_confirm_resolve")
            ) {
                Text("Confirm Resolution")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("btn_cancel_resolve")
            ) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStaffDialog(
    onDismiss: () -> Unit,
    onAdd: (name: String, dept: String, category: ComplaintCategory) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var dept by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ComplaintCategory.TECHNICAL_ISSUE) }
    var categoryExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Support Staff", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Staff Full Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_staff_name")
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = dept,
                    onValueChange = { dept = it },
                    label = { Text("Department *") },
                    placeholder = { Text("e.g. Operations, IT, Finance") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_staff_dept")
                )
                Spacer(modifier = Modifier.height(10.dp))
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedCategory.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Specialization") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        ComplaintCategory.entries.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.displayName) },
                                onClick = {
                                    selectedCategory = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && dept.isNotBlank()) {
                        onAdd(name.trim(), dept.trim(), selectedCategory)
                    }
                },
                modifier = Modifier.testTag("btn_confirm_add_staff")
            ) {
                Text("Add Staff")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun ComplaintDetailModal(
    complaint: Complaint,
    onDismiss: () -> Unit,
    onAssignClicked: () -> Unit,
    onResolveClicked: () -> Unit,
    onUpdateStatus: (ComplaintStatus) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 20.dp)
                .testTag("complaint_detail_modal")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header with badges
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = complaint.id,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = ResolveXPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        PriorityBadge(priority = complaint.priority)
                        Spacer(modifier = Modifier.width(6.dp))
                        StatusBadge(status = complaint.status)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title & Category
                Text(
                    text = complaint.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                CategoryChip(category = complaint.category)

                Spacer(modifier = Modifier.height(12.dp))

                // Description Box
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = complaint.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Timeline
                ComplaintTimelineView(currentStatus = complaint.status)

                Spacer(modifier = Modifier.height(14.dp))

                // Customer & Staff cards
                Row(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = ResolveXPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Customer", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(complaint.customerName, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text(complaint.customerId, fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                            Text(complaint.customerPhone, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(complaint.customerEmail, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AssignmentInd, contentDescription = null, tint = StatusAssigned, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Assigned Staff", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            if (complaint.assignedStaffName != null) {
                                Text(complaint.assignedStaffName, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text("ID: ${complaint.assignedStaffId}", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                            } else {
                                Text("Unassigned", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Dates & Remarks
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Registered: ${complaint.registeredDateFormatted}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (complaint.resolutionDateFormatted != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp), tint = StatusResolved)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Resolved: ${complaint.resolutionDateFormatted}",
                            fontSize = 11.sp,
                            color = StatusResolved,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (complaint.resolutionRemarks != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = StatusResolved.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Resolution Remarks", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = StatusResolved)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(complaint.resolutionRemarks, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Actions Bar
                Text("Manage Workflow", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onAssignClicked,
                        modifier = Modifier.weight(1f).testTag("detail_btn_assign")
                    ) {
                        Text("Assign Staff")
                    }

                    if (complaint.status.isOpen) {
                        if (complaint.status != ComplaintStatus.IN_PROGRESS) {
                            Button(
                                onClick = { onUpdateStatus(ComplaintStatus.IN_PROGRESS) },
                                colors = ButtonDefaults.buttonColors(containerColor = StatusInProgress),
                                modifier = Modifier.weight(1f).testTag("detail_btn_in_progress")
                            ) {
                                Text("In Progress")
                            }
                        }

                        Button(
                            onClick = onResolveClicked,
                            colors = ButtonDefaults.buttonColors(containerColor = StatusResolved),
                            modifier = Modifier.weight(1f).testTag("detail_btn_resolve")
                        ) {
                            Text("Resolve")
                        }
                    } else if (complaint.status == ComplaintStatus.RESOLVED) {
                        Button(
                            onClick = { onUpdateStatus(ComplaintStatus.CLOSED) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.outline),
                            modifier = Modifier.weight(1f).testTag("detail_btn_close")
                        ) {
                            Text("Close Ticket")
                        }
                    }
                }
            }
        }
    }
}
