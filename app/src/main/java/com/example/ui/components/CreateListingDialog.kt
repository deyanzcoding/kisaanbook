package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListingDialog(
    userDistrict: String,
    onDismiss: () -> Unit,
    onPublish: (
        title: String,
        category: String,
        price: Long,
        unit: String,
        description: String,
        district: String,
        phone: String,
        quantity: String?,
        imageRes: Int?
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Seeds") }
    var priceStr by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("فی 50 کلو بوری") }
    var description by remember { mutableStateOf("") }
    var district by remember { mutableStateOf(userDistrict.split(",").firstOrNull()?.trim() ?: "Mardan") }
    var phone by remember { mutableStateOf("03001234567") }
    var quantity by remember { mutableStateOf("50 بوریاں") }
    var selectedImageRes by remember { mutableStateOf<Int?>(null) }

    // Machinery specific fields
    var tractorModel by remember { mutableStateOf("Millat MF 385") }
    var horsepower by remember { mutableStateOf("85 HP") }
    var machineryCondition by remember { mutableStateOf("بہترین زیرِ استعمال") }

    val categories = listOf("Seeds", "Tractor & Machinery", "Fertilizers", "Pesticides", "Tools", "Produce")

    // Automatic adjustment of unit when category changes
    val isMachinery = category == "Tractor & Machinery"

    LaunchedEffect(category) {
        if (isMachinery) {
            unit = "مکمل ٹریکٹر / یونٹ"
            quantity = "1 یونٹ"
        } else if (category == "Seeds" || category == "Fertilizers") {
            unit = "فی 50 کلو بوری"
            quantity = "50 بوریاں"
        } else if (category == "Pesticides") {
            unit = "فی 1 لیٹر بوتل"
            quantity = "20 بوتلیں"
        } else {
            unit = "فی من (40 کلو)"
            quantity = "10 من"
        }
    }

    val sampleImages = listOf(
        Pair("Tractor", R.drawable.img_pakistan_tractor),
        Pair("Wheat Seed", R.drawable.img_wheat_field),
        Pair("Maize Seed", R.drawable.img_maize_harvest)
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(22.dp)),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isMachinery) "ٹریکٹر یا زرعی مشینری برائے فروخت" else "نیا زرعی پروڈکٹ برائے فروخت",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Category selector chips
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "کیٹیگری منتخب کریں",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(categories) { cat ->
                                val isSelected = cat == category
                                val displayCat = when (cat) {
                                    "Seeds" -> "بیج (Seeds)"
                                    "Tractor & Machinery" -> "ٹریکٹر و مشینری"
                                    "Fertilizers" -> "کھادیں"
                                    "Pesticides" -> "زرعی ادویات"
                                    "Tools" -> "آلات و اوزار"
                                    else -> "فصل و اجناس"
                                }
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { category = cat },
                                    label = { Text(displayCat, fontSize = 12.sp) },
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }
                    }

                    // Title
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text(if (isMachinery) "ٹریکٹر کا نام و ماڈل (مثلاً Fiat 480 ماڈل 2020)" else "پروڈکٹ کا نام") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("listing_title_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Dynamic Machinery Inputs
                    if (isMachinery) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "مشینری کی مخصوص تفصیلات",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = horsepower,
                                        onValueChange = { horsepower = it },
                                        label = { Text("ہارس پاور (HP)") },
                                        placeholder = { Text("مثلاً 85 HP") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        shape = RoundedCornerShape(10.dp)
                                    )

                                    OutlinedTextField(
                                        value = machineryCondition,
                                        onValueChange = { machineryCondition = it },
                                        label = { Text("حالت (Condition)") },
                                        placeholder = { Text("مثلاً بہترین، نیا") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Price & Unit
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = priceStr,
                            onValueChange = { if (it.all { c -> c.isDigit() }) priceStr = it },
                            label = { Text(if (isMachinery) "کل قیمت (روپے)" else "قیمت (روپے)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("listing_price_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        // If machinery, unit is locked to tractor/unit. If other, show appropriate units!
                        if (isMachinery) {
                            OutlinedTextField(
                                value = "مکمل ٹریکٹر / یونٹ",
                                onValueChange = {},
                                label = { Text("یونٹ") },
                                enabled = false,
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                        } else {
                            OutlinedTextField(
                                value = unit,
                                onValueChange = { unit = it },
                                label = { Text("یونٹ (مثلاً بوری، کلو)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    // Phone & District
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("فون نمبر (رابطہ و واٹس ایپ)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = district,
                            onValueChange = { district = it },
                            label = { Text("ضلع / علاقہ") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Description
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text(if (isMachinery) "ٹریکٹر کی حالت، انجن کی طاقت اور تفصیلات" else "مصنوعات کی تفصیلات") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 80.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Sample Photo Picker
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "تصویر منسلک کریں (اختیاری)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(sampleImages) { (label, res) ->
                                val isSelected = selectedImageRes == res
                                Box(
                                    modifier = Modifier
                                        .size(76.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(
                                            width = if (isSelected) 3.dp else 1.dp,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { selectedImageRes = res }
                                ) {
                                    Image(
                                        painter = painterResource(id = res),
                                        contentDescription = label,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("منسوخ")
                    }

                    val canPublish = title.isNotBlank() && (priceStr.toLongOrNull() ?: 0L) > 0L

                    Button(
                        onClick = {
                            val parsedPrice = priceStr.toLongOrNull() ?: 0L
                            val finalDesc = if (isMachinery) {
                                "${description.trim()}\n(ماڈل: $tractorModel • طاقت: $horsepower • حالت: $machineryCondition)"
                            } else description.trim()

                            onPublish(
                                title.trim(),
                                category,
                                parsedPrice,
                                unit.trim(),
                                finalDesc,
                                district.trim(),
                                phone.trim(),
                                quantity.trim(),
                                selectedImageRes ?: if (isMachinery) R.drawable.img_pakistan_tractor else null
                            )
                        },
                        enabled = canPublish,
                        modifier = Modifier
                            .weight(1.5f)
                            .testTag("publish_listing_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("اشتہار شائع کریں", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
