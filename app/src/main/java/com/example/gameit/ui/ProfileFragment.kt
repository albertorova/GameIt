package com.example.gameit.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.gameit.LoginActivity
import com.example.gameit.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    //private var usuario: Usuario? = null

    private var user: FirebaseUser? = null

    private lateinit var googleSignInClient: GoogleSignInClient

    private var TAG = "ProfileFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGoogle()
        initViews()

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

       tvUserName.text = user?.displayName

        leerDatosUsuarios()

        btnLogout.setOnClickListener {

            Toast.makeText(requireContext(), "Me deslogueo", Toast.LENGTH_SHORT).show()

            logout()
        }
    }


    private fun leerDatosUsuarios() {
        // Add a new document with a generated ID
        val db = Firebase.firestore
        user?.uid?.let {
            db.collection("users").document(it)
                .get()
                .addOnSuccessListener { document ->
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)

                }.addOnCompleteListener {
                    Log.w(TAG, "Tarea completada")
                }
        }
    }


    private fun logout() {
        googleSignInClient.signOut()?.addOnCompleteListener {
            googleSignInClient.revokeAccess()?.addOnCompleteListener {
                Firebase.auth.signOut()
                // finish()

                val intent = Intent()
                intent.setClass(requireActivity(), LoginActivity::class.java)
                requireActivity().startActivity(intent)
            }
        }

    }
}