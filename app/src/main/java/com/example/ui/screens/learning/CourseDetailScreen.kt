package com.example.ui.screens.learning

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.VidyaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

@Composable
fun CourseDetailScreen(viewModel: VidyaViewModel) {
    val course by viewModel.selectedCourse.collectAsState()
    val batch by viewModel.selectedBatch.collectAsState()

    if (course == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Course not found")
        }
        return
    }

    val currentCourse = course!!

    Scaffold(
        topBar = {
            VidyaTopBar(
                title = currentCourse.title,
                onBackClick = { viewModel.navigateTo(Screen.Main) }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                tonalElevation = 8.dp,
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        if (currentCourse.isFree) {
                            Text("FREE", fontWeight = FontWeight.Black, fontSize = 20.sp, color = SuccessGreen)
                            Text("100% नि:शुल्क", fontSize = 11.sp, color = Color.Gray)
                        } else {
                            Text("₹${currentCourse.priceRupees}", fontWeight = FontWeight.Black, fontSize = 22.sp, color = IndigoPrimary)
                            Text("₹${currentCourse.originalPriceRupees} (Save 60%)", fontSize = 11.sp, color = Color.Gray)
                        }
                    }

                    Button(
                        onClick = {
                            if (batch != null) {
                                viewModel.initiateCheckout(batch!!)
                            } else {
                                val dummyBatch = com.example.data.local.InitialDataGenerator.getInitialBatches().first()
                                viewModel.initiateCheckout(dummyBatch)
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronDark),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("course_buy_now_button")
                    ) {
                        Text(
                            text = if (currentCourse.isFree) "फ्री में शुरू करें / Start Free" else "अभी खरीदें / Buy Now",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
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
                .testTag("course_detail_scroll")
        ) {
            // Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(IndigoContainerLight),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.banner_vidyasetu_study_1790310899912),
                    contentDescription = currentCourse.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${currentCourse.category} • ${currentCourse.subject}",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentCourse.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    text = currentCourse.titleHindi,
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DetailMetric(icon = Icons.Default.Star, label = "${currentCourse.rating} (${currentCourse.reviewCount})", tint = SaffronDark)
                    DetailMetric(icon = Icons.Default.PlayCircle, label = "${currentCourse.lecturesCount} Lectures", tint = IndigoPrimary)
                    DetailMetric(icon = Icons.Default.Assignment, label = "${currentCourse.testsCount} Mock Tests", tint = EmeraldTertiary)
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp))

                // Faculty Profile
                Text("शिक्षक एवं मार्गदर्शक / Mentors", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(IndigoPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.School, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(currentCourse.teacherName, fontWeight = FontWeight.Bold)
                            Text(currentCourse.teacherTitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp))

                // About course
                Text("कोर्स के बारे में / Course Description", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = currentCourse.description,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Syllabus summary
                Text("पाठ्यक्रम कवरेज / Syllabus Covered", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = currentCourse.syllabusSummary,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun DetailMetric(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, tint: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
