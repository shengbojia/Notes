package com.shengbojia.notes.ui.actionmode

import android.content.Context
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import com.shengbojia.notes.R
import com.shengbojia.notes.adapter.NoteAdapter
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.viewmodel.DeleteNoteViewModel
import kotlin.Exception

/**
 * [ActionMode.Callback] that handles the user's request to delete notes.
 */
class MainActionModeCallback(
    private val viewModel: DeleteNoteViewModel,
    private val startContext: AppCompatActivity
) : ActionMode.Callback {

    internal lateinit var adapter: NoteAdapter

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.menu_contexual, menu)

        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {

        // Essentially make the selected notes set empty
        resetDelete()

        // Notifies the adapter that action mode has been exited
        adapter.turnOffActionMode()
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.contextAction_delete -> {
                Log.d(TAG, "pressed delete")

                // TODO: Dialog for confirmation
                viewModel.deleteNotes()
                resetDelete()

                // Call onDestroyActionMode
                mode?.finish()
                true
            }
            else -> false
        }

    }

    /**
     * Starts the action mode from the activity context reference.
     */
    fun startActionMode(): ActionMode {
        return startContext.startSupportActionMode(this) ?: throw Exception("Action mode is null")
    }

    fun addNoteToBeDeleted(note: Note?) {
        viewModel.notesToDelete.add(note ?: throw Exception(""))
    }

    fun removeNoteToBeDeleted(note: Note?): Boolean {
        return viewModel.notesToDelete.remove(note)
    }

    private fun resetDelete() {
        viewModel.notesToDelete.clear()

    }


    companion object {
        private const val TAG = "CallbackActionMode"
    }
}

