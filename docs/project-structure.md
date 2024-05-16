# Project structure

This page contains a full overview of the Mini QR Code app project structure, from a technical standpoint.

If you have any questions or suggestions, feel free to open an issue!

## Tech stack

- [Kotlin programming language](https://kotlinlang.org)
  - [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
  - [Flows](https://kotlinlang.org/docs/flow.html)
  - [Kotlin Symbol Processing (KSP)](https://kotlinlang.org/docs/ksp-overview.html)
  - [Parcelize Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.parcelize)
- [Android Studio + Android SDK](https://developer.android.com/studio)
- [Jetpack Compose](https://developer.android.com/develop/ui/compose)
  - [Material 3 UI](https://m3.material.io/develop/android/jetpack-compose)
  - [Constraint Layout](https://developer.android.com/develop/ui/compose/layouts/constraintlayout)
  - [Google accompanist (for permissions in Compose)](https://github.com/google/accompanist)
  - [Integration with other Jetpack libraries such as ViewModel and Hilt](https://developer.android.com/develop/ui/compose/libraries)
- Additional [Jetpack libraries](https://developer.android.com/jetpack)
  - [CameraX](https://developer.android.com/media/camera/camerax)
  - [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
  - [Room Database](https://developer.android.com/training/data-storage/room)
  - [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
- [Zxing](https://github.com/zxing/zxing)
- [ML Kit barcode scanning](https://developers.google.com/ml-kit/vision/barcode-scanning)
- [Arrow (optics)](https://arrow-kt.io/learn/immutable-data/lens/)
- [Testing](https://developer.android.com/training/testing/fundamentals)
  - [Junit 4](https://github.com/junit-team/junit4) - May be migrated to Junit 5 soon.
  - [Androidx testing](https://developer.android.com/jetpack/androidx/releases/test)
  - [Mockk](https://mockk.io)
  - [Turbine](https://github.com/cashapp/turbine)
- [KtLint](https://pinterest.github.io/ktlint)
  - With [gradle plugin](https://github.com/jeremymailen/kotlinter-gradle)

For full dependency list + versions, checkout [the version catalog](../gradle/libs.versions.toml)

## Module structure

Project currently contains 3 modules:

1. `app` - Main app module, contains most of the app's code
2. `qrcodecomposelib` - Module that contains Jetpack Compose UI for scanning QR Codes, and also generating QR Codes (and barcodes), using Zxing.
3. `qrcodecomposelibmlkit` - Separate module from `qrcodecomposelib`, that includes scanning QR Codes with ML Kit instead of Zxing - overall quicker scans.
    In a separate module because we are bundling ML Kit, resulting in a larger binary size.

The idea of this module separation is merely to extract the scanning and creation of QR Codes with Jetpack Compose, and make it independent from any business logic.
In other words, to make `qrcodecomposelib` a reusable UI module for the sake of scanning or creating QR Codes and barcodes.
The `qrcodecomposelibmlkit` can be seen as an extension of the base library module, and optional in case of constraints on apk size.
Other than that, there wouldn't really be a need for several modules, as it's a **mini** app, that most likely won't grow to a point where module separation becomes advantageous.

In the future, I may publish `qrcodecomposelib` as an actual standalone library, but there are no immediate plans for now.

## Architecture

The project follows a "Clean-ish" architecture for the app, with a [package-by-feature]() organization:

- `domain` - Layer containing business logic, platform agnostic code.
   Can contain business models (data classes) and interfaces for retrieving said models (repositories)
- `data` - Depends on domain. Provides implementation of repositories, using platform (Android) specific frameworks.
- `presentation` - AKA UI layer, has the code for displaying the visual content, and handling user interactions.
   It is platform / framework specific. Only needs knowledge of `domain`, if the implementation (from `data`) is provided externally, e.g. via [dependency injection](https://en.wikipedia.org/wiki/Dependency_injection)

This is a more simplistic form of [the original Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html), and there's [a few aspects right now that break clean architecture](../app/src/main/java/com/pedroid/qrcodecompose/androidapp/features/history/domain/HistoryEntry.kt) - hence the term "Clean-ish".
But it is quite common to have this structure in Android development, and for most small-medium-sized projects it achieves a reasonable level of separation of concerns without becoming unnecessarily complex.

The app has currently 4 features (`scan`, `generate`, `history`, and `settings`), and they all follow the above architecture layer.
The `app` module also has a `core` package for logic common to all the app, that is not feature specific.
Also has the `bridge` package, for connecting the features together in the app.

The `qrcodecomposelib` (and `mlkit` extension module) only have presentation layer components, hence they need not follow this separation.

### UI layer architecture

The presentation layer still has a few more separation of concerns:

- Navigation to and from the screens is separated into a `navigation` package.
- The actual UI components and theming used in the app is in a separate `designsystem` package, can be reused for other projects if one wishes to have the same setup.
- Most screens have more than one navigation action, and these actions listeners are extracted to separate classes.
- The app follows an MVVM / MVI mix [(see video for differences)](https://www.youtube.com/watch?v=b2z1jvD4VMQ) for separating UI screen from UI logic.
   To connect the `ViewModel`, `Screen` and `Navigation`, we use a `Coordinator`, [similar to this article](https://levinzon-roman.medium.com/jetpack-compose-ui-architecture-a34c4d3e4391)


## CI / CD

CI / CD pipelines are configured via [Github Actions](https://docs.github.com/en/actions).

### Continuous Integration

- [BuildAndTest](../.github/workflows/BuildAndTest.yaml)
  - Runs every time the `main` branch is updated, or when there is a Pull Request into `main`.
  - Runs [ktlint](https://pinterest.github.io/ktlint/latest/).
  - Builds the project.
  - Runs all unit tests in the project.
  - Runs all instrumented tests in the project (UI tests may be added in the future).

The above steps are run independently. The Pull Request can only be merged if all the above checks pass.

### Continuous Delivery

- [ReleaseApp](../.github/workflows/ReleaseApp.yaml)
  - Every time a version tag is created on `main` branch.
  - The Mini QR Code app's keystore file and credentials ([Github secrets](https://docs.github.com/en/actions/security-guides/using-secrets-in-github-actions)) are retrieved.
  - A signed release APK is generated.
  - A signed release Android App Bundle (AAB) is generated.
  - Both APK and App Bundle are published to github releases.

Currently the AAB is manually uploaded to the [Play Store](https://play.google.com/store/apps/details?id=com.pedroid.qrcodecompose.androidapp), but it may be automated in the future.
