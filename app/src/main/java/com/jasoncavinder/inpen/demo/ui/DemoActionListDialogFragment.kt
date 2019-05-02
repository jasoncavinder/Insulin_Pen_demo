package com.jasoncavinder.inpen.demo.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jasoncavinder.inpen.demo.R
import kotlinx.android.synthetic.main.fragment_demoaction_list_dialog.*
import kotlinx.android.synthetic.main.fragment_demoaction_list_dialog_item.view.*

const val ARG_DEMO_ACTIONS = "demo_actions"

/**
 *
 * A fragment that shows a demoActionList of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    DemoActionListDialogFragment.newInstance(demoActions.size).show(childFragmentManager, "dialog")
 * </pre>
 *
 * You activity (or fragment) needs to implement [DemoActionListDialogFragment.Listener].
 */
class DemoActionListDialogFragment : BottomSheetDialogFragment() {
    private var mListener: Listener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_demoaction_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        demoActionList.setHasFixedSize(true)
        demoActionList.layoutManager = LinearLayoutManager(context)
        demoActionList.adapter = DemoActionAdapter(
            arguments?.getStringArrayList(ARG_DEMO_ACTIONS) ?: ArrayList()
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        if (parent != null) {
            mListener = parent as Listener
        } else {
            mListener = context as Listener
        }
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    interface Listener {
        fun onDemoActionClicked(position: Int)
    }

    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_demoaction_list_dialog_item, parent, false)) {

        internal val textDemoAction: TextView = itemView.text_demo_action

        init {
            textDemoAction.setOnClickListener {
                mListener?.let {
                    it.onDemoActionClicked(adapterPosition)
                    dismiss()
                }
            }
        }
    }

    private inner class DemoActionAdapter internal constructor(
        private val demoActions: ArrayList<String>
    ) :
        RecyclerView.Adapter<ViewHolder>() {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.textDemoAction.text = demoActions[position]
        }

        override fun getItemCount(): Int {
            return demoActions.size
        }
    }

    companion object {

        fun newInstance(demoActions: ArrayList<DemoAction>): DemoActionListDialogFragment =
            DemoActionListDialogFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(ARG_DEMO_ACTIONS, demoActions.textOnly())
                }
            }


    }
}

data class DemoAction(val text: String, val action: () -> Unit)

fun ArrayList<DemoAction>.textOnly(): ArrayList<String> {
    val texts: ArrayList<String> = ArrayList()
    for (demoAction in this) {
        texts.add(demoAction.text)
    }
    return texts
}













