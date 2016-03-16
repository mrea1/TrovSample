#TwitterClone

##Summary

TwitterClone is a proof of concept app that displays a list of sample tweets, much like the real Twitter app.

##App Architecture

###Network

This app uses Retrofit to simplify REST API calls. Since we do not yet have a live API, the callbacks are mocked with .JSON files for both successful and unsuccessful responses. The successful JSON file contains 16 JSON objects representing Tweets. I extended okhttp3.Interceptor with the class LocalResponseInterceptor. This class intercepts the calls made by retrofit. This is a good way to test the entire network functionality of the app while staying local.

The retrofit interface is housed in the ApiManager class. Since Retrofit is used in more than one activity, I decided to use the Singleton pattern to ensure that only one instance of Retrofit could be instantiated at one time — as it is an expensive object.



###UI

The UI is based on material design. The toolbar, navigation bar, and UI components are styled using the colors defined in styles.xml. The app contains a login screen and a newsfeed screen. The login screen contains text boxes for email and password. The textboxes display hints by default. The textboxes also validate the input provided by the user. The third-party library Butterknife is lightly used to take care of boilerplate code for UI bindings. 

After the login button is pressed, a call to the API is made. A user object is returned by the API. This User object is then sent to the NewsfeedActivity, which hands it off to the NewsfeedFragment. The NewsfeedFragment inflates a layout containing a RecyclerView. An API call is made to retrieve a list of tweets. This list is passed to a custom RecyclerView.Adapter. The options menu on the fragment contains two items. The first removes all items from the RecyclerView and from the database. The second reloads the example .json file. 

The NewsfeedFragment layout also contains a FloatingActionButton (FAB). This button follows Material Design with correct elevation and location on the screen. The button allows the logged in user to add a new tweet to the current list. The RecyclerView automatically scrolls to the bottom in order to display the new tweet. The tweet is displayed with the current time. An API call is made to post the new tweet using the user’s credentials.

###Data

All tweets obtained from the API are stored in Tweet model objects. The GSON library is used to serialize the JSON response into model objects. The model objects are persisted to a SQLite database using the SugarORM library. When the NewsfeedActivity is created, the app first checks the database for tweets before calling the API. Also, when a tweet is added by the user, the tweet is immediately saved to the database.

Another alternative ORM that could be used here is GreenDao. It is faster and more efficient. SugarORM was chosen here due to ease of implementation.

###Testing

A series of unit tests have been developed into the app. The unit tests use the Robolectric library for UI, functionality, and data testing. There are tests for LoginActivity and NewsfeedActivity. Each activity is initially created and tested for a null value. All results of network calls are tested for the correct values, as stubbed in the .json files.  All buttons are tested for the correct functionality. The database is also tested.

While this set of tests cover most functionality in the app, improvements could be made. Further unit tests could be written to test all possibilities on network calls. Additionally, a mocking library such as Mockito could be used to write more complex tests.

Unfortunately, I ran into some issues getting Robolectric to play nice with Android M. This is a known issue within Robolectric. I implemented some workarounds, but I did not have time to figure it all out. I could solve this by compiling with SDK 21 and remove all SDK 22/23 code. I also could use Espresso, Google’s testing library. I have a bit of experience with it, and it looks to be pretty powerful and well supported within Android.
