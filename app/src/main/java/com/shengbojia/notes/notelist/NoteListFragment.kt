package com.shengbojia.notes.notelist


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.shengbojia.notes.EventObserver
import com.shengbojia.notes.R
import com.shengbojia.notes.databinding.FragmentNoteListBinding
import com.shengbojia.notes.utility.obtainViewModel
import com.shengbojia.notes.utility.setupSnackbar

/**
 * [NoteListFragment] displays saved notes in a [RecyclerView], and has a [FloatingActionButton] for
 * adding new notes.
 */
class NoteListFragment : Fragment() {

    //private lateinit var viewModel: NoteListViewModel
    private lateinit var binding: FragmentNoteListBinding
    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNoteListBinding.inflate(inflater, container, false).apply {
            viewModel = obtainViewModel(NoteListViewModel::class.java)
        }

        setHasOptionsMenu(true)

        // Set the fragment as the lifecycle owner
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupSnackbar()
        setupAdapter()
        setupNavigation()
        setupFab()

        binding.viewModel?.getAllNotes()
    }

    private fun setupSnackbar() {
        binding.viewModel?.let {
            view?.setupSnackbar(this, it.snackBarText, Snackbar.LENGTH_SHORT)
        }
        arguments?.let {
            val message = NoteListFragmentArgs.fromBundle(it).userMessage
            binding.viewModel?.showEditResultMessage(message)
        }
    }

    private fun setupFab() {
        binding.fabNoteListNewNote.setOnClickListener {
            navigateToAddNewNote()
        }
    }

    private fun setupAdapter() {
        val viewModel = binding.viewModel
        if (viewModel != null) {
            adapter = NoteAdapter(viewModel)
            binding.recyclerViewNoteList.adapter = adapter
        } else {
            Log.d(TAG, "Viewmodel null when trying to setup adapter")
        }
    }

    private fun setupNavigation() {
        binding.viewModel?.openNoteEvent?.observe(this, EventObserver {
            navigateToExistingNote(it)
        })

        binding.viewModel?.newNoteEvent?.observe(this, EventObserver {
            navigateToAddNewNote()
        })
    }

    private fun navigateToAddNewNote() {
        val direction =
            NoteListFragmentDirections.actionNoteListFragmentToAddEditNoteFragment(
                null,
                getString(R.string.fragment_title_addNote)
            )
        findNavController().navigate(direction)
    }

    private fun navigateToExistingNote(noteId: String) {
        val direction =
            NoteListFragmentDirections.actionNoteListFragmentToNoteDetailFragment(noteId)
        findNavController().navigate(direction)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_deleteAll -> {
                binding.viewModel?.deleteAllNotes()
                Log.d(TAG, "Deleted all")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    companion object {
        private const val TAG = "FragNoteList"
    }

}
