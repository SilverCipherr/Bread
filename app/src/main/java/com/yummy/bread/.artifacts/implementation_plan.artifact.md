# Implementation Plan - Vibrant Full-Screen Immersive Background

The user reports that the background animation appears as a circular blob rather than a full-screen wash, and the colors are not vibrant enough. This is due to using `drawCircle` (which defaults to a small radius) instead of `drawRect` to fill the viewport with gradients.

## Proposed Changes

### 1. Restore Vibrant Colors
- **[MODIFY] [Color.kt](file:///home/silvercipher/Projects/Bread/app/src/main/java/com/yummy/bread/ui/theme/Color.kt)**: Re-introduce the high-contrast iOS 17 vibrant colors (`Indigo`, `Sky`, `Rose`, `Green`) with full opacity for the base definitions, allowing controlled transparency in the background layer.

### 2. Fix Full-Screen Blending
- **[MODIFY] [GlassBackground.kt](file:///home/silvercipher/Projects/Bread/app/src/main/java/com/yummy/bread/ui/components/GlassBackground.kt)**:
    - Replace `drawCircle` with `drawRect`. This ensures each gradient layer fills the entire screen, allowing the radial gradients to fade out naturally to the edges rather than being clipped into a circle shape.
    - Increase the `radius` of the radial gradients to be significantly larger than the screen dimensions to create a soft, blended "wash" effect.
    - Use all four vibrant colors for a richer spectrum.

## Verification Plan

### Automated Tests
- Build the project to ensure no syntax errors.

### Manual Verification
- Verify that the background no longer shows distinct circular boundaries.
- Ensure the colors are vibrant and blend into each other across the entire screen.
- Confirm the animation remains fluid and immersive across all screens.
