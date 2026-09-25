package com.example.ui.screens.testing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.data.model.Question
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestCbtScreen(viewModel: VidyaViewModel) {
    val state by viewModel.cbtState.collectAsState()
    var showPaletteSheet by remember { mutableStateOf(false) }
    var showSubmitConfirmation by remember { mutableStateOf(false) }

    val currentTest = state.test
    val questions = state.questions
    val currentIndex = state.currentQuestionIndex
    val currentQuestion = questions.getOrNull(currentIndex)

    val remainingMinutes = state.remainingSeconds / 60
    val remainingSec = state.remainingSeconds % 60
    val timerString = String.format("%02d:%02d", remainingMinutes, remainingSec)

    if (currentTest == null || questions.isEmpty() || currentQuestion == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = currentTest.title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1
                        )
                        Text(
                            text = "Question ${currentIndex + 1} of ${questions.size}",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                },
                actions = {
                    // Timer Display Chip
                    Surface(
                        color = if (remainingMinutes < 5) ErrorRed.copy(alpha = 0.15f) else IndigoContainerLight,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = if (remainingMinutes < 5) ErrorRed else IndigoPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = timerString,
                                fontWeight = FontWeight.Bold,
                                color = if (remainingMinutes < 5) ErrorRed else IndigoPrimary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Question Palette Toggle Button
                    IconButton(onClick = { showPaletteSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = "Question Palette"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                tonalElevation = 6.dp,
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { if (currentIndex > 0) viewModel.selectQuestionIndex(currentIndex - 1) },
                        enabled = currentIndex > 0,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("पिछला / Prev")
                    }

                    OutlinedButton(
                        onClick = { viewModel.toggleMarkForReview() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (state.markedForReview.contains(currentQuestion.id)) PurpleAccent else MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (state.markedForReview.contains(currentQuestion.id)) "Marked ★" else "Review ★")
                    }

                    if (currentIndex < questions.size - 1) {
                        Button(
                            onClick = { viewModel.selectQuestionIndex(currentIndex + 1) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                        ) {
                            Text("Next / अगला")
                        }
                    } else {
                        Button(
                            onClick = { showSubmitConfirmation = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                            modifier = Modifier.testTag("cbt_final_submit_button")
                        ) {
                            Text("Submit / जमा करें")
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .testTag("test_cbt_screen_content")
        ) {
            // Question Marks Badge
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
                        text = "Subject: ${currentQuestion.subject}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = "+4 Marks / -1 Negative",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Question Text (English)
            Text(
                text = "Q${currentIndex + 1}. ${currentQuestion.questionText}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )
            )

            // Question Text (Hindi Translation)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "प्र. ${currentQuestion.questionTextHindi}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = IndigoPrimary,
                    lineHeight = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 4 Options (A, B, C, D)
            val selectedOpt = state.selectedOptions[currentQuestion.id]
            val options = listOf(
                1 to currentQuestion.optionA,
                2 to currentQuestion.optionB,
                3 to currentQuestion.optionC,
                4 to currentQuestion.optionD
            )

            options.forEach { (optNum, optText) ->
                val isSelected = selectedOpt == optNum
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { viewModel.answerCurrentQuestion(optNum) }
                        .testTag("option_${optNum}"),
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) IndigoContainerLight else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { viewModel.answerCurrentQuestion(optNum) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${('A' + optNum - 1)}. $optText",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) IndigoPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Clear answer button
            if (selectedOpt != null) {
                TextButton(
                    onClick = {
                        val updated = state.selectedOptions.toMutableMap()
                        updated.remove(currentQuestion.id)
                        // Update state
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Clear Response / उत्तर हटाएं", color = ErrorRed, fontSize = 12.sp)
                }
            }
        }
    }

    // Question Palette BottomSheet / Dialog
    if (showPaletteSheet) {
        ModalBottomSheet(onDismissRequest = { showPaletteSheet = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Question Palette / प्रश्न सूची",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    LegendItem(color = SuccessGreen, text = "Answered")
                    LegendItem(color = PurpleAccent, text = "Review")
                    LegendItem(color = Color.LightGray, text = "Unvisited")
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.heightIn(max = 240.dp)
                ) {
                    itemsIndexed(questions) { idx, q ->
                        val isAnswered = state.selectedOptions.containsKey(q.id)
                        val isReview = state.markedForReview.contains(q.id)
                        val isCurrent = idx == currentIndex

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when {
                                        isAnswered -> SuccessGreen
                                        isReview -> PurpleAccent
                                        else -> Color.LightGray.copy(alpha = 0.5f)
                                    }
                                )
                                .clickable {
                                    viewModel.selectQuestionIndex(idx)
                                    showPaletteSheet = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${idx + 1}",
                                color = if (isAnswered || isReview) Color.White else Color.Black,
                                fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        showPaletteSheet = false
                        showSubmitConfirmation = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Submit Test Now", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Submit Confirmation Dialog
    if (showSubmitConfirmation) {
        val answeredCount = state.selectedOptions.size
        val unattemptedCount = questions.size - answeredCount
        val reviewCount = state.markedForReview.size

        AlertDialog(
            onDismissRequest = { showSubmitConfirmation = false },
            title = { Text("टेस्ट जमा करें / Submit Test?") },
            text = {
                Column {
                    Text("क्या आप सचमुच परीक्षा समाप्त करना चाहते हैं?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• हल किए गए प्रश्न: $answeredCount")
                    Text("• अनसुलझे प्रश्न: $unattemptedCount")
                    Text("• समीक्षा हेतु चिन्हित: $reviewCount")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitConfirmation = false
                        viewModel.submitTest()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                ) {
                    Text("हाँ, जमा करें / Yes, Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitConfirmation = false }) {
                    Text("नहीं / Resume")
                }
            }
        )
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
