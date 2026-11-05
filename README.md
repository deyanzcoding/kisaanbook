# Kisaan Book

**Kisaan Book** - Connecting Farmers, Growing Together

A Flutter/Compose based mobile application for the Pakistani agricultural community. This app provides:
- **Community Forum**: Farmers can share experiences, ask questions, and discuss crop issues
- **Marketplace**: Buy and sell seeds, fertilizers, pesticides, and farm machinery
- **Crop Advisor**: Expert advice from agricultural specialists
- **Mandi Prices**: Real-time price updates for crops
- **Farm Expenses Tracker**: Manage your farm's income and expenses
- **Calendar**: Track planting and harvesting schedules

## Features

### Community Screen
- Text posts with images
- Voice recordings
- Question & Answer system with expert validation
- Comment threads and replies
- Like and save functionality

### Marketplace
- Verified dealer listings
- Seeds, fertilizers, pesticides, and machinery
- Contact sellers directly
- Category-wise filtering

### My Farm
- Track multiple crops with growth stages
- Nutrient and medicine schedules
- Weather tips
- Harvest estimation

### Khata (Ledger)
- Expense tracking for seeds, fertilizers, labor, etc.
- Income tracking from sales
- Crop-wise categorization

### Language Support
- English, Urdu, and Pashto
- RTL support for Urdu and Pashto

## Getting Started

### Prerequisites
- Android Studio Flamingo or later
- Flutter SDK (if cross-platform is needed)
- Android SDK 24+

### Installation
1. Clone the repository:
```bash
git clone https://github.com/deyanzcoding/kisaanbook.git
```

2. Open in Android Studio or run:
```bash
./gradlew assembleDebug
```

### Build Variants
- **debug**: Development build with debug flags
- **release**: Production-ready build

## Project Structure

```
kisaanbook/
├── app/
│   ├── src/main/java/com/example/
│   │   ├── ui/           # Compose UI screens
│   │   ├── viewmodel/    # ViewModels and state management
│   │   ├── model/        # Data classes
│   │   ├── data/         # Sample data
│   │   ├── localization/ # Multi-language strings
│   │   └── theme/        # App theming
│   └── src/main/res/     # Android resources
├── gradle/
└── build.gradle.kts
```

## Technical Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose (Material 3)
- **State Management**: Kotlin Flow + StateFlow
- **Architecture**: MVVM (Model-View-ViewModel)
- **Targeting**: Android, iOS (Expo Flutter)

## Contributing

Feel free to open issues and submit pull requests!

## License

This project is developed for the Pakistani farming community.

## Acknowledgments

- Inspired by agricultural social networks in Pakistan
- Designed for Urdu and Pashto speaking farmers
- Built with Flutter for cross-platform compatibility