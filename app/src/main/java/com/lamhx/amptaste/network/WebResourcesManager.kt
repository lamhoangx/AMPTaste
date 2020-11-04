package com.lamhx.amptaste.network

import android.net.Uri
import android.text.TextUtils
import android.webkit.WebResourceResponse
import com.lamhx.amptaste.MainApplication
import com.lamhx.amptaste.diskcache.DiskCacheController
import com.lamhx.amptaste.diskcache.WebDiskCache
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.*
import java.util.concurrent.TimeUnit


class WebResourcesManager: WebCacheRequestResource  {
    private object InstanceHolder {
        var instance = WebResourcesManager()
    }

    companion object {
        @JvmStatic
        fun getInstance(): WebResourcesManager {
            return InstanceHolder.instance
        }
    }

    private var okHttpClient: OkHttpClient = OkHttpClient.Builder()
//        .cache(Cache(
//            directory = File(MainApplication.getAppContext().cacheDir, "http_cache"),
//            maxSize = 100L * 1024L * 1024L // 100 MiB
//        ))
        .build()

    override fun getResource(url: String): WebResourceResponse? {
        if (!DiskCacheController.useResCached) {
            // ignore
            return null
        }
        val uri = Uri.parse(url)
        return getResource(uri)
    }

    override fun getResource(url: Uri): WebResourceResponse? {
        val request = Request.Builder()
//            .cacheControl(CacheControl.Builder().onlyIfCached().build())
            .url(url.toString())
            .build()
        try {
            if (!DiskCacheController.useResCached) {
                // ignore
                return null
            }

            if (TextUtils.equals("http", url.scheme)
                || TextUtils.equals("https", url.scheme)
            ) {
                val cache = WebDiskCache.get(url = url.toString())
                if (cache != null) {
                    return WebResourceResponse(
                        cache.infoCache.mimeType,
                        cache.infoCache.charset,
                        cache.data.inputStream()
                    )
                }
            }
            var response = okHttpClient.newCall(request).execute()
            if (response.code != 504) {
                // The resource was cached! Show it.
            } else {
                val request: Request = Request.Builder()
//                    .cacheControl(
//                        CacheControl.Builder()
//                            .maxStale(7, TimeUnit.DAYS)
//                            .build()
//                    )
                    .url(url = url.toString())
                    .build()
                response = okHttpClient.newCall(request).execute()
            }
            val inputStream = response.body?.byteStream()
            val contentType = response.body?.contentType()
            val mimeType: String ="${contentType?.type}/${contentType?.subtype}"
            val charset: String? = contentType?.parameter("charset")

            if (TextUtils.equals("http", url.scheme)
                || TextUtils.equals("https", url.scheme)
            ) {
                // cache to local
                inputStream?.let {
                    val fileStored = WebDiskCache.flushToFile(
                        url = url.toString(),
                        mimeType = mimeType,
                        charset = charset,
                        inputStream = it
                    )
                    return WebResourceResponse(mimeType, charset, fileStored?.inputStream())
                }
            } else {
                return WebResourceResponse(mimeType, charset, inputStream)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return WebResourceResponse(null, null, null)
    }
}

data class WebResourceCache(val infoCache: WebResourceInfoCache, val data: File){}

class WebResourceInfoCache(val mimeType: String, val charset: String, val md5File: String): Serializable {
    companion object {
        fun create(inputStream: InputStream): WebResourceInfoCache? {
            var input: ObjectInput? = null
            try {
                input = ObjectInputStream(inputStream);
                val cache: WebResourceInfoCache =
                    input.readObject() as WebResourceInfoCache;
               return cache
            } catch (e: Throwable) {
                return null
            } finally {
                try {
                    input?.close()
                } catch (ex:IOException) {
                    // ignore close exception
                }
            }
        }
    }
}

fun WebResourceInfoCache.serialize(): ByteArray? {
    val bos =  ByteArrayOutputStream()
    var out: ObjectOutputStream? = null
    try {
        out = ObjectOutputStream(bos)
        out.writeObject(this)
        out.flush()
        return bos.toByteArray()
    } catch (e: Exception) {
        return null
    } finally {
        try {
            bos.close();
        } catch (ex: IOException) {
            // ignore close exception
        }
    }
}