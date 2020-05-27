package com.example.iadvice.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.iadvice.App
import com.example.iadvice.R
import com.example.iadvice.databinding.LoginFragmentBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_fragment.*


class LoginFragment : Fragment() {

    companion object {
        const val TAG = "LoginFragment"
        const val SIGN_IN_RESULT_CODE = 1001
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
                val user = username_text.text.toString()
                App.user = user
                viewModel.loginUser(
                    binding.passwordText.text.toString(),
                    binding.usernameText.text.toString()
                )


                launchSignInFlow()

            }

            registerButton.setOnClickListener {
               requireView().findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
                //requireView().findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }

            facebookLoginButton.setOnClickListener {}
            googleLoginButton.setOnClickListener {}
            twitterLoginButton.setOnClickListener {}
        }
        return binding.root
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  It is also useful for fragments that use
     * [.setRetainInstance] to retain their instance,
     * as this callback tells the fragment when it is fully associated with
     * the new activity instance.  This is called after [.onCreateView]
     * and before [.onViewStateRestored].
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity()!!.findViewById<AppBarLayout>(R.id.appBarLayout).setVisibility(View.GONE)
    }

    private fun launchSignInFlow() {
        // Give users the option to sign in / register with their email
        // If users choose to register with their email,
        // they will need to create a password as well
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
            //
        )

        // Create and launch sign-in intent.
        // We listen to the response of this activity with the
        // SIGN_IN_RESULT_CODE code
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                providers
            ).build(), SIGN_IN_RESULT_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in.
                Log.i(RegisterFragment.TAG, "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")

                //to pass the safeargs id to chat activity
//                val action = LoginFragmentDirections.actionLoginFragmentToChatActivity(321)
//                requireView().findNavController().navigate(action)
                requireView().findNavController().navigate(R.id.action_loginFragment_to_chatActivity)
            } else {
                // Sign in failed. If response is null, the user canceled the
                // sign-in flow using the back button. Otherwise, check
                // the error code and handle the error.
                Log.i(RegisterFragment.TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }
}
