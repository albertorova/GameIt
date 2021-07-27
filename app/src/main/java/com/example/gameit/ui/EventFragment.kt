package com.example.gameit.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gameit.R
import com.example.gameit.adapters.GameAdapter
import com.example.gameit.databinding.FragmentEventBinding
import com.example.gameit.models.Game
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
import java.util.*


class EventFragment : Fragment() {

    private var usuario: Usuario? = null

    var tempPortada: String = "https://cdn2.unrealengine.com/metaimage1-1920x1080-abb60090deaf.png"

    var tempNombre: String = "Fornite"

    var tempNivel: String = "NOOB"

    val listaJuegos = arrayListOf<Game>()

    private var user: FirebaseUser? = null

    private lateinit var googleSignInClient: GoogleSignInClient

    private var db = Firebase.firestore

    private var TAG = "EventFragment"

    private var _binding: FragmentEventBinding? = null

    private val b get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentEventBinding.inflate(inflater, container, false)

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initGoogle()

        initViews()

        initCodigo()

        leerDatosUsuario()

        leerJuegos()

        initSpinner()

        incrementarApuesta()

        b.btnCrear.setOnClickListener {

                crearPartida()

            }

    }

    private fun initCodigo() {

        val rnd = getRandomString(10)

        b.crearCodigo.setText(rnd)

        b.crearCodigo.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) b.crearCodigo.setText("")
        }
    }

    private fun getRandomString(sizeOfRandomString: Int): String {

        val characters = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"

        val random = Random()
        val sb = StringBuilder(sizeOfRandomString)
        for (i in 0 until sizeOfRandomString)
            sb.append(characters[random.nextInt(characters.length)])
        return sb.toString()
    }

    private fun leerDatosUsuario() {
        // Add a new document with a generated ID
        val db = Firebase.firestore
        user?.uid?.let {
            db.collection("users").document(it)
                .get()
                .addOnSuccessListener { result ->

                    Log.d(TAG, "$result")

                    usuario = result.toObject(Usuario::class.java)

                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)

                }.addOnCompleteListener {
                    Log.w(TAG, "Tarea completada")
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

    private fun incrementarApuesta() {


        b.btnSum.setOnClickListener {

            val a = b.crearApuesta.text.toString().toInt()
            val c = a + 1
            b.crearApuesta.text = c.toString()

        }

        b.btnRes.setOnClickListener {
            val a = b.crearApuesta.text.toString().toInt()

            if (a == 1) {

                Log.v(TAG, "no se puede bajar de 1")

            } else {

                val c = a - 1
                b.crearApuesta.text = c.toString()
            }
        }
    }


    private fun initSpinner() {

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.level_array,
            R.layout.spinner_layout

        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
            // Apply the adapter to the spinner

            b.levelSpinner.adapter = adapter
            b.levelSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

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

            tempPortada = it.portada.toString()


        }
        b.gameRecyclerView.layoutManager = GridLayoutManager(context, 2)
        b.gameRecyclerView.adapter = mAdapter
    }

    private fun crearPartida() {

            val unaPartida = Partida()

            unaPartida.portada = tempPortada
            unaPartida.creador = usuario?.nickname
            unaPartida.nombre = tempNombre
            unaPartida.nivel = tempNivel
            unaPartida.apuesta = b.crearApuesta.text.toString().toInt()
            unaPartida.codigo = b.crearCodigo.text.toString()
            unaPartida.isAccepted
            unaPartida.isFinished
            unaPartida.isVictory

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
