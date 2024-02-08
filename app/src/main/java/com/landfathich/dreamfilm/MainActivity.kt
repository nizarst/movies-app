package com.landfathich.dreamfilm

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.landfathich.dreamfilm.Fragments.HomeFragment
import com.landfathich.dreamfilm.MovieRepo.Singleton.databaseRef
import com.landfathich.dreamfilm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // get the data from the database
        val repo = MovieRepo()
        val myCallback: () -> Unit = {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container , HomeFragment(this))
            transaction.addToBackStack(null)
            transaction.commit()
        }

        repo.fetchTmdbConfig("f822ee258fb1f07774b09e5ba51e04e7")
        repo.fetchTrendingMoviesFromTMDB("f822ee258fb1f07774b09e5ba51e04e7")
        repo.PopularMovies(myCallback)



        /*
        repo.updateData{
            //inflate the home fragement into the main view once the data is collected
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container , HomeFragment(this))
            transaction.addToBackStack(null)
            transaction.commit()
        }

         */
    }
}