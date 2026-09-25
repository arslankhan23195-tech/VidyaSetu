package com.example.ui.screens.learning

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.data.model.Lecture
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel
import kotlinx.coroutines.delay

@Composable
fun LecturePlayerScreen(viewModel: VidyaViewModel) {
    val lecture by viewModel.activeLecture.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val playbackSpeed by viewModel.playbackSpeed.collectAsState()
    val videoQuality by viewModel.videoQuality.collectAsState()
    val videoProgress by viewModel.videoProgress.collectAsState()

    var showControls by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Overview, 1: Notes, 2: Doubts
    var doubtText by remember { mutableStateOf("") }
    var doubtSubmittedToast by remember { mutableStateOf(false) }

    if (lecture == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No active lecture selected")
        }
        return
    }

    val currentLecture = lecture!!

    // Auto-hide player controls after 4 seconds
    LaunchedEffect(showControls, isPlaying) {
        if (showControls && isPlaying) {
            delay(4000)
            showControls = false
        }
    }

    // Advance simulated video time when playing
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(1000)
            val newProgress = (videoProgress + 0.005f).coerceAtMost(1f)
            viewModel.seekVideo(newProgress)
        }
    }

    val totalDurationSeconds = currentLecture.durationMinutes * 60
    val currentSeconds = (videoProgress * totalDurationSeconds).toInt()
    val currentMinutes = currentSeconds / 60
    val currentSecRem = currentSeconds % 60
    val totalMinutes = totalDurationSeconds / 60
    val totalSecRem = totalDurationSeconds % 60
    val formattedCurrentTime = String.format("%02d:%02d", currentMinutes, currentSecRem)
    val formattedTotalTime = String.format("%02d:%02d", totalMinutes, totalSecRem)

    Scaffold(
        topBar = {
            // Optional top bar or controls overlay
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("lecture_player_screen")
        ) {
            // 1. VIDEO PLAYER CANVAS (Professional Dark Video Frame)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(Color.Black)
                    .clickable { showControls = !showControls }
                    .testTag("video_player_box"),
                contentAlignment = Alignment.Center
            ) {
                // Video visual mock: chalkboard style lecture canvas with formula & topic
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Surface(
                        color = IndigoPrimary.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "VIDYA-SETU HD LECTURE STREAM",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = currentLecture.chapterName,
                        color = SaffronSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "I = ∑ m_i r_i² • τ = I α • L = I ω",
                        color = Color.LightGray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currentLecture.teacherName,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }

                // Controls Overlay
                androidx.compose.animation.AnimatedVisibility(
                    visible = showControls,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.55f))
                    ) {
                        // Top row of controls (Back, Title, Quality, Speed)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { viewModel.navigateTo(Screen.BatchDetail) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                // Quality Selector Chip
                                Surface(
                                    color = Color.White.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.clickable {
                                        val nextQuality = when (videoQuality) {
                                            "360p" -> "720p"
                                            "720p" -> "1080p"
                                            else -> "360p"
                                        }
                                        viewModel.setVideoQuality(nextQuality)
                                    }
                                ) {
                                    Text(
                                        text = videoQuality,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                // Playback Speed Chip
                                Surface(
                                    color = Color.White.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.clickable {
                                        val nextSpeed = when (playbackSpeed) {
                                            1.0f -> 1.25f
                                            1.25f -> 1.5f
                                            1.5f -> 2.0f
                                            else -> 1.0f
                                        }
                                        viewModel.setPlaybackSpeed(nextSpeed)
                                    }
                                ) {
                                    Text(
                                        text = "${playbackSpeed}x",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        // Center Play/Pause button & 10s Rewind/Forward
                        Row(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalArrangement = Arrangement.spacedBy(28.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    viewModel.seekVideo((videoProgress - 0.05f).coerceAtLeast(0f))
                                },
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f))
                            ) {
                                Icon(Icons.Default.Replay10, contentDescription = "Rewind 10s", tint = Color.White)
                            }

                            IconButton(
                                onClick = { viewModel.togglePlayPause() },
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(IndigoPrimary)
                                    .testTag("video_play_pause_button")
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Pause" else "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            IconButton(
                                onClick = {
                                    viewModel.seekVideo((videoProgress + 0.05f).coerceAtMost(1f))
                                },
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f))
                            ) {
                                Icon(Icons.Default.Forward10, contentDescription = "Forward 10s", tint = Color.White)
                            }
                        }

                        // Bottom Player bar (SeekBar & Timestamps & Fullscreen)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Slider(
                                value = videoProgress,
                                onValueChange = { viewModel.seekVideo(it) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = SliderDefaults.colors(
                                    thumbColor = SaffronSecondary,
                                    activeTrackColor = SaffronSecondary,
                                    inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                                )
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$formattedCurrentTime / $formattedTotalTime",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Fullscreen,
                                        contentDescription = "Fullscreen",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. LECTURE HEADER & QUICK ACTIONS
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = currentLecture.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${currentLecture.teacherName} • ${currentLecture.subject}",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    PlayerActionItem(
                        icon = Icons.Default.Description,
                        label = "PDF नोट्स",
                        onClick = {
                            val dummyNote = com.example.data.local.InitialDataGenerator.getInitialNotes().first()
                            viewModel.openNotePdf(dummyNote)
                        }
                    )
                    PlayerActionItem(
                        icon = Icons.Default.Download,
                        label = "डाउनलोड",
                        onClick = { /* simulated download */ }
                    )
                    PlayerActionItem(
                        icon = Icons.Default.BookmarkBorder,
                        label = "सेव करें",
                        onClick = { /* simulated bookmark */ }
                    )
                    PlayerActionItem(
                        icon = Icons.Default.QuestionAnswer,
                        label = "डाउट पूछें",
                        onClick = { selectedTab = 1 }
                    )
                }
            }

            Divider()

            // 3. TAB NAVIGATION (Overview, Ask Doubt, Next Lectures)
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("विवरण / Overview", fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("संदेह पूछें / Ask Doubt", fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("अगले लेक्चर्स / Playlist", fontSize = 12.sp) }
                )
            }

            // Tab Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                when (selectedTab) {
                    0 -> {
                        Column {
                            Text("अध्याय का सार / Chapter Concept", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "इस व्याख्यान में घूर्णन गति (Rotational Motion) के मूलभूत सिद्धांतों, द्रव्यमान केंद्र (Center of Mass), बल आघूर्ण (Torque) और कोणीय संवेग संरक्षण (Conservation of Angular Momentum) का विस्तृत विश्लेषण किया गया है। जेईई एडवांस्ड के पिछले 10 वर्षों के महत्वपूर्ण प्रश्नों को स्टेप-बाय-स्टेप हल किया गया है।",
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("संलग्न अध्ययन सामग्री / Attached Material", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        val dummyNote = com.example.data.local.InitialDataGenerator.getInitialNotes().first()
                                        viewModel.openNotePdf(dummyNote)
                                    },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = EmeraldTertiary)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text("Rotational Mechanics Class Notes (Handwritten).pdf", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                        Text("4.8 MB • 38 Pages", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                    1 -> {
                        Column {
                            Text("इस लेक्चर से सम्बंधित प्रश्न पूछें / Ask Doubt", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "शिक्षक द्वारा 24 घंटे में आपके प्रश्न का विस्तृत उत्तर दिया जाएगा।",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = doubtText,
                                onValueChange = { doubtText = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                placeholder = { Text("अपना संदेह यहाँ विस्तार से लिखें (उदा. समय 24:10 पर फ़ॉर्मूला कैसे लगा?)...") },
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    if (doubtText.isNotBlank()) {
                                        viewModel.submitDoubt(
                                            subject = currentLecture.subject,
                                            chapter = currentLecture.chapterName,
                                            text = doubtText
                                        )
                                        doubtText = ""
                                        doubtSubmittedToast = true
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                            ) {
                                Text("संदेह भेजें / Submit Doubt", fontWeight = FontWeight.Bold)
                            }

                            if (doubtSubmittedToast) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "✅ आपका संदेह सफलता पूर्वक भेज दिया गया है!",
                                    color = SuccessGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                    2 -> {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            val allLectures = com.example.data.local.InitialDataGenerator.getInitialLectures()
                            allLectures.forEach { lec ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.openLecture(lec) },
                                    shape = RoundedCornerShape(10.dp),
                                    border = CardDefaults.outlinedCardBorder(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (lec.id == currentLecture.id) IndigoContainerLight else MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = if (lec.id == currentLecture.id) Icons.Default.Equalizer else Icons.Default.PlayCircle,
                                            contentDescription = null,
                                            tint = if (lec.id == currentLecture.id) IndigoPrimary else Color.Gray
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(lec.title, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, maxLines = 1)
                                            Text("${lec.durationMinutes} mins • ${lec.teacherName}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
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
fun PlayerActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = IndigoPrimary, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    }
}
