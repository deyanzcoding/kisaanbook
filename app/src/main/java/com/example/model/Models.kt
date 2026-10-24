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

/**
 * Community Post: supports Text, Voice audio recording, Picture, or all at once!
 */
data class CommunityPost(
    val id: String,
    val author: Farmer,
    val text: String = "",
    val voiceUri: String? = null,
    val voiceDurationSeconds: Int = 0,
    val imageRes: Int? = null,
    val imageUri: String? = null,
    val category: String = "General",
    val district: String,
    val timestamp: String,
    val likeCount: Int = 0,
    val isLiked: Boolean = false,
    val comments: List<Comment> = emptyList()
) {
    val hasText: Boolean get() = text.isNotBlank()
    val hasVoice: Boolean get() = !voiceUri.isNullOrBlank() || voiceDurationSeconds > 0
    val hasPicture: Boolean get() = imageRes != null || !imageUri.isNullOrBlank()
}

/**
 * Tractor & Machinery specific details for marketplace
 */
data class MachineryDetails(
    val makeModelYear: String,
    val horsepower: String,
    val condition: String // "New", "Used - Excellent", "Used - Fair", "Reconditioned"
)

data class MarketplaceListing(
    val id: String,
    val title: String,
    val category: String, // "Tractor & Machinery", "Seeds", "Fertilizers", "Pesticides & Sprays", "Crops & Produce", "Livestock"
    val price: Long,
    val unit: String,
    val sellerName: String,
    val sellerDistrict: String,
    val sellerPhone: String,
    val description: String,
    val imageRes: Int? = null,
    val timestamp: String,
    val isVerifiedSeller: Boolean = true,
    val availableQuantity: String? = null,
    val sellerBio: String = "Authorized Agricultural Input Dealer & Farmer Partner.",
    val machineryDetails: MachineryDetails? = null
)

// ========== FARM CROP PLANNER & CROPS MANAGEMENT ==========

data class NutrientItem(
    val name: String,
    val nameUrdu: String,
    val stage: String,
    val dosage: String,
    val purpose: String,
    val recommendedDealers: List<String> = emptyList()
)

data class MedicineItem(
    val diseaseOrPest: String,
    val diseaseOrPestUrdu: String,
    val sprayName: String,
    val timing: String,
    val dosage: String,
    val recommendedDealers: List<String> = emptyList()
)

data class FarmCrop(
    val id: String,
    val cropName: String,
    val cropNameUrdu: String,
    val variety: String,
    val sowingDate: String,
    val areaAcres: Double,
    val soilType: String,
    val district: String,
    val province: String,
    val currentStage: String,
    val currentStageUrdu: String,
    val durationDays: Int,
    val daysElapsed: Int,
    val weatherTips: List<String>,
    val nutrientsSchedule: List<NutrientItem>,
    val medicineSchedule: List<MedicineItem>,
    val estimatedYield: String = "40–45 Maunds/Acre"
) {
    val progressPercent: Float
        get() = (daysElapsed.toFloat() / durationDays.toFloat()).coerceIn(0f, 1f)
    val remainingDays: Int
        get() = maxOf(0, durationDays - daysElapsed)
}

// ========== ZAMEEN-DARI KHATA (FARM EXPENSE & INCOME LEDGER) ==========

enum class KhataType(val labelEn: String, val labelUr: String) {
    EXPENSE("Expense", "خرچہ"),
    INCOME("Income", "آمدن")
}

data class KhataEntry(
    val id: String,
    val cropId: String? = null,
    val cropName: String,
    val type: KhataType,
    val category: String, // "Seed", "Fertilizer", "Land Prep / Tractor", "Spray / Medicine", "Tube Well / Water", "Labor / Harvest", "Harvest Crop Sale"
    val amount: Long,
    val description: String,
    val date: String
)

// ========== AI PLANT PROBLEM DIAGNOSIS ==========

data class PlantDiagnosis(
    val problemName: String,
    val problemNameUrdu: String,
    val severity: String, // "Mild", "Moderate", "Critical"
    val symptoms: List<String>,
    val recommendedTreatment: String,
    val recommendedSprays: List<String>,
    val preventiveTips: List<String>,
    val recommendedDealers: List<String> = emptyList(),
    val sampleImageRes: Int = 0
)

// ========== CALENDAR SCREEN MODELS ==========

enum class CalendarType(val title: String) {
    GREGORIAN("English"),
    HIJRI("Arabic / Islamic"),
    PASHTUN("Pashtun / Desi")
}

enum class Province(val displayName: String) {
    PUNJAB("Punjab"),
    SINDH("Sindh"),
    KP("Khyber Pakhtunkhwa"),
    BALOCHISTAN("Balochistan"),
    ISLAMABAD("Islamabad / North"),
    UNKNOWN("Pakistan (General)")
}

data class MonthInfo(
    val id: Int,
    val gregorianName: String,
    val pashtoName: String,
    val pashtoScript: String,
    val hijriName: String,
    val baseCrops: List<String>
)

enum class NotificationType {
    LIKE, COMMENT, REPLY, FOLLOW, ADVISORY, SHARE, MARKET_INQUIRY
}

data class NotificationItem(
    val id: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean = false,
    val actorName: String,
    val targetPostId: String? = null,
    val category: String = "COMMUNITY" // "COMMUNITY", "MARKET", "SYSTEM"
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
