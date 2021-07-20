package com.example.gameit.ui

import android.content.Intent
import android.os.Bundle
import android.system.Os.accept
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gameit.MainActivity
import com.example.gameit.R
import com.example.gameit.adapters.ActualesAdapter
import com.example.gameit.models.Partida
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_actuales.*

class ActualesFragment : Fragment() {

    val listaPartidasAceptadas = arrayListOf<Partida>()

    private var user: FirebaseUser? = null

    private lateinit var googleSignInClient: GoogleSignInClient

    private var db = Firebase.firestore

    private var TAG = "ActualesFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_actuales, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGoogle()

        initViews()

        leerPartidasAceptadas()

    }

    private fun leerPartidasAceptadas() {
        user?.uid?.let {
            db.collection("partidas")
                .whereEqualTo("accepted", true)
                .get()
                .addOnSuccessListener { documents ->

                    for (document in documents) {
                        Log.d(TAG, "${document.id} => ${document.data}")

                        val a = document.toObject<Partida>()

                        listaPartidasAceptadas.add(a)

                    }
                    Log.d(TAG, "$listaPartidasAceptadas")

                    initAdapter()

                }

                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)

                }.addOnCompleteListener {
                    Log.w(TAG, "Tarea completada")
                }
        }
    }

    private fun initAdapter() {
        val mAdapter = ActualesAdapter(listaPartidasAceptadas) {

            Log.d(TAG, "Partida actual Clickada!")

            initDialog()

        }
        actualesRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        actualesRecyclerView.adapter = mAdapter
    }

    private fun initDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Resultado")
            .setMessage("Quien ha ganado")

            .setNegativeButton("Derrota") { dialog, which ->
                modificarPartidaDerrota()
            }
            .setPositiveButton("Victoria") { dialog, which ->
                modificarPartidaVictoria()
            }
            .show()
    }

    private fun modificarPartidaVictoria() {

        val a = Partida()

        a.isFinished = true
        a.isVictory = true

        user?.uid?.let {
            db.collection("partidas").document(it)
                .set(a)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")

                    Toast.makeText(
                        requireContext(),
                        "Partida ganada",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.parent_fragment_container, ActualesFragment())?.commit()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
    }

    private fun modificarPartidaDerrota() {
        val a = Partida()

        a.isFinished = true

        user?.uid?.let {
            db.collection("partidas").document(it)
                .set(a)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")

                    Toast.makeText(
                        requireContext(),
                        "Partida perdida",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.parent_fragment_container, ActualesFragment())?.commit()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
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

