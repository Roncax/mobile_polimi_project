package com.example.iadvice.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.iadvice.App
import com.example.iadvice.R
import com.example.iadvice.database.AppDatabase
import com.example.iadvice.databinding.LoginFragmentBinding
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.login_fragment.*

private const val TAG = "LoginViewModel" //used for the logs

//TODO use livedata to observe the loginviewmodel
class LoginFragment : Fragment() {
    //private lateinit var binding: LoginFragmentBinding //class created by the compiler for the binding

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
        val userDataSource = AppDatabase.getInstance(application).userDao
        val viewModelFactory = LoginViewModelFactory(userDataSource, application)
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
                requireView().findNavController().navigate(R.id.action_loginFragment_to_chatActivity)
            }

            registerButton.setOnClickListener {
               // requireView().findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
                requireView().findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
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
}
