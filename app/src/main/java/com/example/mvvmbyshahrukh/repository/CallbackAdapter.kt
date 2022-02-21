package com.example.mvvmbyshahrukh.repository

import android.util.Log
import com.example.mvvmbyshahrukh.ui.response.BaseResponse
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class CallbackAdapter : Callback<JsonObject?> {
    override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
        if (call.isCanceled) {
            val errorResponse = ErrorResponse("Seems like connectivity issue. Please try again.")
            onApiResponse(errorResponse, null)
            Log.e("errrorMsg3, ",call.toString())
            return
        }
        if (!response.isSuccessful) {
            var errorResponse: ErrorResponse?
            try {
                    errorResponse = ErrorResponse(response.errorBody()!!.string())
            } catch (e: Exception) {
                errorResponse = ErrorResponse.unknownError()
            }
            onApiResponse(errorResponse, null)
            return
        }
        val body = response.body()
        if (body == null) {
            try {
            } catch (e: Exception) {
                e.printStackTrace()
            }
            onApiResponse(ErrorResponse.unknownError(), null)
            return
        }
        if (body.has("status")) {
            val status = body["status"].asBoolean
            if (!status) {
                try {
                    println("body $body")
                    val errorResponse: ErrorResponse =
                        ErrorResponse.fromJson(body["error"].asJsonObject)
                    val errorCode = body["error"].asJsonObject["code"].toString()
                    onApiResponse(errorResponse, null)
                } catch (e: Exception) {
                    val errorResponse = ErrorResponse("Something went wrong, Please try again.")
                    onApiResponse(errorResponse, null)
                }
            } else {
                val baseResponse: BaseResponse = BaseResponse.fromJson(body)
                onApiResponse(null, baseResponse)
            }
        } else {
            val errorResponse = ErrorResponse("Something went wrong, Please try again.")
            onApiResponse(errorResponse, null)
        }
    }

    override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
//        ErrorResponse errorResponse = new ErrorResponse(-2, t.getLocalizedMessage());
//        ErrorResponse errorResponse = new ErrorResponse("Please check your internet connection.");
        var mesg = ""
        val errorResponse =
            ErrorResponse("Network failure, Seems like connectivity issue. Please try again. $mesg")
        onApiResponse(errorResponse, null)
        Log.e("errrorMsg2" ,""+ errorResponse.toString())
    }

    abstract fun onApiResponse(err: ErrorResponse?, response: BaseResponse?)

    companion object {
        private val gson =
            GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
    }
}
