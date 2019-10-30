package com.truongkhanh.noteapp.view.listnote.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.truongkhanh.noteapp.model.Note
import kotlinx.android.synthetic.main.item_list_note.view.*

class ListNoteViewHolder(view: View, itemClickListener: (Note) -> Unit) : RecyclerView.ViewHolder(view) {

    val title: TextView = view.tvTitle
    val content: TextView = view.tvContent

    var data: Note? = null

    init {
        view.setOnClickListener {
            data?.let {
                itemClickListener(it)
            }
        }
    }
}