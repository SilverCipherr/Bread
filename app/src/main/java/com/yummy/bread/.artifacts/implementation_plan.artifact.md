# Implementation Plan - Centralized Glass Color Control

The goal is to enable changing the color of all glassy components (cards, top bar, bottom bar) across the entire app by editing just a single line of code.

## Proposed Changes

### 1. Centralize the Glass Color source
- **[MODIFY] [Color.kt](file:///home/silvercipher/Projects/Bread/app/src/main/java/com/yummy/bread/ui/theme/Color.kt)**:
    - Add a new variable `val GlassColor = Color.White`. This will be the "master switch" for your glassy components.

### 2. Link the Modifiers to the Master Switch
- **[MODIFY] [GlassModifier.kt](file:///home/silvercipher/Projects/Bread/app/src/main/java/com/yummy/bread/ui/components/GlassModifier.kt)**:
    - Update the `glassPanel` and `glassPanelHeavy` functions to use `GlassColor` instead of the hardcoded `Color.White` for both the background and the border gradients.

## Verification Plan

### Manual Verification
- Change `GlassColor` in `Color.kt` to a different color (e.g., `Color.Blue`) and verify that all cards and bars in the app immediately reflect this change.
- Revert it back to `Color.White` once confirmed.
