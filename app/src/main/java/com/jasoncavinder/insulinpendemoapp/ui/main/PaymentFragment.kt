/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.database.entities.payment.Payment
import com.jasoncavinder.insulinpendemoapp.database.entities.payment.PaymentType
import com.jasoncavinder.insulinpendemoapp.databinding.FragmentPaymentBinding
import com.jasoncavinder.insulinpendemoapp.ui.login.afterTextChanged
import com.jasoncavinder.insulinpendemoapp.utilities.Result
import com.jasoncavinder.insulinpendemoapp.utilities.UpdateToolbarListener
import com.jasoncavinder.insulinpendemoapp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_payment.*

class PaymentFragment : Fragment() {

    private val TAG by lazy { this::class.java.simpleName }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var navController: NavController

    private lateinit var updateToolbarListener: UpdateToolbarListener

    private var newPayment: MutableLiveData<Payment> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        mainViewModel = ViewModelProviders.of(requireActivity())
            .get(MainViewModel::class.java)

        mainViewModel.paymentMethod.value?.let { newPayment.value = it }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentProfileBinding =
            DataBindingUtil.inflate<FragmentPaymentBinding>(
                inflater, R.layout.fragment_payment, container, false
            ).apply {
                this.user = mainViewModel.user
                this.paymentMethod = newPayment
                this.lifecycleOwner = viewLifecycleOwner
            }

        return fragmentProfileBinding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        updateToolbarListener.updateToolbar(
            "Update Payment",
            R.menu.menu_empty,
            R.menu.menu_payment_right,
            mapOf(
                Pair(R.id.menu_item_profile_settings, R.id.action_paymentFragment_pop)
            )
        )

        image_button_brand_visa.setOnClickListener { setPaymentType(PaymentType.VISA) }
        image_button_brand_mastercard.setOnClickListener { setPaymentType(PaymentType.MASTER) }
        image_button_brand_amazon_pay.setOnClickListener { setPaymentType(PaymentType.AMAZON) }
        image_button_brand_paypal.setOnClickListener { setPaymentType(PaymentType.PAYPAL) }

        button_cancel.setOnClickListener { navController.navigate(R.id.action_paymentFragment_pop) }
        button_save.setOnClickListener {
            mainViewModel.updatePaymentResult.observe(this, Observer {
                Snackbar.make(
                    requireView(),
                    when (it) {
                        is Result.Success -> "Your payment method has been updated."
                        is Result.Error -> String.format("Failed to update payment method: %s", it.exception)
                    },
                    Snackbar.LENGTH_LONG
                ).show()
                navController.navigate(R.id.action_paymentFragment_pop)
            })
            newPayment.value?.let { mainViewModel.updatePaymentMethod(it) }
        }

        edit_text_email.afterTextChanged {
            newPayment.value = newPayment.value?.copy(email = if (it.isNotBlank()) it else null)
        }
        edit_text_card_number.afterTextChanged {
            newPayment.value = newPayment.value?.copy(ccnum = if (it.isNotBlank()) it.toLong() else null)
        }
        edit_text_expiration.afterTextChanged {
            newPayment.value = newPayment.value?.copy(ccexp = if (it.isNotBlank()) it.toInt() else null)
        }
        edit_text_name_on_card.afterTextChanged {
            newPayment.value = newPayment.value?.copy(ccname = if (it.isNotBlank()) it else null)
        }

        newPayment.observe(this, Observer { payment ->
            payment?.let {
                when (payment.type) {

                    PaymentType.VISA, PaymentType.MASTER -> {
                        if (payment.let { it.ccnum?.let { num -> num > 1000000000000000L } == true }
                                .also { result ->
                                    if (!result) edit_text_card_number.error = getString(R.string.error_ccnum)
                                }
                            &&
                            payment.let { it.ccexp?.let { exp -> exp > 119 } == true }
                                .also { result ->
                                    if (!result) edit_text_expiration.error = getString(R.string.error_ccexp)
                                }
                            &&
                            payment.let { it.ccname?.isNotBlank() == true }
                                .also { result ->
                                    if (!result) edit_text_name_on_card.error = getString(R.string.error_ccname)
                                }
                        ) {
                            button_save.isEnabled = true
                            return@Observer
                        }
                    }
                    PaymentType.PAYPAL, PaymentType.AMAZON -> {
                        if (payment.let {
                                it.email?.let { email ->
                                    Patterns.EMAIL_ADDRESS.matcher(email).matches()
                                } == true
                            }
                                .also { result ->
                                    if (!result) edit_text_email.error = getString(R.string.error_payment_email)
                                }
                        ) {
                            button_save.isEnabled = true
                            return@Observer
                        }
                    }
                }
            }
            button_save.isEnabled = false
        })

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            updateToolbarListener = context as UpdateToolbarListener
        } catch (castException: ClassCastException) {
            /** The activity does not implement the listener. */
            Log.d(TAG, "$context must implement UpdateToolbarListener")
        }
    }

    override fun onResume() {
        super.onResume()

//        mainViewModel.verifyLogin()

        newPayment.value = newPayment.value?.copy(email = mainViewModel.user.value?.email)

    }

    private fun setPaymentType(type: PaymentType) {
        newPayment.apply {
            value.let {
                value = when (it) {
                    null -> Payment(
                        userId = mainViewModel.user.value?.userId ?: throw Exception("userId cannot be null"),
                        type = type
                    )
                    else -> it.copy(type = type)
                }
            }
        }
    }
}
