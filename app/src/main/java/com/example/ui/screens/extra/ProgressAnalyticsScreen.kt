package com.example.ui.screens.extra

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.ui.components.VidyaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

@Composable
fun ProgressAnalyticsScreen(viewModel: VidyaViewModel) {
    val user by viewModel.currentUser.collectAsState()

    val weeklyStudyHours = listOf(
        "सोम (Mon)" to 4.2f,
        "मंगल (Tue)" to 5.5f,
        "बुध (Wed)" to 3.8f,
        "गुरु (Thu)" to 6.0f,
        "शुक्र (Fri)" to 4.8f,
        "शनि (Sat)" to 7.2f,
        "रवि (Sun)" to 5.0f
    )
    val maxHours = 8.0f

    Scaffold(
        topBar = {
            VidyaTopBar(
                title = "प्रगति विश्लेषण / Study Analytics",
                subtitle = "विस्तृत अध्ययन समय, सटीकता एवं विषयवार रिपोर्ट",
                onBackClick = { viewModel.navigateTo(Screen.Main) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .testTag("progress_analytics_screen")
        ) {
            // Study Streak Hero Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SaffronContainerLight)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🔥", fontSize = 36.sp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "${user?.streakDays ?: 7} दिन लगातार अध्ययन!",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF78350F)
                            )
                        )
                        Text(
                            text = "इस सप्ताह कुल 36.5 घंटे अध्ययन किया गया • लक्ष्य: 40 घंटे",
                            fontSize = 11.sp,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Weekly Study Hours Graph
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "साप्ताहिक अध्ययन घंटे / Weekly Study Hours",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        weeklyStudyHours.forEach { (day, hours) ->
                            val heightFraction = (hours / maxHours).coerceIn(0.1f, 1f)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "${hours}h",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IndigoPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(18.dp)
                                        .fillMaxHeight(heightFraction)
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(IndigoPrimary)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = day.take(3),
                                    fontSize = 9.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Subject Completion Status
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "विषयवार पाठ्यक्रम पूर्णता / Subject Mastery",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    SubjectProgressRow(name = "भौतिकी (Physics)", percent = 72, color = IndigoPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    SubjectProgressRow(name = "रसायन विज्ञान (Chemistry)", percent = 65, color = EmeraldTertiary)
                    Spacer(modifier = Modifier.height(8.dp))
                    SubjectProgressRow(name = "गणित (Mathematics)", percent = 80, color = SaffronDark)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Strong vs Weak Topics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Strong Topics
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldContainerLight.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("💪", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("मजबूत टॉपिक्स", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = EmeraldTertiary)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("• Kinematics 1D & 2D", fontSize = 11.sp)
                        Text("• Chemical Bonding", fontSize = 11.sp)
                        Text("• Quadratic Equations", fontSize = 11.sp)
                    }
                }

                // Needs Improvement
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ErrorRed.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⚠️", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("सुधार की आवश्यकता", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = ErrorRed)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("• Rotational Dynamics", fontSize = 11.sp)
                        Text("• Ionic Equilibrium", fontSize = 11.sp)
                        Text("• Definite Integrals", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectProgressRow(name: String, percent: Int, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text("$percent%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { percent / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color
        )
    }
}
