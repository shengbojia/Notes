<h1 align="center"> Notes </h1> <br>

<p align="center">
  Android app for keeping track of ideas, on the go.
</p>

<p align="center">
  <img src="https://user-images.githubusercontent.com/26352502/56065548-29fab900-5d43-11e9-80d7-f16ab3b97061.gif">
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
* A note can have a title, short description, and a priority rating
* Notes are listed in chronological order
* Quickly clear all notes from the main screen

## Showcase

### Add a new Note
<p align="center">
  <img src="https://user-images.githubusercontent.com/26352502/54404054-a51b7180-46a8-11e9-9a83-606bce51ea0c.gif" alt="Add a Note">
</p>

### Open and view a Note
<p align="center">
  <img src="https://user-images.githubusercontent.com/26352502/54402838-08ef6b80-46a4-11e9-8792-83dde17ed0b3.gif" alt="Open a Note">
</p>

### Edit an existing Note
<p align="center">
  <img src="https://user-images.githubusercontent.com/26352502/54403855-f119e680-46a7-11e9-8022-45ebcb189fb3.gif" alt="Edit a Note">
</p>

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
