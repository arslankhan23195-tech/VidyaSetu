package com.example.ui.screens.doubts

import androidx.compose.foundation.background
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
import com.example.data.model.Doubt
import com.example.ui.components.VidyaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

@Composable
fun DoubtsScreen(viewModel: VidyaViewModel) {
    val doubts by viewModel.allDoubts.collectAsState()
    var showAskDoubtDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            VidyaTopBar(
                title = "संदेह निवारण / Doubt Solving 24x7",
                subtitle = "विशेषज्ञ शिक्षकों द्वारा सीधे सटीक समाधान",
                onBackClick = { viewModel.navigateTo(Screen.Main) }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAskDoubtDialog = true },
                containerColor = IndigoPrimary,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.AddComment, contentDescription = null) },
                text = { Text("नया डाउट पूछें / Ask Doubt", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("ask_new_doubt_fab")
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("doubts_screen")
        ) {
            // Hero Tip
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SaffronContainerLight)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("💡", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "24x7 शिक्षक सहायता उपलब्ध",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF78350F),
                            fontSize = 13.sp
                        )
                        Text(
                            text = "अपनी नोटबुक का फोटो खींचकर भेजें या प्रश्न टाइप करें। हमारे शिक्षक कुछ ही देर में उत्तर देंगे।",
                            fontSize = 11.sp,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            }

            Text(
                text = "आपके पूछे गए प्रश्न / Your Doubts (${doubts.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(doubts) { doubt ->
                    DoubtCardItem(doubt = doubt)
                }
            }
        }
    }

    if (showAskDoubtDialog) {
        AskDoubtDialog(
            onDismiss = { showAskDoubtDialog = false },
            onSubmit = { subject, chapter, text ->
                viewModel.submitDoubt(subject, chapter, text)
                showAskDoubtDialog = false
            }
        )
    }
}

@Composable
fun DoubtCardItem(doubt: Doubt) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
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
                        text = "${doubt.subject} • ${doubt.chapter}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                Surface(
                    color = if (doubt.status == "ANSWERED") SuccessGreen.copy(alpha = 0.15f) else SaffronContainerLight,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (doubt.status == "ANSWERED") "हल हो गया / ANSWERED" else "प्रतीक्षारत / PENDING",
                        color = if (doubt.status == "ANSWERED") SuccessGreen else SaffronDark,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "प्र. ${doubt.questionText}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "पूछा गया: ${doubt.timestamp}",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (doubt.status == "ANSWERED") {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = EmeraldContainerLight.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = EmeraldTertiary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "शिक्षक का उत्तर: ${doubt.teacherName}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = EmeraldTertiary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = doubt.teacherAnswer,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AskDoubtDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String) -> Unit
) {
    var subject by remember { mutableStateOf("Physics") }
    var chapter by remember { mutableStateOf("Rotational Motion") }
    var questionText by remember { mutableStateOf("") }
    var photoAttached by remember { mutableStateOf(false) }

    val subjects = listOf("Physics", "Chemistry", "Mathematics", "Biology", "General Studies")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("नया संदेह पूछें / Ask Doubt", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("विषय चुनें / Select Subject:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    subjects.take(3).forEach { s ->
                        FilterChip(
                            selected = subject == s,
                            onClick = { subject = s },
                            label = { Text(s, fontSize = 10.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = chapter,
                    onValueChange = { chapter = it },
                    label = { Text("अध्याय का नाम / Chapter Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = questionText,
                    onValueChange = { questionText = it },
                    label = { Text("अपना प्रश्न लिखें / Type question") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Photo upload simulation button
                OutlinedButton(
                    onClick = { photoAttached = !photoAttached },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = if (photoAttached) Icons.Default.CheckCircle else Icons.Default.AddPhotoAlternate,
                        contentDescription = null,
                        tint = if (photoAttached) SuccessGreen else IndigoPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (photoAttached) "फोटो संलग्न है (1 Photo Attached)" else "नोट्स या प्रश्न का फोटो जोड़ें / Attach Photo")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (questionText.isNotBlank()) {
                        onSubmit(subject, chapter, questionText)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
            ) {
                Text("जमा करें / Submit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("रद्द करें / Cancel")
            }
        }
    )
}
