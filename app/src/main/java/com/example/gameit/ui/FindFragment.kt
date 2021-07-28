package com.example.gameit.ui

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gameit.MainActivity
import com.example.gameit.R
import com.example.gameit.adapters.FindAdapter
import com.example.gameit.databinding.FragmentFindBinding
import com.example.gameit.models.Partida
import com.example.gameit.models.Usuario
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class FindFragment : Fragment() {

    private var usuario: Usuario? = null

    private var monedasDelUsuario: Int? = null

    private var balanceMonedas: Int? = null

    val listaPartidas = arrayListOf<Partida>()

    private var user: FirebaseUser? = null

    private lateinit var googleSignInClient: GoogleSignInClient

    private var db = Firebase.firestore

    private var TAG = "FindFragment"

    private var _binding: FragmentFindBinding? = null

    private val b get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFindBinding.inflate(inflater, container, false)

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initGoogle()

        initViews()

        leerPartidas()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.settings_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.settings -> {

                //Ir a settings
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.main_container, SettingsFragment())?.commit()

                true
            }

            R.id.comprar -> {

                //Ir a settings
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.main_container, ComprarFragment())?.commit()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun leerPartidas() {

        db.collection("partidas")
            .whereEqualTo("accepted", false)
            .get()
            .addOnSuccessListener { documents ->

                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    val a = document.toObject<Partida>()

                    a.id = document.id

                    listaPartidas.add(a)

                }

                Log.d(TAG, "$listaPartidas")

                if (listaPartidas.isEmpty()) {

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

        val mAdapter = FindAdapter(listaPartidas, activity) {

            Log.w(TAG, "Click en partida del find")

            initDialog(it)

        }

        b.findRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        b.findRecyclerView.adapter = mAdapter

    }

    private fun initDialog(partida: Partida) {

        MaterialAlertDialogBuilder(requireContext())

            .setTitle("¿Aceptas el desafío?")

            .setNegativeButton("Cancelar") { dialog, which ->

                dialog.dismiss()

            }
            .setPositiveButton("A por ello") { dialog, which ->

                comprobarSaldo(partida, dialog)

            }

            .show()
            .window?.setBackgroundDrawableResource(R.color.orangy)

    }

    private fun comprobarSaldo(partida: Partida, dialog: DialogInterface) {
        // Add a new document with a generated ID

        val monedasRestadas = partida.apuesta

        user = Firebase.auth.currentUser

        user?.uid?.let {
            db.collection("users").document(it)
                .get()
                .addOnSuccessListener { result ->

                    Log.d(TAG, "$result")

                    usuario = result.toObject(Usuario::class.java)

                    monedasDelUsuario = usuario?.monedas

                    balanceMonedas = monedasDelUsuario!! - monedasRestadas!!

                    Log.v(TAG, "$usuario")

                    if (balanceMonedas!! < 0) {

                        Toast.makeText(
                            requireContext(),
                            "Monedas insuficientes!",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                        dialog.dismiss()

                    } else {

                        aceptarPartida(partida)
                    }

                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)

                }.addOnCompleteListener {
                    Log.w(TAG, "Tarea completada")
                }
        }
    }

    private fun aceptarPartida(partida: Partida) {

        partida.id?.let {
            db.collection("partidas").document(it)
                .update("accepted", true)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated!")

                    restarApuesta()

                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        }
    }

    private fun restarApuesta() {

        user = Firebase.auth.currentUser

        user?.uid?.let {
            db.collection("users").document(it)
                .update("monedas", balanceMonedas)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated!")

                    Toast.makeText(
                        requireContext(),
                        "Desafio aceptado!",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    (activity as? MainActivity)?.actualizarBalanceMonedas(balanceMonedas)

                    listaPartidas.clear()

                    leerPartidas()

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


