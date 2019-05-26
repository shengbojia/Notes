package com.shengbojia.notes.notelist

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ActionMode
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.databinding.ItemNotesBinding
import com.shengbojia.notes.ui.NoteListFragmentDirections
import com.shengbojia.notes.viewmodel.NoteListViewModel

/**
 * Adapter for the [RecyclerView] in [MainActivity]
 */
class NoteAdapter(
    private val noteListViewModel: NoteListViewModel
) : ListAdapter<Note, NoteAdapter.NoteHolder>(NoteDiffCallBack()) {

    private var inActionMode = false
    private lateinit var actionMode: ActionMode

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder =
        NoteHolder(
            ItemNotesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val note = getItem(position)
        note.let {
            with(holder) {
                this.itemView.tag = it
                makeCheckboxVisible(inActionMode)
                if (inActionMode) {
                    bind(actionModeClickListener(this), createOnLongClickListener(this), it)
                } else {
                    bind(createOnClickListener(it.id), createOnLongClickListener(this), it)
                }
            }
        }
    }

    /**
     * Creates an on click listener that opens up a [EditNoteFragment] on click
     */
    private fun createOnClickListener(noteId: String): View.OnClickListener {
        if (noteId != null) {
            return View.OnClickListener {
                Log.d(TAG, "You pressed note $noteId")
                val direction = NoteListFragmentDirections.actionNoteListFragmentToEditNoteFragment(noteId)
                it.findNavController().navigate(direction)
            }
        } else throw IllegalStateException("Current note not found")
    }

    private fun actionModeClickListener(holder: NoteHolder) = View.OnClickListener {
        Log.d(TAG, "Action click")

        holder.changeCheckBox(actionModeCallback)
    }

    private fun createOnLongClickListener(holder: NoteHolder): View.OnLongClickListener {

        return View.OnLongClickListener {
            Log.d(TAG, "Long pressed")

            holder.updateOnClick(actionModeClickListener(holder))
            actionMode = actionModeCallback.startActionMode()
            inActionMode = true
            notifyItemRangeChanged(0, itemCount)
            holder.changeCheckBox(actionModeCallback)
            true
        }
    }

    internal fun turnOffActionMode() {
        inActionMode = false
        notifyItemRangeChanged(0, itemCount)
    }


    class NoteHolder(
        private val binding: ItemNotesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, longListener: View.OnLongClickListener, item: Note) {
            binding.apply {
                clickListener = listener
                longClickListener = longListener
                note = item
                executePendingBindings()
            }
        }

        fun updateOnClick(listener: View.OnClickListener) {
            binding.clickListener = listener
            binding.executePendingBindings()
        }

        fun changeCheckBox(actionModeCallback: MainActionModeCallback) {

            binding.apply {
                if (checkboxNoteSelector.isChecked) {

                    if (!actionModeCallback.removeNoteToBeDeleted(note)) {
                        Log.d(TAG, "Nothing got removed from the set of selected, wack")
                    }
                    checkboxNoteSelector.isChecked = false

                } else {
                    actionModeCallback.addNoteToBeDeleted(note ?: throw Exception("note not yet bound when doing checkbox"))

                    checkboxNoteSelector.isChecked = true
                }
            }
        }

        fun makeCheckboxVisible(shouldBeVisible: Boolean) {
            if (shouldBeVisible) {
                binding.checkboxNoteSelector.visibility = View.VISIBLE

            } else if (!shouldBeVisible) {
                binding.checkboxNoteSelector.isChecked = false
                binding.checkboxNoteSelector.visibility = View.GONE
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

