package com.example.model

enum class PostType(val labelEn: String, val labelUr: String) {
    GENERAL("Update", "عام معلومات"),
    QUESTION("Question", "سوال"),
    CROP_PROBLEM("Crop Issue", "فصل کا مسئلہ"),
    SUCCESS("Success Story", "کامیابی"),
    LISTING("Marketplace", "خرید و فروخت"),
    AGRI_NEWS("Advisory", "زرعی مشورہ")
}

data class Farmer(
    val id: String,
    val name: String,
    val username: String,
    val district: String,
    val mainCrops: List<String>,
    val experienceYears: Int = 5,
    val bio: String,
    val isExpert: Boolean = false,
    val isFollowing: Boolean = false,
    val followersCount: Int = 120,
    val followingCount: Int = 45,
    val postsCount: Int = 12,
    val avatarInitials: String = name.take(2).uppercase()
)

data class Comment(
    val id: String,
    val authorName: String,
    val authorDistrict: String,
    val text: String,
    val timestamp: String,
    val isExpert: Boolean = false
)

data class Post(
    val id: String,
    val author: Farmer,
    val text: String,
    val imageRes: Int? = null,
    val category: String,
    val postType: PostType,
    val district: String,
    val timestamp: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val comments: List<Comment> = emptyList()
)

data class Answer(
    val id: String,
    val authorName: String,
    val authorRole: String,
    val district: String,
    val text: String,
    val timestamp: String,
    val upvotes: Int,
    val isAccepted: Boolean = false,
    val isExpert: Boolean = false
)

data class Question(
    val id: String,
    val author: Farmer,
    val title: String,
    val description: String,
    val cropCategory: String,
    val district: String,
    val timestamp: String,
    val acceptedAnswerId: String? = null,
    val answers: List<Answer> = emptyList()
) {
    val isSolved: Boolean get() = acceptedAnswerId != null
    val repliesCount: Int get() = answers.size
}

data class MarketplaceListing(
    val id: String,
    val title: String,
    val category: String,
    val price: Long,
    val unit: String,
    val sellerName: String,
    val sellerDistrict: String,
    val sellerPhone: String,
    val description: String,
    val imageRes: Int? = null,
    val timestamp: String,
    val isVerifiedSeller: Boolean = true,
    val availableQuantity: String? = null
)

enum class NotificationType {
    LIKE, COMMENT, REPLY, FOLLOW, ADVISORY
}

data class NotificationItem(
    val id: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean = false,
    val actorName: String
)

data class MandiPrice(
    val cropName: String,
    val cropNameUrdu: String,
    val mandiLocation: String,
    val priceMin: Int,
    val priceMax: Int,
    val unit: String,
    val dateUpdated: String
)

data class AgriArticle(
    val id: String,
    val title: String,
    val titleUrdu: String,
    val category: String,
    val summary: String,
    val content: String,
    val source: String,
    val readTime: String
)

data class FarmerStory(
    val id: String,
    val authorName: String,
    val authorDistrict: String,
    val avatarInitials: String,
    val imageRes: Int,
    val caption: String,
    val timeAgo: String,
    val isViewed: Boolean = false
)
