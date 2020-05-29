package com.interview.task.ui.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.interview.task.data.db.AppDatabase
import com.interview.task.ui.listeners.AddUpdateProductActivityListener

// Override ViewModel
class AddUpdateProductActivityViewModel : ViewModel() {

    lateinit var db: AppDatabase

    // Over Activity Listener
    var addUpdateProductActivityListener: AddUpdateProductActivityListener? = null

    fun addProduct(context: Context) {
        addUpdateProductActivityListener?.showProgress()

        db = AppDatabase(context)

        var list = db.allProducts(context)

        if (list.size != 0) {
            addUpdateProductActivityListener?.onSuccess("Product inserted successfully")
        } else {
            addUpdateProductActivityListener?.onFailure("Oops!! Please try after some time")
        }

        addUpdateProductActivityListener?.hideProgress()
    }

    fun updateProduct(context: Context) {
        addUpdateProductActivityListener?.showProgress()

        db = AppDatabase(context)

        var list = db.allProducts(context)

        if (list.size != 0) {
            addUpdateProductActivityListener?.onSuccess("Product updated successfully")
        } else {
            addUpdateProductActivityListener?.onFailure("Oops!! Please try after some time")
        }

        addUpdateProductActivityListener?.hideProgress()
    }
}