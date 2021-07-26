package com.example.gameit.ui


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.gameit.MainActivity
import com.example.gameit.R
import com.example.gameit.databinding.FragmentRegisterBinding
import com.example.gameit.models.Usuario
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private var user: FirebaseUser? = null

    private var db = Firebase.firestore

    private lateinit var googleSignInClient: GoogleSignInClient

    private var _binding: FragmentRegisterBinding? = null

    private val b get() = _binding!!

    private var TAG = "RegisterFragment"

    var nick: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        registrarse()

        volver()

    }

    private fun volver() {

        b.btVolver.setOnClickListener {

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.login_container, LoginFragment())?.commit()
        }
    }

    private fun registrarse() {

        b.btRegistrar.setOnClickListener {

            val email = b.registerEmail.text.toString()
            val password = b.registerPassword.text.toString()
            val passwordConfirm = b.registerConfirmPassword.text.toString()

            nick = b.registerNick.text.toString()

            if (password == passwordConfirm && password.length > 5) {
                registrarFirebase(email, password)
            } else {
                Toast.makeText(context, "Error, password no coinciden", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registrarFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    user = auth.currentUser
                    if (user != null) {

                        crearUsuario()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser) {

        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)

    }

    private fun crearUsuario() {

        val user1 = Usuario().apply {

            nombre = "Nombre"
            apellido1 = "Apellido1"
            apellido2 = "Apellido2"
            edad = 99
            region = "Region"
            pais = "Pais"
            joyas = 50
            nickname = nick
        }

        user?.uid?.let {
            db.collection("users")
                .add(user1)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")

                    updateUI(user!!)
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}