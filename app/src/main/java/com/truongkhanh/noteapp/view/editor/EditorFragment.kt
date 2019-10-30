package com.truongkhanh.noteapp.view.editor

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.truongkhanh.noteapp.base.BaseFragment
import com.truongkhanh.noteapp.R
import com.truongkhanh.noteapp.util.BUNDLE_NOTE
import com.truongkhanh.noteapp.util.getEnableView
import com.truongkhanh.noteapp.util.removeUTFCharacters
import com.truongkhanh.noteapp.view.editor.WebViewInterface.WebViewInterface
import kotlinx.android.synthetic.main.fragment_editor.*


class EditorFragment : BaseFragment() {

    private lateinit var webViewInterface: WebViewInterface
    private lateinit var editorFragmentViewModel: EditorFragmentViewModel
    private val getValueCallback = ValueCallback<String> { value ->
        editorFragmentViewModel.content.postValue(
            value?.substring(
                1,
                value.length - 1
            )?.removeUTFCharacters()
        )
        editorFragmentViewModel.updateCurrentNote()
    }
    private val setValueCallback = ValueCallback<String> { Unit }
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            editorFragmentViewModel.title.postValue(
                s.toString()
            )
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    }

    companion object {
        fun getInstance() = EditorFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_editor, container, false)
    }

    override fun onStop() {
        super.onStop()
        getData()
    }

    override fun setUpView(view: View, savedInstanceState: Bundle?) {
        bindingViewModel()
        initWebView()
        setUpListener()
    }

    private fun bindingViewModel() {
        val activity = activity ?: return
        editorFragmentViewModel =
            ViewModelProviders.of(activity, EditorFragmentViewModel.Factory(activity))
                .get(EditorFragmentViewModel::class.java)

        editorFragmentViewModel.note.observe(this, Observer {note ->
            note.title?.let {
                etTitle.setText(it)
            }
            note.content?.let {
                setData(it)
            }
        })
        editorFragmentViewModel.navigateToActivity.observe(this, Observer {event ->
            event.getContentIfNotHandled()?.let{
                activity.finish()
            }
        })
    }

    private fun setUpListener() {
        btnBack.setOnClickListener {
            getData()
        }
        textChangeListener()
        webViewListener()
    }

    private fun webViewListener() {
        wvMain.webViewClient = object: WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                getNoteBundle()
                rlEmpty.visibility = getEnableView(false)
                clEditorContent.visibility = getEnableView(true)
            }
        }
    }

    private fun getNoteBundle() {
        editorFragmentViewModel.note.postValue(arguments?.getParcelable(BUNDLE_NOTE))
    }

    private fun textChangeListener() {
        etTitle.addTextChangedListener(textWatcher)
    }

    private fun getData() {
        val script = "(function() {return window.editor.getData();}) ();"
        wvMain.evaluateJavascript(script, getValueCallback)
    }

    private fun setData(content: String) {
        val script = "(function() {window.editor.setData('$content');}) ();"
        wvMain.evaluateJavascript(script, setValueCallback)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        wvMain.settings.javaScriptEnabled = true
        webViewInterface = WebViewInterface()
        wvMain.clearCache(true)
        wvMain.loadUrl("file:///android_asset/main.html")
        wvMain.addJavascriptInterface(webViewInterface, "MyInterface")
    }

}