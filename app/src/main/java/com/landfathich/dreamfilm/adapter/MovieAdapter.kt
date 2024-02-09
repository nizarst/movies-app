package com.landfathich.dreamfilm.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.landfathich.dreamfilm.MainActivity
import com.landfathich.dreamfilm.MovieDetail
import com.landfathich.dreamfilm.MovieModel
import com.landfathich.dreamfilm.MovieRepo
import com.landfathich.dreamfilm.R

class MovieAdapter(
    val context : MainActivity,
    private val movielist : List<MovieModel>,
    private val layoutId : Int
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val movieImage = view.findViewById<ImageView>(R.id.image_item)
        val MovieName : TextView? = view.findViewById(R.id.Movie_Name)
        val MoviePopularity : TextView? = view.findViewById(R.id.Movie_popularity_value)
        val MovieReleaseDate : TextView? = view.findViewById(R.id.Release_date)
        var heart_icon : ImageView? = view.findViewById(R.id.ic_heart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater
           .from(parent.context)
           .inflate(layoutId , parent , false)

        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return movielist.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = MovieRepo()

        val currentMovie = movielist[position]

        // use glide to get the images of the
        Glide.with(context).load(Uri.parse(currentMovie.imageUrl)).into(holder.movieImage)
        holder.MovieName?.text = currentMovie.name
        holder.MovieReleaseDate?.text = currentMovie.releaseDate
        holder.MoviePopularity?.text = currentMovie.popularity.toString()

        if (currentMovie.liked){
            holder.heart_icon?.setImageResource(R.drawable.filled_heart)
        }else{
            holder.heart_icon?.setImageResource(R.drawable.ic_favorite)
        }

        holder.heart_icon?.setOnClickListener{
            currentMovie.liked = !currentMovie.liked
            if (currentMovie.liked){
                holder.heart_icon?.setImageResource(R.drawable.filled_heart)
                repo.UpdateMovie(currentMovie)
            }else{
                holder.heart_icon?.setImageResource(R.drawable.ic_favorite)
                repo.deleteMovie(currentMovie)
            }
            //notifyDataSetChanged()
        }

        // the moment we click on the movie

        holder.itemView.setOnClickListener{
            MovieDetail(this, currentMovie).show()
        }
    }
}