package com.interview.task.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.interview.task.R
import kotlinx.android.synthetic.main.product_sub_item.view.*

class DisplaySubProductAdapter(
    private val AdapterList: ArrayList<String>
) : RecyclerView.Adapter<DisplaySubProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_sub_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return AdapterList.size
    }

    fun addSubProduct(subItem: String) {
        AdapterList.add(subItem)
        notifyDataSetChanged()
    }

    fun getSubItemList(): ArrayList<String> {
        return AdapterList
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.lblTitle.text = AdapterList[position]
        holder.imgDelete.visibility = View.GONE
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var lblTitle = view.lblTitle
        var imgDelete = view.imgDelete
    }

}