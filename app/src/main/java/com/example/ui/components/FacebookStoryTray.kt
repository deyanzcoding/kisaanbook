package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.R
import com.example.model.Farmer
import com.example.model.FarmerStory

@Composable
fun FacebookStoryTray(
    stories: List<FarmerStory>,
    currentUser: Farmer,
    onCreateStoryClick: () -> Unit,
    onStoryClick: (FarmerStory) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 10.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // "Create Story" Card (Facebook Style)
        item {
            Card(
                modifier = Modifier
                    .width(105.dp)
                    .height(170.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onCreateStoryClick() }
                    .testTag("create_story_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Upper half background
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.68f)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        if (currentUser.avatarUri != null) {
                            AsyncImage(
                                model = currentUser.avatarUri,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text(
                                text = currentUser.avatarInitials,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 28.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    // Lower half with + icon and label
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .offset(y = (-6).dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Story",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Text(
                            text = "Add Story",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "فارم کی کہانی",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Farmer Stories list
        items(stories, key = { it.id }) { story ->
            Card(
                modifier = Modifier
                    .width(105.dp)
                    .height(170.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onStoryClick(story) }
                    .testTag("story_card_${story.id}"),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Background: Either image or text card background
                    if (story.storyType == "TEXT") {
                        val bgColor = Color(story.backgroundColor ?: 0xFF2E7D32)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(bgColor)
                                .padding(horizontal = 8.dp, vertical = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = story.caption,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 4,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else if (story.imageUri != null) {
                        AsyncImage(
                            model = story.imageUri,
                            contentDescription = story.caption,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else if (story.imageRes != 0) {
                        Image(
                            painter = painterResource(id = story.imageRes),
                            contentDescription = story.caption,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                        )
                    }

                    // Gradient overlay at bottom for readable text
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.35f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.85f)
                                    )
                                )
                            )
                    )

                    // Author DP at Top Left with ring
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(36.dp)
                            .align(Alignment.TopStart)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = story.avatarInitials,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Author Name & District at bottom
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = story.authorName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = story.authorDistrict,
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

/**
 * Functional Create Story Dialog: allows farmer to post Picture, Video, or Text story!
 */
@Composable
fun CreateStoryDialog(
    currentUser: Farmer,
    onDismiss: () -> Unit,
    onPostStory: (storyType: String, caption: String, imageRes: Int, imageUri: String?, bgColor: Long?) -> Unit
) {
    var selectedType by remember { mutableStateOf("PHOTO") } // PHOTO, VIDEO, TEXT
    var captionText by remember { mutableStateOf("") }
    var selectedPresetRes by remember { mutableIntStateOf(R.drawable.img_wheat_field) }
    var selectedBgColor by remember { mutableLongStateOf(0xFF86AF2F) }
    val context = LocalContext.current

    val presetColors = listOf(
        0xFF86AF2F, // Brand green
        0xFF2E7D32, // Dark forest
        0xFF1565C0, // Agri Blue
        0xFF795548, // Earth Soil
        0xFFE65100  // Harvest Amber
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Create Farm Story",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Story Type Toggle Tabs: Picture, Video, Text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedType == "PHOTO",
                        onClick = { selectedType = "PHOTO" },
                        label = { Text("Picture") },
                        leadingIcon = { Icon(Icons.Default.PhotoCamera, null, Modifier.size(16.dp)) },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = selectedType == "VIDEO",
                        onClick = { selectedType = "VIDEO" },
                        label = { Text("Video") },
                        leadingIcon = { Icon(Icons.Default.Videocam, null, Modifier.size(16.dp)) },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = selectedType == "TEXT",
                        onClick = { selectedType = "TEXT" },
                        label = { Text("Text") },
                        leadingIcon = { Icon(Icons.Default.TextFields, null, Modifier.size(16.dp)) },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Preview Box based on story type
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (selectedType == "TEXT") Color(selectedBgColor) else MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedType == "TEXT") {
                        Text(
                            text = captionText.ifBlank { "Type your farm lesson, advice, or announcement here..." },
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = selectedPresetRes),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        if (selectedType == "VIDEO") {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.6f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(28.dp))
                            }
                        }
                    }
                }

                // Controls depending on type
                if (selectedType == "TEXT") {
                    Text("Select Background Color:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        presetColors.forEach { colorVal ->
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(colorVal))
                                    .border(
                                        width = if (selectedBgColor == colorVal) 3.dp else 1.dp,
                                        color = if (selectedBgColor == colorVal) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedBgColor = colorVal }
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                selectedPresetRes = R.drawable.img_wheat_field
                                Toast.makeText(context, "Wheat field selected", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Wheat Farm", fontSize = 11.sp)
                        }
                        Button(
                            onClick = {
                                selectedPresetRes = R.drawable.img_maize_harvest
                                Toast.makeText(context, "Maize harvest selected", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Maize Harvest", fontSize = 11.sp)
                        }
                    }
                }

                // Caption input
                OutlinedTextField(
                    value = captionText,
                    onValueChange = { captionText = it },
                    label = { Text(if (selectedType == "TEXT") "Story Text" else "Caption") },
                    placeholder = { Text("What's happening on your farm today?") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 3
                )

                // Submit Button
                Button(
                    onClick = {
                        val finalCaption = if (captionText.isNotBlank()) captionText else "Fresh farm update from ${currentUser.name}"
                        onPostStory(
                            selectedType,
                            finalCaption,
                            if (selectedType == "TEXT") 0 else selectedPresetRes,
                            null,
                            if (selectedType == "TEXT") selectedBgColor else null
                        )
                        Toast.makeText(context, "Story posted successfully!", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Upload, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Post to Story", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Functional Story Viewer Dialog with Zoom in/out, Viewers Sheet, Likes, and Comments
 */
@Composable
fun StoryViewerDialog(
    stories: List<FarmerStory>,
    initialStory: FarmerStory,
    onDismiss: () -> Unit,
    onLikeStory: (storyId: String) -> Unit = {},
    onCommentStory: (storyId: String, text: String) -> Unit = { _, _ -> }
) {
    var currentIndex by remember(initialStory.id) {
        val idx = stories.indexOfFirst { it.id == initialStory.id }
        mutableIntStateOf(if (idx >= 0) idx else 0)
    }

    val currentStory = remember(currentIndex, stories) {
        if (stories.isNotEmpty() && currentIndex in stories.indices) stories[currentIndex] else initialStory
    }

    var scale by remember(currentIndex) { mutableFloatStateOf(1f) }
    var offset by remember(currentIndex) { mutableStateOf(Offset.Zero) }
    var showViewersSheet by remember { mutableStateOf(false) }
    var storyCommentText by remember { mutableStateOf("") }
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Main Content (Zoomable with pinch gestures)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(currentIndex) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(1f, 4f)
                                offset += pan
                            }
                        }
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationX = offset.x
                            translationY = offset.y
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (currentStory.storyType == "TEXT") {
                        val bgColor = Color(currentStory.backgroundColor ?: 0xFF2E7D32)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(bgColor)
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentStory.caption,
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                lineHeight = 32.sp
                            )
                        }
                    } else if (currentStory.imageUri != null) {
                        AsyncImage(
                            model = currentStory.imageUri,
                            contentDescription = currentStory.caption,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else if (currentStory.imageRes != 0) {
                        Image(
                            painter = painterResource(id = currentStory.imageRes),
                            contentDescription = currentStory.caption,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // Left and Right tap zones (When not zoomed in)
                if (scale <= 1.1f) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable {
                                    if (currentIndex > 0) currentIndex-- else onDismiss()
                                }
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable {
                                    if (currentIndex < stories.lastIndex) currentIndex++ else onDismiss()
                                }
                        )
                    }
                }

                // Top Bar with progress bars, author info, and controls
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(16.dp)
                ) {
                    // Multi-story progress bar line (Facebook style)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        stories.forEachIndexed { index, _ ->
                            val progress = when {
                                index < currentIndex -> 1f
                                index == currentIndex -> 1f
                                else -> 0f
                            }
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = Color.White,
                                trackColor = Color.White.copy(alpha = 0.3f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = currentStory.avatarInitials,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = currentStory.authorName,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "${currentStory.authorDistrict} • ${currentStory.timeAgo}",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Reset Zoom button if zoomed
                            if (scale > 1.05f) {
                                IconButton(onClick = { scale = 1f; offset = Offset.Zero }) {
                                    Icon(Icons.Default.ZoomOutMap, "Reset Zoom", tint = Color.White)
                                }
                            }
                            IconButton(onClick = onDismiss) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close Story",
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    }
                }

                // Bottom Interactive Panel: Caption, Viewers count button, Like & Comment bar
                Surface(
                    color = Color.Black.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Caption
                        if (currentStory.storyType != "TEXT" && currentStory.caption.isNotBlank()) {
                            Text(
                                text = currentStory.caption,
                                fontSize = 14.sp,
                                color = Color.White,
                                lineHeight = 20.sp
                            )
                        }

                        // Viewers and Likes info row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Viewers button (Seen by ...)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White.copy(alpha = 0.15f))
                                    .clickable { showViewersSheet = true }
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.Visibility, null, tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "Seen by ${currentStory.viewers.size}",
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            // Like count
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ThumbUp, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("${currentStory.likesCount} likes", fontSize = 11.sp, color = Color.White)
                            }
                        }

                        // Bottom Reply / Comment Input Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = storyCommentText,
                                onValueChange = { storyCommentText = it },
                                placeholder = { Text("Send reply to ${currentStory.authorName}...", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(20.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color.White,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
                                ),
                                singleLine = true
                            )

                            Spacer(Modifier.width(8.dp))

                            // Like Story Button
                            IconButton(
                                onClick = { onLikeStory(currentStory.id) },
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f))
                            ) {
                                Icon(
                                    imageVector = if (currentStory.isLiked) Icons.Default.ThumbUp else Icons.Default.ThumbUp,
                                    contentDescription = "Like Story",
                                    tint = if (currentStory.isLiked) MaterialTheme.colorScheme.primary else Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(Modifier.width(4.dp))

                            // Send Story Reply
                            IconButton(
                                onClick = {
                                    if (storyCommentText.isNotBlank()) {
                                        onCommentStory(currentStory.id, storyCommentText)
                                        Toast.makeText(context, "Reply sent to ${currentStory.authorName}!", Toast.LENGTH_SHORT).show()
                                        storyCommentText = ""
                                    }
                                },
                                enabled = storyCommentText.isNotBlank(),
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Send",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // Viewers Bottom Sheet
                if (showViewersSheet) {
                    AlertDialog(
                        onDismissRequest = { showViewersSheet = false },
                        title = { Text("Story Activity & Viewers") },
                        text = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 300.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("Seen by (${currentStory.viewers.size}):", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                if (currentStory.viewers.isEmpty()) {
                                    Text("No viewers yet.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                } else {
                                    LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        items(currentStory.viewers) { viewerName ->
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(28.dp)
                                                        .clip(CircleShape)
                                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(viewerName.take(2).uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                }
                                                Spacer(Modifier.width(8.dp))
                                                Text(viewerName, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                            }
                                        }
                                    }
                                }

                                if (currentStory.comments.isNotEmpty()) {
                                    Spacer(Modifier.height(8.dp))
                                    Text("Replies & Comments:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    currentStory.comments.forEach { c ->
                                        Text("• $c", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = { showViewersSheet = false }) {
                                Text("Close")
                            }
                        }
                    )
                }
            }
        }
    }
}
