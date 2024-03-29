package com.landfathich.dreamfilm

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.landfathich.dreamfilm.Fragments.FavoritesFragment
import com.landfathich.dreamfilm.Fragments.HomeFragment
import com.landfathich.dreamfilm.MovieRepo.Singleton.databaseRef
import com.landfathich.dreamfilm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val repo = MovieRepo()

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val callbackConf: () -> Unit = {
            val callbacktrend: () -> Unit = {
                val callbackPop: () -> Unit = {
                    loadFragment(HomeFragment(this))
                }
                repo.fetchPopularMoviesFromTMDB("f822ee258fb1f07774b09e5ba51e04e7" , callbackPop)
            }
            repo.fetchTrendingMoviesFromTMDB("f822ee258fb1f07774b09e5ba51e04e7" , callbacktrend)
        }
        repo.fetchTmdbConfig("f822ee258fb1f07774b09e5ba51e04e7" ,callbackConf)

        //repo.fetchPopularMoviesFromTMDB("f822ee258fb1f07774b09e5ba51e04e7")
        //loadFragment(HomeFragment(this))

        //geth the navigation view
        val navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.Home_navigation -> {
                    loadFragment(HomeFragment(this))
                    Log.d(" home_fragement ", "git into the home menu")
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.Favorites_navigation -> {
                    loadFragment(FavoritesFragment(this))
                    Log.d(" fav_fragement ", "git into the fav menu")
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }
    private fun loadFragment(fragment: Fragment){
        Log.d(" load_function ", fragment.toString())
        val myCallback: () -> Unit = {
            val transaction = supportFragmentManager.beginTransaction()
            Log.d(" callback_function ", fragment.toString())
            transaction.replace(R.id.fragment_container , fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        //repo.fetchTmdbConfig("f822ee258fb1f07774b09e5ba51e04e7")
        repo.PopularMovies(myCallback)
        /*
        if (fragment is HomeFragment) {
            // If the fragment is HomeFragment, do something
            repo.fetchTmdbConfig("f822ee258fb1f07774b09e5ba51e04e7")
            repo.fetchTrendingMoviesFromTMDB("f822ee258fb1f07774b09e5ba51e04e7")

        } else {
            repo.updateData(myCallback)
        }

         */

    }
}
