# CountJoy ProGuard Rules
# Version: 1.0.0

# Keep line numbers for better crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep annotations
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions

# ============================================
# Kotlin
# ============================================
-keep class kotlin.Metadata { *; }
-keep class kotlin.reflect.** { *; }
-dontwarn kotlin.reflect.**
-keep class kotlin.coroutines.** { *; }

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# ============================================
# Android Components
# ============================================

# Keep Application class
-keep class com.countjoy.CountJoyApplication { *; }

# Keep Activities
-keep class com.countjoy.MainActivity { *; }

# Keep Services
-keep class com.countjoy.service.** { *; }

# Keep Receivers
-keep class com.countjoy.receiver.** { *; }

# ============================================
# Jetpack Compose
# ============================================
-keep class androidx.compose.** { *; }
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.material3.** { *; }
-keep @androidx.compose.runtime.Composable class * { *; }

# Keep Compose State classes
-keepclassmembers class * {
    @androidx.compose.runtime.* <fields>;
}

# ============================================
# Hilt/Dagger
# ============================================
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }
-keep @dagger.Module class * { *; }
-keep @dagger.hilt.InstallIn class * { *; }
-keep @javax.inject.Inject class * { *; }

# Keep Hilt generated classes
-keep class **_HiltModules { *; }
-keep class **_HiltModules$* { *; }
-keep class **_Factory { *; }
-keep class **_Factory$* { *; }
-keep class **_Impl { *; }
-keep class **_Impl$* { *; }
-keep class **_MembersInjector { *; }

# ============================================
# Room Database
# ============================================
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao interface *
-keep @androidx.room.Database class * { *; }
-keepclassmembers class * {
    @androidx.room.* <methods>;
}

# Keep database entities
-keep class com.countjoy.data.local.entity.** { *; }
-keep class com.countjoy.data.local.dao.** { *; }
-keep class com.countjoy.data.local.CountJoyDatabase { *; }
-keep class com.countjoy.data.local.converter.** { *; }

# ============================================
# Domain Models
# ============================================
-keep class com.countjoy.domain.model.** { *; }
-keep class com.countjoy.domain.repository.** { *; }

# ============================================
# Data Classes
# ============================================
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep data classes
-keep class * {
    @kotlinx.parcelize.Parcelize *;
}

# ============================================
# Encrypted SharedPreferences
# ============================================
-keep class androidx.security.crypto.** { *; }
-keep class com.google.crypto.tink.** { *; }

# ============================================
# Navigation
# ============================================
-keep class androidx.navigation.** { *; }
-keep interface androidx.navigation.** { *; }

# ============================================
# WorkManager (if used)
# ============================================
-keep class androidx.work.** { *; }
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.ListenableWorker

# ============================================
# Gson/Moshi/Kotlinx Serialization (if used)
# ============================================
-keepattributes Signature
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe { *; }

# ============================================
# OkHttp/Retrofit (if used)
# ============================================
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# ============================================
# Glide/Coil (if used for images)
# ============================================
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
    <init>(...);
}

# ============================================
# Remove Logging in Release
# ============================================
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
}

# ============================================
# Optimization
# ============================================
-optimizationpasses 5
-dontpreverify
-repackageclasses ''
-allowaccessmodification
-mergeinterfacesaggressively

# ============================================
# Warnings
# ============================================
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe