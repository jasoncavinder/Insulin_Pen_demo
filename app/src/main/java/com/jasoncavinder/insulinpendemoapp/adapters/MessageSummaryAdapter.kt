/*
 * Copyright (c) 2019, Jason Cavinder <jason.cavinder@gmail.com>.
 * This project is licenced to the client of Upwork contract #21949291. It is not
 * licensed for public use. See the LICENSE.md file for details
 */

package com.jasoncavinder.insulinpendemoapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.jasoncavinder.insulinpendemoapp.R
import com.jasoncavinder.insulinpendemoapp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.content_message_summary.view.*

class MessageSummaryAdapter(
    private val messageSummaryList: LiveData<List<MainViewModel.MessageSummary>>,
    val providers: LiveData<Map<String, String>>
) :
    RecyclerView.Adapter<MessageSummaryAdapter.MessageSummaryViewHolder>() {

    class MessageSummaryViewHolder(val messageSummaryView: RelativeLayout) : RecyclerView.ViewHolder(messageSummaryView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageSummaryViewHolder {
        val messageSummaryView = LayoutInflater.from(parent.context)
            .inflate(R.layout.content_message_summary, parent, false) as RelativeLayout

        return MessageSummaryViewHolder(messageSummaryView)
    }


    override fun onBindViewHolder(holder: MessageSummaryViewHolder, position: Int) {
        holder.messageSummaryView.message_from.text =
            providers.value?.get(messageSummaryList.value?.get(position)?.from) ?: "Error Loading Providers"
        holder.messageSummaryView.message_content.text =
            messageSummaryList.value?.get(position)?.content
    }

    override fun getItemCount(): Int = messageSummaryList.value?.size ?: 0

//    fun updateMessageList(newList: List<MainViewModel.MessageSummary>) {
//        this.messageSummaryList.postValue(newList)
//        notifyDataSetChanged()
//    }
}