package com.jasoncavinder.inpen.demo.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jasoncavinder.inpen.demo.R
import com.jasoncavinder.inpen.demo.utilities.UpdateToolbarListener

//import com.jasoncavinder.inpen.demo.onboarding.ui.UserViewModel


class HomeFragment : Fragment() {

//    private val viewModel: UserViewModel by activityViewModels()

    private lateinit var updateToolbarListener: UpdateToolbarListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        updateToolbarListener.updateToolbar(
            "At-a-Glance",
            R.menu.menu_home_left,
            R.menu.menu_home_right
        )

//
//        // setup toolbar
////        listener?.updateToolbar(
////            "At a Glance",
////            R.drawable.ic_account_circle_foreground_24dp,
////            R.id.action_homeFragment_to_profile,
////            R.drawable.ic_history_24dp,
////            R.id.action_homeFragment_to_doseHistory)
//
//        // Setup toggle listeners
////        view.icon_message_toggle.setOnClickListener {
////                toggleVisibility(
////                    group_message_display,
////                    R.drawable.more_unfold_24dp,
////                    R.drawable.less_unfold_24dp,
////                    it as ImageView )}
////        view.icon_report_toggle.setOnClickListener {
////                toggleVisibility(
////                    group_report_long,
////                    R.drawable.more_unfold_24dp,
////                    R.drawable.less_unfold_24dp,
////                    it as ImageView)
////                toggleVisibility(
////                    group_report_short,null,null,null) }
//
//

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            updateToolbarListener = context as UpdateToolbarListener
        } catch (castException: ClassCastException) {
            /** The activity does not implement the listener. */
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        Log.d("menu","$menu")
//        inflater.inflate(R.menu.menu_home_left, )
//        inflater.inflate(R.menu.menu_home_right, (activity as AppCompatActivity).toolbar.menu_right.menu)
//    }

    //    override fun onHiddenChanged(hidden: Boolean) {
//        Toast.makeText(activity, "Loaded 'HomeFragment'",Toast.LENGTH_LONG).show()
//        super.onHiddenChanged(hidden)
////        if (!hidden) listener?.updateToolbar("Dose History", R.drawable.ic_arrow_back_black_24dp,R.id.action_nav_graph_pop,null,null)
//    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is updateToolbarListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
//    }

//    interface updateToolbarListener {
//        fun updateToolbar(title: String, leftIcon: Int?, leftAction: Int?, rightIcon: Int?, rightAction: Int?)
//    }


    //    // TODO: Rename method, update argument and hook method into UI event
//    fun onButtonPressed(uri: Uri) {
//        listener?.onFragmentInteraction(uri)
//    }


//    fun toggleVisibility(group: Group, visibleSrc: Int?, goneSrc: Int?, toggle: ImageView?) {
//
//        when(group.visibility) {
//            View.VISIBLE -> {
//                group.visibility = View.GONE
//                toggle?.let {
//                    visibleSrc?.let {
//                        try { toggle.setImageResource(visibleSrc) }
//                        catch (e: Error) { } } } }
//            View.GONE -> {
//                group.visibility = View.VISIBLE
//                toggle?.let {
//                    goneSrc?.let {
//                        try { toggle.setImageResource(goneSrc) }
//                        catch (e: Error) { } } } }
//            else -> return
//        }
//    }

}