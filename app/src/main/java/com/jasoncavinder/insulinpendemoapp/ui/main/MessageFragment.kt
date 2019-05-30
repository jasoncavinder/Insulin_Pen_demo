/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.adapters.MessagingRecyclerViewAdapter
import com.jasoncavinder.insulinpendemoapp.database.entities.message.MessageList
import com.jasoncavinder.insulinpendemoapp.utilities.DemoAction
import com.jasoncavinder.insulinpendemoapp.utilities.DemoActionListDialogFragment
import com.jasoncavinder.insulinpendemoapp.utilities.UpdateToolbarListener
import com.jasoncavinder.insulinpendemoapp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_message_list.view.*

class MessageFragment : Fragment(), DemoActionListDialogFragment.Listener {
    private val TAG by lazy { this::class.java.simpleName }

    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController

    private lateinit var updateToolbarListener: UpdateToolbarListener

    lateinit var messageList: MessageList

    private var columnCount = 1
//    private var listener: OnListFragmentInteractionListener? = null

    /* BEGIN: Required for Demo Actions */
    private var _demoActions: ArrayList<DemoAction> = arrayListOf(
        //TODO: Create any Demo Actions
    )

    // TODO: Add any Demo Action functions
    override fun onDemoActionClicked(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    /* END: Required for Demo Actions */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity())
            .get(MainViewModel::class.java)

        messageList = MessageList(viewModel.messages, viewModel.provider)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_message_list, container, false)

        messageList.observe(viewLifecycleOwner, Observer {})

        // Set the adapter
        if (view.message_list is RecyclerView) {
            with(view.message_list) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                        .apply {
                            reverseLayout = true
                        }
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MessagingRecyclerViewAdapter(messageList/*, listener*/)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateToolbarListener.updateToolbar(
            "Messaging",
            R.menu.menu_messaging_left,
            R.menu.menu_empty,
            mapOf(
                Pair(R.id.menu_item_home, R.id.action_messageFragment_pop)
            )
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            updateToolbarListener = context as UpdateToolbarListener
        } catch (castException: ClassCastException) {
            /** The activity does not implement the listener. */
            Log.d(TAG, "$context must implement UpdateToolbarListener")
        }

//        if (context is OnListFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
//        }
    }

//    override fun onDetach() {
//        super.onDetach()
//        listener = null
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
//    interface OnListFragmentInteractionListener {
//         TODO: Update argument type and name
//        fun onListFragmentInteraction(message: Message)
//    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            MessageFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
