package com.shengbojia.notes.adapter

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.shengbojia.notes.R
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.databinding.ItemNotesBinding
import com.shengbojia.notes.ui.NoteListFragmentDirections
import kotlinx.android.synthetic.main.item_notes.view.*

/**
 * Adapter for the [RecyclerView] in [MainActivity]
 */

class NoteAdapter : ListAdapter<Note, NoteAdapter.NoteHolder>(NoteDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder =
        NoteHolder(ItemNotesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val note = getItem(position)
        note.let {
            with(holder) {
                this.itemView.tag = it
                bind(createOnClickListener(it.id), it)
            }
        }
    }

    private fun createOnClickListener(noteId: Int?): View.OnClickListener {
        if (noteId != null) {
            return View.OnClickListener {
                Log.d(TAG, "You pressed note $noteId")
                val direction = NoteListFragmentDirections.actionNoteListFragmentToEditNoteFragment(noteId)
                it.findNavController().navigate(direction)
            }
        } else throw IllegalStateException("Current note not found")
    }

    class NoteHolder(
        private val binding: ItemNotesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: Note) {
            binding.apply {
                clickListener = listener
                note = item
                executePendingBindings()
            }
        }
    }

    companion object {
        private const val TAG = "AdapterNote"
    }
}

private class NoteDiffCallBack : DiffUtil.ItemCallback<Note>() {

    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}
/*
class NoteAdapter internal constructor(
    private val context: Context
): androidx.recyclerview.widget.RecyclerView.Adapter<NoteAdapter.NoteHolder>() {

    //private val inflater = LayoutInflater.from(context)
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

    inner class NoteHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal val titleTv = itemView.tv_note_title
        internal val descriptionTv = itemView.tv_note_desc
        internal val priorityTv = itemView.tv_note_priority

        init {
            itemView.setOnClickListener(this)
        }

        // TODO:  Also make a onLongClick, with invisible checkbox that comes up and allows for deletion

        override fun onClick(v: View?) {

            Log.d(TAG, "onClick worked")

            if (adapterPosition != androidx.recyclerview.widget.RecyclerView.NO_POSITION) {

                Log.d(TAG, "at position: $adapterPosition")

                val noteClicked = notes[adapterPosition]
                startEditActivity(noteClicked)
            }
        }

        /**
         * Creates and calls an intent from the context that is passed in adapter's constructor to a new add edit
         * note activity.
         */
        private fun startEditActivity(note: Note) {
            val editIntent = Intent(context, AddEditNoteActivity::class.java)

            editIntent.putExtra(EXTRA_ID, note.id)
            editIntent.putExtra(EXTRA_TITLE, note.title)
            editIntent.putExtra(EXTRA_DESC, note.description)
            editIntent.putExtra(EXTRA_PRIORITY, note.priority)

            context.startActivity(editIntent)
        }
    }

    companion object {
        internal const val EXTRA_ID = "com.shengbojia.notes.ui.EXTRA_ID"
        internal const val EXTRA_TITLE = "com.shengbojia.notes.ui.EXTRA_TITLE"
        internal const val EXTRA_DESC = "com.shengbojia.notes.ui.EXTRA_DESC"
        internal const val EXTRA_PRIORITY = "com.shengbojia.notes.ui.EXTRA_PRIORITY"

        private const val TAG = "AdapterNote"
    }

}

        */