package com.truongkhanh.noteapp.view.listnote.adapter

import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.truongkhanh.noteapp.R
import com.truongkhanh.noteapp.model.Note
import java.util.regex.Pattern

class ListNoteAdapter(
    private val itemLongClickListener: (Pair<View, Note>) -> Unit,
    private val itemClickListener: (Note) -> Unit
) : ListAdapter<Note, ListNoteViewHolder>(Note.diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNoteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_note, parent, false)
        return ListNoteViewHolder(view, itemLongClickListener, itemClickListener)
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
        holder.content.text = formatContent(content)
    }

    private fun formatContent(content: String?): String? {
        val newContent = if (Build.VERSION.SDK_INT >= 24)
            Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY).toString()
        else
            Html.fromHtml(content).toString()
        return newContent.split("\n").getOrNull(0)
    }

}