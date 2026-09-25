package com.example.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.StatCard
import com.example.ui.components.VidyaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

@Composable
fun AdminDashboardScreen(viewModel: VidyaViewModel) {
    val allCourses by viewModel.allCourses.collectAsState()
    val allBatches by viewModel.allBatches.collectAsState()
    val allDoubts by viewModel.allDoubts.collectAsState()

    var showCreateCourseDialog by remember { mutableStateOf(false) }
    var showCreateBatchDialog by remember { mutableStateOf(false) }
    var showAnnouncementDialog by remember { mutableStateOf(false) }
    var showAddLectureDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            VidyaTopBar(
                title = "प्रशासक डैशबोर्ड / Admin Control Panel",
                subtitle = "VidyaSetu Platform Management Suite",
                actions = {
                    IconButton(onClick = { viewModel.switchUserRole("student") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Switch to Student",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .testTag("admin_dashboard_root")
        ) {
            // Role Banner
            Surface(
                color = IndigoPrimary,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🛡️", fontSize = 32.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "VidyaSetu Super Admin",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Logged in as Administrator • Full platform management permissions",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "कुल विद्यार्थी",
                    value = "56,420+",
                    icon = Icons.Default.People,
                    iconColor = IndigoPrimary,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "सक्रिय बैचेस",
                    value = "${allBatches.size}",
                    icon = Icons.Default.Groups,
                    iconColor = SaffronDark,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "कुल कोर्सेज",
                    value = "${allCourses.size}",
                    icon = Icons.Default.MenuBook,
                    iconColor = EmeraldTertiary,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "मासिक राजस्व",
                    value = "₹42.8 Lakh",
                    icon = Icons.Default.CurrencyRupee,
                    iconColor = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Management Actions
            Text(
                text = "त्वरित प्रशासनिक कार्य / Management Actions",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminActionButton(
                    title = "नया कोर्स बनाएँ",
                    subtitle = "Add New Course",
                    icon = Icons.Default.AddCircle,
                    color = IndigoPrimary,
                    modifier = Modifier.weight(1f),
                    onClick = { showCreateCourseDialog = true }
                )
                AdminActionButton(
                    title = "नया बैच जोड़ें",
                    subtitle = "Create Batch",
                    icon = Icons.Default.GroupAdd,
                    color = SaffronDark,
                    modifier = Modifier.weight(1f),
                    onClick = { showCreateBatchDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminActionButton(
                    title = "लेक्चर अपलोड",
                    subtitle = "Add Lecture Video",
                    icon = Icons.Default.VideoCall,
                    color = EmeraldTertiary,
                    modifier = Modifier.weight(1f),
                    onClick = { showAddLectureDialog = true }
                )
                AdminActionButton(
                    title = "घोषणा प्रसारित करें",
                    subtitle = "Broadcast Alert",
                    icon = Icons.Default.Campaign,
                    color = ErrorRed,
                    modifier = Modifier.weight(1f),
                    onClick = { showAnnouncementDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Recent Pending Doubts Overview
            Text(
                text = "छात्रों के लंबित संदेह / Doubts Awaiting Attention",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            allDoubts.take(3).forEach { doubt ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("${doubt.studentName} (${doubt.subject})", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(doubt.questionText, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                        }
                        Surface(
                            color = if (doubt.status == "ANSWERED") SuccessGreen else SaffronDark,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = doubt.status,
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Back to Student View Button
            OutlinedButton(
                onClick = { viewModel.switchUserRole("student") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("विद्यार्थी दृश्य में वापस जाएँ / Return to Student App")
            }
        }
    }

    // Dialogs
    if (showCreateCourseDialog) {
        CreateCourseDialog(
            onDismiss = { showCreateCourseDialog = false },
            onCreate = { title, cat, subj, price, desc ->
                viewModel.createCourse(title, cat, subj, price, desc)
                showCreateCourseDialog = false
            }
        )
    }

    if (showCreateBatchDialog) {
        CreateBatchDialog(
            courses = allCourses,
            onDismiss = { showCreateBatchDialog = false },
            onCreate = { courseId, title, price, medium ->
                viewModel.createBatch(courseId, title, price, medium)
                showCreateBatchDialog = false
            }
        )
    }

    if (showAnnouncementDialog) {
        CreateAnnouncementDialog(
            onDismiss = { showAnnouncementDialog = false },
            onBroadcast = { title, msg, prio ->
                viewModel.broadcastAnnouncement(title, msg, prio)
                showAnnouncementDialog = false
            }
        )
    }

    if (showAddLectureDialog) {
        AddLectureDialog(
            batches = allBatches,
            onDismiss = { showAddLectureDialog = false },
            onAdd = { batchId, title, subj, teacher, dur ->
                viewModel.addLecture(batchId, title, subj, teacher, dur)
                showAddLectureDialog = false
            }
        )
    }
}

@Composable
fun AdminActionButton(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(26.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// Dialog Implementations
@Composable
fun CreateCourseDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, String, Int, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("JEE") }
    var subject by remember { mutableStateOf("Physics") }
    var priceText by remember { mutableStateOf("2499") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("नया कोर्स जोड़ें / New Course") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Course Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category (JEE/NEET/Class 10)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject (Physics/Chemistry/Maths)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("Price in Rupees (0 for Free)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val p = priceText.toIntOrNull() ?: 0
                if (title.isNotBlank()) onCreate(title, category, subject, p, description)
            }) {
                Text("Create Course")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun CreateBatchDialog(
    courses: List<com.example.data.model.Course>,
    onDismiss: () -> Unit,
    onCreate: (String, String, Int, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("2999") }
    var medium by remember { mutableStateOf("Hinglish") }
    val selectedCourseId = courses.firstOrNull()?.id ?: "course_jee_lakshya"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("नया बैच बनाएँ / Create Batch") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Batch Name (e.g. Lakshya 3.0)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("Batch Fee (₹)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = medium,
                    onValueChange = { medium = it },
                    label = { Text("Medium (Hindi / Hinglish / English)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val p = priceText.toIntOrNull() ?: 0
                if (title.isNotBlank()) onCreate(selectedCourseId, title, p, medium)
            }) {
                Text("Create Batch")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun CreateAnnouncementDialog(
    onDismiss: () -> Unit,
    onBroadcast: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("URGENT") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("घोषणा प्रसारित करें / Broadcast Alert") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Announcement Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Detailed Message") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("NORMAL", "URGENT", "EXAM_ALERT").forEach { p ->
                        FilterChip(
                            selected = priority == p,
                            onClick = { priority = p },
                            label = { Text(p, fontSize = 10.sp) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (title.isNotBlank() && message.isNotBlank()) onBroadcast(title, message, priority)
            }) {
                Text("Broadcast")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AddLectureDialog(
    batches: List<com.example.data.model.Batch>,
    onDismiss: () -> Unit,
    onAdd: (String, String, String, String, Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("Physics") }
    var teacher by remember { mutableStateOf("Prof. Anand Verma") }
    var durationText by remember { mutableStateOf("60") }
    val batchId = batches.firstOrNull()?.id ?: "batch_jee_2027"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("नया व्याख्यान जोड़ें / Add Lecture") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Lecture Title (e.g. Thermodynamics Lec 1)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = teacher,
                    onValueChange = { teacher = it },
                    label = { Text("Teacher Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = durationText,
                    onValueChange = { durationText = it },
                    label = { Text("Duration (minutes)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val dur = durationText.toIntOrNull() ?: 60
                if (title.isNotBlank()) onAdd(batchId, title, subject, teacher, dur)
            }) {
                Text("Add Lecture")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
