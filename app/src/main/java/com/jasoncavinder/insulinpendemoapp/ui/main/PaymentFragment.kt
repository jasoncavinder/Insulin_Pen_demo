/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.ui.main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.databinding.FragmentPaymentBinding
import com.jasoncavinder.insulinpendemoapp.databinding.ModalEditPasswordBinding
import com.jasoncavinder.insulinpendemoapp.databinding.ModalEditProfileBinding
import com.jasoncavinder.insulinpendemoapp.utilities.Result
import com.jasoncavinder.insulinpendemoapp.utilities.UpdateToolbarListener
import com.jasoncavinder.insulinpendemoapp.viewmodels.MainViewModel

class PaymentFragment : Fragment() {

    companion object {
        fun newInstance() = PaymentFragment()
    }

    private val TAG by lazy { this::class.java.simpleName }

    private lateinit var mainViewModel: MainViewModel
//    private lateinit var navController: NavController

    private lateinit var updateToolbarListener: UpdateToolbarListener

    /* BEGIN: Required for Demo Actions */
//    private var _demoActions = arrayListOf(
//        DemoAction("Simulate receive message", this::simulateReceiveMessage)
//    )
//    override fun onDemoActionClicked(position: Int) {
//        _demoActions[position].action()
//    }
//    /* END: Required for Demo Actions */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        mainViewModel = ViewModelProviders.of(requireActivity())
            .get(MainViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentProfileBinding =
            DataBindingUtil.inflate<FragmentPaymentBinding>(
                inflater, R.layout.fragment_payment, container, false
            ).apply {
                this.viewModel = mainViewModel
                this.lifecycleOwner = viewLifecycleOwner
            }

        return fragmentProfileBinding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateToolbarListener.updateToolbar(
            "Update Payment",
            R.menu.menu_payment_left,
            R.menu.menu_payment_right,
            mapOf(
                Pair(R.id.menu_item_profile_settings, R.id.action_paymentFragment_pop)
            )
        )

        mainViewModel.userProfile.observe(this, Observer { })

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            updateToolbarListener = context as UpdateToolbarListener
        } catch (castException: ClassCastException) {
            /** The activity does not implement the listener. */
            Log.d(TAG, "$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onResume() {
        super.onResume()

        mainViewModel.verifyLogin()

        // TODO: edit to updatePaymentResult
        mainViewModel.updateUserResult.observe(this, Observer {
            when (it) {
                is Result.Error ->
                    Snackbar.make(
                        requireView(),
                        "Failed to update user. Please try again or, if you can reproduce this error, file a bug report for the developer.",
                        Snackbar.LENGTH_LONG
                    ).show()
                is Result.Success ->
                    Snackbar.make(
                        requireView(),
                        "Your profile has been updated successfully.",
                        Snackbar.LENGTH_SHORT
                    ).show()

            }
        })

        /* BEGIN: Required for Demo Actions */
//        fab_demo_actions_home.setOnClickListener {
//            DemoActionListDialogFragment.newInstance(_demoActions)
//                .show(childFragmentManager, "demoActionsDialog")
//        }
        /* END: Required for Demo Actions */

    }

    private fun editProfile(): View.OnClickListener {
        class EditProfileDialog : DialogFragment() {

            override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
                return activity?.let {
                    val inflater = requireActivity().layoutInflater
                    val binding = DataBindingUtil.inflate<ModalEditProfileBinding>(
                        inflater, R.layout.modal_edit_profile, null, false
                    )
                    val builder = AlertDialog.Builder(it).apply {
                        setView(binding.root)

                        setNegativeButton("Cancel") { _, _ -> dialog?.cancel() }
                        setPositiveButton("Save") { _, _ ->
                            mainViewModel.updateUserData(
                                firstName = binding.editTextFname.text.toString(),
                                lastName = binding.editTextLname.text.toString(),
                                email = binding.editTextEmail.text.toString(),
                                locationCity = binding.editTextCity.text.toString(),
                                locationState = binding.editTextState.text.toString()
                            )
                        }

                    }
                    binding.viewModel = mainViewModel
                    builder.create()
                } ?: throw IllegalStateException("Activity cannot be null")
            }
        }

        return View.OnClickListener { EditProfileDialog().show(requireFragmentManager(), "editProfile") }

    }

    private fun changePassword(): View.OnClickListener {
        class ChangePasswordDialog : DialogFragment() {

            override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
                return activity?.let {
                    val inflater = requireActivity().layoutInflater
                    val binding = DataBindingUtil.inflate<ModalEditPasswordBinding>(
                        inflater, R.layout.modal_edit_password, null, false
                    )
                    val builder = AlertDialog.Builder(it).apply {
                        setView(binding.root)

                        setNegativeButton("Cancel") { _, _ -> dialog?.cancel() }
                        setPositiveButton("Save") { _, _ ->
                            mainViewModel.changePassword(password = binding.editTextPassword.text.toString())
                        }
                    }
                    binding.viewModel = mainViewModel
                    builder.create()
                } ?: throw IllegalStateException("Activity cannot be null")
            }
        }

        return View.OnClickListener { ChangePasswordDialog().show(requireFragmentManager(), "changePassword") }
    }

}

