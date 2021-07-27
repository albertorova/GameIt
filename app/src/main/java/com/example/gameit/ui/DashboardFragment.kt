package com.example.gameit.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import com.example.gameit.R
import com.example.gameit.databinding.FragmentDashboardBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class DashboardFragment : Fragment() {

    private var user: FirebaseUser? = null

    private lateinit var googleSignInClient: GoogleSignInClient

    private var TAG = "DashboardFragment"

    private var _binding: FragmentDashboardBinding? = null

    private val b get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initGoogle()

        initViews()

        colorOnActuales()

        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.parent_fragment_container, ActualesFragment())?.commit()

        b.tvHistorial.setOnClickListener {

            colorOffActuales()

            colorOnHistorial()

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.parent_fragment_container, HistorialFragment())?.commit()

        }

        b.tvActuales.setOnClickListener {

            colorOnActuales()

            colorOffHistorial()

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.parent_fragment_container, ActualesFragment())?.commit()
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

    private fun colorOnActuales() {
        b.tvActuales.setTextColor(ContextCompat.getColor(requireContext(), R.color.yellow))
        b.view1.setBackgroundResource(R.color.yellow)
    }

    private fun colorOffActuales() {
        b.tvActuales.setTextColor(ContextCompat.getColor(requireContext(), R.color.bliu))
        b.view1.setBackgroundResource(R.color.bliu)
    }

    private fun colorOnHistorial() {
        b.tvHistorial.setTextColor(ContextCompat.getColor(requireContext(), R.color.yellow))
        b.view2.setBackgroundResource(R.color.yellow)
    }

    private fun colorOffHistorial() {
        b.tvHistorial.setTextColor(ContextCompat.getColor(requireContext(), R.color.bliu))
        b.view2.setBackgroundResource(R.color.bliu)
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