package com.example.ui.screens.extra

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Announcement
import com.example.ui.components.VidyaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

@Composable
fun AnnouncementsScreen(viewModel: VidyaViewModel) {
    val announcements by viewModel.allAnnouncements.collectAsState()

    Scaffold(
        topBar = {
            VidyaTopBar(
                title = "घोषणाएं / Announcements & Alerts",
                subtitle = "परीक्षा तिथियां, बैच अपडेट्स एवं महत्वपूर्ण सूचनाएं",
                onBackClick = { viewModel.navigateTo(Screen.Main) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("announcements_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(announcements) { ann ->
                AnnouncementDetailCard(ann = ann)
            }
        }
    }
}

@Composable
fun AnnouncementDetailCard(ann: Announcement) {
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
                    color = when (ann.priority) {
                        "URGENT" -> ErrorRed.copy(alpha = 0.15f)
                        "EXAM_ALERT" -> SaffronContainerLight
                        else -> IndigoContainerLight
                    },
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = ann.priority,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (ann.priority) {
                            "URGENT" -> ErrorRed
                            "EXAM_ALERT" -> SaffronDark
                            else -> IndigoPrimary
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = ann.timestamp,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = ann.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = ann.message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "जारीकर्ता: ${ann.authorName} • लक्षित बैच: ${ann.targetBatch}",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
