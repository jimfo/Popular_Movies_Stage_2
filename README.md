# Popular_Movies Stage 2

Popular Movies is the second Udacity project challenge. This part of the project queries TMDB for Trailers and Reviews and saves Favorites to a DB. Favorites are saved to the database using ORM (Object Relational Mapping) via Android's Room. Favorite movies are inserted and deleted in real time using LiveData.

# Stage 1

Stage 1 is the first part of the project challenge and can be found here. (Link to be inserted)

# Stage 2
In this stage you’ll add additional functionality to the app you built in Stage 1.
You’ll add more information to your movie details view:

You’ll allow users to view and play trailers ( either in the youtube app or a web browser).
You’ll allow users to read reviews of a selected movie.
You’ll also allow users to mark a movie as a favorite in the details view by tapping a button(star).
You'll create a database to store the names and ids of the user's favorite movies (and optionally, the rest of the information needed to display their favorites collection while offline).
You’ll modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.

# Rubric

Meets Specifications
Good job with the project.
You did an amazing work here and I can see that you are able to implement the concepts taught very well, keep it up! 👏🏼👏🏼

Best!

### Common Project Requirements
-[x] App is written solely in the Java Programming Language.

App conforms to common standards found in the Android Nanodegree General Project Guidelines.

App utilizes stable release versions of all libraries, Gradle, and Android Studio.

### User Interface - Layout
UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.

Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.

UI contains a screen for displaying the details for a selected movie.

Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.

Movie Details layout contains a section for displaying trailer videos and user reviews.

### User Interface - Function
When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.

When a movie poster thumbnail is selected, the movie details screen is launched.

When a trailer is selected, app uses an Intent to launch the trailer.

In the movies detail screen, a user can tap a button (for example, a star) to mark it as a Favorite. Tap the button on a favorite movie will unfavorite it.

### Network API Implementation
In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.

App requests for related videos for a selected movie via the /movie/{id}/videos endpoint in a background thread and displays those details when the user selects a movie.

App requests for user reviews for a selected movie via the /movie/{id}/reviews endpoint in a background thread and displays those details when the user selects a movie.

### Data Persistence
The titles and IDs of the user’s favorite movies are stored in a native SQLite database and exposed via a ContentProvider
OR
stored using Room.

Data is updated whenever the user favorites or unfavorites a movie. No other persistence libraries are used.

When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the database.

### Android Architecture Components
If Room is used, database is not re-queried unnecessarily. LiveData is used to observe changes in the database and update the UI accordingly.

If Room is used, database is not re-queried unnecessarily after rotation. Cached LiveData from ViewModel is used instead.

![1](https://user-images.githubusercontent.com/5784029/40395370-b8acd0a0-5df5-11e8-8ca4-e8a2a05ea756.png)

A Setting Menu allows the user to select between Popular Movies and Top Rated movies to display in the
GridView. (Favorites option has not been implemented yet.)

![2](https://user-images.githubusercontent.com/5784029/40395598-e020058e-5df6-11e8-812f-69bd6697128d.png)

Clicking a movie image in the GridView takes the user to the Movie Detail Activty and displays data about
the selected movie. The Status Bar and Action Bar display the average color of the movie backdrop image
using an algorithm that averages the pixel colors. Palette with Picasso sometimes generates an empty
swatch so I chose to use the average color algorithm.

![screenshot_2018-05-27-23-12-18_resized](https://user-images.githubusercontent.com/5784029/40595664-fe9acdc6-6203-11e8-8380-8dbe0edbfa73.png)
