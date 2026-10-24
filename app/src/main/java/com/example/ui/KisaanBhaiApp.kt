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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.data.SampleData
import com.example.localization.AppLanguage
import com.example.localization.Strings
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.KisaanBookTheme
import com.example.viewmodel.AppTab
import com.example.viewmodel.HomeSubScreen
import com.example.viewmodel.KisaanBhaiViewModel

@Composable
fun KisaanBhaiApp(
    viewModel: KisaanBhaiViewModel = viewModel()
) {
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val homeSubScreen by viewModel.homeSubScreen.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()

    val farmCrops by viewModel.farmCrops.collectAsStateWithLifecycle()
    val khataEntries by viewModel.khataEntries.collectAsStateWithLifecycle()
    val communityPosts by viewModel.communityPosts.collectAsStateWithLifecycle()
    val communityFilter by viewModel.communityFilter.collectAsStateWithLifecycle()

    val posts by viewModel.posts.collectAsStateWithLifecycle()
    val stories by viewModel.stories.collectAsStateWithLifecycle()
    val activeStory by viewModel.activeStory.collectAsStateWithLifecycle()
    val activeFullscreenPost by viewModel.activeFullscreenPost.collectAsStateWithLifecycle()
    val questions by viewModel.questions.collectAsStateWithLifecycle()
    val listings by viewModel.listings.collectAsStateWithLifecycle()
    val mandiPrices by viewModel.mandiPrices.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()

    val selectedCategory by viewModel.selectedCategoryFilter.collectAsStateWithLifecycle()
    val selectedDistrict by viewModel.selectedDistrict.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isSearchActive by viewModel.isSearchActive.collectAsStateWithLifecycle()

    val showAddKhataDialog by viewModel.showAddKhataDialog.collectAsStateWithLifecycle()
    val showCreateCropDialog by viewModel.showCreateCropDialog.collectAsStateWithLifecycle()
    val showCreateCommunityPostDialog by viewModel.showCreateCommunityPostDialog.collectAsStateWithLifecycle()

    val showCreatePostSheet by viewModel.showCreatePostSheet.collectAsStateWithLifecycle()
    val showCreateQuestionSheet by viewModel.showCreateQuestionSheet.collectAsStateWithLifecycle()
    val showCreateListingSheet by viewModel.showCreateListingSheet.collectAsStateWithLifecycle()
    val showCreateStoryDialog by viewModel.showCreateStoryDialog.collectAsStateWithLifecycle()
    val activeCommentsPost by viewModel.activeCommentsPost.collectAsStateWithLifecycle()
    val activeContactListing by viewModel.activeContactListing.collectAsStateWithLifecycle()

    val showAgriGuidesDialog by viewModel.showAgriGuidesDialog.collectAsStateWithLifecycle()
    val showEditProfileDialog by viewModel.showEditProfileDialog.collectAsStateWithLifecycle()
    val showSettingsDialog by viewModel.showSettingsDialog.collectAsStateWithLifecycle()
    val showAuthDialog by viewModel.showAuthDialog.collectAsStateWithLifecycle()
    val authIsLoginMode by viewModel.authIsLoginMode.collectAsStateWithLifecycle()

    var showDistrictPickerDialog by remember { mutableStateOf(false) }

    val unreadNotifCount = remember(notifications) {
        notifications.count { !it.isRead }
    }

    // Predictive back navigation: if on a sub-screen inside Home, back returns to Home main.
    // If on another tab, back returns to Home tab.
    BackHandler(enabled = currentTab != AppTab.HOME || homeSubScreen != HomeSubScreen.HOME_MAIN) {
        if (currentTab == AppTab.HOME && homeSubScreen != HomeSubScreen.HOME_MAIN) {
            viewModel.setHomeSubScreen(HomeSubScreen.HOME_MAIN)
        } else {
            viewModel.setTab(AppTab.HOME)
        }
    }

    val isUrduOrPashto = appLanguage == AppLanguage.URDU || appLanguage == AppLanguage.PASHTO
    val layoutDirection = if (isUrduOrPashto) LayoutDirection.Ltr else LayoutDirection.Rtl

    KisaanBookTheme(
        themeMode = themeMode,
        isUrduOrPashto = isUrduOrPashto
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
            Scaffold(
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 6.dp
                    ) {
                    // Home (Farmer Utility Hub)
                    NavigationBarItem(
                        selected = currentTab == AppTab.HOME,
                        onClick = {
                            viewModel.setTab(AppTab.HOME)
                            viewModel.setHomeSubScreen(HomeSubScreen.HOME_MAIN)
                        },
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

                    // Community (Voice, Text, Photo)
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
                            when (homeSubScreen) {
                                HomeSubScreen.HOME_MAIN -> {
                                    HomeScreen(
                                        currentUser = currentUser,
                                        farmCrops = farmCrops,
                                        khataEntries = khataEntries,
                                        selectedDistrict = selectedDistrict,
                                        unreadNotifCount = unreadNotifCount,
                                        appLanguage = appLanguage,
                                        onDistrictChange = { viewModel.setDetectedLocation(it) },
                                        onNotificationClick = { viewModel.setTab(AppTab.NOTIFICATIONS) },
                                        onOpenPlantDoctor = { viewModel.setHomeSubScreen(HomeSubScreen.PLANT_DOCTOR) },
                                        onOpenMyCrops = { viewModel.setHomeSubScreen(HomeSubScreen.MY_CROPS) },
                                        onOpenCreateCrop = { viewModel.openCreateCropDialog() },
                                        onOpenKhata = { viewModel.setHomeSubScreen(HomeSubScreen.KHATA) },
                                        onOpenAddKhataEntry = { viewModel.openAddKhataDialog() },
                                        onOpenCalendar = { viewModel.setHomeSubScreen(HomeSubScreen.CALENDAR) }
                                    )
                                }
                                HomeSubScreen.PLANT_DOCTOR -> {
                                    PlantDoctorScreen(
                                        listings = listings,
                                        onBackClick = { viewModel.setHomeSubScreen(HomeSubScreen.HOME_MAIN) }
                                    )
                                }
                                HomeSubScreen.MY_CROPS -> {
                                    MyCropsScreen(
                                        crops = farmCrops,
                                        onAddCropClick = { viewModel.openCreateCropDialog() },
                                        onBackClick = { viewModel.setHomeSubScreen(HomeSubScreen.HOME_MAIN) }
                                    )
                                }
                                HomeSubScreen.KHATA -> {
                                    KhataScreen(
                                        entries = khataEntries,
                                        crops = farmCrops,
                                        onAddEntryClick = { viewModel.openAddKhataDialog() },
                                        onDeleteEntry = { viewModel.deleteKhataEntry(it) },
                                        onBackClick = { viewModel.setHomeSubScreen(HomeSubScreen.HOME_MAIN) },
                                        onCreateCropClick = { viewModel.openCreateCropDialog() }
                                    )
                                }
                                HomeSubScreen.CALENDAR -> {
                                    CalendarScreen(
                                        onBackClick = { viewModel.setHomeSubScreen(HomeSubScreen.HOME_MAIN) }
                                    )
                                }
                            }
                        }
                        AppTab.COMMUNITY -> {
                            CommunityScreen(
                                communityPosts = communityPosts,
                                selectedMediaFilter = communityFilter,
                                onMediaFilterSelect = { viewModel.setCommunityFilter(it) },
                                onCreatePostClick = { viewModel.openCreateCommunityPost() },
                                onToggleLike = { viewModel.toggleCommunityPostLike(it) },
                                onAddComment = { pId, text -> viewModel.addCommunityComment(pId, text) },
                                onAddReply = { pId, cId, text -> viewModel.addCommunityReply(pId, cId, text) },
                                onToggleCommentLike = { pId, cId -> viewModel.toggleCommunityCommentLike(pId, cId) },
                                onToggleReplyLike = { pId, cId, rId -> viewModel.toggleCommunityReplyLike(pId, cId, rId) }
                            )
                        }
                        AppTab.MARKETPLACE -> {
                            MarketplaceScreen(
                                listings = listings,
                                onCreateListingClick = { viewModel.openCreateListingSheet() },
                                onContactSellerClick = { viewModel.openContactSeller(it) }
                            )
                        }
                        AppTab.NOTIFICATIONS -> {
                            NotificationsScreen(
                                notifications = notifications,
                                onBackClick = { viewModel.setTab(AppTab.HOME) },
                                onNotificationClick = { viewModel.onNotificationClicked(it) },
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
                                onDeletePost = { viewModel.deletePost(it) },
                                onFollowClick = { viewModel.toggleFollow(it) },
                                onImageClick = { viewModel.openFullscreenPost(it) }
                            )
                        }
                    }
                }
            }
        }

        // Add Khata Entry Dialog
        if (showAddKhataDialog) {
            AddKhataEntryDialog(
                crops = farmCrops,
                language = appLanguage,
                onDismiss = { viewModel.closeAddKhataDialog() },
                onSave = { entry -> viewModel.addKhataEntry(entry) }
            )
        }

        // Create Farm Crop Dialog
        if (showCreateCropDialog) {
            CreateCropDialog(
                userDistrict = selectedDistrict,
                onDismiss = { viewModel.closeCreateCropDialog() },
                onCropCreated = { newCrop -> viewModel.addFarmCrop(newCrop) }
            )
        }

        // Create Community Post Dialog (Text, Voice, Picture)
        if (showCreateCommunityPostDialog) {
            CreateCommunityPostDialog(
                currentUser = currentUser,
                onDismiss = { viewModel.closeCreateCommunityPost() },
                onSubmitPost = { newPost -> viewModel.addCommunityPost(newPost) }
            )
        }

        // Story Viewer Modal
        activeStory?.let { story ->
            StoryViewerDialog(
                stories = stories,
                initialStory = story,
                onDismiss = { viewModel.closeStory() },
                onLikeStory = { viewModel.likeStory(it) },
                onCommentStory = { id, text -> viewModel.addStoryComment(id, text) }
            )
        }

        // Create Story Dialog
        if (showCreateStoryDialog) {
            CreateStoryDialog(
                currentUser = currentUser,
                onDismiss = { viewModel.closeCreateStory() },
                onPostStory = { type, caption, res, uri, bg ->
                    viewModel.addStory(type, caption, res, uri, bg)
                }
            )
        }

        // Fullscreen Post Viewer Modal
        activeFullscreenPost?.let { post ->
            FullscreenPostViewer(
                post = post,
                onDismiss = { viewModel.closeFullscreenPost() },
                onLikeClick = { viewModel.toggleLike(post.id) },
                onCommentClick = { viewModel.openComments(post) },
                onFollowClick = { viewModel.toggleFollow(it) }
            )
        }

        // Modal Sheets and Dialogs
        if (showCreatePostSheet) {
            CreatePostDialog(
                userDistrict = currentUser.district,
                onDismiss = { viewModel.closeCreatePostSheet() },
                onPublish = { text, category, postType, district, imageRes, imageUri, customType ->
                    viewModel.createPost(text, category, postType, district, imageRes, imageUri, customType)
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
                onAddComment = { text -> viewModel.addComment(post.id, text) },
                onReplyToComment = { commentId, replyText -> viewModel.replyToComment(post.id, commentId, replyText) },
                onLikeComment = { commentId -> viewModel.likeComment(post.id, commentId) },
                onLikeReply = { commentId, replyId -> viewModel.likeReply(post.id, commentId, replyId) }
            )
        }

        activeContactListing?.let { listing ->
            ContactSellerSheet(
                listing = listing,
                onDismiss = { viewModel.closeContactSeller() }
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
                onSave = { name, dist, crops, bio, avatarUri, userType ->
                    viewModel.updateProfile(name, dist, crops, bio, avatarUri, userType)
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
}
