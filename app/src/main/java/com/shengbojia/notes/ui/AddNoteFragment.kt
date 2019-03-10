package com.shengbojia.notes.ui


import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.NavUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.shengbojia.notes.R
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.databinding.FragmentAddNoteBinding
import com.shengbojia.notes.utility.InjectorUtils
import com.shengbojia.notes.viewmodel.AddNoteViewModel

/**
 * [Fragment] for writing and saving a new note.
 *
 */
class AddNoteFragment : Fragment() {
    private lateinit var addNoteViewModel: AddNoteViewModel
    private lateinit var binding: FragmentAddNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = InjectorUtils.provideAddNoteViewModelFactory(requireContext())
        addNoteViewModel = ViewModelProviders.of(this, factory)
            .get(AddNoteViewModel::class.java)

        binding = FragmentAddNoteBinding.inflate(inflater, container, false)

        // Set up number picker
        binding.numpickerAddPriority.minValue = 1
        binding.numpickerAddPriority.maxValue = 5

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.save_note -> {
                saveNewNote()
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    /**
     * Calls the repository to insert a new note into the database.
     */
    private fun saveNewNote() {
        val title = binding.etAddTitle.text.toString()
        val desc = binding.etAddDesc.text.toString()
        val priority = binding.numpickerAddPriority.value

        if (title.isBlank() && desc.isBlank()) {
            return
        }

        addNoteViewModel.saveNewNote(
            Note(
                title = title,
                description = desc,
                priority = priority
            )
        )

        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
        Toast.makeText(activity, getString(R.string.toast_savedSuccess), Toast.LENGTH_SHORT).show()
    }


}
