package com.example.gameit.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.gameit.LoginActivity
import com.example.gameit.MainActivity
import com.example.gameit.R
import com.example.gameit.models.Partida
import com.example.gameit.models.Usuario
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_profile.*

class EventFragment : Fragment() {

    private var user: FirebaseUser? = null

    private lateinit var googleSignInClient: GoogleSignInClient

    private var db = Firebase.firestore

    private var TAG = "EventFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGoogle()

        initViews()

        crearPartida()

    }

    private fun crearPartida() {

        btnCrear.setOnClickListener {

            val nombre = crearNombre.text.toString()
            val nivel = crearNivel.text.toString()
            val apuesta = crearApuesta.text.toString().toInt()


            val unaPartida = Partida()
            unaPartida.creador = user?.displayName
            unaPartida.nombre = nombre
            unaPartida.nivel = nivel
            unaPartida.apuesta = apuesta.toString()

            user?.uid?.let {
                db.collection("partidas")
                    .add(unaPartida)
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!")

                        Toast.makeText(requireContext(), "Partida creada!", Toast.LENGTH_SHORT).show()

                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.main_container, EventFragment())?.commit()

                    }
                    .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
            }

        }
    }

    private fun initGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

    }

    private fun initViews() {
        user = Firebase.auth.currentUser


    }
}
