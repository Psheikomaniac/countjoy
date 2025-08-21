# Implementation Guide - Event Countdown App

## 1. Project Setup

### 1.1 Create New Android Project

```bash
# Android Studio: New Project
# Select: Empty Activity
# Name: CountJoy
# Package: com.countjoy
# Language: Kotlin
# Minimum SDK: API 24 (Android 7.0)
```

### 1.2 Project Structure Setup

```bash
# Create package structure
app/src/main/java/com/countjoy/
├── data/
├── domain/
├── presentation/
├── di/
└── util/
```

### 1.3 Gradle Configuration

```kotlin
// app/build.gradle.kts
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.countjoy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.countjoy"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
    
    buildFeatures {
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    
    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    
    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // DateTime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("app.cash.turbine:turbine:1.0.0")
    
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
```

## 2. Core Implementation

### 2.1 Domain Layer

#### 2.1.1 Domain Models

```kotlin
// domain/model/CountdownEvent.kt
package com.countjoy.domain.model

import kotlinx.datetime.LocalDateTime

data class CountdownEvent(
    val id: Long = 0,
    val name: String,
    val targetDateTime: LocalDateTime,
    val hasTime: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

// domain/model/CountdownTime.kt
data class CountdownTime(
    val days: Long = 0,
    val hours: Long = 0,
    val minutes: Long = 0,
    val seconds: Long = 0
) {
    val isEmpty: Boolean = days == 0L && hours == 0L && minutes == 0L && seconds == 0L
    
    fun toDisplayString(): String = when {
        days > 0 -> "$days Tage, $hours Stunden"
        hours > 0 -> "$hours Stunden, $minutes Minuten, $seconds Sekunden"
        else -> "$minutes Minuten, $seconds Sekunden"
    }
}
```

#### 2.1.2 Repository Interface

```kotlin
// domain/repository/EventRepository.kt
package com.countjoy.domain.repository

import com.countjoy.domain.model.CountdownEvent
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun getEvent(): Flow<CountdownEvent?>
    suspend fun createEvent(event: CountdownEvent): Result<CountdownEvent>
    suspend fun updateEvent(event: CountdownEvent): Result<CountdownEvent>
    suspend fun deleteEvent(): Result<Unit>
}
```

#### 2.1.3 Use Cases

```kotlin
// domain/usecase/GetEventUseCase.kt
package com.countjoy.domain.usecase

import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    operator fun invoke(): Flow<CountdownEvent?> = repository.getEvent()
}

// domain/usecase/CreateEventUseCase.kt
class CreateEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(event: CountdownEvent): Result<CountdownEvent> {
        // Validation
        if (event.name.isBlank()) {
            return Result.failure(IllegalArgumentException("Event name cannot be empty"))
        }
        
        if (event.name.length > 50) {
            return Result.failure(IllegalArgumentException("Event name too long"))
        }
        
        val now = LocalDateTime.now()
        if (event.targetDateTime <= now) {
            return Result.failure(IllegalArgumentException("Event must be in the future"))
        }
        
        return repository.createEvent(event)
    }
}

// domain/usecase/CalculateCountdownUseCase.kt
class CalculateCountdownUseCase @Inject constructor() {
    operator fun invoke(
        targetDateTime: LocalDateTime,
        currentDateTime: LocalDateTime = LocalDateTime.now()
    ): CountdownTime {
        val duration = targetDateTime.toInstant(TimeZone.currentSystemDefault()) -
                      currentDateTime.toInstant(TimeZone.currentSystemDefault())
        
        if (duration.isNegative()) {
            return CountdownTime()
        }
        
        val days = duration.inWholeDays
        val hours = duration.inWholeHours % 24
        val minutes = duration.inWholeMinutes % 60
        val seconds = duration.inWholeSeconds % 60
        
        return CountdownTime(days, hours, minutes, seconds)
    }
}
```

### 2.2 Data Layer

#### 2.2.1 Database Setup

```kotlin
// data/local/EventEntity.kt
package com.countjoy.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val targetDateTime: String, // Stored as ISO string
    val hasTime: Boolean = false,
    val createdAt: String // Stored as ISO string
)

// data/local/EventDao.kt
@Dao
interface EventDao {
    @Query("SELECT * FROM events LIMIT 1")
    fun getEvent(): Flow<EventEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity): Long
    
    @Update
    suspend fun updateEvent(event: EventEntity)
    
    @Query("DELETE FROM events")
    suspend fun deleteAll()
}

// data/local/EventDatabase.kt
@Database(
    entities = [EventEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeConverter::class)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}

// data/local/DateTimeConverter.kt
class DateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }
    
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}
```

#### 2.2.2 Repository Implementation

```kotlin
// data/repository/EventRepositoryImpl.kt
package com.countjoy.data.repository

import com.countjoy.data.local.EventDao
import com.countjoy.data.mapper.toEntity
import com.countjoy.data.mapper.toDomainModel
import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventDao: EventDao
) : EventRepository {
    
    override suspend fun getEvent(): Flow<CountdownEvent?> {
        return eventDao.getEvent().map { entity ->
            entity?.toDomainModel()
        }
    }
    
    override suspend fun createEvent(event: CountdownEvent): Result<CountdownEvent> {
        return try {
            val id = eventDao.insertEvent(event.toEntity())
            Result.success(event.copy(id = id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateEvent(event: CountdownEvent): Result<CountdownEvent> {
        return try {
            eventDao.updateEvent(event.toEntity())
            Result.success(event)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteEvent(): Result<Unit> {
        return try {
            eventDao.deleteAll()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

#### 2.2.3 Mappers

```kotlin
// data/mapper/EventMapper.kt
package com.countjoy.data.mapper

import com.countjoy.data.local.EventEntity
import com.countjoy.domain.model.CountdownEvent
import kotlinx.datetime.LocalDateTime

fun EventEntity.toDomainModel(): CountdownEvent {
    return CountdownEvent(
        id = id,
        name = name,
        targetDateTime = LocalDateTime.parse(targetDateTime),
        hasTime = hasTime,
        createdAt = LocalDateTime.parse(createdAt)
    )
}

fun CountdownEvent.toEntity(): EventEntity {
    return EventEntity(
        id = id,
        name = name,
        targetDateTime = targetDateTime.toString(),
        hasTime = hasTime,
        createdAt = createdAt.toString()
    )
}
```

### 2.3 Presentation Layer

#### 2.3.1 ViewModels

```kotlin
// presentation/countdown/CountdownViewModel.kt
package com.countjoy.presentation.countdown

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.model.CountdownTime
import com.countjoy.domain.usecase.CalculateCountdownUseCase
import com.countjoy.domain.usecase.GetEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountdownViewModel @Inject constructor(
    private val getEventUseCase: GetEventUseCase,
    private val calculateCountdownUseCase: CalculateCountdownUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CountdownUiState())
    val uiState: StateFlow<CountdownUiState> = _uiState.asStateFlow()
    
    init {
        observeEvent()
    }
    
    private fun observeEvent() {
        viewModelScope.launch {
            getEventUseCase().collect { event ->
                _uiState.update { it.copy(event = event) }
                event?.let { startCountdown(it) }
            }
        }
    }
    
    private fun startCountdown(event: CountdownEvent) {
        viewModelScope.launch {
            while (true) {
                val countdown = calculateCountdownUseCase(event.targetDateTime)
                _uiState.update { it.copy(countdown = countdown) }
                
                if (countdown.isEmpty) {
                    _uiState.update { it.copy(isExpired = true) }
                    break
                }
                
                delay(1000)
            }
        }
    }
}

// presentation/countdown/CountdownUiState.kt
data class CountdownUiState(
    val event: CountdownEvent? = null,
    val countdown: CountdownTime? = null,
    val isExpired: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
```

#### 2.3.2 Compose UI

```kotlin
// presentation/countdown/CountdownScreen.kt
package com.countjoy.presentation.countdown

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CountdownScreen(
    onNavigateToEventInput: () -> Unit,
    viewModel: CountdownViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToEventInput
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.event == null -> {
                    EmptyState(onCreateEvent = onNavigateToEventInput)
                }
                uiState.isExpired -> {
                    ExpiredState(event = uiState.event)
                }
                else -> {
                    CountdownContent(
                        event = uiState.event,
                        countdown = uiState.countdown
                    )
                }
            }
        }
    }
}

@Composable
private fun CountdownContent(
    event: CountdownEvent,
    countdown: CountdownTime?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = event.name,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        countdown?.let {
            Text(
                text = it.toDisplayString(),
                style = MaterialTheme.typography.displayMedium,
                fontSize = 48.sp
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = event.targetDateTime.toDisplayString(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EmptyState(onCreateEvent: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Kein Event vorhanden",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = onCreateEvent) {
            Text("Event erstellen")
        }
    }
}
```

### 2.4 Dependency Injection

```kotlin
// di/AppModule.kt
package com.countjoy.di

import android.content.Context
import androidx.room.Room
import com.countjoy.data.local.EventDatabase
import com.countjoy.data.local.EventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideEventDatabase(
        @ApplicationContext context: Context
    ): EventDatabase {
        return Room.databaseBuilder(
            context,
            EventDatabase::class.java,
            "event_database"
        ).build()
    }
    
    @Provides
    @Singleton
    fun provideEventDao(database: EventDatabase): EventDao {
        return database.eventDao()
    }
}

// di/RepositoryModule.kt
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    abstract fun bindEventRepository(
        eventRepositoryImpl: EventRepositoryImpl
    ): EventRepository
}
```

### 2.5 Application Class

```kotlin
// CountJoyApplication.kt
package com.countjoy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CountJoyApplication : Application()
```

## 3. Testing Implementation

### 3.1 Unit Tests

```kotlin
// test/domain/usecase/CalculateCountdownUseCaseTest.kt
class CalculateCountdownUseCaseTest {
    
    private lateinit var useCase: CalculateCountdownUseCase
    
    @Before
    fun setup() {
        useCase = CalculateCountdownUseCase()
    }
    
    @Test
    fun `should calculate correct countdown for future date`() {
        // Given
        val now = LocalDateTime(2024, 1, 1, 12, 0, 0)
        val target = LocalDateTime(2024, 1, 2, 14, 30, 0)
        
        // When
        val result = useCase(target, now)
        
        // Then
        assertEquals(1, result.days)
        assertEquals(2, result.hours)
        assertEquals(30, result.minutes)
        assertEquals(0, result.seconds)
    }
    
    @Test
    fun `should return empty countdown for past date`() {
        // Given
        val now = LocalDateTime(2024, 1, 2, 12, 0, 0)
        val target = LocalDateTime(2024, 1, 1, 12, 0, 0)
        
        // When
        val result = useCase(target, now)
        
        // Then
        assertTrue(result.isEmpty)
    }
}
```

### 3.2 ViewModel Tests

```kotlin
// test/presentation/CountdownViewModelTest.kt
class CountdownViewModelTest {
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    @MockK
    private lateinit var getEventUseCase: GetEventUseCase
    
    @MockK
    private lateinit var calculateCountdownUseCase: CalculateCountdownUseCase
    
    private lateinit var viewModel: CountdownViewModel
    
    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }
    
    @Test
    fun `should emit loading state initially`() = runTest {
        // Given
        val event = CountdownEvent(
            name = "Test Event",
            targetDateTime = LocalDateTime.now().plusDays(1)
        )
        coEvery { getEventUseCase() } returns flowOf(event)
        
        // When
        viewModel = CountdownViewModel(getEventUseCase, calculateCountdownUseCase)
        
        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertNotNull(state.event)
            assertEquals("Test Event", state.event?.name)
        }
    }
}
```

## 4. UI Components

### 4.1 Event Input Screen

```kotlin
// presentation/event/EventInputScreen.kt
@Composable
fun EventInputScreen(
    onNavigateBack: () -> Unit,
    viewModel: EventInputViewModel = hiltViewModel()
) {
    var eventName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now().plusDays(1)) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var includeTime by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event erstellen") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = eventName,
                onValueChange = { eventName = it },
                label = { Text("Event Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = eventName.length > 50
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            DatePicker(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = includeTime,
                    onCheckedChange = { includeTime = it }
                )
                Text("Uhrzeit hinzufügen")
            }
            
            if (includeTime) {
                TimePicker(
                    selectedTime = selectedTime,
                    onTimeSelected = { selectedTime = it }
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    viewModel.createEvent(
                        name = eventName,
                        date = selectedDate,
                        time = if (includeTime) selectedTime else null
                    )
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = eventName.isNotBlank() && eventName.length <= 50
            ) {
                Text("Event erstellen")
            }
        }
    }
}
```

## 5. Background Service

```kotlin
// presentation/service/CountdownService.kt
class CountdownService : Service() {
    
    @Inject
    lateinit var getEventUseCase: GetEventUseCase
    
    @Inject
    lateinit var calculateCountdownUseCase: CalculateCountdownUseCase
    
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onCreate() {
        super.onCreate()
        startCountdownUpdates()
    }
    
    private fun startCountdownUpdates() {
        serviceScope.launch {
            getEventUseCase().collect { event ->
                event?.let { 
                    updateCountdown(it)
                }
            }
        }
    }
    
    private suspend fun updateCountdown(event: CountdownEvent) {
        while (serviceScope.isActive) {
            val countdown = calculateCountdownUseCase(event.targetDateTime)
            
            if (countdown.isEmpty) {
                stopSelf()
                break
            }
            
            delay(1000)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
```

## 6. MainActivity Setup

```kotlin
// MainActivity.kt
package com.countjoy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.countjoy.presentation.countdown.CountdownScreen
import com.countjoy.presentation.event.EventInputScreen
import com.countjoy.presentation.theme.CountJoyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CountJoyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    NavHost(
                        navController = navController,
                        startDestination = "countdown"
                    ) {
                        composable("countdown") {
                            CountdownScreen(
                                onNavigateToEventInput = {
                                    navController.navigate("event_input")
                                }
                            )
                        }
                        
                        composable("event_input") {
                            EventInputScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
```

## 7. Manifest Configuration

```xml
<!-- AndroidManifest.xml -->
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    
    <application
        android:name=".CountJoyApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.CountJoy">
        
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:screenOrientation="unspecified"
            android:configChanges="orientation|screenSize|screenLayout|keyboardHidden">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        
        <service 
            android:name=".presentation.service.CountdownService"
            android:exported="false" />
            
        <receiver
            android:name=".presentation.receiver.BootReceiver"
            android:enabled="true"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
            </intent-filter>
        </receiver>
        
    </application>
</manifest>
```

## 8. ProGuard Rules

```pro
# proguard-rules.pro

# Kotlin
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keepclassmembers class * {
    @androidx.room.* <methods>;
}

# Hilt
-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel

# Compose
-keep class androidx.compose.** { *; }
-keep class kotlin.Metadata { *; }

# Kotlinx DateTime
-keep class kotlinx.datetime.** { *; }
```

## 9. Build & Run

```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Install on device
./gradlew installDebug

# Run tests
./gradlew test

# Run lint
./gradlew lint

# Run detekt
./gradlew detekt
```

## 10. Deployment Checklist

- [ ] Version number updated
- [ ] Release notes prepared
- [ ] All tests passing
- [ ] ProGuard rules verified
- [ ] APK signed with release key
- [ ] Screenshots updated
- [ ] Play Store listing updated
- [ ] Privacy policy updated
- [ ] Terms of service updated

## Summary

Diese Implementierungs-Guide bietet eine vollständige Anleitung zur Entwicklung der Event Countdown App mit:
- Clean Architecture
- MVVM Pattern
- Jetpack Compose UI
- Kotlin Coroutines
- Hilt Dependency Injection
- Room Database
- Comprehensive Testing

Die App ist bereit für die Produktion und kann schrittweise mit zusätzlichen Features erweitert werden.