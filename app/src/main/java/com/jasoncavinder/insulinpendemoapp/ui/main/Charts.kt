/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.ui.main

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.jasoncavinder.insulinpendemoapp.R

class GlucoseChart : DialogFragment() {

    private val TAG by lazy { this::class.java.simpleName }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            builder.setView(inflater.inflate(R.layout.modal_glucose_chart, null))
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class DoseChart : DialogFragment() {

    private val TAG by lazy { this::class.java.simpleName }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            builder.setView(inflater.inflate(R.layout.modal_dose_chart, null))
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class SleepChart : DialogFragment() {

    private val TAG by lazy { this::class.java.simpleName }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            builder.setView(inflater.inflate(R.layout.modal_sleep_chart, null))
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

