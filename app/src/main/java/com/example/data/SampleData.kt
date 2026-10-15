package com.example.data

import com.example.R
import com.example.model.*

object SampleData {

    val currentUser = Farmer(
        id = "user_me",
        name = "Rashid Ali",
        username = "rashid_mardan",
        district = "Mardan, Khyber Pakhtunkhwa",
        mainCrops = listOf("Wheat", "Maize", "Sugarcane"),
        experienceYears = 8,
        bio = "Passionate multigenerational farmer from Mardan. Sharing field lessons on grain cultivation and organic soil enriching.",
        isExpert = false,
        isFollowing = false,
        followersCount = 184,
        followingCount = 62,
        postsCount = 14
    )

    val expertDrTariq = Farmer(
        id = "expert_tariq",
        name = "Dr. Tariq Mahmood",
        username = "drtariq_agri",
        district = "Faisalabad, Punjab",
        mainCrops = listOf("Soil Science", "Crop Protection"),
        experienceYears = 16,
        bio = "Agricultural Scientist & Crop Protection Advisor. Helping Pakistani farmers with science-backed solutions.",
        isExpert = true,
        isFollowing = true,
        followersCount = 1420,
        followingCount = 110,
        postsCount = 88
    )

    val farmerHajiSarwar = Farmer(
        id = "farmer_sarwar",
        name = "Haji Ghulam Sarwar",
        username = "sarwar_ryk",
        district = "Rahim Yar Khan, Punjab",
        mainCrops = listOf("Wheat", "Cotton", "Mangoes"),
        experienceYears = 22,
        bio = "Commercial farmer in Southern Punjab. Advocate for water conservation and certified seeds.",
        isExpert = false,
        isFollowing = true,
        followersCount = 540,
        followingCount = 130,
        postsCount = 35
    )

    val farmerBilal = Farmer(
        id = "farmer_bilal",
        name = "Bilal Khan",
        username = "bilal_swat_agro",
        district = "Swat, Khyber Pakhtunkhwa",
        mainCrops = listOf("Maize", "Apples", "Peaches", "Vegetables"),
        experienceYears = 6,
        bio = "High-altitude orchards and hybrid maize farmer. Experimenting with terrace irrigation in Swat Valley.",
        isExpert = false,
        isFollowing = false,
        followersCount = 310,
        followingCount = 89,
        postsCount = 21
    )

    val initialPosts = listOf(
        Post(
            id = "post_1",
            author = currentUser,
            text = "Assalam-o-Alaikum brothers! My wheat crop is showing yellowing and minor powdery rust patches on lower leaves after recent damp morning fog. Has anyone in KP experienced this this week? What fungicide or irrigation adjustment should I check?",
            imageRes = R.drawable.img_wheat_field,
            category = "Wheat",
            postType = PostType.CROP_PROBLEM,
            district = "Mardan",
            timestamp = "2 hours ago",
            likeCount = 34,
            commentCount = 6,
            isLiked = false,
            isSaved = false,
            comments = listOf(
                Comment(
                    id = "c_1",
                    authorName = "Dr. Tariq Mahmood",
                    authorDistrict = "Faisalabad",
                    text = "Walaykum Assalam Rashid Bhai. Look closely at the leaf underside. If you see linear yellow stripe stripes, it's yellow rust. Avoid overhead sprinkling and apply Propiconazole or Tebuconazole if 5% leaves are affected.",
                    timestamp = "1 hour ago",
                    isExpert = true
                ),
                Comment(
                    id = "c_2",
                    authorName = "Haji Ghulam Sarwar",
                    authorDistrict = "Rahim Yar Khan",
                    text = "Ensure you don't over-water with cold canal water right now; excess moisture in cold weather speeds up fungus.",
                    timestamp = "45 mins ago",
                    isExpert = false
                )
            )
        ),
        Post(
            id = "post_2",
            author = farmerBilal,
            text = "Alhamdulillah! My hybrid maize crop has reached peak maturity with uniform cobs and zero borer infestation this season. Timely trichogramma cards and balanced potassium made a huge difference. Sharing blessings with fellow kisaan!",
            imageRes = R.drawable.img_maize_harvest,
            category = "Maize",
            postType = PostType.SUCCESS,
            district = "Swat",
            timestamp = "5 hours ago",
            likeCount = 89,
            commentCount = 11,
            isLiked = true,
            isSaved = true,
            comments = listOf(
                Comment(
                    id = "c_3",
                    authorName = "Rashid Ali",
                    authorDistrict = "Mardan",
                    text = "MashaAllah brother Bilal! What seed spacing did you use on the ridges?",
                    timestamp = "3 hours ago",
                    isExpert = false
                )
            )
        ),
        Post(
            id = "post_3",
            author = expertDrTariq,
            text = "🌾 Farmer Advisory: When selecting phosphatic fertilizers for wheat sowing, compare cost per unit of active P2O5 between DAP, TSP, and SSP based on your soil pH. Alkaline soils (>7.8 pH) perform better when phosphorus is placed near the seed drill rather than broad-cast.",
            imageRes = null,
            category = "Fertilizer Advice",
            postType = PostType.AGRI_NEWS,
            district = "Faisalabad",
            timestamp = "Yesterday",
            likeCount = 145,
            commentCount = 18,
            isLiked = false,
            isSaved = true,
            comments = emptyList()
        ),
        Post(
            id = "post_4",
            author = farmerHajiSarwar,
            text = "Deep plowing and laser leveling underway for wheat sowing in Rahim Yar Khan. Proper leveling has helped us save almost 25% canal water on tubewell turns. Machinery owners, keep your implements serviced!",
            imageRes = R.drawable.img_pakistan_tractor,
            category = "Machinery",
            postType = PostType.GENERAL,
            district = "Rahim Yar Khan",
            timestamp = "1 day ago",
            likeCount = 67,
            commentCount = 4,
            isLiked = false,
            isSaved = false,
            comments = emptyList()
        )
    )

    val initialQuestions = listOf(
        Question(
            id = "q_1",
            author = farmerBilal,
            title = "My tomato plants have spots on lower leaves. What symptoms should I document?",
            description = "Leaf spots are dark brown with faint concentric rings. Stems look healthy so far. Should I treat for early blight, and what biological or chemical control works best without damaging flowering?",
            cropCategory = "Vegetables",
            district = "Swat",
            timestamp = "3 hours ago",
            acceptedAnswerId = "ans_1",
            answers = listOf(
                Answer(
                    id = "ans_1",
                    authorName = "Dr. Tariq Mahmood",
                    authorRole = "Agricultural Scientist",
                    district = "Faisalabad",
                    text = "Concentric rings are classic symptoms of Early Blight (Alternaria solani). 1) Prune bottom leaves touching soil. 2) Apply Mancozeb or Chlorothalonil early morning. 3) Water via furrow or drip, never wet the foliage in evenings.",
                    timestamp = "2 hours ago",
                    upvotes = 24,
                    isAccepted = true,
                    isExpert = true
                ),
                Answer(
                    id = "ans_2",
                    authorName = "Haji Ghulam Sarwar",
                    authorRole = "Experienced Farmer",
                    district = "Rahim Yar Khan",
                    text = "Also check drainage. Water logging in vegetable plots always triggers early blight in humid weather.",
                    timestamp = "1 hour ago",
                    upvotes = 8,
                    isAccepted = false,
                    isExpert = false
                )
            )
        ),
        Question(
            id = "q_2",
            author = currentUser,
            title = "Recommended seed rate for Akbar-2019 wheat variety under irrigated conditions?",
            description = "We are planning sowing in mid-November in Mardan canal command area. What is the optimal seed rate in kg per acre, and do you recommend seed dressing fungicide?",
            cropCategory = "Wheat",
            district = "Mardan",
            timestamp = "Yesterday",
            acceptedAnswerId = null,
            answers = listOf(
                Answer(
                    id = "ans_3",
                    authorName = "Haji Ghulam Sarwar",
                    authorRole = "Wheat Grower",
                    district = "Rahim Yar Khan",
                    text = "For Akbar-2019 between Nov 10 to Nov 25, 45 to 50 kg per acre is optimal. If delayed past Dec 1st, increase seed rate to 55-60 kg. Never sow without treating seed with Thiram or Imidacloprid combo.",
                    timestamp = "18 hours ago",
                    upvotes = 15,
                    isAccepted = false,
                    isExpert = false
                )
            )
        ),
        Question(
            id = "q_3",
            author = farmerHajiSarwar,
            title = "How to manage canal water deficit during sugarcane tillering phase?",
            description = "Our distributary canal has a 14-day rotation closure. What mulch or deficit irrigation strategy preserves cane tillers best in hot sandy loam?",
            cropCategory = "Sugarcane",
            district = "Rahim Yar Khan",
            timestamp = "2 days ago",
            acceptedAnswerId = null,
            answers = emptyList()
        )
    )

    val initialListings = listOf(
        MarketplaceListing(
            id = "list_1",
            title = "Certified Akbar-2019 Wheat Seed (PSC Certified Bags)",
            category = "Seeds",
            price = 4800,
            unit = "50 kg bag",
            sellerName = "Haji Ghulam Sarwar",
            sellerDistrict = "Rahim Yar Khan",
            sellerPhone = "+92 300 7891234",
            description = "Cleaned, graded, and certified germinated wheat seed (98% germination rate). Treated with seed fungicide. Delivery available via cargo to Multan and Bahawalpur.",
            imageRes = R.drawable.img_wheat_field,
            timestamp = "1 day ago",
            isVerifiedSeller = true,
            availableQuantity = "120 Bags"
        ),
        MarketplaceListing(
            id = "list_2",
            title = "Massey Ferguson 385 Tractor Rental (with Laser Leveler)",
            category = "Machinery",
            price = 2500,
            unit = "hour",
            sellerName = "Engr. Farooq Ahmad",
            sellerDistrict = "Sahiwal",
            sellerPhone = "+92 321 4567890",
            description = "85 HP powerful tractor with precision computerized laser leveler and experienced operator. Suitable for paddy and wheat land preparation within 25 km of Sahiwal.",
            imageRes = R.drawable.img_pakistan_tractor,
            timestamp = "3 hours ago",
            isVerifiedSeller = true,
            availableQuantity = "2 Units available"
        ),
        MarketplaceListing(
            id = "list_3",
            title = "Pioneer 30Y87 Hybrid Maize Seed (High Yield)",
            category = "Seeds",
            price = 9500,
            unit = "bag",
            sellerName = "Bilal Khan",
            sellerDistrict = "Swat",
            sellerPhone = "+92 333 1122334",
            description = "Original sealed Pioneer company packing. Exceptional disease resistance and cob filling. Stored in temperature-controlled room.",
            imageRes = R.drawable.img_maize_harvest,
            timestamp = "Yesterday",
            isVerifiedSeller = true,
            availableQuantity = "35 Bags"
        ),
        MarketplaceListing(
            id = "list_4",
            title = "Imported Water Soluble NPK 20-20-20 (Drip & Foliar)",
            category = "Fertilizers",
            price = 7200,
            unit = "25 kg sack",
            sellerName = "Zafar Iqbal (Agro Care)",
            sellerDistrict = "Multan",
            sellerPhone = "+92 301 9876543",
            description = "100% water soluble balanced macro nutrients with chelated micro nutrients (Zinc, Boron, Iron). Ideal for vegetables and citrus orchards.",
            imageRes = null,
            timestamp = "2 days ago",
            isVerifiedSeller = true,
            availableQuantity = "80 Sacks"
        )
    )

    val initialNotifications = listOf(
        NotificationItem(
            id = "notif_1",
            type = NotificationType.REPLY,
            title = "Expert Advice on Your Post",
            message = "Dr. Tariq Mahmood replied with recommended fungicide dosage for your wheat crop issue.",
            timestamp = "1 hour ago",
            isRead = false,
            actorName = "Dr. Tariq Mahmood"
        ),
        NotificationItem(
            id = "notif_2",
            type = NotificationType.LIKE,
            title = "Post Liked",
            message = "Bilal Khan and 14 other farmers appreciated your farm update.",
            timestamp = "3 hours ago",
            isRead = false,
            actorName = "Bilal Khan"
        ),
        NotificationItem(
            id = "notif_3",
            type = NotificationType.FOLLOW,
            title = "New Follower",
            message = "Haji Ghulam Sarwar started following your farming journey.",
            timestamp = "Yesterday",
            isRead = true,
            actorName = "Haji Ghulam Sarwar"
        ),
        NotificationItem(
            id = "notif_4",
            type = NotificationType.ADVISORY,
            title = "Weather & Mandi Advisory",
            message = "Morning fog expected across Mardan and Peshawar valley. Inspect early wheat nursery for damp damping-off.",
            timestamp = "2 days ago",
            isRead = true,
            actorName = "Kisaan Book Advisory"
        )
    )

    val mandiPrices = listOf(
        MandiPrice(
            cropName = "Wheat (New Crop)",
            cropNameUrdu = "گندم (نئی فصل)",
            mandiLocation = "Faisalabad Ghalla Mandi",
            priceMin = 3900,
            priceMax = 4150,
            unit = "40 kg (Maund)",
            dateUpdated = "Updated Today 08:30 AM"
        ),
        MandiPrice(
            cropName = "Basmati Rice (Super Kernal)",
            cropNameUrdu = "سپر کرنل باسمتی چاول",
            mandiLocation = "Gujranwala Rice Market",
            priceMin = 4800,
            priceMax = 5350,
            unit = "40 kg (Maund)",
            dateUpdated = "Updated Today 09:15 AM"
        ),
        MandiPrice(
            cropName = "Maize (Hybrid Grain)",
            cropNameUrdu = "مکئی (ہائبرڈ دانہ)",
            mandiLocation = "Mardan Mandi",
            priceMin = 2650,
            priceMax = 2850,
            unit = "40 kg (Maund)",
            dateUpdated = "Updated Today 07:45 AM"
        ),
        MandiPrice(
            cropName = "Cotton / Phutti",
            cropNameUrdu = "کپاس پھٹی",
            mandiLocation = "Multan Mandi",
            priceMin = 7400,
            priceMax = 8200,
            unit = "40 kg (Maund)",
            dateUpdated = "Updated Yesterday"
        ),
        MandiPrice(
            cropName = "Sugarcane Support Rate",
            cropNameUrdu = "کماد سرکاری نرخ",
            mandiLocation = "Rahim Yar Khan Mills",
            priceMin = 425,
            priceMax = 450,
            unit = "40 kg (Maund)",
            dateUpdated = "Current Season Rate"
        )
    )

    val cropCategories = listOf(
        "All Crops",
        "Wheat",
        "Maize",
        "Rice",
        "Cotton",
        "Sugarcane",
        "Vegetables",
        "Fruits",
        "Livestock",
        "Machinery",
        "Fertilizers"
    )

    val pakistaniDistricts = listOf(
        "Mardan",
        "Peshawar",
        "Swat",
        "Charsadda",
        "Faisalabad",
        "Multan",
        "Lahore",
        "Gujranwala",
        "Sahiwal",
        "Sargodha",
        "Rahim Yar Khan",
        "Bahawalpur",
        "Hyderabad",
        "Sukkur",
        "Quetta"
    )

    val articles = listOf(
        AgriArticle(
            id = "art_1",
            title = "Managing Yellow Rust in Wheat During Humid Morning Fog",
            titleUrdu = "گندم میں زرد کنگی کا سائنسی تدارک اور احتیاطی تدابیر",
            category = "Pest & Disease",
            summary = "Recognize early leaf stripe symptoms and understand when chemical spray is economically beneficial.",
            content = "Yellow rust (Puccinia striiformis) thrives in temperatures between 10°C and 15°C accompanied by dew or high humidity. Farmers should regularly inspect the field on sunny afternoons. Look for bright yellow, powdery pustules aligned linearly along leaf veins. If more than 5% of flag leaves show symptoms, apply approved triazole fungicides (such as Propiconazole 25% EC at 200 ml/acre or Tebuconazole) using a hollow-cone nozzle. Ensure proper water volume (100-120 liters of clean water per acre).",
            source = "Punjab & KP Agriculture Extension Guidelines",
            readTime = "3 min read"
        ),
        AgriArticle(
            id = "art_2",
            title = "Balanced NPK Fertilization: Maximizing Grain Weight",
            titleUrdu = "متوازن کھادوں کا استعمال: نائٹروجن، فاسفورس اور پوٹاش کا تناسب",
            category = "Soil & Fertilizer",
            summary = "Why relying solely on Urea without Potassium limits tillering and root development.",
            content = "Excessive nitrogen (Urea) produces weak, lodging-prone stems and makes plants succulent and attractive to aphids and rust. Phosphorus (DAP/TSP) must be placed deep at sowing time for vigorous root anchoring. Potassium (SOP/MOP) should not be skipped, as it regulates stomatal water loss and directly fills grain starch during milking stage.",
            source = "National Agricultural Research Centre (NARC)",
            readTime = "4 min read"
        ),
        AgriArticle(
            id = "art_3",
            title = "Laser Land Leveling: Saving 25% Canal Water",
            titleUrdu = "لیزر لینڈ لیولنگ: پانی اور ڈیزل کی ۲۵ فیصد بچت",
            category = "Machinery & Water",
            summary = "How flat seedbeds prevent water-logging in furrows and ensure uniform germination.",
            content = "Unlevel fields suffer from water ponding in depressions and drought on high ridges. Laser leveling using scraper attachments ensures uniform water sheet propagation, cutting irrigation run time by 20-30%, reducing weed infestation by 40%, and increasing overall fertilizer efficiency.",
            source = "On-Farm Water Management Directorate",
            readTime = "3 min read"
        )
    )

    val initialStories = listOf(
        FarmerStory(
            id = "story_1",
            authorName = "Dr. Tariq",
            authorDistrict = "Faisalabad",
            avatarInitials = "TM",
            imageRes = R.drawable.img_wheat_field,
            caption = "Wheat Rust Scouting: Check flag leaves early morning for yellow powder pustules.",
            timeAgo = "1h ago"
        ),
        FarmerStory(
            id = "story_2",
            authorName = "Bilal Khan",
            authorDistrict = "Swat",
            avatarInitials = "BK",
            imageRes = R.drawable.img_maize_harvest,
            caption = "Alhamdulillah, 98 maunds per acre harvested from Pioneer 30Y87 hybrid plot!",
            timeAgo = "3h ago"
        ),
        FarmerStory(
            id = "story_3",
            authorName = "Haji Sarwar",
            authorDistrict = "Rahim Yar Khan",
            avatarInitials = "GS",
            imageRes = R.drawable.img_pakistan_tractor,
            caption = "Laser leveling completed for 25 acres. Saving tubewell diesel and irrigation turns.",
            timeAgo = "5h ago"
        ),
        FarmerStory(
            id = "story_4",
            authorName = "Agro Multan",
            authorDistrict = "Multan",
            avatarInitials = "AM",
            imageRes = R.drawable.img_wheat_field,
            caption = "New batch of certified imported DAP and zinc arrived at Multan Mandi gate.",
            timeAgo = "7h ago"
        )
    )
}
