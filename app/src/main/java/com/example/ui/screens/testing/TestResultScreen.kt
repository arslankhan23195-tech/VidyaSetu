package com.example.ui.screens.testing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import com.example.ui.components.VidyaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

@Composable
fun TestResultScreen(viewModel: VidyaViewModel) {
    val state by viewModel.cbtState.collectAsState()
    val test = state.test
    val questions = state.questions

    val totalMarks = state.totalMarks.coerceAtLeast(1)
    val percentage = ((state.score.toFloat() / totalMarks) * 100).toInt().coerceIn(0, 100)
    val totalAttempted = state.correctCount + state.wrongCount
    val accuracy = if (totalAttempted > 0) ((state.correctCount.toFloat() / totalAttempted) * 100).toInt() else 0

    var showSolutionsTab by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            VidyaTopBar(
                title = "परीक्षा परिणाम / Test Analysis",
                subtitle = test?.title ?: "Score Card",
                onBackClick = { viewModel.navigateTo(Screen.Main) }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                tonalElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.navigateTo(Screen.Main) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("डैशबोर्ड पर जाएँ / Home")
                    }

                    Button(
                        onClick = { showSolutionsTab = !showSolutionsTab },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                    ) {
                        Text(if (showSolutionsTab) "स्कोर कार्ड / Score" else "विस्तृत हल / Solutions")
                    }
                }
            }
        }
    ) { padding ->
        if (showSolutionsTab) {
            // Detailed Solutions List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "विस्तृत हल एवं व्याख्या / Step-by-Step Solutions",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                itemsIndexed(questions) { index, q ->
                    val userOption = state.selectedOptions[q.id]
                    val isCorrect = userOption == q.correctOption
                    val isUnattempted = userOption == null

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        border = CardDefaults.outlinedCardBorder(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "Q${index + 1} • ${q.subject}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Surface(
                                    color = when {
                                        isCorrect -> SuccessGreen.copy(alpha = 0.15f)
                                        isUnattempted -> Color.LightGray.copy(alpha = 0.3f)
                                        else -> ErrorRed.copy(alpha = 0.15f)
                                    },
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = when {
                                            isCorrect -> "CORRECT (+4)"
                                            isUnattempted -> "UNATTEMPTED (0)"
                                            else -> "WRONG (-1)"
                                        },
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when {
                                            isCorrect -> SuccessGreen
                                            isUnattempted -> Color.DarkGray
                                            else -> ErrorRed
                                        },
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = q.questionText,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "आपका उत्तर: ${if (userOption != null) ('A' + userOption - 1).toString() else "कोई नहीं (Not attempted)"}",
                                fontSize = 12.sp,
                                color = if (isCorrect) SuccessGreen else if (isUnattempted) Color.Gray else ErrorRed,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "सही उत्तर: विकल्प ${('A' + q.correctOption - 1)}",
                                fontSize = 12.sp,
                                color = SuccessGreen,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("💡 स्पष्टीकरण / Explanation:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = IndigoPrimary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(q.explanationText, fontSize = 11.sp, lineHeight = 18.sp)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Main Scorecard & Performance Metrics
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Score Hero Banner
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("result_score_hero_card"),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = IndigoPrimary)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🏆 आपका प्राप्तांक / Your Score",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "${state.score}",
                                    color = Color.White,
                                    fontSize = 44.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = " / $totalMarks",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = SaffronSecondary,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "प्रतिशत: $percentage% • AIR रैंक अनुमान: Top 4%",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                // Breakdown Grid (Correct, Wrong, Unattempted, Accuracy)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ResultMetricBox(
                            title = "सही उत्तर",
                            value = "${state.correctCount}",
                            icon = Icons.Default.CheckCircle,
                            color = SuccessGreen,
                            modifier = Modifier.weight(1f)
                        )
                        ResultMetricBox(
                            title = "गलत उत्तर",
                            value = "${state.wrongCount}",
                            icon = Icons.Default.Cancel,
                            color = ErrorRed,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ResultMetricBox(
                            title = "अनसुलझे",
                            value = "${state.unattemptedCount}",
                            icon = Icons.Default.HelpOutline,
                            color = Color.Gray,
                            modifier = Modifier.weight(1f)
                        )
                        ResultMetricBox(
                            title = "सटीकता",
                            value = "$accuracy%",
                            icon = Icons.Default.Analytics,
                            color = IndigoPrimary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Performance Insight
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "विशेषज्ञ समीक्षा / Performance Insight",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (percentage >= 70) {
                                    "शानदार प्रदर्शन! आपकी सैद्धांतिक समझ और गति दोनों उत्कृष्ट स्तर पर हैं। गलत प्रश्नों के हल देखकर कमजोर क्षेत्रों को मजबूत करें।"
                                } else {
                                    "अच्छा प्रयास! गति के साथ सटीकता पर विशेष ध्यान दें। गलत प्रश्नों के हल ध्यानपूर्वक पढ़ें और संबंधित अध्याय का पुनरीक्षण करें।"
                                },
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResultMetricBox(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
            Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
