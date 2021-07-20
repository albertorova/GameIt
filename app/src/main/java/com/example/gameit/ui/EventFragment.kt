package com.example.gameit.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gameit.R
import com.example.gameit.adapters.GameAdapter
import com.example.gameit.models.Game
import com.example.gameit.models.Partida
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_event.*


class EventFragment : Fragment() {

    var tempNombre: String = ""

    var tempNivel: String = ""

    val listaJuegos = arrayListOf<Game>()

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

        leerJuegos()

        initSpinner()

        crearPartida()

    }

    private fun initSpinner() {

        val spinner = requireView().findViewById<View>(R.id.level_spinner) as Spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.level_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    // An item was selected. You can retrieve the selected item using
                    // parent.getItemAtPosition(pos)
                    tempNivel = parent.getItemAtPosition(pos) as String

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Another interface callback
                }
            }
        }

    }

    private fun leerJuegos() {
        user?.uid?.let {
            db.collection("games")
                .get()
                .addOnSuccessListener { documents ->

                    for (document in documents) {
                        Log.d(TAG, "${document.id} => ${document.data}")

                        val a = document.toObject<Game>()

                        listaJuegos.add(a)

                    }
                    Log.d(TAG, "$listaJuegos")

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
        val mAdapter = GameAdapter(listaJuegos) {

            tempNombre = it.nombre.toString()

            //  gamePortada.alpha = 0.5F

        }
        gameRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        gameRecyclerView.adapter = mAdapter
    }

    private fun crearPartida() {

        btnCrear.setOnClickListener {


            // val nivel = crearNivel.text.toString()
            // val apuesta = crearApuesta.text.toString().toInt()


            val unaPartida = Partida()
            unaPartida.creador = user?.displayName
            unaPartida.nombre = tempNombre
            //unaPartida.nivel = nivel
            unaPartida.nivel = tempNivel
            //unaPartida.apuesta = apuesta.toString()
            unaPartida.apuesta = "40"
            unaPartida.codigo = "12345"
            unaPartida.isAccepted
            unaPartida.isFinished
            unaPartida.isVictory

            user?.uid?.let {
                db.collection("partidas")
                    .add(unaPartida)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully written!")

                        Toast.makeText(
                            requireContext(),
                            "Partida creada!",
                            Toast.LENGTH_SHORT
                        )
                            .show()

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
