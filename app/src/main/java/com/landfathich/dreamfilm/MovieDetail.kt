package com.landfathich.dreamfilm

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.landfathich.dreamfilm.adapter.MovieAdapter

class MovieDetail(
    private val adapter : MovieAdapter,
    private val currentMovie :MovieModel
) : Dialog(adapter.context){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.movie_details)
        setupComponents()
        setupCloseButton()
        setupLikeButton()
    }

    private fun setupComponents() {
        val movieImage = findViewById<ImageView>(R.id.detail_image_item)
        Glide.with(adapter.context).load(Uri.parse(currentMovie.imageUrl)).into(movieImage)

        findViewById<TextView>(R.id.Movie_detail_Name).text = currentMovie.name
        findViewById<TextView>(R.id.Movie_Details_Description).text = currentMovie.description
        findViewById<TextView>(R.id.Movie_details_rating).text = currentMovie.popularity.toString()
    }

    private fun setupLikeButton() {

        if (currentMovie.liked){
            findViewById<ImageView>(R.id.ic_heart).setImageResource(R.drawable.filled_heart)
        }else{
            findViewById<ImageView>(R.id.ic_heart).setImageResource(R.drawable.ic_favorite)
        }

        findViewById<ImageView>(R.id.ic_heart).setOnClickListener {
            val repo = MovieRepo()
            currentMovie.liked = !currentMovie.liked
            Log.d("liked", currentMovie.liked.toString())
            if (currentMovie.liked){
                findViewById<ImageView>(R.id.ic_heart).setImageResource(R.drawable.filled_heart)
                repo.UpdateMovie(currentMovie)
            }else{
                findViewById<ImageView>(R.id.ic_heart).setImageResource(R.drawable.ic_favorite)
                repo.deleteMovie(currentMovie)
            }


            /*
            val movie_status = currentMovie.liked
            if (movie_status){
                // before clicking the button , it was liked
                findViewById<ImageView>(R.id.ic_heart).setImageResource(R.drawable.ic_favorite)
                repo.deleteMovie(currentMovie)
            }else{
                findViewById<ImageView>(R.id.ic_heart).setImageResource(R.drawable.filled_heart)
                repo.InsertMovie(currentMovie)
            }

             */
        }
    }


    private fun setupCloseButton() {
        findViewById<ImageView>(R.id.ic_detail_close).setOnClickListener{
            dismiss()
        }
    }


}