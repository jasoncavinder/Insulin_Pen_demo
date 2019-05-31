/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.utilities

import android.os.SystemClock
import android.view.MenuItem
import android.view.View

/*
* Using this: https://stackoverflow.com/a/16514644/4976526
* to resolve this: https://stackoverflow.com/a/52415792/4976526
*
* Sample Use:
*/


class SafeClick(val function: () -> Unit) : View.OnClickListener {
    companion object {
        private var lastClickTime: Long = 0
    }

    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 250) return
        lastClickTime = SystemClock.elapsedRealtime()

        run(this.function)
    }
}

class MenuSafeClick(val function: () -> Unit) : MenuItem.OnMenuItemClickListener {
    companion object {
        private var lastClickTime: Long = 0
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (SystemClock.elapsedRealtime() - lastClickTime < 250) return false
        lastClickTime = SystemClock.elapsedRealtime()

        run(this.function)
        return true
    }
}