/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.database.entities.message.MessageList
import kotlinx.android.synthetic.main.fragment_message_from.view.*


class MessagingRecyclerViewAdapter(
    private val messageList: MessageList
//    private val listener: OnListFragmentInteractionListener?,
) : RecyclerView.Adapter<MessagingRecyclerViewAdapter.ViewHolder>() {

    private val TAG by lazy { this::class.java.simpleName }
//    private val onClickListener: View.OnClickListener

    init {
        messageList.observeForever { notifyDataSetChanged() }
//        onClickListener = View.OnClickListener { v ->
//            val message = v.tag as Message
        // Notify the active callbacks interface (the activity, if the fragment is attached to
        // one) that an item has been selected.
//            listener?.onMessageSummaryInteraction(message)
//        }
    }

    private val SENT = 1
    private val RECEIVED = 2
    private val UNKNOWN = 0

    override fun getItemViewType(position: Int): Int {
        return when (messageList.value?.get(position)?.sent) {
            true -> SENT
            false -> RECEIVED
            else -> UNKNOWN
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).run {
            when (viewType) {
                SENT -> this.inflate(R.layout.fragment_message_to, parent, false) as LinearLayout
                RECEIVED -> this.inflate(R.layout.fragment_message_from, parent, false) as LinearLayout
                else -> throw Exception("sent is null")
            }
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.messageContentView.text = messageList.value?.get(position)?.content
        when (holder.itemViewType) {
            SENT -> messageList.userPhoto?.let { holder.senderImageView.apply { setImageResource(it) } }
            RECEIVED -> messageList.providerPhoto?.let { holder.senderImageView.apply { setImageResource(it) } }
        }

    }

    /*
         with(holder.view)
         {
             tag = message
    //            setOnClickListener(onClickListener)
         }
    */
//    override fun onViewAttachedToWindow(holder: MessageSummaryViewHolder) {
//        super.onViewAttachedToWindow(holder)
//        Log.d(TAG, "${messageSummaryList.value?.get(holder.adapterPosition)}")
//    }


    override fun getItemCount(): Int = messageList.value?.size ?: 0

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val messageContentView: TextView = view.message_content
        val senderImageView: ImageView = view.sender_image

        override fun toString(): String {
            return super.toString() + " '" + messageContentView.text + "'"
        }
    }
}
