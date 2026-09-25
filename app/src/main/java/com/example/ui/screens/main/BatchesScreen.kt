package com.example.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.example.data.model.Batch
import com.example.ui.components.VidyaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.VidyaViewModel

@Composable
fun BatchesScreen(viewModel: VidyaViewModel) {
    val allBatches by viewModel.allBatches.collectAsState()
    val enrolledBatches by viewModel.enrolledBatches.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Enrolled, 1: Explore All

    Scaffold(
        topBar = {
            VidyaTopBar(
                title = "बैचेस / Study Batches",
                subtitle = "लाइव क्लासेस, टेस्ट सीरीज एवं स्ट्रक्चर्ड पढ़ाई"
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("batches_screen_root")
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
                    text = {
                        Text(
                            "मेरे बैचेस (${enrolledBatches.size})",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            "सभी उपलब्ध बैचेस (${allBatches.size})",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }

            val displayList = if (selectedTab == 0) enrolledBatches else allBatches

            if (displayList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎓", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (selectedTab == 0) "कोई सक्रिय एनरोल्ड बैच नहीं है" else "कोई बैच उपलब्ध नहीं है",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (selectedTab == 0) "उपलब्ध बैचेस टैब में जाकर अपनी पसंदीदा परीक्षा का बैच चुनें।" else "कृपया बाद में जाँचें।",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        if (selectedTab == 0) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { selectedTab = 1 },
                                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                            ) {
                                Text("नए बैचेस देखें / Explore Batches")
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(displayList) { batch ->
                        BatchCardItem(
                            batch = batch,
                            isEnrolled = batch.isEnrolled,
                            onBatchClick = { viewModel.selectBatch(batch) },
                            onEnrollClick = { viewModel.initiateCheckout(batch) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BatchCardItem(
    batch: Batch,
    isEnrolled: Boolean,
    onBatchClick: () -> Unit,
    onEnrollClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onBatchClick)
            .testTag("batch_card_${batch.id}"),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(IndigoContainerLight),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.banner_vidyasetu_study_1790310899912),
                    contentDescription = batch.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    color = if (isEnrolled) SuccessGreen else SaffronDark,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                ) {
                    Text(
                        text = if (isEnrolled) "ENROLLED" else "${batch.enrolledCount}+ Students",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${batch.targetExam} • ${batch.mediumLanguage}",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = batch.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "शिक्षक: ${batch.teachersList}",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Metadata row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("📺 ${batch.lectureCount} लेक्चर्स", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("📝 ${batch.testCount} टेस्ट्स", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("📚 ${batch.notesCount} नोट्स", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (isEnrolled) {
                    // Show Progress bar
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("बैच पूर्णता / Progress", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${batch.progressPercent}%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { batch.progressPercent / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = SuccessGreen
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = onBatchClick,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                        ) {
                            Text("कक्षा में जाएँ / Enter Classroom", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            if (batch.priceRupees == 0) {
                                Text("FREE / निःशुल्क", fontWeight = FontWeight.ExtraBold, color = SuccessGreen, fontSize = 16.sp)
                            } else {
                                Text("₹${batch.priceRupees}", fontWeight = FontWeight.ExtraBold, color = IndigoPrimary, fontSize = 18.sp)
                                Text("₹${batch.originalPriceRupees}", fontSize = 11.sp, color = Color.Gray)
                            }
                        }

                        Button(
                            onClick = onEnrollClick,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronDark)
                        ) {
                            Text("अभी प्रवेश लें / Buy Now", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
