package com.example.notes.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.model.Note
import kotlinx.android.synthetic.main.notes_list_item.view.*

class NoteAdapter (var notes:List<Note>?) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    var onItemClickListener:OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.notes_list_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount() = notes?.size?:0

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val res = holder.itemView.resources
        val priorities= res.getStringArray(R.array.priorities)
        val viewPriority = holder.viewPriority
        when (notes?.get(position)?.priority) {
            priorities[0]-> viewPriority.setBackgroundColor(Color.GREEN)
            priorities[1]-> viewPriority.setBackgroundColor(Color.RED)
            priorities[2]-> viewPriority.setBackgroundColor(Color.YELLOW)
        }

        holder.textViewTitle.text = notes?.get(position)?.title
        holder.textViewDate.text = res.getString(R.string.created) + notes?.get(position)?.date
    }

    fun swapList(notes: List<Note>?) {
        this.notes = notes
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val viewPriority = itemView.view_priority
        val textViewTitle = itemView.text_view_title
        val textViewDate = itemView.edit_text_date

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION)
                    onItemClickListener?.onItemClick(notes?.get(position)!!)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick (note: Note)
    }

}