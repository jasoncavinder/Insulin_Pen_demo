package com.jasoncavinder.insulinpen_demo

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jasoncavinder.insulinpen_demo.ui.UserViewModel
import com.jasoncavinder.insulinpen_demo.ui.UserViewModel.AuthenticationState.INVALID_AUTHENTICATION
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*


class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var navController: NavController

    // TODO: setup biometrics
    val biometricPromptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("TODO: Title")
        .setSubtitle("TODO: Subtitle")
        .setDescription("TODO: Description")
        .setNegativeButtonText("TODO: Cancel")
        .build()
//    val biometricPrompt = BiometricPrompt(activity, Executor {  }, BiometricPrompt.AuthenticationCallback())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        userViewModel.mediator.observe(viewLifecycleOwner, Observer {
            //            Snackbar.make(view, it.toString(), Snackbar.LENGTH_INDEFINITE).show()
        })

        view.button_signin.setOnClickListener {
            group_login_main.visibility = View.GONE
            group_login_form.visibility = View.VISIBLE
        }
        view.button_signup.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_createUserFragment)
        }
        view.button_cancel.setOnClickListener {
            group_login_form.visibility = View.GONE
            group_login_main.visibility = View.VISIBLE
            hideKeyboard()
        }
        view.button_login.setOnClickListener {
            userViewModel.login(
                view.edit_text_email.text.toString(),
                view.edit_text_pass.text.toString()
            )
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, OnBackPressedCallback {
            group_login_form.visibility = View.GONE
            group_login_main.visibility = View.VISIBLE
            navController.popBackStack(R.id.loginFragment, false)
            true
        })

        userViewModel.authenticationState.observe(viewLifecycleOwner, Observer {
            when (it) {
                INVALID_AUTHENTICATION -> {
                    Snackbar.make(
                        view,
                        userViewModel.getAuthenticationFailedReason().value ?: "Unknown Error",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                else -> {
                }
            }
        })
    }

    private fun hideKeyboard() {
        val view = this.view
        view?.let { v ->
            val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

}
