package com.example.notes.db

import androidx.room.*
import com.example.notes.model.Note

@Dao
interface NoteDao {
    @Insert
    fun addNote (note: Note)

    @Update
    fun updateNote (note: Note)

    @Delete
    fun deleteNote (note: Note)

    @Query("DELETE FROM note")
    fun deleteAllNotes ()

    @Query("SELECT * FROM note")
    fun getAllNotes(): List<Note>

}