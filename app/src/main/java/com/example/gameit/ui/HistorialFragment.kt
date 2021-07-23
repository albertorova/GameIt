package com.example.gameit.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gameit.R
import com.example.gameit.adapters.HistorialAdapter
import com.example.gameit.databinding.FragmentHistorialBinding
import com.example.gameit.models.Partida
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class HistorialFragment : Fragment() {

    val listaPartidasTerminadas = arrayListOf<Partida>()

    private var user: FirebaseUser? = null

    private lateinit var googleSignInClient: GoogleSignInClient

    private var db = Firebase.firestore

    private var TAG = "HistorialFragment"

    private var _binding: FragmentHistorialBinding? = null

    private val b get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistorialBinding.inflate(inflater, container, false)

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGoogle()

        initViews()

        leerPartidasTerminadas()

    }

    private fun leerPartidasTerminadas() {
        user?.uid?.let {
            db.collection("partidas")
                .whereEqualTo("finished", true)
                .get()
                .addOnSuccessListener { documents ->

                    for (document in documents) {
                        Log.d(TAG, "${document.id} => ${document.data}")

                        val a = document.toObject<Partida>()

                        listaPartidasTerminadas.add(a)

                    }
                    Log.d(TAG, "$listaPartidasTerminadas")

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

        val mAdapter = HistorialAdapter(listaPartidasTerminadas)
        b.historialRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        b.historialRecyclerView.adapter = mAdapter

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}