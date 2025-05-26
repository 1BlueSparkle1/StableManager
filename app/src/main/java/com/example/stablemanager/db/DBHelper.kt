package com.example.stablemanager.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.stablemanager.Components.Managers.AuthManager
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
            quantity REAL DEFAULT 0.0 CHECK (quantity >= 0.0),
            stableId INTEGER,
            FOREIGN KEY (stableId) REFERENCES stables(id)
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

    fun getOwners(): List<Owner>?{
        val db = this.readableDatabase
        val owners = mutableListOf<Owner>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT surname, name, patronymic, email, login, password, ban FROM owners", null)
            if (cursor.moveToFirst()) {
                do {
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

                    val owner = if(ban == 1){
                        Owner(surname, name, patronymic, email, login, password, true)
                    } else{
                        Owner(surname, name, patronymic, email, login, password, false)
                    }

                    owners.add(owner)

                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при получении данных из owners: ${e.message}")
        } finally {
            cursor?.close()
        }

        return owners
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

    fun getAllStables(): List<Stable>{
        val db = this.readableDatabase
        val stables = mutableListOf<Stable>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT title, description, ownerId FROM stables", null)
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

    fun getIdOwner(email: String, login: String): Int?{
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            val query = "SELECT id FROM owners WHERE email = ? AND login = ?"
            cursor = db.rawQuery(query, arrayOf(email, login))
            if (cursor.moveToFirst()) {
                val idColumnIndex = cursor.getColumnIndex("id")

                if (idColumnIndex == -1 ) {
                    Log.e("Database", "Один или несколько столбцов не найдены в таблице owners!")
                    return null
                }

                val id = cursor.getInt(idColumnIndex)
                return id
            }
            else {
                Log.d("Database", "Владелец не найден")
                return null
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при проверке существования владельца: ${e.message}")
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
            Log.e("Database", "Ошибка при проверке существования конюшни: ${e.message}")
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

    fun getRolesById(roleId: Int) : Role?{
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT title FROM roles WHERE id = ?", arrayOf(roleId.toString()))
            if (cursor.moveToFirst()) {
                val titleColumnIndex = cursor.getColumnIndex("title")

                if (titleColumnIndex == -1) {
                    Log.e("Database", "Один или несколько столбцов не найдены в таблице roles!")
                    return null
                }

                val title = cursor.getString(titleColumnIndex)


                return Role(roleId, title)

            } else {
                Log.d("Database", "Роль с ID $roleId не найдена")
                return null
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при получении роли: ${e.message}")
            return null
        } finally {
            cursor?.close()
        }
    }

    fun updateRole(roleId: Int, title: String): Boolean{
        val db = this.readableDatabase
        try {
            val values = ContentValues().apply {
                put("title", title)
            }

            val rowsAffected = db.update(
                "roles",
                values,
                "id = ?",
                arrayOf(roleId.toString())
            )

            if (rowsAffected > 0) {
                Log.d("Database", "Роль успешно обновлена.")
                return true
            } else {
                Log.w("Database", "Роль не найдена для обновления.")
                return false
            }

        } catch (e: Exception) {
            Log.e("Database", "Ошибка при обновлении роли: ${e.message}")
            return false
        }
    }

    fun getIdRole(title: String): Int?{
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            val query = "SELECT id FROM roles WHERE title = ? "
            cursor = db.rawQuery(query, arrayOf(title))
            if (cursor.moveToFirst()) {
                val idColumnIndex = cursor.getColumnIndex("id")

                if (idColumnIndex == -1 ) {
                    Log.e("Database", "Один или несколько столбцов не найдены в таблице roles!")
                    return null
                }

                val id = cursor.getInt(idColumnIndex)
                return id
            }
            else {
                Log.d("Database", "Роль не найдена")
                return null
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при проверке существования роли: ${e.message}")
            return null
        } finally {
            cursor?.close()
        }
    }

    fun getTypeBreed(): List<TypeBreed>{
        val db = this.readableDatabase
        val typeBreeds = mutableListOf<TypeBreed>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT title FROM type_breeds", null)
            if (cursor.moveToFirst()) {
                do {
                    val titleColumnIndex = cursor.getColumnIndex("title")

                    if (titleColumnIndex == -1) {
                        Log.e("Database", "Один или несколько столбцов не найдены!")
                        return emptyList()
                    }

                    val title = cursor.getString(titleColumnIndex)

                    val typeBreed = TypeBreed(title)
                    typeBreeds.add(typeBreed)

                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при получении данных из type_breeds: ${e.message}")
        } finally {
            cursor?.close()
        }

        return typeBreeds
    }

    fun addTypeBreed(typeBreed: TypeBreed){
        val values = ContentValues()
        values.put("title", typeBreed.title)

        val db = this.writableDatabase
        db.insert("type_breeds", null, values)

        db.close()
    }

    fun getTypeBreedById(typeBreedId: Int) : TypeBreed?{
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT title FROM type_breeds WHERE id = ?", arrayOf(typeBreedId.toString()))
            if (cursor.moveToFirst()) {
                val titleColumnIndex = cursor.getColumnIndex("title")

                if (titleColumnIndex == -1) {
                    Log.e("Database", "Один или несколько столбцов не найдены в таблице type_breeds!")
                    return null
                }

                val title = cursor.getString(titleColumnIndex)


                return TypeBreed(title)

            } else {
                Log.d("Database", "Тип породы с ID $typeBreedId не найдена")
                return null
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при получении типа породы: ${e.message}")
            return null
        } finally {
            cursor?.close()
        }
    }

    fun updateTypeBreed(typeBreedId: Int, title: String): Boolean{
        val db = this.readableDatabase
        try {
            val values = ContentValues().apply {
                put("title", title)
            }

            val rowsAffected = db.update(
                "type_breeds",
                values,
                "id = ?",
                arrayOf(typeBreedId.toString())
            )

            if (rowsAffected > 0) {
                Log.d("Database", "Тип породы успешно обновлен.")
                return true
            } else {
                Log.w("Database", "Тип породы не найден для обновления.")
                return false
            }

        } catch (e: Exception) {
            Log.e("Database", "Ошибка при обновлении типа породы: ${e.message}")
            return false
        }
    }

    fun getIdTypeBreed(title: String): Int?{
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            val query = "SELECT id FROM type_breeds WHERE title = ? "
            cursor = db.rawQuery(query, arrayOf(title))
            if (cursor.moveToFirst()) {
                val idColumnIndex = cursor.getColumnIndex("id")

                if (idColumnIndex == -1 ) {
                    Log.e("Database", "Один или несколько столбцов не найдены в таблице type_breeds!")
                    return null
                }

                val id = cursor.getInt(idColumnIndex)
                return id
            }
            else {
                Log.d("Database", "Тип породы не найден")
                return null
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при проверке существования типа породы: ${e.message}")
            return null
        } finally {
            cursor?.close()
        }
    }


    //Здесь начало породы
    fun getBreeds(): List<Breed>{
        val db = this.readableDatabase
        val breeds = mutableListOf<Breed>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT title, typeBreedId FROM breeds", null)
            if (cursor.moveToFirst()) {
                do {
                    val titleColumnIndex = cursor.getColumnIndex("title")
                    val typeBreedIdColumnIndex = cursor.getColumnIndex("typeBreedId")

                    if (titleColumnIndex == -1 || typeBreedIdColumnIndex == -1) {
                        Log.e("Database", "Один или несколько столбцов не найдены!")
                        return emptyList()
                    }

                    val title = cursor.getString(titleColumnIndex)
                    val typeBreedId = cursor.getInt(typeBreedIdColumnIndex)

                    val breed = Breed(title, typeBreedId)
                    breeds.add(breed)

                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при получении данных из type_breeds: ${e.message}")
        } finally {
            cursor?.close()
        }

        return breeds
    }

//    fun addTypeBreed(typeBreed: TypeBreed){
//        val values = ContentValues()
//        values.put("title", typeBreed.title)
//
//        val db = this.writableDatabase
//        db.insert("type_breeds", null, values)
//
//        db.close()
//    }
//
//    fun getTypeBreedById(typeBreedId: Int) : TypeBreed?{
//        val db = this.readableDatabase
//        var cursor: Cursor? = null
//        try {
//            cursor = db.rawQuery("SELECT title FROM type_breeds WHERE id = ?", arrayOf(typeBreedId.toString()))
//            if (cursor.moveToFirst()) {
//                val titleColumnIndex = cursor.getColumnIndex("title")
//
//                if (titleColumnIndex == -1) {
//                    Log.e("Database", "Один или несколько столбцов не найдены в таблице type_breeds!")
//                    return null
//                }
//
//                val title = cursor.getString(titleColumnIndex)
//
//
//                return TypeBreed(title)
//
//            } else {
//                Log.d("Database", "Тип породы с ID $typeBreedId не найдена")
//                return null
//            }
//        } catch (e: Exception) {
//            Log.e("Database", "Ошибка при получении типа породы: ${e.message}")
//            return null
//        } finally {
//            cursor?.close()
//        }
//    }
//
//    fun updateTypeBreed(typeBreedId: Int, title: String): Boolean{
//        val db = this.readableDatabase
//        try {
//            val values = ContentValues().apply {
//                put("title", title)
//            }
//
//            val rowsAffected = db.update(
//                "type_breeds",
//                values,
//                "id = ?",
//                arrayOf(typeBreedId.toString())
//            )
//
//            if (rowsAffected > 0) {
//                Log.d("Database", "Тип породы успешно обновлен.")
//                return true
//            } else {
//                Log.w("Database", "Тип породы не найден для обновления.")
//                return false
//            }
//
//        } catch (e: Exception) {
//            Log.e("Database", "Ошибка при обновлении типа породы: ${e.message}")
//            return false
//        }
//    }

    fun getIdBreed(title: String, typeBreedId: Int): Int?{
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            val query = "SELECT id FROM breeds WHERE title = ? AND typeBreedId = ?"
            cursor = db.rawQuery(query, arrayOf(title, typeBreedId.toString()))
            if (cursor.moveToFirst()) {
                val idColumnIndex = cursor.getColumnIndex("id")

                if (idColumnIndex == -1 ) {
                    Log.e("Database", "Один или несколько столбцов не найдены в таблице breeds!")
                    return null
                }

                val id = cursor.getInt(idColumnIndex)
                return id
            }
            else {
                Log.d("Database", "Порода не найдена")
                return null
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при проверке существования породы: ${e.message}")
            return null
        } finally {
            cursor?.close()
        }
    }

    //здесь начало пола

    fun getGenderHorse(): List<GenderHorse>{
        val db = this.readableDatabase
        val genderHorses = mutableListOf<GenderHorse>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT title FROM gender_horses", null)
            if (cursor.moveToFirst()) {
                do {
                    val titleColumnIndex = cursor.getColumnIndex("title")

                    if (titleColumnIndex == -1) {
                        Log.e("Database", "Один или несколько столбцов не найдены!")
                        return emptyList()
                    }

                    val title = cursor.getString(titleColumnIndex)

                    val genderHorse = GenderHorse(title)
                    genderHorses.add(genderHorse)

                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при получении данных из gender_horses: ${e.message}")
        } finally {
            cursor?.close()
        }

        return genderHorses
    }

    fun addGenderHorse(genderHorse: GenderHorse){
        val values = ContentValues()
        values.put("title", genderHorse.title)

        val db = this.writableDatabase
        db.insert("gender_horses", null, values)

        db.close()
    }

    fun getGenderHorseById(genderHorseId: Int) : GenderHorse?{
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT title FROM gender_horses WHERE id = ?", arrayOf(genderHorseId.toString()))
            if (cursor.moveToFirst()) {
                val titleColumnIndex = cursor.getColumnIndex("title")

                if (titleColumnIndex == -1) {
                    Log.e("Database", "Один или несколько столбцов не найдены в таблице gender_horses!")
                    return null
                }

                val title = cursor.getString(titleColumnIndex)


                return GenderHorse(title)

            } else {
                Log.d("Database", "Порода лошади с ID $genderHorseId не найдена")
                return null
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при получении Породы лошади : ${e.message}")
            return null
        } finally {
            cursor?.close()
        }
    }

    fun updateGenderHorse(genderHorseId: Int, title: String): Boolean{
        val db = this.readableDatabase
        try {
            val values = ContentValues().apply {
                put("title", title)
            }

            val rowsAffected = db.update(
                "gender_horses",
                values,
                "id = ?",
                arrayOf(genderHorseId.toString())
            )

            if (rowsAffected > 0) {
                Log.d("Database", "Пол лошадей успешно обновлен.")
                return true
            } else {
                Log.w("Database", "Пол лошадей не найден для обновления.")
                return false
            }

        } catch (e: Exception) {
            Log.e("Database", "Ошибка при обновлении пола лошадей: ${e.message}")
            return false
        }
    }

    fun getIdGenderHorse(title: String): Int?{
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            val query = "SELECT id FROM gender_horses WHERE title = ? "
            cursor = db.rawQuery(query, arrayOf(title))
            if (cursor.moveToFirst()) {
                val idColumnIndex = cursor.getColumnIndex("id")

                if (idColumnIndex == -1 ) {
                    Log.e("Database", "Один или несколько столбцов не найдены в таблице gender_horses!")
                    return null
                }

                val id = cursor.getInt(idColumnIndex)
                return id
            }
            else {
                Log.d("Database", "Пол лошадей не найден")
                return null
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при проверке существования пола лошадей: ${e.message}")
            return null
        } finally {
            cursor?.close()
        }
    }

    fun getIdEmployee(email: String, login: String): Int?{
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            val query = "SELECT id FROM employees WHERE email = ? AND login = ?"
            cursor = db.rawQuery(query, arrayOf(email, login))
            if (cursor.moveToFirst()) {
                val idColumnIndex = cursor.getColumnIndex("id")

                if (idColumnIndex == -1 ) {
                    Log.e("Database", "Один или несколько столбцов не найдены в таблице employees!")
                    return null
                }

                val id = cursor.getInt(idColumnIndex)
                return id
            }
            else {
                Log.d("Database", "Сотрудник не найден")
                return null
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при проверке существования сотрудника: ${e.message}")
            return null
        } finally {
            cursor?.close()
        }
    }

    fun getAllEmployees(): List<Employee>?{
        val db = this.readableDatabase
        val employees = mutableListOf<Employee>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT surname, name, patronymic, email, login, password, roleId, dateOfBirth, salary, stableId, imageProfile FROM employees", null)
            if (cursor.moveToFirst()) {
                do {
                    val surnameColumnIndex = cursor.getColumnIndex("surname")
                    val nameColumnIndex = cursor.getColumnIndex("name")
                    val patronymicColumnIndex = cursor.getColumnIndex("patronymic")
                    val emailColumnIndex = cursor.getColumnIndex("email")
                    val loginColumnIndex = cursor.getColumnIndex("login")
                    val passwordColumnIndex = cursor.getColumnIndex("password")
                    val roleIdColumnIndex = cursor.getColumnIndex("roleId")
                    val dateOfBirthColumnIndex = cursor.getColumnIndex("dateOfBirth")
                    val salaryColumnIndex = cursor.getColumnIndex("salary")
                    val stableIdColumnIndex = cursor.getColumnIndex("stableId")
                    val imageProfileColumnIndex = cursor.getColumnIndex("imageProfile")

                    if (surnameColumnIndex == -1 || nameColumnIndex == -1 || emailColumnIndex == -1 || patronymicColumnIndex == -1
                        || loginColumnIndex == -1 || passwordColumnIndex == -1 || roleIdColumnIndex == -1 || dateOfBirthColumnIndex == -1
                        || salaryColumnIndex == -1 || stableIdColumnIndex == -1 || imageProfileColumnIndex == -1) {
                        Log.e("Database", "Один или несколько столбцов не найдены в таблице employees!")
                        return null
                    }

                    val surname = cursor.getString(surnameColumnIndex)
                    val name = cursor.getString(nameColumnIndex)
                    val patronymic = cursor.getString(patronymicColumnIndex)
                    val email = cursor.getString(emailColumnIndex)
                    val login = cursor.getString(loginColumnIndex)
                    val password = cursor.getString(passwordColumnIndex)
                    val roleId = cursor.getInt(roleIdColumnIndex)
                    val dateOfBirth = cursor.getString(dateOfBirthColumnIndex)
                    val salary = cursor.getDouble(salaryColumnIndex)
                    val stableId = cursor.getInt(stableIdColumnIndex)
                    val imageProfile = cursor.getBlob(imageProfileColumnIndex)

                    val employee = Employee(surname, name, patronymic, email, login, password, roleId, dateOfBirth, salary, stableId, imageProfile)

                    employees.add(employee)

                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при получении данных из employees: ${e.message}")
        } finally {
            cursor?.close()
        }

        return employees
    }

    fun addEmployee(employee: Employee){
        val values = ContentValues()
        values.put("surname", employee.surname)
        values.put("name", employee.name)
        values.put("patronymic", employee.patronymic)
        values.put("email", employee.email)
        values.put("login", employee.login)
        values.put("password", employee.password)
        values.put("dateOfBirth", employee.dateOfBirth)
        values.put("roleId", employee.roleId)
        values.put("salary", employee.salary)
        values.put("stableId", employee.stableId)
        values.put("imageProfile", employee.imageProfile)

        val db = this.writableDatabase
        db.insert("employees", null, values)

        db.close()
    }

    fun getAllHorses(): List<Horse>{
        val db = this.readableDatabase
        val horses = mutableListOf<Horse>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT moniker, dateOfBirth, healthStatus, genderId, breedId, stableId, imageProfile FROM horses", null)
            if (cursor.moveToFirst()) {
                do {
                    val monikerColumnIndex = cursor.getColumnIndex("moniker")
                    val dateOfBirthColumnIndex = cursor.getColumnIndex("dateOfBirth")
                    val healthStatusColumnIndex = cursor.getColumnIndex("healthStatus")
                    val genderIdColumnIndex = cursor.getColumnIndex("genderId")
                    val breedIdColumnIndex = cursor.getColumnIndex("breedId")
                    val stableIdColumnIndex = cursor.getColumnIndex("stableId")
                    val imageProfileColumnIndex = cursor.getColumnIndex("imageProfile")

                    if (monikerColumnIndex == -1 || dateOfBirthColumnIndex == -1 || healthStatusColumnIndex == -1
                        || genderIdColumnIndex == -1 || breedIdColumnIndex == -1 || stableIdColumnIndex == -1 || imageProfileColumnIndex == -1) {
                        Log.e("Database", "Один или несколько столбцов не найдены!")
                        return emptyList()
                    }

                    val moniker = cursor.getString(monikerColumnIndex)
                    val dateOfBirth = cursor.getString(dateOfBirthColumnIndex)
                    val healthStatus = cursor.getString(healthStatusColumnIndex)
                    val genderId = cursor.getInt(genderIdColumnIndex)
                    val breedId = cursor.getInt(breedIdColumnIndex)
                    val stableId = cursor.getInt(stableIdColumnIndex)
                    val image = cursor.getBlob(imageProfileColumnIndex)

                    val horse = Horse(moniker, dateOfBirth, healthStatus, genderId, breedId, stableId, image)
                    horses.add(horse)

                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при получении данных из stables: ${e.message}")
        } finally {
            cursor?.close()
        }

        return horses
    }

    fun getIdHorse(horse: Horse): Int?{
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            val query = "SELECT id FROM horses WHERE moniker = ? AND dateOfBirth = ? AND healthStatus = ? AND genderId = ? AND breedId = ? AND stableId = ? AND image = ?"
            cursor = db.rawQuery(query, arrayOf(horse.moniker, horse.dateOfBirth, horse.healthStatus, horse.genderId.toString(), horse.breedId.toString(), horse.stableId.toString(), horse.imageProfile.toString()))
            if (cursor.moveToFirst()) {
                val idColumnIndex = cursor.getColumnIndex("id")

                if (idColumnIndex == -1 ) {
                    Log.e("Database", "Один или несколько столбцов не найдены в таблице horses!")
                    return null
                }

                val id = cursor.getInt(idColumnIndex)
                return id
            }
            else {
                Log.d("Database", "Лошадь не найдена")
                return null
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при проверке существования лошади: ${e.message}")
            return null
        } finally {
            cursor?.close()
        }
    }

}