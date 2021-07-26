package com.example.gameit.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gameit.R
import com.example.gameit.databinding.FragmentProfileBinding
import com.example.gameit.models.Usuario
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {

    private var usuario: Usuario? = null

    private lateinit var googleSignInClient: GoogleSignInClient

    private var TAG = "ProfileFragment"

    private var _binding: FragmentProfileBinding? = null

    private val b get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return b.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        initGoogle()

        initViews()

        removeText()



        b.btnModificar.setOnClickListener {

            modificarUsuario()
        }


    }

    private fun removeText() {
        b.etNombre.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) b.etNombre.setText("")
        }
        b.etApellido1.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) b.etApellido1.setText("")
        }
        b.etApellido2.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) b.etApellido2.setText("")
        }
        b.etEdad.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) b.etEdad.setText("")
        }
        b.etPais.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) b.etPais.setText("")
        }
        b.etRegion.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) b.etRegion.setText("")
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

            else -> super.onOptionsItemSelected(item)
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

        leerDatosUsuarios()

        Firebase.auth.currentUser?.photoUrl?.let {
            Picasso.get().load(it).into(b.userProfilePhoto)
        }

    }

    private fun modificarUsuario() {

        b.etNombre.alpha = 1F
        b.etApellido1.alpha = 1F
        b.etApellido2.alpha = 1F
        b.etEdad.alpha = 1F
        b.etPais.alpha = 1F
        b.etRegion.alpha = 1F


        val user1 = Usuario().apply {

            nombre = b.etNombre.text.toString()
            apellido1 = b.etApellido1.text.toString()
            apellido2 = b.etApellido2.text.toString()
            edad = b.etEdad.text.toString().toInt()
            region = b.etRegion.text.toString()
            pais = b.etPais.text.toString()
        }

        val db = Firebase.firestore
        val userFirebase = Firebase.auth.currentUser

        userFirebase?.uid?.let {
            db.collection("users").document(it)
                .set(user1)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")

                    Toast.makeText(
                        requireContext(),
                        "Datos modificados satisfactoriamente",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }


    }

    private fun leerDatosUsuarios() {
        // Add a new document with a generated ID
        val db = Firebase.firestore
        val userFirebase = Firebase.auth.currentUser

        userFirebase?.uid?.let {
            db.collection("users").document(it)
                .get()
                .addOnSuccessListener { result ->

                    Log.d(TAG, "$result")

                    usuario = result.toObject(Usuario::class.java)

                    Log.v(TAG, "$usuario")

                    initDatosUsuario()
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)

                }.addOnCompleteListener {
                    Log.w(TAG, "Tarea completada")
                }
        }
    }

    private fun initDatosUsuario() {

        val userFirebase = Firebase.auth.currentUser

        b.etNombre.setText(usuario?.nombre)
        b.etApellido1.setText(usuario?.apellido1)
        b.etApellido2.setText(usuario?.apellido2)
        b.etEdad.setText(usuario?.edad.toString())
        b.etRegion.setText(usuario?.region)
        b.etPais.setText(usuario?.pais)
        b.userProfileName.text = userFirebase?.displayName

    }
}