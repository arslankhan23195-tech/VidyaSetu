package com.example.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
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
fun ProfileScreen(viewModel: VidyaViewModel) {
    val user by viewModel.currentUser.collectAsState()
    val appLanguage by viewModel.appLanguage.collectAsState()
    var showLanguageDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            VidyaTopBar(
                title = "विद्यार्थी प्रोफ़ाइल / My Profile",
                subtitle = "अध्ययन प्रगति, प्रमाण-पत्र एवं सेटिंग्स"
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .testTag("profile_screen_root")
        ) {
            // Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(IndigoPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (user?.name?.firstOrNull() ?: 'र').toString(),
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = user?.name ?: "राहुल शर्मा",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )

                    Text(
                        text = "${user?.mobile} • ${user?.email}",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = IndigoContainerLight,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "🎯 ${user?.studentClass} • ${user?.targetExam}",
                            color = IndigoPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Stats Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ProfileStatBadge(
                    emoji = "🔥",
                    value = "${user?.streakDays ?: 7} दिन",
                    label = "Study Streak",
                    modifier = Modifier.weight(1f)
                )
                ProfileStatBadge(
                    emoji = "📺",
                    value = "${user?.totalLecturesWatched ?: 38}",
                    label = "लेक्चर्स पूर्ण",
                    modifier = Modifier.weight(1f)
                )
                ProfileStatBadge(
                    emoji = "📝",
                    value = "${user?.testsCompleted ?: 9}",
                    label = "टेस्ट्स हल",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Study Actions & Quick Links
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column {
                    ProfileMenuRow(
                        icon = Icons.Default.Analytics,
                        title = "अध्ययन विश्लेषण / Performance Analytics",
                        subtitle = "साप्ताहिक समय, मजबूत व कमजोर अध्याय",
                        onClick = { viewModel.navigateTo(Screen.ProgressAnalytics) }
                    )
                    Divider()
                    ProfileMenuRow(
                        icon = Icons.Default.Description,
                        title = "सहेजे गए नोट्स / Bookmarked Notes",
                        subtitle = "त्वरित रिवीज़न हेतु सेव किए गए PDF",
                        onClick = { viewModel.navigateTo(Screen.StudyNotes) }
                    )
                    Divider()
                    ProfileMenuRow(
                        icon = Icons.Default.QuestionAnswer,
                        title = "मेरे पूछे गए सवाल / My Doubts",
                        subtitle = "शिक्षकों से प्राप्त उत्तर देखें",
                        onClick = { viewModel.navigateTo(Screen.Doubts) }
                    )
                    Divider()
                    ProfileMenuRow(
                        icon = Icons.Default.Translate,
                        title = "भाषा बदलें / App Language ($appLanguage)",
                        subtitle = "Hindi, Hinglish, English",
                        onClick = { showLanguageDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Switch Role / Mode Section (To explore Admin Panel or Teacher Panel)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SaffronContainerLight)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⚡ एडमिन व शिक्षक पैनल एक्सप्लोर करें / Role Portal",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF78350F),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "विद्यासेतु एडमिन या फैकल्टी के रूप में लॉगिन करके कोर्स, टेस्ट, लाइव क्लास व डाउट प्रबंधन देखें:",
                        fontSize = 11.sp,
                        color = Color(0xFF92400E)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.switchUserRole("admin") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                        ) {
                            Text("एडमिन पैनल", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { viewModel.switchUserRole("teacher") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronDark)
                        ) {
                            Text("शिक्षक पैनल", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout Button
            OutlinedButton(
                onClick = { viewModel.navigateTo(Screen.Login) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("profile_logout_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("लॉग आउट / Logout", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text("माध्यम चुनें / Select Language") },
            text = {
                Column {
                    listOf("Hindi", "Hinglish", "English").forEach { lang ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setLanguage(lang)
                                    showLanguageDialog = false
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = appLanguage == lang, onClick = {
                                viewModel.setLanguage(lang)
                                showLanguageDialog = false
                            })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(lang, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }
}

@Composable
fun ProfileStatBadge(
    emoji: String,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun ProfileMenuRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
    }
}
