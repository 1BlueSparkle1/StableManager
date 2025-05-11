package com.example.stablemanager

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.mindrot.jbcrypt.BCrypt

class DBHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "horse_club", factory, 3) {
    override fun onCreate(db: SQLiteDatabase?) {
        val queryOwners = """
        CREATE TABLE owners (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            surname TEXT NOT NULL,
            name TEXT NOT NULL,
            patronymic TEXT,
            email TEXT UNIQUE NOT NULL,
            login TEXT UNIQUE NOT NULL,
            password TEXT NOT NULL,
            ban INTEGER DEFAULT 0,
            Image BLOB
        )
    """.trimIndent()
        db!!.execSQL(queryOwners)

        val queryStable = """
        CREATE TABLE stables (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT NOT NULL,
            description TEXT,
            ownerId INTEGER,
            FOREIGN KEY (ownerId) REFERENCES owners(Id)
        )
    """.trimIndent()
        db.execSQL(queryStable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS owners")
        db.execSQL("DROP TABLE IF EXISTS stables")
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

    fun getOwner(context: Context, login: String, passwordAttempt: String): Boolean {
        val db = this.readableDatabase
        var result: Cursor? = null
        try {
            result = db.rawQuery("SELECT id, password FROM owners WHERE login = ?", arrayOf(login))
            if (result.moveToFirst()) {
                val passwordColumnIndex = result.getColumnIndex("password")
                val idColumnIndex = result.getColumnIndex("id")
                if (passwordColumnIndex == -1 || idColumnIndex == -1) {
                    Log.e("Database", "Один или несколько столбцов не найдены в результирующем наборе данных!")
                    return false
                }

                val hashedPassword = result.getString(passwordColumnIndex)
                val userId = result.getInt(idColumnIndex)

                val authManager = AuthManager(context)
                authManager.saveUserId(userId)

                if (BCrypt.checkpw(passwordAttempt, hashedPassword)) {
                    Log.d("Authentication", "Пароль верен!")
                    return true
                } else {
                    Log.d("Authentication", "Неверный пароль!")
                    return false
                }
            } else {
                Log.d("Authentication", "Пользователь с логином $login не найден")
                return false
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при проверке пароля: ${e.message}")
            return false
        } finally {
            result?.close()
        }
    }

    fun getStables(userId: Int): List<Stable>{
        val db = this.readableDatabase
        val stables = mutableListOf<Stable>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT title, description, ownerId FROM stables WHERE ownerId = ?", arrayOf(userId.toString()))
            if (cursor.moveToFirst()) {
                do {
                    val titleColumnIndex = cursor.getColumnIndex("title")
                    val descriptionColumnIndex = cursor.getColumnIndex("description")
                    val ownerIdColumnIndex = cursor.getColumnIndex("ownerId")

                    if (titleColumnIndex == -1 || descriptionColumnIndex == -1 || ownerIdColumnIndex == -1) {
                        Log.e("Database", "Один или несколько столбцов не найдены!")
                        return emptyList()
                    }

                    val title = cursor.getString(titleColumnIndex)
                    val description = cursor.getString(descriptionColumnIndex)
                    val ownerId = cursor.getInt(ownerIdColumnIndex)

                    val stable = Stable(title, description, ownerId)
                    stables.add(stable)

                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при получении данных из stables: ${e.message}")
        } finally {
            cursor?.close()
        }

        return stables
    }

    fun addStable(stable: Stable){
        val values = ContentValues()
        values.put("title", stable.title)
        values.put("description", stable.description)
        values.put("ownerId", stable.ownerId)

        val db = this.writableDatabase
        db.insert("stables", null, values)

        db.close()
    }

}