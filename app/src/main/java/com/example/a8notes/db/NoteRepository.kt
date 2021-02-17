package com.example.a8notes.db

import android.content.Context
import android.os.AsyncTask
import com.example.a8notes.model.Note

private lateinit var notes: List<Note>

class NoteRepository (context: Context) {

    val noteDao: NoteDao? = NoteDB.getInstance(context)?.noteDao()

    fun addNote(note: Note) = AddNoteAsyncTask(noteDao).execute(note)

    fun updateNote(note: Note) = UpdateNoteAsyncTask(noteDao).execute(note)

    fun deleteNote(note: Note) = DeleteNoteAsyncTask(noteDao).execute(note)

    fun deleteAllNotes() = DeleteAllNotesAsyncTask(noteDao).execute()

    fun getAllNotes() = GetAllNotesAsyncTask(noteDao).execute().get()

    private class AddNoteAsyncTask (val noteDao: NoteDao?) : AsyncTask<Note, Void, Void?>() {
        override fun doInBackground(vararg notes: Note?): Void? {

            if (notes.isNotEmpty())
                notes[0]?.let { noteDao?.addNote(it) }
            return null
        }
    }

    private class UpdateNoteAsyncTask (val noteDao: NoteDao?) : AsyncTask<Note, Void, Void?>() {
        override fun doInBackground(vararg notes: Note?): Void? {

            if (notes.isNotEmpty())
                notes[0]?.let { noteDao?.updateNote(it) }
            return null
        }
    }

    private class DeleteNoteAsyncTask (val noteDao: NoteDao?) : AsyncTask<Note, Void, Void?>() {
        override fun doInBackground(vararg notes: Note?): Void? {

            if (notes.isNotEmpty())
                notes[0]?.let { noteDao?.deleteNote(it) }
            return null
        }
    }

    private class GetAllNotesAsyncTask (val noteDao: NoteDao?) : AsyncTask<Void, Void, List<Note>?>() {

        override fun doInBackground(vararg p0: Void?): List<Note>? {
            return noteDao?.getAllNotes()
        }

    }

    private class DeleteAllNotesAsyncTask (val noteDao: NoteDao?) : AsyncTask<Void, Void, Void?>() {
        override fun doInBackground(vararg p0: Void?): Void? {

            noteDao?.deleteAllNotes()
            return null
        }
    }
}