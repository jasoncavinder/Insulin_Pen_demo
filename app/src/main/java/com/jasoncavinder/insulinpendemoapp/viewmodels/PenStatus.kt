/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.viewmodels

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class PenStatus : Observable() {

    companion object {
        private var _batteryCharge = MutableLiveData<Int>()
    }

    private val _isConnecting = MutableLiveData<Boolean>()
    val isConnecting: LiveData<Boolean> = _isConnecting

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected

    private val _isTransferring = MutableLiveData<Boolean>()
    var isTransferring: LiveData<Boolean> = _isTransferring

    private val _isPluggedIn = MutableLiveData<Boolean>()
    var isPluggedIn: LiveData<Boolean> = _isPluggedIn

    private val _statusMessage = MediatorLiveData<String>()
    val statusMessage: LiveData<String> = _statusMessage

    var batteryCharge: LiveData<Int> = _batteryCharge

    private val pluggedInObserver = Observer<Boolean> {
        when {
            it -> {
                batteryChargeHandler.removeCallbacks(dischargeBattery)
                batteryChargeHandler.post(chargeBattery)
            }
            !it -> {
                batteryChargeHandler.removeCallbacks(chargeBattery)
                batteryChargeHandler.post(dischargeBattery)
            }
        }

    }
    private val connectedObserver = Observer<Boolean> { connected ->
        _batteryCharge.value.let { charge ->
            when (charge) {
                null -> _batteryCharge.postValue(100)
                0 -> _batteryCharge.postValue(charge.plus(10))
            }
        }
        when {
            connected -> _isPluggedIn.observeForever(pluggedInObserver)
            !connected -> _isPluggedIn.removeObserver(pluggedInObserver)
        }
    }
    private val batteryChargeHandler = Handler()
    private val chargeBattery = object : Runnable {
        override fun run() {
            _batteryCharge.postValue(_batteryCharge.value?.run { if (this < 100) this.inc() else 100 } ?: 100)
            _batteryCharge.value?.let { if (it != 100) batteryChargeHandler.postDelayed(this, 1500) }
        }
    }
    private val dischargeBattery = object : Runnable {
        override fun run() {
            _batteryCharge.postValue(_batteryCharge.value?.run { if (this > 0) this.dec() else 0 } ?: 0)
            _batteryCharge.value?.let { if (it == 0) disconnect() else batteryChargeHandler.postDelayed(this, 3000) }
        }
    }

    init {
        _statusMessage.addSource(_isTransferring) {
            _statusMessage.postValue(if (it) "Transferring pen data. Please wait." else "")
        }
        _statusMessage.addSource(_isConnecting) {
            _statusMessage.postValue(if (it) "Insulin pen connecting. Please wait." else "")
        }
        _statusMessage.addSource(_isConnected) {
            _statusMessage.postValue(if (it) "" else "Searching for insulin pen. Please make sure it is powered on and nearby.")
        }

        _isPluggedIn.value = false
        _isTransferring.value = false
        _isConnecting.value = false
        _isConnected.value = false

        _batteryCharge.value = 100
        _isConnected.observeForever(connectedObserver)
    }

    fun connect() {
        _isConnecting.postValue(true)
        GlobalScope.launch {
            withContext(Main) {
                delay(2000)
                _isConnecting.postValue(false)
                _isConnected.postValue(true)
                delay(500)
                transferPenData()
            }
        }
    }

    fun disconnect() {
        _isConnected.postValue(false)
    }

    fun transferPenData() {
        _isTransferring.postValue(true)
        GlobalScope.launch {
            withContext(Main) {
                delay(5000)
                _isTransferring.postValue(false)
            }
        }
    }

    fun charge() {
        _isPluggedIn.value = true
    }

    fun discharge() {
        _isPluggedIn.value = false
    }




}