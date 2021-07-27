package com.example.gameit.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.gameit.MainActivity
import com.example.gameit.R
import com.example.gameit.databinding.FragmentLoginBinding
import com.example.gameit.models.Usuario
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class LoginFragment : Fragment() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    private val RC_SIGN_IN = 40043034

    private var user: FirebaseUser? = null

    private val TAG = "LoginActivity"

    private var db = Firebase.firestore

    private var _binding: FragmentLoginBinding? = null

    private val b get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Picasso.get()
            .load("https://yt3.ggpht.com/ytc/AKedOLRyqsQbpKNjJBgwlvqyaAL9mLZUqPanLVsXUaXOUw=s900-c-k-c0x00ffffff-no-rj")
            .into(b.loginImage)

        initGoogle()

        initViews()

    }

    private fun initViews() {
        b.btnGoogle.setOnClickListener {
            signIn()
        }

        b.btnSingIn.setOnClickListener {
            loginEmailAndPass()
        }

        b.tvCreateAccount.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.login_container, RegisterFragment())?.commit()
        }

    }

    private fun loginEmailAndPass() {

        val email = b.loginEmail.text.toString()
        val password = b.loginPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)

                        Toast.makeText(
                            requireContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()

                        updateUI(null)
                    }
                }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun initGoogle() {
        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    user = auth.currentUser

                    comprobarUsuario()
                    //updateUI(user)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun comprobarUsuario() {
        // Add a new document with a generated ID
        val db = Firebase.firestore

        user?.uid?.let {
            db.collection("users").document(it)
                .get()
                .addOnSuccessListener { result ->

                    if (result.data != null) {

                        Log.d(TAG, "$result")

                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                    } else {

                        crarUsuario()

                    }


                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)


                }.addOnCompleteListener {
                }
        }
    }

    private fun crarUsuario() {

        val user1 = Usuario().apply {

            nombre = "Nombre"
            apellido1 = "Apellido1"
            apellido2 = "Apellido2"
            edad = 99
            region = "Region"
            pais = "Pais"
            monedas = 100
            joyas = 50
            nickname = user?.displayName
            victorias = 0
        }

        user?.uid?.let {
            db.collection("users").document(it)
                .set(user1)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")

                    updateUI(user!!)

                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }


    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent()
            intent.setClass(requireActivity(), MainActivity::class.java)
            requireActivity().startActivity(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}






