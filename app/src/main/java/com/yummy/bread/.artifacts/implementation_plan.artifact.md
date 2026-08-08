# Implementation Plan - Simultaneous Debug and Release Installation

To allow both the debug and release versions of the app to be installed on your phone at the same time, they need to have different **Application IDs**. I will also add a suffix to the version name so you can identify it in the app info.

## Proposed Changes

### 1. Build Configuration
- **[MODIFY] [app/build.gradle.kts](file:///home/silvercipher/Projects/Bread/app/build.gradle.kts)**:
    - Add a `debug` build type block.
    - Set `applicationIdSuffix = ".debug"`. This is required to allow both apps to coexist on the device.
    - Set `versionNameSuffix = "-debug"`. This will show up in the phone's App Info (e.g., `1.1-beta-debug`).
    - I will **NOT** change the app name/label, as per your request.

## Verification Plan

### Automated Build
- Run `./gradlew :app:assembleDebug` and `./gradlew :app:assembleRelease`.

### Manual Verification
- Install the release APK.
- Install the debug APK.
- Verify that both apps appear as separate icons (with the same name) on your phone's home screen.
- Verify that the debug version shows the `-debug` suffix in the system App Info settings.
