package com.interview.task.data.repositorys

import com.interview.task.data.responses.login.ResponseData
import com.interview.task.model.api.ApiClient
import io.reactivex.Observable

// Repository
class LoginActivityRepository() {
    fun postUserLogin(
        username: String,
        password: String
    ): Observable<ResponseData> {
        var prm = HashMap<String, String>()
        prm["username"] = username
        prm["password"] = password
        return ApiClient().postLogin(prm)
    }
}