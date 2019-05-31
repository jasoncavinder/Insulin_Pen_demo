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
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Group
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.adapters.MessageSummaryAdapter
import com.jasoncavinder.insulinpendemoapp.database.entities.dose.Dose
import com.jasoncavinder.insulinpendemoapp.database.entities.dose.DoseType
import com.jasoncavinder.insulinpendemoapp.database.entities.message.Message
import com.jasoncavinder.insulinpendemoapp.databinding.FragmentHomeBinding
import com.jasoncavinder.insulinpendemoapp.databinding.ModalEditDosesBinding
import com.jasoncavinder.insulinpendemoapp.databinding.ModalReportDoseBinding
import com.jasoncavinder.insulinpendemoapp.utilities.DemoAction
import com.jasoncavinder.insulinpendemoapp.utilities.DemoActionListDialogFragment
import com.jasoncavinder.insulinpendemoapp.utilities.Result
import com.jasoncavinder.insulinpendemoapp.utilities.UpdateToolbarListener
import com.jasoncavinder.insulinpendemoapp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.content_messages.*
import kotlinx.android.synthetic.main.content_profile_summary.*
import kotlinx.android.synthetic.main.content_report_summary.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.time.LocalDate
import java.time.LocalTime
import java.util.*


class HomeFragment : Fragment(), DemoActionListDialogFragment.Listener {
    val TAG by lazy { this::class.java.simpleName }

//    private val viewModel: MainViewModel by activityViewModels()

    lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController

    private lateinit var updateToolbarListener: UpdateToolbarListener

    //messages Recycler
    private lateinit var messagesAdapter: RecyclerView.Adapter<*>
    private lateinit var messagesManager: RecyclerView.LayoutManager

    private var sampleMessageContent: ArrayList<String> = arrayListOf()


    /* BEGIN: Required for Demo Actions */
    private var _demoActions: ArrayList<DemoAction> = arrayListOf(
        DemoAction("Simulate receive message", this::simulateReceiveMessage),
        DemoAction("Simulate dose alert", this::simulateDoseAlert)
    )

    private fun simulateConnectPen() = viewModel.penStatus.connect()
    private fun simulateDisconnectPen() = viewModel.penStatus.disconnect()
    private fun simulatePutPenOnCharger() = viewModel.penStatus.charge()
    private fun simulateRemovePenFromCharger() = viewModel.penStatus.discharge()
    private fun simulateReceiveMessage() {
        try {
            if (sampleMessageContent.size == 0) throw java.lang.Exception("sampleMessageContent is empty")

            val userId = viewModel.user.value?.userId
                ?: throw Exception("userId not found")

            val providerId = viewModel.provider.value?.providerId
                ?: throw java.lang.Exception("providerId not found")

            val message = Message(
                messageId = 0,
                timeStamp = Calendar.getInstance(),
                providerId = providerId,
                userId = userId,
                content = sampleMessageContent.shuffled().first(),
                sent = false,
                read = false
            )

            viewModel.newMessage(message)
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
            ?.setIcon(R.drawable.ic_warning_black_24dp)
        builder?.apply {
            setPositiveButton("OK") { _, _ ->
                // TODO: Alert acknowledged
            }
        }
        val dialog: AlertDialog? = builder?.create()
        dialog?.show()
    }

    private fun simulateDoseAlert() {
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }
        builder?.setMessage("Your Last Insulin Injection was 3.5U with a Normal Blood Glucose Value of 80 mg/dL. Based on Big Data and AI, your Next Injection will be at 7PM. We will Notify you Automatically.")
            ?.setTitle("Dose Alert")
            ?.setIcon(R.drawable.ic_event_available_black_24dp)
        builder?.apply {
            setPositiveButton("Ok, Schedule Alert") { _, _ ->
                // TODO:
            }
            setNegativeButton("Don't Schedule") { _, _ ->
                // TODO:
            }
        }
        val dialog: AlertDialog? = builder?.create()
        dialog?.show()
    }

    private fun simulateBolusDose() {
        viewModel.nextBolusDose.value?.let {
            viewModel.injectDose(it)
            _demoActions.remove(DemoAction("Simulate inject bolus dose", this::simulateBolusDose))
            viewModel.injectDoseResult.observe(this, Observer { result ->
                when (result) {
                    is Result.Success -> showCompletedInjectionDialog(result.data)
                    is Result.Error -> Snackbar.make(
                        requireParentFragment().requireView(),
                        "An error occurred while updating your dose history.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    override fun onDemoActionClicked(position: Int) {
        _demoActions[position].action()
    }
    /* END: Required for Demo Actions */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Entered onCreate")
        setHasOptionsMenu(true)

        viewModel = ViewModelProviders.of(requireActivity())
            .get(MainViewModel::class.java)

        viewModel.penStatus.isConnected.observe(this, Observer { connected ->
            if (connected) {
                _demoActions.addAll(
                    arrayOf(
                        DemoAction("Simulate disconnect pen (bluetooth)", this::simulateDisconnectPen),
                        DemoAction("Simulate put pen on charger", this::simulatePutPenOnCharger),
                        DemoAction("Simulate remove pen from charger", this::simulateRemovePenFromCharger),
                        DemoAction("Simulate temperature alert", this::simulateTempAlert)
                    )
                )
                _demoActions.remove(DemoAction("Simulate connect pen (bluetooth)", this::simulateConnectPen))
            } else {
                _demoActions.addAll(arrayOf(DemoAction("Simulate connect pen (bluetooth)", this::simulateConnectPen)))
                _demoActions.removeAll(
                    arrayOf(
                        DemoAction("Simulate disconnect pen (bluetooth)", this::simulateDisconnectPen),
                        DemoAction("Simulate put pen on charger", this::simulatePutPenOnCharger),
                        DemoAction("Simulate remove pen from charger", this::simulateRemovePenFromCharger),
                        DemoAction("Simulate temperature alert", this::simulateTempAlert)
                    )
                )
            }
        })

        viewModel.nextBolusDose.observe(this, Observer { dose ->
            if (dose == null)
                _demoActions.remove(DemoAction("Simulate inject bolus dose", this::simulateBolusDose))
            else
                _demoActions.add(DemoAction("Simulate inject bolus dose", this::simulateBolusDose))

        })

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
        val fragmentHomeBinding =
            DataBindingUtil.inflate<FragmentHomeBinding>(
                inflater, R.layout.fragment_home, container, false
            ).apply {
                this.user = viewModel.user
                this.penStatus = viewModel.penStatus
                this.nextBasalDose = viewModel.nextBasalDose
                this.nextBolusDose = viewModel.nextBolusDose
                this.noMessages = viewModel.noMessages
                this.lifecycleOwner = viewLifecycleOwner
            }

        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "Entered onViewCreated")

        updateToolbarListener.updateToolbar(
            "At a Glance",
            R.menu.menu_home_left,
            R.menu.menu_home_right,
            mapOf(
                Pair(R.id.menu_item_profile_settings, R.id.action_homeFragment_to_profileFragment),
//                Pair(R.id.menu_item_dose_history, R.id.action_nav_fail_safe)
                Pair(R.id.menu_item_messaging, R.id.action_homeFragment_to_messageFragment)
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
        messagesAdapter = MessageSummaryAdapter(viewModel.unreadMessages, providers = viewModel.providers)

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

        viewModel.provider.observe(this, Observer { })

        viewModel.providers.observe(this, Observer {
            messagesAdapter.notifyDataSetChanged()
        })

        viewModel.unreadMessages.observe(this, Observer {
            messagesAdapter.notifyDataSetChanged()
        })

        button_logout.setOnClickListener {
            viewModel.logout()
        }

        button_set_schedule.setOnClickListener {
            showEditDosesDialog(doseType = DoseType.BASAL)
        }
        button_change_basal.setOnClickListener {
            showEditDosesDialog(dose = viewModel.nextBasalDose.value)
        }
        button_calculate.setOnClickListener {
            showEditDosesDialog(doseType = DoseType.BOLUS)
        }
        button_change_bolus.setOnClickListener {
            showEditDosesDialog(dose = viewModel.nextBolusDose.value)
        }
        button_next_message.setOnClickListener {
            (recycler_messages.layoutManager as LinearLayoutManager).let {
                if (it.findFirstVisibleItemPosition() < it.itemCount)
                    recycler_messages.smoothScrollToPosition(it.findFirstVisibleItemPosition().inc())
            }
        }
        button_prev_message.setOnClickListener {
            (recycler_messages.layoutManager as LinearLayoutManager).let {
                if (it.findFirstVisibleItemPosition() > 0)
                    recycler_messages.smoothScrollToPosition(it.findFirstVisibleItemPosition().dec())
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "Entered on Attach")
        try {
            updateToolbarListener = context as UpdateToolbarListener
        } catch (castException: ClassCastException) {
            /** The activity does not implement the listener. */
            Log.d(TAG, context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "Entered onResume")

        /* BEGIN: Required for Demo Actions */
        fab_demo_actions_home.setOnClickListener {
            DemoActionListDialogFragment.newInstance(_demoActions)
                .show(childFragmentManager, "demoActionsDialog")
        }
        /* END: Required for Demo Actions */

    }

    private fun toggleVisibility(group: Group, visibleSrc: Int?, goneSrc: Int?, toggle: ImageView?) {

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

    fun showCompletedInjectionDialog(dose: Dose) {
        class ReportDoseDialog(val dose: Dose) : DialogFragment() {
            override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
                return activity?.let {
                    val inflater = requireActivity().layoutInflater
                    val binding = DataBindingUtil.inflate<ModalReportDoseBinding>(
                        inflater, R.layout.modal_report_dose, null, false
                    )
                    val builder = AlertDialog.Builder(it).apply {
                        setView(binding.root)

                        setPositiveButton("Report") { _, _ ->
                            /* TODO */
                        }
                    }
                    binding.title = "Successful Injection"
                    binding.dose = dose
                    binding.provider = viewModel.provider

                    builder.create()
                } ?: throw IllegalStateException("Activity cannot be null")
            }
        }

        ReportDoseDialog(dose = dose).show(
            requireFragmentManager(),
            "reportInjection"
        )
    }

    fun showEditDosesDialog(
        dose: Dose? = null,
        doseType: DoseType = dose!!.type
    ) {
        class EditDosesDialog(
            val dose: Dose? = null,
            val doseType: DoseType = dose!!.type
        ) : DialogFragment() {
            override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
                return activity?.let {
                    val dosage = MainViewModel.Dosage(actualBloodSugar = 220, userWeightInPounds = 170)
                    val inflater = requireActivity().layoutInflater
                    val binding = DataBindingUtil.inflate<ModalEditDosesBinding>(
                        inflater, R.layout.modal_edit_doses, null, false
                    )
                    val builder = AlertDialog.Builder(it).apply {
                        setView(binding.root)

                        setNegativeButton("Cancel") { _, _ -> dialog?.cancel() }
                        dose?.run {
                            setNeutralButton("unschedule") { _, _ ->
                                viewModel.removeScheduledDose(dose)
                            }
                        }
                        fun setDose(
                            type: DoseType = doseType,
                            time: Calendar? = null,
                            amount: Int,
                            bloodSugar: Int? = null
                        ) {
                            viewModel.editDoseResult.observe(this@HomeFragment.viewLifecycleOwner, Observer { result ->
                                when (result) {
                                    is Result.Error ->
                                        Snackbar.make(
                                            requireParentFragment().requireView(),
                                            "Failed to edit dose(s). Please try again or, if you can reproduce this error, file a bug report for the developer.",
                                            Snackbar.LENGTH_LONG
                                        ).show()
                                    is Result.Success ->
                                        Snackbar.make(
                                            requireParentFragment().requireView(),
                                            "Dose updated", //TODO
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                }
                            })
                            viewModel.updateDose(
                                Dose(
                                    doseId = binding.dose?.doseId,
                                    userId = viewModel.userId.value!!,
                                    type = type,
                                    scheduledTime = time,
                                    scheduledAmount = amount,
                                    bloodSugar = bloodSugar
                                )
                            )
                        }

                        when (doseType) {
                            DoseType.BASAL ->
                                setPositiveButton("Save") { _, _ ->
                                    var timeText: String = binding.editTextDoseTime.text.toString()
                                    if (timeText[1] == ':') timeText = "0".plus(timeText)
                                    val localTime = LocalTime.parse(timeText)
                                    val localDate =
                                        LocalDate.now().plusDays(if (localTime.isBefore(LocalTime.now())) 1L else 0L)

                                    val scheduledTime = GregorianCalendar.from(
                                        localTime
                                            .atDate(localDate)
                                            .atZone(TimeZone.getDefault().toZoneId())
                                    ) as Calendar
                                    setDose(type = doseType, time = scheduledTime, amount = dosage.basalDose)
                                }
                            DoseType.BOLUS -> {
                                setPositiveButton("Save") { _, _ ->
                                    setDose(
                                        type = doseType,
                                        amount = binding.calcResult ?: throw Exception("Dose amount cannot bet Null"),
                                        bloodSugar = binding.editTextBloodSugar.text.toString().toIntOrNull()
                                    )
                                }
                            }
                        }
                    }
                    binding.title = when (dose) {
                        null -> when (doseType) {
                            DoseType.BASAL -> "Schedule Basal Doses"
                            DoseType.BOLUS -> "Calculate Bolus Dose"
                        }
                        else -> when (doseType) {
                            DoseType.BASAL -> "Change Basal Dose Schedule"
                            DoseType.BOLUS -> "Change Bolus Dose"
                        }
                    }
                    binding.guidance = when (doseType) {
                        DoseType.BASAL -> getString(R.string.guidance_basal)
                        DoseType.BOLUS -> getString(R.string.guidance_bolus)
                    }
                    binding.dose = this.dose
                    binding.doseType = this.doseType
                    binding.providerName = viewModel.provider.value?.name ?: "your doctor"
                    binding.bloodSugar = dosage.actualBloodSugar.toString()
                    binding.editTextBloodSugar.doAfterTextChanged { bloodSugar ->
                        dosage.actualBloodSugar = bloodSugar.toString().toIntOrNull() ?: 0
                        binding.calcResult = dosage.bolusDose
                    }
                    binding.editTextCarbs.doAfterTextChanged { carbs ->
                        dosage.carbsAtMeal = carbs.toString().toIntOrNull() ?: 0
                        binding.calcResult = dosage.bolusDose
                    }
                    builder.create()
                } ?: throw IllegalStateException("Activity cannot be null")
            }
        }

        EditDosesDialog(dose = dose, doseType = doseType)
            .show(
                requireFragmentManager(),
                when (doseType) {
                    DoseType.BASAL -> "editBasalDoses"
                    DoseType.BOLUS -> "editBolusDose"
                }
            )
    }

}
