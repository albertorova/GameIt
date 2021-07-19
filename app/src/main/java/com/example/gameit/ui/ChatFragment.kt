package com.example.gameit.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gameit.R
import com.example.gameit.adapters.ChatAdapter
import com.example.gameit.adapters.FindAdapter
import com.example.gameit.models.Mensaje
import com.example.gameit.models.Partida
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_find.*

class ChatFragment : Fragment() {

    var demoNames = listOf(
        "Jamey Bush","Casandra Red", "Melvin Detrick", "Mirella Jiggetts","Brook Hetzel",
        "Eva Mccrystal","Glennie Hiott", "Alverta Ruggles", "Floria Pedroza", "Marianela Redman",
        "Colby Bellew", "Marquerite Kite", "Marcelene Rhoads", "Taneka Burgin",
        "Marci Smits","Michelle Madero", "Pinkie Josey", "Marlys Nieman","Ling Reddick"
    )

    private var user: FirebaseUser? = null

    private lateinit var googleSignInClient: GoogleSignInClient

    private var db = Firebase.firestore

    private var TAG = "ChatFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        initGoogle()

        initViews()

        crearMensaje()
    }

    private fun crearMensaje() {

        btnSend.setOnClickListener {

            //val usuario = crearNombre.text.toString()
            val mensaje = chatText.text.toString()


            val unMensaje = Mensaje()
            unMensaje.usuario = user?.displayName
            unMensaje.mensaje = mensaje


            user?.uid?.let {
                db.collection("chat")
                    .add(unMensaje)
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!")

                        Toast.makeText(requireContext(), "Mensaje enviado", Toast.LENGTH_SHORT).show()

                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.main_container, ChatFragment())?.commit()

                    }
                    .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
            }

        }
    }

    private fun initAdapter() {
        val mAdapter = ChatAdapter(demoNames)
        chatRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        chatRecyclerView.adapter = mAdapter
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
