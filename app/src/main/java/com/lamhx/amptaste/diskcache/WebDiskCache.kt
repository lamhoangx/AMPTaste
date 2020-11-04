package com.lamhx.amptaste.diskcache

import com.lamhx.amptaste.MainApplication
import com.lamhx.amptaste.extension.toMD5
import com.lamhx.amptaste.network.WebResourceCache
import com.lamhx.amptaste.network.WebResourceInfoCache
import com.lamhx.amptaste.network.serialize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream

class WebDiskCache {
    companion object {

        fun getWebCacheDir(): File {
            return File(MainApplication.getAppContext().cacheDir, "webcache_local")
        }

        fun flushToFile(
            url: String,
            mimeType: String,
            charset: String?,
            inputStream: InputStream
        ): File? {
            val md5Url = url.toMD5()
            val data = File(getWebCacheDir(), "${md5Url}_data")
            if(data.exists()){
                data.delete()
            }
            data.parentFile?.mkdirs()
            data.createNewFile()
            data.copyInputStreamToFile(inputStream)
            WebResourceInfoCache(
                mimeType = mimeType,
                charset = charset ?: "",
                md5File = md5Url
            ).serialize()?.let {
                val info = File(getWebCacheDir(), md5Url)
                if(info.exists()) {
                    info.delete()
                }
                info.createNewFile()
                info.writeBytes(it)

            }
            return data
        }
        fun get(url: String): WebResourceCache? {
            val md5Url = url.toMD5()
            val fileInfoCache: File = File(getWebCacheDir(), md5Url)
            val data: File = File(getWebCacheDir(), "${md5Url}_data")
            if(fileInfoCache.exists() && data.exists()) {
                return WebResourceInfoCache.create(fileInfoCache.inputStream())?.let {
                    WebResourceCache(
                        it, data)
                }
            }
            return null
        }
    }
}

fun File.copyInputStreamToFile(inputStream: InputStream) {
    this.outputStream().use { fileOut ->
        inputStream.copyTo(fileOut)
    }
}

