package com.example.mvvmbyshahrukh.ui.response

import android.net.Uri
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import java.lang.NumberFormatException

class Page(
    nextPage: String?,
    previousPage: String?,
    serverdatetime: Long?,
    currentPriority: Long?
) {
    var nextPage: Long = 0
    var previousPage: Long = 0
    var datetime: Long = 0
    var serverdatetime: Long = 0
    var currentPriority: Long = 0

    fun hasNextPage(): Boolean {
        return nextPage > 0
    }

    fun hasPreviousPage(): Boolean {
        return previousPage > 0
    }

    override fun toString(): String {
        return gson.toJson(this)
    }

    companion object {
        private val gson: Gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        fun fromJson(page: JsonObject): Page? {
            val nextPage: String? = if (page.has("next")) page.get("next").asString else null
            val prevPage: String? =
                if (page.has("previous")) page.get("previous").asString else null
            val currentPriority: Long? =
                if (page.has("current_priority")) page.get("current_priority").asLong else null
            val serverDatetime: Long =
                if (page.has("serverdatetime")) page.get("serverdatetime").asLong else 0
            return if (nextPage == null && prevPage == null && serverDatetime == 0L) null else Page(
                nextPage,
                prevPage,
                serverDatetime,
                currentPriority
            )
        }
    }

    init {
        var nextPage = nextPage
        var previousPage = previousPage
        if (nextPage != null) {
            val uri = Uri.parse(nextPage)
            nextPage = uri.getQueryParameter("pageno")
            try {
                this.nextPage = java.lang.Long.valueOf(nextPage)
            } catch (e: NumberFormatException) {
                this.nextPage = -1
            }
            datetime = try {
                java.lang.Long.valueOf(uri.getQueryParameter("datetime"))
            } catch (e: NumberFormatException) {
                -1
            }
            try {
                this.currentPriority =
                    java.lang.Long.valueOf(uri.getQueryParameter("current_priority"))
            } catch (e: NumberFormatException) {
                this.currentPriority = -1
            }
        }
        if (currentPriority != null) {
            this.currentPriority = currentPriority
        }
        if (previousPage != null) {
            val uri = Uri.parse(previousPage)
            previousPage = uri.getQueryParameter("pageno")
            try {
                this.previousPage = java.lang.Long.valueOf(previousPage)
            } catch (e: NumberFormatException) {
                this.previousPage = -1
            }
            datetime = try {
                java.lang.Long.valueOf(uri.getQueryParameter("datetime"))
            } catch (e: NumberFormatException) {
                -1
            }
        }
        if (serverdatetime != 0L) {
            this.serverdatetime = serverdatetime!!
        }
    }
}
