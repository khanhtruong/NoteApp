package com.truongkhanh.noteapp.view.editor

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.webkit.WebViewAssetLoader
import com.truongkhanh.noteapp.base.BaseFragment
import com.truongkhanh.noteapp.R
import com.truongkhanh.noteapp.util.BUNDLE_NOTE
import com.truongkhanh.noteapp.util.getEnableView
import com.truongkhanh.noteapp.util.removeUTFCharacters
import com.truongkhanh.noteapp.view.editor.WebViewInterface.WebViewInterface
import kotlinx.android.synthetic.main.fragment_editor.*
import android.net.Uri
import android.webkit.*


class EditorFragment : BaseFragment(), WebViewInterface.InteractionListener {

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
        webViewListener()
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
    }

    private fun webViewListener() {
        wvMain.clearCache(true)
        wvMain.webViewClient = object: WebViewClient(){
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                val assetLoader = WebViewAssetLoader.Builder()
                    .setDomain("truong.khanh.com")
                    .addPathHandler("/truongkhanh/assets/", WebViewAssetLoader.AssetsPathHandler(view.context))
                    .build()
                return assetLoader.shouldInterceptRequest(request.url)
            }
        }
    }

    private fun getNoteBundle() {
        editorFragmentViewModel.note.postValue(arguments?.getParcelable(BUNDLE_NOTE))
    }

    private fun textChangeListener() {
        etTitle.addTextChangedListener(textWatcher)
    }

    fun getData() {
        val script = "(function() {return window.editor.getData();}) ();"
        wvMain.evaluateJavascript(script, getValueCallback)
    }

    private fun setData(content: String) {
        val script = "(function() {window.editor.setData('$content');}) ();"
        wvMain.evaluateJavascript(script, null)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
//        wvMain.settings.allowUniversalAccessFromFileURLs = false
//        wvMain.settings.allowFileAccess = false
//        wvMain.settings.allowContentAccess = false
//        wvMain.settings.allowFileAccessFromFileURLs = false
        wvMain.settings.javaScriptEnabled = true
        webViewInterface = WebViewInterface(this)
        wvMain.addJavascriptInterface(webViewInterface, "MyInterface")

        val uri = Uri.Builder()
            .scheme("https")
            .authority("truong.khanh.com")
            .appendPath("truongkhanh")
            .appendPath("assets")
            .appendPath("www")
            .appendPath("main.html")
            .build()
        wvMain.loadUrl(uri.toString())
    }

    override fun editorFinishLoading() {
        activity?.runOnUiThread {
            getNoteBundle()
            rlEmpty.visibility = getEnableView(false)
        }
    }
}