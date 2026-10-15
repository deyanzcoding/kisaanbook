package com.example.ui.components

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.SampleData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AskQuestionDialog(
    userDistrict: String,
    onDismiss: () -> Unit,
    onSubmit: (title: String, description: String, category: String, district: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Wheat") }
    var district by remember { mutableStateOf(userDistrict.split(",").firstOrNull()?.trim() ?: "Mardan") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.85f)
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
                        text = "Ask Farming Community",
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
                    // Question Title
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Question Title / خلاصہ") },
                        placeholder = { Text("e.g. Yellow spots on wheat leaves after morning fog") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("question_title_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Crop Category
                    Text("Crop Category", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(SampleData.cropCategories.filter { it != "All Crops" }) { cat ->
                            FilterChip(
                                selected = cat == category,
                                onClick = { category = cat },
                                label = { Text(cat) }
                            )
                        }
                    }

                    // District
                    Text("District / Location", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(SampleData.pakistaniDistricts.take(8)) { dist ->
                            FilterChip(
                                selected = dist == district,
                                onClick = { district = dist },
                                label = { Text(dist) }
                            )
                        }
                    }

                    // Detailed Description
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Details & Symptoms / تفصیلات") },
                        placeholder = { Text("Mention crop age, soil type, irrigation schedule, and recent sprays...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .testTag("question_desc_input"),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        onSubmit(title, description, category, district)
                    },
                    enabled = title.isNotBlank() && description.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_question_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Post Question", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}
