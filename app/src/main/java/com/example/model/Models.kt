package com.example.model

enum class UserType(val code: String, val labelEn: String, val labelUr: String) {
    FARMER("kisaan", "Kisaan", "کسان"),
    EXPERT("zari-mahir", "Zari-Mahir", "زرعی ماہر"),
    DEALER("dokandar", "Dokandar", "دوکاندار")
}

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
    val userType: UserType = if (isExpert) UserType.EXPERT else UserType.FARMER,
    val isFollowing: Boolean = false,
    val followersCount: Int = 120,
    val followingCount: Int = 45,
    val postsCount: Int = 12,
    val avatarInitials: String = name.take(2).uppercase(),
    val avatarUri: String? = null
)

data class ReplyComment(
    val id: String,
    val authorName: String,
    val authorType: UserType = UserType.FARMER,
    val authorDistrict: String,
    val text: String,
    val timestamp: String,
    val isLiked: Boolean = false,
    val likeCount: Int = 0
)

data class Comment(
    val id: String,
    val authorName: String,
    val authorDistrict: String,
    val text: String,
    val timestamp: String,
    val isExpert: Boolean = false,
    val authorType: UserType = if (isExpert) UserType.EXPERT else UserType.FARMER,
    val isLiked: Boolean = false,
    val likeCount: Int = 0,
    val replies: List<ReplyComment> = emptyList()
)

data class Post(
    val id: String,
    val author: Farmer,
    val text: String,
    val imageRes: Int? = null,
    val imageUri: String? = null,
    val category: String,
    val postType: PostType,
    val customPostType: String? = null,
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
    val isExpert: Boolean = false,
    val authorType: UserType = if (isExpert) UserType.EXPERT else UserType.FARMER
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
    val answers: List<Answer> = emptyList(),
    val mediaType: String? = null, // "VOICE", "PICTURE", "TEXT"
    val mediaUri: String? = null,
    val audioDurationSeconds: Int = 0
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
    val actorName: String,
    val targetPostId: String? = null
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
    val avatarInitials: String = authorName.take(2).uppercase(),
    val imageRes: Int = 0,
    val imageUri: String? = null,
    val caption: String,
    val timeAgo: String,
    val isViewed: Boolean = false,
    val storyType: String = "PHOTO", // "PHOTO", "VIDEO", "TEXT"
    val backgroundColor: Long? = null,
    val viewers: List<String> = emptyList(),
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val comments: List<String> = emptyList()
)
