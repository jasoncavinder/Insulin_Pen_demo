/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.ui.login

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.utilities.DemoAction
import com.jasoncavinder.insulinpendemoapp.utilities.DemoActionListDialogFragment
import com.jasoncavinder.insulinpendemoapp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_login_welcome.*
import kotlinx.android.synthetic.main.fragment_login_welcome.view.*

class WelcomeFragment : Fragment(), DemoActionListDialogFragment.Listener {

    private val TAG by lazy { this::class.java.simpleName }

    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController

    private var backPressedTimer: CountDownTimer? = null
    private var backToast: Toast? = null

    /* BEGIN: Required for Demo Actions */
    private var _demoActions = arrayListOf(
        DemoAction(
            "Reset Database to Demo Defaults: (delete users, pens, etc)",
            this::resetData
        )
    )

    override fun onDemoActionClicked(position: Int) {
        _demoActions[position].action()
    }
    /* END: Required for Demo Actions */

    private fun resetData() {
        viewModel.resetDb()
        Snackbar.make(requireView(), "Database has been reset.", Snackbar.LENGTH_LONG).show()
        fab_demo_actions_welcome.visibility = View.GONE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "Creating view")
        return inflater.inflate(R.layout.fragment_login_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "Entering 'onViewCreated'")
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        viewModel = requireActivity().run { ViewModelProviders.of(this).get(MainViewModel::class.java) }

        viewModel.localUsers.observe(this, Observer {
            when (it) {
                0 -> {
                    view.button_signin.isEnabled = false
                    view.fab_demo_actions_welcome.visibility = View.GONE
                }
                else -> {
                    view.button_signin.isEnabled = true
                    view.fab_demo_actions_welcome.visibility = View.VISIBLE
                }
            }
        })

        view.button_signup.setOnClickListener {
            navController.navigate(R.id.action_welcomeFragment_to_createUserFragment)
        }
        view.button_signin.setOnClickListener {
            navController.navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
        Log.d(TAG, "Finished 'onCreateView'")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "Resuming")
        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (backPressedTimer) {
                    null -> {
                        backToast = Toast.makeText(
                            requireContext().applicationContext,
                            "Press back again to exit",
                            Toast.LENGTH_SHORT
                        )
                        backToast?.show()
                        backPressedTimer = object : CountDownTimer(2000, 1000) {
                            override fun onFinish() {
                                backPressedTimer = null
                                backToast?.cancel()
                            }

                            override fun onTick(millisUntilFinished: Long) {}
                        }.start()
                    }
                    else -> {
                        backToast?.cancel()
                        requireActivity().finish()
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backCallback)

        /* BEGIN: Required for Demo Actions */
        fab_demo_actions_welcome.setOnClickListener {
            DemoActionListDialogFragment.newInstance(_demoActions)
                .show(childFragmentManager, "demoActionsDialog")
        }
        /* END: Required for Demo Actions */
    }


    companion object {
        fun newInstance() = WelcomeFragment()
    }

}
