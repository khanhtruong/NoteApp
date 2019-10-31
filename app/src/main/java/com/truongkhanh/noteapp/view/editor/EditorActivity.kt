package com.truongkhanh.noteapp.view.editor

import android.os.Bundle
import com.truongkhanh.noteapp.R
import com.truongkhanh.noteapp.base.BaseNoAppBarActivity

class EditorActivity : BaseNoAppBarActivity() {

    private lateinit var editorFragment: EditorFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            editorFragment = EditorFragment.getInstance()
            if (intent != null) {
                editorFragment.arguments = intent.extras
            }
            replaceFragment(R.id.fragmentContainer, editorFragment)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        editorFragment.getData()
    }
}
