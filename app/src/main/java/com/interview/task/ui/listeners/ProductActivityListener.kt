package com.interview.task.ui.listeners

import com.interview.task.data.db.entitys.Product

interface ProductActivityListener {
    fun showProgress()
    fun hideProgress()
    fun onSuccess(data: ArrayList<Product>)
    fun onDeleteSuccess(message: String)
    fun onDelete(data: Product)
    fun onFailure(message: String)
}