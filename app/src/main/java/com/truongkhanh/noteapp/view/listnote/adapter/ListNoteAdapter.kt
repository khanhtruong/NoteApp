package com.truongkhanh.noteapp.view.listnote.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.truongkhanh.noteapp.R
import com.truongkhanh.noteapp.model.Note

class ListNoteAdapter(private val itemClickListener: (Note) -> Unit) : ListAdapter<Note, ListNoteViewHolder>(Note.diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_note, parent, false)
        return ListNoteViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: ListNoteViewHolder, position: Int) {

    }

    override fun onBindViewHolder(
        holder: ListNoteViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val note = getItem(position)

        holder.data = note
        holder.title.text = note.title

        val content = note.content
        holder.content.text = content?.substring(content.indexOf("<p>") + 3, content.indexOf("</p>", 3))
    }

}