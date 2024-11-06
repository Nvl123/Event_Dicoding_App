package com.dicoding.mysubmision1novil.data.ui

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mysubmision1novil.R
import com.dicoding.mysubmision1novil.data.response.ListEventsItem
import com.dicoding.mysubmision1novil.databinding.Event2Binding

class EventAdapter2 : ListAdapter<ListEventsItem, EventAdapter2.MyViewHolder>(CALL_BACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = Event2Binding.inflate(LayoutInflater.from(parent.context), parent, false )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = getItem(position)
        holder.updateView(post)

        holder.itemView.setOnClickListener{ view ->

            val context = view.context
            val intent  = Intent(context, DetailActivity::class.java)

            intent.putExtra("IdEvent", post.id.toString())
            context.startActivity(intent)
        }


    }


    class MyViewHolder(private val binding: Event2Binding) : RecyclerView.ViewHolder(binding.root){

        fun updateView(post : ListEventsItem){

            try {

                val context = binding.root.context

                binding.tvEventName.text = post.name
                binding.tvDescEvent2.text = post.summary

                Glide.with(binding.imgEvent2)
                    .load(post.mediaCover)
                    .error(R.drawable.error)
                    .into(binding.imgEvent2)
            }catch (e : Exception){

                Log.e("Adapter past Event", "Error Updating view : ${e.message}", e)

                val context = binding.root.context

                binding.tvEventName.text = context.getString(R.string.error_event_name)
                binding.tvDescEvent2.text = context.getString(R.string.error_event_summary)
                binding.imgEvent2.setImageResource(R.drawable.error)

            }



        }

    }

    companion object{
        val CALL_BACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem==newItem
            }

        }
    }



}