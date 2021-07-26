package com.example.gameit

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.example.gameit.databinding.ActivityMainBinding
import com.example.gameit.models.Usuario
import com.example.gameit.ui.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {

    private var usuario: Usuario? = null

    private var user: FirebaseUser? = null

    private lateinit var googleSignInClient: GoogleSignInClient

    private var TAG = "MainActivity"

    private var db = Firebase.firestore

    private lateinit var b: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(0xFF000000.toInt()))

        title = "GameIt                         \uD83E\uDE99     \uD83D\uDC8E"

        initGoogle()

        initViews()

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
        b.bottomNavigationView.selectedItemId = R.id.action_find
    }

    private fun initViews() {

        user = Firebase.auth.currentUser

        leerDatosUsuarios()

    }

    private fun leerDatosUsuarios() {
        // Add a new document with a generated ID
        db = Firebase.firestore
        user?.uid?.let {
            db.collection("users").document(it)
                .get()
                .addOnSuccessListener { result ->

                    Log.d(TAG, "$result")

                    usuario = result.toObject(Usuario::class.java)

                    initAppBar()
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)

                }.addOnCompleteListener {
                    Log.w(TAG, "Tarea completada")
                }
        }
    }

    private fun initAppBar() {

        var a = usuario?.joyas.toString()
        title = "GameIt                     100 \uD83E\uDE99  $a \uD83D\uDC8E"

    }

    private fun initGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    fun goToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit()
    }
}