package com.truongkhanh.noteapp.service

import androidx.lifecycle.LiveData
import androidx.room.*
import com.truongkhanh.noteapp.model.Note
import io.reactivex.Completable

@Dao
interface NoteDao {
    @Query("Select * from note")
    fun getAllFullContent(): LiveData<List<Note>>

    @Query("Select * from note where id = :noteID")
    fun getNoteById(noteID: Int): LiveData<Note>

    @Update
    fun update(note: Note): Completable

    @Insert
    fun insert(note: Note): Completable

    @Delete
    suspend fun delete(note: Note)
}