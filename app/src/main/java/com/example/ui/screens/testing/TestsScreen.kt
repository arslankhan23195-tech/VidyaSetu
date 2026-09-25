package com.example.ui.screens.testing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.data.model.ExamTest
import com.example.ui.components.VidyaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

@Composable
fun TestsScreen(viewModel: VidyaViewModel) {
    val tests by viewModel.allTests.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Full Mocks", "Chapter Tests", "Daily Quiz", "PYQs")

    Scaffold(
        topBar = {
            VidyaTopBar(
                title = "टेस्ट सीरीज / Test Series & Quizzes",
                subtitle = "असली परीक्षा (CBT) जैसा अनुभव एवं सटीक विश्लेषण"
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("tests_screen_root")
        ) {
            // Category Filter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 11.sp, fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }

            // Summary Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoContainerLight)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🎯", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "कंप्यूटर-बेस्ड टेस्ट (CBT) मोड",
                            fontWeight = FontWeight.Bold,
                            color = IndigoPrimary,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "प्रत्येक टेस्ट के बाद ऑल इंडिया रैंक, विषयवार सटीकता और विस्तृत समाधान उपलब्ध हैं।",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(tests) { test ->
                    ExamTestCard(
                        test = test,
                        onStart = { viewModel.startTest(test) }
                    )
                }
            }
        }
    }
}

@Composable
fun ExamTestCard(test: ExamTest, onStart: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("test_card_${test.id}"),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${test.targetExam} • ${test.subject}",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                Surface(
                    color = when (test.difficulty) {
                        "Hard" -> ErrorRed.copy(alpha = 0.15f)
                        "Easy" -> SuccessGreen.copy(alpha = 0.15f)
                        else -> SaffronContainerLight
                    },
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = test.difficulty,
                        color = when (test.difficulty) {
                            "Hard" -> ErrorRed
                            "Easy" -> SuccessGreen
                            else -> SaffronDark
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = test.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("${test.totalQuestions} प्रश्न", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("${test.durationMinutes} मिनट", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Grade, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("${test.totalMarks} अंक", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (test.isAttempted) {
                    Column {
                        Text(
                            text = "स्कोर: ${test.lastScore}/${test.totalMarks}",
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "सटीकता: ${((test.correctCount.toFloat() / (test.totalAttempted.coerceAtLeast(1))) * 100).toInt()}%",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Text(
                        text = "प्रयास नहीं किया / Unattempted",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                Button(
                    onClick = onStart,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    modifier = Modifier.testTag("start_test_btn_${test.id}")
                ) {
                    Text(
                        text = if (test.isAttempted) "पुनः टेस्ट दें / Re-attempt" else "टेस्ट शुरू करें / Start Test",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
