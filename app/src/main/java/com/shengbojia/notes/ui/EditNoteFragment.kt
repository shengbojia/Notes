package com.shengbojia.notes.ui


import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.shengbojia.notes.R
import com.shengbojia.notes.data.Note
import com.shengbojia.notes.databinding.FragmentEditNoteBinding
import com.shengbojia.notes.utility.InjectorUtils
import com.shengbojia.notes.viewmodel.EditNoteViewModel

/**
 * A simple [Fragment] subclass.
 *
 */
class EditNoteFragment : androidx.fragment.app.Fragment() {

    private lateinit var editNoteViewModel: EditNoteViewModel

    private val args: EditNoteFragmentArgs by navArgs()

    private lateinit var binding: FragmentEditNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val factory = InjectorUtils.provideEditNoteViewModelFactory(requireActivity(), args.noteId)
        editNoteViewModel = ViewModelProviders.of(this, factory).get(EditNoteViewModel::class.java)

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
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun saveEditedNote() {

        val title = binding.etAddTitle.text.toString()
        val desc = binding.etAddDesc.text.toString()
        val priority = binding.numpickerAddPriority.value

        if (title.isBlank() && desc.isBlank()) {
            return
        }

        editNoteViewModel.saveEditedNote(
            Note(
                id = args.noteId,
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
