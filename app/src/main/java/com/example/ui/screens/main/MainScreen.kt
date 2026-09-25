package com.example.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.testing.TestsScreen
import com.example.ui.theme.IndigoPrimary
import com.example.ui.viewmodel.VidyaViewModel

@Composable
fun MainScreen(viewModel: VidyaViewModel) {
    val selectedTab by viewModel.selectedBottomTab.collectAsState()

    val navItems = listOf(
        NavigationItem("Home", "होम", Icons.Filled.Home, Icons.Outlined.Home, "nav_home"),
        NavigationItem("Courses", "कोर्सेज", Icons.Filled.MenuBook, Icons.Outlined.MenuBook, "nav_courses"),
        NavigationItem("Batches", "बैचेस", Icons.Filled.Groups, Icons.Outlined.Groups, "nav_batches"),
        NavigationItem("Tests", "टेस्ट", Icons.Filled.Assignment, Icons.Outlined.Assignment, "nav_tests"),
        NavigationItem("Profile", "प्रोफाइल", Icons.Filled.Person, Icons.Outlined.Person, "nav_profile")
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 600.dp

        if (isWideScreen) {
            // Adaptive Tablet / Desktop Layout with Navigation Rail
            Row(modifier = Modifier.fillMaxSize()) {
                NavigationRail(
                    modifier = Modifier
                        .fillMaxHeight()
                        .testTag("main_navigation_rail"),
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    navItems.forEachIndexed { index, item ->
                        val isSelected = selectedTab == index
                        NavigationRailItem(
                            selected = isSelected,
                            onClick = { viewModel.setBottomTab(index) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.titleHindi, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            modifier = Modifier.testTag(item.tag)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    TabContent(selectedTab, viewModel)
                }
            }
        } else {
            // Mobile Phone Layout with Bottom Navigation Bar
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("main_scaffold"),
                bottomBar = {
                    NavigationBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .testTag("main_bottom_nav_bar"),
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 6.dp
                    ) {
                        navItems.forEachIndexed { index, item ->
                            val isSelected = selectedTab == index
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.setBottomTab(index) },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.titleHindi,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = IndigoPrimary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                modifier = Modifier.testTag(item.tag)
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    TabContent(selectedTab, viewModel)
                }
            }
        }
    }
}

@Composable
private fun TabContent(selectedTab: Int, viewModel: VidyaViewModel) {
    when (selectedTab) {
        0 -> HomeScreen(viewModel)
        1 -> CoursesScreen(viewModel)
        2 -> BatchesScreen(viewModel)
        3 -> TestsScreen(viewModel)
        4 -> ProfileScreen(viewModel)
    }
}

private data class NavigationItem(
    val title: String,
    val titleHindi: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val tag: String
)
