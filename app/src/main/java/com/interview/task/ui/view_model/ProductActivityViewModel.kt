package com.interview.task.ui.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.interview.task.data.db.AppDatabase
import com.interview.task.data.db.entitys.Product
import com.interview.task.ui.listeners.ProductActivityListener

// Override ViewModel
class ProductActivityViewModel : ViewModel() {

    lateinit var db: AppDatabase

    // Over Activity Listener
    var productActivityListener: ProductActivityListener? = null

    fun getAllProducts(context: Context) {
        productActivityListener?.showProgress()

        db = AppDatabase(context)

        var list = db.allProducts(context)

        if (list.size != 0) {
            productActivityListener?.onSuccess(list)
        } else {
            productActivityListener?.onSuccess(list)
            productActivityListener?.onFailure("Product not found")
        }

        productActivityListener?.hideProgress()
    }

    fun deleteProduct(context: Context, product: Product) {
        productActivityListener?.showProgress()

        db = AppDatabase(context)

        if (db.deleteProduct(product)) {
            productActivityListener?.onDeleteSuccess("Product deleted successfully")
        } else {
            productActivityListener?.onFailure("Product not found")
        }

        productActivityListener?.hideProgress()
    }

    fun getProductsFilter(context: Context) {
        productActivityListener?.showProgress()

        db = AppDatabase(context)

        var list = db.allProducts(context)

        if (list.size != 0) {
            productActivityListener?.onSuccess(list)
        } else {
            productActivityListener?.onFailure("Product not found")
        }

        productActivityListener?.hideProgress()
    }
}