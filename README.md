# Giphy

This sample Android app presents the Giphy trending animated gifs for sharing or download. You can
now also search animated gifs by keyword.

&nbsp;

<p align="center">
  <img src="240422_animated_screenshot.gif" width="600" /><br/>
</p>

&nbsp;

## High level architecture

* Kotlin
* MVVM & clean architecture
* Jetpack Compose - Single Activity
* Kotlin Coroutines and Flow
* Dependency Injection using Dagger Hilt
* Material 3 dynamic colour theming supporting light and dark modes
* Dynamic screen layout support using Windows Size Class
* Gradle Kotlin DSL and Version Catalog
* Macrobenchmark and Baseline Profile
* Full unit test and UI (Journey) test suite

&nbsp;

## Major libraries used

### Dependencies

* [AndroidX Core KTX](https://developer.android.com/jetpack/androidx/releases/core) - Apache 2.0 - Extensions to Java APIs for Android development
* [JUnit](https://junit.org/junit5/) - EPL 2.0 - A simple framework to write repeatable tests
* [AndroidX Espresso](https://developer.android.com/training/testing/espresso) - Apache 2.0 - UI testing framework
* [AndroidX Activity Compose](https://developer.android.com/jetpack/androidx/releases/activity) - Apache 2.0 - Jetpack Compose integration with Activity
* [Jetpack Compose BOM](https://developer.android.com/jetpack/compose/bom) - Apache 2.0 - Bill of Materials for Jetpack Compose
* [AndroidX Compose UI](https://developer.android.com/jetpack/androidx/releases/compose-ui) - Apache 2.0 - UI components for Jetpack Compose
* [AndroidX Material3](https://developer.android.com/jetpack/androidx/releases/compose-material3) - Apache 2.0 - Material Design components for Jetpack Compose
* [AndroidX Benchmark](https://developer.android.com/jetpack/androidx/releases/benchmark) - Apache 2.0 - Benchmarking library
* [AndroidX Core Splashscreen](https://developer.android.com/jetpack/androidx/releases/core) - Apache 2.0 - Core splash screen
* [AndroidX DataStore Preferences](https://developer.android.com/jetpack/androidx/releases/datastore) - Apache 2.0 - Data storage solution
* [AndroidX Legacy Support](https://developer.android.com/jetpack/androidx/releases/legacy) - Apache 2.0 - Legacy libraries
* [AndroidX Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) - Apache 2.0 - Lifecycles-aware components
* [AndroidX Room](https://developer.android.com/jetpack/androidx/releases/room) - Apache 2.0 - Persistence library
* [Kotlinx DateTime](https://github.com/Kotlin/kotlinx-datetime) - Apache 2.0 - A multiplatform Kotlin library for working with date and time
* [Timber](https://github.com/JakeWharton/timber) - Apache 2.0 - A logger with a small, extensible API
* [Coil](https://coil-kt.github.io/coil/) - Apache 2.0 - An image loading library for Android backed by Kotlin Coroutines
* [MockK](https://mockk.io/) - Apache 2.0 - Mocking library for Kotlin
* [Kotlinx Coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Apache 2.0 - Libraries for Kotlin coroutines
* [Kotest](https://kotest.io/) - Apache 2.0 - Kotlin test framework
* [Robolectric](http://robolectric.org/) - MIT - A framework that brings fast, reliable unit tests to Android
* [AndroidX Test](https://developer.android.com/jetpack/androidx/releases/test) - Apache 2.0 - Testing framework for Android
* [Hilt](https://dagger.dev/hilt/) - Apache 2.0 - A dependency injection library for Android that reduces the boilerplate of doing manual dependency injection
* [LeakCanary](https://square.github.io/leakcanary/) - Apache 2.0 - A memory leak detection library for Android
* [Ktor](https://ktor.io/) - Apache 2.0 - Framework for building asynchronous servers and clients in connected systems

### Plugins

* [Android Application Plugin](https://developer.android.com/studio/build/gradle-plugin-3-0-0-migration) - Google - Plugin for building Android applications
* [Jetbrains Kotlin Android Plugin](https://kotlinlang.org/docs/gradle.html) - JetBrains - Plugin for Kotlin Android projects
* [Compose Compiler Plugin](https://developer.android.com/jetpack/compose) - JetBrains - Plugin for Jetpack Compose
* [Hilt Android Plugin](https://dagger.dev/hilt/gradle-setup.html) - Google - Plugin for Hilt dependency injection
* [Kover Plugin](https://github.com/Kotlin/kotlinx-kover) - JetBrains - Code coverage tool for Kotlin
* [Ktlint Plugin](https://github.com/JLLeitschuh/ktlint-gradle) - JLLeitschuh - Plugin for Kotlin linter
* [Google DevTools KSP](https://github.com/google/ksp) - Google - Kotlin Symbol Processing API plugin
* [Android Test Plugin](https://developer.android.com/studio/test) - Google - Plugin for Android testing
* [Baseline Profile Plugin](https://developer.android.com/studio/profile/baselineprofile) - AndroidX - Plugin for generating baseline profiles
* [Serialization Plugin](https://github.com/Kotlin/kotlinx.serialization) - JetBrains - Plugin for Kotlin serialization

&nbsp;

## Building the App
### Requirements

* To build the app by yourself, you need your own [Giphy API Key](https://developers.giphy.com/)

&nbsp;

### Setting up the keystore

Release builds will be signed if either the keystore file or environment variables are set.
Otherwise, the app will be built unsigned and without the Giphy API key installed, which will not
pull any data from the endpoint.

### Local

* Android Keystore is not being stored in this repository. You need your own Keystore to generate
  the apk / App Bundle

* If your project folder is at `/app/giphy/`, the Keystore file and `keystore.properties`
  should be placed at `/app/`

* The format of `keystore.properties` is:
  ```
     store=/app/release-key.keystore
     alias=<alias>
     pass=<alias password>
     storePass=<keystore password>
     giphyApiKey="<your API Key here>"
  ```

### CI environment

* This project has been configured to support automated CI builds.

* The following environment variables have been set to provide the keystore:
  ```
     BITRISE = true
     HOME = <the home directory of the bitrise environment>
     BITRISEIO_ANDROID_KEYSTORE_PASSWORD = <your keystore password>
     BITRISEIO_ANDROID_KEYSTORE_ALIAS = <your keystore alias>
     BITRISEIO_ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD = <your keystore private key password>
     GIPHYAPIKEY= <your API Key>
  ```

### Build and install on the connected device

This app has two build variants: `Debug` and `Release`. The most common build commands are:

* `./gradlew clean installDebug`
* `./gradlew clean instal`
* `./gradlew clean bundleRelease`
* `./gradlew clean assembleRelease`

The generated apk(s) will be stored under `app/build/outputs/`



