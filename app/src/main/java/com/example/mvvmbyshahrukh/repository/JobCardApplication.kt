package com.example.mvvmbyshahrukh.repository

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatDelegate

class JobCardApplication : Application() {
    var bitmap: Bitmap? = null

    override fun onCreate() {
        super.onCreate()
        jobCardApplication = this
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
//        database = JobCardDbOperations(applicationContext)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

     fun getApiClient():JobCardClient{
        if(apiClient==null){
            apiClient = ServiceGenerator.createService(
                JobCardClient::class.java, RequestHeaderInterceptor()
            )
        }
        return apiClient!!;
    }

    companion object {
        var apiClient: JobCardClient? = null
        var jobCardApplication: JobCardApplication? = null
        val appContext: Context?
            get() {
                if (jobCardApplication == null) {
                    jobCardApplication = JobCardApplication()
                }
                return jobCardApplication
            }
        val instant: JobCardApplication?
            get() {
                if (jobCardApplication == null) {
                    jobCardApplication = JobCardApplication()
                }
                return jobCardApplication
            }
    }

    init {
        jobCardApplication = this
    }
}
