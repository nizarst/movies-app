package com.landfathich.dreamfilm.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.landfathich.dreamfilm.MainActivity
import com.landfathich.dreamfilm.MovieModel
import com.landfathich.dreamfilm.MovieRepo.Singleton.movieList
import com.landfathich.dreamfilm.MovieRepo.Singleton.movieListTrend
import com.landfathich.dreamfilm.R
import com.landfathich.dreamfilm.adapter.MovieAdapter

class HomeFragment(
    private val context : MainActivity
) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // this is where we create our dataset (this should be coming from the database I would suggest


        val view = inflater.inflate(R.layout.fragement_home , container , false)

        val horizontalRecyclerView = view.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView.adapter = MovieAdapter(context , movieListTrend , R.layout.item_horizontal_movie)

        // get the second recycler view
        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView.adapter = MovieAdapter(context , movieList , R.layout.vertical_movie_item)

        return view
    }
}