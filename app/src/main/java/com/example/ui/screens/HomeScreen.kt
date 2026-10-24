package com.example.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.model.FarmCrop
import com.example.model.Farmer
import com.example.model.KhataEntry
import com.example.model.KhataType
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentUser: Farmer,
    farmCrops: List<FarmCrop>,
    khataEntries: List<KhataEntry>,
    selectedDistrict: String,
    unreadNotifCount: Int,
    appLanguage: AppLanguage,
    onDistrictChange: (String) -> Unit,
    onNotificationClick: () -> Unit,
    onOpenPlantDoctor: () -> Unit,
    onOpenMyCrops: () -> Unit,
    onOpenCreateCrop: () -> Unit,
    onOpenKhata: () -> Unit,
    onOpenAddKhataEntry: () -> Unit,
    onOpenCalendar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isLocating by remember { mutableStateOf(false) }
    var weatherExpanded by remember { mutableStateOf(false) }

    val isUrdu = appLanguage == AppLanguage.URDU
    val isPashto = appLanguage == AppLanguage.PASHTO

    // Real Android Location Permission Request Launcher
    @SuppressLint("MissingPermission")
    fun fetchRealDeviceLocation() {
        try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providers = listOf(
                LocationManager.GPS_PROVIDER,
                LocationManager.NETWORK_PROVIDER,
                LocationManager.PASSIVE_PROVIDER
            )
            var bestLocation: Location? = null
            for (provider in providers) {
                if (locationManager.isProviderEnabled(provider)) {
                    val loc = locationManager.getLastKnownLocation(provider)
                    if (loc != null && (bestLocation == null || loc.accuracy < bestLocation.accuracy)) {
                        bestLocation = loc
                    }
                }
            }

            if (bestLocation != null) {
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(bestLocation.latitude, bestLocation.longitude, 1)
                    val locality = addresses?.firstOrNull()?.locality
                        ?: addresses?.firstOrNull()?.subAdminArea
                        ?: addresses?.firstOrNull()?.adminArea
                        ?: "Mardan"
                    onDistrictChange(locality)
                    Toast.makeText(context, if (isUrdu) "مقام: $locality" else "Location: $locality", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    onDistrictChange("Mardan")
                }
            } else {
                Toast.makeText(context, if (isUrdu) "جی پی ایس فعال کریں" else "Please enable GPS", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Location error", Toast.LENGTH_SHORT).show()
        } finally {
            isLocating = false
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            fetchRealDeviceLocation()
        } else {
            isLocating = false
            Toast.makeText(
                context,
                if (isUrdu) "مقام کی اجازت درکار ہے" else "Location permission denied",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Khata calculations
    val totalExpense = remember(khataEntries) {
        khataEntries.filter { it.type == KhataType.EXPENSE }.sumOf { it.amount }
    }
    val totalIncome = remember(khataEntries) {
        khataEntries.filter { it.type == KhataType.INCOME }.sumOf { it.amount }
    }
    val netProfit = totalIncome - totalExpense
    val numberFormat = remember { NumberFormat.getNumberInstance(Locale.US) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Top App Header Bar
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isUrdu) "کسان بُک" else if (isPashto) "کِسان بُک" else "Kisaan Book",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = if (isUrdu) "کسانوں کا اپنا زرعی اور مالیاتی مرکز" else "Farmer's Agricultural & Financial Hub",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Location Pill
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable {
                                        isLocating = true
                                        locationPermissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            )
                                        )
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = "Location",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = selectedDistrict.ifBlank { "Mardan" },
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    if (isLocating) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(12.dp),
                                            strokeWidth = 2.dp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }

                            // Notification Icon
                            IconButton(
                                onClick = onNotificationClick,
                                modifier = Modifier.testTag("home_notif_button")
                            ) {
                                BadgedBox(
                                    badge = {
                                        if (unreadNotifCount > 0) {
                                            Badge { Text(unreadNotifCount.toString()) }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Notifications,
                                        contentDescription = "Notifications",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Compact Weather Section (Today First, Dropdown for Coming Days, Weather Icons, No Humidity/Breeze)
        item {
            CompactWeatherCard(
                district = selectedDistrict,
                isUrdu = isUrdu,
                isPashto = isPashto,
                isExpanded = weatherExpanded,
                onToggleExpand = { weatherExpanded = !weatherExpanded }
            )
        }

        // 3. Feature 1: Plant Doctor / Crop Problem Scanner (Camera / Picture AI)
        item {
            PaddingBox {
                FeatureHubCard(
                    title = if (isUrdu) "پودوں کا ڈاکٹر اور بیماری کی تشخیص" else if (isPashto) "د بوټو ډاکټر او د ناروغۍ تشخیص" else "Plant Doctor & Crop Problem Scanner",
                    subtitle = if (isUrdu) "کیمرہ سے پودے یا پتے کی تصویر لیں، فوری بیماری، علامات اور مجاز ادویات جانیں" else "Take a picture of damaged crop/leaf to instantly identify disease, symptoms, and sprays",
                    icon = Icons.Default.CameraAlt,
                    badgeText = if (isUrdu) "کیمرہ تشخیص" else "Camera Scanner",
                    primaryActionText = if (isUrdu) "فصل معائنہ شروع کریں" else "Open Plant Doctor",
                    gradientColors = listOf(Color(0xFF2E7D32), Color(0xFF1B5E20)),
                    onClick = onOpenPlantDoctor
                )
            }
        }

        // 4. Feature 2: My Farm Crops Planner (Zameen-dari)
        item {
            PaddingBox {
                FarmCropsHubCard(
                    crops = farmCrops,
                    isUrdu = isUrdu,
                    isPashto = isPashto,
                    onOpenAllCrops = onOpenMyCrops,
                    onCreateCrop = onOpenCreateCrop
                )
            }
        }

        // 5. Feature 3: Zameen-dari Khata (Farm Expense & Income Ledger)
        item {
            PaddingBox {
                KhataHubCard(
                    netProfit = netProfit,
                    totalExpense = totalExpense,
                    totalIncome = totalIncome,
                    entryCount = khataEntries.size,
                    numberFormat = numberFormat,
                    isUrdu = isUrdu,
                    isPashto = isPashto,
                    onOpenKhata = onOpenKhata,
                    onAddEntry = onOpenAddKhataEntry
                )
            }
        }

        // 6. Feature 4: Pakistan Crop Calendar (Gregorian, Hijri, Pashtun/Desi)
        item {
            PaddingBox {
                FeatureHubCard(
                    title = if (isUrdu) "پاکستان زرعی کلینڈر" else if (isPashto) "د پاکستان کرنیزه جنتري" else "Pakistan Agricultural Calendar",
                    subtitle = if (isUrdu) "عیسوی، ہجری اور پشتون/دیسی کلینڈر کے مطابق بوائی اور کٹائی کے اوقات" else "Gregorian, Hijri, and Pashtun / Desi calendar with provincial crop guides",
                    icon = Icons.Default.CalendarMonth,
                    badgeText = if (isUrdu) "پشتون و دیسی" else "3 Calendars",
                    primaryActionText = if (isUrdu) "زرعی کلینڈر کھولیں" else "Open Calendar",
                    gradientColors = listOf(Color(0xFF00796B), Color(0xFF004D40)),
                    onClick = onOpenCalendar
                )
            }
        }
    }
}

@Composable
private fun PaddingBox(content: @Composable () -> Unit) {
    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        content()
    }
}

/**
 * Compact Weather Card:
 * - Priority on Today's weather
 * - Dropdown expand icon for coming days of the week
 * - Shows sun, cloud, half sun icons
 * - Hides humidity and gentle breeze
 */
@Composable
fun CompactWeatherCard(
    district: String,
    isUrdu: Boolean,
    isPashto: Boolean,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Main Today Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Weather Icon (Sun / Half Sun / Cloud)
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFFFF3E0),
                        modifier = Modifier.size(52.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = "Sunny",
                                tint = Color(0xFFF57C00),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "24°C",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isUrdu) "آج، معتدل دھوپ" else if (isPashto) "نن، نمر لرونکی" else "Today, Sunny",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "${district.ifBlank { "Mardan" }} • " + if (isUrdu) "فصلوں کے لیے سازگار موسم" else if (isPashto) "د فصلونو لپاره مناسب موسم" else "Good farming conditions",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                IconButton(onClick = onToggleExpand) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle Forecast",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Expandable Coming Days of the Week
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    Text(
                        text = if (isUrdu) "ہفتہ وار موسمی صورتحال" else if (isPashto) "د اونۍ موسم حال" else "Weekly Weather Forecast",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Current Day Highlight Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WbSunny,
                                    contentDescription = null,
                                    tint = Color(0xFFF57C00),
                                    modifier = Modifier.size(22.dp)
                                )
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (isUrdu) "آج (موجودہ دن)" else if (isPashto) "نن (اوسنۍ ورځ)" else "Today (Current)",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = MaterialTheme.colorScheme.primary
                                        ) {
                                            Text(
                                                text = if (isUrdu) "موجودہ" else if (isPashto) "اوسنی" else "Now",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            Text(
                                text = "24°C / 13°C",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    val comingDays = listOf(
                        ForecastDay(if (isUrdu) "کل (منگل)" else if (isPashto) "سبا (نهه)" else "Tomorrow (Tue)", "25°C / 12°C", Icons.Default.WbSunny, Color(0xFFF57C00)),
                        ForecastDay(if (isUrdu) "بدھ" else if (isPashto) "شورو" else "Wednesday", "23°C / 13°C", Icons.Default.WbCloudy, Color(0xFF78909C)),
                        ForecastDay(if (isUrdu) "جمعرات" else if (isPashto) "زیارت" else "Thursday", "22°C / 11°C", Icons.Default.CloudQueue, Color(0xFF5C6BC0)),
                        ForecastDay(if (isUrdu) "جمعہ" else if (isPashto) "جمعه" else "Friday", "26°C / 14°C", Icons.Default.WbSunny, Color(0xFFF57C00)),
                        ForecastDay(if (isUrdu) "ہفتہ" else if (isPashto) "خالي" else "Saturday", "24°C / 12°C", Icons.Default.WbSunny, Color(0xFFF57C00)),
                        ForecastDay(if (isUrdu) "اتوار" else if (isPashto) "اتوار" else "Sunday", "21°C / 10°C", Icons.Default.WbCloudy, Color(0xFF78909C))
                    )

                    comingDays.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = null,
                                    tint = item.iconColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = item.day,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Text(
                                text = item.tempRange,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class ForecastDay(
    val day: String,
    val tempRange: String,
    val icon: ImageVector,
    val iconColor: Color
)

/**
 * Standard High-Impact Feature Hub Card
 */
@Composable
fun FeatureHubCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeText: String,
    primaryActionText: String,
    gradientColors: List<Color>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(gradientColors))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.88f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = gradientColors.first()
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = primaryActionText,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

/**
 * Farm Crops Hub Card (Zameen-dari)
 */
@Composable
fun FarmCropsHubCard(
    crops: List<FarmCrop>,
    isUrdu: Boolean,
    isPashto: Boolean,
    onOpenAllCrops: () -> Unit,
    onCreateCrop: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Agriculture,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Column {
                        Text(
                            text = if (isUrdu) "میری فصلیں (Crop Planner)" else if (isPashto) "زما فصلونه" else "My Farm Crops",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isUrdu) "${crops.size} فصلیں زیر کاشت" else "${crops.size} active crops",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                FilledTonalButton(
                    onClick = onCreateCrop,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isUrdu) "نئی فصل" else "Add Crop", fontSize = 12.sp)
                }
            }

            if (crops.isEmpty()) {
                Text(
                    text = if (isUrdu) "ابھی کوئی فصل درج نہیں ہے۔ نئی فصل شامل کریں تاکہ کھاد اور سپرے کی شیڈولنگ مل سکے۔" else "No crops added yet. Add your crop for tailored nutrients and medicine schedules.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                crops.take(2).forEach { crop ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenAllCrops() },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${crop.cropNameUrdu} (${crop.variety})",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "${crop.areaAcres} " + if (isUrdu) "ایکڑ" else "Acres",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            LinearProgressIndicator(
                                progress = { crop.progressPercent },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = crop.currentStageUrdu,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = if (isUrdu) "${crop.remainingDays} دن باقی" else "${crop.remainingDays} days left",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            OutlinedButton(
                onClick = onOpenAllCrops,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (isUrdu) "فصلوں کی مکمل فہرست و شیڈول دیکھیں" else "View All Crops & Schedules")
            }
        }
    }
}

/**
 * Zameen-dari Khata Hub Card
 */
@Composable
fun KhataHubCard(
    netProfit: Long,
    totalExpense: Long,
    totalIncome: Long,
    entryCount: Int,
    numberFormat: NumberFormat,
    isUrdu: Boolean,
    isPashto: Boolean,
    onOpenKhata: () -> Unit,
    onAddEntry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFE8F5E9),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = if (isUrdu) "زمین داری کھاتہ (Farm Ledger)" else if (isPashto) "د ځمکه دارۍ حساب" else "Zameen-dari Khata",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isUrdu) "$entryCount اندراجات محفوظ" else "$entryCount recorded entries",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                FilledTonalButton(
                    onClick = onAddEntry,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isUrdu) "نیا اندراج" else "Add Entry", fontSize = 12.sp)
                }
            }

            // Financial Summary KPI Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Expense Box
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = if (isUrdu) "کل خرچ" else "Total Expense",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFC62828)
                        )
                        Text(
                            text = "₨ ${numberFormat.format(totalExpense)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC62828),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Income Box
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = if (isUrdu) "کل آمدن" else "Total Income",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF2E7D32)
                        )
                        Text(
                            text = "₨ ${numberFormat.format(totalIncome)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Net Profit Box
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (netProfit >= 0) Color(0xFFE3F2FD) else Color(0xFFFFEBEE)
                    )
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = if (isUrdu) "خالص نفع" else "Net Profit",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (netProfit >= 0) Color(0xFF1565C0) else Color(0xFFC62828)
                        )
                        Text(
                            text = "₨ ${numberFormat.format(netProfit)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (netProfit >= 0) Color(0xFF1565C0) else Color(0xFFC62828),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = onOpenKhata,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (isUrdu) "مکمل کھاتہ و تفصیلات دیکھیں" else "Open Full Ledger & Reports")
            }
        }
    }
}
