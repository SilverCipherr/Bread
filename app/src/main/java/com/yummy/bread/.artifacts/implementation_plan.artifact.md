# Implementation Plan - Logo and Icon Refresh

I will replace the existing app logo and all launcher icons with the newly uploaded design.

## Proposed Changes

### 1. Resource Cleanup
I will ensure all legacy `.webp` or older `.png` launcher icons are removed to prevent density-mismatch or duplication issues.

### 2. Asset Generation
Using ImageMagick, I will generate the following from the source at `app/src/main/res/drawable/bread_logo.png`:

- **Main UI Logo**:
    - `app/src/main/res/drawable/bread_logo.png` (Resize the source to 512x512 for optimal UI performance).

- **Standard Launcher Icons (`ic_launcher.png` & `ic_launcher_round.png`)**:
    - `mipmap-mdpi`: 48x48 px
    - `mipmap-hdpi`: 72x72 px
    - `mipmap-xhdpi`: 96x96 px
    - `mipmap-xxhdpi`: 144x144 px
    - `mipmap-xxxhdpi`: 192x192 px

- **Adaptive Icon Foreground (`ic_launcher_foreground.png`)**:
    - The logo will be scaled to ~83% (90dp) within a 108dp canvas to ensure it looks "full" while staying safely within system masks.
    - `mipmap-mdpi`: 108x108 px
    - `mipmap-hdpi`: 162x162 px
    - `mipmap-xhdpi`: 216x216 px
    - `mipmap-xxhdpi`: 324x324 px
    - `mipmap-xxxhdpi`: 432x432 px

## Verification Plan

### Automated Tests
- Run `./gradlew :app:packageDebugResources` to verify that all new resources are correctly generated and valid.

### Manual Verification
- Verify the new logo appears in the **Top App Bar** and **Splash Screen**.
- Confirm the app icon on the device home screen is sharp and correctly scaled without white borders.
