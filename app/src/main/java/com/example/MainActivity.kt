package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.admin.TeacherDashboardScreen
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.auth.OnboardingScreen
import com.example.ui.screens.auth.RegisterScreen
import com.example.ui.screens.auth.SplashScreen
import com.example.ui.screens.doubts.DoubtsScreen
import com.example.ui.screens.extra.*
import com.example.ui.screens.learning.*
import com.example.ui.screens.main.MainScreen
import com.example.ui.screens.testing.TestCbtScreen
import com.example.ui.screens.testing.TestResultScreen
import com.example.ui.screens.testing.TestsScreen
import com.example.ui.theme.VidyaSetuTheme
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: VidyaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VidyaSetuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigationHost(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigationHost(viewModel: VidyaViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val selectedTab by viewModel.selectedBottomTab.collectAsState()

    // Handle system back navigation gracefully
    BackHandler(enabled = currentScreen != Screen.Splash && currentScreen != Screen.Main) {
        when (currentScreen) {
            Screen.CourseDetail, Screen.BatchDetail, Screen.LiveClasses,
            Screen.StudyNotes, Screen.TestList, Screen.Doubts,
            Screen.Announcements, Screen.ProgressAnalytics, Screen.Search,
            Screen.Notifications -> {
                viewModel.navigateTo(Screen.Main)
            }
            Screen.LecturePlayer -> {
                viewModel.navigateTo(Screen.BatchDetail)
            }
            Screen.PdfViewer -> {
                viewModel.navigateTo(Screen.StudyNotes)
            }
            Screen.TestResult -> {
                viewModel.navigateTo(Screen.Main)
            }
            Screen.Checkout -> {
                viewModel.navigateTo(Screen.BatchDetail)
            }
            Screen.Register -> {
                viewModel.navigateTo(Screen.Login)
            }
            Screen.AdminDashboard, Screen.TeacherDashboard -> {
                viewModel.switchUserRole("student")
            }
            else -> {
                if (selectedTab != 0) {
                    viewModel.setBottomTab(0)
                }
            }
        }
    }

    when (currentScreen) {
        Screen.Splash -> SplashScreen(viewModel)
        Screen.Onboarding -> OnboardingScreen(viewModel)
        Screen.Login -> LoginScreen(viewModel)
        Screen.Register -> RegisterScreen(viewModel)
        Screen.Main -> MainScreen(viewModel)
        Screen.CourseDetail -> CourseDetailScreen(viewModel)
        Screen.BatchDetail -> BatchDetailScreen(viewModel)
        Screen.LecturePlayer -> LecturePlayerScreen(viewModel)
        Screen.LiveClasses -> LiveClassesScreen(viewModel)
        Screen.StudyNotes -> StudyNotesScreen(viewModel)
        Screen.PdfViewer -> PdfViewerScreen(viewModel)
        Screen.TestList -> TestsScreen(viewModel)
        Screen.TestCbtTaking -> TestCbtScreen(viewModel)
        Screen.TestResult -> TestResultScreen(viewModel)
        Screen.Doubts -> DoubtsScreen(viewModel)
        Screen.Announcements -> AnnouncementsScreen(viewModel)
        Screen.ProgressAnalytics -> ProgressAnalyticsScreen(viewModel)
        Screen.Checkout -> CheckoutScreen(viewModel)
        Screen.AdminDashboard -> AdminDashboardScreen(viewModel)
        Screen.TeacherDashboard -> TeacherDashboardScreen(viewModel)
        Screen.Search -> SearchScreen(viewModel)
        Screen.Notifications -> NotificationsScreen(viewModel)
    }
}
