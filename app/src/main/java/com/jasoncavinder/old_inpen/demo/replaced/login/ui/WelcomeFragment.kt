//package com.jasoncavinder.inpen.demo.replaced.login.ui
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.navigation.NavController
//import androidx.navigation.fragment.findNavController
//import com.jasoncavinder.inpen.demo.R
//import kotlinx.android.synthetic.main.fragment_login_welcome.view.*
//
//
//class WelcomeFragment : Fragment() {
//
//    companion object {
//        fun newInstance() = WelcomeFragment()
//    }
//
//    private lateinit var navController: NavController
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_login_welcome, container, false)
//    }
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        navController = findNavController()
//
//        view.button_signup.setOnClickListener {
//            navController.navigate(com.jasoncavinder.inpen.demo.R.id.action_welcomeFragment_to_createUserFragment)
//        }
//        view.button_signin.setOnClickListener {
//            navController.navigate(com.jasoncavinder.inpen.demo.R.id.action_welcomeFragment_to_loginFragment)
//        }
//
//    }
//
//    // TODO: Disable back button on this screen
//}
