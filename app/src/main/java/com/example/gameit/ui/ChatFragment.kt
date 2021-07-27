package com.example.gameit.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gameit.R
import com.example.gameit.adapters.ChatAdapter
import com.example.gameit.databinding.FragmentChatBinding
import com.example.gameit.models.Game
import com.example.gameit.models.Mensaje
import com.example.gameit.models.Partida
import com.example.gameit.models.Usuario
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.util.*


class ChatFragment : Fragment() {


    val listaMensajes = arrayListOf<Mensaje>()

    val listaJuegos = arrayListOf<Game>()

    val listaTitulos = arrayListOf<String>()

    var tempTitulo: String = "Fortnite"

    private var user: FirebaseUser? = null

    private lateinit var googleSignInClient: GoogleSignInClient

    private var db = Firebase.firestore

    private var TAG = "ChatFragment"

    private var _binding: FragmentChatBinding? = null

    private val b get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatBinding.inflate(inflater, container, false)

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initGoogle()

        initViews()

        initSpinner()

        leerMensajes()

        b.btnReload.setOnClickListener {

            listaMensajes.clear()

            leerMensajes()

        }

        //leerJuegos()

        //initSpinnerDynamic()

        crearMensaje()
    }

    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.games_array,
            R.layout.spinner_layout

        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
            // Apply the adapter to the spinner

            b.gamesSpinner.adapter = adapter
            b.gamesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {

                    tempTitulo = parent.getItemAtPosition(pos) as String

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
                    listaJuegos.forEach {
                        it.nombre?.let { it1 -> listaTitulos.add(it1) }

                        Log.v(TAG, listaTitulos.toString())
                    }
                }

                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)

                }.addOnCompleteListener {
                    Log.w(TAG, "Tarea completada")
                }
        }
    }


    private fun initSpinnerDynamic() {

        val adp = ArrayAdapter(
            requireContext(),
            R.layout.spinner_layout,
            listaTitulos
        )
        adp.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        b.gamesSpinner.adapter = adp
        b.gamesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                arg3: Long
            ) {
                //b.tituloSala.text = b.gamesSpinner.selectedItem as CharSequence?
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }
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

    private fun leerMensajes() {

        db.collection("chat")
            .orderBy("fecha")
            .get()
            .addOnSuccessListener { documents ->

                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    val a = document.toObject<Mensaje>()

                    if (a.juego == tempTitulo){
                        listaMensajes.add(a)
                    }
                }

                Log.d(TAG, "$listaMensajes")

                if (listaMensajes.isEmpty()) {

                    b.tv1.isVisible = true

                    initAdapter()

                } else {

                    b.tv1.isVisible = false

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
        val mAdapter = ChatAdapter(listaMensajes)
        b.chatRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        b.chatRecyclerView.adapter = mAdapter
    }

    private fun crearMensaje() {

        b.btnSend.setOnClickListener {

            val m = b.chatText.text.toString()

            val crearUnMensaje = Mensaje().apply {

                usuario = user?.displayName
                mensaje = m
                fecha = Date()
                juego = tempTitulo
            }

            user?.uid?.let {
                db.collection("chat")
                    .add(crearUnMensaje)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully written!")

                        Toast.makeText(requireContext(), "Mensaje enviado", Toast.LENGTH_SHORT)
                            .show()

                        b.chatText.text.clear()

                        listaMensajes.clear()

                        leerMensajes()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


