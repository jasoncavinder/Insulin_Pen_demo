/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.utilities

import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.jasoncavinder.insulinpendemoapp.R


@BindingAdapter("visibleOrGone")
fun View.setVisibleOrGone(show: Boolean) {
    visibility = if (show) VISIBLE else GONE
}

@BindingAdapter("focusedByDefault")
fun View.focusedByDefault(focused: Boolean) {
    isFocusedByDefault = focused
}

@BindingAdapter("bind:charging", "bind:chargePercent")
fun ImageView.setBatteryChargeIcon(charging: Boolean = false, chargePercent: Int = 100) {
    val chargeIcon = when (chargePercent) {
        //                 0, 20, 30, 50, 60, 80, 90, 100
        in (95..100) -> if (charging) R.drawable.ic_battery_charging_full_black_24dp else R.drawable.ic_battery_full_black_24dp
        in (85..94) -> if (charging) R.drawable.ic_battery_charging_90_black_24dp else R.drawable.ic_battery_90_black_24dp
        in (70..84) -> if (charging) R.drawable.ic_battery_charging_80_black_24dp else R.drawable.ic_battery_80_black_24dp
        in (55..69) -> if (charging) R.drawable.ic_battery_charging_60_black_24dp else R.drawable.ic_battery_60_black_24dp
        in (40..54) -> if (charging) R.drawable.ic_battery_charging_50_black_24dp else R.drawable.ic_battery_50_black_24dp
        in (25..39) -> if (charging) R.drawable.ic_battery_charging_30_black_24dp else R.drawable.ic_battery_30_black_24dp
        in (5..24) -> if (charging) R.drawable.ic_battery_charging_20_black_24dp else R.drawable.ic_battery_20_black_24dp
        in (0..5) -> R.drawable.ic_battery_alert_black_24dp
        else -> R.drawable.ic_battery_unknown_black_24dp
    }
    setImageDrawable(resources.getDrawable(chargeIcon, null))
}

@BindingAdapter("android:text")
fun TextView.textFromList(list: List<String>?) {
    text = list?.joinToString()
}

@BindingAdapter("android:text")
fun TextView.textFromInnt(num: Int) {
    text = num.toString()
}

@BindingAdapter("app:srcCompat")
fun ImageView.setPhotoResource(photo: String?) {
    when (photo) {
        null -> Log.d("setImageResource (Binding Adapter)", "photo was null")
        else -> setImageResource(context.resources.getIdentifier(photo, "raw", context.packageName))
    }
}
