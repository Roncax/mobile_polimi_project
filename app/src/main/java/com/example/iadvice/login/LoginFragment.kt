package com.example.iadvice.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.iadvice.R
import com.example.iadvice.databinding.LoginFragmentBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    companion object {
        const val TAG = "LoginFragment"
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // bind the login_fragment layout with the binding variable
        val binding = DataBindingUtil.inflate<LoginFragmentBinding>(
            inflater,
            R.layout.login_fragment, container, false
        )

        // viewModelProviders used to not destroy the viewModel until detached
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.apply {

            loginButton.setOnClickListener {
                val password = binding.passwordText.text.toString()
                val email = binding.usernameText.text.toString()
                if (password.isEmpty()){
                    Toast.makeText(
                        context, "Password field cannot be empty, try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                if (email.isEmpty()){
                    Toast.makeText(
                        context, "Email field cannot be empty, try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                performLogin(email, password)
            }

            registerButton.setOnClickListener {
                requireView().findNavController()
                    .navigate(R.id.action_loginFragment_to_registerFragment)
            }

            return binding.root
        }
    }

    private fun performLogin(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d(TAG, "signInWithCustomToken:success")
                val uid = it.result!!.user!!.uid
                retrieveUser(uid)

                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                findNavController().navigate(action)

                Toast.makeText(
                    this.context, "Login successful",
                    Toast.LENGTH_SHORT
                ).show()

            }
            .addOnFailureListener {
                Toast.makeText(
                    this.context, "Authentication failed, try again.",
                    Toast.LENGTH_SHORT
                ).show()

            }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().findViewById<AppBarLayout>(R.id.appBarLayout).visibility = View.GONE
    }

    // Download the user's info from the database and put it into the preferences(now only the nickname)
    private fun retrieveUser(uid: String) {

        val onlineDb = Firebase.database.reference

        val messagesUploadListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                //method that is called if the read is canceled (eg no permission)
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val children = p0.children
                var child = ""
                children.forEach {
                    child = it.value.toString()
                }

                val sharedPreference =
                    activity?.getSharedPreferences("USERS", Context.MODE_PRIVATE) ?: return
                val editor = sharedPreference.edit()
                editor.putString("username", child)
                editor.apply()
            }
        }

        onlineDb.child("users").child(uid).addValueEventListener(messagesUploadListener)
    }

}
