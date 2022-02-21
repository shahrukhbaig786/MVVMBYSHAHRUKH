package com.vastra.aapRepository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mvvmbyshahrukh.repository.*
import com.example.mvvmbyshahrukh.repository.resource.Resource
import com.example.mvvmbyshahrukh.ui.response.BaseResponse
import com.google.gson.JsonObject
import retrofit2.Call

/* Creared by shaharukh 10/4/2021*/

class VatraRepository {

    private fun getRetrofitInstance(): JobCardClient {
        return JobCardApplication().getApiClient()
    }

    fun callMaterialListApi(): MutableLiveData<Resource<BaseResponse?>> {
        val call = getRetrofitInstance().getMaterialStockList(
            getUdid(),
            "06662840-8afd-11ec-963a-39bd53658e27",
            "",
            1,
            null,
            null
        )
        return getResponse(call)
    }

    var result = MutableLiveData<Resource<BaseResponse?>>()
    lateinit var mListener: ResponseListener


    /* Comman in all vastra app calling */
    private fun getResponse(call: Call<JsonObject>): MutableLiveData<Resource<BaseResponse?>> {
        ApiCallManager.enqueue("login", call, object : CallbackAdapter() {
            override fun onApiResponse(err: ErrorResponse?, response: BaseResponse?) {
                if (err != null) {
                    Log.e("ERORRR", "" + err.message)
                    result.value = Resource.Error(null, err.message)
                    mListener.response(result)
                }
                if (response?.data != null) {
                    Log.e("response iss", "" + response.data.toString())
                    result.value = Resource.Success(response)
                    mListener.response(result)
                }
            }
        })
        return result
    }

    @SuppressLint("HardwareIds")
    fun getUdid(): String? {
        return "eb9dacb081f05322"
    }

    interface ResponseListener {
        fun response(selectedPosition: MutableLiveData<Resource<BaseResponse?>>)
    }
}

