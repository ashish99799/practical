package com.interview.task.ui.view_model

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.interview.task.data.repositorys.LoginActivityRepository
import com.interview.task.data.responses.login.ResponseData
import com.interview.task.ui.listeners.LoginActivityListener
import com.interview.task.utils.CheckInternetConnectionAvailable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

// Override ViewModel
class LoginActivityViewModel : ViewModel() {

    // Over Activity Listener
    var loginActivityListener: LoginActivityListener? = null
    private var myCompositeDisposable: CompositeDisposable? = null
    private var loginActivityRepository: LoginActivityRepository? = null

    fun firstValidate(username: String, password: String): Boolean {
        if (TextUtils.isEmpty(username)) {
            loginActivityListener?.onFailure("Please enter Username")
            return false
        } else if (TextUtils.isEmpty(password)) {
            loginActivityListener?.onFailure("Please enter Password")
            return false
        } else {
            return true
        }
    }

    fun getSearchUser(context: Context, username: String, password: String) {
        // Check Internet connectivity
        if (context.CheckInternetConnectionAvailable()) {
            // API Calling Start
            loginActivityListener?.showProgress()

            loginActivityRepository = LoginActivityRepository()
            // Ratrofit API Calling
            myCompositeDisposable = CompositeDisposable()
            myCompositeDisposable?.add(
                loginActivityRepository?.postUserLogin(username, password)
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.io())
                    ?.subscribe({ response -> onResponse(response) }, { t -> onFailure(t) })!!
            )
        } else {
            // Internet is not connected
            loginActivityListener?.hideProgress()
            loginActivityListener?.onFailure("Please check your internet connection!")
        }
    }

    private fun onResponse(response: ResponseData) {
        loginActivityListener?.hideProgress()
        loginActivityListener?.onSuccess(response)
    }

    private fun onFailure(error: Throwable) {
        loginActivityListener?.hideProgress()
        var response: ResponseData =
            Gson().fromJson(
                (error as HttpException).response()!!.errorBody()!!.string(),
                ResponseData::class.java
            )
        loginActivityListener?.onFailure("${response.errors!!.validate}")
    }
}