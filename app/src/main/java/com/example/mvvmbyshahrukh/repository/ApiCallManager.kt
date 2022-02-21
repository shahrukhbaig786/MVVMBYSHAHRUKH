/*
 * Copyright 2016, The Digicorp Information Systems Pvt. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.mvvmbyshahrukh.repository

import com.google.gson.JsonObject
import com.example.mvvmbyshahrukh.repository.CallbackAdapter
import com.example.mvvmbyshahrukh.repository.ApiCallManager
import retrofit2.Call
import java.util.*

/**
 * Helper class to execute api call and can be useful to cancel it later if required.<br></br>
 * example: <br></br>
 * <pre>`
 * public void onCreate() {
 * ...
 * Call<JsonObject> call = apiClient.getCategories();
 * ApiCallManager.enqueue("get_categories", call, new CallbackAdapter() {
 * public void onApiResponse(ErrorResponse err, BaseResponse response) {
 * // handle api response
 * }
 * });
 * }
 *
 * public void onStop() {
 * ApiCallManager.cancelCall("get_categories");
 * super.onStop();
 * }
` * <pre>
 *
 * @author kevin.adesara on 1/3/17.
</pre></pre> */
object ApiCallManager {
    private val apiCallMap = WeakHashMap<String, Call<JsonObject>>()
    fun enqueue(tag: String?, call: Call<JsonObject>?, callbackAdapter: CallbackAdapter) {
        requireNotNull(tag) { "\"tag\" can't be null" }
        requireNotNull(call) { "\"call\" can't be null" }
        require(!call.isCanceled) { "Can't enqueue canceled call" }
        cancelCall(tag)
        executeCall(call, callbackAdapter)
        apiCallMap[tag] = call
    }

    fun cancelCall(tag: String) {
        if (!apiCallMap.containsKey(tag)) return
        val call = apiCallMap[tag]
        if (call == null || call.isCanceled) return
        if (!call.isExecuted) {
            call.cancel()
        }
        apiCallMap.remove(tag)
    }

    private fun executeCall(call: Call<JsonObject>, callbackAdapter: CallbackAdapter) {
        call.enqueue(callbackAdapter)
    }
}