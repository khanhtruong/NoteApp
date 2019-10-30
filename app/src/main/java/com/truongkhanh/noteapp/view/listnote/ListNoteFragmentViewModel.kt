package com.truongkhanh.noteapp.view.listnote

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.truongkhanh.noteapp.model.Note
import com.truongkhanh.noteapp.repository.NoteRepository
import com.truongkhanh.noteapp.service.ApplicationDatabase
import com.truongkhanh.noteapp.util.DisposeBag
import com.truongkhanh.noteapp.util.Event
import com.truongkhanh.noteapp.util.disposedBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListNoteFragmentViewModel(private val context: Context) : ViewModel() {

    val navigateToActivity: LiveData<Event<Note>> get() = _navigateToActivity
    private val _navigateToActivity = MutableLiveData<Event<Note>>()
    private val noteRepository: NoteRepository
    var listNote: LiveData<List<Note>>
    private val bag = DisposeBag(context as LifecycleOwner)

    init {
        val noteDao = ApplicationDatabase.getDatabase(context).noteDao()
        noteRepository = NoteRepository(noteDao)
        listNote = noteRepository.listNote
    }

    fun getAllNote() {
        listNote = noteRepository.listNote
    }

    fun create(note: Note) {
        noteRepository.insert(note = note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("Debuggg", "Insert successfully")
                _navigateToActivity.value = Event(note)
            }
            .disposedBy(bag)
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ListNoteFragmentViewModel(context = context) as T
        }
    }
}