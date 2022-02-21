package com.example.mvvmbyshahrukh.repository

import com.example.mvvmbyshahrukh.ApiConstant
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

class ServiceGenerator {

    fun <S> createService(serviceClass: Class<S>?): S {
        val httpClient = httpClientBuilder!!
            .build()
        val retrofit = builder.client(httpClient).build()
        return retrofit.create(serviceClass)
    }

    companion object {
        var DATE_FORMATS = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        var httpClientBuilder: OkHttpClient.Builder? = null
            get() {
                if (field == null) {
                    field = OkHttpClient.Builder()
                }
                try {
                    val trustAllCerts = arrayOf<TrustManager>(
                        object : X509TrustManager {
                            override fun checkClientTrusted(
                                chain: Array<X509Certificate>,
                                authType: String
                            ) {
                            }

                            override fun checkServerTrusted(
                                chain: Array<X509Certificate>,
                                authType: String
                            ) {
                            }

                            override fun getAcceptedIssuers(): Array<X509Certificate> {
                                return arrayOf()
                            }
                        }
                    )
                    var sslContext: SSLContext? = null
                    try {
                        sslContext = SSLContext.getInstance("SSL")
                    } catch (e: NoSuchAlgorithmException) {
                        e.printStackTrace()
                    }
                    try {
                        sslContext?.init(null, trustAllCerts, SecureRandom())
                    } catch (e: KeyManagementException) {
                        e.printStackTrace()
                    }
                    var sslSocketFactory: SSLSocketFactory? = null
                    if (sslContext != null) {
                        sslSocketFactory = sslContext.socketFactory
                    }
                    if (sslSocketFactory != null) {
                        field!!.sslSocketFactory(
                            sslSocketFactory,
                            trustAllCerts[0] as X509TrustManager
                        )
                    }
                    field!!.hostnameVerifier { hostname: String?, session: SSLSession? -> true }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return field
            }
            private set
        private var httpClient: OkHttpClient? = null
        private val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
        private val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(ApiConstant.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))

        fun getOkHTTPClient(interceptor: Interceptor?): OkHttpClient? {
            if (httpClient == null) {
                httpClient = httpClientBuilder!!
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build()
            }
            return httpClient
        }

        fun <S> createService(serviceClass: Class<S>?, interceptor: Interceptor?): S {
            val httpClient =
                getOkHTTPClient(interceptor)
            httpClient!!.dispatcher().cancelAll()
            val retrofit = builder.client(httpClient).build()
            return retrofit.create(serviceClass)
        }
    }
}
