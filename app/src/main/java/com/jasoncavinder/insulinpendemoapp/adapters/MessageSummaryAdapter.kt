/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.ui.main.HomeFragment
import com.jasoncavinder.insulinpendemoapp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.content_message_summary.view.*

class MessageSummaryAdapter(
    private val listener: HomeFragment.OnMessageSummaryInteractionListener?,
    private val messageSummaryList: LiveData<List<MainViewModel.MessageSummary>>,
    val providers: LiveData<Map<String, String>>
) :
    RecyclerView.Adapter<MessageSummaryAdapter.MessageSummaryViewHolder>() {

    private val TAG by lazy { this::class.java.simpleName }

    private val onClickListener: View.OnClickListener = View.OnClickListener { listener?.onMessageSummaryInteraction() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageSummaryViewHolder {
        val messageSummaryView = LayoutInflater.from(parent.context)
            .inflate(R.layout.content_message_summary, parent, false) as RelativeLayout

        return MessageSummaryViewHolder(messageSummaryView)
    }

    override fun onBindViewHolder(holder: MessageSummaryViewHolder, position: Int) {
        holder.messageSummaryView.message_from.text =
            providers.value?.get(messageSummaryList.value?.get(position)?.from)
                ?: "Error loading providers"
        holder.messageSummaryView.message_content.text =
            messageSummaryList.value?.get(position)?.content
                ?: "Error loading message summary"
        holder.messageSummaryView.setOnClickListener(onClickListener)
    }

    override fun getItemCount(): Int = messageSummaryList.value?.size ?: 0

/*
    with(holder.view)
    {
        tag = message
            setOnClickListener(onClickListener)
    }
*/


    inner class MessageSummaryViewHolder(val messageSummaryView: RelativeLayout) :
        RecyclerView.ViewHolder(messageSummaryView)
}