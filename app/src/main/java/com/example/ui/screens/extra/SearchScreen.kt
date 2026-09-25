package com.example.ui.screens.extra

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.VidyaTopBar
import com.example.ui.theme.IndigoPrimary
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

@Composable
fun SearchScreen(viewModel: VidyaViewModel) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val allCourses by viewModel.allCourses.collectAsState()
    val allBatches by viewModel.allBatches.collectAsState()
    val allNotes by viewModel.allNotes.collectAsState()
    val allTests by viewModel.allTests.collectAsState()

    val matchedCourses = if (searchQuery.isNotBlank()) {
        allCourses.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.teacherName.contains(searchQuery, ignoreCase = true) ||
            it.subject.contains(searchQuery, ignoreCase = true) ||
            it.category.contains(searchQuery, ignoreCase = true)
        }
    } else emptyList()

    val matchedBatches = if (searchQuery.isNotBlank()) {
        allBatches.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.teachersList.contains(searchQuery, ignoreCase = true) ||
            it.targetExam.contains(searchQuery, ignoreCase = true)
        }
    } else emptyList()

    val matchedNotes = if (searchQuery.isNotBlank()) {
        allNotes.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.chapterName.contains(searchQuery, ignoreCase = true) ||
            it.subject.contains(searchQuery, ignoreCase = true)
        }
    } else emptyList()

    val matchedTests = if (searchQuery.isNotBlank()) {
        allTests.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.subject.contains(searchQuery, ignoreCase = true)
        }
    } else emptyList()

    Scaffold(
        topBar = {
            VidyaTopBar(
                title = "वैश्विक खोज / Global Search",
                subtitle = "कोर्स, शिक्षक, नोट्स व टेस्ट्स खोजें",
                onBackClick = { viewModel.navigateTo(Screen.Main) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("search_screen_root")
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("global_search_input"),
                placeholder = { Text("उदाहरण: Physics, Anand Verma, JEE, Calculus...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            if (searchQuery.isBlank()) {
                // Popular Searches suggestions
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("लोकप्रिय सर्च / Popular Searches", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    listOf(
                        "JEE 2027", "Rotational Dynamics", "Organic Chemistry",
                        "NEET Biology", "Prof. Anand Verma", "Class 10 Board", "Formula Sheets"
                    ).forEach { suggestion ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.updateSearchQuery(suggestion) }
                                .padding(vertical = 6.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(suggestion, fontSize = 13.sp)
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
                    if (matchedCourses.isNotEmpty()) {
                        item {
                            Text("कोर्सेज / Courses (${matchedCourses.size})", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        items(matchedCourses) { course ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.selectCourse(course) },
                                shape = RoundedCornerShape(10.dp),
                                border = CardDefaults.outlinedCardBorder()
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = IndigoPrimary)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(course.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text("${course.teacherName} • ${course.category}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }

                    if (matchedBatches.isNotEmpty()) {
                        item {
                            Text("बैचेस / Batches (${matchedBatches.size})", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        items(matchedBatches) { batch ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.selectBatch(batch) },
                                shape = RoundedCornerShape(10.dp),
                                border = CardDefaults.outlinedCardBorder()
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Groups, contentDescription = null, tint = com.example.ui.theme.SaffronDark)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(batch.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(batch.targetExam, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }

                    if (matchedNotes.isNotEmpty()) {
                        item {
                            Text("अध्ययन नोट्स / Notes (${matchedNotes.size})", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        items(matchedNotes) { note ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.openNotePdf(note) },
                                shape = RoundedCornerShape(10.dp),
                                border = CardDefaults.outlinedCardBorder()
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = com.example.ui.theme.EmeraldTertiary)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(note.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text("${note.subject} • ${note.chapterName}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }

                    if (matchedTests.isNotEmpty()) {
                        item {
                            Text("टेस्ट्स / Tests (${matchedTests.size})", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        items(matchedTests) { test ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.startTest(test) },
                                shape = RoundedCornerShape(10.dp),
                                border = CardDefaults.outlinedCardBorder()
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Assignment, contentDescription = null, tint = IndigoPrimary)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(test.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text("${test.totalQuestions} Questions • ${test.durationMinutes} mins", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
