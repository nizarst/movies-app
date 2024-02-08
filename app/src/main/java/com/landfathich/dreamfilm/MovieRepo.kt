package com.landfathich.dreamfilm

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.landfathich.dreamfilm.MovieRepo.Singleton.databaseRef
import com.landfathich.dreamfilm.MovieRepo.Singleton.movieList
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.Gson
import com.landfathich.dreamfilm.MovieRepo.Singleton.movieListTrend
import com.landfathich.dreamfilm.adapter.MovieAdapter

class MovieRepo {
    // connect to the movie database

    object Singleton {
        val databaseRef = Firebase.database.getReference("Movies")
        var imageBaseUrl: String = "" // Store the base URL for images
        val movieList = arrayListOf<MovieModel>()
        val movieListTrend = arrayListOf<MovieModel>()
    }

    val existingMovieIds = mutableSetOf<String>()

    // defining the schema of the data we are fetching from TMDB
    data class TmdbConfigResponse(val images: ImagesConfig)
    data class ImagesConfig(val secure_base_url: String, val base_url: String)

    data class MovieResponse(val results: List<Movie>)
    data class Movie(
        val title: String,
        val overview: String,
        val id:Int,
        val release_date: String,
        val poster_path: String,
        val original_language: String,
        val popularity : Double
    )

    fun fetchTmdbConfig(apiKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val url = URL("https://api.themoviedb.org/3/configuration?api_key=$apiKey")
            val connection = url.openConnection() as HttpURLConnection
            try {
                val data = connection.inputStream.bufferedReader().readText()
                val configResponse = Gson().fromJson(data, TmdbConfigResponse::class.java)
                Singleton.imageBaseUrl = configResponse.images.secure_base_url // Or use base_url
                Log.d("ConfigFetch", "Base Image URL fetched: ${Singleton.imageBaseUrl}")
            } catch (e: Exception) {
                Log.e("ConfigFetch", "Error fetching TMDB config", e)
            } finally {
                connection.disconnect()
            }
        }
    }

    fun fetchPopularMoviesFromTMDB(apiKey: String , callback : () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val url = URL("https://api.themoviedb.org/3/movie/popular?api_key=$apiKey&language=en-US&page=1")
            val connection = url.openConnection() as HttpURLConnection
            try {
                val data = connection.inputStream.bufferedReader().readText()
                val movieResponse = Gson().fromJson(data, MovieResponse::class.java)
                movieList.clear()
                movieResponse.results.forEach { movie ->
                    val posterUrl = Singleton.imageBaseUrl + "w185" + movie.poster_path
                    val isLiked = existingMovieIds.contains(movie.id.toString())
                    val mappedMovie = MovieModel(
                        id = movie.id.toString(),
                        name = movie.title,
                        description = movie.overview,
                        imageUrl = posterUrl,
                        liked = isLiked,
                        releaseDate = movie.release_date,
                        popularity = movie.popularity
                    )
                    // check if the movie is already in the database , if so we give it the value liked in the
                    // DB , if not then we give it false


                    movieList.add(mappedMovie)
                    Log.d("MovieFetchPopular", "Title: ${movie.popularity}, Poster URL: $posterUrl")
                }
                // Update your movie list or UI accordingly on the main thread
                withContext(Dispatchers.Main) {
                    callback()
                }
            } catch (e: Exception) {
                Log.e("MovieFetch", "Error fetching movies", e)
            } finally {
                connection.disconnect()
            }
        }
    }

    fun fetchTrendingMoviesFromTMDB(apiKey: String ) {
        CoroutineScope(Dispatchers.IO).launch {
            val url = URL("https://api.themoviedb.org/3/movie/top_rated?api_key=$apiKey&language=en-US&page=1")
            val connection = url.openConnection() as HttpURLConnection
            try {
                val data = connection.inputStream.bufferedReader().readText()
                val movieResponse = Gson().fromJson(data, MovieResponse::class.java)
                movieListTrend.clear()
                movieResponse.results.forEach { movie ->
                    val posterUrl = Singleton.imageBaseUrl + "w185" + movie.poster_path
                    val mappedMovie = MovieModel(
                        id = movie.id.toString(),
                        name = movie.title,
                        description = movie.overview,
                        imageUrl = posterUrl,
                        liked = false,
                        releaseDate = movie.release_date,
                        popularity = movie.popularity
                    )
                    movieListTrend.add(mappedMovie)
                    Log.d("MovieFetch", "Title: ${movie.popularity}, Poster URL: $posterUrl")
                }
                // Update your movie list or UI accordingly on the main thread
                withContext(Dispatchers.Main) {
                    // do nothing here since there is no callback
                }
            } catch (e: Exception) {
                Log.e("MovieFetch", "Error fetching movies", e)
            } finally {
                connection.disconnect()
            }
        }
    }


    fun PopularMovies(callback : () -> Unit){

        databaseRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                existingMovieIds.clear()
                snapshot.children.forEach {
                    val id = it.child("id").getValue(String::class.java)
                    if (id != null) {
                        existingMovieIds.add(id)
                    }
                }
                Log.d("get_ids", existingMovieIds.toString())
                fetchPopularMoviesFromTMDB("f822ee258fb1f07774b09e5ba51e04e7") { callback() }
                // After fetching existing movies, fetch from TMDB

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("failed_db", "Failed to read value.", error.toException())
            }
        })
    }

    fun updateData(callback : () -> Unit){
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // remove the items that were already imported
                movieList.clear()
                // get the data from the database
                for (datasnap in snapshot.children){
                    val movie = datasnap.getValue(MovieModel::class.java)
                    if (movie != null){
                        movieList.add(movie)
                        Log.d(movie.name , movie.description)
                    }
                }
                callback()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("failed_db", "Failed to read value.", error.toException())
            }
        })
    }

    fun UpdateMovie(movie : MovieModel){
        databaseRef.child(movie.id).setValue(movie)
    }

    fun deleteMovie(movie : MovieModel){
        databaseRef.child(movie.id).removeValue()
    }

    fun InsertMovie(movie : MovieModel){
        databaseRef.child(movie.id).setValue(movie)
    }

}