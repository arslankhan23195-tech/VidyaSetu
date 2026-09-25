package com.example.ui.screens.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.VidyaBrandLogo
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

data class OnboardingPage(
    val titleHindi: String,
    val titleEnglish: String,
    val description: String,
    val tag: String
)

@Composable
fun OnboardingScreen(viewModel: VidyaViewModel) {
    val pages = remember {
        listOf(
            OnboardingPage(
                titleHindi = "भारत के शीर्ष शिक्षकों से सीधा मार्गदर्शन",
                titleEnglish = "Learn from India's Top Mentors",
                description = "IITians, AIIMS डॉक्टर्स एवं टॉप रैंकर्स की टीम द्वारा तैयार किए गए कॉन्सेप्ट्स के साथ अपनी पढ़ाई को नया आयाम दें।",
                tag = "Expert Mentorship"
            ),
            OnboardingPage(
                titleHindi = "लाइव व रिकॉर्डेड लेक्चर्स एवं हस्तलिखित नोट्स",
                titleEnglish = "Interactive Lectures & Handwritten Notes",
                description = "प्रत्येक अध्याय के वीडियो लेक्चर्स, 24x7 संदेह निवारण (Doubt Solving) और रिवीजन फॉर्मूला बुक्स एक क्लिक पर।",
                tag = "Full Syllabus Coverage"
            ),
            OnboardingPage(
                titleHindi = "ऑल इंडिया टेस्ट सीरीज एवं विस्तृत विश्लेषण",
                titleEnglish = "Practice with Tests & Track Progress",
                description = "असली परीक्षा जैसे कंप्यूटर-बेस्ड टेस्ट (CBT) दीजिए, अपनी राष्ट्रीय रैंक और विषयवार सुधार के सुझाव तुरंत पाइए।",
                tag = "Proven Results"
            )
        )
    }

    var currentPage by remember { mutableIntStateOf(0) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("onboarding_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header with logo and Skip button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                VidyaBrandLogo(size = 40)
                TextButton(
                    onClick = { viewModel.navigateTo(Screen.Login) },
                    modifier = Modifier.testTag("onboarding_skip_button")
                ) {
                    Text(
                        text = "Skip / छोड़ें",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Visual Hero Asset
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(IndigoContainerLight),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.banner_vidyasetu_study_1790310899912),
                    contentDescription = "VidyaSetu Study Illustration",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Animated content for each step
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "onboarding_text"
            ) { pageIndex ->
                val page = pages[pageIndex]
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        color = SaffronContainerLight,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = page.tag,
                            color = SaffronDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = page.titleHindi,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = page.titleEnglish,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = page.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }

            // Footer: Indicators & Next / Get Started Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Page Indicator dots
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    pages.indices.forEach { index ->
                        val isSelected = index == currentPage
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .height(8.dp)
                                .width(if (isSelected) 24.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) IndigoPrimary else BorderSubtleLight
                                )
                        )
                    }
                }

                Button(
                    onClick = {
                        if (currentPage < pages.size - 1) {
                            currentPage++
                        } else {
                            viewModel.navigateTo(Screen.Login)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("onboarding_next_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (currentPage == pages.size - 1) "शुरू करें / Get Started" else "आगे बढ़ें / Next",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = if (currentPage == pages.size - 1) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}
