package com.interview.task.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.interview.task.R
import com.interview.task.data.responses.category.Categorys
import com.interview.task.utils.SharedUtill.setCategoryList
import kotlinx.android.synthetic.main.product_filter_cell.view.*

class CategoryFilterProductAdapter(
    private var context: Context,
    private val AdapterList: ArrayList<Categorys>
) : RecyclerView.Adapter<CategoryFilterProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_filter_cell, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return AdapterList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.lblTitle.text = AdapterList[position].title
        holder.ckFilter.isChecked = AdapterList[position].is_select

        holder.ckFilter.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                AdapterList[position].is_select = holder.ckFilter.isChecked
                context.setCategoryList(Gson().toJson(AdapterList))
                notifyDataSetChanged()
            }
        })
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var lblTitle = view.lblTitle
        var ckFilter = view.ckFilter
    }

}