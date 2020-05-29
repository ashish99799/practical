package com.interview.task.ui.view

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.interview.task.R
import com.interview.task.data.adapters.DisplaySubProductAdapter
import com.interview.task.data.db.entitys.Product
import com.interview.task.utils.INTENT_DATA
import kotlinx.android.synthetic.main.sub_items.*
import java.util.HashMap

class SubItems : AppCompatActivity() {

    lateinit var context: Context

    lateinit var hashMap: HashMap<String, Any>

    lateinit var product: Product

    lateinit var subProductAdapter: DisplaySubProductAdapter
    lateinit var subItemList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sub_items)

        context = this

        hashMap = intent.getSerializableExtra(INTENT_DATA) as (HashMap<String, Any>)

        product = Gson().fromJson(hashMap.get("Product").toString(), Product::class.java)
        subItemList = Gson().fromJson(
            product.sub_items!!.toString(),
            object : TypeToken<List<String>>() {}.getType()
        )

        // Setup SupportActionBar to over Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = product.title

        rvSubItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        subProductAdapter = DisplaySubProductAdapter(subItemList)
        rvSubItems.adapter = subProductAdapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
