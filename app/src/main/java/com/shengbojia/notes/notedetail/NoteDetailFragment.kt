package com.shengbojia.notes.notedetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.shengbojia.notes.EventObserver
import com.shengbojia.notes.R
import com.shengbojia.notes.databinding.FragmentNoteDetailBinding
import com.shengbojia.notes.utility.DELETE_RESULT_OK
import com.shengbojia.notes.utility.obtainViewModel
import com.shengbojia.notes.utility.setupSnackbar


class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false).apply {
            viewModel = obtainViewModel(NoteDetailViewModel::class.java)
        }

        binding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupFab()
        setupSnackbar()
        setupNavigation()

    }

    override fun onResume() {
        super.onResume()
        val noteId = arguments?.let {
            NoteDetailFragmentArgs.fromBundle(it).noteId
        }
        binding.viewModel?.start(noteId)
    }

    private fun setupNavigation() {
        binding.viewModel?.deleteNoteCommand?.observe(this, EventObserver {
            val direction = NoteDetailFragmentDirections
                .actionNoteDetailFragmentToNoteListFragment(DELETE_RESULT_OK)
            findNavController().navigate(direction)
        })
        binding.viewModel?.editNoteCommand?.observe(this, EventObserver {
            val noteId = NoteDetailFragmentArgs.fromBundle(arguments!!).noteId
            val direction = NoteDetailFragmentDirections
                .actionNoteDetailFragmentToAddEditNoteFragment(noteId, getString(R.string.fragment_title_editNote))
            findNavController().navigate(direction)
        })
    }

    private fun setupSnackbar() {
        binding.viewModel?.let {
            view?.setupSnackbar(this, it.snackbarText, Snackbar.LENGTH_SHORT)
        }
    }

    private fun setupFab() {
        binding.fabNoteDetailEdit.setOnClickListener {
            binding.viewModel?.editNote()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                binding.viewModel?.deleteNote()
                true
            }
            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
    }

}
