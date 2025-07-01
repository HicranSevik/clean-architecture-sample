package com.example.memorynotes.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memorynotes.R
import com.example.memorynotes.framework.ListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.util.Log

class ListFragment : Fragment() {

    private val viewModel: ListViewModel by viewModels()
    private lateinit var notesListView: RecyclerView
    private lateinit var addNote: FloatingActionButton
    private lateinit var loadingView: ProgressBar
    private lateinit var notesListAdapter: NotesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notesListView = view.findViewById(R.id.noteListView)
        addNote = view.findViewById(R.id.addNote)
        loadingView = view.findViewById(R.id.loadingView)

        notesListAdapter = NotesListAdapter(
            ArrayList(),
            { note ->
                //todo
            }
        )

        notesListView.apply{
            layoutManager = LinearLayoutManager(context)
            adapter = notesListAdapter
        }

        addNote.setOnClickListener { goToNoteDetails() }
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getNotes()
    }

    private fun observeViewModel() {
        viewModel.notes.observe(viewLifecycleOwner, Observer { noteList ->
            loadingView.visibility = View.GONE
            notesListView.visibility = View.VISIBLE
            notesListAdapter.updatesNotes(noteList.sortedByDescending { it.updateTime })
        })
    }

    private fun goToNoteDetails(id: Long = 0L) {
        val action = ListFragmentDirections.actionGoToNote(id)
        findNavController().navigate(action)
    }
}