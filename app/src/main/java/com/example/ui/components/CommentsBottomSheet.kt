package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Comment
import com.example.model.Post
import com.example.model.ReplyComment
import com.example.model.UserType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
    post: Post,
    onDismiss: () -> Unit,
    onAddComment: (String) -> Unit,
    onReplyToComment: (commentId: String, replyText: String) -> Unit = { _, _ -> },
    onLikeComment: (commentId: String) -> Unit = {},
    onLikeReply: (commentId: String, replyId: String) -> Unit = { _, _ -> }
) {
    var commentText by remember { mutableStateOf("") }
    var replyingToComment by remember { mutableStateOf<Comment?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Discussion & Comments (${post.comments.size})",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Comments"
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            // Comments List
            if (post.comments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No comments yet. Share your experience or advice!",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(post.comments, key = { it.id }) { comment ->
                        CommentItem(
                            comment = comment,
                            onReplyClick = { replyingToComment = comment },
                            onLikeClick = { onLikeComment(comment.id) },
                            onLikeReply = { replyId -> onLikeReply(comment.id, replyId) }
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            // Replying Indicator Banner
            if (replyingToComment != null) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Reply,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Replying to ${replyingToComment?.authorName}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        IconButton(
                            onClick = { replyingToComment = null },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cancel reply",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Comment Input Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = {
                        Text(
                            if (replyingToComment != null) "Reply to ${replyingToComment?.authorName}..."
                            else "Write helpful advice for brother..."
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("comment_input_field"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            val target = replyingToComment
                            if (target != null) {
                                onReplyToComment(target.id, commentText)
                                replyingToComment = null
                            } else {
                                onAddComment(commentText)
                            }
                            commentText = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .testTag("send_comment_button"),
                    enabled = commentText.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send Comment",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun CommentItem(
    comment: Comment,
    onReplyClick: () -> Unit,
    onLikeClick: () -> Unit,
    onLikeReply: (replyId: String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Main comment row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        when (comment.authorType) {
                            UserType.EXPERT -> Color(0xFFFFF8E1)
                            UserType.DEALER -> Color(0xFFE3F2FD)
                            UserType.FARMER -> MaterialTheme.colorScheme.primaryContainer
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = comment.authorName.take(2).uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = when (comment.authorType) {
                        UserType.EXPERT -> Color(0xFFB8860B)
                        UserType.DEALER -> Color(0xFF1976D2)
                        UserType.FARMER -> MaterialTheme.colorScheme.onPrimaryContainer
                    }
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Crucial: Use Modifier.weight(1f) for comment text area so it pushes action buttons cleanly
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Name & Type badge & Timestamp
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = comment.authorName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    // Visual badge
                    when (comment.authorType) {
                        UserType.EXPERT -> {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Zari-Mahir",
                                tint = Color(0xFFD4AF37),
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        UserType.DEALER -> {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = "Dokandar",
                                tint = Color(0xFF1976D2),
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        UserType.FARMER -> {
                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = "Kisaan",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "· ${comment.authorDistrict}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "· ${comment.timestamp}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Comment text
                Text(
                    text = comment.text,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Actions: Like and Reply with 8.dp spacing
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    // Like button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { onLikeClick() }
                            .padding(vertical = 2.dp, horizontal = 4.dp)
                    ) {
                        Icon(
                            imageVector = if (comment.isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                            contentDescription = "Like comment",
                            tint = if (comment.isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        if (comment.likeCount > 0) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${comment.likeCount}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (comment.isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Reply button
                    Text(
                        text = "Reply",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { onReplyClick() }
                            .padding(vertical = 2.dp, horizontal = 4.dp)
                    )
                }
            }
        }

        // Hierarchical Replies List (indented neatly so it never collapses or clips)
        if (comment.replies.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 36.dp, top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                comment.replies.forEach { reply ->
                    ReplyItem(
                        reply = reply,
                        onLikeClick = { onLikeReply(reply.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ReplyItem(
    reply: ReplyComment,
    onLikeClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(
                    when (reply.authorType) {
                        UserType.EXPERT -> Color(0xFFFFF8E1)
                        UserType.DEALER -> Color(0xFFE3F2FD)
                        UserType.FARMER -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = reply.authorName.take(2).uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = when (reply.authorType) {
                    UserType.EXPERT -> Color(0xFFB8860B)
                    UserType.DEALER -> Color(0xFF1976D2)
                    UserType.FARMER -> MaterialTheme.colorScheme.onPrimaryContainer
                }
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = reply.authorName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(3.dp))
                when (reply.authorType) {
                    UserType.EXPERT -> Icon(Icons.Default.Verified, null, tint = Color(0xFFD4AF37), modifier = Modifier.size(11.dp))
                    UserType.DEALER -> Icon(Icons.Default.Storefront, null, tint = Color(0xFF1976D2), modifier = Modifier.size(11.dp))
                    UserType.FARMER -> Icon(Icons.Default.Eco, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(11.dp))
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "· ${reply.timestamp}",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = reply.text,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Reply Like action
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { onLikeClick() }
                    .padding(vertical = 2.dp, horizontal = 4.dp)
            ) {
                Icon(
                    imageVector = if (reply.isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                    contentDescription = "Like reply",
                    tint = if (reply.isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(12.dp)
                )
                if (reply.likeCount > 0) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${reply.likeCount}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (reply.isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
