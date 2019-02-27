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
import com.shengbojia.notes.viewmodel.AddNoteViewModel
import kotlinx.android.synthetic.main.activity_add_note.*

class AddNoteActivity : AppCompatActivity() {
    private lateinit var addNoteViewModel: AddNoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        setSupportActionBar(toolbar_add)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Add note"

        numpicker_add_priority.minValue = 1
        numpicker_add_priority.maxValue = 5

        addNoteViewModel = ViewModelProviders.of(this).get(AddNoteViewModel::class.java)
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

        if (title.isBlank() && desc.isBlank()) {
            Toast.makeText(this, "Please enter a title and a description", Toast.LENGTH_SHORT).show()
            return
        }

        addNoteViewModel.insert(
            Note(
                title = title,
                description = desc,
                priority = priority
            )
        )
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
        NavUtils.navigateUpFromSameTask(this)
    }

    companion object {
        private const val TAG = "ActAddNote"
    }
}
