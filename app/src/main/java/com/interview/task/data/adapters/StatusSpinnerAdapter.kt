package com.interview.task.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.interview.task.R
import com.interview.task.data.responses.status.Status

class StatusSpinnerAdapter(val context: Context, var dataSource: List<Status>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.spinner_layout, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.label.text = dataSource.get(position).title

        return view
    }

    override fun getItem(position: Int): Any? {
        return dataSource[position];
    }

    override fun getCount(): Int {
        return dataSource.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(row: View?) {
        val label: TextView

        init {
            label = row?.findViewById(R.id.lblTitle) as TextView
        }
    }

}

/*
class StatusSpinnerAdapter(
    context: Context,
    groupid: Int,
    id: Int,
    list: ArrayList<Status>
) :
    ArrayAdapter<Status>(context, id, list) {
    var groupid: Int
    var list: ArrayList<Status>
    var inflater: LayoutInflater
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val itemView = inflater.inflate(groupid, parent, false)
        val textView = itemView.findViewById<View>(R.id.lblTitle) as TextView
        textView.setText(list[position].status)
        return itemView
    }

    override fun getDropDownView(
        position: Int,
        convertView: View,
        parent: ViewGroup
    ): View {
        return getView(position, convertView, parent)
    }

    init {
        this.list = list
        inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.groupid = groupid
    }
}*/
