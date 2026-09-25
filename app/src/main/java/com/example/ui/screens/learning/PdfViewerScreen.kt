package com.example.ui.screens.learning

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.VidyaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

@Composable
fun PdfViewerScreen(viewModel: VidyaViewModel) {
    val note by viewModel.selectedNoteForViewing.collectAsState()
    var currentPage by remember { mutableIntStateOf(1) }
    var isNightMode by remember { mutableStateOf(false) }
    var zoomLevel by remember { mutableFloatStateOf(1.0f) }

    if (note == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Document not found")
        }
        return
    }

    val currentNote = note!!
    val totalPages = currentNote.pageCount

    Scaffold(
        topBar = {
            VidyaTopBar(
                title = currentNote.title,
                subtitle = "पृष्ठ $currentPage / $totalPages • ${currentNote.subject}",
                onBackClick = { viewModel.navigateTo(Screen.StudyNotes) },
                actions = {
                    IconButton(onClick = { isNightMode = !isNightMode }) {
                        Icon(
                            imageVector = if (isNightMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Night Mode"
                        )
                    }
                    IconButton(onClick = { viewModel.toggleNoteBookmark(currentNote) }) {
                        Icon(
                            imageVector = if (currentNote.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (currentNote.isBookmarked) SaffronDark else Color.Gray
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                tonalElevation = 6.dp,
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { if (currentPage > 1) currentPage-- },
                        enabled = currentPage > 1,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("पिछला / Prev")
                    }

                    Text(
                        text = "Page $currentPage of $totalPages",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )

                    Button(
                        onClick = { if (currentPage < totalPages) currentPage++ },
                        enabled = currentPage < totalPages,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("अगला / Next")
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(if (isNightMode) Color(0xFF18181B) else Color(0xFFF1F5F9))
                .testTag("pdf_viewer_screen"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Document Page Simulation Sheet
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isNightMode) Color(0xFF27272A) else Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                ) {
                    // Page Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "VIDYA-SETU ACADEMIC NOTES",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = IndigoPrimary
                        )
                        Text(
                            text = "JEE / NEET CRACKER SERIES",
                            fontSize = 10.sp,
                            color = SaffronDark,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 10.dp))

                    Text(
                        text = "${currentNote.chapterName} - Page $currentPage",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = if (isNightMode) Color.White else Color.Black
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Simulated Academic Handwritten/Typeset Content
                    Text(
                        text = "1. Fundamental Formulae & Theory:\n" +
                                "• Moment of Inertia for continuous body: I = ∫ r² dm\n" +
                                "• Parallel Axis Theorem: I = I_cm + M d²\n" +
                                "• Perpendicular Axis Theorem: I_z = I_x + I_y (Only for laminar planar bodies)\n" +
                                "• Kinetic Energy of Pure Rolling Body: K_total = 1/2 M v_cm² + 1/2 I_cm ω²\n" +
                                "• Work Energy Theorem: W_all = ΔK_rotational + ΔK_translational",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        lineHeight = 22.sp,
                        color = if (isNightMode) Color(0xFFE4E4E7) else Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Surface(
                        color = if (isNightMode) Color(0xFF3F3F46) else SaffronContainerLight,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "💡 Topper's Golden Tip (टॉपर की सलाह):",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = SaffronDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "घूर्णन गति के प्रश्नों में हमेशा संपर्क बिंदु (Point of Contact) के सापेक्ष टॉर्क निकालें, जिससे घर्षण बल (Friction force) का टॉर्क शून्य हो जाता है और गणना 2 गुना तेज़ हो जाती है!",
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                color = if (isNightMode) Color.White else Color(0xFF78350F)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "2. Solved Previous Year Question (JEE Advanced):\n" +
                                "Q: Find acceleration of a solid sphere rolling down a rough inclined plane of angle θ without slipping.\n" +
                                "Sol: a = g sin θ / (1 + k²/R²). For solid sphere k²/R² = 2/5.\n" +
                                "Hence a = 5/7 g sin θ.",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        lineHeight = 20.sp,
                        color = if (isNightMode) Color(0xFFE4E4E7) else Color(0xFF1E293B)
                    )
                }
            }
        }
    }
}
