package com.shengbojia.notes.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shengbojia.notes.R
import com.shengbojia.notes.data.Note
import kotlinx.android.synthetic.main.item_notes.view.*

class NoteAdapter internal constructor(
    context: Context
): RecyclerView.Adapter<NoteAdapter.NoteHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var notes = listOf<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView = inflater.inflate(R.layout.item_notes, parent, false)

        return NoteHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val currentNote = notes[position]
        holder.titleTv.text = currentNote.title
        holder.descriptionTv.text = currentNote.description
        holder.priorityTv.text = currentNote.priority.toString()
    }

    override fun getItemCount() = notes.size

    fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    inner class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val titleTv = itemView.tv_note_title
        internal val descriptionTv = itemView.tv_note_desc
        internal val priorityTv = itemView.tv_note_priority
    }



}