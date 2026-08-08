# Walkthrough - Centralized Glass Color Control

I have centralized the color control for all glassy components in the app. You can now change the tint of every card and bar by editing a single line.

## Changes Made

### 1. Master Color Switch
- **[Modified Color.kt](file:///home/silvercipher/Projects/Bread/app/src/main/java/com/yummy/bread/ui/theme/Color.kt)**: Added `val GlassColor = Color.White`. This is now the source of truth for all glassy effects.

### 2. Linked Glass Modifiers
- **[Modified GlassModifier.kt](file:///home/silvercipher/Projects/Bread/app/src/main/java/com/yummy/bread/ui/components/GlassModifier.kt)**:
    - Updated `glassPanel` and `glassPanelHeavy` to use `GlassColor` for their backgrounds and borders.
    - Added the necessary import to link these files.

## Verification Results

### Build Success
- **Build Status**: `Build finished successfully.`

> [!TIP]
> To change the look of your app's glass, simply go to [Color.kt](file:///home/silvercipher/Projects/Bread/app/src/main/java/com/yummy/bread/ui/theme/Color.kt) and change `GlassColor`. All components will update automatically!
