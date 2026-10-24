package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.model.Farmer
import com.example.model.Post
import com.example.model.Question
import com.example.ui.components.PostCard

@Composable
fun ProfileScreen(
    farmer: Farmer,
    isLoggedIn: Boolean,
    appLanguage: AppLanguage,
    posts: List<Post>,
    questions: List<Question>,
    savedPosts: List<Post>,
    onEditProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAuthClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onLikeClick: (String) -> Unit,
    onCommentClick: (Post) -> Unit,
    onSaveClick: (String) -> Unit,
    onDeletePost: (String) -> Unit,
    onFollowClick: (String) -> Unit = {},
    onImageClick: (Post) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedProfileTab by remember { mutableIntStateOf(0) } // 0: Posts, 1: Questions, 2: Saved
    var isBioExpanded by remember { mutableStateOf(false) }

    val myPosts = remember(posts, farmer.id) {
        posts.filter { it.author.id == farmer.id }
    }

    val myQuestions = remember(questions, farmer.id) {
        questions.filter { it.author.id == farmer.id }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Top Profile Card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    // Header Row with Settings button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = Strings.tabProfile(appLanguage),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        IconButton(
                            onClick = onSettingsClick,
                            modifier = Modifier.testTag("profile_settings_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (isLoggedIn) {
                        // Avatar and Stats Row (Instagram Style)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Large Avatar Circle
                            Box(
                                modifier = Modifier
                                    .size(76.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                if (farmer.avatarUri != null) {
                                    AsyncImage(
                                        model = farmer.avatarUri,
                                        contentDescription = "Profile Photo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                    )
                                } else {
                                    Text(
                                        text = farmer.avatarInitials,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 24.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(20.dp))

                            // Stats: Posts, Followers, Following
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                StatColumn(count = myPosts.size.toString(), label = "Posts")
                                StatColumn(count = farmer.followersCount.toString(), label = "Followers")
                                StatColumn(count = farmer.followingCount.toString(), label = "Following")
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Bio Details Header
                        Text(
                            text = farmer.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "@${farmer.username}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${farmer.district} • ${farmer.experienceYears}+ years farming",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))

                        // Compact Bio paragraph: Clicking the bio itself toggles show more/show less
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isBioExpanded = !isBioExpanded }
                        ) {
                            Text(
                                text = farmer.bio,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 18.sp,
                                maxLines = if (isBioExpanded) Int.MAX_VALUE else 2,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Text(
                                    text = if (isBioExpanded) "کم دکھائیں (Show less)" else "مزید دکھائیں (Show more)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Icon(
                                    imageVector = if (isBioExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Compact Row: Crops Grown Chips & Edit Profile Button side-by-side or tightly aligned
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                farmer.mainCrops.take(3).forEach { crop ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.secondaryContainer
                                    ) {
                                        Text(
                                            text = crop,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            // Compact Edit Profile Button
                            OutlinedButton(
                                onClick = onEditProfileClick,
                                modifier = Modifier
                                    .height(34.dp)
                                    .testTag("edit_profile_button"),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(Strings.editProfile(appLanguage), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    } else {
                        // Guest State Card
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Welcome to Kisaan Book",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Sign in to save posts, ask questions, and list products in the marketplace.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = onAuthClick,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(Strings.login(appLanguage), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Tabs Row: Posts, Questions, Saved
        item {
            TabRow(
                selectedTabIndex = selectedProfileTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = selectedProfileTab == 0,
                    onClick = { selectedProfileTab = 0 },
                    text = { Text("${Strings.myPosts(appLanguage)} (${myPosts.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                    icon = { Icon(Icons.Default.GridOn, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedProfileTab == 1,
                    onClick = { selectedProfileTab = 1 },
                    text = { Text("${Strings.myQuestions(appLanguage)} (${myQuestions.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                    icon = { Icon(Icons.Default.QuestionAnswer, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedProfileTab == 2,
                    onClick = { selectedProfileTab = 2 },
                    text = { Text("${Strings.savedItems(appLanguage)} (${savedPosts.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                    icon = { Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
            }
        }

        // Tab Content
        when (selectedProfileTab) {
            0 -> {
                if (myPosts.isEmpty()) {
                    item {
                        EmptyProfileSection("You haven't posted any farm updates yet.")
                    }
                } else {
                    items(myPosts, key = { it.id }) { post ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                            PostCard(
                                post = post,
                                onLikeClick = { onLikeClick(post.id) },
                                onCommentClick = { onCommentClick(post) },
                                onSaveClick = { onSaveClick(post.id) },
                                onFollowClick = { onFollowClick(post.author.id) },
                                onImageClick = { onImageClick(post) }
                            )
                        }
                    }
                }
            }
            1 -> {
                if (myQuestions.isEmpty()) {
                    item {
                        EmptyProfileSection("No community questions submitted yet.")
                    }
                } else {
                    items(myQuestions, key = { it.id }) { q ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(q.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(q.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Crop: ${q.cropCategory}", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                                    Text("${q.answers.size} replies", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                if (savedPosts.isEmpty()) {
                    item {
                        EmptyProfileSection("No saved posts. Tap the bookmark icon on any post to save it for reference.")
                    }
                } else {
                    items(savedPosts, key = { it.id }) { post ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                            PostCard(
                                post = post,
                                onLikeClick = { onLikeClick(post.id) },
                                onCommentClick = { onCommentClick(post) },
                                onSaveClick = { onSaveClick(post.id) },
                                onFollowClick = { onFollowClick(post.author.id) },
                                onImageClick = { onImageClick(post) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatColumn(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EmptyProfileSection(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
