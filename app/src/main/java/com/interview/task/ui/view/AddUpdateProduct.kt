package com.interview.task.ui.view

import android.app.Activity
import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.interview.task.R
import com.interview.task.data.adapters.CategorySpinnerAdapter
import com.interview.task.data.adapters.StatusSpinnerAdapter
import com.interview.task.data.adapters.SubProductAdapter
import com.interview.task.data.db.AppDatabase
import com.interview.task.data.db.entitys.Product
import com.interview.task.data.responses.category.Categorys
import com.interview.task.data.responses.status.Status
import com.interview.task.notification.MyNotificationPublisher
import com.interview.task.notification.MyNotificationPublisher.Companion.DEFAULT_NOTIFICATION_CHANNEL_ID
import com.interview.task.notification.MyNotificationPublisher.Companion.NOTIFICATION_CHANNEL_ID
import com.interview.task.notification.MyNotificationPublisher.Companion.NOTIFICATION_ID
import com.interview.task.ui.listeners.AddUpdateProductActivityListener
import com.interview.task.ui.view_model.AddUpdateProductActivityViewModel
import com.interview.task.utils.*
import com.interview.task.utils.SharedUtill.getCategoryList
import com.interview.task.utils.SharedUtill.getStatusList
import kotlinx.android.synthetic.main.add_update_product.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddUpdateProduct : AppCompatActivity(), View.OnClickListener,
    AddUpdateProductActivityListener {

    lateinit var context: Context

    lateinit var db: AppDatabase

    lateinit var hashMap: HashMap<String, Any>

    lateinit var viewModel: AddUpdateProductActivityViewModel

    lateinit var product: Product

    lateinit var subProductAdapter: SubProductAdapter
    lateinit var subItemList: ArrayList<String>

    lateinit var categoryList: ArrayList<Categorys>
    lateinit var categorySpinnerAdapter: CategorySpinnerAdapter

    lateinit var statusList: ArrayList<Status>
    lateinit var statusSpinnerAdapter: StatusSpinnerAdapter

    var IsAdd: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_update_product)

        context = this

        // Setup SupportActionBar to over Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        db = AppDatabase(context)

        InitView()
    }

    fun InitView() {
        viewModel = ViewModelProvider(this).get(AddUpdateProductActivityViewModel::class.java)
        viewModel.addUpdateProductActivityListener = this

        hashMap = intent.getSerializableExtra(INTENT_DATA) as (HashMap<String, Any>)

        IsAdd = hashMap.get("IsAdd") as Boolean

        categoryList =
            Gson().fromJson(
                getCategoryList(),
                object : TypeToken<List<Categorys?>?>() {}.getType()
            )
        statusList = Gson().fromJson(
            getStatusList(),
            object : TypeToken<List<Status?>?>() {}.getType()
        )

        txtDate.setOnClickListener(this)
        txtTime.setOnClickListener(this)
        btnAdd.setOnClickListener(this)
        imgAddSubItems.setOnClickListener(this)

        categorySpinnerAdapter = CategorySpinnerAdapter(context, categoryList)
        spinCategory.setAdapter(categorySpinnerAdapter)
        spinCategory.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                txtCategory.setText(categoryList[position].title)
            }
        })

        statusSpinnerAdapter = StatusSpinnerAdapter(context, statusList)
        spinStatus.setAdapter(statusSpinnerAdapter)
        spinStatus.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                txtStatus.setText(statusList[position].title)
            }
        })

        if (IsAdd) {
            supportActionBar?.title = "New Product"
            btnAdd.text = "Add"
            product = Product()
            subItemList = ArrayList<String>()
        } else {
            supportActionBar?.title = "Update Product"
            btnAdd.text = "Update"
            product = Gson().fromJson(hashMap.get("Product").toString(), Product::class.java)
            subItemList = Gson().fromJson(
                product.sub_items!!.toString(),
                object : TypeToken<List<String>>() {}.getType()
            )
            IsUpdate()
        }

        rvSubItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        subProductAdapter = SubProductAdapter(subItemList)
        rvSubItems.adapter = subProductAdapter

    }

    fun IsUpdate() {
        txtTitle.setText(product.title ?: "")
        txtDate.setText(product.date ?: "")
        txtTime.setText(product.time ?: "")
        txtDescription.setText(product.description ?: "")
        txtCategory.setText(product.category ?: "")
        txtStatus.setText(product.status ?: "")

        for (i in 0..categoryList.size) {
            if (categoryList[i].title.equals(product.category ?: "")) {
                spinCategory.setSelection(i)
                break
            }
        }
        for (i in 0..statusList.size) {
            if (statusList[i].title.equals(product.status ?: "")) {
                spinStatus.setSelection(i)
                break
            }
        }

    }

    override fun onClick(v: View) {
        if (v.id == txtDate.id) {
            GetDate(txtDate)
        }
        if (v.id == txtTime.id) {
            GetTime(txtTime)
        }
        if (v.id == imgAddSubItems.id) {
            if (TextUtils.isEmpty(txtSubItem.text.toString().trim())) {
                txtSubItem.error = "Please enter sub item"
                return
            } else {
                txtSubItem.error = null
            }
            subProductAdapter.addSubProduct(txtSubItem.text.toString().trim())

            txtSubItem.setText("")
        }
        if (v.id == btnAdd.id) {
            if (TextUtils.isEmpty(txtTitle.text.toString().trim())) {
                txtTitle.error = "Please enter title"
                return
            } else {
                txtTitle.error = null
            }
            if (TextUtils.isEmpty(txtDate.text.toString().trim())) {
                txtDate.error = "Please select date"
                return
            } else {
                txtDate.error = null
            }
            if (TextUtils.isEmpty(txtTime.text.toString().trim())) {
                txtTime.error = "Please select time"
                return
            } else {
                txtTime.error = null
            }
            if (TextUtils.isEmpty(txtTime.text.toString().trim())) {
                txtTime.error = "Please select time"
                return
            } else {
                txtTime.error = null
            }
            if (subItemList.isEmpty()) {
                txtSubItem.error = "Please enter sub item"
                return
            } else {
                txtSubItem.error = null
            }

            product.title = txtTitle.text.toString().trim()
            product.date = txtDate.text.toString().trim()
            product.time = txtTime.text.toString().trim()
            product.sub_items = subItemList.toString()
            product.category = txtCategory.text.toString().trim()
            product.status = txtStatus.text.toString().trim()
            product.description = txtDescription.text.toString().trim()

            if (IsAdd) {
                if (db.insertProduct(product)) {
                    ToastMessage("Product inserted successfully")

                    val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.US)
                    var customTime: Date? = null
                    try {
                        customTime = sdf.parse(
                            txtDate.text.toString().trim() + " " + txtTime.text.toString().trim()
                        )

                       val dilay = customTime.time

                        scheduleNotification(
                            getNotification(product.title ?: ""),
                            dilay
                        );

                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }

                    val result = intent
                    result.putExtra("IsRefresh", true)
                    setResult(Activity.RESULT_OK, result)
                    finish()
                } else {
                    ToastMessage("Oops!! Please try after some time")
                }
            } else {
                if (db.updateProduct(product)) {
                    ToastMessage("Product updated successfully")
                    val result = intent
                    result.putExtra("IsRefresh", true)
                    setResult(Activity.RESULT_OK, result)
                    finish()
                } else {
                    ToastMessage("Oops!! Please try after some time")
                }
            }
        }
    }

    fun getNotification(ContentText: String): Notification {
        val builder = NotificationCompat.Builder(this, DEFAULT_NOTIFICATION_CHANNEL_ID)
        builder.setContentTitle("Scheduled Notification")
        builder.setContentText(ContentText)
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
        builder.setAutoCancel(true)
        builder.setChannelId(NOTIFICATION_CHANNEL_ID)
        return builder.build()
    }

    fun scheduleNotification(
        notification: Notification,
        delay: Long
    ) {
        val notificationIntent = Intent(this, MyNotificationPublisher::class.java)
        notificationIntent.putExtra(NOTIFICATION_ID, Random(1000))
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager =
            (getSystemService(Context.ALARM_SERVICE) as AlarmManager)
        alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, delay] = pendingIntent
    }

    override fun onSupportNavigateUp(): Boolean {
        setResult(Activity.RESULT_CANCELED)
        onBackPressed()
        return true
    }

    override fun showProgress() {
        showProgressDialog()
    }

    override fun hideProgress() {
        hideProgressDialog()
    }

    override fun onSuccess(message: String) {
        ToastMessage(message)
        onBackPressed()
    }

    override fun onFailure(message: String) {
        ToastMessage(message)
    }

}
