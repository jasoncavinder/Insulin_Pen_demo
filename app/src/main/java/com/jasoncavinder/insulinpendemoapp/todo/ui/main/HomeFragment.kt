/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.todo.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.Group
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.database.entities.user.UserProfile
import com.jasoncavinder.insulinpendemoapp.databinding.FragmentHomeBinding
import com.jasoncavinder.insulinpendemoapp.todo.viewmodels.MainViewModel
import com.jasoncavinder.insulinpendemoapp.utilities.UpdateToolbarListener
import kotlinx.android.synthetic.main.content_messages.*
import kotlinx.android.synthetic.main.content_report_summary.*


class HomeFragment : Fragment() {
    private val TAG by lazy { this::class.java.simpleName }

//    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var navController: NavController

    private lateinit var updateToolbarListener: UpdateToolbarListener
    lateinit var userProfile: LiveData<UserProfile>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        mainViewModel = ViewModelProviders.of(requireActivity())
            .get(MainViewModel::class.java)

        userProfile = mainViewModel.userProfile

        userProfile.observe(this, Observer { })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.fragment_home, container, false)
        val fragmentHomeBinding =
            DataBindingUtil.inflate<FragmentHomeBinding>(
                inflater, R.layout.fragment_home, container, false
            ).apply {
                this.viewModel = mainViewModel
                this.lifecycleOwner = this@HomeFragment
            }

        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateToolbarListener.updateToolbar(
            "At a Glance",
            R.menu.menu_home_left,
            R.menu.menu_home_right,
            mapOf(
                Pair(R.id.menu_item_profile_settings, R.id.action_nav_fail_safe),
                Pair(R.id.menu_item_dose_history, R.id.action_nav_fail_safe)
            )
        )

        // Setup toggle listeners
        // TODO: move redundant code to a class
        icon_message_toggle.setOnClickListener {
            toggleVisibility(
                group_message_display,
                R.drawable.more_unfold_24dp,
                R.drawable.less_unfold_24dp,
                it as ImageView
            )
        }
        icon_report_toggle.setOnClickListener {
            toggleVisibility(
                group_report_long,
                R.drawable.more_unfold_24dp,
                R.drawable.less_unfold_24dp,
                it as ImageView
            )
            toggleVisibility(
                group_report_short, null, null, null
            )
        }

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

    // TODO: replace with DemoActionsFab
//    fun onFabClicked(uri: Uri) {
//        listener?.showDemoActionsDialog(uri)
//    }


    fun toggleVisibility(group: Group, visibleSrc: Int?, goneSrc: Int?, toggle: ImageView?) {

        when (group.visibility) {
            View.VISIBLE -> {
                group.visibility = View.GONE
                toggle?.let {
                    visibleSrc?.let {
                        try {
                            toggle.setImageResource(visibleSrc)
                        } catch (e: Error) {
                        }
                    }
                }
            }
            View.GONE -> {
                group.visibility = View.VISIBLE
                toggle?.let {
                    goneSrc?.let {
                        try {
                            toggle.setImageResource(goneSrc)
                        } catch (e: Error) {
                        }
                    }
                }
            }
            else -> return
        }
    }

}