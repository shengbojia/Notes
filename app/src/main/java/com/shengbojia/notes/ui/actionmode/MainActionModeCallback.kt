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
import com.shengbojia.notes.viewmodel.DeleteNoteViewModel
import com.shengbojia.notes.viewmodel.NoteListViewModel


class MainActionModeCallback(
    private val viewModel: DeleteNoteViewModel,
    private val startContext: AppCompatActivity
) : ActionMode.Callback {

    internal lateinit var adapter: NoteAdapter

    private var multiselect = false

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.menu_contexual, menu)

        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        adapter.turnOffActionMode()
        adapter.notifyDataSetChanged()
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.contextAction_delete -> {
                Log.d(TAG, "pressed delete")
                mode?.finish()
                true
            }
            else -> false
        }

    }

    private fun handleDelete() {

    }

    fun startActionMode(view: View) {
        startContext.startSupportActionMode(this)
    }


    companion object {
        private const val TAG = "CallbackActionMode"
    }
}