package com.interview.task.ui.listeners

import com.interview.task.data.responses.login.ResponseData

interface LoginActivityListener {
    fun showProgress()
    fun hideProgress()
    fun onSuccess(data: ResponseData)
    fun onFailure(message: String)
}