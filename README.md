# Currency Converter & Communication App

 📌 Overview
The Currency Converter & Communication App is a native Android application engineered to simplify global transitions. It combines an instant currency conversion engine with a real-time global chat pool, enabling travelers, international business managers, and cross-border users to eliminate conversion confusion while exchanging on-the-ground local insights.

🚀 Key Features

 **Instant Currency Conversion:** Real-time mathematical engine supporting seven major global currencies (USD, EUR, GBP, LBP, IQD, IRR, and TRY).
 **Real-Time Global Chat Pool:** Synchronized multi-user chat room built on top of Firebase Realtime Database with custom message layouts.
 **Granular Message Controls:** 
  * **Individual Message Deletion (Unsend):** Long-click on sent messages to delete them from Firebase (ownership-verified).
  * **Administrative Chat Clear:** Option to wipe the conversation history for all users.
 **Persistent User Profiles:** Stores user credentials, country details, and gallery profile pictures.
  * **URI Persistence:** Uses `takePersistableUriPermission` to ensure profile pictures remain visible after app restarts.
  * **Long-Click Reset:** Long-press the profile photo to revert to default settings.
 **Secure Authentication:** User registration, login validation, and session management via Firebase Authentication.

 🛠️ Architecture & Tech Stack

 **Language:** Java
 **UI/UX:** XML (ConstraintLayout, CardView, ShapeableImageView, RecyclerView)
 **Architecture Pattern:** Model-View-Controller (MVC)
 **Cloud Backend Services:**
  * **Firebase Authentication:** Handles secure user login and session disposal.
  * **Firebase Realtime Database:** Provides live data synchronization across devices for chat messages and profile details.

📱 Screen Breakdown

### 1. Welcome Screen (`welcome_activity.xml` / `welcomeActivity.java`)
* Features the app's mission statement and a single call-to-action button.
* Clears itself from the back stack upon proceeding to optimize memory usage.

### 2. Authentication Screen (`activity_main.xml` / `MainActivity.java`)
* Elevated `CardView` login form with input validation for email and password.
* Integrates `FirebaseAuth.signInWithEmailAndPassword()` to grant secure user access.

### 3. Currency Converter (`converter_activity.xml` / `converter.java`)
* Dual `Spinner` widgets to select target currencies.
* Computes conversions using standard conversion math:
  Result = (Amount / Rate_From) * Rate_To
* Options menu provides access to Profile and Global Chat screens.

### 4. User Profile (`profile_activity.xml` / `profileActivity.java`)
* Utilizes `ShapeableImageView` for circular avatar formatting.
* Employs `dbRef.updateChildren()` for selective non-destructive updates to Firebase.

### 5. Chat Pool (`activity_chat_pool.xml` / `ChatPoolActivity.java` / `ChatAdapter.java`)
* Dynamic `RecyclerView` using multi-view types (`TYPE_SENT` aligned right, `TYPE_RECEIVED` aligned left).
* Real-time sync powered by `addValueEventListener` with automatic scroll-to-bottom handling.


