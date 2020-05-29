package com.interview.task.model.api

import com.interview.task.data.responses.login.ResponseData
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiClient {

    companion object {
        operator fun invoke(): ApiClient {
            val client = OkHttpClient
                .Builder()
                .build()

            return Retrofit.Builder()
                .baseUrl("https://slbs21.luckbyspin.in/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(ApiClient::class.java)
        }
    }

    @POST("API/LoginDemo")
    @FormUrlEncoded
    fun postLogin(
        @FieldMap params: Map<String, String>
    ): Observable<ResponseData>

}
