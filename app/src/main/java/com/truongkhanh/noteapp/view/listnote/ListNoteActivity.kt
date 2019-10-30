package com.truongkhanh.noteapp.view.listnote

import android.os.Bundle
import com.truongkhanh.noteapp.R
import com.truongkhanh.noteapp.base.BaseNoAppBarActivity

class ListNoteActivity : BaseNoAppBarActivity() {
    private lateinit var listNoteFragment: ListNoteFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            listNoteFragment = ListNoteFragment.getInstance()
            if (intent != null)
                listNoteFragment.arguments = intent.extras
            replaceFragment(R.id.fragmentContainer, listNoteFragment)
        }
    }
}