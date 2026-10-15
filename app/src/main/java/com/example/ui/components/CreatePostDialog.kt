package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.model.PostType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostDialog(
    userDistrict: String,
    onDismiss: () -> Unit,
    onPublish: (text: String, category: String, postType: PostType, district: String, imageRes: Int?) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(PostType.GENERAL) }
    var selectedCategory by remember { mutableStateOf("Wheat") }
    var selectedDistrict by remember { mutableStateOf(userDistrict.split(",").firstOrNull()?.trim() ?: "Mardan") }
    var selectedImageRes by remember { mutableStateOf<Int?>(null) }

    val sampleImageOptions = listOf(
        Pair("Wheat Field", R.drawable.img_wheat_field),
        Pair("Maize Harvest", R.drawable.img_maize_harvest),
        Pair("Tractor Farm", R.drawable.img_pakistan_tractor)
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.9f)
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
                        text = "Create Farm Post",
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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Post Type Selection
                    Text(
                        text = "Post Type",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(PostType.entries) { type ->
                            val isSelected = type == selectedType
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedType = type },
                                label = { Text(type.labelEn) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }

                    // Crop Category Selection
                    Text(
                        text = "Crop Category",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(SampleData.cropCategories.filter { it != "All Crops" }) { cat ->
                            val isSelected = cat == selectedCategory
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCategory = cat },
                                label = { Text(cat) }
                            )
                        }
                    }

                    // District Selection
                    Text(
                        text = "District / Location",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(SampleData.pakistaniDistricts.take(8)) { dist ->
                            val isSelected = dist == selectedDistrict
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedDistrict = dist },
                                label = { Text(dist) }
                            )
                        }
                    }

                    // Description text input
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("What is happening on your farm?") },
                        placeholder = { Text("Describe crop conditions, ask advice, or share harvesting updates...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .testTag("create_post_text_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Photo Attachment Options
                    Text(
                        text = "Attach Farm Photo",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // None option
                        item {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selectedImageRes == null) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedImageRes = null },
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "No Photo",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        // Sample farm photos
                        items(sampleImageOptions) { (label, resId) ->
                            val isSelected = selectedImageRes == resId
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedImageRes = resId }
                                    .border(
                                        width = if (isSelected) 2.dp else 0.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else androidx.compose.ui.graphics.Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                            ) {
                                Image(
                                    painter = painterResource(id = resId),
                                    contentDescription = label,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Publish Action
                Button(
                    onClick = {
                        onPublish(
                            text,
                            selectedCategory,
                            selectedType,
                            selectedDistrict,
                            selectedImageRes
                        )
                    },
                    enabled = text.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("publish_post_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Publish to Feed",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
