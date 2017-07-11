package io.luciferldy.zhihudailykotlin.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebView
import io.luciferldy.zhihudailykotlin.R

/**
 * Created by Lucifer on 2017/6/19.
 */

class DetailsActivity: AppCompatActivity() {

    companion object {
        val LOG_TAG: String = DetailsActivity::class.java.simpleName
        val STORY_URL: String = "http://daily.zhihu.com/story/"
    }

    private lateinit var  mWebView: WebView
    private var mStoryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate")
        setContentView(R.layout.activity_details)

        val toolbar: Toolbar = findViewById(R.id.details_toolbar) as Toolbar
        with(toolbar) {
            inflateMenu(R.menu.details_menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.open_in_browser -> {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(STORY_URL + mStoryId))
                        startActivity(intent)
                        true
                    }
                    else -> {
                        Log.d(LOG_TAG, "itemId ${item.itemId} can not match.")
                        false
                    }
                }
            }
            setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            setNavigationOnClickListener {
                onBackPressed()
            }
        }


        mWebView = findViewById(R.id.details_web) as WebView
        mWebView.setWebChromeClient(object: WebChromeClient() {

        })
        mWebView.settings.javaScriptEnabled = true

        when (intent.action) {
            Intent.ACTION_VIEW -> {
                Log.d(LOG_TAG, "onCreate ACTION_VIEW")
                val uri: Uri? = intent.data
                uri?.let {
                    mWebView.loadUrl(uri.toString())
                }
            }
            else -> {
                val bundle: Bundle? = intent.extras
                bundle?.let {
                    mStoryId = bundle.getString("id")
                    if (!TextUtils.isEmpty(mStoryId)) {
                        mWebView.loadUrl(STORY_URL + mStoryId)
                    } else {
                        Log.d(LOG_TAG, "story id is null.")
                    }
                }
            }
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(LOG_TAG, "onNewIntent")
        intent?.let {
            when (intent.action) {
                Intent.ACTION_VIEW -> {
                    Log.d(LOG_TAG, "onNewIntent ACTION_VIEW")
                    val uri: Uri? = intent.data
                    uri?.let {
                        mWebView.loadUrl(uri.toString())
                    }
                }
                else -> {
                    val bundle: Bundle? = intent.extras
                    bundle?.let {
                        mStoryId = bundle.getString("id")
                        Log.d(LOG_TAG, "onNewIntent id is $mStoryId")
                        if (!TextUtils.isEmpty(mStoryId)) {
                            mWebView.loadUrl(STORY_URL + mStoryId)
                        }
                    }
                }
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (mWebView.canGoBack()) {
                    mWebView.goBack()
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        mWebView.stopLoading()
    }
}