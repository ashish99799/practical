package com.interview.task.ui.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.interview.task.R
import com.interview.task.data.adapters.ProductAdapter
import com.interview.task.data.db.entitys.Product
import com.interview.task.data.responses.category.Categorys
import com.interview.task.data.responses.status.Status
import com.interview.task.ui.listeners.ProductActivityListener
import com.interview.task.ui.view_model.ProductActivityViewModel
import com.interview.task.utils.*
import com.interview.task.utils.SharedUtill.setCategoryList
import com.interview.task.utils.SharedUtill.setStatusList
import kotlinx.android.synthetic.main.product_activity.*
import java.util.*
import kotlin.collections.ArrayList

class ProductActivity : AppCompatActivity(), ProductActivityListener, View.OnClickListener,
    SearchView.OnQueryTextListener {

    lateinit var context: Context

    var categoryList: ArrayList<Categorys>
    var statusList: ArrayList<Status>
    var AscDesc: Boolean

    var alertDialog: AlertDialog? = null
    var builder: AlertDialog.Builder? = null

    var productList: ArrayList<Product>
    lateinit var productAdapter: ProductAdapter
    lateinit var viewModel: ProductActivityViewModel

    init {
        categoryList = ArrayList<Categorys>()
        statusList = ArrayList<Status>()
        AscDesc = true
        productList = ArrayList<Product>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_activity)

        context = this

        // Setup SupportActionBar to over Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        InitView()
    }

    fun InitView() {
        fabAdd.setOnClickListener(this)

        rvProduct.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewModel = ViewModelProvider(this).get(ProductActivityViewModel::class.java)
        viewModel.productActivityListener = this

        var categorys = resources.getStringArray(R.array.category)

        categorys.forEach {
            categoryList.add(Categorys(it, false))
        }

        setCategoryList(Gson().toJson(categoryList))

        var status = resources.getStringArray(R.array.status)

        status.forEach {
            statusList.add(Status(it, false))
        }

        setStatusList(Gson().toJson(statusList))

        viewModel.getAllProducts(context)

        searchView.setOnQueryTextListener(this)
        val searchClose =
            searchView.findViewById(androidx.appcompat.R.id.search_close_btn) as ImageView
        searchClose.setOnClickListener {
            searchView.clearFocus()
            searchView.setQuery("", false)
            viewModel.getAllProducts(context)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        productAdapter.filter.filter(newText)
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.filter_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.menuFilter -> {
                val data = HashMap<String, Any>()
                data.put("AscDesc", AscDesc)
                NewIntentWithDataActivityResult(ProductFilter::class.java, data, false, false)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
        if (v!!.id == fabAdd.id) {
            val data = HashMap<String, Any>()
            data.put("IsAdd", true)
            NewIntentWithDataActivityResult(AddUpdateProduct::class.java, data, false, false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
        when (requestCode) {
            ACTIVITY_RESULT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (intentData != null) {
                        try {
                            val IsUpdate = intentData.getBooleanExtra("IsRefresh", false)
                            if (IsUpdate) {
                                viewModel.getAllProducts(context)
                            }
                        } catch (e: Exception) {
                            hideProgress()
                        }
                    } else {
                        hideProgress()
                    }
                }
            }
        }
    }

    override fun showProgress() {
        showProgressDialog()
    }

    override fun hideProgress() {
        hideProgressDialog()
    }

    override fun onSuccess(data: ArrayList<Product>) {
        productList.clear()
        productList = data
        productAdapter = ProductAdapter(context, this, productList)
        rvProduct.adapter = productAdapter
    }

    override fun onDeleteSuccess(message: String) {
        ToastMessage(message)
        viewModel.getAllProducts(context)
    }

    override fun onDelete(product: Product) {
        if (alertDialog != null) {
            if (alertDialog!!.isShowing) {
                return
            }
        }

        builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder!!.setTitle(product.title)
        //set message for alert dialog
        builder!!.setMessage("Are your sure you want to delete this Product!")
        builder!!.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder!!.setPositiveButton("Yes") { dialogInterface, which ->
            viewModel.deleteProduct(context, product)
            alertDialog!!.dismiss()
            alertDialog = null
        }
        //performing cancel action
        builder!!.setNeutralButton("No") { dialogInterface, which ->
            alertDialog!!.dismiss()
            alertDialog = null
        }

        // Create the AlertDialog
        alertDialog = builder!!.create()
        // Set other dialog properties
        alertDialog!!.setCancelable(false)
        alertDialog!!.show()
    }

    override fun onFailure(message: String) {
        ToastMessage(message)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
