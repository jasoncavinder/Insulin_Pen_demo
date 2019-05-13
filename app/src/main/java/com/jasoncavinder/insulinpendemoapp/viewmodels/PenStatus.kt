/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.viewmodels

import android.os.CountDownTimer
import java.util.*

class PenStatus : Observable() {
    private var _batteryCharge: Int = 100
        set(charge: Int) {
            field = charge
            batteryCharge = when (charge) {
                // 0, 20, 30, 50, 60, 80, 90, 100
                in (95..100) -> 100
                in (85..94) -> 90
                in (70..84) -> 80
                in (55..69) -> 60
                in (40..54) -> 50
                in (25..39) -> 30
                in (1..24) -> 20
                else -> 0
            }
            setChanged()
            notifyObservers()
        }

    private var batteryTimer: CountDownTimer? = null

    private fun chargeOrDrainBattery(cod: ChargeOrDrain) {
        batteryTimer?.cancel()
        if (cod == ChargeOrDrain.STOP) return

        data class TimerArgs(val time: Long, val tick: Long, val tickAction: () -> Unit, val finishAction: () -> Unit)
        val (time, tick, tickAction, finishAction) = run {
            when (cod) {
                ChargeOrDrain.CHARGE ->
                    TimerArgs(
                        15000L * (100 - _batteryCharge),
                        15000L,
                        { _batteryCharge.inc() },
                        { _batteryCharge = 100 }
                    )
                ChargeOrDrain.DRAIN ->
                    TimerArgs(
                        30000L * _batteryCharge,
                        30000L,
                        { _batteryCharge.dec() },
                        {
                            _batteryCharge = 0
                            isTransferring = false
                            isConnected = false
                        }
                    )
                else -> TimerArgs(0, 0, {}, {})
            }
        }

        batteryTimer = object : CountDownTimer(time, tick) {
            override fun onTick(millisUntilFinished: Long) = tickAction()
            override fun onFinish() = finishAction()
        }.start()
    }

    private enum class ChargeOrDrain { CHARGE, DRAIN, STOP }

    var batteryCharge: Int = _batteryCharge

    var isConnected: Boolean = false
        set(bool) {
            when (bool) {
                true -> if (!isPluggedIn) chargeOrDrainBattery(ChargeOrDrain.DRAIN)
                false -> if (!isPluggedIn) chargeOrDrainBattery(ChargeOrDrain.STOP)
            }
            field = bool
        }

    var isTransferring: Boolean = false
        set (bool) {
            field = bool
            if (bool) {
                val transferTimer = object : CountDownTimer(5000, 5000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        field = false
                        setChanged()
                        notifyObservers()
                    }
                }.start()
            }
            setChanged()
            notifyObservers()
        }

    var isPluggedIn: Boolean = false
        set(bool) {
            field = bool
            when (bool) {
                true -> chargeOrDrainBattery(ChargeOrDrain.CHARGE)
                false -> if (isConnected) chargeOrDrainBattery(ChargeOrDrain.DRAIN)
            }
        }
}
