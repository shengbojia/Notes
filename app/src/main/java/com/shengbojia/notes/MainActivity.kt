package com.shengbojia.notes

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.shengbojia.notes.ui.AddNoteActivity
import com.shengbojia.notes.ui.NoteAdapter
import com.shengbojia.notes.viewmodel.NoteViewModel

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)

        recyclerView_main_noteList.layoutManager = LinearLayoutManager(this)
        recyclerView_main_noteList.setHasFixedSize(true)

        val adapter = NoteAdapter(this)
        recyclerView_main_noteList.adapter = adapter

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        noteViewModel.getAllNotes().observe(this, Observer { notes ->
            notes?.let { adapter.setNotes(it) }
        })

        fab.setOnClickListener {
            onFabClick(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onFabClick(view: View) {
        val addIntent = Intent(this, AddNoteActivity::class.java)
        startActivity(addIntent)
    }

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_NOTE_REQ
            && resultCode == Activity.RESULT_OK
            && data != null
        ) {
            val title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE)
            val desc = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION)
            val priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 0)

            Log.d(TAG, "$title, $desc, $priority")
        }
    }
    */
    companion object {
        const val ADD_NOTE_REQ = 1

        private const val TAG = "ActMain"
    }
}
