package com.truongkhanh.noteapp.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.truongkhanh.noteapp.model.Note

@Database(entities = [Note::class], version = 1)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ApplicationDatabase? = null

        fun getDatabase(context: Context): ApplicationDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ApplicationDatabase::class.java,
                    "note_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}