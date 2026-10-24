package com.example.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.model.CalendarType
import com.example.model.MonthInfo
import com.example.model.Province
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.time.LocalDate

// ========== DATA & HELPERS ==========

val allMonthsList = listOf(
    MonthInfo(1, "January", "Ma", "ما", "Muharram/Safar", listOf("Late Wheat", "Barley", "Mustard", "Peas", "Spinach", "Onion", "Citrus harvest")),
    MonthInfo(2, "February", "Pagan", "پکڼ", "Safar/Rabi-ul-Awwal", listOf("Wheat", "Mustard", "Early Cotton (south)", "Peas", "Garlic", "Spring vegetables")),
    MonthInfo(3, "March", "Chetar", "چېتر", "Rabi-ul-Awwal", listOf("Wheat harvest starts", "Cotton planting", "Sugarcane planting", "Spring maize")),
    MonthInfo(4, "April", "Besak", "بېساک", "Rabi-us-Sani", listOf("Cotton", "Sugarcane", "Spring maize", "Onion", "Tomato")),
    MonthInfo(5, "May", "Jheet", "جېټ", "Jamadi", listOf("Cotton", "Rice nursery", "Sugarcane", "Maize", "Okra", "Chilli")),
    MonthInfo(6, "June", "Harh", "هاړ", "Rajab", listOf("Rice transplanting", "Cotton", "Maize", "Sugarcane", "Monsoon vegetables")),
    MonthInfo(7, "July", "Pashakal", "پشکال", "Shaban", listOf("Rice", "Cotton", "Maize", "Sugarcane growth", "Okra", "Gourds")),
    MonthInfo(8, "August", "Badro", "بادرو", "Ramadan", listOf("Rice", "Cotton", "Maize", "Sugarcane", "Okra", "Bitter gourd")),
    MonthInfo(9, "September", "Asu", "اسو", "Shawwal", listOf("Cotton picking starts", "Rice harvest begins", "Early Rabi prep", "Cauliflower", "Cabbage")),
    MonthInfo(10, "October", "Katak", "کتک", "Zul-Qadah", listOf("Wheat sowing starts", "Rice harvest", "Potato", "Peas", "Spinach", "Garlic", "Mustard")),
    MonthInfo(11, "November", "Magan", "مګن", "Zul-Hijjah", listOf("Main Wheat sowing", "Chickpea / Lentil", "Winter vegetables", "Onion nursery")),
    MonthInfo(12, "December", "Po", "پوه", "Muharram", listOf("Wheat growth", "Late sowing", "Winter vegetables", "Citrus", "Garlic"))
)

fun getProvinceCropNote(crop: String, province: Province, monthId: Int): String {
    val c = crop.lowercase()
    return when {
        c.contains("wheat") -> when (province) {
            Province.SINDH -> if (monthId == 10 || monthId == 11) "Lower Sindh: best 1–20 Nov. Upper Sindh: from 7 Nov." else ""
            Province.KP -> if (monthId == 10) "Early sowing from ~25 Oct recommended." else ""
            Province.PUNJAB -> "Main window Oct–Nov. Aim for early November."
            else -> "Oct–Dec depending on area."
        }
        c.contains("cotton") -> when (province) {
            Province.SINDH -> "Earlier start possible (Feb–Mar in south)."
            else -> "Main sowing April–May."
        }
        c.contains("rice") -> when (province) {
            Province.SINDH -> "Nursery earlier (April–May)."
            else -> "Nursery May–June, transplant June–July."
        }
        else -> ""
    }
}

fun detectProvince(lat: Double, lon: Double): Province {
    return when {
        lat in 23.5..28.5 && lon in 66.5..71.5 -> Province.SINDH
        lat in 27.5..34.0 && lon in 69.5..75.5 -> Province.PUNJAB
        lat in 31.5..37.0 && lon in 69.0..74.5 -> Province.KP
        lat in 24.5..32.0 && lon in 60.5..70.5 -> Province.BALOCHISTAN
        lat in 33.4..33.9 && lon in 72.8..73.3 -> Province.ISLAMABAD
        else -> Province.UNKNOWN
    }
}

fun getDisplayName(month: MonthInfo, type: CalendarType): String = when (type) {
    CalendarType.GREGORIAN -> month.gregorianName
    CalendarType.PASHTUN -> "${month.pashtoScript}\n${month.pashtoName}"
    CalendarType.HIJRI -> month.hijriName
}

@SuppressLint("MissingPermission")
fun fetchLocation(context: Context, onResult: (Double, Double) -> Unit) {
    try {
        val client = LocationServices.getFusedLocationProviderClient(context)
        client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, CancellationTokenSource().token)
            .addOnSuccessListener { location ->
                if (location != null) {
                    onResult(location.latitude, location.longitude)
                }
            }
            .addOnFailureListener {
                client.lastLocation.addOnSuccessListener { loc ->
                    if (loc != null) onResult(loc.latitude, loc.longitude)
                }
            }
    } catch (_: Exception) {
        // Fallback for emulator without Google Play Services
    }
}

// ========== MAIN CALENDAR SCREEN ==========

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedType by remember { mutableStateOf(CalendarType.GREGORIAN) }
    var selectedMonth by remember { mutableStateOf<MonthInfo?>(null) }
    var province by remember { mutableStateOf(Province.KP) }
    var searchQuery by remember { mutableStateOf("") }
    var locationStatus by remember { mutableStateOf("Location: Khyber Pakhtunkhwa") }

    val today = remember { LocalDate.now() }
    val currentMonthId = today.monthValue

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            fetchLocation(context) { lat, lon ->
                province = detectProvince(lat, lon)
                locationStatus = "Location: ${province.displayName}"
            }
        } else {
            locationStatus = "Location: Khyber Pakhtunkhwa (Default)"
        }
    }

    LaunchedEffect(Unit) {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED) {
            fetchLocation(context) { lat, lon ->
                province = detectProvince(lat, lon)
                locationStatus = "Location: ${province.displayName}"
            }
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    val filteredMonths = if (searchQuery.isBlank()) {
        allMonthsList
    } else {
        allMonthsList.filter { month ->
            month.baseCrops.any { it.contains(searchQuery, ignoreCase = true) } ||
            month.gregorianName.contains(searchQuery, ignoreCase = true) ||
            month.pashtoName.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarMonth, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("Pakistan Crop Calendar", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    var expanded by remember { mutableStateOf(false) }
                    TextButton(onClick = { expanded = true }) {
                        Text(selectedType.title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        CalendarType.values().forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.title) },
                                onClick = {
                                    selectedType = type
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(14.dp)
        ) {
            // Location status badge
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = locationStatus,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search crop (wheat, cotton, spinach, rice...)") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            if (searchQuery.isNotBlank()) {
                Text(
                    text = "Showing months related to \"$searchQuery\":",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Month grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredMonths, key = { it.id }) { month ->
                    MonthCard(
                        month = month,
                        displayName = getDisplayName(month, selectedType),
                        isCurrent = month.id == currentMonthId,
                        onClick = { selectedMonth = month }
                    )
                }
            }
        }
    }

    // Month detail dialog
    selectedMonth?.let { month ->
        AlertDialog(
            onDismissRequest = { selectedMonth = null },
            title = {
                Column {
                    Text(getDisplayName(month, selectedType), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(
                        text = "${month.gregorianName} (${month.hijriName}) • ${province.displayName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Recommended Activities & Sowing Crops:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    month.baseCrops.forEach { crop ->
                        val note = getProvinceCropNote(crop, province, month.id)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "• $crop",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                                if (note.isNotEmpty()) {
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = "→ $note",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Farmer Advisory: Always check current soil moisture and frost forecasts with local Agriculture Extension officers before sowing.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedMonth = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun MonthCard(
    month: MonthInfo,
    displayName: String,
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrent)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrent) 3.dp else 1.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (isCurrent) {
                    Text(
                        text = "Current Month",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(2.dp))
                }
                Text(
                    text = displayName,
                    textAlign = TextAlign.Center,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
