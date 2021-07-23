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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterFragment : Fragment() {

    private var usuario: Usuario? = null

    private lateinit var auth: FirebaseAuth

    private var TAG = "RegisterFragment"

    private var _binding: FragmentRegisterBinding? = null

    private val b get() = _binding!!

    var nick: String? = ""


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
                    val user = auth.currentUser

                    if (user != null) {

                        nickDialog(user)


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

    private fun nickDialog(user: FirebaseUser) {

        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Introduce tu NickName")

        // Set up the input
        val input = EditText(context)

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.hint = "Crea tu NickName"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->



            if (input.text.isEmpty()) {

                Toast.makeText(
                    requireContext(), "Introduce un Nick!",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                nick = input.text.toString()

                updateUI(user)
            }

        })

        builder.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()

    }


    private fun updateUI(user: FirebaseUser) {

        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.putExtra("nick",nick)
        startActivity(intent)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}