package com.example.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Course
import com.example.data.model.LiveClass
import com.example.ui.components.LivePillBadge
import com.example.ui.components.VidyaBrandLogo
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

@Composable
fun HomeScreen(viewModel: VidyaViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val allCourses by viewModel.allCourses.collectAsState()
    val enrolledBatches by viewModel.enrolledBatches.collectAsState()
    val liveClasses by viewModel.allLiveClasses.collectAsState()
    val announcements by viewModel.allAnnouncements.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen_root"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Top Bar & Greeting Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                IndigoPrimaryDark,
                                IndigoPrimary
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 18.dp)
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(SaffronSecondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (currentUser?.name?.firstOrNull() ?: 'र').toString(),
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "नमस्ते, ${currentUser?.name?.split(" ")?.firstOrNull() ?: "विद्यार्थी"}! 👋",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Surface(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "🎯 ${currentUser?.targetExam ?: "JEE 2027"}",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Row {
                        IconButton(
                            onClick = { viewModel.navigateTo(Screen.Notifications) },
                            modifier = Modifier.testTag("home_notification_button")
                        ) {
                            BadgedBox(badge = { Badge { Text("2") } }) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = Color.White
                                )
                            }
                        }
                        IconButton(
                            onClick = { viewModel.navigateTo(Screen.Search) },
                            modifier = Modifier.testTag("home_search_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search Bar Trigger
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { viewModel.navigateTo(Screen.Search) }
                        .testTag("home_search_trigger_bar"),
                    color = Color.White,
                    tonalElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "कोर्स, विषय, अध्याय या शिक्षक खोजें...",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // 2. Study Streak & Progress Counter
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clickable { viewModel.navigateTo(Screen.ProgressAnalytics) }
                    .testTag("home_streak_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SaffronContainerLight)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🔥", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "7 Days Study Streak!",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF78350F)
                                )
                            )
                            Text(
                                text = "32 घंटे कुल अध्ययन • 38 लेक्चर्स पूर्ण",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF92400E))
                            )
                        }
                    }
                    Button(
                        onClick = { viewModel.navigateTo(Screen.ProgressAnalytics) },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronDark),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("एनालिटिक्स", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 3. Continue Learning Banner
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                Text(
                    text = "जारी रखें / Continue Learning",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("continue_learning_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    color = IndigoContainerLight,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "Physics • Rotational Motion",
                                        color = IndigoPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Lecture 03: Rolling Motion & Torque Masterclass",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "By Prof. Anand Verma • 45m left",
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                            IconButton(
                                onClick = {
                                    // Open lecture player
                                    val dummyLec = com.example.data.local.InitialDataGenerator.getInitialLectures().first()
                                    viewModel.openLecture(dummyLec)
                                },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(IndigoPrimary)
                                    .testTag("resume_lecture_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Resume",
                                    tint = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Progress bar
                        LinearProgressIndicator(
                            progress = { 0.65f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = IndigoPrimary,
                            trackColor = BorderSubtleLight,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("65% Watched", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Resume at 24:10", fontSize = 10.sp, color = IndigoPrimary, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // 4. Quick Action Grid (Live, Notes, Doubts, Tests)
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionButton(
                        title = "Live Classes",
                        hindi = "लाइव क्लासेस",
                        icon = Icons.Default.Videocam,
                        badge = "LIVE",
                        color = LiveRed,
                        modifier = Modifier.weight(1f)
                    ) { viewModel.navigateTo(Screen.LiveClasses) }

                    QuickActionButton(
                        title = "Ask Doubt",
                        hindi = "संदेह पूछें",
                        icon = Icons.Default.HelpOutline,
                        badge = "24x7",
                        color = PurpleAccent,
                        modifier = Modifier.weight(1f)
                    ) { viewModel.navigateTo(Screen.Doubts) }

                    QuickActionButton(
                        title = "Study Notes",
                        hindi = "PDF नोट्स",
                        icon = Icons.Default.Description,
                        badge = "PDF",
                        color = EmeraldTertiary,
                        modifier = Modifier.weight(1f)
                    ) { viewModel.navigateTo(Screen.StudyNotes) }

                    QuickActionButton(
                        title = "Daily Quiz",
                        hindi = "दैनिक क्विज",
                        icon = Icons.Default.Quiz,
                        badge = "NEW",
                        color = SaffronDark,
                        modifier = Modifier.weight(1f)
                    ) {
                        val quiz = com.example.data.local.InitialDataGenerator.getInitialTests().find { it.id == "test_daily_quiz" }
                        if (quiz != null) viewModel.startTest(quiz) else viewModel.navigateTo(Screen.TestList)
                    }
                }
            }
        }

        // 5. Today's Live Classes
        item {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "लाइव क्लासेस / Live Classes",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        LivePillBadge()
                    }
                    TextButton(onClick = { viewModel.navigateTo(Screen.LiveClasses) }) {
                        Text("सभी देखें", fontWeight = FontWeight.Bold)
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(liveClasses) { live ->
                        LiveClassCard(live = live, onJoin = {
                            val dummyLec = com.example.data.local.InitialDataGenerator.getInitialLectures().first()
                            viewModel.openLecture(dummyLec)
                        })
                    }
                }
            }
        }

        // 6. My Enrolled Batches
        item {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "मेरे बैचेस / My Enrolled Batches",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    TextButton(onClick = { viewModel.setBottomTab(2) }) {
                        Text("सारे बैच", fontWeight = FontWeight.Bold)
                    }
                }

                if (enrolledBatches.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("अभी कोई बैच एनरोल नहीं किया गया है।")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.setBottomTab(1) }) {
                                Text("कोर्स एक्सप्लोर करें")
                            }
                        }
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(enrolledBatches) { batch ->
                            Card(
                                modifier = Modifier
                                    .width(260.dp)
                                    .clickable { viewModel.selectBatch(batch) }
                                    .testTag("enrolled_batch_${batch.id}"),
                                shape = RoundedCornerShape(14.dp),
                                border = CardDefaults.outlinedCardBorder(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Surface(
                                        color = EmeraldContainerLight,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "Enrolled",
                                            color = EmeraldTertiary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = batch.title,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = batch.targetExam,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    LinearProgressIndicator(
                                        progress = { batch.progressPercent / 100f },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(2.dp)),
                                        color = EmeraldTertiary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${batch.progressPercent}% पूरा हुआ",
                                        fontSize = 10.sp,
                                        color = EmeraldTertiary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 7. Recommended Courses
        item {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "लोकप्रिय कोर्सेज / Popular Courses",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    TextButton(onClick = { viewModel.setBottomTab(1) }) {
                        Text("सभी देखें", fontWeight = FontWeight.Bold)
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(allCourses.take(4)) { course ->
                        CourseCard(course = course, onSelect = { viewModel.selectCourse(course) })
                    }
                }
            }
        }

        // 8. Latest Announcements
        item {
            Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "घोषणाएं / Recent Announcements",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    TextButton(onClick = { viewModel.navigateTo(Screen.Announcements) }) {
                        Text("और देखें", fontWeight = FontWeight.Bold)
                    }
                }

                announcements.take(2).forEach { ann ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("📢", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = ann.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = ann.message,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
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
fun QuickActionButton(
    title: String,
    hindi: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    badge: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag("quick_action_${title.lowercase().replace(" ", "_")}"),
        color = MaterialTheme.colorScheme.surface,
        border = CardDefaults.outlinedCardBorder(),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = hindi,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 1
            )
            Text(
                text = title,
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
fun LiveClassCard(live: LiveClass, onJoin: () -> Unit) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .clickable(onClick = onJoin)
            .testTag("live_class_card_${live.id}"),
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
                if (live.isLiveNow) {
                    LivePillBadge()
                } else {
                    Surface(
                        color = IndigoContainerLight,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = live.scheduledTime,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = IndigoPrimary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${live.attendeeCount}",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = live.title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${live.teacherName} • ${live.subject}",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onJoin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (live.isLiveNow) LiveRed else IndigoPrimary
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = if (live.isLiveNow) "कक्षा से जुड़ें / Join Live" else "रिमाइंडर सेट करें / Remind Me",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CourseCard(course: Course, onSelect: () -> Unit) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .clickable(onClick = onSelect)
            .testTag("course_card_${course.id}"),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(IndigoContainerLight),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.banner_vidyasetu_study_1790310899912),
                    contentDescription = course.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    color = Color.Black.copy(alpha = 0.65f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = course.category,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = SaffronDark,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${course.rating}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        Text(
                            text = " (${course.reviewCount})",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = "${course.lecturesCount} Lectures",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = course.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = course.teacherName,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (course.isFree) {
                        Text(
                            text = "FREE / निःशुल्क",
                            fontWeight = FontWeight.ExtraBold,
                            color = SuccessGreen,
                            fontSize = 14.sp
                        )
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "₹${course.priceRupees}",
                                fontWeight = FontWeight.ExtraBold,
                                color = IndigoPrimary,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "₹${course.originalPriceRupees}",
                                fontSize = 11.sp,
                                color = Color.Gray,
                                style = androidx.compose.ui.text.TextStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
                            )
                        }
                    }

                    Button(
                        onClick = onSelect,
                        modifier = Modifier.height(32.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                    ) {
                        Text(text = "विवरण / View", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
