# Mini QR Code

## Aka QR Code X, or QR Code Compose X


<a href="https://play.google.com/store/apps/details?id=com.pedroid.qrcodecompose.androidapp"><img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" width="200"></a>

<a href="https://github.com/pedro-mgb/qr_code_compose_x/releases">Alternatively, check the Releases page</a>

[![Min API Version](https://img.shields.io/badge/Min_API-21-brightgreen.svg)](https://android-arsenal.com/api?level=21)
[![Target API Version](https://img.shields.io/badge/Target_API-34-brightgreen.svg)](https://developer.android.com/about/versions/14)
[![Kotlin Version](https://img.shields.io/badge/Kotlin-2.0.x-blue.svg)](https://kotlinlang.org)
[![Jetpack Compose Version](https://img.shields.io/badge/Compose-1.6.x-blue.svg)](https://developer.android.com/develop/ui/compose)

This repository contains the source code for the Mini QR Code Android app - Scan and generate QR Codes and various barcodes.

## Screenshots

<img src="docs/media/phone_en_01.png" width="200"  alt="Mini QR Code homepage scan" />     <img src="docs/media/phone_en_02.png" width="200"  alt="Mini QR Code generate empty" />     <img src="docs/media/phone_en_03.png" width="200" alt="Mini QR Code generate content" />     <img src="docs/media/phone_en_04.png" width="200" alt="Mini QR Code history" />

## About the project

The app is available only for Android - A Kotlin MultiPlatform migration (to also be available for iOS) may be done one day, but nothing planned as of yet.

Currently support languages are English and Portuguese.

App is built 100% with Kotlin, and UI with JetPack Compose. Also uses several other Jetpack libraries, and a few third-party as well.

### For more info on app architecture, CI/CD, and the overall codebase see [Project structure](docs/project-structure.md)

## Contributing

Anyone is free to open issues of bugs and feature suggestions.
You can even create Pull Requests with implementation of said features / fixes, add app translations for other languages, etc.

However, Pull Requests will be reviewed by me, and given my limited free time, may take some time to address them.

## Credits

The following credits are for people or projects that have been helpful in building the app or served as inspiration for the codebase:

- **[Svg2Compose](https://github.com/DenisMondon/Svg2Compose)** - For converting Jetpack Compose vector graphics
- **[Icon Kitchen](https://icon.kitchen)** - For building the app icon
- **[Material UI Icons](https://materialui.co/icon)** - Providing several icons used in the app
- **[Material 3 Theme builder](https://m3.material.io/theme-builder#/custom)** - For building Mini QR Code app theme
- **[Phillip Lackner](https://www.youtube.com/@PhilippLackner)** - Android content creator with several relevant instructional videos, such as [Simple QR Code Scanner](https://www.youtube.com/watch?v=asl1mFtkMkc&pp=ygUXcXIgY29kZSBzY2FubmVyIGNvbXBvc2U%3D) or [Clean architecture sample](https://www.youtube.com/watch?v=EF33KmyprEQ)
- **[Roman Levinzon](https://levinzon-roman.medium.com/jetpack-compose-ui-architecture-a34c4d3e4391)** - with a pretty good article on app UI architecture, that I adapted to this project.
- **[Journeyapps barcode scanner](https://github.com/journeyapps/zxing-android-embedded)** - Popular Android library for QR Code scanning with Zxing
- **[igorwojda's android showcase](https://github.com/igorwojda/android-showcase)** - Repository to showcase (HA) modern Android development, going beyond the app itself (CI, codestyle, etc.)
- **[Now In Android (aka NIA)](https://github.com/android/nowinandroid)** - Modern living app made by the Android team, 100% Kotlin + Jetpack Compose, showing a lot of recommended practices in Android app development, with scalability in mind.
