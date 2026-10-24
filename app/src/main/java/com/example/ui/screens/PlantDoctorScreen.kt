package com.example.ui.screens

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.model.Farmer
import com.example.model.PlantDiagnosis
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDoctorScreen(
    dealers: List<Farmer> = emptyList(),
    onBackClick: () -> Unit = {},
    onContactDealer: (Farmer) -> Unit = {}
) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<String?>(null) }
    var selectedImageRes by remember { mutableStateOf<Int?>(R.drawable.img_wheat_field) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var diagnosisResult by remember { mutableStateOf<PlantDiagnosis?>(null) }

    // Preset diagnostics for instant realistic field results
    val sampleDiagnoses = remember {
        listOf(
            PlantDiagnosis(
                problemName = "Yellow Rust (Stripe Rust)",
                problemNameUrdu = "گندم کا زرد زنگ",
                severity = "Moderate",
                symptoms = listOf(
                    "Linear rows of yellow-orange powdery pustules on leaves",
                    "Rapid spread during cool, damp mornings and foggy weather",
                    "Chlorosis and drying of infected leaf tissues"
                ),
                recommendedTreatment = "Stop excess nitrogen application. Spray certified triazole fungicides immediately on clear sunny hours.",
                recommendedSprays = listOf(
                    "Propiconazole 25% EC (100ml / 100L water per acre)",
                    "Tebuconazole + Trifloxystrobin (Nativo 75 WG)",
                    "Difenoconazole 250 EC"
                ),
                preventiveTips = listOf(
                    "Avoid overhead irrigation during foggy periods",
                    "Choose rust-resistant certified varieties (Fakhr-e-Bhakkar, Dilkash, Akbar-19)",
                    "Apply recommended balance of Potash at sowing"
                ),
                recommendedDealers = listOf("Hasnain Raza", "Shafiq Ahmad", "Gulzar Ahmad"),
                sampleImageRes = R.drawable.img_wheat_field
            ),
            PlantDiagnosis(
                problemName = "Fall Armyworm (سنڈی کا حملہ)",
                problemNameUrdu = "مکئی کی لشکری سنڈی",
                severity = "Critical",
                symptoms = listOf(
                    "Shot-hole feeding damage and ragged whorl leaves",
                    "Accumulation of moist sawdust-like frass inside central whorl",
                    "Larvae burrowing into developing cobs"
                ),
                recommendedTreatment = "Target whorl application in early morning or late evening. Biological trichogramma cards or selective larvicides.",
                recommendedSprays = listOf(
                    "Emamectin Benzoate 5% SG (80g / 100L water)",
                    "Chlorantraniliprole (Coragen 20 SC)",
                    "Spinetoram 11.7 SC"
                ),
                preventiveTips = listOf(
                    "Install pheromone traps to monitor adult moth arrival",
                    "Release Trichogramma cards at 10-day intervals",
                    "Destroy residue of previous crop"
                ),
                recommendedDealers = listOf("Hasnain Raza", "Bashir Ahmad"),
                sampleImageRes = R.drawable.img_maize_harvest
            )
        )
    }

    // Default to first diagnosis for initial display
    LaunchedEffect(Unit) {
        diagnosisResult = sampleDiagnoses.first()
    }

    fun saveBitmapToCache(bitmap: Bitmap): String? {
        return try {
            val file = File(context.cacheDir, "scan_crop_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            Uri.fromFile(file).toString()
        } catch (_: Exception) {
            null
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            val uri = saveBitmapToCache(bitmap)
            if (uri != null) {
                selectedImageUri = uri
                selectedImageRes = null
                isAnalyzing = true
                Toast.makeText(context, "Analyzing leaf patterns...", Toast.LENGTH_SHORT).show()
                diagnosisResult = sampleDiagnoses.first()
                isAnalyzing = false
            }
        }
    }

    val cameraPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Camera permission needed for crop scanning", Toast.LENGTH_SHORT).show()
        }
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri.toString()
            selectedImageRes = null
            diagnosisResult = sampleDiagnoses.last()
            Toast.makeText(context, "Photo loaded. Diagnosis updated!", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Crop Problem Scanner", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("فصل اور پودوں کے امراض کا سائنسی معائنہ", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Photo Preview & Capture Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Image Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedImageUri != null) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Scanned Leaf Photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else if (selectedImageRes != null) {
                            Image(
                                painter = painterResource(id = selectedImageRes!!),
                                contentDescription = "Sample Leaf",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(Icons.Default.Spa, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(64.dp))
                        }
                    }

                    // Action buttons (Camera / Gallery)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { cameraPermission.launch(Manifest.permission.CAMERA) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("take_crop_photo_button")
                        ) {
                            Icon(Icons.Outlined.PhotoCamera, null, Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Camera", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("pick_crop_photo_button")
                        ) {
                            Icon(Icons.Outlined.PhotoLibrary, null, Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Gallery", fontWeight = FontWeight.SemiBold)
                        }
                    }

                    // Quick Sample Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Sample Test:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        AssistChip(
                            onClick = {
                                selectedImageRes = R.drawable.img_wheat_field
                                selectedImageUri = null
                                diagnosisResult = sampleDiagnoses.first()
                            },
                            label = { Text("Wheat Rust", fontSize = 11.sp) }
                        )
                        AssistChip(
                            onClick = {
                                selectedImageRes = R.drawable.img_maize_harvest
                                selectedImageUri = null
                                diagnosisResult = sampleDiagnoses.last()
                            },
                            label = { Text("Maize Armyworm", fontSize = 11.sp) }
                        )
                    }
                }
            }

            // Diagnostic Results Section
            diagnosisResult?.let { result ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Title & Severity Tag
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = result.problemName,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = result.problemNameUrdu,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (result.severity == "Critical") Color(0xFFFFEBEE) else Color(0xFFFFF3E0)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Warning,
                                        contentDescription = null,
                                        tint = if (result.severity == "Critical") Color(0xFFD32F2F) else Color(0xFFE65100),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text = result.severity,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (result.severity == "Critical") Color(0xFFD32F2F) else Color(0xFFE65100)
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                        // Observed Symptoms
                        Text("Observed Symptoms:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        result.symptoms.forEach { symptom ->
                            Row(verticalAlignment = Alignment.Top) {
                                Text("• ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Text(symptom, fontSize = 12.sp, lineHeight = 17.sp)
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                        // Recommended Immediate Treatment
                        Text("Recommended Action / طریقہ علاج:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(
                            text = result.recommendedTreatment,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 19.sp
                        )

                        // Medicines / Sprays
                        Text("Certified Fungicide / Spray Options:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        result.recommendedSprays.forEach { spray ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Outlined.CheckCircle, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text(spray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                        // Contact Verified Agri-Dealers to purchase medicine
                        Text("Buy from Authorized Local Dealers:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        dealers.take(2).forEach { dealer ->
                            OutlinedCard(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.primaryContainer),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(dealer.avatarInitials, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Spacer(Modifier.width(10.dp))
                                        Column {
                                            Text(dealer.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            Text("Dokandar · ${dealer.district}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }

                                    Button(
                                        onClick = { onContactDealer(dealer) },
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(Icons.Default.Phone, null, Modifier.size(14.dp))
                                        Spacer(Modifier.width(4.dp))
                                        Text("Contact", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
