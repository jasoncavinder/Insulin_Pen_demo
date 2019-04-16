package com.jasoncavinder.insulinpen_demo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation
import androidx.constraintlayout.widget.Barrier
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home_create_user.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CreateUserFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CreateUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CreateUserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var activityHasActionBar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        if (activity is AppCompatActivity) (activity as AppCompatActivity).supportActionBar?.let { activityHasActionBar = true }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // create ContextThemeWrapper from MainActivity context with AppTheme style
        val contextThemeWrapper = ContextThemeWrapper(activity,R.style.AppTheme)
        // Inflate the layout for this fragment
        val localInflater = inflater.cloneInContext(contextThemeWrapper)

        val view = localInflater.inflate(R.layout.fragment_home_create_user, container, false)

        view.back_button.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_dest_createUserFragment_pop, null))
        view.button_create_user.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_global_home,null))

//        if (!activityHasActionBar) {
//            val act = (activity as AppCompatActivity)
//            act.setSupportActionBar(view.toolbar)
//            act.supportActionBar?.setTitle(R.string.title_fragment_create_user)
//            act.supportActionBar?.setDisplayHomeAsUpEnabled(true)
//            view.toolbar.visibility = ViewGroup.VISIBLE
//
//
//        } else view.toolbar.visibility = ViewGroup.GONE

        if (activityHasActionBar) view.mainToolbar.visibility = ViewGroup.GONE

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }

    }

    override fun onDetach() {
        super.onDetach()
        listener = null

    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment CreateUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            CreateUserFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
