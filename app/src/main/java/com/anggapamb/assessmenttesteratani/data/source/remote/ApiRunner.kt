package com.anggapamb.assessmenttesteratani.data.source.remote

import com.nuvyz.core.data.model.response.ApiResponse
import com.nuvyz.core.data.model.response.ApiStatus
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException

object ApiRunner {

    suspend inline fun <reified T> run(crossinline block: suspend () -> T): ApiResponse<T> {
        return try {
            val data = block()
            ApiResponse(status = ApiStatus.SUCCESS, data = data)
        } catch (e: HttpException) {
            val msg = parseGoRestError(e)
            ApiResponse(status = ApiStatus.ERROR, message = msg, data = null)
        } catch (e: Exception) {
            ApiResponse(status = ApiStatus.ERROR, message = e.localizedMessage ?: "Internal error", data = null)
        }
    }

    fun parseGoRestError(e: HttpException): String {
        val raw = e.response()?.errorBody()?.string().orEmpty()
        if (raw.isBlank()) return e.message()

        return try {
            val t = raw.trim()
            if (t.startsWith("[")) {
                val arr = JSONArray(t)
                val parts = mutableListOf<String>()
                for (i in 0 until arr.length()) {
                    val o = arr.optJSONObject(i) ?: continue
                    val field = o.optString("field")
                    val msg = o.optString("message")
                    parts.add(if (field.isNotBlank()) "$field: $msg" else msg)
                }
                parts.joinToString("; ")
            } else {
                val obj = JSONObject(t)
                when {
                    obj.has("message") -> obj.optString("message")
                    else -> e.message()
                }
            }
        } catch (_: Exception) {
            e.message()
        }
    }
}