/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.utilities.Result
import com.jasoncavinder.insulinpendemoapp.viewmodels.CreateUserViewModel
import kotlinx.android.synthetic.main.fragment_login_create_user.*

class CreateUserFragment : Fragment() {

    companion object {
        fun newInstance() = CreateUserFragment()
    }

    private lateinit var _createUserViewModel: CreateUserViewModel
    private lateinit var _navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login_create_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _navController = findNavController()

        _createUserViewModel =
            requireActivity().run { ViewModelProviders.of(this).get(CreateUserViewModel::class.java) }

        _createUserViewModel.createUserFormState.observe(this, Observer {
            val createUserState = it ?: return@Observer

            // disable create loggedInUser button unless all fields are valid
            button_create_user.isEnabled = createUserState.isDataValid

            createUserState.firstNameError?.let {
                edit_text_first_name.error = getString(createUserState.firstNameError)
            }
            createUserState.lastNameError?.let {
                edit_text_last_name.error = getString(createUserState.lastNameError)
            }
            createUserState.emailError?.let {
                edit_text_email.error = getString(createUserState.emailError)
            }
            createUserState.passwordError?.let {
                edit_text_email.error = getString(createUserState.passwordError)
            }
            createUserState.confirmError?.let {
                edit_text_confirm.error = getString(createUserState.confirmError)
            }
        })

        _createUserViewModel.createUserResult.observe(this, Observer {

            when (val createUserResult = it ?: return@Observer) {
                is Result.Error -> {
                    showCreateUserFailed(createUserResult.exception.localizedMessage)
                    loading_bar.visibility = View.GONE
                    group_form_create_user.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    Snackbar.make(
                        requireParentFragment().requireView(),
                        "Hello ${edit_text_first_name.text}. Let's add your insulin pen.",
                        Snackbar.LENGTH_LONG
                    ).show()
                    _navController.navigate(R.id.action_createUserFragment_to_addPenFragment)
                }
            }
        })

        fun dataChanged() = _createUserViewModel.createUserDataChanged(
            edit_text_first_name.text.toString(),
            edit_text_last_name.text.toString(),
            edit_text_email.text.toString(),
            edit_text_email.text.toString(),
            edit_text_confirm.text.toString()
        )

        edit_text_first_name.afterTextChanged { dataChanged() }
        edit_text_last_name.afterTextChanged { dataChanged() }
        edit_text_email.afterTextChanged { dataChanged() }
        edit_text_email.apply { afterTextChanged { dataChanged() } }
        edit_text_confirm.apply { afterTextChanged { dataChanged() } }

        button_cancel.setOnClickListener {
            hideKeyboard()
            _navController.navigateUp()
        }

        button_create_user.setOnClickListener {
            hideKeyboard()
            group_form_create_user.visibility = View.GONE
            loading_bar.visibility = View.VISIBLE
            _createUserViewModel.createUser(
                edit_text_first_name.text.toString(),
                edit_text_last_name.text.toString(),
                edit_text_email.text.toString(),
                edit_text_email.text.toString()
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(
                true // default to enabled
            ) {
                override fun handleOnBackPressed() {
                    cancel()
                }
            }
        )
    }

    private fun showCreateUserFailed(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    private fun cancel() {
        hideKeyboard()
        _navController.navigateUp()
    }

    private fun hideKeyboard() {
        val view = this.view
        view?.let { v ->
            val imm = context?.applicationContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}
