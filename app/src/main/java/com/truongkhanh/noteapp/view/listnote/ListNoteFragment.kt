package com.truongkhanh.noteapp.view.listnote

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.truongkhanh.noteapp.R
import com.truongkhanh.noteapp.base.BaseFragment
import com.truongkhanh.noteapp.model.Note
import com.truongkhanh.noteapp.util.BUNDLE_NOTE
import com.truongkhanh.noteapp.util.getEnableView
import com.truongkhanh.noteapp.view.editor.EditorActivity
import com.truongkhanh.noteapp.view.listnote.adapter.ListNoteAdapter
import kotlinx.android.synthetic.main.fragment_list_note.*

class ListNoteFragment : BaseFragment() {

    private lateinit var listNoteFragmentViewModel: ListNoteFragmentViewModel
    private var listNoteAdapter: ListNoteAdapter? = null

    companion object {
        fun getInstance() = ListNoteFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_note, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (listNoteAdapter != null)
            listNoteFragmentViewModel.getAllNote()
    }

    override fun setUpView(view: View, savedInstanceState: Bundle?) {
        bindingViewModel()
        initRecyclerView()
        initListener()
    }

    private fun bindingViewModel() {
        val activity = activity?:return
        listNoteFragmentViewModel = ViewModelProviders.of(activity, ListNoteFragmentViewModel.Factory(activity))
            .get(ListNoteFragmentViewModel::class.java)
        listNoteFragmentViewModel.listNote.observe(this, Observer {listNote ->
            rlEmpty.visibility = getEnableView(listNote.isEmpty())
            listNoteAdapter?.submitList(listNote)
        })
        listNoteFragmentViewModel.navigateToActivity.observe(this, Observer {event ->
            event.getContentIfNotHandled()?.let {data ->
                navigateToEditor(data)
            }
        })
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvListNote.layoutManager = layoutManager
        listNoteAdapter = ListNoteAdapter {data ->
            navigateToEditor(data)
        }
        rvListNote.adapter = listNoteAdapter
    }

    private fun initListener() {
        btnCreateNote.setOnClickListener {
            listNoteFragmentViewModel.create(Note(0, "Default title", "<p>Example content</p>"))
        }
    }

    private fun navigateToEditor(note: Note) {
        val bundle = Bundle()
        bundle.putParcelable(BUNDLE_NOTE, note)
        val intent = Intent(context, EditorActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

}