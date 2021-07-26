package com.example.gameit.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gameit.R
import com.example.gameit.adapters.ActualesAdapter
import com.example.gameit.databinding.FragmentActualesBinding
import com.example.gameit.models.Partida
import com.example.gameit.models.Usuario
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ActualesFragment : Fragment() {

    val listaPartidasAceptadas = arrayListOf<Partida>()

    private var user: FirebaseUser? = null

    private lateinit var googleSignInClient: GoogleSignInClient

    private var db = Firebase.firestore

    private var TAG = "ActualesFragment"

    private var _binding: FragmentActualesBinding? = null

    private val b get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentActualesBinding.inflate(inflater, container, false)

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGoogle()

        initViews()

        leerPartidasAceptadas()

    }

    private fun leerPartidasAceptadas() {

        db.collection("partidas")
            .whereEqualTo("accepted", true)
            .whereEqualTo("finished", false)
            .get()
            .addOnSuccessListener { documents ->

                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    val a = document.toObject<Partida>()

                    a.id = document.id

                    listaPartidasAceptadas.add(a)

                }
                Log.d(TAG, "$listaPartidasAceptadas")

                if (listaPartidasAceptadas.isEmpty()) {

                    b.image1.isVisible = true

                    initAdapter()

                } else {

                    b.image1.isVisible = false

                    initAdapter()

                }

            }

            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)

            }.addOnCompleteListener {
                Log.w(TAG, "Tarea completada")
            }
    }


    private fun initAdapter() {
        val mAdapter = ActualesAdapter(listaPartidasAceptadas) {

            Log.d(TAG, "Partida actual Clickada!")

            initDialog(it)

        }
        b.actualesRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        b.actualesRecyclerView.adapter = mAdapter
    }

    private fun initDialog(partida: Partida) {
        MaterialAlertDialogBuilder(requireContext())

            .setTitle("Resultado")

            .setNegativeButton("Derrota") { dialog, which ->

                modificarPartidaDerrota(partida)
            }
            .setPositiveButton("Victoria") { dialog, which ->

                modificarPartidaVictoria(partida)
            }

            .show()
            .window?.setBackgroundDrawableResource(R.color.orangy)

    }

    private fun modificarPartidaVictoria(partida: Partida) {

        partida.id?.let {
            db.collection("partidas").document(it)
                .update("finished", true, "victory", true)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated!")

                    addPremioUsuario(partida)

                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

        }
    }

    private fun addPremioUsuario(partida: Partida) {

        val a = partida.apuesta?.toLong()?.times(2)

        user = Firebase.auth.currentUser

        user?.uid?.let {
            db.collection("users").document(it)
                .update("joyas", a?.let { it1 -> FieldValue.increment(it1) })
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated!")

                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.parent_fragment_container, ActualesFragment())?.commit()

                    Toast.makeText(
                        requireContext(),
                        "Has ganado",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        }

    }


    private fun modificarPartidaDerrota(partida: Partida) {

        partida.id?.let {
            db.collection("partidas").document(it)
                .update("finished", true)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated!")

                    Toast.makeText(
                        requireContext(),
                        "Has perdido",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.parent_fragment_container, ActualesFragment())?.commit()

                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

