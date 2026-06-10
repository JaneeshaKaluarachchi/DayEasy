# DayEasy - Daily Wellness Tracker App

## App Description

**DayEasy** is a comprehensive wellness tracking application designed to help users maintain and improve their daily well-being through habit tracking, mood journaling, and hydration monitoring. Built with modern Android development practices using Kotlin, the app provides an intuitive and user-friendly interface that makes wellness tracking effortless and engaging.

---

## Overview

DayEasy is your personal wellness companion that combines multiple aspects of daily health management into one seamless experience. The app focuses on three core wellness pillars: habit formation, emotional awareness, and hydration tracking, all presented through a clean, modern interface with easy navigation.

---

## Key Features

### 1. **Daily Habit Tracker**
The habit tracking feature allows users to create, manage, and monitor their daily wellness habits. Users can:
- Add custom habits with personalized names
- Mark habits as complete or incomplete each day
- Edit existing habits to adapt to changing goals
- Delete habits that are no longer relevant
- View real-time progress with a visual progress bar
- Track completion percentage for the day
- Receive motivational messages upon completing habits
- Access all habit data through an intuitive recycler view interface

The habit tracker helps users build consistency and accountability in their daily routines, with persistent data storage ensuring progress is never lost.

### 2. **Mood Journal with Emoji Selector**
The mood journaling feature provides an engaging way to track emotional well-being. Key capabilities include:
- **8 Mood Options**: Very Happy (😄), Happy (😊), Neutral (😐), Sad (😢), Angry (😡), Loving (😍), Tired (😴), and Excited (🤩)
- Visual emoji-based mood selection for quick and intuitive logging
- Optional notes to capture thoughts, feelings, or context for each mood entry
- Timestamp for every mood entry to track emotional patterns over time
- Comprehensive mood history displaying all past entries
- Share functionality to export mood entries
- Delete option to manage mood history
- Numerical mood values (1-5) for analytics purposes

This feature promotes emotional awareness and helps users identify patterns in their mental well-being over time.

### 3. **Hydration Reminder & Tracker**
The hydration feature ensures users maintain proper water intake throughout the day:
- **Water Intake Tracking**: Log glasses of water consumed with a simple tap
- **Customizable Daily Goal**: Set personal hydration targets
- **Visual Progress**: Water wave animation showing daily progress
- **Smart Reminders**: Configurable notification system with multiple interval options:
  - Every 30 minutes
  - Every 1 hour
  - Every 2 hours
  - Every 3 hours
- **Reset Functionality**: Clear daily count to start fresh
- **Goal Management**: Adjust water goals through the settings
- **Persistent Storage**: Track hydration progress across app sessions

The hydration tracker uses Android WorkManager for reliable background notifications, ensuring users never miss their water intake reminders.

### 4. **Analytics & Mood Trends**
The analytics section provides valuable insights into emotional patterns:
- **7-Day Mood Chart**: Visual line graph displaying mood trends over the past week
- **Interactive Charts**: Built using MPAndroidChart library for smooth, professional visualizations
- **Pattern Recognition**: Identify emotional highs and lows over time
- **Data-Driven Insights**: Make informed decisions about wellness practices
- **Color-Coded Display**: Easy-to-read graphs with clear axis labels
- **Touch Interactions**: Zoom and pan capabilities for detailed analysis

This advanced feature helps users understand their emotional journey and make positive changes based on observed patterns.

### 5. **User Authentication System**
Secure and personalized user experience with:
- **Sign Up**: Create new accounts with name, email, and password
- **Login**: Secure authentication for returning users
- **Account Management**: Store user credentials securely using SharedPreferences
- **Personalized Experience**: Display user name in settings and throughout the app
- **Logout Functionality**: Securely sign out and return to login screen

### 6. **Settings & Preferences**
Comprehensive settings management including:
- **Account Information Display**: View current user name and email
- **Current Settings Overview**: See all active configurations at a glance
- **Data Management**: Reset all app data when needed
- **Logout Option**: Sign out with confirmation dialog
- **User-Friendly Interface**: Clear organization of all settings

### 7. **Home Screen Widget** (Advanced Feature)
Quick access to wellness information directly from the home screen:
- Display today's habit completion percentage
- Show motivational messages based on progress
- One-tap access to open the full app
- Real-time updates reflecting current progress
- Minimal battery impact with efficient updates

---

## Technical Implementation

### Architecture & Design Patterns
- **MVVM-inspired Architecture**: Clean separation of concerns
- **Fragment-based Navigation**: Smooth transitions between app sections
- **Bottom Navigation**: Easy access to all major features
- **Material Design**: Modern, intuitive user interface following Google's design guidelines

### Data Persistence
- **SharedPreferences**: Efficient storage for:
  - User authentication data
  - Habit lists and completion status
  - Mood entries and history
  - Hydration tracking data
  - App settings and preferences
- **JSON Serialization**: Complex data structures stored as JSON strings
- **Persistent Data**: All user data saved locally on the device

### Advanced Android Features
- **WorkManager**: Reliable background task scheduling for hydration reminders
- **Notifications**: Custom notifications with actions and channels
- **App Widgets**: Home screen widget for quick habit tracking
- **Material Components**: Modern UI components for enhanced user experience
- **View Binding**: Type-safe view access eliminating findViewById calls

### Libraries & Dependencies
- **MPAndroidChart**: Professional charting library for mood analytics
- **Material Design Components**: Google's UI component library
- **AndroidX Libraries**: Modern Android development libraries
- **WorkManager**: Background task management
- **RecyclerView**: Efficient list displays

---

## User Interface Design

### Design Philosophy
DayEasy features a clean, modern, and user-friendly interface designed for ease of use and visual appeal:

- **Custom Color Scheme**: Cohesive color palette with primary purple tones (#6200EE) creating a calming, professional appearance
- **Consistent Typography**: Readable fonts with appropriate sizing for different content types
- **Intuitive Navigation**: Bottom navigation bar with custom icons for each section:
  - 🏠 Habit Tracker (habit.png)
  - 😊 Mood Journal (mood.png)
  - 💧 Hydration (hydration.png)
  - 📊 Analytics (analytics.png)
  - ⚙️ Settings (settings.png)
- **Visual Feedback**: Animations and color changes for user interactions
- **Responsive Layout**: Adapts to different screen sizes and orientations
- **Empty States**: Helpful messages when no data is available
- **Progress Indicators**: Visual progress bars and charts for motivation

### Screen Organization
1. **Splash Screen**: Welcome screen with app logo (dayeasylogo.png)
2. **Authentication Screens**: Clean login and signup interfaces
3. **Main App**: Five main sections accessible via bottom navigation
4. **Dialogs**: Contextual dialogs for adding/editing data

---

## User Experience Features

### Motivational Elements
- Congratulatory messages when completing habits
- Visual progress indicators showing achievement
- Emoji-based mood tracking for emotional engagement
- Streak tracking for consistency building

### Data Management
- Easy data entry with minimal taps required
- Edit and delete capabilities for all user-created content
- Confirmation dialogs preventing accidental data loss
- Share functionality for mood entries
- Bulk reset options in settings

### Accessibility & Usability
- Large, tappable buttons and icons
- Clear visual hierarchy
- Descriptive labels and helper text
- Snackbar notifications for user feedback
- Smooth animations without overwhelming users

---

## App Benefits

### For Daily Wellness
- **Habit Building**: Develop and maintain positive daily routines
- **Emotional Awareness**: Track and understand mood patterns
- **Health Monitoring**: Ensure adequate hydration throughout the day
- **Progress Tracking**: See improvements over time with analytics

### For Users
- **All-in-One Solution**: Multiple wellness features in a single app
- **Privacy-Focused**: All data stored locally on device
- **No Internet Required**: Works completely offline
- **Free to Use**: No subscriptions or in-app purchases
- **Lightweight**: Minimal storage and battery impact

---

## Target Audience

DayEasy is designed for:
- Individuals seeking to improve daily wellness habits
- People interested in mood tracking and emotional awareness
- Users wanting to maintain proper hydration
- Anyone looking for a simple, effective wellness companion
- Students, professionals, and health-conscious individuals

---

## Future Potential

While DayEasy already offers comprehensive wellness tracking, potential future enhancements could include:
- Cloud backup and sync across devices
- Social features for sharing progress with friends
- Additional analytics and insights
- More customization options
- Integration with fitness trackers
- Export functionality for data backup
- Dark mode theme
- Multiple language support

---

## Conclusion

DayEasy represents a modern approach to personal wellness management, combining essential health tracking features into an accessible, user-friendly Android application. Through thoughtful design, robust technical implementation, and focus on user experience, the app successfully helps users build healthier daily habits, understand their emotional patterns, and maintain proper hydration—all while providing an enjoyable and motivating user experience.

The app demonstrates proficiency in Android development best practices, including proper use of fragments, data persistence, background tasks, widgets, and third-party libraries, making it a comprehensive showcase of modern mobile application development.

---

## Technical Specifications

- **Platform**: Android
- **Language**: Kotlin
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: Latest Android version
- **Architecture**: Fragment-based with SharedPreferences persistence
- **Build System**: Gradle with Kotlin DSL
- **Key Permissions**: Notifications, Exact Alarms

---

*DayEasy - Making wellness tracking simple, one day at a time.*

