# Kotlin Best Practices für Android Development

## 1. Grundlegende Kotlin-Konventionen

### 1.1 Naming Conventions

```kotlin
// Packages: lowercase, no underscores
package com.countjoy.domain.usecase

// Classes & Interfaces: PascalCase
class CountdownViewModel
interface EventRepository

// Functions & Properties: camelCase
fun calculateRemainingTime()
val eventName: String

// Constants: UPPER_SNAKE_CASE
const val MAX_EVENT_NAME_LENGTH = 50
const val DEFAULT_ANIMATION_DURATION = 300L

// Backing properties: underscore prefix
private val _uiState = MutableStateFlow<UiState>()
val uiState: StateFlow<UiState> = _uiState.asStateFlow()
```

### 1.2 File Organization

```kotlin
// Eine Klasse pro File (außer bei sealed classes/data classes)
// Dateiname = Klassenname

// EventRepository.kt
interface EventRepository {
    suspend fun getEvent(): Flow<CountdownEvent?>
}

// Sealed classes mit ihren Implementierungen
// EventAction.kt
sealed class EventAction {
    data class Create(val event: CountdownEvent) : EventAction()
    data class Update(val event: CountdownEvent) : EventAction()
    object Delete : EventAction()
}
```

## 2. Null Safety & Type Safety

### 2.1 Null Handling

```kotlin
// ✅ GOOD: Elvis operator für default values
val displayName = event?.name ?: "Unnamed Event"

// ✅ GOOD: Safe calls
event?.let { 
    updateCountdown(it)
}

// ✅ GOOD: requireNotNull mit aussagekräftiger Message
val event = requireNotNull(getEvent()) { 
    "Event must not be null at this point" 
}

// ❌ BAD: Force unwrapping ohne Check
val name = event!!.name
```

### 2.2 Smart Casts

```kotlin
// ✅ GOOD: Nutze smart casts
fun processEvent(event: Any) {
    if (event is CountdownEvent) {
        // event wird automatisch zu CountdownEvent gecastet
        println(event.targetDateTime)
    }
}

// ✅ GOOD: when expression mit smart cast
when (val result = repository.getEvent()) {
    is Success -> handleSuccess(result.data)
    is Error -> showError(result.message)
}
```

## 3. Coroutines & Async

### 3.1 Coroutine Best Practices

```kotlin
// ✅ GOOD: Structured Concurrency mit viewModelScope
class CountdownViewModel : ViewModel() {
    init {
        viewModelScope.launch {
            repository.getEvent()
                .flowOn(Dispatchers.IO)
                .collect { event ->
                    _uiState.update { it.copy(event = event) }
                }
        }
    }
}

// ✅ GOOD: Proper exception handling
viewModelScope.launch {
    try {
        val event = repository.createEvent(newEvent)
        _uiState.update { it.copy(isLoading = false) }
    } catch (e: Exception) {
        _uiState.update { 
            it.copy(isLoading = false, error = e.message) 
        }
    }
}

// ❌ BAD: GlobalScope usage
GlobalScope.launch {
    // Avoid GlobalScope
}
```

### 3.2 Flow Best Practices

```kotlin
// ✅ GOOD: StateFlow für UI State
private val _uiState = MutableStateFlow(CountdownUiState())
val uiState: StateFlow<CountdownUiState> = _uiState.asStateFlow()

// ✅ GOOD: SharedFlow für Events
private val _events = MutableSharedFlow<UiEvent>()
val events: SharedFlow<UiEvent> = _events.asSharedFlow()

// ✅ GOOD: Flow operators
fun observeCountdown() = flow {
    while (currentCoroutineContext().isActive) {
        emit(calculateRemainingTime())
        delay(1000)
    }
}.flowOn(Dispatchers.Default)
    .distinctUntilChanged()
    .catch { e -> 
        Timber.e(e, "Error in countdown flow")
    }
```

## 4. Extension Functions & Properties

### 4.1 Extension Functions

```kotlin
// ✅ GOOD: Domain-specific extensions
fun LocalDateTime.toDisplayString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    return format(formatter)
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

// ✅ GOOD: Scope functions für cleaner code
inline fun <T> T.applyIf(condition: Boolean, block: T.() -> T): T {
    return if (condition) block() else this
}

// Usage
text.applyIf(isImportant) { 
    copy(fontWeight = FontWeight.Bold) 
}
```

### 4.2 Property Extensions

```kotlin
// ✅ GOOD: Computed properties
val CountdownEvent.isExpired: Boolean
    get() = targetDateTime.isBefore(LocalDateTime.now())

val CountdownEvent.formattedDate: String
    get() = targetDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE)
```

## 5. Functional Programming

### 5.1 Higher-Order Functions

```kotlin
// ✅ GOOD: Use higher-order functions
inline fun measureTimeAndLog(
    tag: String,
    block: () -> Unit
) {
    val startTime = System.currentTimeMillis()
    block()
    val endTime = System.currentTimeMillis()
    Timber.d("$tag took ${endTime - startTime}ms")
}

// Usage
measureTimeAndLog("Database Query") {
    repository.getAllEvents()
}
```

### 5.2 Collection Operations

```kotlin
// ✅ GOOD: Functional collection operations
val futureEvents = events
    .filter { it.targetDateTime.isAfter(LocalDateTime.now()) }
    .sortedBy { it.targetDateTime }
    .map { it.toUiModel() }

// ✅ GOOD: Sequence for large collections
val processed = largeList.asSequence()
    .filter { it.isValid() }
    .map { transform(it) }
    .take(100)
    .toList()
```

## 6. Data Classes & Sealed Classes

### 6.1 Data Classes

```kotlin
// ✅ GOOD: Immutable data classes
data class CountdownEvent(
    val id: Long = 0,
    val name: String,
    val targetDateTime: LocalDateTime,
    val hasTime: Boolean = false
) {
    // Computed properties
    val isToday: Boolean
        get() = targetDateTime.toLocalDate() == LocalDate.now()
}

// ✅ GOOD: Copy for updates
fun updateEvent(event: CountdownEvent, newName: String) = 
    event.copy(name = newName)
```

### 6.2 Sealed Classes

```kotlin
// ✅ GOOD: Sealed classes for state management
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val exception: Throwable) : UiState<Nothing>()
}

// ✅ GOOD: Exhaustive when
fun <T> handleState(state: UiState<T>) = when (state) {
    is UiState.Loading -> showLoading()
    is UiState.Success -> showData(state.data)
    is UiState.Error -> showError(state.exception)
}
```

## 7. Dependency Injection with Hilt

### 7.1 Module Setup

```kotlin
// ✅ GOOD: Clear module organization
@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    
    @Provides
    @Singleton
    fun provideEventDao(database: EventDatabase): EventDao = 
        database.eventDao()
    
    @Provides
    @Singleton
    fun provideEventRepository(
        dao: EventDao,
        @ApplicationContext context: Context
    ): EventRepository = EventRepositoryImpl(dao, context)
}
```

### 7.2 ViewModel Injection

```kotlin
// ✅ GOOD: Constructor injection
@HiltViewModel
class CountdownViewModel @Inject constructor(
    private val getEventUseCase: GetEventUseCase,
    private val calculateCountdownUseCase: CalculateCountdownUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // Implementation
}
```

## 8. Testing Best Practices

### 8.1 Unit Testing

```kotlin
// ✅ GOOD: Test naming convention
@Test
fun `when event is created with past date, should return error`() {
    // Given
    val pastDate = LocalDateTime.now().minusDays(1)
    
    // When
    val result = createEventUseCase(
        CountdownEvent(name = "Test", targetDateTime = pastDate)
    )
    
    // Then
    assertTrue(result is Result.Error)
    assertEquals("Cannot create event in the past", result.message)
}
```

### 8.2 Coroutine Testing

```kotlin
// ✅ GOOD: Test coroutines with runTest
@Test
fun `should emit loading state before data`() = runTest {
    // Given
    val events = listOf(mockEvent)
    coEvery { repository.getEvents() } returns flowOf(events)
    
    // When
    viewModel.uiState.test {
        // Then
        assertEquals(UiState.Loading, awaitItem())
        assertEquals(UiState.Success(events), awaitItem())
    }
}
```

## 9. Performance Optimization

### 9.1 Lazy Initialization

```kotlin
// ✅ GOOD: Lazy for expensive objects
class CountdownViewModel : ViewModel() {
    private val dateFormatter by lazy {
        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    }
    
    private val heavyObject by lazy(LazyThreadSafetyMode.NONE) {
        // Thread-unsafe lazy for single-threaded access
        HeavyObject()
    }
}
```

### 9.2 Inline Functions

```kotlin
// ✅ GOOD: Inline for higher-order functions
inline fun <reified T> Context.startActivity(
    extras: Bundle? = null,
    options: Bundle? = null
) {
    val intent = Intent(this, T::class.java).apply {
        extras?.let { putExtras(it) }
    }
    startActivity(intent, options)
}

// Usage
startActivity<EventInputActivity>()
```

## 10. Android Specific

### 10.1 Context Usage

```kotlin
// ✅ GOOD: Use application context for long-lived references
@Singleton
class SharedPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Implementation
}

// ❌ BAD: Holding Activity context
class BadManager(private val activity: Activity) {
    // Memory leak risk!
}
```

### 10.2 Lifecycle Awareness

```kotlin
// ✅ GOOD: Lifecycle-aware coroutines
@Composable
fun CountdownScreen(viewModel: CountdownViewModel = hiltViewModel()) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    
    LaunchedEffect(lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                // Handle events
            }
        }
    }
}
```

## 11. Code Style

### 11.1 Formatting

```kotlin
// ✅ GOOD: Consistent formatting
class CountdownViewModel(
    private val repository: EventRepository,
    private val calculator: CountdownCalculator
) : ViewModel() {
    
    fun createEvent(
        name: String,
        date: LocalDate,
        time: LocalTime? = null
    ): Flow<Result<CountdownEvent>> = flow {
        emit(Result.Loading)
        
        val event = CountdownEvent(
            name = name,
            targetDateTime = if (time != null) {
                LocalDateTime.of(date, time)
            } else {
                date.atStartOfDay()
            },
            hasTime = time != null
        )
        
        emit(repository.createEvent(event))
    }.catch { e ->
        emit(Result.Error(e))
    }.flowOn(Dispatchers.IO)
}
```

### 11.2 Documentation

```kotlin
/**
 * Calculates the remaining time until the target event.
 * 
 * @param event The countdown event to calculate time for
 * @param currentTime The current time (defaults to now)
 * @return CountdownTime object with days, hours, minutes, seconds
 * @throws IllegalArgumentException if event is in the past
 */
fun calculateRemainingTime(
    event: CountdownEvent,
    currentTime: LocalDateTime = LocalDateTime.now()
): CountdownTime {
    require(event.targetDateTime.isAfter(currentTime)) {
        "Event must be in the future"
    }
    // Implementation
}
```

## 12. Common Pitfalls to Avoid

### 12.1 Memory Leaks

```kotlin
// ❌ BAD: Anonymous inner class holds reference
button.setOnClickListener(object : View.OnClickListener {
    override fun onClick(v: View) {
        // This holds reference to outer class
    }
})

// ✅ GOOD: Lambda doesn't capture unnecessary references
button.setOnClickListener { 
    // Handle click
}
```

### 12.2 Blocking Main Thread

```kotlin
// ❌ BAD: Blocking I/O on main thread
val data = runBlocking {
    repository.getData()
}

// ✅ GOOD: Async on appropriate dispatcher
viewModelScope.launch {
    val data = withContext(Dispatchers.IO) {
        repository.getData()
    }
    _uiState.value = UiState.Success(data)
}
```

## 13. Resources & Tools

### 13.1 Static Analysis
- **Detekt**: Custom rules for Kotlin
- **KtLint**: Kotlin linter
- **Android Lint**: Android-specific checks

### 13.2 Gradle Configuration

```kotlin
// build.gradle.kts
detekt {
    config = files("$projectDir/config/detekt.yml")
    buildUponDefaultConfig = true
}

ktlint {
    android.set(true)
    outputColorName.set("RED")
}
```

## 14. Summary

Diese Best Practices sollen sicherstellen, dass unser Kotlin-Code:
- **Idiomatisch**: Nutzt Kotlin-Features effektiv
- **Sicher**: Minimiert Null Pointer Exceptions und Memory Leaks
- **Performant**: Optimiert für Android-Umgebung
- **Wartbar**: Leicht zu lesen und zu modifizieren
- **Testbar**: Einfach zu testen und zu verifizieren

Durch die konsequente Anwendung dieser Praktiken erreichen wir eine hohe Code-Qualität und Entwicklerproduktivität.