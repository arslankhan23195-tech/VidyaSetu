package com.example.ui.screens.extra

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.VidyaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VidyaViewModel

@Composable
fun CheckoutScreen(viewModel: VidyaViewModel) {
    val batch by viewModel.selectedBatch.collectAsState()
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()
    val couponDiscount by viewModel.couponDiscountRupees.collectAsState()
    val isProcessing by viewModel.isPaymentProcessing.collectAsState()
    val isSuccess by viewModel.paymentSuccess.collectAsState()

    var couponInput by remember { mutableStateOf("") }
    var couponMessage by remember { mutableStateOf<String?>(null) }
    var selectedPaymentMethod by remember { mutableStateOf("UPI") } // "UPI", "CARD", "NETBANKING"

    if (batch == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No batch selected for checkout")
        }
        return
    }

    val currentBatch = batch!!
    val originalPrice = currentBatch.priceRupees
    val finalPrice = (originalPrice - couponDiscount).coerceAtLeast(0)
    val gstAmount = (finalPrice * 0.18).toInt()
    val totalPayable = finalPrice + gstAmount

    Scaffold(
        topBar = {
            VidyaTopBar(
                title = "सुरक्षित भुगतान / Secure Checkout",
                subtitle = "256-bit SSL Encrypted Payment Gateway",
                onBackClick = { viewModel.navigateTo(Screen.BatchDetail) }
            )
        }
    ) { padding ->
        if (isSuccess) {
            // Payment Success Receipt
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(SuccessGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "बधाई हो! प्रवेश सफल रहा 🎉",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen
                    )
                )

                Text(
                    text = "Congratulations! You are successfully enrolled in ${currentBatch.title}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Transaction ID:", fontSize = 12.sp, color = Color.Gray)
                            Text("VS_TXN_${System.currentTimeMillis() % 1000000}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Amount Paid:", fontSize = 12.sp, color = Color.Gray)
                            Text("₹$totalPayable", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = IndigoPrimary)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Validity:", fontSize = 12.sp, color = Color.Gray)
                            Text(currentBatch.validity, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = { viewModel.navigateTo(Screen.BatchDetail) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("checkout_start_learning_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                ) {
                    Text("कक्षा शुरू करें / Start Learning Now", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            // Checkout Form
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .testTag("checkout_screen_form")
            ) {
                // Order Item Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    border = CardDefaults.outlinedCardBorder(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Selected Batch",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(currentBatch.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(currentBatch.targetExam, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("शिक्षकों की टीम: ${currentBatch.teachersList}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Coupon Code Section
                Text("कूपन कोड / Promo Code", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = couponInput,
                        onValueChange = { couponInput = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Enter VIDYA500 or TOPPER") },
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val ok = viewModel.applyCoupon(couponInput)
                            couponMessage = if (ok) "₹500 कूपन छूट लागू!" else "अमान्य कूपन कोड"
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronDark)
                    ) {
                        Text("लागू करें")
                    }
                }

                if (couponMessage != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = couponMessage!!,
                        color = if (couponDiscount > 0) SuccessGreen else ErrorRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Price Summary
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("भुगतान सारांश / Price Details", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("बैच शुल्क / Course Fee", fontSize = 12.sp)
                            Text("₹$originalPrice", fontSize = 12.sp)
                        }

                        if (couponDiscount > 0) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("कूपन छूट ($appliedCoupon)", fontSize = 12.sp, color = SuccessGreen)
                                Text("- ₹$couponDiscount", fontSize = 12.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("GST (18% Govt Tax)", fontSize = 12.sp)
                            Text("₹$gstAmount", fontSize = 12.sp)
                        }

                        Divider(modifier = Modifier.padding(vertical = 10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("कुल देय राशि / Total Amount", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("₹$totalPayable", fontWeight = FontWeight.Black, fontSize = 18.sp, color = IndigoPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Payment Method Options
                Text("भुगतान का तरीका / Payment Method", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))

                PaymentOptionTile(
                    title = "UPI (Google Pay, PhonePe, Paytm, BHIM)",
                    subtitle = "Instant approval, 0% convenience fee",
                    selected = selectedPaymentMethod == "UPI",
                    onSelect = { selectedPaymentMethod = "UPI" }
                )
                Spacer(modifier = Modifier.height(8.dp))
                PaymentOptionTile(
                    title = "डेबिट / क्रेडिट कार्ड (Visa, Mastercard, RuPay)",
                    subtitle = "All major Indian banks supported",
                    selected = selectedPaymentMethod == "CARD",
                    onSelect = { selectedPaymentMethod = "CARD" }
                )
                Spacer(modifier = Modifier.height(8.dp))
                PaymentOptionTile(
                    title = "नेट बैंकिंग / Net Banking",
                    subtitle = "SBI, HDFC, ICICI, Axis & 50+ Banks",
                    selected = selectedPaymentMethod == "NETBANKING",
                    onSelect = { selectedPaymentMethod = "NETBANKING" }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.executeMockPayment() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("checkout_pay_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    enabled = !isProcessing
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("भुगतान सत्यापित हो रहा है / Verifying...", fontWeight = FontWeight.Bold)
                    } else {
                        Text(
                            text = "₹$totalPayable का भुगतान करें / Pay Securely",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentOptionTile(
    title: String,
    subtitle: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder(),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) IndigoContainerLight else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = selected, onClick = onSelect)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                Text(subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
