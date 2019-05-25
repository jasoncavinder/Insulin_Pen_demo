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


@BindingAdapter("visibleOrGone")
fun View.setVisibleOrGone(show: Boolean) {
    visibility = if (show) VISIBLE else GONE
}

@BindingAdapter("focusedByDefault")
fun View.focusedByDefault(focused: Boolean) {
    isFocusedByDefault = focused
}
object BindingUtils {

    private val TAG by lazy { this::class.java.simpleName }

    @BindingAdapter("android:text")
    @JvmStatic
    fun setText(view: TextView, list: List<String>?) {
        view.text = list?.joinToString { it -> it }
    }

    @BindingAdapter("app:srcCompat")
    @JvmStatic
    fun setImageResource(view: ImageView, photo: String?) {
        when (photo) {
            null -> Log.d(TAG, "photo was null")
            else -> view.setImageResource(
                view.context.resources.getIdentifier(
                    photo, "raw", view.context.packageName
                )
            )
        }
    }
}
