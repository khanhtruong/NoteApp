package com.truongkhanh.noteapp.repository

import androidx.lifecycle.LiveData
import com.truongkhanh.noteapp.model.Note
import com.truongkhanh.noteapp.service.NoteDao
import io.reactivex.Completable
import io.reactivex.Single

class NoteRepository (private val noteDao: NoteDao) {
    val listNote: LiveData<List<Note>> = noteDao.getAllFullContent()

    fun currentNote(noteId: Int): LiveData<Note> = noteDao.getNoteById(noteId)

    fun insert(note: Note): Completable = noteDao.insert(note)

    fun update(note: Note): Completable = noteDao.update(note)

    fun delete(note: Note): Completable = noteDao.delete(note)
}