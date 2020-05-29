package com.interview.task.ui.listeners

import com.interview.task.data.db.entitys.Product

interface AddUpdateProductActivityListener {
    fun showProgress()
    fun hideProgress()
    fun onSuccess(message: String)
    fun onFailure(message: String)
}