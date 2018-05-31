# Popular_Movies

Popular Movies is the second Udacity project challenge. The project queries TMDB (The Movie Database) API 
for Top Rated and Popular Movies and parses the JSON response. While parsing the JSON response an ArrayList
of Movie objects is created ad returned to Main Activity. A custom array adapter is used to retrieve the
movie poster path and display each movie's poster in it's corresponding GridView cell using Picasso.

![1](https://user-images.githubusercontent.com/5784029/40395370-b8acd0a0-5df5-11e8-8ca4-e8a2a05ea756.png)

A Setting Menu allows the user to select between Popular Movies and Top Rated movies to display in the
GridView. (Favorites option has not been implemented yet.)

![2](https://user-images.githubusercontent.com/5784029/40395598-e020058e-5df6-11e8-812f-69bd6697128d.png)

Clicking a movie image in the GridView takes the user to the Movie Detail Activty and displays data about
the selected movie. The Status Bar and Action Bar display the average color of the movie backdrop image
using an algorithm that averages the pixel colors. Palette with Picasso sometimes generates an empty
swatch so I chose to use the average color algorithm.

![screenshot_2018-05-27-23-12-18_resized](https://user-images.githubusercontent.com/5784029/40595664-fe9acdc6-6203-11e8-8380-8dbe0edbfa73.png)
