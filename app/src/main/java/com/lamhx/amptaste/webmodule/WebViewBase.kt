package com.lamhx.amptaste.webmodule

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.core.util.Preconditions
import androidx.core.view.ViewCompat
import com.lamhx.amptaste.network.WebCacheRequestResource
import java.util.regex.Pattern

class WebViewBase : WebView {
    var destroyAllowed = false
    val jsErrorMatcher = JS_ERROR_PATTERN.matcher("")

    private var resourceRequester: WebCacheRequestResource? = null

    fun setResourceRequester(requester: WebCacheRequestResource) {
        this.resourceRequester = requester
    }

    constructor(context: Context?) : super(context!!) {
        configure()
    }

    constructor(
        context: Context?,
        attributeSet: AttributeSet?
    ) : super(context!!, attributeSet) {
        configure()
    }

    constructor(
        context: Context?,
        attributeSet: AttributeSet?,
        i: Int
    ) : super(context!!, attributeSet, i) {
        configure()
    }

    private fun configure() {
        initWebViewSettings()
        initWebViewClient()
        initWebChromeClient()
    }

    /* access modifiers changed from: protected */
    @SuppressLint("SetJavaScriptEnabled")
    fun initWebViewSettings() {
        val settings = settings
        settings.allowFileAccess = false
        settings.databaseEnabled = false
        settings.domStorageEnabled = true
        settings.javaScriptEnabled = true
        settings.setSupportMultipleWindows(true)
        settings.setSupportZoom(true)
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = false
        if (!didSetRenderPriority) {
            didSetRenderPriority = true
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        }
        ViewCompat.setImportantForAccessibility(this, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES)
        isFocusableInTouchMode = true
    }

    override fun canScrollHorizontally(i: Int): Boolean {
        return false
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        requestDisallowInterceptTouchEvent(true)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event?.action == MotionEvent.ACTION_UP || event?.action == MotionEvent.ACTION_CANCEL) {
            requestDisallowInterceptTouchEvent(false)
        }
        return super.onTouchEvent(event)
    }

    @SuppressLint("RestrictedApi")
    override fun destroy() {
        Preconditions.checkState(destroyAllowed)
        super.destroy()
    }

    fun startDestroy() {
        stopLoading()
        onPause()
        postDelayed({
            onReallyDestroy()
            destroyAllowed = true
            val unused = destroyAllowed
            destroy()
            destroyAllowed = false
            val unused2 = destroyAllowed
        }, 2000)
    }

    /* access modifiers changed from: protected */
    fun onReallyDestroy() {}

    private fun initWebViewClient() {
        webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                webView: WebView,
                url: String
            ): WebResourceResponse? {
                return if (resourceRequester != null) {
                    resourceRequester!!.getResource(url)
                } else {
                    super.shouldInterceptRequest(webView, url)
                }
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                if(resourceRequester != null) {
                    return resourceRequester!!.getResource(request!!.url)
                }
                return super.shouldInterceptRequest(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.d("WebBaseLoad", "onPageStarted: ${System.currentTimeMillis()}")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("WebBaseLoad", "onPageFinished: ${System.currentTimeMillis()}")
            }
        }
    }

    private fun initWebChromeClient() {
        webChromeClient = WebChromeClient()
    }

    private fun getHorizontalScrollMax(): Int {
        return Math.max(
            computeHorizontalScrollRange() - computeHorizontalScrollExtent(),
            0
        )
    }

    private fun getVerticalScrollMax(): Int {
        return Math.max(computeVerticalScrollRange() - computeVerticalScrollExtent(), 0)
    }

    companion object {
        val JS_ERROR_PATTERN =
            Pattern.compile(".*(Syntax|Type|Reference)Error.*")
        const val WEBVIEW_HTML_FILENAME = "webview.html"
        const val WEBVIEW_HTML_LEGACY_FILENAME = "webview_legacy.html"
        var didSetRenderPriority = false
    }
}