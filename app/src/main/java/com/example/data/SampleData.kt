package com.example.data

import com.example.R
import com.example.model.*

object SampleData {

    // Current logged-in user
    val currentUser = Farmer(
        id = "farmer_khursheed",
        name = "Khursheed Ahmad",
        username = "khursheed_mardan",
        district = "Mardan, Khyber Pakhtunkhwa",
        mainCrops = listOf("Wheat", "Maize", "Sugarcane"),
        experienceYears = 12,
        bio = "مردان سے گندم اور کماد کے پرانے کاشتکار۔ زمین کی قدرتی زرخیزی اور بہتر فصل کے طریقے اپنانے کے حامی۔",
        isExpert = false,
        userType = UserType.FARMER,
        isFollowing = false,
        followersCount = 384,
        followingCount = 92,
        postsCount = 18
    )

    // Requested Farmers
    val farmerHussainShah = Farmer(
        id = "farmer_hussain",
        name = "Hussain Shah",
        username = "hussain_swat",
        district = "Swat, Khyber Pakhtunkhwa",
        mainCrops = listOf("Maize", "Apples", "Peaches"),
        experienceYears = 10,
        bio = "سوات وادی کے باغات اور ہائبرڈ مکئی کے کسان۔ آبپاشی کے جدید طریقوں کے تجربات۔",
        isExpert = false,
        userType = UserType.FARMER,
        isFollowing = true,
        followersCount = 520,
        followingCount = 110,
        postsCount = 24
    )

    val farmerHestaRahman = Farmer(
        id = "farmer_hesta",
        name = "Hesta Rahman",
        username = "hesta_charsadda",
        district = "Charsadda, Khyber Pakhtunkhwa",
        mainCrops = listOf("Sugarcane", "Wheat", "Vegetables"),
        experienceYears = 8,
        bio = "چارسدہ میں کماد اور گڑ کی خالص تیاری۔ زمین کی تیاری میں دیسی کھاد کا باقاعدہ استعمال۔",
        isExpert = false,
        userType = UserType.FARMER,
        isFollowing = true,
        followersCount = 290,
        followingCount = 74,
        postsCount = 15
    )

    val farmerRasoolKhan = Farmer(
        id = "farmer_rasool",
        name = "Rasool Khan",
        username = "rasool_swabi",
        district = "Swabi, Khyber Pakhtunkhwa",
        mainCrops = listOf("Tobacco", "Wheat", "Maize"),
        experienceYears = 15,
        bio = "صوابی سے تمباکو اور گندم کے کامیاب کاشتکار۔ زرعی رہنمائی اور محنت پر یقین۔",
        isExpert = false,
        userType = UserType.FARMER,
        isFollowing = false,
        followersCount = 410,
        followingCount = 88,
        postsCount = 29
    )

    val farmerAmirBadshah = Farmer(
        id = "farmer_amir",
        name = "Amir Badshah",
        username = "amir_peshawar",
        district = "Peshawar, Khyber Pakhtunkhwa",
        mainCrops = listOf("Vegetables", "Wheat"),
        experienceYears = 7,
        bio = "پشاور میں سبزیوں کی پنیری اور گندم کی جدید کاشت۔",
        isExpert = false,
        userType = UserType.FARMER,
        isFollowing = false,
        followersCount = 180,
        followingCount = 45,
        postsCount = 9
    )

    val farmerJahangirKhan = Farmer(
        id = "farmer_jahangir",
        name = "Jahangir Khan",
        username = "jahangir_nowshera",
        district = "Nowshera, Khyber Pakhtunkhwa",
        mainCrops = listOf("Citrus", "Wheat", "Maize"),
        experienceYears = 14,
        bio = "نوشہرہ کے باغات اور اجناس کے کاشتکار۔ پانی کی کمی کے تدراک کے طریقے پر کام۔",
        isExpert = false,
        userType = UserType.FARMER,
        isFollowing = false,
        followersCount = 330,
        followingCount = 65,
        postsCount = 17
    )

    // Requested Zari-Mahir (Agricultural Experts)
    val expertAhmadAli = Farmer(
        id = "expert_ahmad",
        name = "Ahmad Ali",
        username = "ahmad_agri_expert",
        district = "Faisalabad, Punjab",
        mainCrops = listOf("Agronomy", "Crop Protection"),
        experienceYears = 18,
        bio = "زرعی یونیورسٹی فیصل آباد کے محقق اور شعبہ فصلات کے مشیر۔ کسان بھائیوں کو سائنسی بنیادوں پر مفت مشورے۔",
        isExpert = true,
        userType = UserType.EXPERT,
        isFollowing = true,
        followersCount = 1680,
        followingCount = 120,
        postsCount = 95
    )

    val expertShafiqHussain = Farmer(
        id = "expert_shafiq",
        name = "Shafiq Hussain",
        username = "shafiq_plant_doc",
        district = "Multan, Punjab",
        mainCrops = listOf("Cotton", "Mangoes", "Soil Science"),
        experienceYears = 16,
        bio = "پودوں کی بیماریوں اور کیڑوں کے سدباب کے اسپیشلسٹ۔ ملتان و جنوبی پنجاب کے زرعی ماہر۔",
        isExpert = true,
        userType = UserType.EXPERT,
        isFollowing = true,
        followersCount = 1420,
        followingCount = 95,
        postsCount = 82
    )

    val expertFarmanKhan = Farmer(
        id = "expert_farman",
        name = "Farman Khan",
        username = "farman_agri_kp",
        district = "Peshawar, Khyber Pakhtunkhwa",
        mainCrops = listOf("Horticulture", "Wheat Research"),
        experienceYears = 12,
        bio = "خیبر پختونخوا میں گندم کی اقسام اور باغبانی کے سینئر زرعی افسر۔",
        isExpert = true,
        userType = UserType.EXPERT,
        isFollowing = false,
        followersCount = 980,
        followingCount = 80,
        postsCount = 64
    )

    val expertMubasirKhan = Farmer(
        id = "expert_mubasir",
        name = "Mubasir Khan",
        username = "mubasir_soil_lab",
        district = "Lahore, Punjab",
        mainCrops = listOf("Soil Fertility", "Fertilizer Management"),
        experienceYears = 14,
        bio = "زمین کے نمونوں کا تجزیہ اور متوازن کھادوں کے استعمال کے مشیر۔",
        isExpert = true,
        userType = UserType.EXPERT,
        isFollowing = false,
        followersCount = 1120,
        followingCount = 105,
        postsCount = 71
    )

    val expertZahidHussain = Farmer(
        id = "expert_zahid",
        name = "Zahid Hussain",
        username = "zahid_water_mgt",
        district = "Rawalpindi, Punjab",
        mainCrops = listOf("Drip Irrigation", "Barani Agriculture"),
        experienceYears = 15,
        bio = "بارانی علاقوں میں پانی کا بچاؤ اور ڈرپ اریگیشن کے ماہر انجینئر۔",
        isExpert = true,
        userType = UserType.EXPERT,
        isFollowing = false,
        followersCount = 890,
        followingCount = 76,
        postsCount = 53
    )

    // Requested Dokandar (Dealers / Agri Input Suppliers)
    val dealerHasnainRaza = Farmer(
        id = "dealer_hasnain",
        name = "Hasnain Raza",
        username = "hasnain_agri_center",
        district = "Gujranwala, Punjab",
        mainCrops = listOf("Seeds", "Fertilizers", "Pesticides"),
        experienceYears = 11,
        bio = "رضا زرعی سروسز گوجرانوالہ۔ تمام کمپنیوں کے تصدیق شدہ بیج اور اعلیٰ معیار کی کھادیں ہول سیل ریٹ پر دستیاب ہیں۔",
        isExpert = false,
        userType = UserType.DEALER,
        isFollowing = false,
        followersCount = 640,
        followingCount = 55,
        postsCount = 42
    )

    val dealerSajidAli = Farmer(
        id = "dealer_sajid",
        name = "Sajid Ali",
        username = "sajid_kisan_dokan",
        district = "Sargodha, Punjab",
        mainCrops = listOf("Citrus Spray", "Fertilizers"),
        experienceYears = 9,
        bio = "سرگودھا کسان مارکیٹ۔ کنو کے باغات کے خصوصی سپرے اور این پی کے کھادیں۔",
        isExpert = false,
        userType = UserType.DEALER,
        isFollowing = false,
        followersCount = 480,
        followingCount = 40,
        postsCount = 33
    )

    val dealerShafiqAhmad = Farmer(
        id = "dealer_shafiq",
        name = "Shafiq Ahmad",
        username = "shafiq_seed_store",
        district = "Mardan, Khyber Pakhtunkhwa",
        mainCrops = listOf("Certified Seed", "Spray Pumps"),
        experienceYears = 13,
        bio = "مردان بازار بیج کارپوریشن۔ گندم، مکئی اور سبزیوں کے منظور شدہ بیج۔",
        isExpert = false,
        userType = UserType.DEALER,
        isFollowing = false,
        followersCount = 510,
        followingCount = 62,
        postsCount = 38
    )

    val dealerGulzarAhmad = Farmer(
        id = "dealer_gulzar",
        name = "Gulzar Ahmad",
        username = "gulzar_chemicals",
        district = "Sahiwal, Punjab",
        mainCrops = listOf("Fungicides", "Weedicides"),
        experienceYears = 15,
        bio = "گلزار زرعی کیمیکلز ساہیوال۔ جڑی بوٹی مار اور پھپھوندی کش مستند ادویات۔",
        isExpert = false,
        userType = UserType.DEALER,
        isFollowing = false,
        followersCount = 590,
        followingCount = 48,
        postsCount = 45
    )

    val dealerImamAhmad = Farmer(
        id = "dealer_imam",
        name = "Imam Ahmad",
        username = "imam_agri_bahawalpur",
        district = "Bahawalpur, Punjab",
        mainCrops = listOf("Cotton Inputs", "Foliar Sprays"),
        experienceYears = 10,
        bio = "امام زرعی مرکز بہاولپور۔ کپاس اور سرسوں کی نگہداشت کی ادویات۔",
        isExpert = false,
        userType = UserType.DEALER,
        isFollowing = false,
        followersCount = 430,
        followingCount = 37,
        postsCount = 26
    )

    val dealerBashirAhmad = Farmer(
        id = "dealer_bashir",
        name = "Bashir Ahmad",
        username = "bashir_ryk_traders",
        district = "Rahim Yar Khan, Punjab",
        mainCrops = listOf("Urea", "DAP", "Potash"),
        experienceYears = 20,
        bio = "بشیر ٹریڈرز رحیم یار خان۔ سرکاری نرخوں پر کھاد کی ترسیل۔",
        isExpert = false,
        userType = UserType.DEALER,
        isFollowing = false,
        followersCount = 720,
        followingCount = 50,
        postsCount = 49
    )

    val dealerTariqAji = Farmer(
        id = "dealer_tariq",
        name = "Tariq Aji",
        username = "tariq_machinery",
        district = "Sheikhupura, Punjab",
        mainCrops = listOf("Rotavator", "Seed Drills", "Laser Leveler"),
        experienceYears = 14,
        bio = "طارق زرعی آلات شیخوپورہ۔ روٹاویٹر، بیج ڈرل اور لیزر لیولر کرایہ اور فروخت کے لیے۔",
        isExpert = false,
        userType = UserType.DEALER,
        isFollowing = false,
        followersCount = 610,
        followingCount = 44,
        postsCount = 37
    )

    val dealerMuhammadAlam = Farmer(
        id = "dealer_alam",
        name = "Muhammad Alam",
        username = "alam_sukkur_traders",
        district = "Sukkur, Sindh",
        mainCrops = listOf("Rice Seeds", "Zinc", "Pesticides"),
        experienceYears = 16,
        bio = "عالم زرعی کارپوریشن سکھر۔ سندھ کے کسانوں کے لیے چاول کے بیج اور زنک۔",
        isExpert = false,
        userType = UserType.DEALER,
        isFollowing = false,
        followersCount = 540,
        followingCount = 39,
        postsCount = 31
    )

    val allFarmersList: List<Farmer> = listOf(
        currentUser,
        farmerHussainShah,
        farmerHestaRahman,
        farmerRasoolKhan,
        farmerAmirBadshah,
        farmerJahangirKhan,
        expertAhmadAli,
        expertShafiqHussain,
        expertFarmanKhan,
        expertMubasirKhan,
        expertZahidHussain,
        dealerHasnainRaza,
        dealerSajidAli,
        dealerShafiqAhmad,
        dealerGulzarAhmad,
        dealerImamAhmad,
        dealerBashirAhmad,
        dealerTariqAji,
        dealerMuhammadAlam
    )

    // Demo Posts with STRICT Urdu descriptions
    val initialPosts = listOf(
        Post(
            id = "post_1",
            author = currentUser,
            text = "السلام علیکم کسان بھائیو! حالیہ صبح کی شدید دھند اور نم موسم کے باعث میری گندم کی فصل کے نچلے پتوں پر زردی اور ہلکے سفوفی دھبے نظر آ رہے ہیں۔ کیا خیبر پختونخوا کے دیگر علاقوں میں بھی یہ مسئلہ درپیش ہے؟ بروقت بچاؤ کے لیے کون سا فنجی سائیڈ سپرے یا پانی کا شیڈول مفید رہے گا؟ رہنمائی فرمائیں۔",
            imageRes = R.drawable.img_wheat_field,
            category = "Wheat",
            postType = PostType.CROP_PROBLEM,
            district = "Mardan",
            timestamp = "2h",
            likeCount = 42,
            commentCount = 5,
            isLiked = false,
            isSaved = false,
            comments = listOf(
                Comment(
                    id = "c_1",
                    authorName = "Ahmad Ali",
                    authorDistrict = "Faisalabad",
                    authorType = UserType.EXPERT,
                    text = "وعلیکم السلام خورشید بھائی! پتے کے نچلے حصے پر اگر لمبائی کے رخ زرد لکیریں ہیں تو یہ زرد زنگ (Yellow Rust) ہو سکتا ہے۔ فوری طور پر نائٹروجنی کھاد کا زیادہ استعمال روکیں اور محکمہ زراعت سے تصدیق شدہ فنجی سائیڈ سپرے کریں۔",
                    timestamp = "1h",
                    isExpert = true,
                    isLiked = true,
                    likeCount = 12,
                    replies = listOf(
                        ReplyComment(
                            id = "r_1_1",
                            authorName = "Khursheed Ahmad",
                            authorDistrict = "Mardan",
                            authorType = UserType.FARMER,
                            text = "بہت شکریہ احمد علی صاحب! کیا سپرے کے بعد فوری پانی لگانا مناسب ہوگا یا دو دن کا وقفہ دینا چاہیے؟",
                            timestamp = "45m",
                            isLiked = false,
                            likeCount = 3
                        ),
                        ReplyComment(
                            id = "r_1_2",
                            authorName = "Ahmad Ali",
                            authorDistrict = "Faisalabad",
                            authorType = UserType.EXPERT,
                            text = "سپرے کے کم از کم 48 گھنٹے بعد ہلکا پانی دیں تاکہ دوا پتوں پر اچھی طرح اثر کر سکے اور دھوپ میں جذب ہو جائے۔",
                            timestamp = "30m",
                            isLiked = true,
                            likeCount = 8
                        )
                    )
                ),
                Comment(
                    id = "c_2",
                    authorName = "Shafiq Ahmad",
                    authorDistrict = "Mardan",
                    authorType = UserType.DEALER,
                    text = "مردان کچہری روڈ پر ہماری دکان پر این پی کے اور فنجی سائیڈ کا فریش اسٹاک موجود ہے، اگر ضرورت ہو تو وزٹ فرمائیں۔",
                    timestamp = "35m",
                    isExpert = false,
                    isLiked = false,
                    likeCount = 2,
                    replies = emptyList()
                )
            )
        ),
        Post(
            id = "post_2",
            author = farmerHussainShah,
            text = "الحمد للہ! اس سیزن میں ہائبرڈ مکئی کی فصل بہترین تیار ہوئی ہے۔ ٹرائیکوگراما کارڈز اور زنک کے بروقت استعمال سے سُنڈی کا حملہ صفر رہا۔ تمام کسان بھائیوں کے ساتھ خوشی اور پیداوار کا تجربہ شیئر کر رہا ہوں۔ محنت کبھی رائیگاں نہیں جاتی۔",
            imageRes = R.drawable.img_maize_harvest,
            category = "Maize",
            postType = PostType.SUCCESS,
            district = "Swat",
            timestamp = "4h",
            likeCount = 98,
            commentCount = 6,
            isLiked = true,
            isSaved = true,
            comments = listOf(
                Comment(
                    id = "c_3",
                    authorName = "Khursheed Ahmad",
                    authorDistrict = "Mardan",
                    authorType = UserType.FARMER,
                    text = "ماشاء اللہ حسین شاہ بھائی! پودوں کے درمیان آپ نے کیا فاصلہ رکھا تھا؟",
                    timestamp = "3h",
                    isLiked = true,
                    likeCount = 5,
                    replies = listOf(
                        ReplyComment(
                            id = "r_3_1",
                            authorName = "Hussain Shah",
                            authorDistrict = "Swat",
                            authorType = UserType.FARMER,
                            text = "خورشید بھائی، ہم نے پٹڑیوں پر 8 انچ کا فاصلہ رکھا اور قطاروں میں ڈھائی فٹ رکھا تھا۔",
                            timestamp = "2h",
                            isLiked = true,
                            likeCount = 4
                        )
                    )
                )
            )
        ),
        Post(
            id = "post_3",
            author = expertShafiqHussain,
            text = "تمام کاشتکار بھائیوں کے لیے اہم زرعی الرٹ: گندم میں جڑی بوٹیوں کی تلفی کے لیے سپرے ہمیشہ وتر حالت میں اور دھوپ نکلنے پر کریں۔ چوڑے اور نوکیلے پتوں والی جڑی بوٹیوں کے لیے تجویز کردہ زہریں ہی استعمال کریں۔ غلط وقت پر سپرے فصل کو پیلا کر دیتا ہے۔",
            imageRes = null,
            category = "Wheat",
            postType = PostType.AGRI_NEWS,
            district = "Multan",
            timestamp = "6h",
            likeCount = 135,
            commentCount = 8,
            isLiked = false,
            isSaved = false,
            comments = emptyList()
        ),
        Post(
            id = "post_4",
            author = dealerHasnainRaza,
            text = "گوجرانوالہ اور گردونواح کے کسانوں کے لیے خوشخبری: این ایف سی اور سونا یوریا کھاد کے سرکاری کوٹے کے نئے ٹرک اسٹاک میں پہنچ چکے ہیں۔ منصفانہ تقسیم کے لیے کسان اپنا کسان کارڈ یا شناختی کارڈ دکھا کر کوٹہ حاصل کر سکتے ہیں۔",
            imageRes = null,
            category = "Fertilizer",
            postType = PostType.LISTING,
            district = "Gujranwala",
            timestamp = "7h",
            likeCount = 56,
            commentCount = 3,
            isLiked = false,
            isSaved = false,
            comments = emptyList()
        ),
        Post(
            id = "post_5",
            author = farmerHestaRahman,
            text = "چارسدہ میں کماد (گنّے) کی ترائی اور مٹی چڑھانے کا کام شروع ہے۔ زمین کو نامیاتی مادوں اور گوبر کی کھاد سے زرخیز کرنے کا بہت فائدہ ہوا ہے۔ مٹی نرم رہنے سے گڑ کی مٹھاس اور گٹھے کا وزن دونوں بہتر ہوتے ہیں۔",
            imageRes = null,
            category = "Sugarcane",
            postType = PostType.GENERAL,
            district = "Charsadda",
            timestamp = "9h",
            likeCount = 74,
            commentCount = 4,
            isLiked = false,
            isSaved = false,
            comments = emptyList()
        ),
        Post(
            id = "post_6",
            author = expertZahidHussain,
            text = "بارانی علاقوں میں ڈرپ اریگیشن کے کامیاب تجربات سے یہ بات واضح ہوئی ہے کہ روایتی کھالے کے مقابلے میں 40 فیصد پانی کی بچت ہوتی ہے۔ باغات اور سبزیوں میں زمین نم رہتی ہے اور جڑی بوٹیاں کم اگتی ہیں۔ کسان جدید آبپاشی پر سبسڈی سے فائدہ اٹھائیں۔",
            imageRes = null,
            category = "Water Management",
            postType = PostType.AGRI_NEWS,
            district = "Rawalpindi",
            timestamp = "12h",
            likeCount = 112,
            commentCount = 9,
            isLiked = true,
            isSaved = true,
            comments = emptyList()
        )
    )

    // Demo Stories
    val initialStories = listOf(
        FarmerStory(
            id = "story_1",
            authorName = "Hussain Shah",
            authorDistrict = "Swat",
            avatarInitials = "HS",
            imageRes = R.drawable.img_maize_harvest,
            caption = "سوات وادی میں مکئی کی کٹائی کا شاندار منظر! تمام کسان بھائیوں کو مبارک ہو۔",
            timeAgo = "1h",
            isViewed = false,
            storyType = "PHOTO",
            viewers = listOf("Khursheed Ahmad", "Ahmad Ali", "Shafiq Hussain", "Farman Khan", "Hasnain Raza"),
            likesCount = 28,
            isLiked = true,
            comments = listOf("ماشاء اللہ زبردست پیداوار!", "بہت مبارک ہو بھائی!")
        ),
        FarmerStory(
            id = "story_2",
            authorName = "Ahmad Ali",
            authorDistrict = "Faisalabad",
            avatarInitials = "AA",
            imageRes = R.drawable.img_wheat_field,
            caption = "گندم کے کھیت میں زرد زنگ کا معائنہ۔ احتیاطی تدابیر لازمی کریں۔",
            timeAgo = "2h",
            isViewed = false,
            storyType = "PHOTO",
            viewers = listOf("Khursheed Ahmad", "Hussain Shah", "Sajid Ali", "Zahid Hussain"),
            likesCount = 45,
            isLiked = false,
            comments = listOf("ڈاکٹر صاحب بہت بروقت الرٹ دیا")
        ),
        FarmerStory(
            id = "story_3",
            authorName = "Hasnain Raza",
            authorDistrict = "Gujranwala",
            avatarInitials = "HR",
            imageRes = 0,
            caption = "تصدیق شدہ بیج اور اعلیٰ معیار کی جڑی بوٹی مار ادویات دکان پر دستیاب ہیں۔",
            timeAgo = "4h",
            isViewed = false,
            storyType = "TEXT",
            backgroundColor = 0xFF2E7D32,
            viewers = listOf("Khursheed Ahmad", "Farman Khan", "Hesta Rahman"),
            likesCount = 19,
            isLiked = false,
            comments = emptyList()
        ),
        FarmerStory(
            id = "story_4",
            authorName = "Farman Khan",
            authorDistrict = "Peshawar",
            avatarInitials = "FK",
            imageRes = R.drawable.img_wheat_field,
            caption = "پشاور ریسرچ فارم پر گندم کی نئی ورائٹی کی پیش رفت۔",
            timeAgo = "5h",
            isViewed = true,
            storyType = "PHOTO",
            viewers = listOf("Khursheed Ahmad", "Mubasir Khan", "Rasool Khan"),
            likesCount = 31,
            isLiked = false,
            comments = emptyList()
        )
    )

    // Demo Questions for Community Screen
    val initialQuestions = listOf(
        Question(
            id = "q_1",
            author = currentUser,
            title = "گندم میں نائٹروجن اور یوریا کی دوسری قسط کب دینی چاہیے؟",
            description = "ہماری گندم تقریباً 50 دن کی ہو چکی ہے۔ پہلے پانی پر یوریا اور زنک دیا تھا۔ اب دوسرے پانی کے وقت کیا پوٹاش یا یوریا دینا بہتر ہے؟ زمین درمیانی زرخیز ہے۔",
            cropCategory = "Wheat",
            district = "Mardan",
            timestamp = "3h",
            acceptedAnswerId = "ans_1",
            answers = listOf(
                Answer(
                    id = "ans_1",
                    authorName = "Ahmad Ali",
                    authorRole = "Zari-Mahir",
                    district = "Faisalabad",
                    text = "خورشید بھائی، 50 سے 55 دن گندم کے لیے گوبھ کی تیاری کا وقت ہے۔ دوسرے پانی کے ساتھ آدھی بوری یوریا اور 12 کلو پوٹاش دینا دانوں کی موٹائی اور وزن کے لیے انتہائی مفید ہے۔ زیادہ نائٹروجن سے پرہیز کریں ورنہ فصل گرنے کا اندیشہ رہتا ہے۔",
                    timestamp = "2h",
                    upvotes = 18,
                    isAccepted = true,
                    isExpert = true,
                    authorType = UserType.EXPERT
                ),
                Answer(
                    id = "ans_2",
                    authorName = "Hussain Shah",
                    authorRole = "Kisaan",
                    district = "Swat",
                    text = "ہم نے بھی پوٹاش کا استعمال کیا تھا، دانہ بہت چمکدار اور وزنی بنا تھا۔ ماہر صاحب کی رائے بالکل درست ہے۔",
                    timestamp = "1h",
                    upvotes = 6,
                    isAccepted = false,
                    isExpert = false,
                    authorType = UserType.FARMER
                )
            )
        ),
        Question(
            id = "q_2",
            author = farmerRasoolKhan,
            title = "تمباکو اور گندم کی پنیری میں اکھاڑہ کی بیماری کا علاج؟",
            description = "پنیری کے پودے جڑ کے پاس سے کالے ہو کر سوکھ رہے ہیں۔ فنجی سائیڈ سپرے کیا تھا لیکن کنٹرول نہیں ملا۔",
            cropCategory = "Agrochemicals",
            district = "Swabi",
            timestamp = "5h",
            acceptedAnswerId = null,
            answers = listOf(
                Answer(
                    id = "ans_3",
                    authorName = "Shafiq Hussain",
                    authorRole = "Zari-Mahir",
                    district = "Multan",
                    text = "یہ ڈیمپنگ آف (Damping-off) یعنی اکھاڑہ کی علامات ہیں۔ کیا بیج کو بوائی سے پہلے تھائیو فینیٹ میتھائل یا میٹالیکسل لگایا تھا؟ اب تنے کے قریب ڈرینچنگ کریں اور پانی کا نکاس ٹھیک کریں۔",
                    timestamp = "4h",
                    upvotes = 11,
                    isAccepted = false,
                    isExpert = true,
                    authorType = UserType.EXPERT
                )
            )
        ),
        Question(
            id = "q_3",
            author = farmerHestaRahman,
            title = "کماد کی نئی فصل میں دیمک اور بورر کے حملے سے بچاؤ کے طریقے؟",
            description = "پچھلے سال گٹھے کے اندر بورر کا حملہ زیادہ تھا۔ اس سال بوائی کے وقت کیا دانے دار زہر ڈالنا ضروری ہے؟",
            cropCategory = "Farmer problems",
            district = "Charsadda",
            timestamp = "8h",
            acceptedAnswerId = null,
            answers = emptyList()
        )
    )

    // Demo Listings for Marketplace
    val initialListings = listOf(
        MarketplaceListing(
            id = "mkt_1",
            title = "تصدیق شدہ فخر بھکر گندم بیج",
            category = "Seeds",
            price = 5200,
            unit = "فی 50 کلو بوری",
            sellerName = "Shafiq Ahmad",
            sellerDistrict = "Mardan, KP",
            sellerPhone = "03001234567",
            sellerBio = "پنجاب و خیبر سیڈ کارپوریشن کے تصدیق شدہ بیج ڈیلر۔ مردان میں 15 سالہ کسان خدمت۔",
            description = "پنجاب سیڈ کارپوریشن کا تصدیق شدہ اور جراثیم کش دوا لگا فخر بھکر گندم کا بیج۔ اگاؤ کی شرح 92 فیصد سے زائد ہے۔",
            imageRes = R.drawable.img_wheat_field,
            timestamp = "1d",
            isVerifiedSeller = true,
            availableQuantity = "120 بوریاں"
        ),
        MarketplaceListing(
            id = "mkt_2",
            title = "ہائبرڈ پائنیر مکئی بیج P1429",
            category = "Seeds",
            price = 14500,
            unit = "فی پیکٹ",
            sellerName = "Hasnain Raza",
            sellerDistrict = "Gujranwala, Punjab",
            sellerPhone = "03217654321",
            sellerBio = "رجسٹرڈ زرعی مرکز گوجرانوالہ۔ اصلی بیج اور جدید ادویات کی گارنٹی۔",
            description = "اصل درآمد شدہ پائنیر مکئی بیج، زیادہ پیداوار اور سُنڈی کے خلاف قوت مدافعت رکھنے والی قسم۔",
            imageRes = R.drawable.img_maize_harvest,
            timestamp = "2d",
            isVerifiedSeller = true,
            availableQuantity = "40 پیکٹس"
        ),
        MarketplaceListing(
            id = "mkt_3",
            title = "Millat Massey Ferguson 385 (85 HP)",
            category = "Tractor & Machinery",
            price = 3250000,
            unit = "مکمل ٹریکٹر",
            sellerName = "Tariq Aji",
            sellerDistrict = "Sheikhupura, Punjab",
            sellerPhone = "03339876543",
            sellerBio = "ٹریکٹر و زرعی مشینری شوروم شیخوپورہ۔ آسان اقساط اور نقد لین دین۔",
            description = "ملت ٹریکٹرز ماڈل 2022، صرف 1400 گھنٹے چلا ہوا، بالکل اصل کنڈیشن، نئے ٹائر، انجن و گیئر سیل پیک۔",
            imageRes = null,
            timestamp = "3d",
            isVerifiedSeller = true,
            availableQuantity = "1 یونٹ دستیاب",
            machineryDetails = MachineryDetails(
                makeModelYear = "MF 385 - 2022",
                horsepower = "85 HP",
                condition = "Used - Excellent"
            )
        ),
        MarketplaceListing(
            id = "mkt_4",
            title = "Fiat New Holland 480 (55 HP)",
            category = "Tractor & Machinery",
            price = 1950000,
            unit = "مکمل ٹریکٹر",
            sellerName = "Bashir Ahmad",
            sellerDistrict = "Multan, Punjab",
            sellerPhone = "03015554321",
            sellerBio = "ملتان زرعی مشینری منڈی، ٹریکٹرز اور ہارویسٹرز کی بااعتماد خرید و فروخت۔",
            description = "فیٹ 480 ماڈل 2019، بہترین کام کرنے کی حالت میں، کم ڈیزل خرچ اور ڈرائیونگ میں بہترین۔",
            imageRes = null,
            timestamp = "4d",
            isVerifiedSeller = true,
            availableQuantity = "1 یونٹ دستیاب",
            machineryDetails = MachineryDetails(
                makeModelYear = "Fiat 480 - 2019",
                horsepower = "55 HP",
                condition = "Used - Fair"
            )
        )
    )

    val cropCategories = listOf(
        "All Crops",
        "Wheat",
        "Maize",
        "Rice",
        "Sugarcane",
        "Cotton",
        "Vegetables",
        "Citrus",
        "Barley"
    )

    val communityFilterChips = listOf(
        "All",
        "Farmer problems",
        "Zari mahir suggestions",
        "Seeds",
        "Agrochemicals",
        "Others"
    )

    val districtsList = listOf(
        "Mardan, KP",
        "Swat, KP",
        "Charsadda, KP",
        "Swabi, KP",
        "Peshawar, KP",
        "Nowshera, KP",
        "Faisalabad, Punjab",
        "Multan, Punjab",
        "Gujranwala, Punjab",
        "Sargodha, Punjab",
        "Sahiwal, Punjab",
        "Bahawalpur, Punjab",
        "Rahim Yar Khan, Punjab",
        "Sheikhupura, Punjab",
        "Rawalpindi, Punjab",
        "Sukkur, Sindh"
    )

    val pakistaniDistricts = districtsList

    val initialNotifications = listOf(
        NotificationItem(
            id = "notif_1",
            type = NotificationType.COMMENT,
            title = "Ahmad Ali replied to your post",
            message = "احمد علی صاحب نے آپ کی گندم کی پوسٹ پر مشورہ دیا ہے۔",
            timestamp = "1h ago",
            isRead = false,
            actorName = "Ahmad Ali",
            targetPostId = "post_1"
        ),
        NotificationItem(
            id = "notif_2",
            type = NotificationType.LIKE,
            title = "Hussain Shah liked your post",
            message = "حسین شاہ نے آپ کی پوسٹ کو پسند کیا۔",
            timestamp = "2h ago",
            isRead = false,
            actorName = "Hussain Shah",
            targetPostId = "post_1"
        ),
        NotificationItem(
            id = "notif_3",
            type = NotificationType.ADVISORY,
            title = "Yellow Rust Weather Advisory",
            message = "محکمہ زراعت: گندم میں زرد زنگ سے بچاؤ کی ہدایات جاری کر دی گئی ہیں۔",
            timestamp = "5h ago",
            isRead = true,
            actorName = "Zari Advisory",
            targetPostId = null
        )
    )

    val mandiPrices = listOf(
        MandiPrice("Wheat", "گندم", "Mardan Mandi", 4200, 4400, "فی 40 کلو", "آج صبح"),
        MandiPrice("Maize", "مکئی", "Swat Mandi", 2600, 2800, "فی 40 کلو", "آج صبح"),
        MandiPrice("Sugarcane", "کماد", "Charsadda Mill", 425, 450, "فی 40 کلو", "آج صبح")
    )

    val articles = listOf(
        AgriArticle(
            id = "art_1",
            title = "Combating Yellow Rust in Wheat",
            titleUrdu = "گندم میں زرد زنگ کا سدباب",
            category = "Crop Protection",
            summary = "Essential fungicides and irrigation timing for damp weather.",
            content = "نم اور دھند والے موسم میں گندم کے کھیتوں کا روزانہ معائنہ کریں۔ زرد لکیریں ظاہر ہوتے ہی محکمہ زراعت سے تصدیق شدہ فنجی سائیڈ سپرے 48 گھنٹے کے اندر کریں۔",
            source = "Faisalabad Agricultural Research",
            readTime = "3 min read"
        ),
        AgriArticle(
            id = "art_2",
            title = "Balanced Nitrogen and Potash Timing",
            titleUrdu = "متوازن نائٹروجن اور پوٹاش کھاد کا استعمال",
            category = "Soil Fertility",
            summary = "Why potash is crucial at booting stage.",
            content = "گندم اور مکئی میں گوبھ کے وقت پوٹاش کھاد دانے کی چمک، موٹائی اور وزن میں خاطر خواہ اضافہ کرتی ہے۔",
            source = "Soil Science Directorate",
            readTime = "4 min read"
        )
    )

    // ==========================================
    // INITIAL MY FARM CROPS (Zameen-dari)
    // ==========================================
    val initialFarmCrops = listOf(
        FarmCrop(
            id = "crop_wheat_1",
            cropName = "Wheat",
            cropNameUrdu = "گندم",
            variety = "Fakhr-e-Bhakkar (فخر بھکر)",
            sowingDate = "15 Nov 2024",
            areaAcres = 4.5,
            soilType = "Loamy / چکنی مٹی",
            district = "Mardan",
            province = "KP",
            currentStage = "Booting / Grain Filling",
            currentStageUrdu = "گوبھ اور دانہ بھرائی مرحلہ",
            durationDays = 150,
            daysElapsed = 95,
            weatherTips = listOf(
                "موجودہ نمی اور درجہ حرارت زرد زنگ کے لیے موزوں ہے، روزانہ پتوں کا معائنہ کریں۔",
                "تیز ہوا کے دوران آبپاشی سے گریز کریں تاکہ فصل گرنے سے محفوظ رہے۔",
                "دھوپ نکلنے پر پوٹاش اور زنک کا فولیر سپرے دانوں کا وزن 18 فیصد بڑھاتا ہے۔"
            ),
            nutrientsSchedule = listOf(
                NutrientItem(
                    name = "DAP (Di-ammonium Phosphate)",
                    nameUrdu = "ڈی اے پی کھاد",
                    stage = "بوائی کے وقت (Basal Dose)",
                    dosage = "1 بوری فی ایکڑ",
                    purpose = "جڑوں کی مضبوطی اور پودے کا ابتدائی پھیلاؤ",
                    recommendedDealers = listOf("Shafiq Ahmad (Mardan)", "Hasnain Raza (Gujranwala)")
                ),
                NutrientItem(
                    name = "Urea + Potash (SOP)",
                    nameUrdu = "یوریا اور پوٹاش",
                    stage = "پہلے و دوسرے پانی پر (Crown Root & Tillering)",
                    dosage = "1 بوری یوریا + 12 کلو پوٹاش فی ایکڑ",
                    purpose = "شاخوں میں اضافہ اور گوبھ کی تیاری",
                    recommendedDealers = listOf("Tariq Aji", "Shafiq Ahmad")
                ),
                NutrientItem(
                    name = "Liquid Boron & Zinc",
                    nameUrdu = "بوران اور زنک فولیر سپرے",
                    stage = "سٹے نکلتے وقت (Heading stage)",
                    dosage = "250 ملی لیٹر فی 100 لیٹر پانی",
                    purpose = "پولینیشن میں بہتری اور دانوں کا مکمل بننا",
                    recommendedDealers = listOf("Shafiq Ahmad (Mardan)")
                )
            ),
            medicineSchedule = listOf(
                MedicineItem(
                    diseaseOrPest = "Yellow Rust (Puccinia striiformis)",
                    diseaseOrPestUrdu = "زرد زنگ (پیلا زنگ)",
                    sprayName = "Nativo (Tebuconazole + Trifloxystrobin)",
                    timing = "علامات ظاہر ہوتے ہی یا نمی بڑھنے پر",
                    dosage = "65 گرام فی 100 لیٹر پانی",
                    recommendedDealers = listOf("Shafiq Ahmad (Mardan)", "Hasnain Raza")
                ),
                MedicineItem(
                    diseaseOrPest = "Aphid / Wheat Louse",
                    diseaseOrPestUrdu = "گندم کا سُست تیلہ",
                    sprayName = "Imidacloprid 200 SL",
                    timing = "فروری کے آخری ہفتے میں اگر تیلہ 10 فی سٹا ہو",
                    dosage = "200 ملی لیٹر فی ایکڑ",
                    recommendedDealers = listOf("Shafiq Ahmad")
                )
            ),
            estimatedYield = "48–52 من فی ایکڑ"
        ),
        FarmCrop(
            id = "crop_maize_2",
            cropName = "Hybrid Maize",
            cropNameUrdu = "ہائبرڈ مکئی",
            variety = "Pioneer P1429 (بہاریہ)",
            sowingDate = "10 Feb 2025",
            areaAcres = 2.0,
            soilType = "Sandy Loam / ریتلی میرا",
            district = "Swat",
            province = "KP",
            currentStage = "Early Vegetative (V4)",
            currentStageUrdu = "ابتدائی پودے کا پھیلاؤ (4 پتے)",
            durationDays = 110,
            daysElapsed = 25,
            weatherTips = listOf(
                "بہاریہ مکئی کو ابتدائی دنوں میں کورے اور رات کی سردی سے بچائیں۔",
                "فال آرمی ورم (لشکری سنڈی) کے ابتدائی نشانات دل پر چیک کریں۔"
            ),
            nutrientsSchedule = listOf(
                NutrientItem(
                    name = "Nitrophos + Zinc",
                    nameUrdu = "نائٹروفاس اور زنک",
                    stage = "بوائی کے وقت",
                    dosage = "1.5 بوری نائٹروفاس فی ایکڑ",
                    purpose = "ابتدائی پودے کی مضبوطی",
                    recommendedDealers = listOf("Hasnain Raza", "Shafiq Ahmad")
                )
            ),
            medicineSchedule = listOf(
                MedicineItem(
                    diseaseOrPest = "Fall Armyworm (Spodoptera frugiperda)",
                    diseaseOrPestUrdu = "لشکری سنڈی / فال آرمی ورم",
                    sprayName = "Emamectin Benzoate 1.9 EC",
                    timing = "پتوں پر سوراخ نظر آتے ہی دل میں سپرے کریں",
                    dosage = "200 ملی لیٹر فی ایکڑ",
                    recommendedDealers = listOf("Hasnain Raza")
                )
            ),
            estimatedYield = "85–95 من فی ایکڑ"
        )
    )

    // ==========================================
    // INITIAL ZAMEEN-DARI KHATA ENTRIES
    // ==========================================
    val initialKhataEntries = listOf(
        KhataEntry(
            id = "khata_1",
            cropId = "crop_wheat_1",
            cropName = "Wheat",
            type = KhataType.EXPENSE,
            category = "Seed",
            amount = 20800,
            description = "4 بوری فخر بھکر تصدیق شدہ بیج",
            date = "15 Nov 2024"
        ),
        KhataEntry(
            id = "khata_2",
            cropId = "crop_wheat_1",
            cropName = "Wheat",
            type = KhataType.EXPENSE,
            category = "Land Prep",
            amount = 14500,
            description = "لیزر لیولر اور روٹاویٹر کرایہ (4.5 ایکڑ)",
            date = "12 Nov 2024"
        ),
        KhataEntry(
            id = "khata_3",
            cropId = "crop_wheat_1",
            cropName = "Wheat",
            type = KhataType.EXPENSE,
            category = "Fertilizer",
            amount = 49500,
            description = "4 بوری ڈی اے پی اور 2 بوری یوریا",
            date = "28 Nov 2024"
        ),
        KhataEntry(
            id = "khata_4",
            cropId = "crop_wheat_1",
            cropName = "Wheat",
            type = KhataType.EXPENSE,
            category = "Spray / Medicine",
            amount = 6200,
            description = "زرد زنگ سے بچاؤ کا حفاظتی فنجی سائیڈ سپرے",
            date = "14 Jan 2025"
        ),
        KhataEntry(
            id = "khata_5",
            cropId = "crop_maize_2",
            cropName = "Hybrid Maize",
            type = KhataType.EXPENSE,
            category = "Seed",
            amount = 29000,
            description = "پائنیر ہائبرڈ مکئی بیج 2 پیکٹ",
            date = "10 Feb 2025"
        ),
        KhataEntry(
            id = "khata_6",
            cropId = "crop_wheat_1",
            cropName = "Wheat",
            type = KhataType.INCOME,
            category = "Harvest Sale",
            amount = 185000,
            description = "پچھلے سیزن کی گندم اور بھوسا فروخت",
            date = "05 Dec 2024"
        )
    )

    // ==========================================
    // INITIAL COMMUNITY POSTS (Voice, Text, Image)
    // ==========================================
    val initialCommunityPosts = listOf(
        CommunityPost(
            id = "comm_post_1",
            author = currentUser,
            text = "السلام علیکم کسان بھائیو! گندم اب 95 دن کی ہو چکی ہے اور گوبھ کے مرحلے پر ہے۔ کل شام پتوں پر ہلکی پیلی دھاریاں نظر آئیں۔ کیا یہ زرد زنگ کے آثار ہیں؟ اس مرحلے پر کون سا فنجی سائیڈ سب سے محفوظ رہے گا؟",
            voiceUri = null,
            voiceDurationSeconds = 18,
            imageRes = R.drawable.img_wheat_field,
            category = "Farmer problems",
            district = "Mardan, KP",
            timestamp = "2h ago",
            likeCount = 14,
            isLiked = false,
            comments = listOf(
                Comment(
                    id = "c_1",
                    authorName = "Ahmad Ali",
                    authorDistrict = "Faisalabad",
                    text = "خورشید بھائی، یہ ابتدائی زرد زنگ (Yellow Rust) کے واضح اثرات ہیں۔ موسم میں نمی زیادہ ہے۔ فوری طور پر نیٹیوو یا ٹیبوکونازول کا سپرے کریں۔ تاخیر سے دانہ کمزور پڑ جائے گا۔",
                    timestamp = "1h ago",
                    isExpert = true,
                    authorType = UserType.EXPERT,
                    isLiked = true,
                    likeCount = 8,
                    replies = listOf(
                        ReplyComment(
                            id = "r_1",
                            authorName = "Khursheed Ahmad",
                            authorType = UserType.FARMER,
                            authorDistrict = "Mardan",
                            text = "شکریہ احمد علی صاحب! کیا سپرے کے ساتھ پوٹاش کھاد بھی ملا سکتے ہیں؟",
                            timestamp = "45m ago",
                            isLiked = false,
                            likeCount = 2
                        ),
                        ReplyComment(
                            id = "r_2",
                            authorName = "Ahmad Ali",
                            authorType = UserType.EXPERT,
                            authorDistrict = "Faisalabad",
                            text = "جی بالکل، این پی کے یا مائع پوٹاش سپرے کے ساتھ ملائی جا سکتی ہے بشرطیکہ پانی صاف ہو۔",
                            timestamp = "30m ago",
                            isLiked = true,
                            likeCount = 4
                        )
                    )
                ),
                Comment(
                    id = "c_2",
                    authorName = "Shafiq Ahmad",
                    authorDistrict = "Mardan",
                    text = "ہماری دکان پر اصلی بائر کی نیٹیوو دستیاب ہے۔ کسان بھائیوں کو مناسب ریٹ پر دیں گے۔",
                    timestamp = "40m ago",
                    isExpert = false,
                    authorType = UserType.DEALER,
                    isLiked = false,
                    likeCount = 3
                )
            )
        ),
        CommunityPost(
            id = "comm_post_2",
            author = farmerHussainShah,
            text = "سوات میں بہاریہ مکئی کی بوائی کے بعد پہلے پانی کی احتیاطی تدابیر۔ آواز میں مکمل تفصیل سنیں۔",
            voiceUri = "sample_voice_2",
            voiceDurationSeconds = 32,
            imageRes = R.drawable.img_maize_harvest,
            category = "Zari mahir suggestions",
            district = "Swat, KP",
            timestamp = "4h ago",
            likeCount = 21,
            isLiked = true,
            comments = listOf(
                Comment(
                    id = "c_3",
                    authorName = "Hesta Rahman",
                    authorDistrict = "Charsadda",
                    text = "بہت زبردست رہنمائی۔ ہم بھی چارسدہ میں اسی طریقے پر عمل کر رہے ہیں۔",
                    timestamp = "3h ago",
                    authorType = UserType.FARMER,
                    isLiked = false,
                    likeCount = 5
                )
            )
        ),
        CommunityPost(
            id = "comm_post_3",
            author = farmerRasoolKhan,
            text = "صوابی میں تمباکو کی پنیری منتقل کرنے کا بہترین وقت اور کھاد کا درست تناسب۔",
            voiceUri = null,
            voiceDurationSeconds = 0,
            imageRes = null,
            category = "Farmer problems",
            district = "Swabi, KP",
            timestamp = "6h ago",
            likeCount = 9,
            isLiked = false,
            comments = emptyList()
        )
    )
}

