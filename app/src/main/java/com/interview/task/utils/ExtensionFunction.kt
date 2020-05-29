package com.interview.task.utils

import android.annotation.SuppressLint
import android.app.*
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.interview.task.R
import java.text.SimpleDateFormat
import java.util.*


val INTENT_DATA = "INTENT_DATA"
val ACTIVITY_RESULT = 500

var datePickerDialog: DatePickerDialog? = null
var mTimePicker: TimePickerDialog? = null

lateinit var mcurrentTime: Calendar
lateinit var dateFormatter: SimpleDateFormat

var hour: Int = 0;
var minute: Int = 0

var AMPM = "AM"

fun Context.GetDate(
    txtDate: EditText
) {
    if (datePickerDialog != null) {
        if (datePickerDialog!!.isShowing) {
            return
        }
    }
    dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    val newCalendar = Calendar.getInstance()
    datePickerDialog = DatePickerDialog(
        this,
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val newDate = Calendar.getInstance()
            newDate[year, monthOfYear] = dayOfMonth
            txtDate.setText(dateFormatter.format(newDate.time))
        },
        newCalendar[Calendar.YEAR],
        newCalendar[Calendar.MONTH],
        newCalendar[Calendar.DAY_OF_MONTH]
    )
    datePickerDialog!!.show()
}

fun Context.GetTime(txtTime: EditText) {
    if (mTimePicker != null) {
        if (mTimePicker!!.isShowing) {
            return
        }
    }

    mcurrentTime = Calendar.getInstance()
    hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
    minute =
        mcurrentTime.get(Calendar.MINUTE)
    mTimePicker = TimePickerDialog(
        this,
        OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
            var selectedHour = selectedHour
            if (selectedHour > 12) {
                selectedHour -= 12
                AMPM = "PM"
            } else {
                AMPM = "AM"
            }
            txtTime.setText(
                String.format(
                    "%02d",
                    selectedHour
                ) + ":" + String.format(
                    "%02d",
                    selectedMinute
                ) + ":00 " + AMPM
            )
        },
        hour,
        minute,
        false
    ) //Yes 24 hour time

    //mTimePicker.setTitle("Select Time");
    val text = TextView(this)
    text.text = "Select Time"
    text.gravity = Gravity.CENTER
    text.setPadding(0, 40, 0, 40)
    text.textSize = 20f
    text.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
    text.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
    mTimePicker!!.setCustomTitle(text)
    mTimePicker!!.show()
}


/**
 * Check internet connection.
 *
 * @return  boolean value for internet connection.
 */
fun Context.CheckInternetConnectionAvailable(): Boolean {
    if (CheckInternetConnection()) {
        Log.d("Network", "Connected")
        return true
    } else {
        CheckNetworkConnectionDialog(
            resources.getString(R.string.no_connection),
            resources.getString(R.string.turn_on_connection)
        )
        Log.d("Network", "Not Connected")
        return false
    }
}

/**
 * Check internet connection.
 *
 * @return  boolean value for internet connection.
 */
fun Context.CheckInternetConnection(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    val isConnected = activeNetwork != null && activeNetwork.isConnected
    if (isConnected) {
        return true
    } else {
        return false
    }
}

/**
 * To display alert dialog when no network available.
 *
 * @param title display title for alert dialog.
 * @param msg display msg for alert dialog.
 */
fun Context.CheckNetworkConnectionDialog(title: String, msg: String) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(msg)
    builder.setNegativeButton(getString(R.string.ok)) { dialog, which -> dialog.dismiss() }
    val alertDialog = builder.create()
    alertDialog.show()
}

/**
 * To display toast in application
 *
 * @param  msg display msg for toast.
 */
fun Context.ToastMessage(message: String) {
    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
}

/**
 * Load Image into
 * @ImageView
 * using Glide
 * */
fun Context.LoadImage(imgUrl: String, img: ImageView) {
    Glide.with(this)
        .load(imgUrl)
        .placeholder(R.drawable.ic_loading_image)
        .error(R.drawable.ic_no_image)
        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
        .centerCrop()
        .into(img)
}


fun Activity.NewIntent(
    ourClass: Class<*>,
    isAnimation: Boolean,
    isFinish: Boolean
) {
    val intent = Intent(this, ourClass)
    startActivity(intent)
    if (isAnimation) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    } else {
        overridePendingTransition(R.anim.animation_one, R.anim.animation_two)
    }
    if (isFinish) {
        finish()
    }
}

fun Activity.NewIntentWithData(
    ourClass: Class<*>,
    hashMap: HashMap<String, Any>,
    isAnimation: Boolean,
    isFinish: Boolean
) {
    val intent = Intent(this, ourClass)
    intent.putExtra(INTENT_DATA, hashMap)
    startActivity(intent)
    if (isAnimation) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    } else {
        overridePendingTransition(R.anim.animation_one, R.anim.animation_two)
    }
    if (isFinish) {
        finish()
    }
}

fun Activity.NewIntentWithDataActivityResult(
    ourClass: Class<*>,
    hashMap: HashMap<String, Any>,
    isAnimation: Boolean,
    isFinish: Boolean
) {
    val intent = Intent(this, ourClass)
    intent.putExtra(INTENT_DATA, hashMap)
    startActivityForResult(intent, ACTIVITY_RESULT)
    if (isAnimation) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    } else {
        overridePendingTransition(R.anim.animation_one, R.anim.animation_two)
    }
    if (isFinish) {
        finish()
    }
}

fun Context.vectorToBitmap(drawableId: Int): Bitmap? {
    val drawable = getDrawable(this, drawableId) ?: return null
    val bitmap = createBitmap(
        drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    ) ?: return null
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

var SpinKitProgressDialog: Dialog? = null

fun Context.showProgressDialog() {
    if (SpinKitProgressDialog != null) {
        if (SpinKitProgressDialog!!.isShowing()) {
            return
        }
    }
    SpinKitProgressDialog = Dialog(this)
    SpinKitProgressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
    SpinKitProgressDialog!!.getWindow()!!
        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    SpinKitProgressDialog!!.setContentView(R.layout.progress)
    SpinKitProgressDialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    SpinKitProgressDialog!!.setCancelable(false)
    val lp = WindowManager.LayoutParams()
    lp.copyFrom(SpinKitProgressDialog!!.getWindow()!!.getAttributes())
    lp.width = WindowManager.LayoutParams.WRAP_CONTENT
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT
    SpinKitProgressDialog!!.show()
    SpinKitProgressDialog!!.getWindow()!!.setAttributes(lp)
}

fun Context.hideProgressDialog() {
    if (SpinKitProgressDialog != null && SpinKitProgressDialog!!.isShowing()) {
        SpinKitProgressDialog!!.dismiss()
        SpinKitProgressDialog!!.cancel()
        SpinKitProgressDialog = null
    }
}

