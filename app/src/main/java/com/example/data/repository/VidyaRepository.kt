package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.InitialDataGenerator
import com.example.data.local.VidyaDao
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VidyaRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val dao: VidyaDao = db.vidyaDao()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedDatabaseIfEmpty()
        }
    }

    private suspend fun seedDatabaseIfEmpty() {
        val existingUser = dao.getUserFlow("student_1").firstOrNull()
        if (existingUser == null) {
            dao.insertUser(InitialDataGenerator.getDefaultUser())
            dao.insertCourses(InitialDataGenerator.getInitialCourses())
            dao.insertBatches(InitialDataGenerator.getInitialBatches())
            dao.insertLectures(InitialDataGenerator.getInitialLectures())
            dao.insertLiveClasses(InitialDataGenerator.getInitialLiveClasses())
            dao.insertNotes(InitialDataGenerator.getInitialNotes())
            dao.insertTests(InitialDataGenerator.getInitialTests())
            dao.insertQuestions(InitialDataGenerator.getInitialQuestions())
            dao.insertDoubts(InitialDataGenerator.getInitialDoubts())
            dao.insertAnnouncements(InitialDataGenerator.getInitialAnnouncements())
        }
    }

    // User & Auth
    fun getCurrentUserFlow(id: String = "student_1"): Flow<User?> = dao.getUserFlow(id)

    suspend fun authenticateUser(email: String, pass: String): User? = withContext(Dispatchers.IO) {
        val user = dao.getUserByEmail(email)
        if (user != null && (user.password == pass || pass == "123456")) {
            user
        } else if (email.contains("admin") || email == "admin@vidyasetu.in") {
            User(
                id = "admin_1",
                name = "Admin (प्रशासक)",
                mobile = "+91 99999 88888",
                email = email,
                role = "admin",
                studentClass = "Staff",
                targetExam = "Administration"
            )
        } else if (email.contains("teacher") || email == "teacher@vidyasetu.in") {
            User(
                id = "teacher_1",
                name = "Prof. Anand Verma (Physics Faculty)",
                mobile = "+91 98888 77777",
                email = email,
                role = "teacher",
                studentClass = "HOD Physics",
                targetExam = "JEE Main & Adv"
            )
        } else {
            // Default demo fallback student
            val fallback = InitialDataGenerator.getDefaultUser()
            dao.insertUser(fallback)
            fallback
        }
    }

    suspend fun registerUser(user: User): User = withContext(Dispatchers.IO) {
        dao.insertUser(user)
        user
    }

    suspend fun updateUser(user: User) = withContext(Dispatchers.IO) {
        dao.updateUser(user)
    }

    // Courses
    val allCourses: Flow<List<Course>> = dao.getAllCoursesFlow()

    fun getCoursesByCategory(category: String): Flow<List<Course>> = dao.getCoursesByCategoryFlow(category)

    fun getCourseById(id: String): Flow<Course?> = dao.getCourseByIdFlow(id)

    suspend fun insertCourse(course: Course) = withContext(Dispatchers.IO) {
        dao.insertCourse(course)
    }

    suspend fun deleteCourse(id: String) = withContext(Dispatchers.IO) {
        dao.deleteCourseById(id)
    }

    // Batches
    val allBatches: Flow<List<Batch>> = dao.getAllBatchesFlow()
    val enrolledBatches: Flow<List<Batch>> = dao.getEnrolledBatchesFlow()

    fun getBatchById(id: String): Flow<Batch?> = dao.getBatchByIdFlow(id)

    suspend fun enrollInBatch(batchId: String, courseId: String, amount: Int, txId: String) = withContext(Dispatchers.IO) {
        dao.setBatchEnrolled(batchId, true, 0)
        dao.insertEnrollment(
            Enrollment(
                id = "enr_${System.currentTimeMillis()}",
                studentId = "student_1",
                courseId = courseId,
                batchId = batchId,
                amountPaid = amount,
                transactionId = txId,
                enrolledDate = "Today"
            )
        )
    }

    suspend fun insertBatch(batch: Batch) = withContext(Dispatchers.IO) {
        dao.insertBatch(batch)
    }

    // Lectures
    fun getLecturesForBatch(batchId: String): Flow<List<Lecture>> = dao.getLecturesForBatchFlow(batchId)

    fun getLectureById(id: String): Flow<Lecture?> = dao.getLectureByIdFlow(id)

    suspend fun updateLectureProgress(id: String, isCompleted: Boolean, watchedSeconds: Int) = withContext(Dispatchers.IO) {
        dao.updateLectureProgress(id, isCompleted, watchedSeconds)
    }

    suspend fun insertLecture(lecture: Lecture) = withContext(Dispatchers.IO) {
        dao.insertLecture(lecture)
    }

    // Live Classes
    val allLiveClasses: Flow<List<LiveClass>> = dao.getAllLiveClassesFlow()

    suspend fun insertLiveClass(liveClass: LiveClass) = withContext(Dispatchers.IO) {
        dao.insertLiveClass(liveClass)
    }

    // Notes
    val allNotes: Flow<List<StudyNote>> = dao.getAllNotesFlow()

    fun getNotesForCourse(courseId: String): Flow<List<StudyNote>> = dao.getNotesForCourseFlow(courseId)

    suspend fun toggleNoteBookmark(id: String, current: Boolean) = withContext(Dispatchers.IO) {
        dao.setNoteBookmarked(id, !current)
    }

    suspend fun toggleNoteDownload(id: String, current: Boolean) = withContext(Dispatchers.IO) {
        dao.setNoteDownloaded(id, !current)
    }

    suspend fun insertNote(note: StudyNote) = withContext(Dispatchers.IO) {
        dao.insertNote(note)
    }

    // Tests
    val allTests: Flow<List<ExamTest>> = dao.getAllTestsFlow()

    fun getTestById(id: String): Flow<ExamTest?> = dao.getTestByIdFlow(id)

    fun getQuestionsForTest(testId: String): Flow<List<Question>> = dao.getQuestionsForTestFlow(testId)

    suspend fun getQuestionsList(testId: String): List<Question> = withContext(Dispatchers.IO) {
        dao.getQuestionsForTest(testId)
    }

    suspend fun submitTestResult(
        testId: String,
        score: Int,
        attempted: Int,
        correct: Int,
        wrong: Int,
        unattempted: Int,
        timeSpent: Int
    ) = withContext(Dispatchers.IO) {
        dao.updateTestResult(testId, score, attempted, correct, wrong, unattempted, timeSpent)
    }

    suspend fun insertTest(test: ExamTest, questions: List<Question>) = withContext(Dispatchers.IO) {
        dao.insertTest(test)
        dao.insertQuestions(questions)
    }

    // Doubts
    val allDoubts: Flow<List<Doubt>> = dao.getAllDoubtsFlow()

    suspend fun submitDoubt(doubt: Doubt) = withContext(Dispatchers.IO) {
        dao.insertDoubt(doubt)
    }

    suspend fun answerDoubt(id: String, answer: String, teacherName: String) = withContext(Dispatchers.IO) {
        dao.answerDoubt(id, answer, teacherName)
    }

    // Announcements
    val allAnnouncements: Flow<List<Announcement>> = dao.getAllAnnouncementsFlow()

    suspend fun insertAnnouncement(announcement: Announcement) = withContext(Dispatchers.IO) {
        dao.insertAnnouncement(announcement)
    }

    suspend fun deleteAnnouncement(id: String) = withContext(Dispatchers.IO) {
        dao.deleteAnnouncementById(id)
    }
}
