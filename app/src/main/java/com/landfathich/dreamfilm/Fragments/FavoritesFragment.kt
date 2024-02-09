package com.landfathich.dreamfilm.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.landfathich.dreamfilm.MainActivity
import com.landfathich.dreamfilm.MovieRepo
import com.landfathich.dreamfilm.MovieRepo.Singleton.movieFav
import com.landfathich.dreamfilm.MovieRepo.Singleton.movieList
import com.landfathich.dreamfilm.MovieRepo.Singleton.movieListTrend
import com.landfathich.dreamfilm.R
import com.landfathich.dreamfilm.adapter.MovieAdapter

class FavoritesFragment (private val context : MainActivity) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites , container , false)

        // get the recycler view
        val favoritesRecyclerView = view.findViewById<RecyclerView>(R.id.favorites_recycler_view)
        favoritesRecyclerView.adapter = MovieAdapter(context , movieFav.filter { it.liked }, R.layout.vertical_movie_item)

        return view
    }

}