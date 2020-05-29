package com.interview.task.ui.view

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.interview.task.R
import com.interview.task.utils.NewIntent
import com.interview.task.utils.SharedUtill.getLoginToken

class SplashActivity : AppCompatActivity() {

    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        handler = Handler()

        handler.postDelayed({
            if (TextUtils.isEmpty(getLoginToken())) {
                NewIntent(LoginActivity::class.java, true, true)
            } else {
                NewIntent(ProductActivity::class.java, true, true)
            }
        }, 3000)
    }

}
