package com.example.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Course
import com.example.ui.components.VidyaTopBar
import com.example.ui.theme.IndigoPrimary
import com.example.ui.viewmodel.VidyaViewModel

@Composable
fun CoursesScreen(viewModel: VidyaViewModel) {
    val allCourses by viewModel.allCourses.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }
    var filterQuery by remember { mutableStateOf("") }
    var priceFilter by remember { mutableStateOf("All") } // "All", "Free", "Paid"

    val categories = listOf(
        "All", "Class 6", "Class 7", "Class 8", "Class 9", "Class 10",
        "Class 11", "Class 12", "JEE", "NEET", "CUET", "SSC", "Railway"
    )

    val filteredCourses = allCourses.filter { course ->
        val matchesCategory = selectedCategory == "All" || course.category.contains(selectedCategory, ignoreCase = true)
        val matchesQuery = filterQuery.isBlank() ||
                course.title.contains(filterQuery, ignoreCase = true) ||
                course.subject.contains(filterQuery, ignoreCase = true) ||
                course.teacherName.contains(filterQuery, ignoreCase = true)
        val matchesPrice = when (priceFilter) {
            "Free" -> course.isFree
            "Paid" -> !course.isFree
            else -> true
        }
        matchesCategory && matchesQuery && matchesPrice
    }

    Scaffold(
        topBar = {
            VidyaTopBar(
                title = "पाठ्यक्रम / All Courses",
                subtitle = "कक्षा 6–12, JEE, NEET, CUET एवं सरकारी परीक्षाएं"
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("courses_screen_root")
        ) {
            // Search Input
            OutlinedTextField(
                value = filterQuery,
                onValueChange = { filterQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("courses_search_field"),
                placeholder = { Text("कोर्स या विषय का नाम खोजें...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (filterQuery.isNotBlank()) {
                        IconButton(onClick = { filterQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Categories horizontal list
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp, fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.testTag("category_chip_$cat")
                    )
                }
            }

            // Price filter row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredCourses.size} कोर्सेज उपलब्ध हैं",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("All", "Paid", "Free").forEach { p ->
                        AssistChip(
                            onClick = { priceFilter = p },
                            label = { Text(p, fontSize = 11.sp) },
                            colors = if (priceFilter == p) AssistChipDefaults.assistChipColors(containerColor = IndigoPrimary.copy(alpha = 0.15f)) else AssistChipDefaults.assistChipColors()
                        )
                    }
                }
            }

            if (filteredCourses.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔍", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "कोई कोर्स नहीं मिला / No courses found",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "कृपया अन्य श्रेणी या फ़िल्टर का चयन करें।",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(filteredCourses) { course ->
                        CourseWideCard(course = course, onSelect = { viewModel.selectCourse(course) })
                    }
                }
            }
        }
    }
}

@Composable
fun CourseWideCard(course: Course, onSelect: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("course_wide_card_${course.id}"),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${course.category} • ${course.subject}",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = course.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Text(
                        text = "${course.teacherName} (${course.teacherTitle})",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("★ ${course.rating}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = course.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                ),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("📺 ${course.lecturesCount} लेक्चर्स", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    Text("📝 ${course.testsCount} टेस्ट्स", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (course.isFree) {
                        Text(
                            text = "निःशुल्क / FREE",
                            fontWeight = FontWeight.ExtraBold,
                            color = com.example.ui.theme.SuccessGreen,
                            fontSize = 15.sp
                        )
                    } else {
                        Text(
                            text = "₹${course.priceRupees}",
                            fontWeight = FontWeight.ExtraBold,
                            color = IndigoPrimary,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "₹${course.originalPriceRupees}",
                            fontSize = 12.sp,
                            color = androidx.compose.ui.graphics.Color.Gray,
                            style = androidx.compose.ui.text.TextStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = onSelect,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(text = "देखें / Enroll", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
