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
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.database.entities.payment.Payment
import com.jasoncavinder.insulinpendemoapp.database.entities.pen.PenWithDataPoints
import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider
import com.jasoncavinder.insulinpendemoapp.database.entities.user.User
import com.jasoncavinder.insulinpendemoapp.databinding.FragmentProfileBinding
import com.jasoncavinder.insulinpendemoapp.databinding.ModalEditPasswordBinding
import com.jasoncavinder.insulinpendemoapp.databinding.ModalEditProfileBinding
import com.jasoncavinder.insulinpendemoapp.utilities.Result
import com.jasoncavinder.insulinpendemoapp.utilities.UpdateToolbarListener
import com.jasoncavinder.insulinpendemoapp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.content_profile_full.*

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val TAG by lazy { this::class.java.simpleName }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var navController: NavController

    private lateinit var updateToolbarListener: UpdateToolbarListener

    private val user: LiveData<User> by lazy { mainViewModel.user }
    private val penWithData: LiveData<PenWithDataPoints> by lazy { mainViewModel.pen }
    private val provider: LiveData<Provider> by lazy { mainViewModel.provider }
    private val paymentMethod: LiveData<Payment> by lazy { mainViewModel.paymentMethod }

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
            DataBindingUtil.inflate<FragmentProfileBinding>(
                inflater, R.layout.fragment_profile, container, false
            ).apply {
                this.user = this@ProfileFragment.user
                this.penWithData = this@ProfileFragment.penWithData
                this.provider = this@ProfileFragment.provider
                this.paymentMethod = this@ProfileFragment.paymentMethod
                this.lifecycleOwner = viewLifecycleOwner
            }
        return fragmentProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        updateToolbarListener.updateToolbar(
            "Profile",
            R.menu.menu_profile_left,
            R.menu.menu_profile_right,
            mapOf(
                Pair(R.id.menu_item_payment_settings, R.id.action_profile_to_payment),
                Pair(R.id.menu_item_home, R.id.action_profileFragment_pop)
            )
        )

        button_edit_profile.setOnClickListener(editProfile())
        button_edit_password.setOnClickListener(changePassword())
        button_edit_payment.setOnClickListener { navController.navigate(R.id.action_profile_to_payment) }

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

        /* BEGIN: Required for Demo Actions */
        /*fab_demo_actions_home.setOnClickListener {
            DemoActionListDialogFragment.newInstance(_demoActions)
                .show(childFragmentManager, "demoActionsDialog")
        }*/
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
                            mainViewModel.updateUserResult.observe(this@ProfileFragment.viewLifecycleOwner, Observer {
                                when (it) {
                                    is Result.Error ->
                                        Snackbar.make(
                                            requireParentFragment().requireView(),
                                            "Failed to update user. Please try again or, if you can reproduce this error, file a bug report for the developer.",
                                            Snackbar.LENGTH_LONG
                                        ).show()
                                    is Result.Success ->
                                        Snackbar.make(
                                            requireParentFragment().requireView(),
                                            "Your profile has been updated successfully.",
                                            Snackbar.LENGTH_SHORT
                                        ).show()

                                }
                            })


                            mainViewModel.updateUserData(
                                firstName = binding.editTextFname.text.toString(),
                                lastName = binding.editTextLname.text.toString(),
                                email = binding.editTextEmail.text.toString(),
                                locationCity = binding.editTextCity.text.toString(),
                                locationState = binding.editTextState.text.toString()
                            )
                        }

                    }
                    binding.user = this@ProfileFragment.user
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
                    builder.create()
                } ?: throw IllegalStateException("Activity cannot be null")
            }
        }

        return View.OnClickListener { ChangePasswordDialog().show(requireFragmentManager(), "changePassword") }
    }

}

