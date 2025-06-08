# Superb Coding Challenge
### Evaluation Criteria
* Kotlin Multiplatform skills: Structure, cross-platform compatibility, idiomatic usage.
* iOS integration: Smooth interop between Swift and KMP.
* Code quality: Clean, modular, and readable code.
* Product mindset: Basic UI/UX decisions and attention to usability.
### Goal
Build a lightweight Point-of-Sale (POS) style iOS app that uses Kotlin Multiplatform (KMP) to
fetch and display product data. This test demonstrates your ability to work with shared
business logic in Kotlin while delivering a native iOS experience.
### App Requirements
Build a basic iOS app using Swift (SwiftUI preferred) that
* Displays a list of products (product name + price).
* Uses a Kotlin Multiplatform module to fetch and process product data.
* On tapping a product, shows a detail view or alert with the product name and price.
### Shared Kotlin Multiplatform Module
Create a Swift-compatible Kotlin Multiplatform module that:
* Defines a Product model ( name, price, id, optional category or description).
* Exposes a function like suspend fun getProducts(): List<Product>.
* Includes basic business logic such as:
  * Sorting by price or name.
  * Filtering out products below a certain price (e.g., < 1.00).


## Solution Overview

### Technologies

This example app takes the [Kotlin Multiplatform app template](https://github.com/kotlin/KMP-App-Template-Native) as a base, together with some ideas from [KaMP Kit](https://github.com/touchlab/KaMPKit).
Not all of the additional libraries are referenced yet (e.g.[Ktor](https://ktor.io/)) but they are included in the project structure to show how to set up a multiplatform project with them.
Other libraries can be added as needed, see [Awesome Kotlin Multiplatform](https://github.com/terrakok/kmp-awesome) for a curated list of relevant options.

The app uses the following multiplatform dependencies in its implementation:

- [Koin](https://github.com/InsertKoinIO/koin) for dependency injection
- [KMP-ObservableViewModel](https://github.com/rickclephas/KMP-ObservableViewModel) for shared ViewModel implementations in common code
- [KMP-NativeCoroutines](https://github.com/rickclephas/KMP-NativeCoroutines)
- [SqlDelight](https://sqldelight.github.io/sqldelight/2.1.0/) for persistent data storage

Not used in this example, but also worth investigating for improved Swift interop is [SKIE](https://github.com/touchlab/SKIE)

### Project Structure
- composeApp: a partial implementation of the app using Jetpack Compose, which is not yet fully functional, but was used to help debug and test the shared code.
- gradle/libs.versions.toml: a file to manage the versions of the dependencies used in the project.
- iosApp: the iOS app, which uses the shared code to display a list of products via SwiftUI.
- shared: the shared Kotlin Multiplatform code, which contains the business logic and data models.
  - shared/src/androidMain: any Android-specific code
  - shared/src/commonMain: primary shared code, including the Product model and business logic
  - shared/src/commonMain/sqldelight: the SQLDelight database schema and queries
  - shared/src/commonText: test cases for the common code
  - shared/src/iosMain: any iOS-specific code

### App Architecture
The app implements a Clean Architecture applying MVVM pattern. Navigation is handled by the UI. Implementing Coordinator pattern to manage the navigation flow, would be a good next step.
The shared viewModels expose Kotlin Flow objects to the UI, facilitating implementation of MVI should that be desirable.

### Running the App
The [Kotlin documentation](https://kotlinlang.org/docs/home.html) provides a [useful guide](https://kotlinlang.org/docs/multiplatform-mobile-setup.html) to set up an environment for multiplatform development. If you encounter any problems, using [KDoctor](https://github.com/Kotlin/kdoctor) may help identify issues.
The app can be built and run from Android Studio, but it makes more sense to use Xcode for the iOS app, as it provides a better development experience for SwiftUI.

### Next Steps
The app is a work in progress, and there are many areas that could be improved or extended. Some ideas for next steps include:
- implement a real API to fetch product data, rather than using a fake data source
- the repository implementation is very basic and should be improved, for example by scheduling data synchronisation with remote source(s)
- there are no tests yet, so adding unit tests for the shared code and UI tests for the iOS app is required
- implement a proper navigation flow using the Coordinator pattern
- the iOS app UI is very basic and could be improved, for example defining a Layout Guide with centralised styles and colors, font definitions etc
- error handling is not implemented yet, NOTE: need to consider coroutine cancellation - do not try/catch everything
- the current requirements are (deliberately) very basic, additional features may challenge assumptions made above.
- Xcodegen or Tuist could be used to generate the Xcode project, which would make it easier to maintain and share the project structure.
- update ktlint rules to allow for JetPack Compose code style
- replace any magic numbers with constants
- there may be a case for creating a typealias for List<Product> to make the code more readable, e.g. Products = List<Product>
- alternatively, encapsulate the list in a class, e.g. value(?) class Products(val products: List<Product>), to better track/extend functionality in the future