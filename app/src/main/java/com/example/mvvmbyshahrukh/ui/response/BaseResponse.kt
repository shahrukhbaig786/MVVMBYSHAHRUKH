package com.example.mvvmbyshahrukh.ui.response

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject

/**
 * Base class for api response <br></br>
 */
class BaseResponse {
    var data: Any
    private var page: Page? = null
    var accessToken: String? = null
    private var filter: JsonObject? = null
    var refresh = false

    constructor(data: Any, page: Page?, accessToken: String?) {
        this.data = data
        this.page = page
        this.accessToken = accessToken
    }

    constructor(data: Any, page: Page?, accessToken: String?, filter: JsonObject?) {
        this.data = data
        this.page = page
        this.accessToken = accessToken
        this.filter = filter
    }

    fun getFilter(): JsonObject? {
        return filter
    }

    fun setFilter(filter: JsonObject?) {
        this.filter = filter
    }

    fun getPage(): Page? {
        return page
    }

    fun setPage(page: Page?) {
        this.page = page
    }

    fun hasPage(): Boolean {
        return page != null
    }

    override fun toString(): String {
        return gson.toJson(this)
    }

    companion object {
        private val gson: Gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        fun fromJson(response: JsonObject): BaseResponse {
            val data: Any = response.get("data")
            var page: Page? = null
            val accessToken: String = if (response.has("access-token")) response.get("access-token")
                .asString else (if (response.has("access_token")) response.get("access_token")
                .asString else "")!!
            //        AppLogger.e("JsonObject  " + response);
            if (response.has("page")) {
                Log.e("page ->>   " ," "+ response.getAsJsonObject("page"))
                page = Page.fromJson(response.getAsJsonObject("page"))
            }
            return if (response.has("filter")) {
                BaseResponse(data, page, accessToken, response.get("filter").asJsonObject)
            } else {
                BaseResponse(data, page, accessToken)
            }
        }
    }
}
