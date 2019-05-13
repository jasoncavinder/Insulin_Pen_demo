/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.ui.main

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Group
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.adapters.MessageSummaryAdapter
import com.jasoncavinder.insulinpendemoapp.database.entities.message.Message
import com.jasoncavinder.insulinpendemoapp.databinding.FragmentHomeBinding
import com.jasoncavinder.insulinpendemoapp.utilities.DemoAction
import com.jasoncavinder.insulinpendemoapp.utilities.DemoActionListDialogFragment
import com.jasoncavinder.insulinpendemoapp.utilities.UpdateToolbarListener
import com.jasoncavinder.insulinpendemoapp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.content_messages.*
import kotlinx.android.synthetic.main.content_report_summary.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class HomeFragment : Fragment(), DemoActionListDialogFragment.Listener {
    private val TAG by lazy { this::class.java.simpleName }

//    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var navController: NavController

    private lateinit var updateToolbarListener: UpdateToolbarListener

    //messages Recycler
    private lateinit var messagesAdapter: RecyclerView.Adapter<*>
    private lateinit var messagesManager: RecyclerView.LayoutManager

    private var sampleMessageContent: ArrayList<String> = arrayListOf()


    /* BEGIN: Required for Demo Actions */
    private var _demoActions = arrayListOf(
        DemoAction("Simulate receive message", this::simulateReceiveMessage),
        DemoAction("Simulate temperature alert", this::simulateTempAlert)
    )

    private fun simulateReceiveMessage() {
        try {
            if (sampleMessageContent.size == 0) throw java.lang.Exception("sampleMessageContent is empty")

            val userId = mainViewModel.userProfile.value?.user?.userId
            if (userId.isNullOrBlank()) throw Exception("userId not found")

            val providerId = mainViewModel.userProfile.value?.provider?.first()?.providerId
            if (providerId.isNullOrBlank()) throw java.lang.Exception("providerId not found")

            val message = Message(
                messageId = 0,
                timeStamp = Calendar.getInstance().timeInMillis,
                providerId = providerId,
                userId = userId,
                content = sampleMessageContent.shuffled().first(),
                sent = false,
                read = false
            )

            mainViewModel.newMessage(message)
        } catch (ex: Exception) {
            Log.e(TAG, "Error creating sample message", ex)
            Toast.makeText(context, "Could not create sample message. See error log for details.", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun simulateTempAlert() {
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }
        builder?.setMessage("Your insulin pen was over 100ºC for 2 hours. Replace the cartridge before continuing your doses.")
            ?.setTitle("Temperature Alert")
        builder?.apply {
            setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                // TODO: Alert acknowledged
            })
        }
        val dialog: AlertDialog? = builder?.create()
    }

    override fun onDemoActionClicked(position: Int) {
        _demoActions[position].action()
    }
    /* END: Required for Demo Actions */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        mainViewModel = ViewModelProviders.of(requireActivity())
            .get(MainViewModel::class.java)

        requireContext().assets.open("sampleMessages.json").use { inputStream ->
            JsonReader(inputStream.reader()).use { jsonReader ->
                class SampleContent {
                    var content: String = ""
                }

                val stringType = object : TypeToken<List<String>>() {}.type
                val sampleContentList: List<String> = Gson().fromJson(jsonReader, stringType)
                for (sampleContent in sampleContentList) {
                    sampleMessageContent.add(sampleContent)
                }
            }
        }
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

//        mainViewModel.userProfile.value.pen.first().

        updateToolbarListener.updateToolbar(
            "At a Glance",
            R.menu.menu_home_left,
            R.menu.menu_home_right,
            mapOf(
                Pair(R.id.menu_item_profile_settings, R.id.action_homeFragment_to_profile),
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

        /* Messages Recycler */
        messagesManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        messagesAdapter = MessageSummaryAdapter(mainViewModel.unreadMessages)

        recycler_messages.apply {
            setHasFixedSize(true)
            layoutManager = messagesManager
            adapter = messagesAdapter
        }
        PagerSnapHelper().attachToRecyclerView(recycler_messages)


        frame_report_glucose.setOnClickListener {
            GlucoseChart().show(requireFragmentManager(), "glucose")
        }
        frame_report_glucose_wide.setOnClickListener {
            GlucoseChart().show(requireFragmentManager(), "glucose")
        }
        frame_report_inject.setOnClickListener {
            DoseChart().show(requireFragmentManager(), "dose")
        }
        frame_report_inject_wide.setOnClickListener {
            DoseChart().show(requireFragmentManager(), "dose")
        }
        frame_report_sleep.setOnClickListener {
            SleepChart().show(requireFragmentManager(), "sleep")
        }
        frame_report_sleep_wide.setOnClickListener {
            SleepChart().show(requireFragmentManager(), "sleep")
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

    override fun onResume() {
        super.onResume()

        mainViewModel.verifyLogin()

        /* BEGIN: Required for Demo Actions */
        fab_demo_actions_home.setOnClickListener {
            DemoActionListDialogFragment.newInstance(_demoActions)
                .show(childFragmentManager, "demoActionsDialog")
        }
        /* END: Required for Demo Actions */

    }

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

