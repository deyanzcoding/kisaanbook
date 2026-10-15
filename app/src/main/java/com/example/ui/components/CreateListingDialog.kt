package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.SampleData

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
    var unit by remember { mutableStateOf("50 kg bag") }
    var description by remember { mutableStateOf("") }
    var district by remember { mutableStateOf(userDistrict.split(",").firstOrNull()?.trim() ?: "Mardan") }
    var phone by remember { mutableStateOf("+92 300 1234567") }
    var quantity by remember { mutableStateOf("50 Bags") }
    var selectedImageRes by remember { mutableStateOf<Int?>(null) }

    val categories = listOf("Seeds", "Fertilizers", "Machinery", "Pesticides", "Tools", "Produce", "Services")
    val units = listOf("50 kg bag", "40 kg maund", "hour", "day", "acre", "kg", "bottle")

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
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(20.dp)),
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
                        text = "List Product / Machinery",
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
                    // Title
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Product or Service Title") },
                        placeholder = { Text("e.g. Certified Akbar-2019 Wheat Seed") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Category
                    Text("Category", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categories) { cat ->
                            FilterChip(
                                selected = cat == category,
                                onClick = { category = cat },
                                label = { Text(cat) }
                            )
                        }
                    }

                    // Price & Unit
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = priceStr,
                            onValueChange = { priceStr = it.filter { char -> char.isDigit() } },
                            label = { Text("Price (Rs.)") },
                            placeholder = { Text("4500") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = unit,
                            onValueChange = { unit = it },
                            label = { Text("Per Unit") },
                            placeholder = { Text("50 kg bag") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Available Quantity
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Available Stock / Quantity (Optional)") },
                        placeholder = { Text("e.g. 50 bags, 2 tractors") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // District
                    Text("Location / District", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(SampleData.pakistaniDistricts.take(8)) { dist ->
                            FilterChip(
                                selected = dist == district,
                                onClick = { district = dist },
                                label = { Text(dist) }
                            )
                        }
                    }

                    // Contact Phone
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Contact Phone / WhatsApp") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Description
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Product Description & Condition") },
                        placeholder = { Text("Describe germination rate, tractor condition, or pickup location...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Image Selection
                    Text("Attach Photo", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selectedImageRes == null) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedImageRes = null }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("No Photo", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                        items(sampleImages) { (label, resId) ->
                            val isSelected = selectedImageRes == resId
                            Image(
                                painter = painterResource(id = resId),
                                contentDescription = label,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(
                                        width = if (isSelected) 2.dp else 0.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedImageRes = resId }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val priceVal = priceStr.toLongOrNull() ?: 0L
                        onPublish(
                            title,
                            category,
                            priceVal,
                            unit,
                            description,
                            district,
                            phone,
                            quantity.ifBlank { null },
                            selectedImageRes
                        )
                    },
                    enabled = title.isNotBlank() && priceStr.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("publish_listing_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Publish to Marketplace", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}
