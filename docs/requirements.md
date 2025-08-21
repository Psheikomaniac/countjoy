# Product Requirements Document (PRD)
## Event Countdown App v1.0

### 1. Projektübersicht

**Projektname:** Event Countdown App  
**Version:** 1.0 (MVP)  
**Plattform:** Android (Tablet optimiert)  
**Entwicklungssprache:** Kotlin  
**Zielgruppe:** Privatnutzer  

### 2. Produktvision

Eine einfache, intuitive Android-App, die es Nutzern ermöglicht, ein Event mit Datum (optional Uhrzeit) einzutragen und einen Live-Countdown bis zu diesem Event anzuzeigen.

### 3. Kernanforderungen (Must-Have)

#### 3.1 Event-Eingabe
- **Datumseingabe:** Nutzer kann ein Zieldatum auswählen (DatePicker)
- **Optionale Uhrzeiteingabe:** Nutzer kann optional eine spezifische Uhrzeit festlegen (TimePicker)
- **Event-Name:** Kurze Beschreibung des Events (max. 50 Zeichen)
- **Validierung:** Nur zukünftige Daten/Zeiten sind erlaubt

#### 3.2 Countdown-Anzeige
- **Adaptive Anzeige:**
  - Bei > 1 Tag: "X Tage, Y Stunden"
  - Bei < 1 Tag: "X Stunden, Y Minuten, Z Sekunden"
- **Live-Update:** Automatische Aktualisierung jede Sekunde
- **Großformat:** Gut lesbare Anzeige auf Tablet-Bildschirmen

#### 3.3 Benutzeroberfläche
- **Minimalistisches Design:** Fokus auf Countdown-Anzeige
- **Tablet-optimiert:** Landscape- und Portrait-Modus
- **Eingabefeld:** Einfache Maske für Event-Erstellung
- **Edit-Funktion:** Möglichkeit das Event zu bearbeiten

### 4. Technische Spezifikationen

#### 4.1 Technologie-Stack
- **Programmiersprache:** Kotlin
- **UI Framework:** Android Views oder Jetpack Compose
- **Minimum SDK:** Android 7.0 (API Level 24)
- **Target SDK:** Android 14 (API Level 34)
- **Architecture Pattern:** MVVM mit LiveData/StateFlow

#### 4.2 Kernkomponenten
- **MainActivity:** Haupt-UI mit Countdown-Anzeige
- **EventInputActivity/Fragment:** Event-Eingabemaske
- **CountdownService:** Background-Service für Timer-Updates
- **SharedPreferences:** Lokale Speicherung des Events
- **BroadcastReceiver:** Für System-Events (Boot, Timezone-Changes)

#### 4.3 Datenmodell
```kotlin
data class CountdownEvent(
    val name: String,
    val targetDateTime: LocalDateTime,
    val hasTime: Boolean = false
)
```

### 5. Funktionale Anforderungen

#### 5.1 App-Start
- App zeigt sofort den aktuellen Countdown an (falls Event gesetzt)
- Wenn kein Event vorhanden: Aufforderung zur Event-Erstellung

#### 5.2 Event-Management
- **Erstellen:** Neue Events über FAB oder Menu
- **Bearbeiten:** Tap auf Event-Details
- **Löschen:** Über Kontext-Menu oder Einstellungen

#### 5.3 Countdown-Berechnung
- Präzise Berechnung basierend auf aktueller Systemzeit
- Berücksichtigung von Zeitzonen-Änderungen
- Automatisches Stop bei Event-Erreichen

### 6. UI/UX Anforderungen

#### 6.1 Layout-Struktur
- **Header:** Event-Name
- **Main Content:** Countdown-Anzeige (zentriert, groß)
- **Footer:** Edit/Settings Button

#### 6.2 Design-Prinzipien
- **Material Design 3** Guidelines
- **Accessibility:** Mindestens AA-Standard
- **Responsive:** Anpassung an verschiedene Bildschirmgrößen
- **Dark/Light Mode:** System-Theme-Support

### 7. Performance-Anforderungen

- **Startup-Zeit:** < 2 Sekunden
- **UI-Responsiveness:** 60 FPS bei Countdown-Updates
- **Speicherverbrauch:** < 50 MB RAM
- **Battery-Optimiert:** Effiziente Background-Updates

### 8. Qualitätssicherung

#### 8.1 Testing-Strategie
- **Unit Tests:** Countdown-Berechnung, Datenmodell
- **UI Tests:** Event-Eingabe, Countdown-Anzeige
- **Manual Tests:** Verschiedene Geräte und Android-Versionen

#### 8.2 Edge Cases
- System-Reboot während laufendem Countdown
- Zeitzone-Änderungen
- Datum/Zeit in der Vergangenheit
- App im Background für längere Zeit

### 9. Ausschlüsse (Out of Scope v1.0)

- Multiple Events gleichzeitig
- Notifications/Alarms
- Event-Export/Import
- Cloud-Synchronisation
- Widgets
- Custom Themes
- Sounds/Vibrationen

### 10. Entwicklungs-Timeline

**Phase 1 (Woche 1-2):**
- Projekt-Setup und Grundarchitektur
- Datenmodell und Persistence

**Phase 2 (Woche 3-4):**
- Countdown-Logik und UI-Grundgerüst
- Event-Eingabe Implementation

**Phase 3 (Woche 5-6):**
- UI-Polish und Testing
- Performance-Optimierung

**Phase 4 (Woche 7):**
- Bug-Fixes und finale Tests
- Release-Vorbereitung

### 11. Risiken und Mitigation

| Risiko | Wahrscheinlichkeit | Impact | Mitigation |
|--------|-------------------|---------|------------|
| Performance-Issues bei langem Background-Lauf | Mittel | Hoch | Effiziente Timer-Implementation, Tests |
| UI-Layout-Probleme verschiedene Screens | Niedrig | Mittel | Responsive Design, Device-Testing |
| Datum/Zeit-Handling Bugs | Hoch | Hoch | Extensive Unit-Tests, Libraries nutzen |

### 12. Erfolgs-Metriken

- **Funktionalität:** App läuft stabil ohne Crashes
- **Usability:** Event-Erstellung in < 30 Sekunden möglich
- **Performance:** Smooth Countdown-Updates ohne Lag
- **User Experience:** Intuitive Bedienung ohne Tutorial nötig
