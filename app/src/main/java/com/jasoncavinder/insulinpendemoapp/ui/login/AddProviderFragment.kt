/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.jasoncavinder.insulinpendemoapp.MainActivity
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.database.entities.provider.Provider
import com.jasoncavinder.insulinpendemoapp.databinding.FragmentAddProviderBinding
import com.jasoncavinder.insulinpendemoapp.utilities.Result
import com.jasoncavinder.insulinpendemoapp.viewmodels.CreateUserViewModel
import kotlinx.android.synthetic.main.fragment_add_provider.*


class AddProviderFragment : Fragment() {

    companion object {
        fun newInstance() = CreateUserFragment()
    }

    private lateinit var createUserViewModel: CreateUserViewModel
    private lateinit var navController: NavController

    private lateinit var _userId: LiveData<Result<String>>
    private var userId = ""

    lateinit var provider: LiveData<Provider>
        private set
    private var providerId = ""

    private var providerSearchCountDownTimer: CountDownTimer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createUserViewModel = ViewModelProviders.of(requireActivity())
            .get(CreateUserViewModel::class.java)

        provider = createUserViewModel.provider

        /*
    val name: String,
    val position: String? = null,
    val rating: Float = 0f,
    val intro: String? = null,
    val copay: Float? = null,
    val languages: List<String>? = null,
    val education: String? = null

         */
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val view = inflater.inflate(com.jasoncavinder.insulinpendemoapp.R.layout.fragment_add_provider, container, false)

        val fragmentAddProviderBinding =
            DataBindingUtil.inflate<FragmentAddProviderBinding>(
                inflater, R.layout.fragment_add_provider, container, false
            ).apply {
                viewModel = createUserViewModel
                lifecycleOwner = this@AddProviderFragment
            }

        // TODO: Can this be removed or moved?
        provider.observe(this, Observer { })

        val view = fragmentAddProviderBinding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        _userId = createUserViewModel.createUserResult

        _userId.observe(this, Observer {
            userId = when (val user = it ?: return@Observer) {
                is Result.Success -> user.data
                is Result.Error -> ""
            }
        })

        createUserViewModel.changeProviderResult.observe(this, Observer {
            when (it) {
                is Result.Error -> text_provider_search_error.apply {
                    this.text = it.exception.message
                    this.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    button_continue.text = getString(R.string.continue_btn)
                    button_continue.isEnabled = true
                }
                else -> return@Observer
            }
        })

        button_skip.setOnClickListener { nextStep() }

        findprovider()
    }

    private fun setProvider() {
        createUserViewModel.changeProvider(provider.value!!.providerId, userId)
        nextStep()
    }


    fun findprovider() {
        providerSearchCountDownTimer = object : CountDownTimer(3000, 1000) {
            override fun onFinish() {
                providerId = provider.value!!.providerId
                progress_bar.visibility = View.GONE
                provider_summary_display.visibility = View.VISIBLE
//                button_skip.isEnabled = false
                button_continue.apply {
                    isEnabled = true
                    text = getString(R.string.continue_btn)
                    setOnClickListener { setProvider() }
                }
            }

            override fun onTick(millisUntilFinished: Long) {}
        }.start()
    }

    private fun nextStep() {
        requireActivity().apply {
            this.setResult(
                Activity.RESULT_OK,
                Intent(activity, MainActivity::class.java)
            )
        }.finish()
    }
}
