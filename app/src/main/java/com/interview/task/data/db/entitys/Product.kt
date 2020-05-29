package com.interview.task.data.db.entitys

data class Product(
    var id: Int? = 0,
    var title: String? = null,
    var sub_items: String? = null,
    var description: String? = null,
    var status: String? = null,
    var category: String? = null,
    var time: String? = null,
    var date: String? = null
)