package com.example.data.local

import com.example.data.model.*

object InitialDataGenerator {

    fun getDefaultUser(): User {
        return User(
            id = "student_1",
            name = "राहुल शर्मा (Rahul Sharma)",
            mobile = "+91 98765 43210",
            email = "rahul.sharma@vidyasetu.in",
            password = "password123",
            targetExam = "JEE Main & Advanced 2027",
            studentClass = "Class 11",
            preferredLanguage = "Hinglish",
            role = "student",
            avatarUrl = "",
            streakDays = 7,
            totalStudyMinutes = 1920,
            totalLecturesWatched = 38,
            testsCompleted = 9
        )
    }

    fun getInitialCourses(): List<Course> {
        return listOf(
            Course(
                id = "course_jee_lakshya",
                title = "Lakshya JEE 2027: Complete Physics, Chemistry & Maths",
                titleHindi = "लक्ष्य JEE 2027: सम्पूर्ण भौतिकी, रसायन एवं गणित",
                category = "JEE",
                subject = "PCM (All Subjects)",
                targetExam = "JEE Main & Advanced",
                teacherName = "Prof. Anand Verma & Team",
                teacherTitle = "Ex-IITian, 14+ Years Teaching Experience",
                rating = 4.9f,
                reviewCount = 3820,
                lecturesCount = 280,
                testsCount = 45,
                priceRupees = 2999,
                originalPriceRupees = 7999,
                isFree = false,
                bannerDrawableName = "banner_vidyasetu_study_1790310899912",
                description = "Comprehensive two-year master batch covering entire Class 11 & 12 syllabus with daily live lectures, DPPs, chapter notes, AI-generated test analysis and 24x7 doubt resolution.",
                syllabusSummary = "Units & Dimensions, Kinematics, Laws of Motion, Work Energy Power, Rotational Dynamics, Thermodynamics, Organic Chemistry, Chemical Bonding, Calculus, Coordinate Geometry.",
                isFeatured = true
            ),
            Course(
                id = "course_neet_arjuna",
                title = "Arjuna NEET 2026: Complete Biology, Physics & Chemistry",
                titleHindi = "अर्जुन NEET 2026: सम्पूर्ण जीवविज्ञान, भौतिकी व रसायन",
                category = "NEET",
                subject = "PCB (All Subjects)",
                targetExam = "NEET-UG 2026",
                teacherName = "Dr. Meenakshi Sundaram & Dr. Alok Tripathi",
                teacherTitle = "MBBS (AIIMS Gold Medalist), Top NEET Mentors",
                rating = 4.85f,
                reviewCount = 2940,
                lecturesCount = 310,
                testsCount = 50,
                priceRupees = 2799,
                originalPriceRupees = 6999,
                isFree = false,
                bannerDrawableName = "banner_vidyasetu_study_1790310899912",
                description = "Dedicated medical entrance program with NCERT line-by-line decoding, 3D anatomical animations, 10,000+ question bank and national rank predictor.",
                syllabusSummary = "Human Physiology, Genetics, Plant Kingdom, Ecology, Mechanics, Electrodynamics, Physical Chemistry, Coordination Compounds.",
                isFeatured = true
            ),
            Course(
                id = "course_class10_topper",
                title = "Udaan Class 10th Board Topper Batch 2026",
                titleHindi = "उड़ान 10वीं बोर्ड टॉपर बैच 2026",
                category = "Class 10",
                subject = "Science, Maths, SST & English",
                targetExam = "CBSE & State Boards",
                teacherName = "Er. Sameer Sen & Neha Joshi Ma'am",
                teacherTitle = "Top Board Ranker Mentors",
                rating = 4.92f,
                reviewCount = 4120,
                lecturesCount = 190,
                testsCount = 30,
                priceRupees = 1499,
                originalPriceRupees = 4499,
                isFree = false,
                bannerDrawableName = "banner_vidyasetu_study_1790310899912",
                description = "Master your Class 10 Board exams with 100/100 target roadmap, model papers, sample answer writing sessions, and revision mindmaps.",
                syllabusSummary = "Light, Electricity, Life Processes, Chemical Reactions, Quadratic Equations, Triangles, Trigonometry, Nationalism in India.",
                isFeatured = true
            ),
            Course(
                id = "course_foundation_free",
                title = "Neev Foundation: Science & Maths Basic Concepts (FREE)",
                titleHindi = "नींव फाउंडेशन: विज्ञान एवं गणित बेसिक कॉन्सेप्ट्स (निःशुल्क)",
                category = "Class 9",
                subject = "Science & Maths",
                targetExam = "Foundation & Olympiad",
                teacherName = "VidyaSetu Star Mentors",
                teacherTitle = "Specialist Academic Team",
                rating = 4.78f,
                reviewCount = 1890,
                lecturesCount = 65,
                testsCount = 15,
                priceRupees = 0,
                originalPriceRupees = 2999,
                isFree = true,
                bannerDrawableName = "banner_vidyasetu_study_1790310899912",
                description = "100% Free foundation course to build crystal clear fundamentals in basic Physics, Chemistry, and Mathematics before taking up JEE/NEET prep.",
                syllabusSummary = "Motion, Gravitation, Matter in our Surroundings, Number Systems, Polynomials, Linear Equations.",
                isFeatured = false
            ),
            Course(
                id = "course_ssc_railway",
                title = "Vijeta SSC CGL & Railway RRB Foundation 2026",
                titleHindi = "विजेता SSC CGL एवं रेलवे RRB फाउंडेशन 2026",
                category = "SSC",
                subject = "Maths, Reasoning & GK/GS",
                targetExam = "SSC CGL / CHSL / RRB NTPC",
                teacherName = "Rakesh Kumar Sir",
                teacherTitle = "National Selection Specialist, 12+ Yrs",
                rating = 4.88f,
                reviewCount = 3210,
                lecturesCount = 220,
                testsCount = 40,
                priceRupees = 1999,
                originalPriceRupees = 5499,
                isFree = false,
                bannerDrawableName = "banner_vidyasetu_study_1790310899912",
                description = "Speed calculation tricks, comprehensive quantitative aptitude, non-verbal reasoning, and comprehensive General Studies notes with previous 10 years papers.",
                syllabusSummary = "Arithmetic, Advanced Maths, Syllogism, Seating Arrangement, Indian Polity, Modern History, Geography, Current Affairs.",
                isFeatured = false
            ),
            Course(
                id = "course_cuet_general",
                title = "Samarth CUET UG 2026: General Test & Domain Subjects",
                titleHindi = "समर्थ CUET UG 2026: जनरल टेस्ट एवं डोमेन विषय",
                category = "CUET",
                subject = "Domain + General Test",
                targetExam = "CUET (Central Universities)",
                teacherName = "Pooja Sharma & Devendra Sir",
                teacherTitle = "Central University Entrance Specialists",
                rating = 4.82f,
                reviewCount = 1650,
                lecturesCount = 140,
                testsCount = 25,
                priceRupees = 1799,
                originalPriceRupees = 4999,
                isFree = false,
                bannerDrawableName = "banner_vidyasetu_study_1790310899912",
                description = "Your guaranteed bridge to Delhi University, BHU, and top central universities with domain revision and high-speed mock test series.",
                syllabusSummary = "General Mental Ability, Numerical Ability, Logical Reasoning, Current Events, Language Comprehension.",
                isFeatured = false
            )
        )
    }

    fun getInitialBatches(): List<Batch> {
        return listOf(
            Batch(
                id = "batch_jee_2027",
                courseId = "course_jee_lakshya",
                title = "Lakshya JEE 2.0 (Class 11 PCM)",
                titleHindi = "लक्ष्य JEE 2.0 (कक्षा 11 PCM)",
                targetExam = "JEE Main & Advanced 2027",
                startDate = "Started 15 Aug",
                validity = "Valid till June 2027",
                mediumLanguage = "Hinglish",
                teachersList = "Prof. Anand Verma (Physics), Dr. R. K. Gupta (Chemistry), Er. Sameer Sen (Maths)",
                subjectsList = "Physics, Physical Chemistry, Organic Chemistry, Inorganic Chemistry, Mathematics",
                priceRupees = 2999,
                originalPriceRupees = 7999,
                lectureCount = 120,
                testCount = 28,
                notesCount = 85,
                enrolledCount = 14500,
                isEnrolled = true,
                progressPercent = 42,
                bannerDrawableName = "banner_vidyasetu_study_1790310899912"
            ),
            Batch(
                id = "batch_neet_arjuna_2026",
                courseId = "course_neet_arjuna",
                title = "Arjuna NEET 2026 Rapid Batch",
                titleHindi = "अर्जुन NEET 2026 रैपिड बैच",
                targetExam = "NEET-UG 2026",
                startDate = "Started 1 Sept",
                validity = "Valid till May 2026",
                mediumLanguage = "Hindi & Hinglish",
                teachersList = "Dr. Meenakshi Sundaram (Zoology), Dr. Alok Tripathi (Botany), Vikas Sir (Physics)",
                subjectsList = "Zoology, Botany, Physics, Chemistry",
                priceRupees = 2799,
                originalPriceRupees = 6999,
                lectureCount = 140,
                testCount = 35,
                notesCount = 92,
                enrolledCount = 18900,
                isEnrolled = false,
                progressPercent = 0,
                bannerDrawableName = "banner_vidyasetu_study_1790310899912"
            ),
            Batch(
                id = "batch_class10_board",
                courseId = "course_class10_topper",
                title = "Udaan 10th Board Power Batch",
                titleHindi = "उड़ान 10वीं बोर्ड पॉवर बैच",
                targetExam = "Class 10 CBSE / State Board",
                startDate = "Started 10 July",
                validity = "Valid till March 2026",
                mediumLanguage = "Hindi",
                teachersList = "Er. Sameer Sen (Maths), Neha Joshi (Science), Rajesh Sir (Social Science)",
                subjectsList = "Mathematics, Science, Social Studies, English, Hindi",
                priceRupees = 1499,
                originalPriceRupees = 4499,
                lectureCount = 95,
                testCount = 20,
                notesCount = 60,
                enrolledCount = 22400,
                isEnrolled = true,
                progressPercent = 68,
                bannerDrawableName = "banner_vidyasetu_study_1790310899912"
            ),
            Batch(
                id = "batch_free_foundation",
                courseId = "course_foundation_free",
                title = "Neev Foundation Free Bridge Batch",
                titleHindi = "नींव फाउंडेशन फ्री ब्रिज बैच",
                targetExam = "Class 9 & 10 Foundation",
                startDate = "Always Open",
                validity = "Lifetime Free",
                mediumLanguage = "Hinglish",
                teachersList = "Academic Mentor Team",
                subjectsList = "Physics Basics, Chemistry Basics, Fundamental Mathematics",
                priceRupees = 0,
                originalPriceRupees = 1999,
                lectureCount = 45,
                testCount = 12,
                notesCount = 30,
                enrolledCount = 35000,
                isEnrolled = true,
                progressPercent = 85,
                bannerDrawableName = "banner_vidyasetu_study_1790310899912"
            )
        )
    }

    fun getInitialLectures(): List<Lecture> {
        return listOf(
            Lecture(
                id = "lec_101",
                batchId = "batch_jee_2027",
                courseId = "course_jee_lakshya",
                title = "Lecture 01: Rotational Dynamics - Centre of Mass & Moment of Inertia",
                titleHindi = "व्याख्यान 01: घूर्णन गति - द्रव्यमान केंद्र एवं जड़त्व आघूर्ण",
                subject = "Physics",
                teacherName = "Prof. Anand Verma",
                durationMinutes = 68,
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                orderIndex = 1,
                isCompleted = true,
                watchedSeconds = 4080,
                notesPdfUrl = "notes_physics_lec1.pdf",
                isFreePreview = true,
                chapterName = "Rotational Motion"
            ),
            Lecture(
                id = "lec_102",
                batchId = "batch_jee_2027",
                courseId = "course_jee_lakshya",
                title = "Lecture 02: Torque & Angular Momentum Conservation with IIT PYQs",
                titleHindi = "व्याख्यान 02: बल आघूर्ण एवं कोणीय संवेग संरक्षण (JEE प्रश्न)",
                subject = "Physics",
                teacherName = "Prof. Anand Verma",
                durationMinutes = 72,
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                orderIndex = 2,
                isCompleted = true,
                watchedSeconds = 4320,
                notesPdfUrl = "notes_physics_lec2.pdf",
                isFreePreview = false,
                chapterName = "Rotational Motion"
            ),
            Lecture(
                id = "lec_103",
                batchId = "batch_jee_2027",
                courseId = "course_jee_lakshya",
                title = "Lecture 03: Rolling Motion on Incline & Work-Energy in Rotation",
                titleHindi = "व्याख्यान 03: नत तल पर लोटनिक गति एवं घूर्णन कार्य-ऊर्जा",
                subject = "Physics",
                teacherName = "Prof. Anand Verma",
                durationMinutes = 75,
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                orderIndex = 3,
                isCompleted = false,
                watchedSeconds = 1240, // Currently watching
                notesPdfUrl = "notes_physics_lec3.pdf",
                isFreePreview = false,
                chapterName = "Rotational Motion"
            ),
            Lecture(
                id = "lec_104",
                batchId = "batch_jee_2027",
                courseId = "course_jee_lakshya",
                title = "Lecture 04: Organic Reaction Mechanisms - Carbocations & SN1 vs SN2",
                titleHindi = "व्याख्यान 04: कार्बनिक अभिक्रिया क्रियाविधि - SN1 एवं SN2",
                subject = "Chemistry",
                teacherName = "Dr. R. K. Gupta",
                durationMinutes = 65,
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                orderIndex = 4,
                isCompleted = false,
                watchedSeconds = 0,
                notesPdfUrl = "notes_chem_lec1.pdf",
                isFreePreview = false,
                chapterName = "Organic Chemistry"
            ),
            Lecture(
                id = "lec_105",
                batchId = "batch_jee_2027",
                courseId = "course_jee_lakshya",
                title = "Lecture 05: Definite Integration & Shortcut Area Formulae",
                titleHindi = "व्याख्यान 05: निश्चित समाकलन एवं क्षेत्रफल ज्ञात करने की शॉर्ट ट्रिक्स",
                subject = "Mathematics",
                teacherName = "Er. Sameer Sen",
                durationMinutes = 80,
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                orderIndex = 5,
                isCompleted = false,
                watchedSeconds = 0,
                notesPdfUrl = "notes_maths_lec1.pdf",
                isFreePreview = false,
                chapterName = "Calculus"
            )
        )
    }

    fun getInitialLiveClasses(): List<LiveClass> {
        return listOf(
            LiveClass(
                id = "live_101",
                batchId = "batch_jee_2027",
                title = "LIVE: Physics Rapid Problem Solving - Work Power & Energy",
                titleHindi = "लाइव: भौतिकी प्रश्न हल सत्र - कार्य, ऊर्जा एवं शक्ति (JEE 2027)",
                teacherName = "Prof. Anand Verma",
                subject = "Physics",
                scheduledTime = "Started 10 mins ago",
                durationMinutes = 90,
                isLiveNow = true,
                isCompleted = false,
                attendeeCount = 2410,
                recordingUrl = ""
            ),
            LiveClass(
                id = "live_102",
                batchId = "batch_jee_2027",
                title = "Today 7:30 PM: Chemistry Mega Doubt Session - Chemical Equilibrium",
                titleHindi = "आज सायं 7:30: रसायन संदेह निवारण सत्र - रासायनिक साम्य",
                teacherName = "Dr. R. K. Gupta",
                subject = "Chemistry",
                scheduledTime = "Today, 07:30 PM",
                durationMinutes = 60,
                isLiveNow = false,
                isCompleted = false,
                attendeeCount = 1850,
                recordingUrl = ""
            ),
            LiveClass(
                id = "live_103",
                batchId = "batch_neet_arjuna_2026",
                title = "Tomorrow 6:00 PM: Human Physiology Rapid Fire Diagrams & NCERT Quiz",
                titleHindi = "कल सायं 6:00: मानव शरीरक्रिया विज्ञान महत्वपूर्ण चित्र एवं NCERT क्विज",
                teacherName = "Dr. Meenakshi Sundaram",
                subject = "Biology",
                scheduledTime = "Tomorrow, 06:00 PM",
                durationMinutes = 75,
                isLiveNow = false,
                isCompleted = false,
                attendeeCount = 3100,
                recordingUrl = ""
            ),
            LiveClass(
                id = "live_104",
                batchId = "batch_jee_2027",
                title = "Recording: Kinematics 2D Projectile Motion Master Class",
                titleHindi = "रिकॉर्डिंग: गतिविज्ञान 2D प्रक्षेप्य गति मास्टर क्लास",
                teacherName = "Prof. Anand Verma",
                subject = "Physics",
                scheduledTime = "Yesterday",
                durationMinutes = 85,
                isLiveNow = false,
                isCompleted = true,
                attendeeCount = 4200,
                recordingUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            )
        )
    }

    fun getInitialNotes(): List<StudyNote> {
        return listOf(
            StudyNote(
                id = "note_1",
                courseId = "course_jee_lakshya",
                batchId = "batch_jee_2027",
                subject = "Physics",
                chapterName = "Rotational Motion",
                title = "Rotational Mechanics Formula Sheet & Handwritten Topper Notes",
                titleHindi = "घूर्णन यांत्रिकी सूत्र पुस्तिका एवं टॉपर हस्तलिखित नोट्स",
                pageCount = 38,
                fileSizeMb = 4.8f,
                isBookmarked = true,
                isDownloaded = true
            ),
            StudyNote(
                id = "note_2",
                courseId = "course_jee_lakshya",
                batchId = "batch_jee_2027",
                subject = "Chemistry",
                chapterName = "Chemical Bonding",
                title = "VSEPR Theory & Molecular Orbital Diagrams Visual Handbook",
                titleHindi = "रासायनिक आबंधन एवं आणविक कक्षक सिद्धांत हैंडबुक",
                pageCount = 26,
                fileSizeMb = 3.2f,
                isBookmarked = false,
                isDownloaded = false
            ),
            StudyNote(
                id = "note_3",
                courseId = "course_jee_lakshya",
                batchId = "batch_jee_2027",
                subject = "Mathematics",
                chapterName = "Calculus",
                title = "Definite Integrals & 100 IIT-JEE Shortcut Techniques",
                titleHindi = "निश्चित समाकलन एवं 100 शॉर्टकट सूत्र संग्रह",
                pageCount = 44,
                fileSizeMb = 5.6f,
                isBookmarked = true,
                isDownloaded = false
            ),
            StudyNote(
                id = "note_4",
                courseId = "course_neet_arjuna",
                batchId = "batch_neet_arjuna_2026",
                subject = "Biology",
                chapterName = "Genetics",
                title = "Mendelian Genetics & Molecular Basis of Inheritance NCERT Booster",
                titleHindi = "आनुवांशिकी एवं वंशागति का आणविक आधार NCERT बूस्टर नोट्स",
                pageCount = 52,
                fileSizeMb = 6.4f,
                isBookmarked = false,
                isDownloaded = true
            )
        )
    }

    fun getInitialTests(): List<ExamTest> {
        return listOf(
            ExamTest(
                id = "test_jee_mock_1",
                courseId = "course_jee_lakshya",
                batchId = "batch_jee_2027",
                title = "All India JEE Main Full Length Mock Test 01",
                titleHindi = "अखिल भारतीय JEE मेन पूर्ण मॉक टेस्ट 01",
                targetExam = "JEE Main",
                subject = "PCM (Physics, Chemistry, Maths)",
                totalQuestions = 15,
                durationMinutes = 45,
                totalMarks = 60,
                difficulty = "Medium",
                isAttempted = true,
                lastScore = 48,
                totalAttempted = 14,
                correctCount = 12,
                wrongCount = 2,
                unattemptedCount = 1,
                timeSpentSeconds = 1840
            ),
            ExamTest(
                id = "test_physics_chapter",
                courseId = "course_jee_lakshya",
                batchId = "batch_jee_2027",
                title = "Physics Chapter Test: Rotational Motion & Torque",
                titleHindi = "भौतिकी अध्याय टेस्ट: घूर्णन गति एवं बल आघूर्ण",
                targetExam = "JEE / NEET",
                subject = "Physics",
                totalQuestions = 10,
                durationMinutes = 20,
                totalMarks = 40,
                difficulty = "Hard",
                isAttempted = false,
                lastScore = 0,
                totalAttempted = 0,
                correctCount = 0,
                wrongCount = 0,
                unattemptedCount = 10,
                timeSpentSeconds = 0
            ),
            ExamTest(
                id = "test_daily_quiz",
                courseId = "course_jee_lakshya",
                batchId = "batch_jee_2027",
                title = "VidyaSetu Daily Super 10 Practice Quiz",
                titleHindi = "विद्यासेतु दैनिक सुपर 10 अभ्यास क्विज",
                targetExam = "General Target",
                subject = "Mixed Concepts",
                totalQuestions = 10,
                durationMinutes = 15,
                totalMarks = 40,
                difficulty = "Easy",
                isAttempted = false,
                lastScore = 0,
                totalAttempted = 0,
                correctCount = 0,
                wrongCount = 0,
                unattemptedCount = 10,
                timeSpentSeconds = 0
            )
        )
    }

    fun getInitialQuestions(): List<Question> {
        return listOf(
            Question(
                id = "q_1",
                testId = "test_physics_chapter",
                questionIndex = 1,
                questionText = "A solid cylinder of mass M and radius R rolls without slipping down an inclined plane of inclination θ. What is the acceleration of its centre of mass?",
                questionTextHindi = "द्रव्यमान M और त्रिज्या R का एक ठोस बेलन θ झुकाव वाले नत तल पर बिना फिसले नीचे लुढ़कता है। इसके द्रव्यमान केंद्र का त्वरण क्या होगा?",
                optionA = "g sin θ",
                optionB = "(2/3) g sin θ",
                optionC = "(1/2) g sin θ",
                optionD = "(3/4) g sin θ",
                correctOption = 2,
                explanationText = "For a rolling solid cylinder, moment of inertia I = (1/2) M R^2. Linear acceleration a = (g sin θ) / (1 + I / (M R^2)) = (g sin θ) / (1 + 1/2) = (2/3) g sin θ. Hence Option B is correct.",
                subject = "Physics"
            ),
            Question(
                id = "q_2",
                testId = "test_physics_chapter",
                questionIndex = 2,
                questionText = "The radius of gyration of a uniform disc of radius R about an axis perpendicular to its plane and passing through its centre is:",
                questionTextHindi = "त्रिज्या R की एक समान डिस्क की उसके तल के लंबवत तथा केंद्र से गुजरने वाली अक्ष के परितः घूर्णन त्रिज्या क्या है?",
                optionA = "R / 2",
                optionB = "R / √2",
                optionC = "R / √3",
                optionD = "R",
                correctOption = 2,
                explanationText = "The moment of inertia of a uniform circular disc is I = (1/2) M R^2. Since I = M k^2, we have k^2 = R^2 / 2, so k = R / √2. Hence Option B is correct.",
                subject = "Physics"
            ),
            Question(
                id = "q_3",
                testId = "test_physics_chapter",
                questionIndex = 3,
                questionText = "When a body moves in a circular path with constant speed, its acceleration is:",
                questionTextHindi = "जब कोई पिंड नियत चाल से वृत्तीय पथ पर गति करता है, तो उसका त्वरण होता है:",
                optionA = "Zero",
                optionB = "Constant in both magnitude and direction",
                optionC = "Directed towards the centre with constant magnitude v^2/R",
                optionD = "Tangential to the circle",
                correctOption = 3,
                explanationText = "In uniform circular motion, speed is constant but direction changes continuously. The centripetal acceleration acts radially towards the centre with magnitude a_c = v^2 / R.",
                subject = "Physics"
            ),
            Question(
                id = "q_4",
                testId = "test_physics_chapter",
                questionIndex = 4,
                questionText = "Two bodies of masses 2 kg and 4 kg are moving with equal kinetic energies. The ratio of their linear momenta (p1 : p2) is:",
                questionTextHindi = "2 किग्रा और 4 किग्रा द्रव्यमान के दो पिंड समान गतिज ऊर्जा से गतिमान हैं। उनके रेखीय संवेगों का अनुपात (p1 : p2) होगा:",
                optionA = "1 : 2",
                optionB = "1 : √2",
                optionC = "√2 : 1",
                optionD = "1 : 4",
                correctOption = 2,
                explanationText = "Kinetic energy K = p^2 / (2m). Since K1 = K2, p1^2 / (2 m1) = p2^2 / (2 m2). Therefore p1 / p2 = √(m1 / m2) = √(2 / 4) = 1 / √2. Hence Option B.",
                subject = "Physics"
            ),
            Question(
                id = "q_5",
                testId = "test_physics_chapter",
                questionIndex = 5,
                questionText = "A particle is projected with velocity u at an angle of 45° with the horizontal. What is its radius of curvature at the highest point?",
                questionTextHindi = "एक कण को क्षैतिज से 45° के कोण पर वेग u से प्रक्षेपित किया जाता है। उच्चतम बिंदु पर इसकी वक्रता त्रिज्या क्या होगी?",
                optionA = "u^2 / g",
                optionB = "u^2 / (2g)",
                optionC = "2 u^2 / g",
                optionD = "u^2 / (√2 g)",
                correctOption = 2,
                explanationText = "At the highest point, velocity is purely horizontal: v = u cos 45° = u / √2. Acceleration perpendicular to velocity is g. Radius of curvature R = v^2 / a_n = (u^2 / 2) / g = u^2 / (2g).",
                subject = "Physics"
            ),
            // Daily Quiz Questions
            Question(
                id = "dq_1",
                testId = "test_daily_quiz",
                questionIndex = 1,
                questionText = "What is the SI unit of electric potential difference?",
                questionTextHindi = "विद्युत विभवांतर का SI मात्रक क्या है?",
                optionA = "Ampere (A)",
                optionB = "Volt (V)",
                optionC = "Joule (J)",
                optionD = "Watt (W)",
                correctOption = 2,
                explanationText = "Electric potential difference is measured in Volts (V), which is equal to 1 Joule per Coulomb.",
                subject = "Science"
            ),
            Question(
                id = "dq_2",
                testId = "test_daily_quiz",
                questionIndex = 2,
                questionText = "Which gas is evolved when zinc granules react with dilute sulphuric acid?",
                questionTextHindi = "तनु सल्फ्यूरिक अम्ल के साथ जिंक की अभिक्रिया से कौन सी गैस निकलती है?",
                optionA = "Oxygen (O2)",
                optionB = "Hydrogen (H2)",
                optionC = "Carbon dioxide (CO2)",
                optionD = "Sulphur dioxide (SO2)",
                correctOption = 2,
                explanationText = "Zn + H2SO4 -> ZnSO4 + H2↑. Hydrogen gas is evolved and burns with a characteristic pop sound.",
                subject = "Chemistry"
            ),
            Question(
                id = "dq_3",
                testId = "test_daily_quiz",
                questionIndex = 3,
                questionText = "In human digestive system, bile juice is secreted by:",
                questionTextHindi = "मानव पाचन तंत्र में पित्त रस (Bile juice) किसके द्वारा स्रावित होता है?",
                optionA = "Pancreas (अग्न्याशय)",
                optionB = "Liver (यकृत)",
                optionC = "Stomach (आमाशय)",
                optionD = "Gall bladder (पित्ताशय)",
                correctOption = 2,
                explanationText = "Bile is secreted by the Liver and stored/concentrated in the Gall Bladder.",
                subject = "Biology"
            )
        )
    }

    fun getInitialDoubts(): List<Doubt> {
        return listOf(
            Doubt(
                id = "doubt_1",
                studentId = "student_1",
                studentName = "राहुल शर्मा",
                subject = "Physics",
                chapter = "Rotational Motion",
                questionText = "Sir, why is moment of inertia calculated differently for hollow cylinder vs solid cylinder about axis of symmetry? Can you explain conceptually?",
                status = "ANSWERED",
                teacherAnswer = "Great question, Rahul! In a solid cylinder, mass is distributed all the way from the center (r=0) to the outer radius (r=R). In a hollow cylinder, all the mass is situated at the maximum distance R from the axis. Since I = ∫ r^2 dm, greater distance from the axis yields greater resistance to rotational acceleration: I_solid = 1/2 MR^2, whereas I_hollow = MR^2.",
                teacherName = "Prof. Anand Verma (VidyaSetu Physics HOD)",
                timestamp = "2 hours ago"
            ),
            Doubt(
                id = "doubt_2",
                studentId = "student_1",
                studentName = "राहुल शर्मा",
                subject = "Chemistry",
                chapter = "Chemical Equilibrium",
                questionText = "What will happen to the equilibrium position if an inert gas like Argon is added at constant volume?",
                status = "ANSWERED",
                teacherAnswer = "At constant volume, adding an inert gas does not change the partial pressures or concentrations of the reacting species. Therefore, there is NO effect on the equilibrium position.",
                teacherName = "Dr. R. K. Gupta",
                timestamp = "Yesterday"
            ),
            Doubt(
                id = "doubt_3",
                studentId = "student_1",
                studentName = "राहुल शर्मा",
                subject = "Mathematics",
                chapter = "Limits & Derivatives",
                questionText = "Sir, how to quickly evaluate lim(x->0) (sin x - x) / x^3 using Taylor expansion?",
                status = "PENDING",
                teacherAnswer = "",
                teacherName = "Er. Sameer Sen",
                timestamp = "Just now"
            )
        )
    }

    fun getInitialAnnouncements(): List<Announcement> {
        return listOf(
            Announcement(
                id = "ann_1",
                title = "🎉 VidyaSetu All India Scholarship Test (VAIST) Registration Live!",
                titleHindi = "🎉 विद्यासेतु अखिल भारतीय छात्रवृत्ति परीक्षा पंजीकरण शुरू!",
                message = "Win up to 100% scholarship for JEE, NEET and Foundation batches. Test scheduled on Sunday, 10:00 AM. Free registration for all VidyaSetu users.",
                targetBatch = "All Batches",
                priority = "URGENT",
                timestamp = "Today, 09:00 AM",
                authorName = "VidyaSetu Academic Director"
            ),
            Announcement(
                id = "ann_2",
                title = "📅 NTA Releases JEE Main 2027 Schedule & Syllabus Clarification",
                titleHindi = "📅 NTA ने जारी किया JEE मेन परीक्षा कार्यक्रम",
                message = "The National Testing Agency has published the official dates and guidelines. A special live analysis session will be conducted today at 8:00 PM by our expert faculty.",
                targetBatch = "Lakshya JEE 2.0",
                priority = "EXAM_ALERT",
                timestamp = "Yesterday, 04:30 PM",
                authorName = "Academic Examination Cell"
            ),
            Announcement(
                id = "ann_3",
                title = "📚 Daily Practice Problems (DPP #14) Uploaded for Rotational Motion",
                titleHindi = "📚 घूर्णन गति के लिए DPP #14 अपलोड किया गया",
                message = "Students are advised to attempt all 15 questions before attending tomorrow's discussion lecture.",
                targetBatch = "Lakshya JEE 2.0",
                priority = "NORMAL",
                timestamp = "2 days ago",
                authorName = "Prof. Anand Verma"
            )
        )
    }
}
