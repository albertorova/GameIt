package com.example.gameit.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gameit.R
import com.example.gameit.adapters.ChatAdapter
import com.example.gameit.models.Mensaje
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment() {


    val listaMensajes = arrayListOf<Mensaje>()

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

        initGoogle()

        initViews()

        leerMensajes()

        crearMensaje()
    }

    private fun leerMensajes() {
        user?.uid?.let {
            db.collection("chat")
                .get()
                .addOnSuccessListener { documents ->

                    for (document in documents) {
                        Log.d(TAG, "${document.id} => ${document.data}")

                        val a = document.toObject<Mensaje>()

                        listaMensajes.add(a)

                    }
                    initAdapter()

                    //Log.d(TAG, "$listaMensajes")
                }

                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)

                }.addOnCompleteListener {
                    Log.w(TAG, "Tarea completada")
                }
        }
    }

    private fun initAdapter() {
        val mAdapter = ChatAdapter(listaMensajes)
        chatRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        chatRecyclerView.adapter = mAdapter
    }

    private fun crearMensaje() {

        btnSend.setOnClickListener {

            val mensaje = chatText.text.toString()

            val crearUnMensaje = Mensaje()
            crearUnMensaje.usuario = user?.displayName
            crearUnMensaje.mensaje = mensaje

            user?.uid?.let {
                db.collection("chat")
                    .add(crearUnMensaje)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully written!")

                        Toast.makeText(requireContext(), "Mensaje enviado", Toast.LENGTH_SHORT)
                            .show()

                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.main_container, ChatFragment())?.commit()

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


