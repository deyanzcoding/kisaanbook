package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.SampleData
import com.example.localization.AppLanguage
import com.example.model.*
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class AppTab(val title: String, val titleUrdu: String) {
    HOME("Home", "ہوم"),
    COMMUNITY("Community", "کمیونٹی"),
    MARKETPLACE("Sell & Buy", "منڈی"),
    NOTIFICATIONS("Alerts", "اطلاعات"),
    PROFILE("Profile", "پروفائل")
}

class KisaanBhaiViewModel : ViewModel() {

    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _currentUser = MutableStateFlow(SampleData.currentUser)
    val currentUser: StateFlow<Farmer> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _appLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val appLanguage: StateFlow<AppLanguage> = _appLanguage.asStateFlow()

    private val _posts = MutableStateFlow(SampleData.initialPosts)
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _stories = MutableStateFlow(SampleData.initialStories)
    val stories: StateFlow<List<FarmerStory>> = _stories.asStateFlow()

    private val _activeStory = MutableStateFlow<FarmerStory?>(null)
    val activeStory: StateFlow<FarmerStory?> = _activeStory.asStateFlow()

    private val _questions = MutableStateFlow(SampleData.initialQuestions)
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    private val _listings = MutableStateFlow(SampleData.initialListings)
    val listings: StateFlow<List<MarketplaceListing>> = _listings.asStateFlow()

    private val _mandiPrices = MutableStateFlow(SampleData.mandiPrices)
    val mandiPrices: StateFlow<List<MandiPrice>> = _mandiPrices.asStateFlow()

    private val _notifications = MutableStateFlow(SampleData.initialNotifications)
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow("All Crops")
    val selectedCategoryFilter: StateFlow<String> = _selectedCategoryFilter.asStateFlow()

    private val _selectedDistrict = MutableStateFlow("Mardan")
    val selectedDistrict: StateFlow<String> = _selectedDistrict.asStateFlow()

    private val _isDetectingLocation = MutableStateFlow(false)
    val isDetectingLocation: StateFlow<Boolean> = _isDetectingLocation.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    // Dialog & Sheet Controls
    private val _showCreatePostSheet = MutableStateFlow(false)
    val showCreatePostSheet: StateFlow<Boolean> = _showCreatePostSheet.asStateFlow()

    private val _showCreateQuestionSheet = MutableStateFlow(false)
    val showCreateQuestionSheet: StateFlow<Boolean> = _showCreateQuestionSheet.asStateFlow()

    private val _showCreateListingSheet = MutableStateFlow(false)
    val showCreateListingSheet: StateFlow<Boolean> = _showCreateListingSheet.asStateFlow()

    private val _activeCommentsPost = MutableStateFlow<Post?>(null)
    val activeCommentsPost: StateFlow<Post?> = _activeCommentsPost.asStateFlow()

    private val _activeContactListing = MutableStateFlow<MarketplaceListing?>(null)
    val activeContactListing: StateFlow<MarketplaceListing?> = _activeContactListing.asStateFlow()

    private val _showWeatherMandiDialog = MutableStateFlow(false)
    val showWeatherMandiDialog: StateFlow<Boolean> = _showWeatherMandiDialog.asStateFlow()

    private val _showUpdateMandiDialog = MutableStateFlow(false)
    val showUpdateMandiDialog: StateFlow<Boolean> = _showUpdateMandiDialog.asStateFlow()

    private val _showAgriGuidesDialog = MutableStateFlow(false)
    val showAgriGuidesDialog: StateFlow<Boolean> = _showAgriGuidesDialog.asStateFlow()

    private val _showEditProfileDialog = MutableStateFlow(false)
    val showEditProfileDialog: StateFlow<Boolean> = _showEditProfileDialog.asStateFlow()

    private val _showSettingsDialog = MutableStateFlow(false)
    val showSettingsDialog: StateFlow<Boolean> = _showSettingsDialog.asStateFlow()

    private val _showAuthDialog = MutableStateFlow(false)
    val showAuthDialog: StateFlow<Boolean> = _showAuthDialog.asStateFlow()

    private val _authIsLoginMode = MutableStateFlow(true)
    val authIsLoginMode: StateFlow<Boolean> = _authIsLoginMode.asStateFlow()

    fun setTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun setCategoryFilter(category: String) {
        _selectedCategoryFilter.value = category
    }

    fun setSelectedDistrict(district: String) {
        _selectedDistrict.value = district
    }

    fun setDetectedLocation(locality: String) {
        _selectedDistrict.value = locality
        _currentUser.update { it.copy(district = "$locality, Pakistan") }
        _isDetectingLocation.value = false
    }

    fun setDetectingLocation(detecting: Boolean) {
        _isDetectingLocation.value = detecting
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleSearchActive(active: Boolean) {
        _isSearchActive.value = active
        if (!active) _searchQuery.value = ""
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }

    fun setLanguage(language: AppLanguage) {
        _appLanguage.value = language
    }

    fun cycleLanguage() {
        _appLanguage.value = when (_appLanguage.value) {
            AppLanguage.ENGLISH -> AppLanguage.URDU
            AppLanguage.URDU -> AppLanguage.PASHTO
            AppLanguage.PASHTO -> AppLanguage.ENGLISH
        }
    }

    // Story Actions
    fun openStory(story: FarmerStory) {
        _activeStory.value = story
    }

    fun closeStory() {
        _activeStory.value = null
    }

    fun addStory(imageRes: Int, caption: String) {
        val newStory = FarmerStory(
            id = "story_${System.currentTimeMillis()}",
            authorName = _currentUser.value.name,
            authorDistrict = _currentUser.value.district.split(",").firstOrNull()?.trim() ?: "Mardan",
            avatarInitials = _currentUser.value.avatarInitials,
            imageRes = imageRes,
            caption = caption.ifBlank { "Live farm update from ${_currentUser.value.name}" },
            timeAgo = "Just now"
        )
        _stories.update { listOf(newStory) + it }
    }

    // Authentication Actions
    fun login(phone: String, pin: String) {
        _currentUser.value = SampleData.currentUser.copy(
            district = if (_currentUser.value.district.isBlank()) "$_selectedDistrict, Pakistan" else _currentUser.value.district
        )
        _isLoggedIn.value = true
        _showAuthDialog.value = false
    }

    fun signup(name: String, phone: String, district: String, cropsStr: String, pin: String) {
        val crops = cropsStr.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        val newFarmer = Farmer(
            id = "user_${System.currentTimeMillis()}",
            name = name.trim().ifEmpty { "Kisaan Farmer" },
            username = name.trim().lowercase().replace(" ", "_").ifEmpty { "kisaan_${phone.takeLast(4)}" },
            district = district.ifEmpty { "Mardan, Pakistan" },
            mainCrops = crops.ifEmpty { listOf("Wheat", "Maize") },
            experienceYears = 3,
            bio = "Active farmer registered on Kisaan Book.",
            isExpert = false,
            isFollowing = false,
            followersCount = 1,
            followingCount = 3,
            postsCount = 0
        )
        _currentUser.value = newFarmer
        _selectedDistrict.value = district.split(",").firstOrNull()?.trim() ?: "Mardan"
        _isLoggedIn.value = true
        _showAuthDialog.value = false
    }

    fun logout() {
        _isLoggedIn.value = false
    }

    // Post Actions
    fun toggleLike(postId: String) {
        if (!_isLoggedIn.value) {
            _authIsLoginMode.value = true
            _showAuthDialog.value = true
            return
        }
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    val newLiked = !post.isLiked
                    val newCount = if (newLiked) post.likeCount + 1 else post.likeCount - 1
                    post.copy(isLiked = newLiked, likeCount = maxOf(0, newCount))
                } else post
            }
        }
        _activeCommentsPost.value?.let { active ->
            if (active.id == postId) {
                _activeCommentsPost.value = _posts.value.find { it.id == postId }
            }
        }
    }

    fun toggleSave(postId: String) {
        if (!_isLoggedIn.value) {
            _authIsLoginMode.value = true
            _showAuthDialog.value = true
            return
        }
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) post.copy(isSaved = !post.isSaved) else post
            }
        }
    }

    fun addComment(postId: String, text: String) {
        if (text.isBlank()) return
        if (!_isLoggedIn.value) {
            _authIsLoginMode.value = true
            _showAuthDialog.value = true
            return
        }
        val newComment = Comment(
            id = "comment_${System.currentTimeMillis()}",
            authorName = _currentUser.value.name,
            authorDistrict = _currentUser.value.district.split(",").firstOrNull()?.trim() ?: "Mardan",
            text = text.trim(),
            timestamp = "Just now",
            isExpert = false
        )

        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    post.copy(
                        comments = listOf(newComment) + post.comments,
                        commentCount = post.commentCount + 1
                    )
                } else post
            }
        }

        _activeCommentsPost.value = _posts.value.find { it.id == postId }
    }

    fun createPost(
        text: String,
        category: String,
        postType: PostType,
        district: String,
        imageRes: Int?
    ) {
        if (text.isBlank()) return
        if (!_isLoggedIn.value) {
            _authIsLoginMode.value = true
            _showAuthDialog.value = true
            return
        }
        val newPost = Post(
            id = "post_${System.currentTimeMillis()}",
            author = _currentUser.value,
            text = text.trim(),
            imageRes = imageRes,
            category = category,
            postType = postType,
            district = district,
            timestamp = "Just now",
            likeCount = 0,
            commentCount = 0,
            isLiked = false,
            isSaved = false,
            comments = emptyList()
        )

        _posts.update { listOf(newPost) + it }
        _currentUser.update { it.copy(postsCount = it.postsCount + 1) }
        _showCreatePostSheet.value = false
    }

    fun deletePost(postId: String) {
        _posts.update { list -> list.filterNot { it.id == postId } }
        _currentUser.update { it.copy(postsCount = maxOf(0, it.postsCount - 1)) }
    }

    // Community Question Actions
    fun addQuestion(title: String, description: String, cropCategory: String, district: String) {
        if (title.isBlank()) return
        if (!_isLoggedIn.value) {
            _authIsLoginMode.value = true
            _showAuthDialog.value = true
            return
        }
        val newQuestion = Question(
            id = "q_${System.currentTimeMillis()}",
            author = _currentUser.value,
            title = title.trim(),
            description = description.trim(),
            cropCategory = cropCategory,
            district = district,
            timestamp = "Just now",
            acceptedAnswerId = null,
            answers = emptyList()
        )
        _questions.update { listOf(newQuestion) + it }
        _showCreateQuestionSheet.value = false
    }

    fun addAnswer(questionId: String, text: String) {
        if (text.isBlank()) return
        if (!_isLoggedIn.value) {
            _authIsLoginMode.value = true
            _showAuthDialog.value = true
            return
        }
        val newAnswer = Answer(
            id = "ans_${System.currentTimeMillis()}",
            authorName = _currentUser.value.name,
            authorRole = "Local Farmer",
            district = _currentUser.value.district.split(",").firstOrNull()?.trim() ?: "Mardan",
            text = text.trim(),
            timestamp = "Just now",
            upvotes = 1,
            isAccepted = false,
            isExpert = false
        )

        _questions.update { list ->
            list.map { q ->
                if (q.id == questionId) {
                    q.copy(answers = q.answers + newAnswer)
                } else q
            }
        }
    }

    fun markAcceptedAnswer(questionId: String, answerId: String) {
        _questions.update { list ->
            list.map { q ->
                if (q.id == questionId) {
                    val updatedAnswers = q.answers.map { ans ->
                        ans.copy(isAccepted = (ans.id == answerId))
                    }
                    q.copy(acceptedAnswerId = answerId, answers = updatedAnswers)
                } else q
            }
        }
    }

    fun upvoteAnswer(questionId: String, answerId: String) {
        _questions.update { list ->
            list.map { q ->
                if (q.id == questionId) {
                    val updatedAnswers = q.answers.map { ans ->
                        if (ans.id == answerId) ans.copy(upvotes = ans.upvotes + 1) else ans
                    }
                    q.copy(answers = updatedAnswers)
                } else q
            }
        }
    }

    // Marketplace Actions
    fun createListing(
        title: String,
        category: String,
        price: Long,
        unit: String,
        description: String,
        district: String,
        phone: String,
        quantity: String?,
        imageRes: Int?
    ) {
        if (title.isBlank()) return
        if (!_isLoggedIn.value) {
            _authIsLoginMode.value = true
            _showAuthDialog.value = true
            return
        }
        val newListing = MarketplaceListing(
            id = "list_${System.currentTimeMillis()}",
            title = title.trim(),
            category = category,
            price = price,
            unit = unit.trim(),
            sellerName = _currentUser.value.name,
            sellerDistrict = district,
            sellerPhone = phone.trim(),
            description = description.trim(),
            imageRes = imageRes,
            timestamp = "Just now",
            isVerifiedSeller = true,
            availableQuantity = quantity
        )
        _listings.update { listOf(newListing) + it }
        _showCreateListingSheet.value = false
    }

    // Mandi Price Actions
    fun updateOrAddMandiPrice(
        cropName: String,
        cropNameUrdu: String,
        mandiLocation: String,
        priceMin: Int,
        priceMax: Int,
        unit: String
    ) {
        val existingIndex = _mandiPrices.value.indexOfFirst {
            it.cropName.equals(cropName, ignoreCase = true) || it.cropNameUrdu.equals(cropNameUrdu, ignoreCase = true)
        }

        val updatedItem = MandiPrice(
            cropName = cropName.trim(),
            cropNameUrdu = cropNameUrdu.trim(),
            mandiLocation = mandiLocation.trim(),
            priceMin = priceMin,
            priceMax = priceMax,
            unit = unit.trim(),
            dateUpdated = "Updated Today by Farmer"
        )

        _mandiPrices.update { current ->
            if (existingIndex >= 0) {
                current.toMutableList().apply { set(existingIndex, updatedItem) }
            } else {
                listOf(updatedItem) + current
            }
        }
        _showUpdateMandiDialog.value = false
    }

    // Notifications Actions
    fun markNotificationRead(id: String) {
        _notifications.update { list ->
            list.map { if (it.id == id) it.copy(isRead = true) else it }
        }
    }

    fun markAllNotificationsRead() {
        _notifications.update { list -> list.map { it.copy(isRead = true) } }
    }

    // Profile Actions
    fun updateProfile(name: String, district: String, cropsStr: String, bio: String) {
        val crops = cropsStr.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        _currentUser.update {
            it.copy(
                name = name.trim(),
                district = district.trim(),
                mainCrops = crops.ifEmpty { listOf("Wheat", "Maize") },
                bio = bio.trim()
            )
        }
        _showEditProfileDialog.value = false
    }

    // UI Dialog triggers
    fun openCreatePostSheet() {
        if (!_isLoggedIn.value) {
            _authIsLoginMode.value = true
            _showAuthDialog.value = true
        } else {
            _showCreatePostSheet.value = true
        }
    }
    fun closeCreatePostSheet() { _showCreatePostSheet.value = false }

    fun openCreateQuestionSheet() {
        if (!_isLoggedIn.value) {
            _authIsLoginMode.value = true
            _showAuthDialog.value = true
        } else {
            _showCreateQuestionSheet.value = true
        }
    }
    fun closeCreateQuestionSheet() { _showCreateQuestionSheet.value = false }

    fun openCreateListingSheet() {
        if (!_isLoggedIn.value) {
            _authIsLoginMode.value = true
            _showAuthDialog.value = true
        } else {
            _showCreateListingSheet.value = true
        }
    }
    fun closeCreateListingSheet() { _showCreateListingSheet.value = false }

    fun openComments(post: Post) { _activeCommentsPost.value = post }
    fun closeComments() { _activeCommentsPost.value = null }

    fun openContactSeller(listing: MarketplaceListing) { _activeContactListing.value = listing }
    fun closeContactSeller() { _activeContactListing.value = null }

    fun openWeatherMandi() { _showWeatherMandiDialog.value = true }
    fun closeWeatherMandi() { _showWeatherMandiDialog.value = false }

    fun openUpdateMandiDialog() { _showUpdateMandiDialog.value = true }
    fun closeUpdateMandiDialog() { _showUpdateMandiDialog.value = false }

    fun openAgriGuides() { _showAgriGuidesDialog.value = true }
    fun closeAgriGuides() { _showAgriGuidesDialog.value = false }

    fun openEditProfile() { _showEditProfileDialog.value = true }
    fun closeEditProfile() { _showEditProfileDialog.value = false }

    fun openSettings() { _showSettingsDialog.value = true }
    fun closeSettings() { _showSettingsDialog.value = false }

    fun openAuth(isLogin: Boolean = true) {
        _authIsLoginMode.value = isLogin
        _showAuthDialog.value = true
    }
    fun closeAuth() { _showAuthDialog.value = false }
    fun setAuthMode(isLogin: Boolean) { _authIsLoginMode.value = isLogin }
}
