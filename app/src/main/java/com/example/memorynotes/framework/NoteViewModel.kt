package com.example.memorynotes.framework

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.core.data.Note
import com.example.core.repository.NoteRepository
import com.example.core.usecase.AddNote
import com.example.core.usecase.GetAllNotes
import com.example.core.usecase.GetNote
import com.example.core.usecase.RemoveNote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val repository = NoteRepository(RoomNoteDataSource(application))

    val useCases = UseCases(
        AddNote(repository),
        GetAllNotes(repository),
        GetNote(repository),
        RemoveNote(repository)
    )

    private val _saved = MutableLiveData<Boolean>()
    val saved: LiveData<Boolean> = _saved

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveNote(note: Note) {
        coroutineScope.launch {
            try {
                useCases.addNote(note)
                _saved.postValue(true)
            } catch (e: Exception) {
                _saved.postValue(false)
            }
        }
    }

    fun loadNotes() {
        coroutineScope.launch {
            _isLoading.postValue(true)
            try {
                val notesList = useCases.getAllNotes()
                _notes.postValue(notesList)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getNote(id: Long) {
        coroutineScope.launch {
            try {
                val note = useCases.getNote(id)
                // Handle note
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun removeNote(note: Note) {
        coroutineScope.launch {
            try {
                useCases.removeNote(note)
                loadNotes() // Refresh list
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}