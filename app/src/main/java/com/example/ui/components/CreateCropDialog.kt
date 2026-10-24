package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.FarmCrop
import com.example.model.MedicineItem
import com.example.model.NutrientItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCropDialog(
    userDistrict: String,
    onDismiss: () -> Unit,
    onCropCreated: (FarmCrop) -> Unit
) {
    val context = LocalContext.current
    var selectedCropName by remember { mutableStateOf("Wheat") }
    var varietyInput by remember { mutableStateOf("Fakhr-e-Bhakkar") }
    var areaInput by remember { mutableStateOf("4.0") }
    var selectedSoil by remember { mutableStateOf("Loam (زرخیز میرا مٹی)") }
    var districtInput by remember { mutableStateOf(userDistrict.split(",").firstOrNull()?.trim() ?: "Mardan") }

    val cropOptions = listOf(
        "Wheat" to "گندم",
        "Maize" to "مکئی",
        "Cotton" to "کپاس",
        "Rice" to "چاول / دھان",
        "Sugarcane" to "کماد / گنا",
        "Potato" to "آلو",
        "Tomato" to "ٹماٹر",
        "Onion" to "پیاز"
    )

    val soilOptions = listOf(
        "Loam (زرخیز میرا مٹی)",
        "Clay Loam (بھاری پکی مٹی)",
        "Sandy Loam (ہلکی ریتلی مٹی)",
        "Canal Command (نہری زرخیز رقبہ)"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
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
                        Text("Add New Farm Crop", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("نئی فصل کا اندراج کریں", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                // Crop Selection
                Text("Select Crop / فصل منتخب کریں:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(cropOptions) { (en, ur) ->
                        val isSelected = en == selectedCropName
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedCropName = en
                                varietyInput = when (en) {
                                    "Wheat" -> "Fakhr-e-Bhakkar"
                                    "Maize" -> "Pioneer P1429"
                                    "Cotton" -> "CKC-01"
                                    "Rice" -> "Super Basmati 515"
                                    "Sugarcane" -> "CPF-246"
                                    "Potato" -> "Mozika"
                                    else -> "Desi Certified"
                                }
                            },
                            label = { Text("$en ($ur)") },
                            leadingIcon = { Icon(Icons.Default.Eco, null, Modifier.size(15.dp)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }

                // Variety
                OutlinedTextField(
                    value = varietyInput,
                    onValueChange = { varietyInput = it },
                    label = { Text("Variety / قسم") },
                    placeholder = { Text("e.g. Fakhr-e-Bhakkar, Dilkash-20, Pioneer") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // Area in Acres
                OutlinedTextField(
                    value = areaInput,
                    onValueChange = { areaInput = it },
                    label = { Text("Land Area (Acres / ایکڑ)") },
                    placeholder = { Text("e.g. 5.0") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // Soil Type
                Text("Soil Type / زمین کی قسم:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(soilOptions) { soil ->
                        val isSelected = soil == selectedSoil
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedSoil = soil },
                            label = { Text(soil, fontSize = 11.sp) }
                        )
                    }
                }

                // District / Location
                OutlinedTextField(
                    value = districtInput,
                    onValueChange = { districtInput = it },
                    label = { Text("District & Area / ضلع") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // Create Action Button
                Button(
                    onClick = {
                        val acres = areaInput.toDoubleOrNull() ?: 1.0
                        val duration = when (selectedCropName) {
                            "Wheat" -> 150
                            "Maize" -> 110
                            "Rice" -> 120
                            "Cotton" -> 180
                            "Sugarcane" -> 300
                            else -> 90
                        }

                        val nutrients = listOf(
                            NutrientItem("DAP (Di-ammonium Phosphate)", "ڈی اے پی", "At Sowing", "1 Bag / Acre", "Root development and seed establishment"),
                            NutrientItem("Urea (Nitrogen First Split)", "یوریا پہلی قسط", "At First Irrigation", "1 Bag / Acre", "Vegetative growth and shoot vigor"),
                            NutrientItem("SOP / MOP (Potash)", "پوٹاش کھاد", "Booting / Pre-Flowering", "12.5 kg / Acre", "Grain weight, size, and stress resilience"),
                            NutrientItem("Zinc Sulfate 33%", "زنک سلفیٹ", "Tillering Stage", "6 kg / Acre", "Prevents leaf bronzing and increases yield")
                        )

                        val medicines = listOf(
                            MedicineItem("Weed Infestation", "جڑی بوٹیاں", "Narrow & Broad-spectrum Herbicide", "25–35 days after sowing", "Strictly in sunny hours"),
                            MedicineItem("Fungal Rust / Leaf Blight", "پھپھوندی اور زنگ", "Propiconazole 25 EC", "On symptom appearance", "100ml / 100L water per acre")
                        )

                        val tips = listOf(
                            "Soil in $districtInput responds well to balanced phosphorus at sowing.",
                            "Avoid over-irrigation during damp morning fog to keep fungus at bay.",
                            "Consult local extension workers if weather shifts unseasonably warm."
                        )

                        val newCrop = FarmCrop(
                            id = "crop_${System.currentTimeMillis()}",
                            cropName = selectedCropName,
                            cropNameUrdu = cropOptions.find { it.first == selectedCropName }?.second ?: selectedCropName,
                            variety = varietyInput.ifBlank { "Certified" },
                            sowingDate = "Today",
                            areaAcres = acres,
                            soilType = selectedSoil,
                            district = districtInput,
                            province = if (districtInput.contains("KP", true) || districtInput.contains("Mardan", true) || districtInput.contains("Swat", true)) "KPK" else "Punjab",
                            currentStage = "Early Sowing / Germination",
                            currentStageUrdu = "بوائی اور اگاؤ کا مرحلہ",
                            durationDays = duration,
                            daysElapsed = 1,
                            weatherTips = tips,
                            nutrientsSchedule = nutrients,
                            medicineSchedule = medicines,
                            estimatedYield = "${(acres * 40).toInt()} Maunds"
                        )

                        onCropCreated(newCrop)
                        Toast.makeText(context, "$selectedCropName crop registered successfully!", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_create_crop_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Eco, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Register Crop (فصل درج کریں)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
