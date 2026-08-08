# Implementation Plan - Fix App Icon White Border

I will convert the legacy app icon into an **Adaptive Icon**. This will eliminate the default white border added by the Android system and allow the logo to fill the icon area while ensuring no parts are cut off.

## Proposed Changes

### 1. Adaptive Icon Definitions
- **[NEW] [ic_launcher.xml](file:///home/silvercipher/Projects/Bread/app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml)**:
    - Define an adaptive icon using a black background and a scaled foreground layer.
- **[NEW] [ic_launcher_round.xml](file:///home/silvercipher/Projects/Bread/app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml)**:
    - Alias to the adaptive icon definition.

### 2. Foreground Layer Generation
I will use the `bread_logo.png` to generate a dedicated foreground layer (`ic_launcher_foreground.png`) for all densities.
- The logo will be centered on a transparent 108dp x 108dp canvas.
- I will scale the logo to ~80% of the canvas size to ensure it fills the icon "fully" without hitting the safe-zone edges (preventing cutoff).

Densities to generate for `ic_launcher_foreground.png`:
- `mdpi`: 108x108 px (logo ~86px)
- `hdpi`: 162x162 px (logo ~130px)
- `xhdpi`: 216x216 px (logo ~173px)
- `xxhdpi`: 324x324 px (logo ~259px)
- `xxxhdpi`: 432x432 px (logo ~346px)

### 3. Background Color
- **[MODIFY] [colors.xml](file:///home/silvercipher/Projects/Bread/app/src/main/res/values/colors.xml)**:
    - Ensure a `ic_launcher_background` color is defined (set to `#000000` to match the logo).

## Verification Plan

### Automated Tests
- Run `./gradlew :app:packageDebugResources` to verify the new resource structure.

### Manual Verification
- Deploy to an Android 8.0+ device/emulator.
- Verify the app icon no longer has a white border and the logo is perfectly centered and scaled.
