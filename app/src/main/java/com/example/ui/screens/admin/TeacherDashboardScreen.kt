package com.example.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Doubt
import com.example.ui.components.VidyaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

@Composable
fun TeacherDashboardScreen(viewModel: VidyaViewModel) {
    val user by viewModel.currentUser.collectAsState()
    val doubts by viewModel.allDoubts.collectAsState()
    val pendingDoubts = doubts.filter { it.status == "PENDING" }

    var showScheduleLiveDialog by remember { mutableStateOf(false) }
    var doubtToAnswer by remember { mutableStateOf<Doubt?>(null) }
    var answerText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            VidyaTopBar(
                title = "शिक्षक पैनल / Faculty Portal",
                subtitle = "${user?.name ?: "Prof. Anand Verma"} • HOD Physics",
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
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showScheduleLiveDialog = true },
                containerColor = SaffronDark,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Videocam, contentDescription = null) },
                text = { Text("लाइव क्लास शेड्यूल करें", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .testTag("teacher_dashboard_root")
        ) {
            // Teacher Profile Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SaffronContainerLight)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("👨‍🏫", fontSize = 36.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "स्वागत है, ${user?.name ?: "आनंद वर्मा सर"}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF78350F)
                        )
                        Text(
                            text = "Assigned Batches: Lakshya JEE 2.0, Arjuna NEET • 33,400 Students",
                            fontSize = 11.sp,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showScheduleLiveDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = LiveRed, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Schedule Live", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("आज या कल की क्लास", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { /* simulated notes upload */ },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Upload Notes", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("हस्तलिखित PDF जोड़ें", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Pending Doubts for Teacher to Answer
            Text(
                text = "छात्रों के अनसुलझे संदेह (${pendingDoubts.size} Pending)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "किसी भी प्रश्न पर टैप करके तुरंत अपना समाधान टाइप करें:",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (pendingDoubts.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = EmeraldContainerLight.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎉", fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("सभी डाउट हल हो चुके हैं! All doubts answered.", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            } else {
                pendingDoubts.forEach { doubt ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                doubtToAnswer = doubt
                                answerText = ""
                            },
                        shape = RoundedCornerShape(12.dp),
                        border = CardDefaults.outlinedCardBorder(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("छात्र: ${doubt.studentName}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Surface(
                                    color = SaffronContainerLight,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "${doubt.subject} • ${doubt.chapter}",
                                        fontSize = 10.sp,
                                        color = SaffronDark,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = doubt.questionText,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    doubtToAnswer = doubt
                                    answerText = ""
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("उत्तर लिखें / Solve Doubt", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = { viewModel.switchUserRole("student") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("विद्यार्थी दृश्य में वापस जाएँ / Switch to Student View")
            }
        }
    }

    // Schedule Live Class Dialog
    if (showScheduleLiveDialog) {
        var liveTitle by remember { mutableStateOf("") }
        var liveSubject by remember { mutableStateOf("Physics") }
        var liveTime by remember { mutableStateOf("Today, 7:00 PM") }

        AlertDialog(
            onDismissRequest = { showScheduleLiveDialog = false },
            title = { Text("लाइव क्लास शेड्यूल करें") },
            text = {
                Column {
                    OutlinedTextField(
                        value = liveTitle,
                        onValueChange = { liveTitle = it },
                        label = { Text("Class Title (e.g. Rapid Problem Solving)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = liveSubject,
                        onValueChange = { liveSubject = it },
                        label = { Text("Subject") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = liveTime,
                        onValueChange = { liveTime = it },
                        label = { Text("Date & Time (e.g. Today, 7:00 PM)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (liveTitle.isNotBlank()) {
                        viewModel.scheduleLiveClass(liveTitle, liveSubject, liveTime)
                        showScheduleLiveDialog = false
                    }
                }) {
                    Text("Schedule Live")
                }
            },
            dismissButton = {
                TextButton(onClick = { showScheduleLiveDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Answer Doubt Dialog
    if (doubtToAnswer != null) {
        AlertDialog(
            onDismissRequest = { doubtToAnswer = null },
            title = { Text("संदेह का उत्तर दें / Reply to Student") },
            text = {
                Column {
                    Text("छात्र का प्रश्न:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(doubtToAnswer!!.questionText, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = answerText,
                        onValueChange = { answerText = it },
                        label = { Text("विस्तृत उत्तर व हल यहाँ लिखें...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (answerText.isNotBlank()) {
                        viewModel.answerDoubt(doubtToAnswer!!.id, answerText)
                        doubtToAnswer = null
                    }
                }) {
                    Text("उत्तर भेजें / Send Solution")
                }
            },
            dismissButton = {
                TextButton(onClick = { doubtToAnswer = null }) { Text("Cancel") }
            }
        )
    }
}
