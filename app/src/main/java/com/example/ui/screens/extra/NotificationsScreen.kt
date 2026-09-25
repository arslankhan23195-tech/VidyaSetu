package com.example.ui.screens.extra

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.VidyaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

data class NotificationItem(
    val id: String,
    val title: String,
    val description: String,
    val time: String,
    val type: String, // "LIVE", "LECTURE", "TEST", "PURCHASE"
    val isUnread: Boolean
)

@Composable
fun NotificationsScreen(viewModel: VidyaViewModel) {
    val notifications = remember {
        listOf(
            NotificationItem(
                id = "n_1",
                title = "🔴 Live Class Started: Work Power & Energy",
                description = "Prof. Anand Verma is now live with rapid problem solving tricks.",
                time = "10 mins ago",
                type = "LIVE",
                isUnread = true
            ),
            NotificationItem(
                id = "n_2",
                title = "🎉 Course Enrollment Confirmed!",
                description = "Your purchase for Lakshya JEE 2.0 has been verified. Welcome aboard!",
                time = "2 hours ago",
                type = "PURCHASE",
                isUnread = true
            ),
            NotificationItem(
                id = "n_3",
                title = "📝 All India Mock Test 01 Live",
                description = "Attempt the full mock test before Sunday 8:00 PM to get All India Rank.",
                time = "Yesterday",
                type = "TEST",
                isUnread = false
            ),
            NotificationItem(
                id = "n_4",
                title = "📚 New Lecture Uploaded: Reaction Mechanisms",
                description = "Dr. R. K. Gupta has added Lecture 04 with handwritten PDF notes.",
                time = "2 days ago",
                type = "LECTURE",
                isUnread = false
            )
        )
    }

    Scaffold(
        topBar = {
            VidyaTopBar(
                title = "सूचनाएं / Notifications",
                subtitle = "लाइव क्लास अलर्ट्स एवं अध्ययन अपडेट्स",
                onBackClick = { viewModel.navigateTo(Screen.Main) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("notifications_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(notifications) { notif ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (notif.isUnread) IndigoContainerLight.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    when (notif.type) {
                                        "LIVE" -> LiveRed.copy(alpha = 0.15f)
                                        "PURCHASE" -> SuccessGreen.copy(alpha = 0.15f)
                                        "TEST" -> SaffronContainerLight
                                        else -> IndigoPrimary.copy(alpha = 0.15f)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (notif.type) {
                                    "LIVE" -> Icons.Default.Videocam
                                    "PURCHASE" -> Icons.Default.CheckCircle
                                    "TEST" -> Icons.Default.Assignment
                                    else -> Icons.Default.PlayCircle
                                },
                                contentDescription = null,
                                tint = when (notif.type) {
                                    "LIVE" -> LiveRed
                                    "PURCHASE" -> SuccessGreen
                                    "TEST" -> SaffronDark
                                    else -> IndigoPrimary
                                },
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(notif.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                if (notif.isUnread) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(IndigoPrimary)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(notif.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(notif.time, fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
