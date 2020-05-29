package com.interview.task.ui.view

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.interview.task.R
import com.interview.task.data.adapters.CategoryFilterProductAdapter
import com.interview.task.data.adapters.StatusFilterProductAdapter
import com.interview.task.data.db.entitys.Product
import com.interview.task.data.responses.category.Categorys
import com.interview.task.data.responses.status.Status
import com.interview.task.utils.SharedUtill.getAscDesc
import com.interview.task.utils.SharedUtill.getCategoryList
import com.interview.task.utils.SharedUtill.getStatusList
import com.interview.task.utils.SharedUtill.setAscDesc
import com.interview.task.utils.SharedUtill.setCategoryList
import com.interview.task.utils.SharedUtill.setStatusList
import kotlinx.android.synthetic.main.product_filter.*

class ProductFilter : AppCompatActivity(), View.OnClickListener {

    lateinit var context: Context

    var categoryList: ArrayList<Categorys>
    var statusList: ArrayList<Status>
    var AscDesc: Boolean

    lateinit var categoryFilterProductAdapter: CategoryFilterProductAdapter
    lateinit var statusFilterProductAdapter: StatusFilterProductAdapter

    init {
        categoryList = ArrayList<Categorys>()
        statusList = ArrayList<Status>()
        AscDesc = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_filter)

        context = this

        // Setup SupportActionBar to over Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        InitView()
    }

    fun InitView() {

        categoryList =
            Gson().fromJson(
                getCategoryList(),
                object : TypeToken<List<Categorys?>?>() {}.getType()
            )
        statusList = Gson().fromJson(
            getStatusList(),
            object : TypeToken<List<Status?>?>() {}.getType()
        )

        rvFilter.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        categoryFilterProductAdapter = CategoryFilterProductAdapter(context, categoryList)
        statusFilterProductAdapter = StatusFilterProductAdapter(context, statusList)

        rvFilter.adapter = categoryFilterProductAdapter

        divCategory.setOnClickListener(this)
        divStatus.setOnClickListener(this)
        divAscDesc.setOnClickListener(this)
        btnApply.setOnClickListener(this)
        btnClear.setOnClickListener(this)

        if (getAscDesc()) {
            rbtnASC.isChecked = true
            rbtnDESC.isChecked = false
        } else {
            rbtnASC.isChecked = false
            rbtnDESC.isChecked = true
        }
    }

    fun onRadioButtonClicked(view: View) {
        when (view.getId()) {
            rbtnASC.id -> {
                setAscDesc(true)
            }
            rbtnDESC.id -> {
                setAscDesc(false)
            }
        }
    }

    override fun onClick(v: View) {
        divCategory.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        divStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        divAscDesc.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))

        if (v.id == divCategory.id) {
            divCategory.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBottom))
            rvFilter.adapter = categoryFilterProductAdapter
            rvFilter.visibility = View.VISIBLE
            rbtnAscDesc.visibility = View.GONE
        }
        if (v.id == divStatus.id) {
            divStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBottom))
            rvFilter.adapter = statusFilterProductAdapter
            rvFilter.visibility = View.VISIBLE
            rbtnAscDesc.visibility = View.GONE
        }

        if (v.id == divAscDesc.id) {
            divAscDesc.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBottom))
            rvFilter.visibility = View.GONE
            rbtnAscDesc.visibility = View.VISIBLE
        }

        if (v.id == btnApply.id) {
            val result = intent
            result.putExtra("IsRefresh", true)
            setResult(Activity.RESULT_OK, result)
            finish()
        }
        if (v.id == btnClear.id) {
            onRadioButtonClicked(rbtnASC)

            categoryList.forEach {
                it.is_select = false
            }

            statusList.forEach {
                it.is_select = false
            }

            setCategoryList(Gson().toJson(categoryList))
            setStatusList(Gson().toJson(statusList))

            categoryFilterProductAdapter = CategoryFilterProductAdapter(context, categoryList)
            statusFilterProductAdapter = StatusFilterProductAdapter(context, statusList)

            onClick(divCategory)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        setResult(Activity.RESULT_CANCELED)
        onBackPressed()
        return true
    }

}
