package com.interview.task.data.adapters

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.interview.task.R
import com.interview.task.data.db.entitys.Product
import com.interview.task.ui.listeners.ProductActivityListener
import com.interview.task.ui.view.AddUpdateProduct
import com.interview.task.ui.view.SubItems
import com.interview.task.utils.NewIntentWithData
import com.interview.task.utils.NewIntentWithDataActivityResult
import kotlinx.android.synthetic.main.product_cell.view.*
import java.util.*
import kotlin.collections.ArrayList

class ProductAdapter(
    private var context: Context,
    private var listener: ProductActivityListener,
    private val AdapterList: ArrayList<Product>
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>(), Filterable {

    var FilterList = ArrayList<Product>()

    init {
        FilterList = AdapterList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_cell, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return FilterList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.lblItemName.text = FilterList[position].title
        holder.lblDescription.text = FilterList[position].description
        holder.lblStatus.text = FilterList[position].status
        holder.lblCategory.text = FilterList[position].category

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val data = HashMap<String, Any>()
                data.put("Product", Gson().toJson(FilterList[position]))
                (context as Activity).NewIntentWithDataActivityResult(
                    SubItems::class.java,
                    data,
                    false,
                    false
                )
            }
        })

        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                ConfirmDialog(FilterList[position])
                return true
            }
        })
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var lblItemName = view.lblItemName
        var lblDescription = view.lblDescription
        var lblStatus = view.lblStatus
        var lblCategory = view.lblCategory
    }

    var alertDialog: AlertDialog? = null
    var builder: AlertDialog.Builder? = null

    fun ConfirmDialog(product: Product) {
        if (alertDialog != null) {
            if (alertDialog!!.isShowing) {
                return
            }
        }

        builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder!!.setTitle(product.title)
        //set message for alert dialog
        builder!!.setMessage("Choose your action")
        builder!!.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder!!.setPositiveButton("Update") { dialogInterface, which ->
            val data = HashMap<String, Any>()
            data.put("Product", Gson().toJson(product))
            data.put("IsAdd", false)
            (context as Activity).NewIntentWithDataActivityResult(
                AddUpdateProduct::class.java,
                data,
                false,
                false
            )
            alertDialog!!.dismiss()
            alertDialog = null
        }
        //performing cancel action
        builder!!.setNeutralButton("Cancel") { dialogInterface, which ->
            alertDialog!!.dismiss()
            alertDialog = null
        }
        //performing negative action
        builder!!.setNegativeButton("Delete") { dialogInterface, which ->
            alertDialog!!.dismiss()
            alertDialog = null
            listener.onDelete(product)
        }
        // Create the AlertDialog
        alertDialog = builder!!.create()
        // Set other dialog properties
        alertDialog!!.setCancelable(false)
        alertDialog!!.show()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    FilterList = AdapterList
                } else {
                    val resultList = ArrayList<Product>()
                    for (row in AdapterList) {
                        if (row.category!!.toLowerCase(Locale.ROOT).contains(
                                charSearch.toLowerCase(
                                    Locale.ROOT
                                )
                            ) || row.title!!.toLowerCase(Locale.ROOT).contains(
                                charSearch.toLowerCase(
                                    Locale.ROOT
                                )
                            )
                        ) {
                            resultList.add(row)
                        }
                    }
                    FilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = FilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                FilterList = results?.values as ArrayList<Product>
                notifyDataSetChanged()
            }
        }
    }
}