package com.example.ui.screens.learning

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.model.StudyNote
import com.example.ui.components.VidyaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

@Composable
fun StudyNotesScreen(viewModel: VidyaViewModel) {
    val notes by viewModel.allNotes.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf("All") }

    val subjects = listOf("All", "Physics", "Chemistry", "Mathematics", "Biology")

    val filteredNotes = notes.filter { note ->
        val matchesSubject = selectedSubject == "All" || note.subject.equals(selectedSubject, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
                note.title.contains(searchQuery, ignoreCase = true) ||
                note.chapterName.contains(searchQuery, ignoreCase = true)
        matchesSubject && matchesSearch
    }

    Scaffold(
        topBar = {
            VidyaTopBar(
                title = "अध्ययन सामग्री / Study Notes & PDF",
                subtitle = "हस्तलिखित नोट्स, फॉर्मूला शीट्स एवं माइंड मैप्स",
                onBackClick = { viewModel.navigateTo(Screen.Main) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("study_notes_screen")
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                placeholder = { Text("अध्याय या विषय का नाम खोजें...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Subject Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                subjects.forEach { subj ->
                    FilterChip(
                        selected = selectedSubject == subj,
                        onClick = { selectedSubject = subj },
                        label = { Text(subj, fontSize = 11.sp) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredNotes) { note ->
                    NoteCardItem(
                        note = note,
                        onOpen = { viewModel.openNotePdf(note) },
                        onBookmark = { viewModel.toggleNoteBookmark(note) },
                        onDownload = { viewModel.toggleNoteDownload(note) }
                    )
                }
            }
        }
    }
}

@Composable
fun NoteCardItem(
    note: StudyNote,
    onOpen: () -> Unit,
    onBookmark: () -> Unit,
    onDownload: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpen)
            .testTag("note_card_${note.id}"),
        shape = RoundedCornerShape(14.dp),
        border = CardDefaults.outlinedCardBorder(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(EmeraldContainerLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PictureAsPdf,
                    contentDescription = null,
                    tint = EmeraldTertiary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "${note.subject} • ${note.chapterName}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = note.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 2
                )
                Text(
                    text = "${note.pageCount} पृष्ठ • ${note.fileSizeMb} MB",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row {
                IconButton(onClick = onBookmark) {
                    Icon(
                        imageVector = if (note.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (note.isBookmarked) SaffronDark else Color.Gray
                    )
                }

                IconButton(onClick = onDownload) {
                    Icon(
                        imageVector = if (note.isDownloaded) Icons.Default.CheckCircle else Icons.Default.Download,
                        contentDescription = "Download",
                        tint = if (note.isDownloaded) SuccessGreen else IndigoPrimary
                    )
                }
            }
        }
    }
}
