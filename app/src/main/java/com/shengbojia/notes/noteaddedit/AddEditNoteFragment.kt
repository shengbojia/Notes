package com.shengbojia.notes.noteaddedit


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.shengbojia.notes.EventObserver
import com.shengbojia.notes.R
import com.shengbojia.notes.databinding.FragmentAddEditNoteBinding
import com.shengbojia.notes.utility.ADD_EDIT_RESULT_OK
import com.shengbojia.notes.utility.obtainViewModel
import com.shengbojia.notes.utility.setupSnackbar

/**
 * A simple [Fragment] subclass.
 *
 */
class AddEditNoteFragment : Fragment() {

    private lateinit var binding: FragmentAddEditNoteBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddEditNoteBinding.inflate(inflater, container, false).apply {
            viewModel = obtainViewModel(AddEditViewModel::class.java)
        }

        // Set the fragment as the lifecycle owner
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupSnackbar()
        setupNavigation()
        populateUi()
    }

    private fun setupSnackbar() {
        binding.viewModel?.let {
            view?.setupSnackbar(this, it.snackBarText, Snackbar.LENGTH_SHORT)
        }
    }

    private fun setupNavigation() {
        binding.viewModel?.noteUpdatedEvent?.observe(this, EventObserver {
            val direction = AddEditNoteFragmentDirections
                .actionAddEditNoteFragmentToNoteListFragment(ADD_EDIT_RESULT_OK)
            findNavController().navigate(direction)
        })
    }

    private fun populateUi() {
        binding.viewModel?.start(getNoteId())
    }

    private fun getNoteId(): String? {
        return arguments?.let {
            AddEditNoteFragmentArgs.fromBundle(it).noteId
        }
    }
}
