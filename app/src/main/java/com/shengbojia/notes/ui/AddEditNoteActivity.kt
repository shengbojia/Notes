package com.shengbojia.notes.ui

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.shengbojia.notes.R
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.adapter.NoteAdapter.Companion.EXTRA_DESC
import com.shengbojia.notes.adapter.NoteAdapter.Companion.EXTRA_ID
import com.shengbojia.notes.adapter.NoteAdapter.Companion.EXTRA_PRIORITY
import com.shengbojia.notes.adapter.NoteAdapter.Companion.EXTRA_TITLE
import com.shengbojia.notes.viewmodel.AddEditNoteViewModel
import kotlinx.android.synthetic.main.activity_add_note.*

class AddEditNoteActivity : AppCompatActivity() {
    private lateinit var addEditNoteViewModel: AddEditNoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        setSupportActionBar(toolbar_add)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        numpicker_add_priority.minValue = 1
        numpicker_add_priority.maxValue = 5

        if (intent.hasExtra(EXTRA_ID)) {

            title = getString(R.string.addEdit_editNote)

            intent.apply {
                et_add_title.setText(getStringExtra(EXTRA_TITLE))
                et_add_desc.setText(getStringExtra(EXTRA_DESC))
                numpicker_add_priority.value = getIntExtra(EXTRA_PRIORITY, 1)
            }

        } else {
            title = getString(R.string.addEdit_addNote)
        }

        addEditNoteViewModel = ViewModelProviders.of(this).get(AddEditNoteViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.save_note -> {
                saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        val title = et_add_title.text.toString()
        val desc = et_add_desc.text.toString()
        val priority = numpicker_add_priority.value

        Log.d(TAG, "$title, $desc, $priority")

        if (title.isBlank() || desc.isBlank()) {
            Toast
                .makeText(this, getString(R.string.toast_contentEmptyMessage), Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (intent.hasExtra(EXTRA_ID)) {
            val noteId = intent.getIntExtra(EXTRA_ID, -1)

            if (noteId == -1) {
                Toast
                    .makeText(this, getString(R.string.toast_idError), Toast.LENGTH_SHORT)
                    .show()
                return

            } else {
                addEditNoteViewModel.update(
                    Note(
                        noteId,
                        title,
                        desc,
                        priority
                    )
                )
            }

        } else {
            addEditNoteViewModel.insert(
                Note(
                    title = title,
                    description = desc,
                    priority = priority
                )
            )
        }

        Toast.makeText(this, getString(R.string.toast_savedSuccess), Toast.LENGTH_SHORT).show()
        NavUtils.navigateUpFromSameTask(this)
    }

    companion object {
        private const val TAG = "ActAddNote"
    }
}
