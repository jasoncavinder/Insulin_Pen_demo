package com.jasoncavinder.inpen.demo.login.ui

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import com.jasoncavinder.inpen.demo.data.LoginViewModel

class CreateUserFragment : Fragment() {

    companion object {
        fun newInstance() = CreateUserFragment()
    }

    private val loginViewModel: LoginViewModel by activityViewModels()
    private lateinit var navController: NavController

    private lateinit var firstName: Editable
    private lateinit var lastName: Editable
    private lateinit var email: Editable
    private lateinit var password: Editable
    private lateinit var confirm: Editable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.jasoncavinder.inpen_demo.R.layout.fragment_login_create_user, container, false) }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        navController = findNavController()
//
//        firstName = view.edit_text_first_name.text
//        lastName = view.edit_text_last_name.text
//        email = view.edit_text_email.text
//        password = view.edit_text_password.text
//        confirm = view.edit_text_confirm.text
//
//        view.button_cancel.setOnClickListener { cancel() }
//
//        view.button_create_user.setOnClickListener {
//            if (firstName.toString().isBlank()
//                || lastName.toString().isBlank()
//            )
//                respond("Please enter your full name.")
//            else if (email.toString().isBlank()) respond("Please enter your email address.")
//            else if (password.toString().isBlank()
//                || confirm.toString().isBlank()
//            )
//                respond("Please enter and confirm your password.")
//            else if (password.toString() != confirm.toString())
//                respond("Your password doesn't match.\nPlease try again.")
//            else {
//                loginViewModel.createUserAndLogin(
//                    email.toString(),
//                    password.toString(),
//                    firstName.toString(),
//                    lastName.toString()
//                )
//            }
//        }
////        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, OnBackPressedCallback {
////            cancel()
////            true
////        })
//
//
//    }
//
//    private fun respond(msg: String) {
//        this.view?.let {
//            Snackbar.make(it, msg, Snackbar.LENGTH_LONG).show()
//        }
//    }
//
//    private fun cancel() {
//        hideKeyboard()
//        loginViewModel.userCancelledRegistration()
//        navController.popBackStack(R.id.createUserFragment, true)
//    }
//
//    private fun hideKeyboard() {
//        val view = this.view
//        view?.let { v ->
//            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(v.windowToken, 0)
//        }
//    }
//
////    // TODO: Rename method, update argument and hook method into UI event
////    fun onButtonPressed(uri: Uri) {
////        listener?.onFragmentInteraction(uri)
////    }
//
////    override fun onAttach(context: Context) {
////        super.onAttach(context)
////        if (context is OnFragmentInteractionListener) {
////            listener = context
////        } else {
////            throw RuntimeException("$context must implement OnFragmentInteractionListener")
////        }
////
////    }
//
////    override fun onDetach() {
////        super.onDetach()
////        listener = null
////
////    }
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     *
//     *
//     * See the Android Training lesson [Communicating with Other Fragments]
//     * (http://developer.android.com/training/basics/fragments/communicating.html)
//     * for more information.
//     */
////    interface OnFragmentInteractionListener {
////        // TODO: Update argument type and name
////        fun onFragmentInteraction(uri: Uri)
////    }
//
//
////    companion object {
////        /**
////         * Use this factory method to create a new instance of
////         * this fragment using the provided parameters.
////         *
////         * @return A new instance of fragment CreateUserFragment.
////         */
////        // TODO: Rename and change types and number of parameters
////        @JvmStatic
////        fun newInstance() =
////            CreateUserFragment().apply {
////                arguments = Bundle().apply {
////                }
////            }
////    }
//
}
