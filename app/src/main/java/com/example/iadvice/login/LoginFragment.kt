package com.example.iadvice.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val application = requireNotNull(this.activity).application
        val viewModelFactory = LoginViewModelFactory(application)
        // viewModelProviders used to not destroy the viewmodel until detached
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        binding.apply {

            loginButton.setOnClickListener {
                val password = binding.passwordText.text.toString()
                val email = binding.usernameText.text.toString()


                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (!it.isSuccessful) return@addOnCompleteListener
                        var uid = it.result!!.user!!.uid
                        retriveUser(uid)
                        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        findNavController().navigate(action)
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "Failed to login user: ${it.message}")
                    }
            }

            registerButton.setOnClickListener {
                requireView().findNavController()
                    .navigate(R.id.action_loginFragment_to_registerFragment)
            }

            facebookLoginButton.setOnClickListener {}
            googleLoginButton.setOnClickListener {}
            return binding.root
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity()!!.findViewById<AppBarLayout>(R.id.appBarLayout)
            .setVisibility(View.GONE)
    }

    fun retriveUser(uid: String) {

        var onlineDb = Firebase.database.reference

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
                Log.d(TAG, "Ho tirato giú l'utente $child")

                val sharedPreference = activity?.getSharedPreferences("USERS", Context.MODE_PRIVATE) ?: return
                var editor = sharedPreference.edit()
                editor.putString("username",child)
                editor.commit()
            }


        }

        onlineDb.child("users").child(uid).addListenerForSingleValueEvent(messagesUploadListener)




    }
}
