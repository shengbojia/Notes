package com.shengbojia.notes.ui


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager


import com.shengbojia.notes.R
import com.shengbojia.notes.adapter.NoteAdapter
import com.shengbojia.notes.databinding.FragmentNoteListBinding
import com.shengbojia.notes.ui.actionmode.MainActionModeCallback
import com.shengbojia.notes.utility.InjectorUtils
import com.shengbojia.notes.viewmodel.DeleteNoteViewModel
import com.shengbojia.notes.viewmodel.NoteListViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * [NoteListFragment] displays saved notes in a [RecyclerView], and has a [FloatingActionButton] for
 * adding new notes.
 */
class NoteListFragment : Fragment() {

    private lateinit var viewModel: NoteListViewModel
    private lateinit var deleteViewModel: DeleteNoteViewModel
    private lateinit var binding: FragmentNoteListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = InjectorUtils.provideDeleteNoteViewModelFactory(requireContext())

        deleteViewModel = activity?.run {
            ViewModelProviders.of(this, factory)
                .get(DeleteNoteViewModel::class.java)
        } ?: throw Exception("Invalid activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNoteListBinding.inflate(inflater, container, false)

        val factory = InjectorUtils.provideNoteListViewModelFactory(requireContext())
        viewModel = ViewModelProviders.of(this, factory).get(NoteListViewModel::class.java)

        setHasOptionsMenu(true)

        binding.fabNoteListNewNote.setOnClickListener {
            val direction = NoteListFragmentDirections.actionNoteListFragmentToAddNoteFragment()
            it.findNavController().navigate(direction)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = InjectorUtils.provideAdapterWithActionMode(
            activity as AppCompatActivity,
            deleteViewModel
        )

        binding.recyclerViewNoteList.adapter = adapter

        // Registers an observer for the LiveData
        subscribeUi(adapter)
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
