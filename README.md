
## Popular Movies Stage 2

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a678a232f2514bafb562f3f05d8fe191)](https://www.codacy.com/app/jimfo/Popular_Movies_Stage_2?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jimfo/Popular_Movies_Stage_2&amp;utm_campaign=Badge_Grade)

Popular Movies is the second Udacity project challenge. This part of the project queries TMDB for Trailers and Reviews and saves Favorites to a DB. Favorites are saved to the database using ORM (Object Relational Mapping) via Android's Room. Favorite movies are inserted and deleted in real time using LiveData.

### Stage 1

Stage 1 is the first part of the project challenge and can be found here. [Stage 1](https://github.com/jimfo/Popular_Movies_Stage_1)

### Stage 2
In this stage you’ll add additional functionality to the app you built in Stage 1.
You’ll add more information to your movie details view:

You’ll allow users to view and play trailers ( either in the youtube app or a web browser).
You’ll allow users to read reviews of a selected movie.
You’ll also allow users to mark a movie as a favorite in the details view by tapping a button(star).
You'll create a database to store the names and ids of the user's favorite movies (and optionally, the rest of the information needed to display their favorites collection while offline).
You’ll modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.

## Rubric

Meets Specifications
Good job with the project.
You did an amazing work here and I can see that you are able to implement the concepts taught very well, keep it up! 👏🏼👏🏼

Best!

### Common Project Requirements
- [x] App is written solely in the Java Programming Language.

- [x] App conforms to common standards found in the Android Nanodegree General Project Guidelines.

- [x] App utilizes stable release versions of all libraries, Gradle, and Android Studio.

### User Interface - Layout
- [x] UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.

- [x] Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.

- [x] UI contains a screen for displaying the details for a selected movie.

- [x] Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.

- [x] Movie Details layout contains a section for displaying trailer videos and user reviews.

### User Interface - Function
- [x] When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.

- [x] When a movie poster thumbnail is selected, the movie details screen is launched.

- [x] When a trailer is selected, app uses an Intent to launch the trailer.

- [x] In the movies detail screen, a user can tap a button (for example, a star) to mark it as a Favorite. Tap the button on a favorite movie will unfavorite it.

### Network API Implementation
- [x] In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.

- [x] App requests for related videos for a selected movie via the /movie/{id}/videos endpoint in a background thread and displays those details when the user selects a movie.

- [x] App requests for user reviews for a selected movie via the /movie/{id}/reviews endpoint in a background thread and displays those details when the user selects a movie.

### Data Persistence
- [x] The titles and IDs of the user’s favorite movies are stored in a native SQLite database and exposed via a ContentProvider
OR
stored using Room.

- [x] Data is updated whenever the user favorites or unfavorites a movie. No other persistence libraries are used.

- [x] When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the database.

### Android Architecture Components
- [x] If Room is used, database is not re-queried unnecessarily. LiveData is used to observe changes in the database and update the UI accordingly.

- [x] If Room is used, database is not re-queried unnecessarily after rotation. Cached LiveData from ViewModel is used instead.


#### Main Activity
Popular Movies is chosen


![1](https://user-images.githubusercontent.com/5784029/41992640-0d8ba4ce-7a17-11e8-929e-55f459809f9b.png)'


#### Detail Activity

![3](https://user-images.githubusercontent.com/5784029/41992645-11ebea9c-7a17-11e8-8b74-fa8f005b8a14.png)

<a href="https://imgflip.com/gif/2da26b"><img src="https://i.imgflip.com/2da26b.gif" title="made at imgflip.com"/></a>

[![ForTheBadge built-by-developers](http://ForTheBadge.com/images/badges/built-by-developers.svg)](https://GitHub.com/Naereen/)
