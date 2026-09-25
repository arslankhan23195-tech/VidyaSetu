package com.example.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.User
import com.example.ui.components.VidyaBrandLogo
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: VidyaViewModel) {
    val coroutineScope = rememberCoroutineScope()
    var isOtpMode by remember { mutableStateOf(false) }
    var emailOrMobile by remember { mutableStateOf("rahul.sharma@vidyasetu.in") }
    var password by remember { mutableStateOf("password123") }
    var passwordVisible by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("4829") }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("login_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            VidyaBrandLogo(size = 54)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "विद्यार्थी लॉगिन / Student Login",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = "Welcome to India's Trusted Learning Bridge",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mode Switcher (Password vs OTP)
            TabRow(
                selectedTabIndex = if (isOtpMode) 1 else 0,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Tab(
                    selected = !isOtpMode,
                    onClick = { isOtpMode = false },
                    text = { Text("Password Login") }
                )
                Tab(
                    selected = isOtpMode,
                    onClick = { isOtpMode = true },
                    text = { Text("Mobile OTP Login") }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = emailOrMobile,
                onValueChange = { emailOrMobile = it },
                label = { Text(if (isOtpMode) "मोबाइल नंबर / Mobile Number" else "ईमेल या मोबाइल / Email or Mobile") },
                leadingIcon = {
                    Icon(
                        imageVector = if (isOtpMode) Icons.Default.Phone else Icons.Default.Email,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_username_field"),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = if (isOtpMode) KeyboardType.Phone else KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (!isOtpMode) {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("पासवर्ड / Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_password_field"),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { showForgotPasswordDialog = true }) {
                        Text(
                            text = "Forgot Password?",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            } else {
                OutlinedTextField(
                    value = otpCode,
                    onValueChange = { otpCode = it },
                    label = { Text("Enter 4-digit OTP (Demo: 4829)") },
                    leadingIcon = { Icon(Icons.Default.Security, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_otp_field"),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (loginError != null) {
                Text(
                    text = loginError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    isLoading = true
                    coroutineScope.launch {
                        val user = viewModel.repository.authenticateUser(emailOrMobile, password)
                        isLoading = false
                        if (user != null) {
                            if (user.role == "admin") {
                                viewModel.navigateTo(Screen.AdminDashboard)
                            } else if (user.role == "teacher") {
                                viewModel.navigateTo(Screen.TeacherDashboard)
                            } else {
                                viewModel.navigateTo(Screen.Main)
                            }
                        } else {
                            loginError = "Invalid credentials. Try demo credentials below."
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("login_submit_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "लॉगिन करें / Secure Login",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("नया खाता बनाएँ? / New to VidyaSetu?")
                TextButton(
                    onClick = { viewModel.navigateTo(Screen.Register) },
                    modifier = Modifier.testTag("login_to_register_button")
                ) {
                    Text("पंजीकरण करें / Register", fontWeight = FontWeight.Bold)
                }
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            // Quick Role Access / Demo Login Panel (Instant access to Student, Teacher, Admin)
            Text(
                text = "⚡ Instant Access Demo Accounts / रोल चुनें",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.switchUserRole("student")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("demo_student_btn"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("👨‍🎓 Student", fontSize = 12.sp)
                }
                OutlinedButton(
                    onClick = {
                        viewModel.switchUserRole("teacher")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("demo_teacher_btn"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("👨‍🏫 Teacher", fontSize = 12.sp)
                }
                OutlinedButton(
                    onClick = {
                        viewModel.switchUserRole("admin")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("demo_admin_btn"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("⚙️ Admin", fontSize = 12.sp)
                }
            }
        }
    }

    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            title = { Text("पासवर्ड रीसेट / Reset Password") },
            text = {
                Text("आपके पंजीकृत मोबाइल नंबर / ईमेल पर पासवर्ड रीसेट लिंक भेज दिया गया है।")
            },
            confirmButton = {
                Button(onClick = { showForgotPasswordDialog = false }) {
                    Text("OK / ठीक है")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(viewModel: VidyaViewModel) {
    val coroutineScope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedClass by remember { mutableStateOf("Class 11") }
    var selectedExam by remember { mutableStateOf("JEE Main & Advanced") }
    var selectedLanguage by remember { mutableStateOf("Hindi") }

    val classOptions = listOf("Class 6", "Class 7", "Class 8", "Class 9", "Class 10", "Class 11", "Class 12", "Repeater/Dropper")
    val examOptions = listOf("JEE Main & Advanced", "NEET-UG", "CUET", "Board Exams", "SSC / Railway", "Olympiad / NTSE")
    val langOptions = listOf("Hindi", "Hinglish", "English")

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("register_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VidyaBrandLogo(size = 46)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "विद्यार्थी पंजीकरण / Student Registration",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("पूरा नाम / Full Name") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("register_name_field"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = mobile,
                onValueChange = { mobile = it },
                label = { Text("मोबाइल नंबर / Mobile Number") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("register_mobile_field"),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("ईमेल / Email Address") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("register_email_field"),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("पासवर्ड / Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("register_password_field"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Class selection chips
            Text(
                text = "कक्षा चुनें / Select Class",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                classOptions.take(4).forEach { item ->
                    FilterChip(
                        selected = selectedClass == item,
                        onClick = { selectedClass = item },
                        label = { Text(item, fontSize = 11.sp) }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                classOptions.drop(4).forEach { item ->
                    FilterChip(
                        selected = selectedClass == item,
                        onClick = { selectedClass = item },
                        label = { Text(item, fontSize = 11.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Target Exam chips
            Text(
                text = "लक्ष्य परीक्षा / Exam Target",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                examOptions.take(3).forEach { item ->
                    FilterChip(
                        selected = selectedExam == item,
                        onClick = { selectedExam = item },
                        label = { Text(item, fontSize = 11.sp) }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                examOptions.drop(3).forEach { item ->
                    FilterChip(
                        selected = selectedExam == item,
                        onClick = { selectedExam = item },
                        label = { Text(item, fontSize = 11.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Preferred Language
            Text(
                text = "पसंदीदा माध्यम / Preferred Medium",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                langOptions.forEach { item ->
                    FilterChip(
                        selected = selectedLanguage == item,
                        onClick = { selectedLanguage = item },
                        label = { Text(item) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        val newUser = User(
                            id = "student_${System.currentTimeMillis()}",
                            name = name.ifBlank { "नया छात्र" },
                            mobile = mobile.ifBlank { "+91 9876543210" },
                            email = email.ifBlank { "student@vidyasetu.in" },
                            password = password.ifBlank { "123456" },
                            studentClass = selectedClass,
                            targetExam = selectedExam,
                            preferredLanguage = selectedLanguage,
                            role = "student"
                        )
                        viewModel.repository.registerUser(newUser)
                        viewModel.navigateTo(Screen.Main)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("register_submit_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
            ) {
                Text(
                    text = "पंजीकरण पूरा करें / Complete Registration",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = { viewModel.navigateTo(Screen.Login) },
                modifier = Modifier.testTag("register_to_login_button")
            ) {
                Text("पहले से खाता है? लॉगिन करें / Existing User? Login")
            }
        }
    }
}
