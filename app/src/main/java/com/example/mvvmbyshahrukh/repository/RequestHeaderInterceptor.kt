package com.example.mvvmbyshahrukh.repository

import android.os.Build
import android.text.TextUtils
import android.util.Log
import com.example.mvvmbyshahrukh.ApiConstant
import okhttp3.*
import okio.BufferedSink
import okio.GzipSink
import okio.Okio
import java.io.IOException
import java.util.*

class RequestHeaderInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalRequestHeaders = original.headers()
        // Request customization: add request headers
        val requestBuilder = original.newBuilder() //                .header("api-key", "va$Tra@pP")
            .header("api-key", "1")
            .header("device-type", ApiConstant.HEADER_DEVICE_TYPE)
            .header("package_id", "")
            .header("mobile", getDeviceName())
        if (originalRequestHeaders.size() > 0) {
            val originalHeaderNames = originalRequestHeaders.names()
            for (headerName in originalHeaderNames) {
                requestBuilder.header(
                    headerName, Objects.requireNonNull(
                        originalRequestHeaders[headerName]
                    )
                )
            }
        }
        if (original.header("Content-Encoding") == null) {
            if (original.body() !is MultipartBody && (original.method()
                    .equals("put", ignoreCase = true) || original.method()
                    .equals("post", ignoreCase = true))
            ) {
                requestBuilder.header("Content-Encoding", "gzip")
                    .method(original.method(), gzip(original.body()))
            }
        }
        val request = requestBuilder.build()
        Log.e("request -- >> ", "" + request.toString())
        val t1 = System.nanoTime()
        Log.e("request  time -- >> ", "" + t1)
        val response = chain.proceed(request)
        val t2 = System.nanoTime()
        Log.e("response  time -- >> ", "" + t2)
        return response
    }

    private fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.lowercase(Locale.getDefault())
                .startsWith(manufacturer.lowercase(Locale.getDefault()))
        ) ({
            capitalize(model)
        }).toString() else {
            capitalize(manufacturer) + " " + model
        }
    }

    private fun capitalize(str: String): String? {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true
        var phrase: String? = ""
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c)
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase += c
        }
        return phrase
    }


    private fun gzip(body: RequestBody?): RequestBody {
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return body!!.contentType()
            }

            override fun contentLength(): Long {
                return -1 // We don't know the compressed length in advance!
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                val gzipSink = Okio.buffer(GzipSink(sink))
                body!!.writeTo(gzipSink)
                gzipSink.close()
            }
        }
    }
}
