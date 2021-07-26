package com.example.gameit

import android.R
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.gameit.databinding.ActivitySplashBinding
import com.squareup.picasso.Picasso


class SplashActivity : AppCompatActivity() {

    var t: Long = 1000

    private lateinit var b: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(b.root)

        supportActionBar?.hide()

        initSplash()

        initProgressbar()

        delayTimeProgress()

    }


    private fun initSplash() {
        when ((1..9).random()) {
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
                    .load("https://img.redbull.com/images/c_fill,w_1500,h_1000,g_auto,f_auto,q_auto/redbullcom/2016/02/09/1331775749506_2/sigue-el-ejemplo-del-maestro-de-tuercas-hearthstone")
                    .into(b.imageSplash)
            }
            4 -> {
                Picasso.get()
                    .load("https://play-lh.googleusercontent.com/Ukery3Xbs_XGQqVdZbVaWPWU6rgEYG1uQPlrpJ2x5JJ5byIRVpE2mim_U5Xw4iYF_YY")
                    .into(b.imageSplash)
            }
            5 -> {
                Picasso.get()
                    .load("https://static-assets-prod.epicgames.com/fortnite/static/webpack/8f9484f10eb14f85a189fb6117a57026.jpg")
                    .into(b.imageSplash)
            }

            6 -> {
                Picasso.get()
                    .load("https://pbs.twimg.com/media/E5nFHxIWUAARUYj.jpg")
                    .into(b.imageSplash)
            }

            7 -> {
                Picasso.get()
                    .load("https://i.blogs.es/65f795/clashroyale0/840_560.jpg")
                    .into(b.imageSplash)
            }

            8 -> {
                Picasso.get()
                    .load("https://esports.as.com/2021/03/30/league-of-legends/League-Legends-muestra-Gwen-campeona_1450964916_647560_1024x576.jpg")
                    .into(b.imageSplash)
            }

            9 -> {
                Picasso.get()
                    .load("https://www.elplural.com/uploads/s1/11/05/70/0/brawl-stars-i-actualizacio-n.jpeg")
                    .into(b.imageSplash)
            }

        }
    }

    private fun delayTimeProgress() {

        val h1 = Handler()

        h1.postDelayed({

            b.progressbar.isVisible = false

            b.comenzar.isVisible = true

            irALogin()

        }, t)

        h1.postDelayed({

            b.tv1.text = "Actualizando librerias"

        }, 700)


        h1.postDelayed({

            b.tv1.text = "Cargando los cargamentos"

        }, 2200)



        h1.postDelayed({

            b.tv1.text = "Inicializando lo inicializable"

        }, 4000)

        h1.postDelayed({


            b.tv1.text = "Coloreando los colores"

        }, 6700)



        h1.postDelayed({

            b.tv1.text = "Empaquetando los paquetes"

        }, 8300)

        h1.postDelayed({

            b.tv1.text = ""

        }, 10000)

        h1.postDelayed({

            b.tv2.text = "."

        }, 500)

        h1.postDelayed({

            b.tv2.text = ". ."

        }, 1000)

        h1.postDelayed({

            b.tv2.text = ". . ."

        }, 1500)

        h1.postDelayed({

            b.tv2.text = "."

        }, 2000)

        h1.postDelayed({

            b.tv2.text = ". ."

        }, 2500)

        h1.postDelayed({

            b.tv2.text = ". . ."

        }, 3000)

        h1.postDelayed({

            b.tv2.text = "."

        }, 3500)

        h1.postDelayed({

            b.tv2.text = ". ."

        }, 4000)

        h1.postDelayed({

            b.tv2.text = ". . ."

        }, 4500)

        h1.postDelayed({

            b.tv2.text = "."

        }, 5000)

        h1.postDelayed({

            b.tv2.text = ". ."

        }, 5500)

        h1.postDelayed({

            b.tv2.text = ". . ."

        }, 6000)

        h1.postDelayed({

            b.tv2.text = "."

        }, 6500)

        h1.postDelayed({

            b.tv2.text = ". ."

        }, 7000)

        h1.postDelayed({

            b.tv2.text = ". . ."

        }, 7500)

        h1.postDelayed({

            b.tv2.text = "."

        }, 8000)

        h1.postDelayed({

            b.tv2.text = ". ."

        }, 8500)

        h1.postDelayed({

            b.tv2.text = ". . ."

        }, 9000)

        h1.postDelayed({

            b.tv2.text = "."

        }, 9500)

        h1.postDelayed({

            b.tv2.text = ""

        }, 10000)


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
            .setDuration(t)
            .start()
    }

}