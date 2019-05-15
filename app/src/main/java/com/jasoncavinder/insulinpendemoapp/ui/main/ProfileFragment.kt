/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.databinding.FragmentProfileBinding
import com.jasoncavinder.insulinpendemoapp.utilities.UpdateToolbarListener
import com.jasoncavinder.insulinpendemoapp.viewmodels.CreateUserViewModel
import com.jasoncavinder.insulinpendemoapp.viewmodels.MainViewModel

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val TAG by lazy { this::class.java.simpleName }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var createUserViewModel: CreateUserViewModel
    private lateinit var navController: NavController

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
        createUserViewModel = ViewModelProviders.of(requireActivity())
            .get(CreateUserViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentProfileBinding =
            DataBindingUtil.inflate<FragmentProfileBinding>(
                inflater, R.layout.fragment_profile, container, false
            ).apply {
                this.viewModel = mainViewModel
                this.cuViewModel = createUserViewModel
                this.lifecycleOwner = viewLifecycleOwner
            }

        return fragmentProfileBinding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateToolbarListener.updateToolbar(
            "At a Glance",
            R.menu.menu_profile_left,
            R.menu.menu_profile_right,
            mapOf(
                Pair(R.id.menu_item_home, R.id.action_profileFragment_pop),
                Pair(R.id.menu_item_dose_history, R.id.action_nav_fail_safe)//R.id.action_profile_to_payment)
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
            Log.d(TAG, context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onResume() {
        super.onResume()

        mainViewModel.verifyLogin()

        /* BEGIN: Required for Demo Actions */
//        fab_demo_actions_home.setOnClickListener {
//            DemoActionListDialogFragment.newInstance(_demoActions)
//                .show(childFragmentManager, "demoActionsDialog")
//        }
        /* END: Required for Demo Actions */

    }

}

