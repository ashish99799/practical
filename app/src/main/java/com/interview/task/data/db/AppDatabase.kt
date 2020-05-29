package com.interview.task.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.interview.task.data.db.entitys.Product
import com.interview.task.data.responses.category.Categorys
import com.interview.task.data.responses.status.Status
import com.interview.task.utils.SharedUtill.getAscDesc
import com.interview.task.utils.SharedUtill.getCategoryList
import com.interview.task.utils.SharedUtill.getStatusList
import kotlin.collections.ArrayList

class AppDatabase(context: Context?) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {
        // Database Version
        private const val DATABASE_VERSION = 1

        // Database Name
        private const val DATABASE_NAME = "product_db"
        const val TABLE_NAME = "product"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_SUB_ITEMS = "sub_items"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_STATUS = "status"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_TIME = "time"
        const val COLUMN_DATE = "date"

        // Create table SQL query
        const val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_SUB_ITEMS + " TEXT,"
                + COLUMN_STATUS + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT"
                + ")")
    }

    // Creating Tables
    override fun onCreate(db: SQLiteDatabase) {
        // create notes table
        db.execSQL(CREATE_TABLE)
    }

    // Upgrading database
    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        // Create tables again
        onCreate(db)
    }

    fun insertProduct(product: Product): Boolean {
        // get writable database as we want to write data
        val db = this.writableDatabase
        val values = ContentValues()
        // `id` and `timestamp` will be inserted automatically.
        // no need to add thems
        values.put(COLUMN_TITLE, product.title)
        values.put(COLUMN_DESCRIPTION, product.description)
        values.put(COLUMN_SUB_ITEMS, product.sub_items)
        values.put(COLUMN_STATUS, product.status)
        values.put(COLUMN_CATEGORY, product.category)
        values.put(COLUMN_TIME, product.time)
        values.put(COLUMN_DATE, product.date)

        // insert row
        val id = db.insert(TABLE_NAME, null, values)

        // close db connection
        db.close()

        // return newly inserted row id
        return if (id != (-1).toLong()) true else false
    }

    fun getProduct(id: Long): Product {
        // get readable database as we are not inserting anything
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(
                COLUMN_ID,
                COLUMN_TITLE,
                COLUMN_SUB_ITEMS,
                COLUMN_TIME,
                COLUMN_DATE,
                COLUMN_DESCRIPTION,
                COLUMN_STATUS
            ),
            "$COLUMN_ID=?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            null
        )
        cursor?.moveToFirst()

        // prepare note object
        val product = Product(
            cursor!!.getInt(cursor.getColumnIndex(COLUMN_ID)),
            cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
            cursor.getString(cursor.getColumnIndex(COLUMN_SUB_ITEMS)),
            cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
            cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)),
            cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)),
            cursor.getString(cursor.getColumnIndex(COLUMN_TIME)),
            cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
        )

        // close the db connection
        cursor.close()
        return product
    }

    // Select All Query
    fun allProducts(context: Context): ArrayList<Product> {
        val products: ArrayList<Product> = ArrayList<Product>()

        // Select All Query
        var selectQuery =
            "SELECT  * FROM " + TABLE_NAME + " ORDER BY " +
                    COLUMN_TITLE + " ${if (context.getAscDesc()) "ASC" else "DESC"}"

        val categoryList = (Gson().fromJson(
            context.getCategoryList(),
            object : TypeToken<List<Categorys>>() {}.getType()
        ) as ArrayList<Categorys>).filter { it.is_select == true }

        val statusList = (Gson().fromJson(
            context.getStatusList(),
            object : TypeToken<List<Status>>() {}.getType()
        ) as ArrayList<Status>).filter { it.is_select == true }

        val categoryCont = categoryList.size
        val statusCont = statusList.size

        if (categoryCont != 0 && statusCont != 0) {

            var categorys = ArrayList<String>()
            categoryList.forEach {
                categorys.add("'" + it.title + "'")
            }

            var status = ArrayList<String>()
            statusList.forEach {
                status.add("'" + it.title + "'")
            }

            selectQuery =
                "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_CATEGORY + " IN (" + categorys.toString()
                    .replace("[", "").replace("]", "") + ") AND " +
                        COLUMN_STATUS + " IN (" + status.toString()
                    .replace("[", "").replace("]", "") + ") ORDER BY " +
                        COLUMN_TITLE + " ${if (context.getAscDesc()) "ASC" else "DESC"}"
        } else if (categoryCont != 0) {

            var categorys = ArrayList<String>()
            categoryList.forEach {
                categorys.add("'" + it.title + "'")
            }

            selectQuery =
                "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_CATEGORY + " IN (" + categorys.toString()
                    .replace("[", "").replace("]", "") + ") ORDER BY " +
                        COLUMN_TITLE + " ${if (context.getAscDesc()) "ASC" else "DESC"}"
        } else if (statusCont != 0) {

            var status = ArrayList<String>()
            statusList.forEach {
                status.add("'" + it.title + "'")
            }

            selectQuery =
                "SELECT  * FROM " + TABLE_NAME + " WHERE " +
                        COLUMN_STATUS + " IN (" + status.toString()
                    .replace("[", "").replace("]", "") + ") ORDER BY " +
                        COLUMN_TITLE + " ${if (context.getAscDesc()) "ASC" else "DESC"}"
        }

        //  /data/user/0/com.interview.task/databases/product_db
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                val product = Product()
                product.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                product.title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
                product.sub_items = cursor.getString(cursor.getColumnIndex(COLUMN_SUB_ITEMS))
                product.description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
                product.date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                product.time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME))
                product.status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS))
                product.category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY))
                products.add(product)
            } while (cursor.moveToNext())
        }

        // close db connection
        db.close()

        // return notes list
        return products
    }

    fun updateProduct(product: Product): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_TITLE, product.title)
        values.put(COLUMN_DESCRIPTION, product.description)
        values.put(COLUMN_SUB_ITEMS, product.sub_items)
        values.put(COLUMN_STATUS, product.status)
        values.put(COLUMN_TIME, product.time)
        values.put(COLUMN_DATE, product.date)
        values.put(COLUMN_CATEGORY, product.category)

        // updating row
        var id = db.update(
            TABLE_NAME,
            values,
            "$COLUMN_ID = ?",
            arrayOf(java.lang.String.valueOf(product.id))
        )

        return if (id != (-1).toInt()) true else false
    }

    fun deleteProduct(product: Product): Boolean {
        val db = this.writableDatabase
        var id = db.delete(
            TABLE_NAME,
            "$COLUMN_ID = ?",
            arrayOf(java.lang.String.valueOf(product.id))
        )
        db.close()

        return if (id != (-1).toInt()) true else false
    }

}