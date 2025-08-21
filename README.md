# CountJoy - Event Countdown App

## 📱 Overview

CountJoy is a modern Android application that allows users to create and track countdowns to important events. Built with Kotlin and Jetpack Compose, it follows Clean Architecture principles and modern Android development best practices.

## ✨ Features

- **Single Event Countdown**: Track one important event at a time
- **Live Updates**: Real-time countdown display with second precision
- **Flexible Time Options**: Set events with or without specific times
- **Tablet Optimized**: Designed for optimal tablet experience
- **Material Design 3**: Modern UI following latest design guidelines
- **Dark/Light Mode**: Automatic theme adaptation

## 🏗️ Architecture

The app follows **Clean Architecture** with **MVVM** pattern:

- **Presentation Layer**: Jetpack Compose UI with ViewModels
- **Domain Layer**: Use Cases and Business Logic
- **Data Layer**: Repository pattern with Room database

See [architecture.md](docs/architecture.md) for detailed information.

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17 or later
- Android SDK 34
- Kotlin 1.9.20 or later

### Setup

1. Clone the repository:
```bash
git clone https://github.com/Psheikomaniac/countjoy.git
cd countjoy
```

2. Open in Android Studio

3. Build the project:
```bash
./gradlew build
```

4. Run tests:
```bash
./gradlew test
```

5. Install on device:
```bash
./gradlew installDebug
```

## 📂 Project Structure

```
countjoy/
├── app/
│   └── src/
│       ├── main/java/com/countjoy/
│       │   ├── data/          # Data layer
│       │   ├── domain/        # Business logic
│       │   ├── presentation/  # UI layer
│       │   └── di/            # Dependency injection
│       └── test/              # Unit tests
├── docs/                      # Documentation
│   ├── architecture.md
│   ├── kotlin-best-practices.md
│   ├── development-workflow.md
│   └── implementation-guide.md
└── .github/                   # GitHub configurations
    └── workflows/             # CI/CD pipelines
```

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: Clean Architecture + MVVM
- **Dependency Injection**: Hilt
- **Database**: Room
- **Async**: Coroutines + Flow
- **Navigation**: Navigation Compose
- **Testing**: JUnit, MockK, Turbine

## 📋 Development Workflow

We follow a structured Git workflow with feature branches:

1. Create issue in GitHub
2. Create feature branch from `dev`
3. Implement feature with TDD
4. Create PR to `dev` branch
5. Code review and CI checks
6. Merge to `dev`
7. Release from `dev` to `main`

See [development-workflow.md](docs/development-workflow.md) for details.

## 🧪 Testing

The project maintains high test coverage:

- **Unit Tests**: Domain logic and ViewModels
- **Integration Tests**: Repository and database
- **UI Tests**: Compose testing

Run all tests:
```bash
./gradlew test
./gradlew connectedAndroidTest
```

## 📱 Minimum Requirements

- Android 7.0 (API Level 24)
- 50 MB storage
- 2 GB RAM

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

Please read our [development workflow](docs/development-workflow.md) for details on our code of conduct and development process.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Team

- Project Owner: @Psheikomaniac

## 📞 Support

For support, please create an issue in the [GitHub repository](https://github.com/Psheikomaniac/countjoy/issues).

## 🔮 Roadmap

- [ ] v1.0 - MVP Release
- [ ] v1.1 - Widget Support
- [ ] v1.2 - Multiple Events
- [ ] v1.3 - Notifications
- [ ] v2.0 - Cloud Sync

## 🙏 Acknowledgments

- Android Development Team for excellent documentation
- Kotlin Team for an amazing language
- Open Source Community for inspiration

---

Built with ❤️ using Kotlin and Jetpack Compose