package com.example.mvvmbyshahrukh.repository.resource

sealed class Resource<T>(
    var status: Status? = null,
    var data: T? = null,
    var message: String? = null
) {
    class Success<T>(data: T?) : Resource<T>(Status.SUCCESS, data!!)
    class Loading<T>(data: T? = null) : Resource<T>(Status.LOADING, data)
    class Error<T>(data: T? = null, message: String?) : Resource<T>(Status.ERROR, data, message)

    enum class Status { /* Errror handle karna isse */
        SUCCESS, ERROR, LOADING
    }
}
