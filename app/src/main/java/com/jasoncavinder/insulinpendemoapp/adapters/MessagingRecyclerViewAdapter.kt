/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.database.entities.message.Message
import kotlinx.android.synthetic.main.fragment_message.view.*

class MessagingRecyclerViewAdapter(
    private val messageList: LiveData<List<Message>>//,
//    private val listener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MessagingRecyclerViewAdapter.ViewHolder>() {

//    private val onClickListener: View.OnClickListener

//    init {
//        onClickListener = View.OnClickListener { v ->
//            val message = v.tag as Message
    // Notify the active callbacks interface (the activity, if the fragment is attached to
    // one) that an item has been selected.
//            listener?.onListFragmentInteraction(message)
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList.value?.get(position)
        holder.mIdView.text = message?.messageId.toString()
        holder.mContentView.text = message?.content

        with(holder.view) {
            tag = message
//            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount(): Int = messageList.value?.size ?: 0

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val mIdView: TextView = view.item_number
        val mContentView: TextView = view.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
