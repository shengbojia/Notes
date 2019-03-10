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

    /**
     * Creates an on click listener that opens up a [EditNoteFragment] on click
     */
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

/**
 * [NoteDiffCallBack] is used to avoid the resource-consuming notifyDataSetChanged
 */
private class NoteDiffCallBack : DiffUtil.ItemCallback<Note>() {

    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}
