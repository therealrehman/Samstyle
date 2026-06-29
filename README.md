# Samstyle - Gradle Build Files

## Files Created

This package contains all the missing Gradle build files for the Samstyle Android keyboard project.

### Root Level Files:
- `build.gradle.kts` - Root build script
- `settings.gradle.kts` - Project settings
- `gradle.properties` - Gradle properties
- `gradlew` - Gradle wrapper (Unix/Linux/Mac)
- `gradlew.bat` - Gradle wrapper (Windows)

### Gradle Wrapper:
- `gradle/wrapper/gradle-wrapper.properties` - Wrapper properties

### App Level:
- `app/build.gradle.kts` - App module build script
- `app/proguard-rules.pro` - ProGuard rules

## Important: Download Gradle Wrapper JAR

You need to download the actual `gradle-wrapper.jar` file:

1. Go to: https://services.gradle.org/distributions/gradle-8.2-bin.zip
2. Extract `gradle-8.2/lib/plugins/gradle-wrapper-8.2.jar`
3. Copy it to: `gradle/wrapper/gradle-wrapper.jar`

Or use this command:
```bash
cd /path/to/project
gradle wrapper --gradle-version 8.2
```

## Dependencies Included

- AndroidX Core & Compose
- AppCompat & Material Design
- EventBus (for reactive settings)
- Lottie (animations)
- Gson (JSON parsing)
- Glide (image loading)
- Preference (Settings)

## Build Instructions

```bash
./gradlew assembleDebug
```

APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

## Requirements

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17 or newer
- Android SDK 34
