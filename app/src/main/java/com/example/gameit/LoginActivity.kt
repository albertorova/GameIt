package com.example.gameit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gameit.databinding.ActivityLoginBinding
import com.example.gameit.ui.LoginFragment


class LoginActivity : AppCompatActivity() {

    private lateinit var b: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)

        supportActionBar?.hide()

        supportFragmentManager.beginTransaction().replace(R.id.login_container, LoginFragment())
            .commit()


    }

}