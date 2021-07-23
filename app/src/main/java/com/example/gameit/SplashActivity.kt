package com.example.gameit

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.gameit.databinding.ActivitySplashBinding
import com.squareup.picasso.Picasso

class SplashActivity : AppCompatActivity() {

    private lateinit var b: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(b.root)

        supportActionBar?.hide()

        initSplash()

        initProgressbar()

        delayTime()
    }

    private fun initSplash() {
        when ((1..5).random()) {
            1 -> {
                Picasso.get()
                    .load("https://areajugones.sport.es/wp-content/uploads/2019/10/call-of-duty-mobile-record.jpg")
                    .into(b.imageSplash)
            }
            2 -> {
                Picasso.get()
                    .load("https://p4.wallpaperbetter.com/wallpaper/1003/987/595/pubg-player-unknown-battleground-players-hd-wallpaper-preview.jpg")
                    .into(b.imageSplash)
            }
            3 -> {
                Picasso.get()
                    .load("https://e00-marca.uecdn.es/assets/multimedia/imagenes/2019/03/28/15537334218713.jpg")
                    .into(b.imageSplash)
            }
            4 -> {
                Picasso.get()
                    .load("https://codigoesports.com/wp-content/uploads/2021/05/fortnite-impossible-odds-loading-screen-1920x1080-83836a2465bb.jpg")
                    .into(b.imageSplash)
            }
            5 -> {
                Picasso.get()
                    .load("https://esports.eldesmarque.com/wp-content/uploads/2021/03/Stutu-1.jpg")
                    .into(b.imageSplash)
            }
        }
    }

    private fun delayTime() {
        val handler = Handler()
        handler.postDelayed({

            b.progressbar.isVisible = false

            b.comenzar.isVisible = true

            irALogin()

        }, 1000)
    }

    private fun irALogin() {

        b.comenzar.setOnClickListener {

            val intent = Intent()
            intent.setClass(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initProgressbar() {
        b.progressbar.max = 100

        val currentProgress = 0

        ObjectAnimator.ofInt(b.progressbar, "progress", currentProgress)
            .setDuration(1000)
            .start()
    }

}