package com.jasoncavinder.inpen.demo.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.jasoncavinder.inpen.demo.R
import com.jasoncavinder.inpen.demo.data.LoginViewModel

class AddProviderFragment : Fragment() {

    companion object {
        fun newInstance() = CreateUserFragment()
    }

    private lateinit var _loginViewModel: LoginViewModel
    private lateinit var _navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_provider, container, false)
    }

}
