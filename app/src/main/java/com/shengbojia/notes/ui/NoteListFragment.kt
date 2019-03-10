package com.shengbojia.notes.ui


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager


import com.shengbojia.notes.R
import com.shengbojia.notes.adapter.NoteAdapter
import com.shengbojia.notes.databinding.FragmentNoteListBinding
import com.shengbojia.notes.utility.InjectorUtils
import com.shengbojia.notes.viewmodel.NoteListViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 *
 */
class NoteListFragment : Fragment() {

    private lateinit var viewModel: NoteListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNoteListBinding.inflate(inflater, container, false)
        val context = context ?: return binding.root

        val factory = InjectorUtils.provideNoteListViewModelFactory(requireContext())
        viewModel = ViewModelProviders.of(this, factory).get(NoteListViewModel::class.java)

        val adapter = NoteAdapter()
        binding.recyclerViewNoteList.adapter = adapter

        subscribeUi(adapter)

        setHasOptionsMenu(true)

        binding.fabNoteListNewNote.setOnClickListener {
            val direction = NoteListFragmentDirections.actionNoteListFragmentToAddNoteFragment()
            it.findNavController().navigate(direction)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_deleteAll -> {
                viewModel.deleteAllNotes()
                Log.d(TAG, "Deleted all")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun subscribeUi(adapter: NoteAdapter) {
        viewModel.getAllNotes().observe(viewLifecycleOwner, Observer { notes ->
            if (notes != null) adapter.submitList(notes)
        })
    }

    companion object {
        private const val TAG = "FragNoteList"
    }

}
