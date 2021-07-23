package com.example.gameit

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.gameit.databinding.ActivityMainBinding
import com.example.gameit.ui.*


class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding

    var nick: String? = ""

    private var TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        nick = intent.getStringExtra("nick")

        Log.v(TAG,"$nick")

        val bundle = Bundle()
        bundle.putString("nick","$nick")

        val fragobj = ProfileFragment()
        fragobj.arguments = bundle

        appBar()

        b.fab.setOnClickListener {
            goToFragment(EventFragment())
        }

        b.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_find -> {
                    goToFragment(FindFragment())
                    true
                }
                R.id.action_chat -> {
                    goToFragment(ChatFragment())
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
        b.bottomNavigationView.selectedItemId = R.id.action_profile
    }

    private fun appBar() {

        val actionBar: ActionBar? = supportActionBar

        supportActionBar?.title =
            Html.fromHtml("<font color=\"yellow\">" + resources.getString(R.string.app_name) + "</font>")

        actionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.black)))
    }


    fun goToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit()
    }
}