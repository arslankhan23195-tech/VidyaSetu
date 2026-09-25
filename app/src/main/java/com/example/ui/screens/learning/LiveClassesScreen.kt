package com.example.ui.screens.learning

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
import com.example.data.model.LiveClass
import com.example.ui.components.LivePillBadge
import com.example.ui.components.VidyaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

@Composable
fun LiveClassesScreen(viewModel: VidyaViewModel) {
    val liveClasses by viewModel.allLiveClasses.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Live & Upcoming, 1: Past Recordings

    Scaffold(
        topBar = {
            VidyaTopBar(
                title = "लाइव क्लासेस / Live Classroom",
                subtitle = "इंटरएक्टिव क्लास, लाइव चैट एवं डाउट क्लियरिंग",
                onBackClick = { viewModel.navigateTo(Screen.Main) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("live_classes_screen")
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(12.dp)),
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("लाइव व आगामी / Live & Upcoming", fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("पूर्व रिकॉर्डिंग्स / Recordings", fontSize = 12.sp) }
                )
            }

            val filteredList = if (selectedTab == 0) {
                liveClasses.filter { !it.isCompleted }
            } else {
                liveClasses.filter { it.isCompleted }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredList) { live ->
                    LiveClassDetailCard(
                        live = live,
                        onJoin = {
                            val dummyLec = com.example.data.local.InitialDataGenerator.getInitialLectures().first()
                            viewModel.openLecture(dummyLec)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LiveClassDetailCard(live: LiveClass, onJoin: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("live_card_${live.id}"),
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
                if (live.isLiveNow) {
                    LivePillBadge()
                } else if (live.isCompleted) {
                    Surface(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "RECORDED",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                } else {
                    Surface(
                        color = SaffronContainerLight,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "UPCOMING",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = live.scheduledTime,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = live.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "फैकल्टी: ${live.teacherName} • ${live.subject}",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "👥 ${live.attendeeCount} छात्र पंजीकृत हैं",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                Button(
                    onClick = onJoin,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (live.isLiveNow) LiveRed else IndigoPrimary
                    )
                ) {
                    Icon(
                        imageVector = if (live.isCompleted) Icons.Default.PlayCircle else Icons.Default.Videocam,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (live.isLiveNow) "लाइव जुड़ें / Join Live" else if (live.isCompleted) "रिकॉर्डिंग देखें / Watch" else "शामिल हों / Set Reminder",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
