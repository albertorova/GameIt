package com.example.gameit.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.gameit.LoginActivity
import com.example.gameit.R
import com.example.gameit.databinding.FragmentSettingsBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



class SettingsFragment : Fragment() {

    private var user: FirebaseUser? = null

    private lateinit var googleSignInClient: GoogleSignInClient

    private var TAG = "SettingsFragment"

    private var _binding: FragmentSettingsBinding? = null

    private val b get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return b.root
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

        b.tvLogOut.setOnClickListener {

            Toast.makeText(requireContext(), "Me deslogueo", Toast.LENGTH_SHORT).show()

            logout()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

