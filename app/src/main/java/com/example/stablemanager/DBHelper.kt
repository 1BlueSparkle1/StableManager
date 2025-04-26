package com.example.stablemanager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "horse_club", factory, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE owners (\n" +
                "    ownerId INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    surname TEXT NOT NULL,\n" +
                "    name TEXT NOT NULL,\n" +
                "    patronymic TEXT,\n" +
                "    email TEXT UNIQUE NOT NULL,\n" +
                "    login TEXT UNIQUE NOT NULL,\n" +
                "    password TEXT NOT NULL,\n" +
                "    ban INTEGER DEFAULT 0,\n" +
                "    Image BLOB\n" +
                ")"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS owners")
        onCreate(db)
    }

    fun addOwner(owner: Owner){
        val values = ContentValues()
        values.put("surname", owner.surname)
        values.put("name", owner.fullname)
        values.put("patronymic", owner.patronymic)
        values.put("email", owner.email)
        values.put("login", owner.login)
        values.put("password", owner.pass)
        values.put("ban", 0)

        val db = this.writableDatabase
        db.insert("owners", null, values)

        db.close()
    }

    fun getOwner(login: String, pass:String): Boolean {
        val db = this.readableDatabase

        val result = db.rawQuery("SELECT * FROM owners WHERE login = '$login' AND password = '$pass'", null)
        return result.moveToFirst()

    }

}