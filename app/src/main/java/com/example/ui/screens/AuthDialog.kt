package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.SampleData
import com.example.localization.AppLanguage
import com.example.localization.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthDialog(
    isLoginModeInitial: Boolean,
    currentLanguage: AppLanguage,
    onLogin: (phone: String, pin: String) -> Unit,
    onSignup: (name: String, phone: String, district: String, crops: String, pin: String) -> Unit,
    onDismiss: () -> Unit
) {
    var isLoginMode by remember { mutableStateOf(isLoginModeInitial) }

    // Inputs
    var phone by remember { mutableStateOf("0300-1234567") }
    var pin by remember { mutableStateOf("1234") }
    var name by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("Mardan") }
    var crops by remember { mutableStateOf("Wheat, Maize") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .wrapContentHeight()
                .clip(RoundedCornerShape(22.dp)),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (isLoginMode) Strings.login(currentLanguage) else Strings.signup(currentLanguage),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Kisaan Book • کسان بک",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Login vs Signup Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(4.dp)
                ) {
                    TabButton(
                        title = Strings.login(currentLanguage),
                        isSelected = isLoginMode,
                        onClick = { isLoginMode = true },
                        modifier = Modifier.weight(1f)
                    )
                    TabButton(
                        title = Strings.signup(currentLanguage),
                        isSelected = !isLoginMode,
                        onClick = { isLoginMode = false },
                        modifier = Modifier.weight(1f)
                    )
                }

                // If Signup: Name Field
                if (!isLoginMode) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(Strings.farmerName(currentLanguage)) },
                        placeholder = { Text("e.g. Rashid Ali") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_name_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                // Phone Field
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(Strings.mobileNumber(currentLanguage)) },
                    placeholder = { Text("0300-1234567") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_phone_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // If Signup: District Selection
                if (!isLoginMode) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = Strings.selectDistrict(currentLanguage),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(SampleData.pakistaniDistricts.take(8)) { dist ->
                                FilterChip(
                                    selected = dist == district,
                                    onClick = { district = dist },
                                    label = { Text(dist) }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = crops,
                        onValueChange = { crops = it },
                        label = { Text(Strings.mainCrops(currentLanguage)) },
                        placeholder = { Text("Wheat, Maize, Rice") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                // 4-Digit PIN
                OutlinedTextField(
                    value = pin,
                    onValueChange = { if (it.length <= 6) pin = it },
                    label = { Text(Strings.password(currentLanguage)) },
                    placeholder = { Text("4-digit PIN") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_pin_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // Action Button
                Button(
                    onClick = {
                        if (isLoginMode) {
                            onLogin(phone, pin)
                        } else {
                            onSignup(name, phone, district, crops, pin)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("auth_submit_button"),
                    shape = RoundedCornerShape(12.dp),
                    enabled = phone.isNotBlank() && pin.isNotBlank() && (isLoginMode || name.isNotBlank())
                ) {
                    Text(
                        text = if (isLoginMode) Strings.login(currentLanguage) else Strings.signup(currentLanguage),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                // Quick Demo Login shortcut for convenience
                if (isLoginMode) {
                    OutlinedButton(
                        onClick = {
                            onLogin("0300-1234567", "1234")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Instant Login as Demo Farmer (Rashid Ali)", fontSize = 12.sp)
                    }
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Continue as Guest / بعد میں کریں", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun TabButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 13.sp,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
