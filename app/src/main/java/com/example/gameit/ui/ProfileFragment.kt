package com.example.gameit.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gameit.LoginActivity
import com.example.gameit.R
import com.example.gameit.models.Usuario
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private var usuario: Usuario? = null

    private var user: FirebaseUser? = null

    private lateinit var googleSignInClient: GoogleSignInClient

    private var TAG = "ProfileFragment"

    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initGoogle()
        initViews()

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
        user = Firebase.auth.currentUser

        leerDatosUsuarios()

        modificarUsuario()

        user_profile_name.text = user?.displayName

        Firebase.auth.currentUser?.photoUrl?.let {
            Picasso.get().load(it).into(user_profile_photo)
        }


    }

    private fun modificarUsuario() {

        btnModificar.setOnClickListener {

            val nombre = etNombre.text.toString()
            val apellido = etApellido.text.toString()
            val edad = etEdad.text.toString().toInt()
            val pais = etPais.text.toString()

            val user1 = Usuario()
            user1.nombre = nombre
            user1.apellido = apellido
            user1.edad = edad.toString()
            user1.pais = pais

            user?.uid?.let {
                db.collection("users").document(it)
                    .set(user1)
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
            }
        }

    }


    private fun leerDatosUsuarios() {
        // Add a new document with a generated ID
        val db = Firebase.firestore
        user?.uid?.let {
            db.collection("users").document(it)
                .get()
                .addOnSuccessListener { result ->

                    Log.d(TAG, "$result")

                    usuario = result.toObject(Usuario::class.java)

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
        etNombre.setText(usuario?.nombre)
        etApellido.setText(usuario?.apellido)
        etEdad.setText(usuario?.edad.toString())
        etPais.setText(usuario?.pais)


    }
}