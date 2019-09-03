package com.shengbojia.notes.notelist

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.databinding.ItemNotesBinding
import timber.log.Timber

/**
 * Adapter for the [RecyclerView] in [MainActivity]
 */
class NoteAdapter(
    private val noteListViewModel: NoteListViewModel
) : ListAdapter<Note, NoteAdapter.NoteHolder>(NoteDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder =
        NoteHolder(
            ItemNotesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val note = getItem(position)
        holder.apply {

            bind(createUserActionListener(), note)
            itemView.tag = note
            /*
            {
                this.itemView.tag = it
                makeCheckboxVisible(inActionMode)
                if (inActionMode) {
                    bind(actionModeClickListener(this), createOnLongClickListener(this), it)
                } else {
                    bind(createOnClickListener(it.id), createOnLongClickListener(this), it)
                }
            }
            */
        }
    }

    private fun createUserActionListener(): NoteItemUserActionsListener =
        object : NoteItemUserActionsListener {

            override fun onNoteClicked(note: Note) {
                noteListViewModel.openNote(note.id)
            }

            override fun onNoteLongClicked(note: Note): Boolean {
                Timber.d("Long presser listener")
                return true
            }
        }




    class NoteHolder(
        private val binding: ItemNotesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(userActionListener: NoteItemUserActionsListener, item: Note) {

            binding.apply {
                listener = userActionListener
                note = item
                executePendingBindings()
            }
        }
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

