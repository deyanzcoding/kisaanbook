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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.model.Farmer
import com.example.model.FarmerStory
import com.example.model.Post
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
                    // Geocoder might not be present on all emulators
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
                    // Logo + App Name (strictly single language according to appLanguage, NO mixed languages)
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
                        placeholder = { Text(Strings.searchPlaceholder(appLanguage)) },
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
            // Compact, today-first weather card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(selectedDistrict, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Today", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                IconButton(onClick = { weatherExpanded = !weatherExpanded }, modifier = Modifier.size(32.dp)) {
                                    Icon(if (weatherExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, "Forecast", modifier = Modifier.size(20.dp))
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(52.dp).clip(CircleShape).background(Color(0xFFFFF3E0)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.WbSunny, "Sunny", tint = Color(0xFFF57C00), modifier = Modifier.size(32.dp))
                                }
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text("28°C", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
                                    Text("Sunny & clear", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            AssistChip(
                                onClick = { locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) },
                                label = { Text(if (isLocating) "Detecting" else "My location", fontSize = 11.sp) },
                                leadingIcon = { Icon(Icons.Default.MyLocation, null, modifier = Modifier.size(15.dp)) }
                            )
                        }

                        AnimatedVisibility(weatherExpanded) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
                                Spacer(Modifier.height(10.dp))
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
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

            if (isSearchActive && searchQuery.isNotBlank()) {
                item {
                    SearchResultsPreview(
                        query = searchQuery,
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
                // Infinite feed: dynamically cycles through posts like Meta apps
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

    selectedProfile?.let { farmer ->
        AlertDialog(
            onDismissRequest = { selectedProfile = null },
            title = { Text(farmer.name) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("${if (farmer.isExpert) "Zari-Mahir" else "Kisaan"} · ${farmer.district}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                    Text(farmer.bio.ifBlank { "اس کسان کا پروفائل ابھی مکمل نہیں کیا گیا۔" })
                    Text("${farmer.postsCount} posts  ·  ${farmer.followersCount} followers  ·  ${farmer.followingCount} following", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            confirmButton = { TextButton(onClick = { selectedProfile = null }) { Text("Close") } }
        )
    }
}

@Composable
private fun SearchResultsPreview(
    query: String,
    posts: List<Post>,
    onUserClick: (Farmer) -> Unit
) {
    val matches = posts
        .filter { it.author.name.contains(query, true) || it.text.contains(query, true) || it.category.contains(query, true) }
        .map { it.author }
        .distinctBy { it.id }
        .take(4)
    val q = query.lowercase()
    val categoryHits = listOf("Wheat", "Maize", "Rice", "Fertilizer", "Machinery", "Pest & Disease", "Seeds").filter { it.lowercase().contains(q) || q.contains(it.substringBefore(" ").lowercase()) }.take(4)
    Card(Modifier.fillMaxWidth().padding(horizontal = 12.dp), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Search results", fontWeight = FontWeight.Bold)
            if (matches.isNotEmpty()) {
                Text("Accounts", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                matches.forEach { farmer ->
                    Row(Modifier.fillMaxWidth().clickable { onUserClick(farmer) }, verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(36.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) { Text(farmer.avatarInitials, fontSize = 11.sp) }
                        Spacer(Modifier.width(8.dp)); Column { Text(farmer.name, fontWeight = FontWeight.SemiBold, fontSize = 13.sp); Text("${if (farmer.isExpert) "Zari-Mahir" else "Kisaan"} · ${farmer.district}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                    }
                }
            }
            Text("Community", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            Text("🌾 ${query.replaceFirstChar { it.uppercase() }} Farmers", fontSize = 13.sp)
            Text("Market", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            Text(if (categoryHits.isNotEmpty()) categoryHits.joinToString("  ·  ") else "Seeds · Fertilizer · Machinery", fontSize = 13.sp)
        }
    }
}

@Composable
private fun ForecastDayColumn(
    day: String,
    temp: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
