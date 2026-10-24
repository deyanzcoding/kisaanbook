package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.localization.AppLanguage
import com.example.model.FarmCrop
import com.example.model.KhataEntry
import com.example.model.KhataType
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddKhataEntryDialog(
    crops: List<FarmCrop>,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (KhataEntry) -> Unit
) {
    var selectedType by remember { mutableStateOf(KhataType.EXPENSE) }
    var amountText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCropId by remember { mutableStateOf(crops.firstOrNull()?.id ?: "") }
    var selectedCategory by remember { mutableStateOf("Seed") }

    val isUrdu = language == AppLanguage.URDU
    val isPashto = language == AppLanguage.PASHTO

    val categories = listOf(
        "Seed" to if (isUrdu) "بیج" else if (isPashto) "تخم" else "Seed",
        "Fertilizer" to if (isUrdu) "کھاد" else if (isPashto) "کود" else "Fertilizer",
        "Land Prep" to if (isUrdu) "زمین تیاری / ٹریکٹر" else if (isPashto) "ځمکه چمتووالی" else "Land Prep",
        "Spray / Medicine" to if (isUrdu) "سپرے / زرعی دوا" else if (isPashto) "درمل" else "Spray / Medicine",
        "Tube Well / Water" to if (isUrdu) "پانی / ٹیوب ویل" else if (isPashto) "اوبه" else "Water",
        "Labor" to if (isUrdu) "مزدوری" else if (isPashto) "مزدوري" else "Labor",
        "Harvest Sale" to if (isUrdu) "فصل فروخت" else if (isPashto) "حاصل پلور" else "Harvest Sale",
        "Other" to if (isUrdu) "دیگر" else if (isPashto) "نور" else "Other"
    )

    val titleText = when {
        isUrdu -> "نیا اندراج شامل کریں"
        isPashto -> "نوې اندراج اضافه کړئ"
        else -> "Add New Ledger Entry"
    }

    val expenseLabel = when {
        isUrdu -> "خرچ (Expense)"
        isPashto -> "مصرف (Expense)"
        else -> "Expense"
    }

    val incomeLabel = when {
        isUrdu -> "آمدن (Income)"
        isPashto -> "عاید (Income)"
        else -> "Income"
    }

    val amountLabel = when {
        isUrdu -> "رقم (روپے)"
        isPashto -> "رقم (روپۍ)"
        else -> "Amount (PKR)"
    }

    val descLabel = when {
        isUrdu -> "تفصیل یا مقصد (مثلاً 2 بوری ڈی اے پی)"
        isPashto -> "تفصیل یا مقصد"
        else -> "Description / Purpose"
    }

    val cropLabel = when {
        isUrdu -> "متعلقہ فصل منتخب کریں"
        isPashto -> "اړونده فصل غوره کړئ"
        else -> "Select Associated Crop"
    }

    val categoryLabel = when {
        isUrdu -> "کیٹیگری"
        isPashto -> "کټګورۍ"
        else -> "Category"
    }

    val saveLabel = when {
        isUrdu -> "محفوظ کریں"
        isPashto -> "خوندي کړئ"
        else -> "Save Entry"
    }

    val cancelLabel = when {
        isUrdu -> "منسوخ"
        isPashto -> "لغوه کول"
        else -> "Cancel"
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = titleText,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }

                // Type Toggle (Expense vs Income)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { selectedType = KhataType.EXPENSE },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedType == KhataType.EXPENSE) Color(0xFFC62828) else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (selectedType == KhataType.EXPENSE) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(expenseLabel, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { selectedType = KhataType.INCOME },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedType == KhataType.INCOME) Color(0xFF2E7D32) else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (selectedType == KhataType.INCOME) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(incomeLabel, fontWeight = FontWeight.Bold)
                    }
                }

                // Amount
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { if (it.all { char -> char.isDigit() }) amountText = it },
                    label = { Text(amountLabel) },
                    leadingIcon = {
                        Icon(Icons.Default.Payments, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(descLabel) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp)
                )

                // Crop selection
                if (crops.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = cropLabel,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(crops) { crop ->
                                val isSelected = crop.id == selectedCropId
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedCropId = crop.id },
                                    label = { Text(crop.cropName) },
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }
                    }
                }

                // Category selector
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = categoryLabel,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { (catKey, catDisplayName) ->
                            val isSelected = catKey == selectedCategory
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCategory = catKey },
                                label = { Text(catDisplayName) },
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                }

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(cancelLabel)
                    }

                    Button(
                        onClick = {
                            val parsedAmount = amountText.toLongOrNull() ?: 0L
                            if (parsedAmount > 0L && description.isNotBlank()) {
                                val selectedCrop = crops.find { it.id == selectedCropId }
                                val newEntry = KhataEntry(
                                    id = UUID.randomUUID().toString(),
                                    cropId = selectedCropId.ifBlank { null },
                                    cropName = selectedCrop?.cropName ?: if (isUrdu) "عام فارم" else "General Farm",
                                    type = selectedType,
                                    category = selectedCategory,
                                    amount = parsedAmount,
                                    description = description.trim(),
                                    date = if (isUrdu) "آج" else "Today"
                                )
                                onSave(newEntry)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = (amountText.toLongOrNull() ?: 0L) > 0L && description.isNotBlank(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(saveLabel)
                    }
                }
            }
        }
    }
}
