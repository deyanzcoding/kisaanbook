package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.data.SampleData
import com.example.localization.Strings
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.KisaanBookTheme
import com.example.viewmodel.AppTab
import com.example.viewmodel.KisaanBhaiViewModel

@Composable
fun KisaanBhaiApp(
    viewModel: KisaanBhaiViewModel = viewModel()
) {
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()

    val posts by viewModel.posts.collectAsStateWithLifecycle()
    val stories by viewModel.stories.collectAsStateWithLifecycle()
    val activeStory by viewModel.activeStory.collectAsStateWithLifecycle()
    val questions by viewModel.questions.collectAsStateWithLifecycle()
    val listings by viewModel.listings.collectAsStateWithLifecycle()
    val mandiPrices by viewModel.mandiPrices.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()

    val selectedCategory by viewModel.selectedCategoryFilter.collectAsStateWithLifecycle()
    val selectedDistrict by viewModel.selectedDistrict.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isSearchActive by viewModel.isSearchActive.collectAsStateWithLifecycle()

    val showCreatePostSheet by viewModel.showCreatePostSheet.collectAsStateWithLifecycle()
    val showCreateQuestionSheet by viewModel.showCreateQuestionSheet.collectAsStateWithLifecycle()
    val showCreateListingSheet by viewModel.showCreateListingSheet.collectAsStateWithLifecycle()
    val activeCommentsPost by viewModel.activeCommentsPost.collectAsStateWithLifecycle()
    val activeContactListing by viewModel.activeContactListing.collectAsStateWithLifecycle()
    val showWeatherMandiDialog by viewModel.showWeatherMandiDialog.collectAsStateWithLifecycle()
    val showUpdateMandiDialog by viewModel.showUpdateMandiDialog.collectAsStateWithLifecycle()
    val showAgriGuidesDialog by viewModel.showAgriGuidesDialog.collectAsStateWithLifecycle()
    val showEditProfileDialog by viewModel.showEditProfileDialog.collectAsStateWithLifecycle()
    val showSettingsDialog by viewModel.showSettingsDialog.collectAsStateWithLifecycle()
    val showAuthDialog by viewModel.showAuthDialog.collectAsStateWithLifecycle()
    val authIsLoginMode by viewModel.authIsLoginMode.collectAsStateWithLifecycle()

    var showDistrictPickerDialog by remember { mutableStateOf(false) }

    val unreadNotifCount = remember(notifications) {
        notifications.count { !it.isRead }
    }

    // Predictive back navigation: if not on Home, back button returns to Home
    BackHandler(enabled = currentTab != AppTab.HOME) {
        viewModel.setTab(AppTab.HOME)
    }

    KisaanBookTheme(themeMode = themeMode) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    // Home (Facebook style feed)
                    NavigationBarItem(
                        selected = currentTab == AppTab.HOME,
                        onClick = { viewModel.setTab(AppTab.HOME) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == AppTab.HOME) Icons.Default.Home else Icons.Outlined.Home,
                                contentDescription = Strings.tabHome(appLanguage)
                            )
                        },
                        label = { Text(Strings.tabHome(appLanguage), fontSize = 11.sp, fontWeight = if (currentTab == AppTab.HOME) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_home")
                    )

                    // Community (Q&A)
                    NavigationBarItem(
                        selected = currentTab == AppTab.COMMUNITY,
                        onClick = { viewModel.setTab(AppTab.COMMUNITY) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == AppTab.COMMUNITY) Icons.Default.Forum else Icons.Outlined.Forum,
                                contentDescription = Strings.tabCommunity(appLanguage)
                            )
                        },
                        label = { Text(Strings.tabCommunity(appLanguage), fontSize = 11.sp, fontWeight = if (currentTab == AppTab.COMMUNITY) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_community")
                    )

                    // Sell & Buy / Market
                    NavigationBarItem(
                        selected = currentTab == AppTab.MARKETPLACE,
                        onClick = { viewModel.setTab(AppTab.MARKETPLACE) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == AppTab.MARKETPLACE) Icons.Default.Storefront else Icons.Outlined.Storefront,
                                contentDescription = Strings.tabMarket(appLanguage)
                            )
                        },
                        label = { Text(Strings.tabMarket(appLanguage), fontSize = 11.sp, fontWeight = if (currentTab == AppTab.MARKETPLACE) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_marketplace")
                    )

                    // Notifications / Alerts
                    NavigationBarItem(
                        selected = currentTab == AppTab.NOTIFICATIONS,
                        onClick = { viewModel.setTab(AppTab.NOTIFICATIONS) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (unreadNotifCount > 0) {
                                        Badge { Text("$unreadNotifCount") }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (currentTab == AppTab.NOTIFICATIONS) Icons.Default.Notifications else Icons.Outlined.Notifications,
                                    contentDescription = Strings.tabAlerts(appLanguage)
                                )
                            }
                        },
                        label = { Text(Strings.tabAlerts(appLanguage), fontSize = 11.sp, fontWeight = if (currentTab == AppTab.NOTIFICATIONS) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_notifications")
                    )

                    // Profile
                    NavigationBarItem(
                        selected = currentTab == AppTab.PROFILE,
                        onClick = { viewModel.setTab(AppTab.PROFILE) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == AppTab.PROFILE) Icons.Default.Person else Icons.Outlined.Person,
                                contentDescription = Strings.tabProfile(appLanguage)
                            )
                        },
                        label = { Text(Strings.tabProfile(appLanguage), fontSize = 11.sp, fontWeight = if (currentTab == AppTab.PROFILE) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_profile")
                    )
                }
            },
            floatingActionButton = {
                if (currentTab == AppTab.HOME || currentTab == AppTab.PROFILE) {
                    FloatingActionButton(
                        onClick = { viewModel.openCreatePostSheet() },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        shape = CircleShape,
                        modifier = Modifier.testTag("create_post_fab")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = Strings.createPost(appLanguage),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Crossfade(targetState = currentTab, label = "tab_transition") { tab ->
                    when (tab) {
                        AppTab.HOME -> {
                            HomeScreen(
                                posts = posts,
                                stories = stories,
                                currentUser = currentUser,
                                isLoggedIn = isLoggedIn,
                                selectedCategory = selectedCategory,
                                selectedDistrict = selectedDistrict,
                                searchQuery = searchQuery,
                                isSearchActive = isSearchActive,
                                unreadNotifCount = unreadNotifCount,
                                appLanguage = appLanguage,
                                onCategorySelect = { viewModel.setCategoryFilter(it) },
                                onDistrictChange = { viewModel.setDetectedLocation(it) },
                                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                onToggleSearch = { viewModel.toggleSearchActive(it) },
                                onNotificationClick = { viewModel.setTab(AppTab.NOTIFICATIONS) },
                                onAuthClick = { viewModel.openAuth(isLogin = true) },
                                onLikeClick = { viewModel.toggleLike(it) },
                                onCommentClick = { viewModel.openComments(it) },
                                onSaveClick = { viewModel.toggleSave(it) },
                                onStoryClick = { viewModel.openStory(it) },
                                onCreateStoryClick = {
                                    viewModel.addStory(
                                        imageRes = R.drawable.img_wheat_field,
                                        caption = "Fresh farm update from ${currentUser.name} in $selectedDistrict!"
                                    )
                                },
                                onOpenCreatePost = { viewModel.openCreatePostSheet() },
                                onOpenAskQuestion = { viewModel.openCreateQuestionSheet() }
                            )
                        }
                        AppTab.COMMUNITY -> {
                            CommunityScreen(
                                questions = questions,
                                selectedCategory = selectedCategory,
                                onCategorySelect = { viewModel.setCategoryFilter(it) },
                                onAskQuestionClick = { viewModel.openCreateQuestionSheet() },
                                onAddAnswer = { qId, text -> viewModel.addAnswer(qId, text) },
                                onMarkAccepted = { qId, aId -> viewModel.markAcceptedAnswer(qId, aId) },
                                onUpvoteAnswer = { qId, aId -> viewModel.upvoteAnswer(qId, aId) }
                            )
                        }
                        AppTab.MARKETPLACE -> {
                            MarketplaceScreen(
                                listings = listings,
                                onCreateListingClick = { viewModel.openCreateListingSheet() },
                                onContactSellerClick = { viewModel.openContactSeller(it) },
                                onOpenMandiRatesClick = { viewModel.openWeatherMandi() },
                                onUpdateMandiRatesClick = { viewModel.openUpdateMandiDialog() }
                            )
                        }
                        AppTab.NOTIFICATIONS -> {
                            NotificationsScreen(
                                notifications = notifications,
                                onMarkAsRead = { viewModel.markNotificationRead(it) },
                                onMarkAllRead = { viewModel.markAllNotificationsRead() }
                            )
                        }
                        AppTab.PROFILE -> {
                            ProfileScreen(
                                farmer = currentUser,
                                isLoggedIn = isLoggedIn,
                                appLanguage = appLanguage,
                                posts = posts,
                                questions = questions,
                                savedPosts = posts.filter { it.isSaved },
                                onEditProfileClick = { viewModel.openEditProfile() },
                                onSettingsClick = { viewModel.openSettings() },
                                onAuthClick = { viewModel.openAuth(isLogin = true) },
                                onLogoutClick = { viewModel.logout() },
                                onLikeClick = { viewModel.toggleLike(it) },
                                onCommentClick = { viewModel.openComments(it) },
                                onSaveClick = { viewModel.toggleSave(it) },
                                onDeletePost = { viewModel.deletePost(it) }
                            )
                        }
                    }
                }
            }
        }

        // Story Viewer Modal
        activeStory?.let { story ->
            StoryViewerDialog(
                story = story,
                onDismiss = { viewModel.closeStory() }
            )
        }

        // Modal Sheets and Dialogs
        if (showCreatePostSheet) {
            CreatePostDialog(
                userDistrict = currentUser.district,
                onDismiss = { viewModel.closeCreatePostSheet() },
                onPublish = { text, category, postType, district, imageRes ->
                    viewModel.createPost(text, category, postType, district, imageRes)
                }
            )
        }

        if (showCreateQuestionSheet) {
            AskQuestionDialog(
                userDistrict = currentUser.district,
                onDismiss = { viewModel.closeCreateQuestionSheet() },
                onSubmit = { title, desc, cat, dist ->
                    viewModel.addQuestion(title, desc, cat, dist)
                }
            )
        }

        if (showCreateListingSheet) {
            CreateListingDialog(
                userDistrict = currentUser.district,
                onDismiss = { viewModel.closeCreateListingSheet() },
                onPublish = { title, cat, price, unit, desc, dist, phone, qty, imgRes ->
                    viewModel.createListing(title, cat, price, unit, desc, dist, phone, qty, imgRes)
                }
            )
        }

        activeCommentsPost?.let { post ->
            CommentsBottomSheet(
                post = post,
                onDismiss = { viewModel.closeComments() },
                onAddComment = { text -> viewModel.addComment(post.id, text) }
            )
        }

        activeContactListing?.let { listing ->
            ContactSellerSheet(
                listing = listing,
                onDismiss = { viewModel.closeContactSeller() }
            )
        }

        if (showWeatherMandiDialog) {
            WeatherMandiDialog(
                currentDistrict = selectedDistrict,
                mandiPrices = mandiPrices,
                onUpdateRatesClick = {
                    viewModel.closeWeatherMandi()
                    viewModel.openUpdateMandiDialog()
                },
                onDismiss = { viewModel.closeWeatherMandi() }
            )
        }

        // Update / Add Mandi Rate Dialog
        if (showUpdateMandiDialog) {
            UpdateMandiRateDialog(
                initialDistrict = selectedDistrict,
                onDismiss = { viewModel.closeUpdateMandiDialog() },
                onSaveRate = { crop, cropUrdu, mandi, min, max, unit ->
                    viewModel.updateOrAddMandiPrice(crop, cropUrdu, mandi, min, max, unit)
                }
            )
        }

        if (showAgriGuidesDialog) {
            AgriGuidesDialog(
                onDismiss = { viewModel.closeAgriGuides() }
            )
        }

        if (showEditProfileDialog) {
            EditProfileDialog(
                farmer = currentUser,
                onDismiss = { viewModel.closeEditProfile() },
                onSave = { name, dist, crops, bio ->
                    viewModel.updateProfile(name, dist, crops, bio)
                }
            )
        }

        // Settings Dialog (Theme Mode & Language switchers)
        if (showSettingsDialog) {
            SettingsDialog(
                currentTheme = themeMode,
                currentLanguage = appLanguage,
                currentUser = currentUser,
                isLoggedIn = isLoggedIn,
                onThemeChange = { viewModel.setThemeMode(it) },
                onLanguageChange = { viewModel.setLanguage(it) },
                onOpenAuth = { viewModel.openAuth(isLogin = true) },
                onLogout = { viewModel.logout() },
                onDismiss = { viewModel.closeSettings() }
            )
        }

        // Authentication Dialog (Log In & Sign Up)
        if (showAuthDialog) {
            AuthDialog(
                isLoginModeInitial = authIsLoginMode,
                currentLanguage = appLanguage,
                onLogin = { phone, pin -> viewModel.login(phone, pin) },
                onSignup = { name, phone, district, crops, pin ->
                    viewModel.signup(name, phone, district, crops, pin)
                },
                onDismiss = { viewModel.closeAuth() }
            )
        }

        // District Picker Dialog
        if (showDistrictPickerDialog) {
            Dialog(onDismissRequest = { showDistrictPickerDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.7f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = Strings.selectDistrict(appLanguage),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(SampleData.pakistaniDistricts) { district ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            viewModel.setSelectedDistrict(district)
                                            showDistrictPickerDialog = false
                                        },
                                    color = if (district == selectedDistrict) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                ) {
                                    Text(
                                        text = district,
                                        fontSize = 14.sp,
                                        fontWeight = if (district == selectedDistrict) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                        color = if (district == selectedDistrict) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                        TextButton(
                            onClick = { showDistrictPickerDialog = false },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Cancel / منسوخ")
                        }
                    }
                }
            }
        }
    }
}
