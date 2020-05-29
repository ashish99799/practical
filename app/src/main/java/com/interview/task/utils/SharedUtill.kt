package com.interview.task.utils

import android.content.Context
import android.content.SharedPreferences
import com.interview.task.R

object SharedUtill {

    internal val LoginToken = "LoginToken"
    internal val CategoryList = "CategoryList"
    internal val StatusList = "StatusList"
    internal val AscDesc = "AscDesc"

    internal fun getSharedPreferences(ctx: Context): SharedPreferences {
        return ctx.getSharedPreferences(ctx.getString(R.string.app_name), Context.MODE_PRIVATE)
    }

    fun Context.setLoginToken(data: String) {
        val editor = getSharedPreferences(this).edit()
        editor.putString(LoginToken, data)
        editor.commit()
    }

    fun Context.getLoginToken(): String? {
        return getSharedPreferences(this).getString(LoginToken, "")
    }

    fun Context.setCategoryList(data: String) {
        val editor = getSharedPreferences(this).edit()
        editor.putString(CategoryList, data)
        editor.commit()
    }

    fun Context.getCategoryList(): String? {
        return getSharedPreferences(this).getString(CategoryList, "")
    }

    fun Context.setStatusList(data: String) {
        val editor = getSharedPreferences(this).edit()
        editor.putString(StatusList, data)
        editor.commit()
    }

    fun Context.getStatusList(): String? {
        return getSharedPreferences(this).getString(StatusList, "")
    }

    fun Context.setAscDesc(data: Boolean) {
        val editor = getSharedPreferences(this).edit()
        editor.putBoolean(AscDesc, data)
        editor.commit()
    }

    fun Context.getAscDesc(): Boolean {
        return getSharedPreferences(this).getBoolean(AscDesc, true)
    }

    fun Context.ClearData() {
        val editor = getSharedPreferences(this).edit()
        editor.clear() //clear all stored data
        editor.commit()
    }

}