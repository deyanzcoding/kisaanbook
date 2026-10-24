package com.example.ui.components

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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.model.CommunityPost
import com.example.model.Farmer
import kotlinx.coroutines.delay
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

enum class RecordingState {
    IDLE, RECORDING, PAUSED, FINISHED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCommunityPostDialog(
    currentUser: Farmer,
    onDismiss: () -> Unit,
    onSubmitPost: (CommunityPost) -> Unit
) {
    val context = LocalContext.current
    var postText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Farmer problems") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Voice recording states
    var recordingState by remember { mutableStateOf(RecordingState.IDLE) }
    var recordingSeconds by remember { mutableStateOf(0) }
    var hasVoiceNote by remember { mutableStateOf(false) }

    // Timer effect for voice recording
    LaunchedEffect(recordingState) {
        if (recordingState == RecordingState.RECORDING) {
            while (recordingState == RecordingState.RECORDING) {
                delay(1000L)
                recordingSeconds += 1
            }
        }
    }

    fun saveBitmapToCache(bitmap: Bitmap): Uri? {
        return try {
            val file = File(context.cacheDir, "crop_post_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            Uri.fromFile(file)
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
            }
        }
    }

    val cameraPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "تصویر بنانے کے لیے کیمرہ کی اجازت درکار ہے", Toast.LENGTH_SHORT).show()
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .padding(8.dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "نئی پوسٹ شیئر کریں",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "آواز، تحریر یا تصویر کے ذریعے کسان بھائیوں سے رابطہ کریں",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                // Scrollable Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Category Selector
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "کیٹیگری منتخب کریں",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        val categories = listOf("Farmer problems", "Zari mahir suggestions", "Seeds", "Agrochemicals", "General")
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(categories) { cat ->
                                val isSelected = cat == selectedCategory
                                val displayCat = when (cat) {
                                    "Farmer problems" -> "کسان مسائل"
                                    "Zari mahir suggestions" -> "زرعی ماہر مشورے"
                                    "Seeds" -> "بیج و اقسام"
                                    "Agrochemicals" -> "ادویات و کھاد"
                                    else -> "عام معلومات"
                                }
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedCategory = cat },
                                    label = { Text(displayCat) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }
                        }
                    }

                    // Text Field Area
                    OutlinedTextField(
                        value = postText,
                        onValueChange = { postText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp, max = 150.dp),
                        label = { Text("اپنا مسئلہ، فصل کا حال یا مشورہ تحریر کریں...") },
                        shape = RoundedCornerShape(14.dp)
                    )

                    // Redesigned Voice Attachment Section (Simple & Elegant with Best Style & Padding)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (recordingState == RecordingState.RECORDING) Color(0xFFFFEBEE)
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
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
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = null,
                                        tint = if (recordingState == RecordingState.RECORDING) Color(0xFFC62828) else MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "صوتی ریکارڈنگ",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (recordingSeconds > 0) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (recordingState == RecordingState.RECORDING) Color(0xFFC62828) else MaterialTheme.colorScheme.primary
                                    ) {
                                        Text(
                                            text = String.format("%02d:%02d", recordingSeconds / 60, recordingSeconds % 60),
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            // Recorder Controls
                            when (recordingState) {
                                RecordingState.IDLE -> {
                                    Button(
                                        onClick = {
                                            recordingState = RecordingState.RECORDING
                                            recordingSeconds = 0
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth().height(44.dp)
                                    ) {
                                        Icon(Icons.Default.Mic, contentDescription = null, Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("آواز ریکارڈ کریں", fontWeight = FontWeight.Bold)
                                    }
                                }

                                RecordingState.RECORDING -> {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Button(
                                            onClick = { recordingState = RecordingState.PAUSED },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57C00)),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1f).height(44.dp)
                                        ) {
                                            Icon(Icons.Default.Pause, contentDescription = null, Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("وقفہ")
                                        }

                                        Button(
                                            onClick = {
                                                recordingState = RecordingState.FINISHED
                                                hasVoiceNote = true
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1f).height(44.dp)
                                        ) {
                                            Icon(Icons.Default.Check, contentDescription = null, Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("مکمل")
                                        }
                                    }
                                }

                                RecordingState.PAUSED -> {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Button(
                                            onClick = { recordingState = RecordingState.RECORDING },
                                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1f).height(44.dp)
                                        ) {
                                            Icon(Icons.Default.PlayArrow, contentDescription = null, Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("جاری رکھیں")
                                        }

                                        Button(
                                            onClick = {
                                                recordingState = RecordingState.FINISHED
                                                hasVoiceNote = true
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1f).height(44.dp)
                                        ) {
                                            Icon(Icons.Default.Check, contentDescription = null, Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("محفوظ کریں")
                                        }
                                    }
                                }

                                RecordingState.FINISHED -> {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.GraphicEq,
                                                contentDescription = null,
                                                tint = Color(0xFF2E7D32)
                                            )
                                            Text(
                                                text = "آواز تیار ہے (${recordingSeconds} سیکنڈ)",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF2E7D32)
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                recordingState = RecordingState.IDLE
                                                recordingSeconds = 0
                                                hasVoiceNote = false
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete Recording",
                                                tint = Color(0xFFC62828)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Picture Attachment Section (Only Camera & Gallery - No sample demo pictures)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "تصویر شامل کریں",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { cameraPermission.launch(Manifest.permission.CAMERA) },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).height(44.dp)
                            ) {
                                Icon(Icons.Default.PhotoCamera, null, Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("کیمرہ", fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).height(44.dp)
                            ) {
                                Icon(Icons.Default.PhotoLibrary, null, Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("گیلری", fontWeight = FontWeight.Bold)
                            }
                        }

                        // Preview Container
                        if (selectedImageUri != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.Black.copy(alpha = 0.05f))
                            ) {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Selected Picture",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )

                                IconButton(
                                    onClick = { selectedImageUri = null },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                        .size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove Photo",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Submit Button
                val canSubmit = postText.isNotBlank() || hasVoiceNote || selectedImageUri != null
                Button(
                    onClick = {
                        val newPost = CommunityPost(
                            id = "post_${UUID.randomUUID()}",
                            author = currentUser,
                            text = postText.trim(),
                            voiceUri = if (hasVoiceNote) "sample_voice_uri" else null,
                            voiceDurationSeconds = if (hasVoiceNote) recordingSeconds else 0,
                            imageRes = null,
                            imageUri = selectedImageUri?.toString(),
                            category = selectedCategory,
                            district = currentUser.district.split(",").firstOrNull()?.trim() ?: "Mardan",
                            timestamp = "ابھی",
                            likeCount = 0,
                            isLiked = false,
                            comments = emptyList()
                        )
                        onSubmitPost(newPost)
                        onDismiss()
                    },
                    enabled = canSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("پوسٹ شائع کریں", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}
