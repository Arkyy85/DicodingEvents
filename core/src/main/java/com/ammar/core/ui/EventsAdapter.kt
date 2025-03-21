package com.ammar.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ammar.core.databinding.ItemEventBinding
import com.ammar.core.domain.model.Events
import com.bumptech.glide.Glide

class EventsAdapter : ListAdapter<Events, EventsAdapter.EventViewHolder>(
    DIFF_CALLBACK
) {
    var onItemClick: ((Events) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EventViewHolder(
            ItemEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    inner class EventViewHolder(private var binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Events) {
            binding.tvEventName.text = event.name
            Glide.with(itemView.context)
                .load(event.imageLogo)
                .into(binding.imgEventPhoto)
        }
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(bindingAdapterPosition))
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Events> =
            object : DiffUtil.ItemCallback<Events>() {
                override fun areItemsTheSame(oldItem: Events, newItem: Events): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Events, newItem: Events): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
