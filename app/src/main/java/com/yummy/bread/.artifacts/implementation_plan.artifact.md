# Implementation Plan - Profile Deletion with Confirmation

Add the ability for users to delete profiles from the "Who's spending?" screen, including a confirmation dialog to prevent accidental data loss.

## Proposed Changes

### 1. Data Layer
- **[MODIFY] [BreadRepository.kt](file:///home/silvercipher/Projects/Bread/app/src/main/java/com/yummy/bread/data/BreadRepository.kt)**:
    - Add `deleteProfileData(profileId: String)` to remove all shared preferences associated with a specific profile.

### 2. ViewModel
- **[MODIFY] [BreadViewModel.kt](file:///home/silvercipher/Projects/Bread/app/src/main/java/com/yummy/bread/BreadViewModel.kt)**:
    - Add `deleteProfile(profile: Profile)` method.
    - This method will:
        1. Remove the profile from the `profiles` list in `BreadUiState`.
        2. Call `repository.saveProfiles()` with the updated list.
        3. Call `repository.deleteProfileData(profile.id)`.
        4. Clear `lastActiveProfileId` if the deleted profile was the last active one.

### 3. UI Layer
- **[MODIFY] [ProfileSelectorScreen.kt](file:///home/silvercipher/Projects/Bread/app/src/main/java/com/yummy/bread/ui/screens/ProfileSelectorScreen.kt)**:
    - Add state to track the profile currently being considered for deletion (for the confirmation dialog).
    - Add an `IconButton` with `Icons.Default.Delete` to each `ProfileCard`.
    - Implement an `AlertDialog` that appears when the delete icon is clicked.
    - The dialog will ask "Delete [Profile Name]?" and warn about data loss.
    - Confirming will call `viewModel.deleteProfile(profile)`.

## Verification Plan

### Automated Tests
- Build the project to ensure no compilation errors.

### Manual Verification
1. Open the "Who's spending?" screen.
2. Click the delete icon on a profile.
3. Verify the confirmation dialog appears with the correct profile name.
4. Click "Cancel" and verify the profile is NOT deleted.
5. Click the delete icon again and click "Delete".
6. Verify the profile is removed from the list.
7. Restart the app to verify the profile remains deleted (persisted in SharedPreferences).
