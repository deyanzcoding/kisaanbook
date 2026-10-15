package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.SampleData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateMandiRateDialog(
    initialDistrict: String,
    onDismiss: () -> Unit,
    onSaveRate: (cropName: String, cropNameUrdu: String, mandiLocation: String, minPrice: Int, maxPrice: Int, unit: String) -> Unit
) {
    var cropName by remember { mutableStateOf("Wheat (New Crop)") }
    var cropNameUrdu by remember { mutableStateOf("گندم (نئی فصل)") }
    var mandiLocation by remember { mutableStateOf("$initialDistrict Mandi") }
    var minPriceStr by remember { mutableStateOf("3950") }
    var maxPriceStr by remember { mutableStateOf("4200") }
    var unit by remember { mutableStateOf("40 kg (Maund)") }

    val presetCrops = listOf(
        Pair("Wheat", "گندم"),
        Pair("Basmati Rice", "چاول باسمتی"),
        Pair("Maize", "مکئی"),
        Pair("Cotton", "کپاس پھٹی"),
        Pair("Sugarcane", "کماد")
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .wrapContentHeight()
                .clip(RoundedCornerShape(20.dp)),
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
                            text = "Update Mandi Market Rate",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "منڈی کے نئے نرخ درج کریں",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                // Select Common Crop
                Text("Select Crop / فصل منتخب کریں", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(presetCrops) { (en, ur) ->
                        val isSelected = cropName.startsWith(en)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                cropName = "$en (Current Season)"
                                cropNameUrdu = ur
                            },
                            label = { Text("$en ($ur)") }
                        )
                    }
                }

                // Mandi Location
                Text("Mandi Location / منڈی کا مقام", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(SampleData.pakistaniDistricts.take(8)) { dist ->
                        FilterChip(
                            selected = mandiLocation.contains(dist),
                            onClick = { mandiLocation = "$dist Mandi" },
                            label = { Text(dist) }
                        )
                    }
                }

                OutlinedTextField(
                    value = mandiLocation,
                    onValueChange = { mandiLocation = it },
                    label = { Text("Mandi / Market Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Price range
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = minPriceStr,
                        onValueChange = { minPriceStr = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Min Price (Rs.)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = maxPriceStr,
                        onValueChange = { maxPriceStr = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Max Price (Rs.)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Unit
                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("Weight / Unit (e.g. 40 kg Maund)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        val min = minPriceStr.toIntOrNull() ?: 0
                        val max = maxPriceStr.toIntOrNull() ?: min
                        onSaveRate(cropName, cropNameUrdu, mandiLocation, min, max, unit)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("save_mandi_rate_button"),
                    shape = RoundedCornerShape(12.dp),
                    enabled = cropName.isNotBlank() && minPriceStr.isNotBlank() && mandiLocation.isNotBlank()
                ) {
                    Text("Publish Mandi Rate / محفوظ کریں", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}
