package com.example.ui.screens.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(viewModel: VidyaViewModel) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        delay(2200)
        viewModel.navigateTo(Screen.Onboarding)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        IndigoPrimaryDark,
                        IndigoPrimary,
                        Color(0xFF0F172A)
                    )
                )
            )
            .testTag("splash_screen_root"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .scale(scale)
                    .clip(RoundedCornerShape(26.dp))
                    .background(Color.White.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_vidyasetu_logo_1790310886429),
                    contentDescription = "VidyaSetu Logo",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "VidyaSetu",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = SaffronSecondary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(20.dp),
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Text(
                    text = "पढ़ाई का सही सेतु",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = SaffronSecondary
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Class 6-12 • JEE • NEET • CUET • Govt Exams",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            CircularProgressIndicator(
                color = SaffronSecondary,
                strokeWidth = 3.dp,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
