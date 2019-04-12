package com.shengbojia.notes.ui


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs

import com.shengbojia.notes.R
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.databinding.FragmentEditNoteBinding
import com.shengbojia.notes.utility.InjectorUtils
import com.shengbojia.notes.utility.ViewUtils
import com.shengbojia.notes.viewmodel.EditNoteViewModel

/**
 * [Fragment] for editing an existing note.
 *
 */
class EditNoteFragment : Fragment() {

    private lateinit var editNoteViewModel: EditNoteViewModel

    private val args: EditNoteFragmentArgs by navArgs()

    private lateinit var binding: FragmentEditNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val factory = InjectorUtils.provideEditNoteViewModelFactory(requireActivity(), args.noteId)
        editNoteViewModel = ViewModelProviders.of(this, factory)
            .get(EditNoteViewModel::class.java)

        binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        binding.apply {
            viewModel = editNoteViewModel
            lifecycleOwner = this@EditNoteFragment
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.save_note -> {
                saveEditedNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    /**
     * Calls the repository to update the edited note in the database.
     */
    private fun saveEditedNote() {

        val title = binding.etEditTitle.text.toString()
        val desc = binding.etEditDesc.text.toString()

        if (title.isBlank() && desc.isBlank()) {
            return
        }

        editNoteViewModel.saveEditedNote(
            Note(
                id = args.noteId,
                title = title,
                description = desc
            )
        )


        ViewUtils.hideSoftKeyboard(activity, view)

        Toast.makeText(activity, getString(R.string.toast_savedSuccess), Toast.LENGTH_SHORT).show()

        ViewUtils.exitFocus(binding.etEditDummy)
    }

}
