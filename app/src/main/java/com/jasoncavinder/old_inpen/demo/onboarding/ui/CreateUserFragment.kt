//package com.jasoncavinder.inpen.demo.onboarding.ui
//
//import android.content.Context
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.inputmethod.InputMethodManager
//import android.widget.Toast
//import androidx.activity.OnBackPressedCallback
//import androidx.annotation.StringRes
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProviders
//import androidx.navigation.NavController
//import androidx.navigation.fragment.findNavController
//import com.google.android.material.snackbar.Snackbar
//import com.jasoncavinder.inpen.demo.R
//import com.jasoncavinder.inpen.demo.data.LoginViewModel
//import com.jasoncavinder.insulinpendemoapp.ui.login.afterTextChanged
//import kotlinx.android.synthetic.main.fragment_login_create_user.*
//
//class CreateUserFragment : Fragment() {
//
//    companion object {
//        fun newInstance() = CreateUserFragment()
//    }
//
//    private lateinit var _loginViewModel: LoginViewModel
//    private lateinit var _navController: NavController
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_login_create_user, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        _navController = findNavController()
//
//        _loginViewModel = requireActivity().run { ViewModelProviders.of(this).get(LoginViewModel::class.java) }
//
//        _loginViewModel.createUserFormState.observe(this, Observer {
//            val createUserState = it ?: return@Observer
//
//            // disable create loggedInUser button unless all fields are valid
//            button_create_user.isEnabled = createUserState.isDataValid
//
//            createUserState.firstNameError?.let {
//                edit_text_first_name.error = getString(createUserState.firstNameError)
//            }
//            createUserState.lastNameError?.let {
//                edit_text_last_name.error = getString(createUserState.lastNameError)
//            }
//            createUserState.emailError?.let {
//                edit_text_email.error = getString(createUserState.emailError)
//            }
//            createUserState.passwordError?.let {
//                edit_text_password.error = getString(createUserState.passwordError)
//            }
//            createUserState.confirmError?.let {
//                edit_text_confirm.error = getString(createUserState.confirmError)
//            }
//        })
//
//        _loginViewModel.createUserResult.observe(this, Observer {
//            val createUserResult = it ?: return@Observer
//
//            group_form_create_user.visibility = View.VISIBLE
//            loading_bar.visibility = View.GONE
//            createUserResult.error?.let { showCreateUserFailed(createUserResult.error) }
//            createUserResult.success?.let {
//                Snackbar.make(requireParentFragment().requireView(),
//                    "Hello ${it.firstName}. Let's add your insulin penPoints.",
//                    Snackbar.LENGTH_LONG
//                ).show()
////                Toast.makeText(context, "Hello ${it.firstName}. Let's add your insulin penPoints.", Toast.LENGTH_LONG).show()
//                _navController.navigate(R.id.action_createUserFragment_to_addPenFragment)
//            }
//        })
//
//        fun dataChanged() = _loginViewModel.createUserDataChanged(
//            edit_text_first_name.text.toString(),
//            edit_text_last_name.text.toString(),
//            edit_text_email.text.toString(),
//            edit_text_password.text.toString(),
//            edit_text_confirm.text.toString()
//        )
//
//        edit_text_first_name.afterTextChanged { dataChanged() }
//        edit_text_last_name.afterTextChanged { dataChanged() }
//        edit_text_email.afterTextChanged { dataChanged() }
//        edit_text_password.apply { afterTextChanged { dataChanged() } }
//        edit_text_confirm.apply { afterTextChanged { dataChanged() } }
//
//        button_cancel.setOnClickListener {
//            hideKeyboard()
//            _navController.navigateUp()
//        }
//
//        button_create_user.setOnClickListener {
//            loading_bar.visibility = View.VISIBLE
//            group_form_create_user.visibility = View.GONE
//            _loginViewModel.createUser(
//                edit_text_first_name.text.toString(),
//                edit_text_last_name.text.toString(),
//                edit_text_email.text.toString(),
//                edit_text_password.text.toString()
//            )
//            hideKeyboard()
//        }
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//
//        requireActivity().onBackPressedDispatcher.addCallback(
//            this, // LifecycleOwner
//            object : OnBackPressedCallback(
//                true // default to enabled
//            ) {
//                override fun handleOnBackPressed() {
//                    cancel()
//                }
//            }
//        )
//    }
//
//    private fun showCreateUserFailed(@StringRes errorString: Int) {
//        Toast.makeText(context, errorString, Toast.LENGTH_LONG).show()
//    }
//
//    private fun cancel() {
//        hideKeyboard()
////        loginViewModel.userCancelledRegistration()
//        _navController.navigateUp()
//    }
//
//    private fun hideKeyboard() {
//        val view = this.view
//        view?.let { v ->
//            val imm = context?.applicationContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(v.windowToken, 0)
//        }
//    }
//}