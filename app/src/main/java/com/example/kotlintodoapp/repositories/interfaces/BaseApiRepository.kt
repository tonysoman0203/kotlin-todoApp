package com.example.kotlintodoapp.repositories.interfaces

import com.example.kotlintodoapp.helper.TodoLogger
import retrofit2.Response
import java.io.IOException

open class BaseApiRepository {
    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, errorMessage: String): T? {

        val result: Result<T> = safeApiResult(call, errorMessage)
        var data: T? = null

        when (result.isSuccess) {
            true -> data = result.getOrNull()
            false -> {
                TodoLogger.debug(
                    "1.BaseApiRepository",
                    "$errorMessage & Exception - ${result.exceptionOrNull()}"
                )
            }
        }


        return data

    }

    private suspend fun <T : Any> safeApiResult(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): Result<T> {
        val response = call.invoke()
        if (response.isSuccessful) return Result.success(response.body()!!)

        return Result.failure(IOException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))
    }
}