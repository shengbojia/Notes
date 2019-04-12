<h1 align="center"> Notes </h1> <br>

<p align="center">
  Android app for keeping track of ideas, on the go.
</p>

<p align="center">
  <img src="https://user-images.githubusercontent.com/26352502/56065617-58789400-5d43-11e9-8ff5-d8eda1b902c9.gif">
</p>

## Table of Contents
* [Introduction](#introduction)
* [Features](#features)
* [Showcase](#showcase)
  * [Add a new Note](#add-a-new-note)
  * [Open and view a Note](#open-and-view-a-note)
  * [Edit an existing Note](#edit-an-existing-note)
* [Libraries Used](#libraries-used)
* [License](*license)

## Introduction
Quickly capture your ideas no matter when or where inspiration strikes. Written in Kotlin, Notes allows for a stable and 
efficient user experience by adhering to Google's latest standard for Android Architecure. 

## Features
Things you can do with Notes:
* View a list of saved notes
* Press the "+" button to add a new note
* Edit an existing note by tapping on it
* Notes are timestamped based on the last save
* Long press to enter multi-selection mode
* Quickly clear all notes from the main screen
* Multilingual support for English, French, Chinese, and many more

## Showcase

### Add a new Note
<p align="center">
  <img src="https://user-images.githubusercontent.com/26352502/56068112-a1344b00-5d4b-11e9-9518-27b01131bbfc.gif" alt="Add a Note">
</p>

### Open and view a Note
<p align="center">
  <img src="https://user-images.githubusercontent.com/26352502/56067690-16068580-5d4a-11e9-85d1-8538b64ad620.gif" alt="Open a Note">
</p>

### Edit an existing Note
<p align="center">
  <img src="https://user-images.githubusercontent.com/26352502/56067914-ee63ed00-5d4a-11e9-97ec-fdd7a1ee1e16.gif" alt="Edit a Note">
</p>

### Multilingual support

### Other features
Mutlti-select to Delete            |  Extensive support for a variety of languages and locales
:---------------------------------:|:----------------------------------------------------------:
![](https://...Ocean.png)  |  ![](https://...Dark.png)

## Libraries Used

* [Android Jetpack][0] - Collection of libraries that reduce boilerplate code and simplifies complex tasks.
  * Foundation - Components for backwards compatibility, testing and Kotlin language support.
    * [AppCompat][1] - AppCompatActivity offers support for ActionBar.
    * [Android KTX][2] - Can write more concise, idiomatic Kotlin code.
    * [Test][3] - An Android testing framework for unit and runtime UI tests.
  * [Android Architecture][4] - Keeps app more robust, maintainable, and testable.
    * [Data Binding][5] - Declaratively bind observable data sources to UI components.
    * [Lifecycles][6] - Components respond automatically to lifecycle events.
    * [LiveData][7] - Data objects that notify views when the underlying database changes.
    * [Navigation][8] - Keeps navigation between fragments simple.
    * [Room][9] - Provides layer of abstraction over SQLite for more robust database access.
    * [ViewModel][10] - Store UI-related data between state changes and handles fragment to fragment communication. Easily schedule asynchronous tasks.
  * UI - Widgets and helpers to make app presentable and clean.
    * [Animations & Transitions][11] - Transition between fragments and events.
    * [Fragment][12] - A basic, reusable piece of composable UI.
    * [Layout][13] - Organize widgets in a variety of ways. 
* [Kotlin][20]
  * [Kotlin Coroutines][21] for managing background threads with simplified code and reducing needs for callbacks

[0]: https://developer.android.com/jetpack
[1]: https://developer.android.com/topic/libraries/support-library/packages#v7-appcompat
[2]: https://developer.android.com/kotlin/ktx
[3]: https://developer.android.com/training/testing/
[4]: https://developer.android.com/topic/libraries/architecture
[5]: https://developer.android.com/topic/libraries/data-binding/
[6]: https://developer.android.com/topic/libraries/architecture/lifecycle
[7]: https://developer.android.com/topic/libraries/architecture/livedata
[8]: https://developer.android.com/topic/libraries/architecture/navigation/
[9]: https://developer.android.com/topic/libraries/architecture/room
[10]: https://developer.android.com/topic/libraries/architecture/viewmodel
[11]: https://developer.android.com/training/animation/
[12]: https://developer.android.com/guide/components/fragments
[13]: https://developer.android.com/guide/topics/ui/declaring-layout
[20]: https://kotlinlang.org/
[21]: https://kotlinlang.org/docs/reference/coroutines-overview.html

## License
Notes is under the MIT license. See the [LICENSE](LICENSE) for more information.
