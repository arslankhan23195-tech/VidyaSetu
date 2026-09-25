package com.example.ui.screens.learning

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Lecture
import com.example.ui.components.VidyaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

@Composable
fun BatchDetailScreen(viewModel: VidyaViewModel) {
    val batch by viewModel.selectedBatch.collectAsState()
    val lectures by viewModel.currentBatchLectures.collectAsState()
    val notes by viewModel.allNotes.collectAsState()
    val tests by viewModel.allTests.collectAsState()
    val announcements by viewModel.allAnnouncements.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("लेक्चर्स / Lectures", "नोट्स / Notes", "टेस्ट / Tests", "घोषणाएं / Updates")

    if (batch == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Batch details unavailable")
        }
        return
    }

    val currentBatch = batch!!

    Scaffold(
        topBar = {
            VidyaTopBar(
                title = currentBatch.title,
                subtitle = "${currentBatch.targetExam} • ${currentBatch.mediumLanguage}",
                onBackClick = { viewModel.navigateTo(Screen.Main) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("batch_detail_root")
        ) {
            // Header summary card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = if (currentBatch.isEnrolled) SuccessGreen else SaffronDark,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (currentBatch.isEnrolled) "ENROLLED STUDENT" else "ENROLLMENT OPEN",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = currentBatch.validity,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = currentBatch.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Text(
                        text = "शिक्षकों की टीम: ${currentBatch.teachersList}",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (currentBatch.isEnrolled) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("अध्ययन प्रगति / Course Progress", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${currentBatch.progressPercent}%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { currentBatch.progressPercent / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = SuccessGreen
                            )
                        }
                    } else {
                        Button(
                            onClick = { viewModel.initiateCheckout(currentBatch) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronDark),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("अभी खरीदें ₹${currentBatch.priceRupees}", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Tabs for Lectures, Notes, Tests, Announcements
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.testTag("batch_tab_$index")
                    )
                }
            }

            Divider()

            // Tab Content
            when (selectedTabIndex) {
                0 -> {
                    // Lectures list
                    val batchLectures = lectures.ifEmpty {
                        com.example.data.local.InitialDataGenerator.getInitialLectures()
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(batchLectures) { lecture ->
                            LectureItemCard(
                                lecture = lecture,
                                onLectureClick = { viewModel.openLecture(lecture) }
                            )
                        }
                    }
                }
                1 -> {
                    // Notes list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(notes) { note ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.openNotePdf(note) },
                                shape = RoundedCornerShape(12.dp),
                                border = CardDefaults.outlinedCardBorder(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(EmeraldContainerLight),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = EmeraldTertiary)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(note.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text("${note.subject} • ${note.pageCount} Pages • ${note.fileSizeMb} MB", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    IconButton(onClick = { viewModel.toggleNoteBookmark(note) }) {
                                        Icon(
                                            imageVector = if (note.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                            contentDescription = "Bookmark",
                                            tint = if (note.isBookmarked) SaffronDark else Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Tests list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(tests) { test ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.startTest(test) },
                                shape = RoundedCornerShape(12.dp),
                                border = CardDefaults.outlinedCardBorder(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(test.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text("${test.totalQuestions} Questions • ${test.durationMinutes} Mins • ${test.totalMarks} Marks", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        if (test.isAttempted) {
                                            Text("अंतिम स्कोर: ${test.lastScore}/${test.totalMarks}", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                        }
                                    }
                                    Button(
                                        onClick = { viewModel.startTest(test) },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                                    ) {
                                        Text(if (test.isAttempted) "पुनः दें / Re-take" else "प्रारंभ / Start", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
                3 -> {
                    // Announcements
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(announcements) { ann ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(ann.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(ann.timestamp, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(ann.message, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LectureItemCard(lecture: Lecture, onLectureClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onLectureClick)
            .testTag("lecture_item_${lecture.id}"),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (lecture.isCompleted) SuccessGreen.copy(alpha = 0.15f) else IndigoContainerLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (lecture.isCompleted) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = if (lecture.isCompleted) SuccessGreen else IndigoPrimary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lecture.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${lecture.teacherName} • ${lecture.durationMinutes} mins • ${lecture.subject}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (lecture.isCompleted) {
                Surface(
                    color = SuccessGreen.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Watched",
                        color = SuccessGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            } else if (lecture.watchedSeconds > 0) {
                Surface(
                    color = SaffronContainerLight,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "In Progress",
                        color = SaffronDark,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
