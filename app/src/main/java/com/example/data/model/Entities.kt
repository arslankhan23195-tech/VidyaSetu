package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = "student_1",
    val name: String,
    val mobile: String,
    val email: String,
    val password: String = "123456",
    val targetExam: String = "JEE Main & Advanced 2027",
    val studentClass: String = "Class 11",
    val preferredLanguage: String = "Hindi", // Hindi, English, Hinglish
    val role: String = "student", // "student", "admin", "teacher"
    val avatarUrl: String = "",
    val streakDays: Int = 7,
    val totalStudyMinutes: Int = 1840,
    val totalLecturesWatched: Int = 34,
    val testsCompleted: Int = 12
)

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey val id: String,
    val title: String,
    val titleHindi: String,
    val category: String, // "JEE", "NEET", "Class 10", "Class 12", "CUET", "SSC", "Railway"
    val subject: String, // "Physics", "Chemistry", "Mathematics", "Biology", "All Subjects"
    val targetExam: String,
    val teacherName: String,
    val teacherTitle: String,
    val rating: Float = 4.8f,
    val reviewCount: Int = 1240,
    val lecturesCount: Int = 120,
    val testsCount: Int = 24,
    val priceRupees: Int = 2499,
    val originalPriceRupees: Int = 6999,
    val isFree: Boolean = false,
    val bannerDrawableName: String = "banner_vidyasetu_study_1790310899912",
    val description: String,
    val syllabusSummary: String,
    val isFeatured: Boolean = false
)

@Entity(tableName = "batches")
data class Batch(
    @PrimaryKey val id: String,
    val courseId: String,
    val title: String,
    val titleHindi: String,
    val targetExam: String,
    val startDate: String,
    val validity: String,
    val mediumLanguage: String, // "Hinglish", "Hindi", "English"
    val teachersList: String, // Comma-separated or readable text
    val subjectsList: String,
    val priceRupees: Int,
    val originalPriceRupees: Int,
    val lectureCount: Int,
    val testCount: Int,
    val notesCount: Int,
    val enrolledCount: Int,
    val isEnrolled: Boolean = false,
    val progressPercent: Int = 0,
    val bannerDrawableName: String = "banner_vidyasetu_study_1790310899912"
)

@Entity(tableName = "lectures")
data class Lecture(
    @PrimaryKey val id: String,
    val batchId: String,
    val courseId: String,
    val title: String,
    val titleHindi: String,
    val subject: String,
    val teacherName: String,
    val durationMinutes: Int,
    val videoUrl: String,
    val orderIndex: Int,
    val isCompleted: Boolean = false,
    val watchedSeconds: Int = 0,
    val notesPdfUrl: String = "",
    val isFreePreview: Boolean = false,
    val chapterName: String = "Chapter 1"
)

@Entity(tableName = "live_classes")
data class LiveClass(
    @PrimaryKey val id: String,
    val batchId: String,
    val title: String,
    val titleHindi: String,
    val teacherName: String,
    val subject: String,
    val scheduledTime: String, // e.g. "Today, 6:00 PM"
    val durationMinutes: Int = 60,
    val isLiveNow: Boolean = false,
    val isCompleted: Boolean = false,
    val attendeeCount: Int = 1420,
    val recordingUrl: String = ""
)

@Entity(tableName = "study_notes")
data class StudyNote(
    @PrimaryKey val id: String,
    val courseId: String,
    val batchId: String,
    val subject: String,
    val chapterName: String,
    val title: String,
    val titleHindi: String,
    val pageCount: Int,
    val fileSizeMb: Float,
    val isBookmarked: Boolean = false,
    val isDownloaded: Boolean = false
)

@Entity(tableName = "exam_tests")
data class ExamTest(
    @PrimaryKey val id: String,
    val courseId: String,
    val batchId: String,
    val title: String,
    val titleHindi: String,
    val targetExam: String,
    val subject: String,
    val totalQuestions: Int,
    val durationMinutes: Int,
    val totalMarks: Int,
    val difficulty: String = "Medium", // "Easy", "Medium", "Hard"
    val isAttempted: Boolean = false,
    val lastScore: Int = 0,
    val totalAttempted: Int = 0,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val unattemptedCount: Int = 0,
    val timeSpentSeconds: Int = 0
)

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey val id: String,
    val testId: String,
    val questionIndex: Int,
    val questionText: String,
    val questionTextHindi: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOption: Int, // 1 for A, 2 for B, 3 for C, 4 for D
    val explanationText: String,
    val subject: String
)

@Entity(tableName = "doubts")
data class Doubt(
    @PrimaryKey val id: String,
    val studentId: String = "student_1",
    val studentName: String = "राहुल शर्मा",
    val subject: String,
    val chapter: String,
    val questionText: String,
    val imageUrl: String = "",
    val status: String = "PENDING", // "PENDING", "ANSWERED"
    val teacherAnswer: String = "",
    val teacherName: String = "डॉ. वी. के. सिंह (HOD Physics)",
    val timestamp: String = "Just now"
)

@Entity(tableName = "announcements")
data class Announcement(
    @PrimaryKey val id: String,
    val title: String,
    val titleHindi: String,
    val message: String,
    val targetBatch: String = "All Batches",
    val priority: String = "NORMAL", // "URGENT", "NORMAL", "EXAM_ALERT"
    val timestamp: String = "Today, 10:30 AM",
    val authorName: String = "VidyaSetu Academic Cell"
)

@Entity(tableName = "enrollments")
data class Enrollment(
    @PrimaryKey val id: String,
    val studentId: String,
    val courseId: String,
    val batchId: String,
    val amountPaid: Int,
    val transactionId: String,
    val enrolledDate: String
)
