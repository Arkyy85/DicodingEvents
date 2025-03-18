package com.ammar.core.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import com.ammar.core.databinding.ItemHorizEventBinding
import com.ammar.core.domain.model.Events
import com.bumptech.glide.Glide
import java.lang.ref.WeakReference

class EventsHorizAdapter : ListAdapter<Events, EventsHorizAdapter.EventsHorizViewHolder>(
    DIFF_CALLBACK
) {
    var onItemClick: ((Events) -> Unit)? = null
    private var weakContext: WeakReference<Context>? = null

    fun setContext(context: Context) {
        weakContext = WeakReference(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EventsHorizViewHolder(
            ItemHorizEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: EventsHorizViewHolder, position: Int) {
        if (position < 5) {
            val event = getItem(position)
            holder.bind(event)
        }
    }

    override fun getItemCount(): Int {
        return minOf(super.getItemCount(), 5)
    }

    inner class EventsHorizViewHolder(private val binding: ItemHorizEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Events) {
            binding.tvEventName.text = event.name
            weakContext?.get().let {
                context ->
                if (context != null) {
                    Glide.with(context)
                        .load(event.imageLogo)
                        .into(binding.imgEventPhoto)
                }
            }
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
