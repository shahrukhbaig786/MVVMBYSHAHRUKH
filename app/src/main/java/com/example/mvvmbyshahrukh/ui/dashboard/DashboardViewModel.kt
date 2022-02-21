package com.example.mvvmbyshahrukh.ui.dashboard

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmbyshahrukh.repository.resource.Resource
import com.example.mvvmbyshahrukh.ui.response.BaseResponse
import com.vastra.aapRepository.VatraRepository

class DashboardViewModel : ViewModel() , VatraRepository.ResponseListener {


    private val _text = MutableLiveData<String>().apply {
//        value = "This is dashboard Fragment"
    }

    private val _response = MutableLiveData<Resource<BaseResponse?>>().apply {
        value =  callMaterialAPIList()
    }

    fun callMaterialAPIList(): Resource<BaseResponse?>? {
       return VatraRepository().callMaterialListApi().value
    }

    val text: LiveData<String> = _text
    var response : MutableLiveData<Resource<BaseResponse?>> = _response

    override fun response(apiResponse: MutableLiveData<Resource<BaseResponse?>>) {
        response = apiResponse
    }
}