package com.example.memorynotes.framework.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao()
interface NoteDao {
    @Insert(onConflict = REPLACE) // If we have this note in db, replace it.
    suspend fun addNoteEntity(noteEntity: NoteEntity)

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteEntity(id: Long): NoteEntity?

    @Query("SELECT * FROM note")
    suspend fun getAllNoteEntities(): List<NoteEntity>

    @Delete
    suspend fun deleteNoteEntity(noteEntity: NoteEntity)
}