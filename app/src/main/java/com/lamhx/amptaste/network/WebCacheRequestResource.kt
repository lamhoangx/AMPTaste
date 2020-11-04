package com.lamhx.amptaste.network

import android.net.Uri
import android.webkit.WebResourceResponse

interface WebCacheRequestResource {
    fun getResource(url: String): WebResourceResponse?
    fun getResource(url: Uri): WebResourceResponse?
}