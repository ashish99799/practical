package com.interview.task.data.responses.login

import com.interview.task.data.responses.login.Errors
import com.interview.task.data.responses.login.UserData

data class ResponseData(
    var message: String? = "",
    var data: UserData? = null,
    var errors: Errors? = null
)