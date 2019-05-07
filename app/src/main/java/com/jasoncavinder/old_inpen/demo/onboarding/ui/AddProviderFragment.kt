//package com.jasoncavinder.inpen.demo.onboarding.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProviders
//import androidx.navigation.NavController
//import androidx.navigation.fragment.findNavController
//import com.jasoncavinder.insulinpendemoapp.todo.MainActivity
//import com.jasoncavinder.inpen.demo.R
//import com.jasoncavinder.inpen.demo.data.LoginViewModel
//import com.jasoncavinder.insulinpendemoapp.database.entities.user.UserProfile
//import kotlinx.android.synthetic.main.fragment_add_provider.*
//
//class AddProviderFragment : Fragment() {
//
//    companion object {
//        fun newInstance() = CreateUserFragment()
//    }
//
//    private lateinit var _loginViewModel: LoginViewModel
//    private lateinit var _navController: NavController
//    private lateinit var _profile: UserProfile
//
//    private fun findProvider() {
//        TODO()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_add_provider, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        _navController = findNavController()
//
//        _loginViewModel = ViewModelProviders.of(requireActivity())
//            .get(LoginViewModel::class.java)
//
//        _loginViewModel.user.observe(viewLifecycleOwner, Observer { result ->
//            _profile = result
//        })
//
//        button_skip.setOnClickListener { nextStep() }
//
//
//    }
//
//    private fun nextStep() {
//        requireActivity().setResult(
//            Activity.RESULT_OK,
//            Intent(requireActivity(), MainActivity::class.java)
//                .putExtra("userID", _profile.user.userID)
//        )
//        requireActivity().finish()
//    }
//}
