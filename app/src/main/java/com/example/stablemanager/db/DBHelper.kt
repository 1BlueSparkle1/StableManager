package com.example.stablemanager.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.stablemanager.Components.AuthManager
import org.mindrot.jbcrypt.BCrypt

class DBHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "horse_club", factory, 4) {
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
            ban INTEGER DEFAULT 0
        )
    """.trimIndent()
        db!!.execSQL(queryOwners)

        val queryStable = """
        CREATE TABLE stables (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT NOT NULL,
            description TEXT,
            ownerId INTEGER,
            FOREIGN KEY (ownerId) REFERENCES owners(id)
        )
    """.trimIndent()
        db.execSQL(queryStable)

        val queryRole = """
        CREATE TABLE roles (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT NOT NULL
        )
    """.trimIndent()
        db.execSQL(queryRole)

        val queryEmployee = """
        CREATE TABLE employees (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            surname TEXT NOT NULL,
            name TEXT NOT NULL,
            patronymic TEXT,
            email TEXT UNIQUE NOT NULL,
            login TEXT UNIQUE NOT NULL,
            password TEXT NOT NULL,
            roleId INTEGER,
            dateOfBirth TEXT,
            salary REAL DEFAULT 0.0 CHECK (salary >= 0.0),
            stableId INTEGER,
            imageProfile BLOB,
            FOREIGN KEY (roleId) REFERENCES roles(id),
            FOREIGN KEY (stableId) REFERENCES stables(id)
        )
    """.trimIndent()
        db.execSQL(queryEmployee)

        val queryTypeBreed = """
        CREATE TABLE type_breeds (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT NOT NULL
        )
    """.trimIndent()
        db.execSQL(queryTypeBreed)

        val queryBreed = """
        CREATE TABLE breeds (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT NOT NULL,
            typeBreedId INTEGER,
            FOREIGN KEY (typeBreedId) REFERENCES type_breeds(id)
        )
    """.trimIndent()
        db.execSQL(queryBreed)

        val queryGenderHorse = """
        CREATE TABLE gender_horses (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT NOT NULL
        )
    """.trimIndent()
        db.execSQL(queryGenderHorse)

        val queryHorse = """
        CREATE TABLE horses (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            moniker TEXT NOT NULL,
            dateOfBirth TEXT NOT NULL,
            healthStatus TEXT,
            genderId INTEGER,
            breedId INTEGER,
            stableId INTEGER,
            imageProfile BLOB,
            FOREIGN KEY (genderId) REFERENCES gender_horses(id),
            FOREIGN KEY (breedId) REFERENCES breeds(id),
            FOREIGN KEY (stableId) REFERENCES stables(id)
        )
    """.trimIndent()
        db.execSQL(queryHorse)

        val queryFeed = """
        CREATE TABLE feeds (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT NOT NULL,
            quantity REAL DEFAULT 0.0 CHECK (quantity >= 0.0)
        )
    """.trimIndent()
        db.execSQL(queryFeed)

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

    fun authOwner(context: Context, login: String, passwordAttempt: String): Int? {
        val db = this.readableDatabase
        var result: Cursor? = null
        try {
            result = db.rawQuery("SELECT id, password FROM owners WHERE login = ?", arrayOf(login))
            if (result.moveToFirst()) {
                val passwordColumnIndex = result.getColumnIndex("password")
                val idColumnIndex = result.getColumnIndex("id")
                if (passwordColumnIndex == -1 || idColumnIndex == -1) {
                    Log.e("Database", "Один или несколько столбцов не найдены в результирующем наборе данных!")
                    return null
                }

                val hashedPassword = result.getString(passwordColumnIndex)
                val userId = result.getInt(idColumnIndex)

                val authManager = AuthManager(context)
                authManager.saveUserId(userId)

                if (BCrypt.checkpw(passwordAttempt, hashedPassword)) {
                    Log.d("Authentication", "Пароль верен!")
                    return userId
                } else {
                    Log.d("Authentication", "Неверный пароль!")
                    return null
                }
            } else {
                Log.d("Authentication", "Пользователь с логином $login не найден")
                return null
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при проверке пароля: ${e.message}")
            return null
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

    fun getOwnerById(userId: Int): Owner?{
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT surname, name, patronymic, email, login, password, ban FROM owners WHERE id = ?", arrayOf(userId.toString()))
            if (cursor.moveToFirst()) {
                val surnameColumnIndex = cursor.getColumnIndex("surname")
                val nameColumnIndex = cursor.getColumnIndex("name")
                val patronymicColumnIndex = cursor.getColumnIndex("patronymic")
                val emailColumnIndex = cursor.getColumnIndex("email")
                val loginColumnIndex = cursor.getColumnIndex("login")
                val passwordColumnIndex = cursor.getColumnIndex("password")
                val banColumnIndex = cursor.getColumnIndex("ban")

                if (surnameColumnIndex == -1 || nameColumnIndex == -1 || emailColumnIndex == -1 || patronymicColumnIndex == -1 || loginColumnIndex == -1 || passwordColumnIndex == -1 || banColumnIndex == -1) {
                    Log.e("Database", "Один или несколько столбцов не найдены в таблице owners!")
                    return null
                }

                val surname = cursor.getString(surnameColumnIndex)
                val name = cursor.getString(nameColumnIndex)
                val patronymic = cursor.getString(patronymicColumnIndex)
                val email = cursor.getString(emailColumnIndex)
                val login = cursor.getString(loginColumnIndex)
                val password = cursor.getString(passwordColumnIndex)
                val ban = cursor.getInt(banColumnIndex)

                return if(ban == 1){
                    Owner(surname, name, patronymic, email, login, password, true)
                } else{
                    Owner(surname, name, patronymic, email, login, password, false)
                }

            } else {
                Log.d("Database", "Владелец с ID $userId не найден")
                return null
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при получении владельца: ${e.message}")
            return null
        } finally {
            cursor?.close()
        }
    }

    fun updateOwner(surname: String, name: String, patronymic: String, email: String, login: String): Boolean{
        val db = this.readableDatabase
        try {
            val values = ContentValues().apply {
                put("surname", surname)
                put("name", name)
                put("patronymic", patronymic)
                put("email", email)
                put("login", login)
            }

            val rowsAffected = db.update(
                "owners",
                values,
                "login = ?",
                arrayOf(login)
            )

            if (rowsAffected > 0) {
                Log.d("Database", "Владелец $login успешно обновлен.")
                return true
            } else {
                Log.w("Database", "Владелец $login не найден для обновления.")
                return false
            }

        } catch (e: Exception) {
            Log.e("Database", "Ошибка при обновлении владельца: ${e.message}")
            return false
        }
    }

    fun doesOwnerExist(userId: Int, login: String, email: String): Boolean {
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            val query = "SELECT 1 FROM owners WHERE (login = ? OR email = ?) AND id != ?"
            cursor = db.rawQuery(query, arrayOf(login, email, userId.toString()))

            val exists = cursor.count > 0
            Log.d("Database", "Проверка существования владельца с логином '$login' или email '$email': $exists")
            return exists

        } catch (e: Exception) {
            Log.e("Database", "Ошибка при проверке существования владельца: ${e.message}")
            return true
        } finally {
            cursor?.close()
        }
    }

    fun getIdStable(title: String, description: String, ownerId: Int): Int?{
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            val query = "SELECT id FROM stables WHERE title = ? AND description = ? AND ownerId = ?"
            cursor = db.rawQuery(query, arrayOf(title, description, ownerId.toString()))
            if (cursor.moveToFirst()) {
                val idColumnIndex = cursor.getColumnIndex("id")

                if (idColumnIndex == -1 ) {
                    Log.e("Database", "Один или несколько столбцов не найдены в таблице stables!")
                    return null
                }

                val id = cursor.getInt(idColumnIndex)
                return id
            }
            else {
                Log.d("Database", "Конюшня не найдена")
                return null
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при проверке существования владельца: ${e.message}")
            return null
        } finally {
            cursor?.close()
        }
    }

    fun getStableById(stableId: Int): Stable?{
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT title, description, ownerId FROM stables WHERE id = ?", arrayOf(stableId.toString()))
            if (cursor.moveToFirst()) {
                val titleColumnIndex = cursor.getColumnIndex("title")
                val descriptionColumnIndex = cursor.getColumnIndex("description")
                val ownerIdColumnIndex = cursor.getColumnIndex("ownerId")

                if (titleColumnIndex == -1 || descriptionColumnIndex == -1 || ownerIdColumnIndex == -1 ) {
                    Log.e("Database", "Один или несколько столбцов не найдены в таблице stables!")
                    return null
                }

                val title = cursor.getString(titleColumnIndex)
                val description = cursor.getString(descriptionColumnIndex)
                val ownerId = cursor.getInt(ownerIdColumnIndex)


                return Stable(title, description, ownerId)

            } else {
                Log.d("Database", "Конюшня с ID $stableId не найдена")
                return null
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при получении конюшни: ${e.message}")
            return null
        } finally {
            cursor?.close()
        }
    }

    fun updateStable(id: Int, title: String, description: String, ownerId: Int): Boolean{
        val db = this.readableDatabase
        try {
            val values = ContentValues().apply {
                put("title", title)
                put("description", description)
                put("ownerId", ownerId)
            }

            val rowsAffected = db.update(
                "stables",
                values,
                "id = ?",
                arrayOf(id.toString())
            )

            if (rowsAffected > 0) {
                Log.d("Database", "Конюшня успешно обновлена.")
                return true
            } else {
                Log.w("Database", "Конюшня не найдена для обновления.")
                return false
            }

        } catch (e: Exception) {
            Log.e("Database", "Ошибка при обновлении конюшни: ${e.message}")
            return false
        }
    }

    fun updatePassword(login: String, newPass: String): Boolean{
        val db = this.readableDatabase
        try {
            val values = ContentValues().apply {
                put("password", newPass)
            }

            val rowsAffected = db.update(
                "owners",
                values,
                "login = ?",
                arrayOf(login)
            )

            if (rowsAffected > 0) {
                Log.d("Database", "Пароль успешно обновлен.")
                return true
            } else {
                Log.w("Database", "Владелец не найден для обновления.")
                return false
            }

        } catch (e: Exception) {
            Log.e("Database", "Ошибка при обновлении пароля: ${e.message}")
            return false
        }
    }

    fun getRoles(): List<Role>{
        val db = this.readableDatabase
        val roles = mutableListOf<Role>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT id, title FROM roles", null)
            if (cursor.moveToFirst()) {
                do {
                    val idColumnIndex = cursor.getColumnIndex("id")
                    val titleColumnIndex = cursor.getColumnIndex("title")

                    if (idColumnIndex == -1 || titleColumnIndex == -1) {
                        Log.e("Database", "Один или несколько столбцов не найдены!")
                        return emptyList()
                    }

                    val title = cursor.getString(titleColumnIndex)
                    val id = cursor.getInt(idColumnIndex)

                    val role = Role(id, title)
                    roles.add(role)

                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при получении данных из roles: ${e.message}")
        } finally {
            cursor?.close()
        }

        return roles
    }

    fun addRole(role: Role){
        val values = ContentValues()
        values.put("title", role.title)

        val db = this.writableDatabase
        db.insert("roles", null, values)

        db.close()
    }

}