# Walkthrough - Dual Debug and Release Installation

I have updated the build configuration to allow you to install both the Debug and Release versions of the app on your phone simultaneously.

## Changes Made

### 1. Build Variant Configuration
- **[Modified app/build.gradle.kts](file:///home/silvercipher/Projects/Bread/app/build.gradle.kts)**:
    - Added a custom `debug` build type.
    - **`applicationIdSuffix = ".debug"`**: This creates a unique ID for the debug version (`com.yummy.bread.debug`), allowing Android to see it as a separate app from the release version (`com.yummy.bread`).
    - **`versionNameSuffix = "-debug"`**: This ensures that when you view "App Info" in your phone settings, the debug version is clearly marked (e.g., `1.1-beta-debug`).

## Verification Results

### Build Success
- **Build Task**: `./gradlew :app:assembleDebug :app:assembleRelease`
- **Result**: `Build finished successfully.`

### How to use
1.  **Install Release**: Build and install the release APK.
2.  **Install Debug**: Build and install the debug APK.
3.  **Result**: You will now see two identical "Bread" icons on your home screen. You can differentiate them by checking the version name in the "App Info" section of your phone's settings.
