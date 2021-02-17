package com.example.a8notes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a8notes.model.Note

@Database(entities = arrayOf(Note::class), version = 1)
abstract class NoteDB : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        private var instance: NoteDB? = null

        @Synchronized
        fun getInstance(context: Context): NoteDB? {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        NoteDB::class.java,
                        "note_db")
                        //.allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance
        }
    }
}