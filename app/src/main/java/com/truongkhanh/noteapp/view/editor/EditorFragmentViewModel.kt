package com.truongkhanh.noteapp.view.editor

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

class EditorFragmentViewModel(context: Context) : ViewModel() {

    private var noteRepository: NoteRepository
    var content = MutableLiveData<String>().apply { postValue("") }
    var title = MutableLiveData<String>().apply { postValue("") }
    var note = MutableLiveData<Note>()
    val navigateToActivity: LiveData<Event<Boolean>> get() = _navigateToActivity
    private val _navigateToActivity = MutableLiveData<Event<Boolean>>()

    private val bag = DisposeBag(context as LifecycleOwner)

    init {
        val noteDao = ApplicationDatabase.getDatabase(context).noteDao()
        noteRepository = NoteRepository(noteDao)

        content.observe(context as LifecycleOwner, Observer {
            this.note.value?.content = it
        })
        title.observe(context as LifecycleOwner, Observer {
            this.note.value?.title = it
        })
    }

    private fun update(note: Note){
        noteRepository.update(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _navigateToActivity.value = Event(true)
            }.disposedBy(bag)
    }

    fun updateCurrentNote() {
        note.value?.let{
            update(it)
        }
    }

    class Factory(
        private val context: Context
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EditorFragmentViewModel(context = context) as T
        }
    }
}