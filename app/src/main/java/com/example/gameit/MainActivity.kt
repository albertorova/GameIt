package com.example.gameit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.gameit.ui.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // binding = ActivityMainBinding.inflate(layoutInflater)
       // setContentView(binding.root)


        bottom_navigation_view.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_find -> {
                    goToFragment(FindFragment())
                    true
                }
                R.id.action_chat -> {
                    goToFragment(ChatFragment())
                    true
                }
                R.id.action_events -> {
                    goToFragment(EventFragment())
                    true
                }
                R.id.action_dashboard -> {
                    goToFragment(DashboardFragment())
                    true
                }
                R.id.action_profile -> {
                    goToFragment(ProfileFragment())
                    true
                }



                else -> false
            }
        }
        bottom_navigation_view.selectedItemId = R.id.action_profile
    }
    fun goToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit()
    }
}