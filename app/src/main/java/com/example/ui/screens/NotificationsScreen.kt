package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.NotificationItem
import com.example.model.NotificationType

enum class NotifCategoryFilter {
    ALL, COMMUNITY, MARKET, SYSTEM
}

enum class CommunitySubFilter {
    ALL, LIKES, COMMENTS, SHARES
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem>,
    onBackClick: () -> Unit,
    onNotificationClick: (NotificationItem) -> Unit,
    onMarkAllRead: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(NotifCategoryFilter.ALL) }
    var selectedCommunitySub by remember { mutableStateOf(CommunitySubFilter.ALL) }

    val filteredNotifications = remember(notifications, selectedCategory, selectedCommunitySub) {
        notifications.filter { notif ->
            when (selectedCategory) {
                NotifCategoryFilter.ALL -> true
                NotifCategoryFilter.MARKET -> notif.category.equals("MARKET", ignoreCase = true) || notif.type == NotificationType.MARKET_INQUIRY
                NotifCategoryFilter.SYSTEM -> notif.category.equals("SYSTEM", ignoreCase = true) || notif.type == NotificationType.ADVISORY
                NotifCategoryFilter.COMMUNITY -> {
                    val isCommunity = notif.category.equals("COMMUNITY", ignoreCase = true) ||
                            notif.type in listOf(NotificationType.LIKE, NotificationType.COMMENT, NotificationType.REPLY, NotificationType.SHARE, NotificationType.FOLLOW)
                    if (!isCommunity) false
                    else {
                        when (selectedCommunitySub) {
                            CommunitySubFilter.ALL -> true
                            CommunitySubFilter.LIKES -> notif.type == NotificationType.LIKE
                            CommunitySubFilter.COMMENTS -> notif.type == NotificationType.COMMENT || notif.type == NotificationType.REPLY
                            CommunitySubFilter.SHARES -> notif.type == NotificationType.SHARE
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.testTag("notifications_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "اطلاعات اور پیغامات",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "فصل، کمیونٹی اور مارکیٹ کے الرٹس",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    TextButton(
                        onClick = onMarkAllRead,
                        modifier = Modifier.testTag("mark_all_read_button")
                    ) {
                        Text("سب پڑھیں", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                // Main Category Filter Chips
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == NotifCategoryFilter.ALL,
                            onClick = { selectedCategory = NotifCategoryFilter.ALL },
                            label = { Text("تمام") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = selectedCategory == NotifCategoryFilter.COMMUNITY,
                            onClick = { selectedCategory = NotifCategoryFilter.COMMUNITY },
                            label = { Text("کمیونٹی") },
                            leadingIcon = { Icon(Icons.Default.Forum, null, Modifier.size(16.dp)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = selectedCategory == NotifCategoryFilter.MARKET,
                            onClick = { selectedCategory = NotifCategoryFilter.MARKET },
                            label = { Text("مارکیٹ") },
                            leadingIcon = { Icon(Icons.Default.Storefront, null, Modifier.size(16.dp)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = selectedCategory == NotifCategoryFilter.SYSTEM,
                            onClick = { selectedCategory = NotifCategoryFilter.SYSTEM },
                            label = { Text("موبائل / سسٹم") },
                            leadingIcon = { Icon(Icons.Default.Notifications, null, Modifier.size(16.dp)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }

                // If Community is selected, show Sub-filter Chips
                if (selectedCategory == NotifCategoryFilter.COMMUNITY) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        item {
                            SuggestionChip(
                                onClick = { selectedCommunitySub = CommunitySubFilter.ALL },
                                label = { Text("سبھی", fontSize = 11.sp) },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = if (selectedCommunitySub == CommunitySubFilter.ALL)
                                        MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                )
                            )
                        }
                        item {
                            SuggestionChip(
                                onClick = { selectedCommunitySub = CommunitySubFilter.LIKES },
                                label = { Text("پسند (Likes)", fontSize = 11.sp) },
                                icon = { Icon(Icons.Default.Favorite, null, tint = Color(0xFFD32F2F), modifier = Modifier.size(14.dp)) },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = if (selectedCommunitySub == CommunitySubFilter.LIKES)
                                        MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                )
                            )
                        }
                        item {
                            SuggestionChip(
                                onClick = { selectedCommunitySub = CommunitySubFilter.COMMENTS },
                                label = { Text("تبصرے (Comments)", fontSize = 11.sp) },
                                icon = { Icon(Icons.Default.ChatBubble, null, tint = Color(0xFF1565C0), modifier = Modifier.size(14.dp)) },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = if (selectedCommunitySub == CommunitySubFilter.COMMENTS)
                                        MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                )
                            )
                        }
                        item {
                            SuggestionChip(
                                onClick = { selectedCommunitySub = CommunitySubFilter.SHARES },
                                label = { Text("شیئر (Shares)", fontSize = 11.sp) },
                                icon = { Icon(Icons.Default.Share, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(14.dp)) },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = if (selectedCommunitySub == CommunitySubFilter.SHARES)
                                        MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                )
                            )
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (filteredNotifications.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.NotificationsNone,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "کوئی نیا الرٹ نہیں ہے",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            } else {
                items(filteredNotifications, key = { it.id }) { item ->
                    NotificationCard(
                        item = item,
                        onClick = { onNotificationClick(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationCard(
    item: NotificationItem,
    onClick: () -> Unit
) {
    val icon: ImageVector = when (item.type) {
        NotificationType.LIKE -> Icons.Default.Favorite
        NotificationType.COMMENT, NotificationType.REPLY -> Icons.Default.ChatBubble
        NotificationType.SHARE -> Icons.Default.Share
        NotificationType.FOLLOW -> Icons.Default.PersonAdd
        NotificationType.MARKET_INQUIRY -> Icons.Default.Storefront
        NotificationType.ADVISORY -> Icons.Default.WbSunny
    }

    val iconColor: Color = when (item.type) {
        NotificationType.LIKE -> Color(0xFFD32F2F)
        NotificationType.COMMENT, NotificationType.REPLY -> Color(0xFF1565C0)
        NotificationType.SHARE -> Color(0xFF2E7D32)
        NotificationType.FOLLOW -> Color(0xFF2E7D32)
        NotificationType.MARKET_INQUIRY -> Color(0xFFE65100)
        NotificationType.ADVISORY -> Color(0xFFF57F17)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("notif_item_${item.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (!item.isRead) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.message,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 17.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = item.timestamp,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ) {
                        Text(
                            text = if (item.category == "MARKET") "مارکیٹ" else if (item.category == "SYSTEM") "سسٹم" else "کمیونٹی",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            if (!item.isRead) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}
