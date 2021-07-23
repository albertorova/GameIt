package com.example.gameit.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gameit.R
import com.example.gameit.adapters.GameAdapter
import com.example.gameit.databinding.FragmentEventBinding
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


class EventFragment : Fragment() {

    var tempNombre: String = ""

    var tempNivel: String = ""

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
    ): View? {


        _binding = FragmentEventBinding.inflate(inflater, container, false)

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initGoogle()

        initViews()

        leerJuegos()

        initSpinner()

        incrementarApuesta()

        crearPartida()

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
            if (a == 0){
                Log.v(TAG,"no se puede bajar de 0")

            } else {
                val c = a - 1
                b.crearApuesta.text = c.toString()
            }
        }
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
        b.gameRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        b.gameRecyclerView.adapter = mAdapter
    }

    private fun crearPartida() {

        b.btnCrear.setOnClickListener {


            val unaPartida = Partida()

            unaPartida.creador = user?.displayName
            unaPartida.nombre = tempNombre
            unaPartida.nivel = tempNivel

            unaPartida.apuesta = b.crearApuesta.text.toString().toInt()

            val rnds2 = (100000..999999).random()
            unaPartida.codigo = rnds2.toString()

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
