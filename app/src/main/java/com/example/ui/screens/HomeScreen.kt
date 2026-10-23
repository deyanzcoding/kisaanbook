package com.example.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.SampleData
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.model.Farmer
import com.example.model.FarmerStory
import com.example.model.Post
import com.example.model.UserType
import com.example.ui.components.FacebookStoryTray
import com.example.ui.components.PostCard
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    posts: List<Post>,
    stories: List<FarmerStory>,
    currentUser: Farmer,
    isLoggedIn: Boolean,
    selectedCategory: String,
    selectedDistrict: String,
    searchQuery: String,
    isSearchActive: Boolean,
    unreadNotifCount: Int,
    appLanguage: AppLanguage,
    onCategorySelect: (String) -> Unit,
    onDistrictChange: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onToggleSearch: (Boolean) -> Unit,
    onNotificationClick: () -> Unit,
    onAuthClick: () -> Unit,
    onLikeClick: (String) -> Unit,
    onCommentClick: (Post) -> Unit,
    onSaveClick: (String) -> Unit,
    onStoryClick: (FarmerStory) -> Unit,
    onCreateStoryClick: () -> Unit,
    onOpenCreatePost: () -> Unit,
    onOpenAskQuestion: () -> Unit,
    onFollowClick: (String) -> Unit = {},
    onImageClick: (Post) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isLocating by remember { mutableStateOf(false) }
    var selectedProfile by remember { mutableStateOf<Farmer?>(null) }
    var weatherExpanded by remember { mutableStateOf(false) }

    // Real Android Location Permission Request Launcher
    @SuppressLint("MissingPermission")
    fun fetchRealDeviceLocation() {
        try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providers = listOf(
                LocationManager.GPS_PROVIDER,
                LocationManager.NETWORK_PROVIDER,
                LocationManager.PASSIVE_PROVIDER
            )
            var bestLocation: Location? = null
            for (provider in providers) {
                if (locationManager.isProviderEnabled(provider)) {
                    val loc = locationManager.getLastKnownLocation(provider)
                    if (loc != null && (bestLocation == null || loc.accuracy < bestLocation.accuracy)) {
                        bestLocation = loc
                    }
                }
            }

            if (bestLocation != null) {
                var detectedName: String? = null
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(bestLocation.latitude, bestLocation.longitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val addr = addresses[0]
                        detectedName = addr.locality ?: addr.subAdminArea ?: addr.adminArea
                    }
                } catch (_: Exception) {
                    // Geocoder fallback
                }

                val finalLocality = detectedName ?: "District Lat:${"%.2f".format(bestLocation.latitude)}"
                onDistrictChange(finalLocality)
                Toast.makeText(context, "Location updated: $finalLocality", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Location permission granted. Using $selectedDistrict.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Location set to: $selectedDistrict", Toast.LENGTH_SHORT).show()
        } finally {
            isLocating = false
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fineGranted || coarseGranted) {
            isLocating = true
            fetchRealDeviceLocation()
        } else {
            Toast.makeText(context, "Location permission denied. You can select your district manually.", Toast.LENGTH_SHORT).show()
        }
    }

    // Filter posts by search query if any
    val filteredPosts = remember(posts, searchQuery) {
        if (searchQuery.isBlank()) posts else {
            posts.filter { post ->
                post.text.contains(searchQuery, ignoreCase = true) ||
                post.author.name.contains(searchQuery, ignoreCase = true) ||
                post.category.contains(searchQuery, ignoreCase = true) ||
                post.district.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    // Infinite feed count (simulates infinite social feed pagination like Facebook)
    val infiniteCount = if (filteredPosts.isEmpty()) 0 else 60

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // Header: Logo + App Name on left, Search + Notifications on right
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Logo + App Name
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_kisaan_book_logo),
                            contentDescription = "Kisaan Book Logo",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = Strings.appName(appLanguage),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Right icons: Search + Notifications
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { onToggleSearch(!isSearchActive) },
                            modifier = Modifier.testTag("home_search_toggle")
                        ) {
                            Icon(
                                imageVector = if (isSearchActive) Icons.Default.Close else Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Box {
                            IconButton(
                                onClick = onNotificationClick,
                                modifier = Modifier.testTag("home_notifications_icon")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            if (unreadNotifCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .align(Alignment.TopEnd)
                                        .offset(x = (-8).dp, y = 8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFD32F2F))
                                )
                            }
                        }
                    }
                }

                // Expandable Search Bar
                AnimatedVisibility(visible = isSearchActive) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text("Search accounts, crops, problems, machinery, fertilizer...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchQueryChange("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .testTag("home_search_input"),
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Compact, today-first visual weather card (Hides humidity/wind, shows sun/cloud icons)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Location & Today dropdown header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = selectedDistrict,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { weatherExpanded = !weatherExpanded }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("Today", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(Modifier.width(2.dp))
                                Icon(
                                    imageVector = if (weatherExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = "Forecast dropdown",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Today's main visual temperature & condition (No humidity, no wind)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFFF3E0)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.WbSunny,
                                        contentDescription = "Sunny",
                                        tint = Color(0xFFF57C00),
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text("28°C", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                                    Text("Sunny & clear", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            AssistChip(
                                onClick = {
                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                },
                                label = { Text(if (isLocating) "Detecting..." else "My location", fontSize = 11.sp) },
                                leadingIcon = { Icon(Icons.Default.MyLocation, null, modifier = Modifier.size(15.dp)) }
                            )
                        }

                        // Dropdown Coming Days of Week Forecast (Thu, Fri, Sat, Sun)
                        AnimatedVisibility(weatherExpanded) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    text = "Coming Days Forecast",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    ForecastDayColumn("Thu", "30° / 17°", Icons.Default.WbSunny, Color(0xFFF57C00))
                                    ForecastDayColumn("Fri", "27° / 16°", Icons.Default.Cloud, Color(0xFF78909C))
                                    ForecastDayColumn("Sat", "25° / 15°", Icons.Default.WbCloudy, Color(0xFF78909C))
                                    ForecastDayColumn("Sun", "29° / 17°", Icons.Default.WbSunny, Color(0xFFF57C00))
                                }
                            }
                        }
                    }
                }
            }

            // Categorized Search Results (Accounts, Community, Market, Crops/Problems)
            if (isSearchActive && searchQuery.isNotBlank()) {
                item {
                    SearchResultsPreview(
                        query = searchQuery,
                        allFarmers = SampleData.allFarmersList,
                        posts = posts,
                        onUserClick = { selectedProfile = it }
                    )
                }
            }

            // Facebook-style Story Tray (Horizontal scroll)
            item {
                FacebookStoryTray(
                    stories = stories,
                    currentUser = currentUser,
                    onCreateStoryClick = onCreateStoryClick,
                    onStoryClick = onStoryClick
                )
            }

            // Empty state check
            if (filteredPosts.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Feed,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No posts found",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                // Infinite feed: cycles through posts like Meta apps
                items(
                    count = infiniteCount,
                    key = { index -> "${filteredPosts[index % filteredPosts.size].id}_$index" }
                ) { index ->
                    val post = filteredPosts[index % filteredPosts.size]
                    PostCard(
                        post = post,
                        appLanguage = appLanguage,
                        onLikeClick = { onLikeClick(post.id) },
                        onCommentClick = { onCommentClick(post) },
                        onSaveClick = { onSaveClick(post.id) },
                        onFollowClick = { onFollowClick(post.author.id) },
                        onImageClick = { onImageClick(post) },
                        onAuthorClick = { selectedProfile = post.author },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }
    }

    // Friendly Meta-style Uploader Profile Preview Dialog
    selectedProfile?.let { farmer ->
        AlertDialog(
            onDismissRequest = { selectedProfile = null },
            icon = {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    if (farmer.avatarUri != null) {
                        AsyncImage(
                            model = farmer.avatarUri,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text(
                            text = farmer.avatarInitials,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(farmer.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        when (farmer.userType) {
                            UserType.EXPERT -> {
                                Icon(Icons.Default.Verified, null, tint = Color(0xFFD4AF37), modifier = Modifier.size(14.dp))
                                Text("Zari-Mahir (زرعی ماہر)", color = Color(0xFFB8860B), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                            UserType.DEALER -> {
                                Icon(Icons.Default.Storefront, null, tint = Color(0xFF1976D2), modifier = Modifier.size(14.dp))
                                Text("Dokandar (دوکاندار)", color = Color(0xFF1976D2), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                            UserType.FARMER -> {
                                Icon(Icons.Default.Eco, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(14.dp))
                                Text("Kisaan (کسان)", color = Color(0xFF2E7D32), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Location: ${farmer.district}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = farmer.bio.ifBlank { "Active community member on Kisaan Book." },
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${farmer.postsCount}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Posts", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${farmer.followersCount}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Followers", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${farmer.followingCount}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Following", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedProfile = null }) {
                    Text("Close")
                }
            }
        )
    }
}

/**
 * Categorized Search Results Preview: Accounts, Community, Market, Crops & Problems
 * Zero emojis - uses clean Material Icons
 */
@Composable
private fun SearchResultsPreview(
    query: String,
    allFarmers: List<Farmer>,
    posts: List<Post>,
    onUserClick: (Farmer) -> Unit
) {
    val q = query.trim().lowercase()

    // Matching Accounts from demo list
    val matchingAccounts = allFarmers.filter {
        it.name.lowercase().contains(q) ||
        it.district.lowercase().contains(q) ||
        it.userType.code.contains(q) ||
        it.userType.labelEn.lowercase().contains(q)
    }.take(3)

    // Matching Community topics
    val communityTopics = listOf(
        "Yellow Rust Treatment",
        "Maize Fertilizer Timing",
        "Sugarcane Borer Prevention",
        "Drip Irrigation Advisory",
        "Weed Spraying Conditions"
    ).filter { it.lowercase().contains(q) || q.contains(it.substringBefore(" ").lowercase()) }.take(2)

    // Matching Market items
    val marketItems = listOf(
        "Fakhr-e-Bhakkar Wheat Seed",
        "Pioneer Hybrid Maize P1429",
        "Laser Land Leveler Rental",
        "Certified Urea & DAP Fertilizer",
        "Crop Protection Sprayers"
    ).filter { it.lowercase().contains(q) || q.contains(it.substringBefore(" ").lowercase()) }.take(2)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Search Results for \"$query\"",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            // Section 1: Accounts
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Accounts (${matchingAccounts.size})", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            if (matchingAccounts.isNotEmpty()) {
                matchingAccounts.forEach { farmer ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onUserClick(farmer) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(farmer.avatarInitials, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(farmer.name, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text("${farmer.userType.labelEn} · ${farmer.district}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            } else {
                Text("No matching accounts found.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            // Section 2: Community Q&A
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Groups, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Community Q&A", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            if (communityTopics.isNotEmpty()) {
                communityTopics.forEach { topic ->
                    Text("• $topic", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                }
            } else {
                Text("• ${query.replaceFirstChar { it.uppercase() }} Farmers Discussion", fontSize = 12.sp)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            // Section 3: Market
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Storefront, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Marketplace", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            if (marketItems.isNotEmpty()) {
                marketItems.forEach { item ->
                    Text("• $item", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                }
            } else {
                Text("• Seeds · Fertilizer · Farm Machinery", fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun ForecastDayColumn(
    day: String,
    temp: String,
    icon: ImageVector,
    iconTint: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = day,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = temp,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
