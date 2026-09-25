package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VidyaDao {

    // Users
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserFlow(id: String): Flow<User?>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    // Courses
    @Query("SELECT * FROM courses ORDER BY rating DESC")
    fun getAllCoursesFlow(): Flow<List<Course>>

    @Query("SELECT * FROM courses WHERE category = :category")
    fun getCoursesByCategoryFlow(category: String): Flow<List<Course>>

    @Query("SELECT * FROM courses WHERE id = :id LIMIT 1")
    fun getCourseByIdFlow(id: String): Flow<Course?>

    @Query("SELECT * FROM courses WHERE id = :id LIMIT 1")
    suspend fun getCourseById(id: String): Course?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<Course>)

    @Query("DELETE FROM courses WHERE id = :id")
    suspend fun deleteCourseById(id: String)

    // Batches
    @Query("SELECT * FROM batches")
    fun getAllBatchesFlow(): Flow<List<Batch>>

    @Query("SELECT * FROM batches WHERE isEnrolled = 1")
    fun getEnrolledBatchesFlow(): Flow<List<Batch>>

    @Query("SELECT * FROM batches WHERE id = :id LIMIT 1")
    fun getBatchByIdFlow(id: String): Flow<Batch?>

    @Query("SELECT * FROM batches WHERE id = :id LIMIT 1")
    suspend fun getBatchById(id: String): Batch?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatches(batches: List<Batch>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatch(batch: Batch)

    @Query("UPDATE batches SET isEnrolled = :enrolled, progressPercent = :progress WHERE id = :id")
    suspend fun setBatchEnrolled(id: String, enrolled: Boolean, progress: Int)

    // Lectures
    @Query("SELECT * FROM lectures WHERE batchId = :batchId ORDER BY orderIndex ASC")
    fun getLecturesForBatchFlow(batchId: String): Flow<List<Lecture>>

    @Query("SELECT * FROM lectures WHERE id = :id LIMIT 1")
    fun getLectureByIdFlow(id: String): Flow<Lecture?>

    @Query("SELECT * FROM lectures WHERE id = :id LIMIT 1")
    suspend fun getLectureById(id: String): Lecture?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLectures(lectures: List<Lecture>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLecture(lecture: Lecture)

    @Query("UPDATE lectures SET isCompleted = :completed, watchedSeconds = :seconds WHERE id = :id")
    suspend fun updateLectureProgress(id: String, completed: Boolean, seconds: Int)

    // Live Classes
    @Query("SELECT * FROM live_classes ORDER BY isLiveNow DESC")
    fun getAllLiveClassesFlow(): Flow<List<LiveClass>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLiveClasses(classes: List<LiveClass>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLiveClass(liveClass: LiveClass)

    // Study Notes
    @Query("SELECT * FROM study_notes")
    fun getAllNotesFlow(): Flow<List<StudyNote>>

    @Query("SELECT * FROM study_notes WHERE courseId = :courseId")
    fun getNotesForCourseFlow(courseId: String): Flow<List<StudyNote>>

    @Query("UPDATE study_notes SET isBookmarked = :bookmarked WHERE id = :id")
    suspend fun setNoteBookmarked(id: String, bookmarked: Boolean)

    @Query("UPDATE study_notes SET isDownloaded = :downloaded WHERE id = :id")
    suspend fun setNoteDownloaded(id: String, downloaded: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<StudyNote>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: StudyNote)

    // Exam Tests
    @Query("SELECT * FROM exam_tests")
    fun getAllTestsFlow(): Flow<List<ExamTest>>

    @Query("SELECT * FROM exam_tests WHERE id = :id LIMIT 1")
    fun getTestByIdFlow(id: String): Flow<ExamTest?>

    @Query("SELECT * FROM exam_tests WHERE id = :id LIMIT 1")
    suspend fun getTestById(id: String): ExamTest?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTests(tests: List<ExamTest>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTest(test: ExamTest)

    @Query("UPDATE exam_tests SET isAttempted = 1, lastScore = :score, totalAttempted = :attempted, correctCount = :correct, wrongCount = :wrong, unattemptedCount = :unattempted, timeSpentSeconds = :timeSpent WHERE id = :id")
    suspend fun updateTestResult(id: String, score: Int, attempted: Int, correct: Int, wrong: Int, unattempted: Int, timeSpent: Int)

    // Questions
    @Query("SELECT * FROM questions WHERE testId = :testId ORDER BY questionIndex ASC")
    fun getQuestionsForTestFlow(testId: String): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE testId = :testId ORDER BY questionIndex ASC")
    suspend fun getQuestionsForTest(testId: String): List<Question>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: Question)

    // Doubts
    @Query("SELECT * FROM doubts ORDER BY timestamp DESC")
    fun getAllDoubtsFlow(): Flow<List<Doubt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoubt(doubt: Doubt)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoubts(doubts: List<Doubt>)

    @Query("UPDATE doubts SET status = 'ANSWERED', teacherAnswer = :answer, teacherName = :teacherName WHERE id = :id")
    suspend fun answerDoubt(id: String, answer: String, teacherName: String)

    // Announcements
    @Query("SELECT * FROM announcements ORDER BY id DESC")
    fun getAllAnnouncementsFlow(): Flow<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncements(announcements: List<Announcement>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: Announcement)

    @Query("DELETE FROM announcements WHERE id = :id")
    suspend fun deleteAnnouncementById(id: String)

    // Enrollments
    @Query("SELECT * FROM enrollments WHERE studentId = :studentId")
    fun getEnrollmentsFlow(studentId: String): Flow<List<Enrollment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnrollment(enrollment: Enrollment)
}
