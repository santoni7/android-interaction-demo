# Android Application Interaction Demo
## Author
Anton Sakhniuk:  anton.sakhniuk@gmail.com
## Dependencies
`RxJava 2.2.2`, `ButterKnife 8.8.1`, `Room 1.1.1`, `Dagger 2.11`
## Features
- Data updates are displayed instantly in AppA HistoryView due to rxJava
- Orientation change is supported, view state is saved
- App B CountdownActivity: custom pulsing ring animation for timer
- Disappearing sorting icon on ActivityA AppBar
- Uses Dagger to allow easily replace some components
## Screenshots
<img border="1" width="250" src="/screenshots/test_view.jpg" alt="App B"/> <img width="250" src="/screenshots/history_view.jpg" alt="App B"/> <img width="250" src="/screenshots/app_b.jpg" alt="App B"/>
<img width="250" src="/screenshots/notification.jpg" alt="App B"/>


## Package structure
        com.santoni7.interactiondemo
            ├── app_a  ------------------------------------ Application A
            │   ├── activity  
            │   │   ├── ActivityA.java     - ViewPager with Test and History screens
            │   │   ├── ContractA.java     - View and Presenter contracts
            |   │   ├── PagerState.java    - Enum state of ViewPager (Test screen or HistoryScreen)
            |   │   └── PresenterA.java 
            |   ├── adapter
            |   │   └── HistoryAdapter.java     - RecyclerView Adapter
            |   ├── data
            |   │   ├── ImageLinkDao.java       - Database data-access-object
            |   │   ├── ImageLinkDatabase.java  - Room Database 
            |   │   ├── LinkRepository.java     - Provides ActivityA with needed content. Does not modify database
            |   │   └── LinkSortingOrderEnum.java  - Describes ways to sort links, provides comparators
            |   ├── fragment
            |   │   ├── HistoryFragment.java    - Implements ContractA.View.HistoryView; Displays list of ImageLinks.
            |   │   └── TestFragment.java       - Implements ContractA.View.TestView; Url input & validation.
            |   ├── injection      --- Dagger2 components
            |   │   ├── AppAScope.java
            |   │   ├── ComponentA.java
            |   │   ├── ContextModule.java      - provides context & Thread.UnhandledExceptionHandler
            |   │   ├── DataModule.java         - provides Room database and LinkRepository
            |   │   └── PresenterModule.java    - provides main presenter
            |   ├── provider 
            |   │   └── LinkContentProvider.java  - A content provider lets other applications access ImageLinkDatabase
            |   ├── ApplicationA.java
            |   └── ClipboardExceptionHandler.java
            |
            |
            ├── app_b  -------------------------------- Application B
            |   ├── activity
            |   │   ├── ActivityB.java          - Loads image, displays it, and modifies it's status in database.
                                                - Schedules ContentIntentService execution using AlarmManager
            |   │   ├── ContractB.java          - View and Presenter interfaces
            |   │   ├── CountdownActivity.java  - Launcher activity. Displays animated timer and shuts down
            |   │   └── PresenterB.java         - Main presenter implementation in App B
            |   ├── data
            |   │   ├── LinkContentRepository.java  - A class that accesses App A's database through ContentResolver
                                                    - Provides sync and async methods
            |   │   ├── BitmapSource.java       - Downloads image from url, returns observable image.
            |   │   └── RemoteBitmapSource.java - BitmapSource implementation using URL.openConnection()
            |   ├── injection
            |   │   ├── AppBScope.java
            |   │   ├── ComponentB.java
            |   │   ├── ContextModule.java    - Provides app context
            |   │   ├── DataModule.java       - Provides BitmapSource and LinkContentRepository
            |   │   └── PresenterModule.java  - Provides ContractB.Presenter
            |   ├── service
            |   |   └── ContentIntentService.java  - This service has 2 actions: delete link from database, 
                                                   - and save image to external storage. 
            |   ├── ApplicationB.java
            |   └── Constants.java
            |
            |
            └── lib  ---------------------------- Common Library Module:
                |    ----------------------- Contains ImageLink model, Constants for ContentProvider, base classes
                ├── AndroidUtils.java
                ├── CommonConst.java
                ├── converters
                │   ├── DateConverter.java
                │   └── StatusConverter.java
                ├── model
                │   └── ImageLink.java
                └── mvp
                    ├── MvpPresenter.java
                    ├── MvpView.java
                    └── PresenterBase.java
