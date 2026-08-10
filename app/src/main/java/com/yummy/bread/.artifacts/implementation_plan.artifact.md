# Implementation Plan - Dashboard Visual Enhancements

Refine the Dashboard and Transaction visuals with better color coding and status indicators while maintaining the 3D glass aesthetic.

## Proposed Changes

### 1. Color Palette Refinement
- **[MODIFY] [Color.kt](file:///home/silvercipher/Projects/Bread/app/src/main/java/com/yummy/bread/ui/theme/Color.kt)**:
    - Define `VibrantGreen` (`#32D74B`) and `VibrantRed` (`#FF453A`) for clear status indicators.

### 2. Transaction Item Logic & Colors
- **[MODIFY] [Components.kt](file:///home/silvercipher/Projects/Bread/app/src/main/java/com/yummy/bread/ui/components/Components.kt)**:
    - Update `TransactionItem` to correctly prefix amounts with `+` for income and `-` for expenses.
    - Apply `VibrantGreen` to income amounts and `VibrantRed` to expense amounts.

### 3. Bento Grid Visual Indicators
- **[MODIFY] [DashboardScreen.kt](file:///home/silvercipher/Projects/Bread/app/src/main/java/com/yummy/bread/ui/screens/DashboardScreen.kt)**:
    - **Monthly Income Card**: Add an `ArrowUpward` icon inside a `VibrantGreen` outlined transparent circle.
    - **Monthly Spend Card**: Add an `ArrowDownward` icon inside a `VibrantRed` outlined transparent circle.
    - Ensure these indicators are positioned elegantly within the existing 3D glass cards.

## Verification Plan

### Automated Tests
- Build the project to ensure no compilation errors.

### Manual Verification
1.  Open the Dashboard.
2.  Observe the "Monthly Income" card and verify the green upward arrow indicator.
3.  Observe the "Monthly Spend" card and verify the red downward arrow indicator.
4.  Check the "Recent Transactions" list:
    *   Verify income has a `+` sign and is green.
    *   Verify expenses have a `-` sign and are red.
5.  Confirm that the 3D glass effect (gradients, borders, blur) remains intact on all cards.
