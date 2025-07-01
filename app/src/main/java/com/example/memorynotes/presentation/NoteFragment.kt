package com.example.memorynotes.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.core.data.Note
import com.example.memorynotes.R
import com.example.memorynotes.framework.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NoteFragment : Fragment() {

    private val viewModel: NoteViewModel by viewModels()
    private lateinit var checkButton: FloatingActionButton
    private lateinit var titleView: EditText
    private lateinit var contentView: EditText

    private var currentNote = Note("", "", 0L, 0L)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleView = view.findViewById(R.id.titleView)
        contentView = view.findViewById(R.id.contentView)
        checkButton = view.findViewById(R.id.checkButton)
        
        checkButton.setOnClickListener {
            if(titleView.text.toString() != "" || contentView.text.toString() != "") {
                val time: Long = System.currentTimeMillis()
                currentNote.title = titleView.text.toString()
                currentNote.content = contentView.text.toString()
                currentNote.updateTime = time
                if(currentNote.id == 0L) {
                    currentNote.creationTime = time
                }
                viewModel.saveNote(currentNote)
            } else {
                findNavController().popBackStack()
            }

            observeViewModel()
        }
    }

    private fun observeViewModel() {
        viewModel.saved.observe(viewLifecycleOwner, Observer {
            if(it) {
                Toast.makeText(context, "Done!", Toast.LENGTH_LONG).show()
                Navigation.findNavController(titleView).popBackStack()
            } else {
                Toast.makeText(context, "Something went wrong, please try again", Toast.LENGTH_LONG).show()
            }
        })
    }
}