package com.example.memorynotes.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.core.data.Note
import com.example.memorynotes.R
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList
import android.util.Log

class NotesListAdapter(
    private val notes: ArrayList<Note>,
    private val onNoteClick: (Note) -> Unit
) : RecyclerView.Adapter<NotesListAdapter.NoteViewHolder>() {

    fun updatesNotes(newNotes: List<Note>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
        NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        )

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int = notes.size

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val noteTitle: TextView = view.findViewById(R.id.title)
        private val noteContent: TextView = view.findViewById(R.id.content)
        private val noteDate: TextView = view.findViewById(R.id.date)

        fun bind(note: Note) {
            Log.d("NotesAdapter", "Binding note: title=${note.title}, content=${note.content}")
            
            noteTitle.text = note.title
            noteContent.text = note.content

            try {
                val instant = Instant.ofEpochMilli(note.updateTime)
                val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                noteDate.text = "Last updated: ${localDateTime.format(formatter)}"
            } catch (e: Exception) {
                noteDate.text = "Last updated: Unknown"
            }

            itemView.setOnClickListener {
                onNoteClick(note)
            }
        }
    }
}