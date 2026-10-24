# Kisaan Book (کسان بُک) – Complete Project Architecture & Master AI Prompt

---

## 1. Master AI Continuation Prompt
> **Copy & paste this prompt into any AI agent to continue development without losing context:**

```markdown
You are working on "Kisaan Book" (کسان بُک), a modern, production-grade Android application built using Kotlin and Jetpack Compose (Material 3). The app is tailored for Pakistani farmers, agricultural experts (Zari-Mahir), and rural input dealers (Dokandar).

### Core Brand & Design Rules:
1. Primary Color: `#86AF2F` (Deep Agricultural Olive Green).
2. Default Language: Urdu (`AppLanguage.URDU`), with seamless toggles for English and Pashto. No mixed-language text strings.
3. No Emojis: Strictly use Google Material Symbols / Icons across all UI elements (never emojis).
4. Design System: Clean Meta/Facebook-style spacing (8dp grid, 16dp outer padding, 12-24dp rounded corners, minimum 48dp touch targets).
5. Comment & Action Layout: Action buttons (Like, Reply, Call, WhatsApp) must never collapse or get pushed offscreen (always use Modifier.weight(1f) for variable text).

### Core Architecture & Key Modules:
- Architecture: MVVM with StateFlow & Lifecycle-aware collectors.
- Models (`com.example.model`):
  - `FarmCrop`: Crop name, variety, sowing date, acres, soil type, duration in days, days elapsed, stage progress, weather tips, nutrient schedule, medicine schedule.
  - `KhataEntry`: Expense vs Income, amount, crop association, category (Seed, Fertilizer, Land Prep, Spray, Water, Labor, Harvest Sale), date, description.
  - `CommunityPost`: Author (`Farmer` with `UserType`: FARMER, EXPERT, DEALER), text, voice recording (seconds + preview), compact picture (200dp height, 12dp radius), category, likes, nested comments & replies.
  - `MarketplaceListing`: Title, category, price, unit, seller details (phone, bio, verified badge), machinery details (HP, model year, condition for tractors).
  - `PlantDiagnosis`: Problem name (Urdu/English), symptoms, severity, treatments, sprays, authorized dealers.
  - `CalendarType` & `MonthInfo`: Gregorian, Hijri, and Pashtun / Desi calendar systems with provincial crop schedules.

### Tab Navigation (`AppTab`):
1. HOME: Farmer Utility Hub with Compact Weather (Today-first + dropdown), Plant Doctor Scanner, My Crops Planner, Zameen-dari Khata, and Pakistan Crop Calendar.
2. COMMUNITY: Filter chips (All, Text, Voice, By Picture), Sticky collapsing search bar, Voice recording modal (record, pause, delete), nested comment replies, author badges (Green Leaf = Farmer, Gold Tick = Expert, Store = Dealer), WhatsApp share intent.
3. MARKETPLACE: Direct Call (`tel:...`) and direct WhatsApp (`wa.me/...`) buttons, seller bio/location card, dynamic form inputs for tractors (HP, year, condition, total price) vs crops (unit price, packaging).
4. PROFILE: Compact bio with click-to-expand ("Show more / less"), compact Edit Profile button, posted activity stream, redesigned Settings modal defaulting to Urdu with login/logout states.
```

---

## 2. Project Directory & File Tree

```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/
│   ├── MainActivity.kt                      // Entry point, enableEdgeToEdge, hosts KisaanBhaiApp
│   ├── data/
│   │   └── SampleData.kt                    // Initial crops, khata entries, posts, listings, mandis
│   ├── localization/
│   │   └── Localization.kt                  // AppLanguage enum (URDU, ENGLISH, PASHTO) & Strings
│   ├── model/
│   │   └── Models.kt                        // Domain models (FarmCrop, KhataEntry, CommunityPost, etc.)
│   ├── viewmodel/
│   │   └── KisaanBhaiViewModel.kt           // State management, filters, sub-screen navigation
│   └── ui/
│       ├── KisaanBhaiApp.kt                 // Main Scaffold, NavigationBar, sub-screen switching & dialogs
│       ├── theme/
│       │   ├── Color.kt                     // Primary #86AF2F palette
│       │   ├── Theme.kt                     // KisaanBookTheme (Light, Dark, System)
│       │   └── Type.kt                      // Typography configuration
│       ├── screens/
│       │   ├── HomeScreen.kt                // Farmer Utility Hub & Compact Weather Card
│       │   ├── PlantDoctorScreen.kt         // Camera / Leaf Problem Scanner & Diagnosis
│       │   ├── MyCropsScreen.kt             // Crop progress tracker, nutrient & spray schedules
│       │   ├── KhataScreen.kt               // Zameen-dari farm expense/income ledger & KPIs
│       │   ├── CalendarScreen.kt            // Pakistan 3-in-1 Crop Calendar (Gregorian, Hijri, Pashtun)
│       │   ├── CommunityScreen.kt           // Voice/Text/Picture posts, sticky collapsing top bar
│       │   ├── MarketplaceScreen.kt         // Buy & Sell, Direct Call/WhatsApp, Seller bios
│       │   ├── ProfileScreen.kt             // Farmer profile, click-to-expand bio, activity stream
│       │   ├── NotificationsScreen.kt       // Weather advisories, price updates, social alerts
│       │   └── SettingsDialog.kt            // Urdu-default language switcher & auth state
│       └── components/
│           ├── AddKhataEntryDialog.kt       // Quick Expense/Income creation modal
│           ├── CreateCropDialog.kt          // Sowing date, acre calculator, soil selector
│           ├── CreateCommunityPostDialog.kt // Text, prominent Voice Recorder, photo picker
│           ├── CreateListingDialog.kt       // Dynamic inputs (Machinery vs Seeds/Fertilizers)
│           ├── ContactSellerSheet.kt        // Direct Call, Direct WhatsApp, Seller bio & location
│           ├── CommentsBottomSheet.kt       // Modal comments viewer
│           └── FullscreenPostViewer.kt      // Fullscreen media modal
└── res/
    ├── drawable/                            // Pakistani tractor, wheat, and maize visuals
    └── values/
        ├── strings.xml                      // app_name = "Kisaan Book"
        └── colors.xml
```

---

## 3. Key Functional Modules & Technical Details

### 1. Home Tab (Farmer Utility Hub)
- **Compact Weather Card**:
  - Highlights **Today** temperature, weather state (Sunny, Partly Cloudy, Cloudy) with Material icons.
  - Hides secondary metrics (humidity, breeze) for maximum visual simplicity.
  - Dropdown button expands 7-day upcoming forecast with daily high/low temperatures.
  - GPS locator retrieves district locality via `LocationManager` and `Geocoder`.
- **4 Feature Action Tiles**:
  1. *Plant Doctor*: Direct shortcut to leaf disease scanner with camera preview.
  2. *My Farm Crops*: Active crop progress bars, days remaining, "+ Add Crop" shortcut.
  3. *Zameen-dari Khata*: Real-time financial KPI chips (Total Expense, Total Income, Net Profit).
  4. *Pakistan Crop Calendar*: 3-system agricultural calendar button.

### 2. Community Q&A & Voice Post Feed
- **Sticky Collapsing Top Bar**:
  - Implements scroll-derived collapse on LazyList state (`listState.firstVisibleItemScrollOffset > 40`).
  - Search bar collapses on scroll down; media filter chips (`All`, `Text`, `Voice`, `By Picture`) and category chips remain pinned sticky at the top.
- **Voice Message Feature**:
  - Dedicated Voice Recorder modal: Record, Pause, Resume, Delete, and Done actions with timer.
  - In-feed Voice Player bar: Play/Pause button, animated progress bar, formatted timestamps.
- **Hierarchical Nested Comments**:
  - Comment item uses `Modifier.weight(1f)` for text so Action buttons (Like, Reply) never wrap or clip.
  - Nested sub-comments indented by 24dp with author badges.
- **User Role Badges**:
  - `UserType.FARMER`: Green Leaf icon + "کسان"
  - `UserType.EXPERT`: Gold verified tick + "زرعی ماہر"
  - `UserType.DEALER`: Blue storefront icon + "دوکاندار"

### 3. Marketplace & Direct Seller Interaction
- **One-Click Communication**:
  - Direct Phone Call: `Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))`.
  - Direct WhatsApp Chat: `Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$formattedNumber?text=$encodedText"))`.
- **Seller Profile Summary**:
  - Displays business/shop name, district location, verified supplier badge, and seller bio.
- **Smart Dynamic Listing Form**:
  - When category is "Tractor & Machinery": Automatically replaces weight/bag dropdowns with Model/Year, Horsepower (HP), Machine Condition, and Total Price.

### 4. Profile & Localization Settings
- **Click-to-Expand Bio**:
  - Clicking directly on the bio text toggles 2 lines vs. full description ("مزید دکھائیں / کم دکھائیں").
- **Compact Header**:
  - Main crop tags and Edit Profile button aligned tightly, ensuring user's activity stream is instantly visible above the fold.
- **Settings Screen**:
  - **Urdu is selected as default**, with instant switching to English and Pashto.
  - Clean authentication card showing current account status, district, and single-tap Logout/Login.
