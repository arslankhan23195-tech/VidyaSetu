package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.InitialDataGenerator
import com.example.data.model.*
import com.example.data.repository.VidyaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object Main : Screen("main")
    object CourseDetail : Screen("course_detail")
    object BatchDetail : Screen("batch_detail")
    object LecturePlayer : Screen("lecture_player")
    object LiveClasses : Screen("live_classes")
    object StudyNotes : Screen("study_notes")
    object PdfViewer : Screen("pdf_viewer")
    object TestList : Screen("test_list")
    object TestCbtTaking : Screen("test_cbt_taking")
    object TestResult : Screen("test_result")
    object Doubts : Screen("doubts")
    object Announcements : Screen("announcements")
    object ProgressAnalytics : Screen("progress_analytics")
    object Checkout : Screen("checkout")
    object AdminDashboard : Screen("admin_dashboard")
    object TeacherDashboard : Screen("teacher_dashboard")
    object Search : Screen("search")
    object Notifications : Screen("notifications")
}

data class TestTakingState(
    val test: ExamTest? = null,
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedOptions: Map<String, Int> = emptyMap(), // questionId -> option (1..4)
    val markedForReview: Set<String> = emptySet(),
    val remainingSeconds: Int = 1200,
    val isSubmitted: Boolean = false,
    val score: Int = 0,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val unattemptedCount: Int = 0,
    val totalMarks: Int = 0
)

class VidyaViewModel(application: Application) : AndroidViewModel(application) {

    val repository = VidyaRepository(application)

    // Current Screen
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Splash)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Current Bottom Nav Tab in Main screen (0: Home, 1: Courses, 2: Batches, 3: Tests, 4: Profile)
    private val _selectedBottomTab = MutableStateFlow(0)
    val selectedBottomTab: StateFlow<Int> = _selectedBottomTab.asStateFlow()

    // Current User
    val currentUser: StateFlow<User?> = repository.getCurrentUserFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitialDataGenerator.getDefaultUser())

    // Language Preference
    private val _appLanguage = MutableStateFlow("Hindi") // "Hindi", "English", "Hinglish"
    val appLanguage: StateFlow<String> = _appLanguage.asStateFlow()

    // Courses & Filtering
    val allCourses: StateFlow<List<Course>> = repository.allCourses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedCourseCategory = MutableStateFlow("All")
    val selectedCourseCategory: StateFlow<String> = _selectedCourseCategory.asStateFlow()

    // Batches
    val allBatches: StateFlow<List<Batch>> = repository.allBatches
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val enrolledBatches: StateFlow<List<Batch>> = repository.enrolledBatches
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected Course & Batch for Details
    private val _selectedCourse = MutableStateFlow<Course?>(null)
    val selectedCourse: StateFlow<Course?> = _selectedCourse.asStateFlow()

    private val _selectedBatch = MutableStateFlow<Batch?>(null)
    val selectedBatch: StateFlow<Batch?> = _selectedBatch.asStateFlow()

    // Lectures
    private val _currentBatchLectures = MutableStateFlow<List<Lecture>>(emptyList())
    val currentBatchLectures: StateFlow<List<Lecture>> = _currentBatchLectures.asStateFlow()

    private val _activeLecture = MutableStateFlow<Lecture?>(null)
    val activeLecture: StateFlow<Lecture?> = _activeLecture.asStateFlow()

    // Video Player State
    private val _isPlaying = MutableStateFlow(true)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private val _videoQuality = MutableStateFlow("720p")
    val videoQuality: StateFlow<String> = _videoQuality.asStateFlow()

    private val _videoProgress = MutableStateFlow(0.35f)
    val videoProgress: StateFlow<Float> = _videoProgress.asStateFlow()

    // Live Classes
    val allLiveClasses: StateFlow<List<LiveClass>> = repository.allLiveClasses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Study Notes & PDF
    val allNotes: StateFlow<List<StudyNote>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedNoteForViewing = MutableStateFlow<StudyNote?>(null)
    val selectedNoteForViewing: StateFlow<StudyNote?> = _selectedNoteForViewing.asStateFlow()

    // Tests
    val allTests: StateFlow<List<ExamTest>> = repository.allTests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // CBT Test Taking State
    private val _cbtState = MutableStateFlow(TestTakingState())
    val cbtState: StateFlow<TestTakingState> = _cbtState.asStateFlow()
    private var testTimerJob: Job? = null

    // Doubts
    val allDoubts: StateFlow<List<Doubt>> = repository.allDoubts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Announcements
    val allAnnouncements: StateFlow<List<Announcement>> = repository.allAnnouncements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Global Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Checkout State
    private val _appliedCoupon = MutableStateFlow("")
    val appliedCoupon: StateFlow<String> = _appliedCoupon.asStateFlow()

    private val _couponDiscountRupees = MutableStateFlow(0)
    val couponDiscountRupees: StateFlow<Int> = _couponDiscountRupees.asStateFlow()

    private val _isPaymentProcessing = MutableStateFlow(false)
    val isPaymentProcessing: StateFlow<Boolean> = _isPaymentProcessing.asStateFlow()

    private val _paymentSuccess = MutableStateFlow(false)
    val paymentSuccess: StateFlow<Boolean> = _paymentSuccess.asStateFlow()

    // Navigation helper
    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun setBottomTab(index: Int) {
        _selectedBottomTab.value = index
    }

    fun setLanguage(lang: String) {
        _appLanguage.value = lang
    }

    fun setCourseCategory(category: String) {
        _selectedCourseCategory.value = category
    }

    fun selectCourse(course: Course) {
        _selectedCourse.value = course
        // Select associated batch if any
        viewModelScope.launch {
            val batch = allBatches.value.firstOrNull { it.courseId == course.id }
            _selectedBatch.value = batch
        }
        navigateTo(Screen.CourseDetail)
    }

    fun selectBatch(batch: Batch) {
        _selectedBatch.value = batch
        viewModelScope.launch {
            val course = allCourses.value.firstOrNull { it.id == batch.courseId }
            _selectedCourse.value = course
            repository.getLecturesForBatch(batch.id).collect {
                _currentBatchLectures.value = it
            }
        }
        navigateTo(Screen.BatchDetail)
    }

    fun openLecture(lecture: Lecture) {
        _activeLecture.value = lecture
        _isPlaying.value = true
        _videoProgress.value = if (lecture.durationMinutes > 0) {
            lecture.watchedSeconds.toFloat() / (lecture.durationMinutes * 60f)
        } else 0f
        navigateTo(Screen.LecturePlayer)
    }

    fun togglePlayPause() {
        _isPlaying.value = !_isPlaying.value
    }

    fun setPlaybackSpeed(speed: Float) {
        _playbackSpeed.value = speed
    }

    fun setVideoQuality(quality: String) {
        _videoQuality.value = quality
    }

    fun seekVideo(progress: Float) {
        _videoProgress.value = progress
        val lecture = _activeLecture.value ?: return
        val currentSec = (progress * lecture.durationMinutes * 60).toInt()
        viewModelScope.launch {
            repository.updateLectureProgress(lecture.id, progress >= 0.9f, currentSec)
        }
    }

    fun openNotePdf(note: StudyNote) {
        _selectedNoteForViewing.value = note
        navigateTo(Screen.PdfViewer)
    }

    fun toggleNoteBookmark(note: StudyNote) {
        viewModelScope.launch {
            repository.toggleNoteBookmark(note.id, note.isBookmarked)
        }
    }

    fun toggleNoteDownload(note: StudyNote) {
        viewModelScope.launch {
            repository.toggleNoteDownload(note.id, note.isDownloaded)
        }
    }

    // CBT Test Engine
    fun startTest(test: ExamTest) {
        viewModelScope.launch {
            val questions = repository.getQuestionsList(test.id).ifEmpty {
                InitialDataGenerator.getInitialQuestions()
            }
            _cbtState.value = TestTakingState(
                test = test,
                questions = questions,
                currentQuestionIndex = 0,
                selectedOptions = emptyMap(),
                markedForReview = emptySet(),
                remainingSeconds = test.durationMinutes * 60,
                isSubmitted = false
            )
            startTestTimer()
            navigateTo(Screen.TestCbtTaking)
        }
    }

    private fun startTestTimer() {
        testTimerJob?.cancel()
        testTimerJob = viewModelScope.launch {
            while (_cbtState.value.remainingSeconds > 0 && !_cbtState.value.isSubmitted) {
                delay(1000)
                _cbtState.value = _cbtState.value.copy(
                    remainingSeconds = _cbtState.value.remainingSeconds - 1
                )
            }
            if (!_cbtState.value.isSubmitted) {
                submitTest()
            }
        }
    }

    fun selectQuestionIndex(index: Int) {
        _cbtState.value = _cbtState.value.copy(currentQuestionIndex = index)
    }

    fun answerCurrentQuestion(option: Int) {
        val currQ = _cbtState.value.questions.getOrNull(_cbtState.value.currentQuestionIndex) ?: return
        val updated = _cbtState.value.selectedOptions.toMutableMap()
        updated[currQ.id] = option
        _cbtState.value = _cbtState.value.copy(selectedOptions = updated)
    }

    fun toggleMarkForReview() {
        val currQ = _cbtState.value.questions.getOrNull(_cbtState.value.currentQuestionIndex) ?: return
        val currentSet = _cbtState.value.markedForReview.toMutableSet()
        if (currentSet.contains(currQ.id)) {
            currentSet.remove(currQ.id)
        } else {
            currentSet.add(currQ.id)
        }
        _cbtState.value = _cbtState.value.copy(markedForReview = currentSet)
    }

    fun submitTest() {
        testTimerJob?.cancel()
        val state = _cbtState.value
        val questions = state.questions
        var correct = 0
        var wrong = 0
        var unattempted = 0

        for (q in questions) {
            val userOption = state.selectedOptions[q.id]
            if (userOption == null) {
                unattempted++
            } else if (userOption == q.correctOption) {
                correct++
            } else {
                wrong++
            }
        }
        val marksPerQuestion = 4
        val negativeMarks = 1
        val score = (correct * marksPerQuestion) - (wrong * negativeMarks)
        val timeSpent = (state.test?.durationMinutes?.times(60) ?: 1200) - state.remainingSeconds

        val finalState = state.copy(
            isSubmitted = true,
            score = score.coerceAtLeast(0),
            correctCount = correct,
            wrongCount = wrong,
            unattemptedCount = unattempted,
            totalMarks = questions.size * marksPerQuestion
        )
        _cbtState.value = finalState

        state.test?.let { test ->
            viewModelScope.launch {
                repository.submitTestResult(
                    testId = test.id,
                    score = finalState.score,
                    attempted = correct + wrong,
                    correct = correct,
                    wrong = wrong,
                    unattempted = unattempted,
                    timeSpent = timeSpent
                )
            }
        }
        navigateTo(Screen.TestResult)
    }

    // Doubts
    fun submitDoubt(subject: String, chapter: String, text: String, imageUri: String = "") {
        viewModelScope.launch {
            val newDoubt = Doubt(
                id = "doubt_${System.currentTimeMillis()}",
                studentName = currentUser.value?.name ?: "छात्र",
                subject = subject,
                chapter = chapter,
                questionText = text,
                imageUrl = imageUri,
                status = "PENDING",
                teacherAnswer = "",
                timestamp = "Just now"
            )
            repository.submitDoubt(newDoubt)
        }
    }

    // Teacher Panel: Answer Doubt
    fun answerDoubt(doubtId: String, answer: String) {
        viewModelScope.launch {
            val teacherName = currentUser.value?.name ?: "Prof. Anand Verma"
            repository.answerDoubt(doubtId, answer, teacherName)
        }
    }

    // Checkout & Enrollment
    fun initiateCheckout(batch: Batch) {
        _selectedBatch.value = batch
        _appliedCoupon.value = ""
        _couponDiscountRupees.value = 0
        _isPaymentProcessing.value = false
        _paymentSuccess.value = false
        navigateTo(Screen.Checkout)
    }

    fun applyCoupon(code: String): Boolean {
        return if (code.equals("VIDYA500", ignoreCase = true) || code.equals("TOPPER", ignoreCase = true)) {
            _appliedCoupon.value = code.uppercase()
            _couponDiscountRupees.value = 500
            true
        } else {
            false
        }
    }

    fun executeMockPayment() {
        viewModelScope.launch {
            _isPaymentProcessing.value = true
            delay(1500) // Realistic secure gateway processing animation
            val batch = _selectedBatch.value ?: return@launch
            val discount = _couponDiscountRupees.value
            val finalAmount = (batch.priceRupees - discount).coerceAtLeast(0)
            val txId = "VS_TXN_${System.currentTimeMillis() % 1000000}"

            repository.enrollInBatch(batch.id, batch.courseId, finalAmount, txId)
            _isPaymentProcessing.value = false
            _paymentSuccess.value = true
        }
    }

    // Global Search
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Admin Operations
    fun createCourse(title: String, category: String, subject: String, price: Int, description: String) {
        viewModelScope.launch {
            val id = "course_${System.currentTimeMillis()}"
            val course = Course(
                id = id,
                title = title,
                titleHindi = title,
                category = category,
                subject = subject,
                targetExam = category,
                teacherName = "VidyaSetu Star Mentors",
                teacherTitle = "Senior Academic Faculty",
                priceRupees = price,
                originalPriceRupees = price * 2,
                description = description,
                syllabusSummary = "Full comprehensive syllabus coverage with video lectures and practice tests."
            )
            repository.insertCourse(course)
        }
    }

    fun createBatch(courseId: String, title: String, price: Int, medium: String) {
        viewModelScope.launch {
            val batch = Batch(
                id = "batch_${System.currentTimeMillis()}",
                courseId = courseId,
                title = title,
                titleHindi = title,
                targetExam = "Entrance & Board",
                startDate = "Upcoming Monday",
                validity = "1 Year Access",
                mediumLanguage = medium,
                teachersList = "VidyaSetu Faculty",
                subjectsList = "All Core Subjects",
                priceRupees = price,
                originalPriceRupees = price * 2,
                lectureCount = 100,
                testCount = 20,
                notesCount = 50,
                enrolledCount = 0
            )
            repository.insertBatch(batch)
        }
    }

    fun addLecture(batchId: String, title: String, subject: String, teacherName: String, duration: Int) {
        viewModelScope.launch {
            val lecture = Lecture(
                id = "lec_${System.currentTimeMillis()}",
                batchId = batchId,
                courseId = _selectedCourse.value?.id ?: "course_jee_lakshya",
                title = title,
                titleHindi = title,
                subject = subject,
                teacherName = teacherName,
                durationMinutes = duration,
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                orderIndex = (_currentBatchLectures.value.size + 1),
                chapterName = "Chapter ${_currentBatchLectures.value.size + 1}"
            )
            repository.insertLecture(lecture)
        }
    }

    fun broadcastAnnouncement(title: String, message: String, priority: String) {
        viewModelScope.launch {
            val ann = Announcement(
                id = "ann_${System.currentTimeMillis()}",
                title = title,
                titleHindi = title,
                message = message,
                priority = priority,
                timestamp = "Just now",
                authorName = "VidyaSetu Administrator"
            )
            repository.insertAnnouncement(ann)
        }
    }

    fun scheduleLiveClass(title: String, subject: String, time: String) {
        viewModelScope.launch {
            val live = LiveClass(
                id = "live_${System.currentTimeMillis()}",
                batchId = _selectedBatch.value?.id ?: "batch_jee_2027",
                title = title,
                titleHindi = title,
                teacherName = currentUser.value?.name ?: "Prof. Anand Verma",
                subject = subject,
                scheduledTime = time,
                isLiveNow = false,
                isCompleted = false
            )
            repository.insertLiveClass(live)
        }
    }

    fun switchUserRole(role: String) {
        viewModelScope.launch {
            val curr = currentUser.value ?: InitialDataGenerator.getDefaultUser()
            val updated = when (role) {
                "admin" -> curr.copy(
                    id = "admin_1",
                    name = "Admin (प्रशासक)",
                    role = "admin"
                )
                "teacher" -> curr.copy(
                    id = "teacher_1",
                    name = "Prof. Anand Verma (Physics Faculty)",
                    role = "teacher"
                )
                else -> curr.copy(
                    id = "student_1",
                    name = "राहुल शर्मा (Rahul Sharma)",
                    role = "student"
                )
            }
            repository.updateUser(updated)
            if (role == "admin") {
                navigateTo(Screen.AdminDashboard)
            } else if (role == "teacher") {
                navigateTo(Screen.TeacherDashboard)
            } else {
                navigateTo(Screen.Main)
            }
        }
    }
}
