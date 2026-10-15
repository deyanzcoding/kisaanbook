package com.example.localization

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    URDU("ur", "Urdu", "اردو"),
    PASHTO("ps", "Pashto", "پښتو")
}

object Strings {
    fun appName(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Kisaan Book"
        AppLanguage.URDU -> "کسان بک"
        AppLanguage.PASHTO -> "کِسان بُک"
    }

    fun appSlogan(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Connecting Farmers, Growing Together"
        AppLanguage.URDU -> "کسانوں کا اپنا ڈیجیٹل رجسٹر اور برادری"
        AppLanguage.PASHTO -> "د بزګرانو ډیجیټل راجستر او ټولنه"
    }

    fun tabHome(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Home"
        AppLanguage.URDU -> "ہوم"
        AppLanguage.PASHTO -> "کور"
    }

    fun tabCommunity(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Community"
        AppLanguage.URDU -> "کمیونٹی"
        AppLanguage.PASHTO -> "ټولنه"
    }

    fun tabMarket(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Market"
        AppLanguage.URDU -> "منڈی"
        AppLanguage.PASHTO -> "منډۍ"
    }

    fun tabAlerts(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Alerts"
        AppLanguage.URDU -> "اطلاعات"
        AppLanguage.PASHTO -> "خبرتیاوې"
    }

    fun tabProfile(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Profile"
        AppLanguage.URDU -> "پروفائل"
        AppLanguage.PASHTO -> "پروفایل"
    }

    fun greeting(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Assalam-o-Alaikum, Farmer!"
        AppLanguage.URDU -> "السلام علیکم، کسان بھائی!"
        AppLanguage.PASHTO -> "سلامونه، کِسان وروره!"
    }

    fun like(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Like"
        AppLanguage.URDU -> "پسند"
        AppLanguage.PASHTO -> "خوښول"
    }

    fun comment(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Comment"
        AppLanguage.URDU -> "رائے"
        AppLanguage.PASHTO -> "نظر"
    }

    fun share(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Share"
        AppLanguage.URDU -> "شیئر"
        AppLanguage.PASHTO -> "شریکول"
    }

    fun save(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Save"
        AppLanguage.URDU -> "محفوظ"
        AppLanguage.PASHTO -> "ساتل"
    }

    fun createPost(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "New Post"
        AppLanguage.URDU -> "نیا پوسٹ"
        AppLanguage.PASHTO -> "نوې پُوسټ"
    }

    fun askQuestion(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Ask Question"
        AppLanguage.URDU -> "سوال پوچھیں"
        AppLanguage.PASHTO -> "پوښتنه وکړئ"
    }

    fun sellRent(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Sell / Rent"
        AppLanguage.URDU -> "فروخت / کرایہ"
        AppLanguage.PASHTO -> "خرڅلاو / کرایه"
    }

    fun contactSeller(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Contact"
        AppLanguage.URDU -> "رابطہ کریں"
        AppLanguage.PASHTO -> "اړیکه"
    }

    fun searchPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Search crops, problems, tractors..."
        AppLanguage.URDU -> "فصل، بیماری یا ٹریکٹر تلاش کریں..."
        AppLanguage.PASHTO -> "فصل، ناروغۍ یا ټراکټور لټول..."
    }

    fun settingsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Settings & Preferences"
        AppLanguage.URDU -> "سیٹنگز اور ترجیحات"
        AppLanguage.PASHTO -> "تنظیمات او غوراوي"
    }

    fun theme(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Theme Mode"
        AppLanguage.URDU -> "تھیم موڈ"
        AppLanguage.PASHTO -> "د بڼې حالت"
    }

    fun themeLight(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Light (Day)"
        AppLanguage.URDU -> "روشن (دن)"
        AppLanguage.PASHTO -> "رڼا (ورځ)"
    }

    fun themeDark(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Dark (Night)"
        AppLanguage.URDU -> "ڈارک (رات)"
        AppLanguage.PASHTO -> "تیاره (شپه)"
    }

    fun themeSystem(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "System Auto"
        AppLanguage.URDU -> "خودکار سسٹم"
        AppLanguage.PASHTO -> "خپلسری غونډال"
    }

    fun language(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "App Language"
        AppLanguage.URDU -> "ایپ کی زبان"
        AppLanguage.PASHTO -> "د اپلیکیشن ژبه"
    }

    fun login(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Log In"
        AppLanguage.URDU -> "لاگ ان"
        AppLanguage.PASHTO -> "ننوتل"
    }

    fun signup(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Create Account"
        AppLanguage.URDU -> "نیا اکاؤنٹ بنائیں"
        AppLanguage.PASHTO -> "نوی ګڼون جوړول"
    }

    fun logout(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Log Out / Switch Account"
        AppLanguage.URDU -> "لاگ آؤٹ / اکاؤنٹ بدلیں"
        AppLanguage.PASHTO -> "وتل / ګڼون بدلول"
    }

    fun mobileNumber(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Mobile Phone Number"
        AppLanguage.URDU -> "موبائل فون نمبر"
        AppLanguage.PASHTO -> "ګرځنده شمېره"
    }

    fun password(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Password / 4-Digit PIN"
        AppLanguage.URDU -> "پاسورڈ / ۴ ہندسی پن کوڈ"
        AppLanguage.PASHTO -> "پټنوم / څلور عددي کوډ"
    }

    fun farmerName(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Your Full Name"
        AppLanguage.URDU -> "کسان بھائی کا پورا نام"
        AppLanguage.PASHTO -> "ستاسو بشپړ نوم"
    }

    fun selectDistrict(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Select District"
        AppLanguage.URDU -> "اپنا ضلع منتخب کریں"
        AppLanguage.PASHTO -> "خپله ولسوالۍ غوره کړئ"
    }

    fun mainCrops(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Main Crops"
        AppLanguage.URDU -> "اہم فصلیں"
        AppLanguage.PASHTO -> "مهم فصلونه"
    }

    fun cropAdvice(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Crop Advice"
        AppLanguage.URDU -> "زرعی مشورہ"
        AppLanguage.PASHTO -> "د فصل مشوره"
    }

    fun mandiRates(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Mandi Rates"
        AppLanguage.URDU -> "منڈی ریٹس"
        AppLanguage.PASHTO -> "د منډۍ نرخونه"
    }

    fun weather(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Weather"
        AppLanguage.URDU -> "موسمی صورتحال"
        AppLanguage.PASHTO -> "د هوا وړاندوینه"
    }

    fun machinery(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Machinery & Rental"
        AppLanguage.URDU -> "ٹریکٹر و مشینری"
        AppLanguage.PASHTO -> "ماشینري او کرایه"
    }

    fun markAllRead(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Mark all read"
        AppLanguage.URDU -> "سب پڑھے گئے"
        AppLanguage.PASHTO -> "ټول لوستل شوي"
    }

    fun myPosts(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "My Posts"
        AppLanguage.URDU -> "میری پوسٹس"
        AppLanguage.PASHTO -> "زما پوسټونه"
    }

    fun myQuestions(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "My Questions"
        AppLanguage.URDU -> "میرے سوالات"
        AppLanguage.PASHTO -> "زما پوښتنې"
    }

    fun savedItems(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Saved"
        AppLanguage.URDU -> "محفوظ شدہ"
        AppLanguage.PASHTO -> "ساتل شوي"
    }

    fun editProfile(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Edit Profile"
        AppLanguage.URDU -> "پروفائل درست کریں"
        AppLanguage.PASHTO -> "پروفایل سمول"
    }
}
