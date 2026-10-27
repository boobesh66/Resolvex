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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CategoryChip
import com.example.ui.components.PriorityBadge
import com.example.ui.components.StatusBadge
import com.example.ui.theme.PriorityCritical
import com.example.ui.theme.PriorityCriticalBg
import com.example.ui.theme.ResolveXPrimary
import com.example.ui.theme.ResolveXPrimaryContainer
import com.example.ui.theme.ResolveXSecondary
import com.example.ui.theme.ResolveXSecondaryContainer
import com.example.ui.theme.ResolveXTertiary
import com.example.ui.theme.ResolveXTertiaryContainer
import com.example.ui.theme.StatusResolved
import com.example.viewmodel.ComplaintViewModel

@Composable
fun DataStructureVisualizerScreen(
    viewModel: ComplaintViewModel,
    modifier: Modifier = Modifier
) {
    val pendingQueue by viewModel.pendingQueue.collectAsState()
    val allComplaints by viewModel.allComplaints.collectAsState()
    val diagnosticResult by viewModel.diagnosticResult.collectAsState()

    var testSearchId by remember { mutableStateOf("C1002") }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Live Visualizer, 1: Java Code Reference

    val tabs = listOf("Interactive Visualizer", "Java Implementation Code")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("data_structures_screen")
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))

            // Header
            Column {
                Text(
                    text = "Data Structure Visualization",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "PriorityQueue • ArrayList • HashMap Demonstration",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (selectedTab == 0) {
            // === 1. PRIORITY QUEUE SECTION ===
            item {
                DataStructureHeaderCard(
                    title = "PriorityQueue",
                    purpose = "Complaint Prioritization",
                    description = "A binary max-heap prioritizing higher severity first, with FIFO timestamp resolution for tie-breakers.",
                    complexity = "Insert: O(log N) • Poll: O(log N) • Peek: O(1)",
                    badgeColor = PriorityCritical,
                    badgeBgColor = PriorityCriticalBg,
                    icon = Icons.Default.Bolt
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Live PriorityQueue Elements (Ordered by Comparator)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = ResolveXPrimary
                            )
                            Text(
                                text = "${pendingQueue.size} in Heap",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (pendingQueue.isEmpty()) {
                            Text("PriorityQueue is currently empty.", fontSize = 12.sp, color = StatusResolved)
                        } else {
                            pendingQueue.take(5).forEachIndexed { index, complaint ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (index == 0) PriorityCriticalBg else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "${index + 1}.",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 12.sp,
                                        color = if (index == 0) PriorityCritical else MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = complaint.id,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = ResolveXPrimary
                                    )
                                    Text(" – ", fontSize = 12.sp)
                                    Text(
                                        text = complaint.title,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    PriorityBadge(priority = complaint.priority)
                                }
                            }
                            if (pendingQueue.size > 5) {
                                Text(
                                    text = "... + ${pendingQueue.size - 5} more items in binary heap",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // === 2. ARRAYLIST SECTION ===
            item {
                DataStructureHeaderCard(
                    title = "ArrayList",
                    purpose = "Complaint History & Sequential Audit Log",
                    description = "Dynamic array storing all complaints chronologically. Fast random access using indices [0], [1], [2]...",
                    complexity = "Index Access: O(1) • Amortized Append: O(1) • Search: O(N)",
                    badgeColor = ResolveXPrimary,
                    badgeBgColor = ResolveXPrimaryContainer,
                    icon = Icons.Default.FormatListNumbered
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Indexed Complaint Records (ArrayList<Complaint>)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = ResolveXPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Grid/Row of array elements
                        allComplaints.take(8).forEachIndexed { index, complaint ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                            ) {
                                Text(
                                    text = "[$index]",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = ResolveXPrimary,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(ResolveXPrimaryContainer)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = complaint.id,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = complaint.title,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    modifier = Modifier.weight(1f)
                                )
                                StatusBadge(status = complaint.status)
                            }
                        }

                        if (allComplaints.size > 8) {
                            Text(
                                text = "... Total size: ${allComplaints.size} continuous indexed elements",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // === 3. HASHMAP SECTION ===
            item {
                DataStructureHeaderCard(
                    title = "HashMap",
                    purpose = "Fast Complaint Lookup & Directory Mapping",
                    description = "Hash table mapping unique Complaint ID keys directly to Complaint objects using hashCode() & bucket arrays.",
                    complexity = "Average Lookup: O(1) • Put: O(1) • Worst Case: O(N)",
                    badgeColor = ResolveXSecondary,
                    badgeBgColor = ResolveXSecondaryContainer,
                    icon = Icons.Default.Dataset
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Key-Value Hash Mapping (HashMap<String, Complaint>)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = ResolveXPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Sample Key-Value Mappings
                        allComplaints.take(4).forEach { c ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "\"${c.id}\"",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = ResolveXPrimary
                                )
                                Text(" ➔ ", fontWeight = FontWeight.Bold, color = ResolveXSecondary)
                                Text(
                                    text = "Complaint(title=\"${c.title.take(24)}...\", priority=${c.priority.name})",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        // Interactive Instant Lookup Testing Box
                        Text(
                            text = "Live O(1) Instant Lookup Benchmark",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = testSearchId,
                                onValueChange = {
                                    testSearchId = it
                                    viewModel.testLookup(it)
                                },
                                label = { Text("Enter Complaint ID (e.g. C1001)") },
                                singleLine = true,
                                modifier = Modifier.weight(1f).testTag("input_hashmap_lookup")
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { viewModel.testLookup(testSearchId) },
                                colors = ButtonDefaults.buttonColors(containerColor = ResolveXSecondary),
                                modifier = Modifier.testTag("btn_test_hashmap_lookup")
                            ) {
                                Text("Lookup")
                            }
                        }

                        // Diagnostic Output
                        if (diagnosticResult != null) {
                            val diag = diagnosticResult!!
                            Spacer(modifier = Modifier.height(10.dp))
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = ResolveXSecondaryContainer.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "⚡ Hash Evaluation Metrics:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = ResolveXSecondary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "• Key: \"${diag.key}\"\n" +
                                               "• key.hashCode(): ${diag.hashCode}\n" +
                                               "• Bucket Index: Math.abs(hashCode) % 16 = [Bucket #${diag.bucketIndex}]\n" +
                                               "• HashMap Lookup Time: ${diag.lookupDurationNanos} ns (O(1))\n" +
                                               "• ArrayList Scan Time: ${diag.arrayListScanDurationNanos} ns (O(N) linear iteration)",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 16.sp
                                    )
                                    if (diag.complaint != null) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "✔ Found: [${diag.complaint.id}] ${diag.complaint.title} (${diag.complaint.status.displayName})",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = StatusResolved
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "✖ Key not found in complaintMap.",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        } else {
            // === JAVA CODE REFERENCE SECTION ===
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Code, contentDescription = null, tint = ResolveXPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Java Data Structures Architecture Code",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "This exact Java architecture is implemented in the project for faculty review:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        JavaCodeBlock(
                            code = """
// 1. Data Structure Declarations
PriorityQueue<Complaint> complaintQueue;
ArrayList<Complaint> complaintHistory;
HashMap<String, Complaint> complaintMap;
HashMap<String, Customer> customerMap;
HashMap<String, SupportStaff> staffMap;

// 2. Custom PriorityQueue Comparator
// Rule 1: Higher Priority First (Critical > High > Medium > Low)
// Rule 2: Older Registration Timestamp First (FIFO Tie-Breaker)
Comparator<Complaint> complaintComparator = new Comparator<Complaint>() {
    @Override
    public int compare(Complaint c1, Complaint c2) {
        int priorityCompare = Integer.compare(
            c2.getPriority().getLevel(),
            c1.getPriority().getLevel()
        );
        if (priorityCompare != 0) {
            return priorityCompare;
        }
        return Long.compare(
            c1.getRegisteredTimestamp(),
            c2.getRegisteredTimestamp()
        );
    }
};

complaintQueue = new PriorityQueue<>(11, complaintComparator);
complaintHistory = new ArrayList<>();
complaintMap = new HashMap<>();

// 3. Register Complaint (3-Way Insertion)
public void registerComplaint(Complaint complaint) {
    // Instant O(1) Key-Value Mapping
    complaintMap.put(complaint.getId(), complaint);
    
    // Amortized O(1) Sequential History
    complaintHistory.add(complaint);
    
    // O(log N) Priority-Heap Insertion
    complaintQueue.offer(complaint);
}

// 4. Process Next Complaint (O(log N))
public Complaint processNextComplaint() {
    return complaintQueue.poll(); // Extracts root of max-heap
}

// 5. Fast Lookup by Complaint ID (O(1))
public Complaint lookupById(String complaintId) {
    return complaintMap.get(complaintId);
}
                            """.trimIndent()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun DataStructureHeaderCard(
    title: String,
    purpose: String,
    description: String,
    complexity: String,
    badgeColor: Color,
    badgeBgColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = badgeBgColor.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(badgeColor)
                    ) {
                        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = badgeColor
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = purpose,
                        color = badgeColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "⚡ Time Complexity: $complexity",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = badgeColor
            )
        }
    }
}

@Composable
fun JavaCodeBlock(code: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF0F172A))
            .padding(12.dp)
    ) {
        Text(
            text = code,
            color = Color(0xFFE2E8F0),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            lineHeight = 16.sp
        )
    }
}
