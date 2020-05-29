package com.interview.task.ui.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.interview.task.R
import com.interview.task.data.responses.login.ResponseData
import com.interview.task.ui.listeners.LoginActivityListener
import com.interview.task.ui.view_model.LoginActivityViewModel
import com.interview.task.utils.*
import com.interview.task.utils.SharedUtill.setLoginToken
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : AppCompatActivity(), LoginActivityListener, View.OnClickListener {

    lateinit var context: Context
    lateinit var viewModel: LoginActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        context = this

        InitView()
    }

    fun InitView() {

        // Setup ViewModel
        viewModel = ViewModelProvider(this).get(LoginActivityViewModel::class.java)
        viewModel.loginActivityListener = this

        btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v!!.id == btnLogin.id) {
            if (viewModel.firstValidate(
                    txtUserName.text.toString().trim(),
                    txtPassword.text.toString().trim()
                )
            ) {
                viewModel.getSearchUser(
                    context,
                    txtUserName.text.toString().trim(),
                    txtPassword.text.toString().trim()
                )
            }
        }
    }

    override fun showProgress() {
        showProgressDialog()
    }

    override fun hideProgress() {
        hideProgressDialog()
    }

    override fun onSuccess(response: ResponseData) {
        if (response.data != null) {
            response.data!!.token?.let { setLoginToken(it) }
            NewIntent(ProductActivity::class.java, true, true)
        } else {
            response.errors!!.validate?.let { ToastMessage(it) }
        }
    }

    override fun onFailure(message: String) {
        ToastMessage(message)
    }

}
