package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.model.Comment
import com.example.model.CommunityPost
import com.example.model.Farmer
import com.example.model.ReplyComment
import com.example.model.UserType
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    communityPosts: List<CommunityPost>,
    selectedMediaFilter: String, // "All", "Text", "Voice", "By Picture"
    onMediaFilterSelect: (String) -> Unit,
    onCreatePostClick: () -> Unit,
    onToggleLike: (postId: String) -> Unit,
    onAddComment: (postId: String, text: String) -> Unit,
    onAddReply: (postId: String, commentId: String, text: String) -> Unit,
    onToggleCommentLike: (postId: String, commentId: String) -> Unit,
    onToggleReplyLike: (postId: String, commentId: String, replyId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryChip by remember { mutableStateOf("All") }
    var userProfileModal by remember { mutableStateOf<Farmer?>(null) }

    val listState = rememberLazyListState()

    // Scroll-derived state for collapsing title and search bar
    val isScrolledDown by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 40
        }
    }

    val categoryChips = listOf(
        "All" to "سب",
        "Farmer problems" to "کسان مسائل",
        "Zari mahir suggestions" to "زرعی ماہر مشورے",
        "Seeds" to "بیج",
        "Agrochemicals" to "ادویات",
        "Others" to "دیگر"
    )

    val mediaChips = listOf(
        "All" to "تمام پوسٹس",
        "Text" to "صرف تحریر",
        "Voice" to "صوتی پیغامات",
        "By Picture" to "تصاویر والی"
    )

    // Filtering posts
    val filteredPosts = remember(communityPosts, selectedCategoryChip, selectedMediaFilter, searchQuery) {
        communityPosts.filter { post ->
            val matchesCategory = when (selectedCategoryChip) {
                "All" -> true
                "Farmer problems" -> post.category.equals("Farmer problems", true) || post.author.userType == UserType.FARMER
                "Zari mahir suggestions" -> post.category.equals("Zari mahir suggestions", true) || post.author.userType == UserType.EXPERT
                "Seeds" -> post.category.equals("Seeds", true) || post.text.contains("بیج", true)
                "Agrochemicals" -> post.category.equals("Agrochemicals", true) || post.text.contains("سپرے", true) || post.text.contains("کھاد", true)
                else -> true
            }

            val matchesMedia = when (selectedMediaFilter) {
                "All" -> true
                "Text" -> post.hasText && !post.hasVoice && !post.hasPicture
                "Voice" -> post.hasVoice
                "By Picture" -> post.hasPicture
                else -> true
            }

            val matchesSearch = if (searchQuery.isBlank()) true else {
                post.text.contains(searchQuery, ignoreCase = true) ||
                        post.author.name.contains(searchQuery, ignoreCase = true) ||
                        post.district.contains(searchQuery, ignoreCase = true)
            }

            matchesCategory && matchesMedia && matchesSearch
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Collapsing Top Section: Title & Search Bar hide when scrolling down, sticky Filter Chips remain pinned
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = if (isScrolledDown) 4.dp else 1.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(bottom = 8.dp)
                ) {
                    AnimatedVisibility(visible = !isScrolledDown) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "کسان برادری (Farmer Community Q&A)",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            // Search Bar
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("سوال، کسان اکاؤنٹ یا مسئلہ تلاش کریں...") },
                                leadingIcon = {
                                    Icon(Icons.Default.Search, contentDescription = "Search")
                                },
                                trailingIcon = {
                                    if (searchQuery.isNotBlank()) {
                                        IconButton(onClick = { searchQuery = "" }) {
                                            Icon(Icons.Default.Close, contentDescription = "Clear")
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp)
                            )
                        }
                    }

                    // Sticky Filter Chips Row 1: Media Types (All, Text, Voice, By Picture)
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(mediaChips) { (key, label) ->
                            val isSelected = selectedMediaFilter == key
                            FilterChip(
                                selected = isSelected,
                                onClick = { onMediaFilterSelect(key) },
                                label = { Text(label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                leadingIcon = {
                                    when (key) {
                                        "Voice" -> Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(16.dp))
                                        "By Picture" -> Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(16.dp))
                                        "Text" -> Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                                        else -> null
                                    }
                                },
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }

                    // Sticky Filter Chips Row 2: Categories
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categoryChips) { (key, label) ->
                            val isSelected = selectedCategoryChip == key
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCategoryChip = key },
                                label = { Text(label) },
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                }
            }

            // Post Feed
            if (filteredPosts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forum,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "کوئی پوسٹ نہیں ملی",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "نئی پوسٹ بنانے کے لیے نیچے + کا بٹن دبائیں۔",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredPosts, key = { it.id }) { post ->
                        CommunityPostCard(
                            post = post,
                            onAuthorClick = { userProfileModal = post.author },
                            onToggleLike = { onToggleLike(post.id) },
                            onShareClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "Kisaan Book Post by ${post.author.name}")
                                    putExtra(Intent.EXTRA_TEXT, "${post.author.name} (${post.district}):\n${post.text}\n\n- Kisaan Book App")
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share post via"))
                            },
                            onAddComment = { text -> onAddComment(post.id, text) },
                            onAddReply = { commentId, text -> onAddReply(post.id, commentId, text) },
                            onToggleCommentLike = { commentId -> onToggleCommentLike(post.id, commentId) },
                            onToggleReplyLike = { commentId, replyId -> onToggleReplyLike(post.id, commentId, replyId) }
                        )
                    }
                }
            }
        }

        // Prominent Floating Action Button (+) to Create Post
        FloatingActionButton(
            onClick = onCreatePostClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("create_community_post_fab"),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Post",
                modifier = Modifier.size(28.dp)
            )
        }

        // Author Profile Modal Bottom Sheet (opens from down side professionally)
        userProfileModal?.let { farmer ->
            FarmerProfileBottomSheet(
                farmer = farmer,
                onDismiss = { userProfileModal = null }
            )
        }
    }
}

/**
 * Community Post Item Card (Meta/Facebook Clean Style)
 */
@Composable
fun CommunityPostCard(
    post: CommunityPost,
    onAuthorClick: () -> Unit,
    onToggleLike: () -> Unit,
    onShareClick: () -> Unit,
    onAddComment: (String) -> Unit,
    onAddReply: (commentId: String, text: String) -> Unit,
    onToggleCommentLike: (commentId: String) -> Unit,
    onToggleReplyLike: (commentId: String, replyId: String) -> Unit
) {
    var isExpandedDescription by remember { mutableStateOf(false) }
    var isCommentsOpen by remember { mutableStateOf(false) }
    var newCommentText by remember { mutableStateOf("") }

    // Truncate author name if > 11 chars
    val rawName = post.author.name
    val displayName = if (rawName.length > 11) rawName.take(11) + "..." else rawName

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: DP, Truncated Name, Badge, Address, Timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .clickable { onAuthorClick() }
                        .weight(1f)
                ) {
                    // Avatar
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = post.author.name.take(2).uppercase(),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = displayName,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            // Badge
                            UserBadge(userType = post.author.userType)
                        }

                        // Address up-to-down effect + User Type
                        val typeLabel = when (post.author.userType) {
                            UserType.FARMER -> "کسان (Farmer)"
                            UserType.EXPERT -> "زرعی ماہر (Zari Mahir)"
                            UserType.DEALER -> "دوکاندار (Dokandar)"
                        }
                        Text(
                            text = "${post.district} • $typeLabel • ${post.timestamp}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Post Description with "Show more / Show less" if > 2 lines
            if (post.hasText) {
                Column {
                    Text(
                        text = post.text,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = if (isExpandedDescription) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable { isExpandedDescription = !isExpandedDescription }
                    )
                    if (post.text.length > 80) {
                        Text(
                            text = if (isExpandedDescription) "کم دکھائیں (Show less)" else "مزید دکھائیں (Show more)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable { isExpandedDescription = !isExpandedDescription }
                                .padding(top = 4.dp)
                        )
                    }
                }
            }

            // Prominent Voice Player if post has voice
            if (post.hasVoice) {
                VoicePlayerBar(
                    durationSeconds = post.voiceDurationSeconds.coerceAtLeast(15)
                )
            }

            // Compact Media Container (Height = 200dp with 12dp rounded corners)
            if (post.hasPicture) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.05f))
                ) {
                    if (post.imageUri != null) {
                        AsyncImage(
                            model = post.imageUri,
                            contentDescription = "Post Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else if (post.imageRes != null) {
                        Image(
                            painter = painterResource(id = post.imageRes),
                            contentDescription = "Post Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // Action Buttons: Like, Comment, Share
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Like Button
                TextButton(
                    onClick = onToggleLike,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (post.isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Default.ThumbUp else Icons.Outlined.ThumbUp,
                        contentDescription = "Like",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (post.likeCount > 0) "${post.likeCount} پسند" else "پسند"
                    )
                }

                // Comment Button
                TextButton(
                    onClick = { isCommentsOpen = !isCommentsOpen },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comments",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (post.comments.isNotEmpty()) "${post.comments.size} تبصرے" else "تبصرہ کریں"
                    )
                }

                // Share Button (WhatsApp / Other)
                TextButton(
                    onClick = onShareClick,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("شیئر")
                }
            }

            // Comments & Replies Section
            AnimatedVisibility(visible = isCommentsOpen) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    // Comment Input Field
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = newCommentText,
                            onValueChange = { newCommentText = it },
                            placeholder = { Text("اپنا جواب یا رائے تحریر کریں...", fontSize = 13.sp) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(20.dp),
                            singleLine = true
                        )
                        IconButton(
                            onClick = {
                                if (newCommentText.isNotBlank()) {
                                    onAddComment(newCommentText)
                                    newCommentText = ""
                                }
                            },
                            enabled = newCommentText.isNotBlank(),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Send", modifier = Modifier.size(18.dp))
                        }
                    }

                    // Comments List with proper 16dp outer padding, Modifier.weight(1f) to prevent collapsing
                    post.comments.forEach { comment ->
                        CommentItem(
                            comment = comment,
                            onLike = { onToggleCommentLike(comment.id) },
                            onReply = { replyText -> onAddReply(comment.id, replyText) },
                            onToggleReplyLike = { replyId -> onToggleReplyLike(comment.id, replyId) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * User Badges:
 * - Green Leaf for Farmers
 * - Gold/Platinum tick for Zari Mahir (Experts)
 * - Store icon for Dokandar (Dealers)
 */
@Composable
fun UserBadge(userType: UserType) {
    when (userType) {
        UserType.FARMER -> {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFE8F5E9),
                modifier = Modifier.padding(2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = "Farmer Leaf Badge",
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "کسان",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        }
        UserType.EXPERT -> {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFFFF8E1),
                modifier = Modifier.padding(2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Expert Gold Tick",
                        tint = Color(0xFFF57F17),
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "زرعی ماہر",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF57F17)
                    )
                }
            }
        }
        UserType.DEALER -> {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFE1F5FE),
                modifier = Modifier.padding(2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = "Dealer Store Icon",
                        tint = Color(0xFF0288D1),
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "دوکاندار",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0288D1)
                    )
                }
            }
        }
    }
}

/**
 * Responsive Voice Player Bar with Play/Pause and Animated progress
 */
@Composable
fun VoicePlayerBar(durationSeconds: Int) {
    var isPlaying by remember { mutableStateOf(false) }
    var progressSeconds by remember { mutableStateOf(0) }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (isPlaying && progressSeconds < durationSeconds) {
                delay(1000L)
                progressSeconds += 1
            }
            if (progressSeconds >= durationSeconds) {
                isPlaying = false
                progressSeconds = 0
            }
        }
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconButton(
                onClick = { isPlaying = !isPlaying },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "صوتی پیغام (Voice Message)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = String.format("%02d:%02d / %02d:%02d", progressSeconds / 60, progressSeconds % 60, durationSeconds / 60, durationSeconds % 60),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                LinearProgressIndicator(
                    progress = { if (durationSeconds > 0) progressSeconds.toFloat() / durationSeconds.toFloat() else 0f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

/**
 * Comment & Reply hierarchy item
 * Implements Modifier.weight(1f) to prevent buttons collapsing
 */
@Composable
fun CommentItem(
    comment: Comment,
    onLike: () -> Unit,
    onReply: (String) -> Unit,
    onToggleReplyLike: (String) -> Unit
) {
    var showReplyInput by remember { mutableStateOf(false) }
    var replyText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Comment Box
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Header: Author & Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = comment.authorName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        UserBadge(userType = comment.authorType)
                    }
                    Text(
                        text = comment.timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Comment Content with weight(1f) pattern so buttons don't push off screen
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = comment.text,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )

                    // Action buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (comment.likeCount > 0) "پسند (${comment.likeCount})" else "پسند",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (comment.isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.clickable { onLike() }
                        )

                        Text(
                            text = "جواب دیں",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { showReplyInput = !showReplyInput }
                        )
                    }
                }
            }
        }

        // Reply Input
        if (showReplyInput) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = replyText,
                    onValueChange = { replyText = it },
                    placeholder = { Text("جواب تحریر کریں...", fontSize = 12.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )
                IconButton(
                    onClick = {
                        if (replyText.isNotBlank()) {
                            onReply(replyText)
                            replyText = ""
                            showReplyInput = false
                        }
                    },
                    enabled = replyText.isNotBlank()
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }

        // Nested Replies
        if (comment.replies.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                comment.replies.forEach { reply ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = reply.authorName,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    UserBadge(userType = reply.authorType)
                                }
                                Text(
                                    text = reply.timestamp,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    text = reply.text,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.weight(1f)
                                )

                                Text(
                                    text = if (reply.likeCount > 0) "پسند (${reply.likeCount})" else "پسند",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (reply.isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.clickable { onToggleReplyLike(reply.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Farmer Profile Bottom Sheet (opens professionally from down side)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerProfileBottomSheet(
    farmer: Farmer,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Avatar
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(72.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = farmer.name.take(2).uppercase(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = farmer.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    UserBadge(userType = farmer.userType)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${farmer.district} • ${farmer.experienceYears} سالہ تجربہ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Stats Row: Posts, Followers, Following
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(farmer.postsCount.toString(), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("پوسٹس", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Box(modifier = Modifier.height(24.dp).width(1.dp).background(MaterialTheme.colorScheme.outlineVariant))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(farmer.followersCount.toString(), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("فالوورز", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Box(modifier = Modifier.height(24.dp).width(1.dp).background(MaterialTheme.colorScheme.outlineVariant))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(farmer.followingCount.toString(), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("فالوونگ", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Bio
            if (farmer.bio.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = farmer.bio,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(12.dp),
                        lineHeight = 20.sp
                    )
                }
            }

            if (farmer.mainCrops.isNotEmpty()) {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("اہم فصلیں و مہارت:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        farmer.mainCrops.forEach { crop ->
                            SuggestionChip(
                                onClick = {},
                                label = { Text(crop, fontSize = 11.sp) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("بند کریں", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
