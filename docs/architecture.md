# Event Countdown App - Architecture Document

## 1. Overview

Diese Dokumentation beschreibt die Clean Architecture für die Event Countdown App unter Verwendung moderner Android-Entwicklungspraktiken mit Kotlin, Jetpack Compose und MVVM-Pattern.

## 2. Architecture Pattern

### 2.1 Clean Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                     Presentation Layer                       │
│  (UI Components, ViewModels, State Management)              │
├─────────────────────────────────────────────────────────────┤
│                       Domain Layer                          │
│  (Use Cases, Business Logic, Domain Models)                │
├─────────────────────────────────────────────────────────────┤
│                        Data Layer                           │
│  (Repositories, Data Sources, DTOs, Mappers)               │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 Data Flow

```
UI (Compose) ←→ ViewModel ←→ UseCase ←→ Repository ←→ DataSource
      ↑                                                    ↓
      └────────── StateFlow/UIState ←─────────────────────┘
```

## 3. Module Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/countjoy/
│   │   │   ├── CountJoyApplication.kt
│   │   │   ├── MainActivity.kt
│   │   │   │
│   │   │   ├── presentation/
│   │   │   │   ├── countdown/
│   │   │   │   │   ├── CountdownScreen.kt
│   │   │   │   │   ├── CountdownViewModel.kt
│   │   │   │   │   └── CountdownUiState.kt
│   │   │   │   ├── event/
│   │   │   │   │   ├── EventInputScreen.kt
│   │   │   │   │   ├── EventInputViewModel.kt
│   │   │   │   │   └── EventInputUiState.kt
│   │   │   │   ├── components/
│   │   │   │   │   ├── CountdownDisplay.kt
│   │   │   │   │   ├── DateTimePicker.kt
│   │   │   │   │   └── EventCard.kt
│   │   │   │   ├── navigation/
│   │   │   │   │   └── CountJoyNavigation.kt
│   │   │   │   └── theme/
│   │   │   │       ├── Color.kt
│   │   │   │       ├── Theme.kt
│   │   │   │       └── Type.kt
│   │   │   │
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   │   ├── CountdownEvent.kt
│   │   │   │   │   └── CountdownTime.kt
│   │   │   │   ├── usecase/
│   │   │   │   │   ├── CreateEventUseCase.kt
│   │   │   │   │   ├── UpdateEventUseCase.kt
│   │   │   │   │   ├── DeleteEventUseCase.kt
│   │   │   │   │   ├── GetEventUseCase.kt
│   │   │   │   │   └── CalculateCountdownUseCase.kt
│   │   │   │   └── repository/
│   │   │   │       └── EventRepository.kt
│   │   │   │
│   │   │   ├── data/
│   │   │   │   ├── repository/
│   │   │   │   │   └── EventRepositoryImpl.kt
│   │   │   │   ├── local/
│   │   │   │   │   ├── EventDao.kt
│   │   │   │   │   ├── EventDatabase.kt
│   │   │   │   │   ├── EventEntity.kt
│   │   │   │   │   └── SharedPreferencesManager.kt
│   │   │   │   └── mapper/
│   │   │   │       └── EventMapper.kt
│   │   │   │
│   │   │   ├── di/
│   │   │   │   ├── AppModule.kt
│   │   │   │   ├── DataModule.kt
│   │   │   │   ├── DomainModule.kt
│   │   │   │   └── PresentationModule.kt
│   │   │   │
│   │   │   └── util/
│   │   │       ├── DateTimeUtils.kt
│   │   │       ├── Constants.kt
│   │   │       └── Extensions.kt
│   │   │
│   │   └── res/
│   │
│   └── test/
│       └── java/com/countjoy/
│           ├── domain/usecase/
│           ├── data/repository/
│           └── presentation/viewmodel/
│
└── androidTest/
    └── java/com/countjoy/
        └── ui/
```

## 4. Component Details

### 4.1 Presentation Layer

#### 4.1.1 ViewModels
```kotlin
class CountdownViewModel @Inject constructor(
    private val getEventUseCase: GetEventUseCase,
    private val calculateCountdownUseCase: CalculateCountdownUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CountdownUiState())
    val uiState: StateFlow<CountdownUiState> = _uiState.asStateFlow()
    
    // Business logic and state management
}
```

#### 4.1.2 UI State
```kotlin
data class CountdownUiState(
    val event: CountdownEvent? = null,
    val remainingTime: CountdownTime? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
```

#### 4.1.3 Composables
- Stateless UI components
- State hoisting pattern
- Preview support
- Material Design 3

### 4.2 Domain Layer

#### 4.2.1 Domain Models
```kotlin
data class CountdownEvent(
    val id: Long = 0,
    val name: String,
    val targetDateTime: LocalDateTime,
    val hasTime: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
```

#### 4.2.2 Use Cases
- Single responsibility
- Business logic encapsulation
- Repository abstraction
- Coroutine support

### 4.3 Data Layer

#### 4.3.1 Repository Implementation
```kotlin
class EventRepositoryImpl @Inject constructor(
    private val eventDao: EventDao,
    private val sharedPreferences: SharedPreferencesManager
) : EventRepository {
    
    override suspend fun getEvent(): Flow<CountdownEvent?> {
        return eventDao.getEvent().map { entity ->
            entity?.toDomainModel()
        }
    }
}
```

#### 4.3.2 Local Storage
- Room Database for complex data
- SharedPreferences for simple key-value pairs
- DataStore for typed data (future migration)

## 5. Dependency Injection

### 5.1 Hilt Setup
```kotlin
@HiltAndroidApp
class CountJoyApplication : Application()

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideEventDatabase(@ApplicationContext context: Context): EventDatabase {
        return Room.databaseBuilder(
            context,
            EventDatabase::class.java,
            "event_database"
        ).build()
    }
}
```

## 6. Navigation

### 6.1 Navigation Compose
```kotlin
@Composable
fun CountJoyNavigation(navController: NavHostController) {
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
```

## 7. State Management

### 7.1 StateFlow & Coroutines
- Unidirectional Data Flow (UDF)
- StateFlow for UI state
- SharedFlow for events
- viewModelScope for lifecycle awareness

### 7.2 Background Updates
```kotlin
class CountdownService : Service() {
    private val countdownJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Default + countdownJob)
    
    private fun startCountdown() {
        serviceScope.launch {
            while (isActive) {
                // Update countdown
                delay(1000)
            }
        }
    }
}
```

## 8. Testing Strategy

### 8.1 Unit Tests
- ViewModels: Test state transformations
- Use Cases: Test business logic
- Repositories: Test with fake implementations
- Mappers: Test data transformations

### 8.2 Integration Tests
- Room Database tests
- Repository integration tests
- Navigation tests

### 8.3 UI Tests
- Compose testing
- Screenshot tests
- Accessibility tests

## 9. Performance Considerations

### 9.1 Optimization Strategies
- Lazy loading with LazyColumn/LazyRow
- Remember & derivedStateOf for expensive calculations
- Baseline Profiles
- R8/ProGuard optimization

### 9.2 Memory Management
- Proper lifecycle handling
- Coroutine cancellation
- Resource cleanup in onCleared()

## 10. Security & Best Practices

### 10.1 Security
- No hardcoded secrets
- Proper permission handling
- Secure storage for sensitive data

### 10.2 Code Quality
- Kotlin coding conventions
- Detekt for static analysis
- KtLint for formatting
- SonarQube integration

## 11. Build Configuration

### 11.1 Gradle Configuration
```kotlin
android {
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.countjoy"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }
    
    buildFeatures {
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}
```

## 12. CI/CD Pipeline

### 12.1 GitHub Actions
- Build & Test on PR
- Code quality checks
- Automated releases
- PlayStore deployment

## 13. Future Considerations

### 13.1 Potential Enhancements
- Widget support
- Notifications
- Multiple events
- Cloud sync
- Wear OS support

### 13.2 Migration Path
- DataStore migration from SharedPreferences
- Compose Material3 adoption
- Kotlin Multiplatform consideration

## 14. Conclusion

Diese Architektur bietet:
- Klare Trennung der Verantwortlichkeiten
- Hohe Testbarkeit
- Skalierbarkeit
- Wartbarkeit
- Moderne Android-Entwicklungspraktiken

Die Clean Architecture ermöglicht es uns, die App schrittweise zu erweitern und neue Features hinzuzufügen, ohne die bestehende Codebasis zu gefährden.