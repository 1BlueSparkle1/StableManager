package com.example.stablemanager.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.text.SimpleDateFormat
import android.util.Log
import com.example.stablemanager.Components.Managers.AuthManager
import org.mindrot.jbcrypt.BCrypt
import java.util.Locale

class DBHelper(val context: Context, private val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "horse_club", factory, 5) {
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
            Id INTEGER PRIMARY KEY AUTOINCREMENT,
            Moniker TEXT NOT NULL,
            BirthDate TEXT,
            GenderId INTEGER,
            BreedId INTEGER,
            FeedingConditionId INTEGER,
            TurnoutConditionId INTEGER,
            HealthCondition TEXT,
            FarrierId INTEGER,
            VeterinarianId INTEGER,
            Image BLOB,
            FOREIGN KEY (GenderId) REFERENCES Genders(Id),
            FOREIGN KEY (BreedId) REFERENCES Breeds(Id),
            FOREIGN KEY (FeedingConditionId) REFERENCES FeedingConditions(Id),
            FOREIGN KEY (TurnoutConditionId) REFERENCES TurnoutConditions(Id),
            FOREIGN KEY (FarrierId) REFERENCES Farriers(Id),
            FOREIGN KEY (VeterinarianId) REFERENCES Veterinarians(Id)
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

        val queryVeterinarians = """
        CREATE TABLE Veterinarians (
            Id INTEGER PRIMARY KEY AUTOINCREMENT,
            FullName TEXT NOT NULL,
            PhoneNumber TEXT,
            StableId INTEGER,
            FOREIGN KEY (StableId) REFERENCES Stables(Id)
        );
    """.trimIndent()
        db.execSQL(queryVeterinarians)

        val queryFarriers = """
        CREATE TABLE Farriers (
            Id INTEGER PRIMARY KEY AUTOINCREMENT,
            FullName TEXT NOT NULL,
            PhoneNumber TEXT,
            StableId INTEGER,
            FOREIGN KEY (StableId) REFERENCES Stables(Id)
        );
    """.trimIndent()
        db.execSQL(queryFarriers)

        val queryServices = """
        CREATE TABLE Services (
            Id INTEGER PRIMARY KEY AUTOINCREMENT,
            Title TEXT NOT NULL,
            Price REAL NOT NULL,
            Description TEXT,
            StableId INTEGER NOT NULL,
            FOREIGN KEY (StableId) REFERENCES Stables(Id)
        );
    """.trimIndent()
        db.execSQL(queryServices)

        // Таблица "Запись" (Appointment)
        val queryAppointments = """
        CREATE TABLE Appointments (
            Id INTEGER PRIMARY KEY AUTOINCREMENT,
            ServiceId INTEGER NOT NULL,
            HorseId INTEGER NOT NULL,
            EmployeeId INTEGER NOT NULL,
            ClientName TEXT NOT NULL,
            ClientPhone TEXT NOT NULL,
            AppointmentDateTime DATETIME NOT NULL,
            StableId INTEGER NOT NULL,
            Confirmation INTEGER DEFAULT 0, 
            FOREIGN KEY (ServiceId) REFERENCES Services(Id),
            FOREIGN KEY (HorseId) REFERENCES Horses(Id),
            FOREIGN KEY (EmployeeId) REFERENCES Employees(Id),
            FOREIGN KEY (StableId) REFERENCES Stables(Id)
        );
    """.trimIndent()
        db.execSQL(queryAppointments)

        val queryTrainingLevels = """
        CREATE TABLE TrainingLevels (
            Id INTEGER PRIMARY KEY AUTOINCREMENT,
            Title TEXT NOT NULL
        );
    """.trimIndent()
        db.execSQL(queryTrainingLevels)

        val queryEmployeeTrainingLevels = """
        CREATE TABLE EmployeeTrainingLevels (
            Id INTEGER PRIMARY KEY AUTOINCREMENT,
            TrainingLevelId INTEGER NOT NULL,
            EmployeeId INTEGER NOT NULL,
            FOREIGN KEY (TrainingLevelId) REFERENCES TrainingLevels(Id),
            FOREIGN KEY (EmployeeId) REFERENCES Employees(Id)
        );
    """.trimIndent()
        db.execSQL(queryEmployeeTrainingLevels)

        val queryHorseTrainingLevels = """
        CREATE TABLE HorseTrainingLevels (
            Id INTEGER PRIMARY KEY AUTOINCREMENT,
            TrainingLevelId INTEGER NOT NULL,
            HorseId INTEGER NOT NULL,
            FOREIGN KEY (TrainingLevelId) REFERENCES TrainingLevels(Id),
            FOREIGN KEY (HorseId) REFERENCES Horses(Id)
        );
    """.trimIndent()
        db.execSQL(queryHorseTrainingLevels)

        // Таблица "Выгул" (Turnout)
        val queryTurnouts = """
        CREATE TABLE Turnouts (
            Id INTEGER PRIMARY KEY AUTOINCREMENT,
            NumberOfHorses INTEGER NOT NULL,
            StartTime TIME NOT NULL,
            EndTime TIME NOT NULL,
            StableId INTEGER NOT NULL,
            FOREIGN KEY (StableId) REFERENCES Stables(Id)
        );
    """.trimIndent()
        db.execSQL(queryTurnouts)

        // Таблица "Расписание выгула" (TurnoutSchedule)
        val queryTurnoutSchedules = """
        CREATE TABLE TurnoutSchedules (
            Id INTEGER PRIMARY KEY AUTOINCREMENT,
            TurnoutId INTEGER NOT NULL,
            HorseId INTEGER NOT NULL,
            StableId INTEGER NOT NULL,
            FOREIGN KEY (StableId) REFERENCES Stables(Id),
            FOREIGN KEY (TurnoutId) REFERENCES Turnouts(Id),
            FOREIGN KEY (HorseId) REFERENCES Horses(Id)
        );
    """.trimIndent()
        db.execSQL(queryTurnoutSchedules)

        // Таблица "Выполнение выгула" (TurnoutExecution)
        val queryTurnoutExecutions = """
        CREATE TABLE TurnoutExecutions (
            Id INTEGER PRIMARY KEY AUTOINCREMENT,
            TurnoutScheduleId INTEGER NOT NULL,
            ExecutionDate DATE NOT NULL,
            StableId INTEGER NOT NULL,
            FOREIGN KEY (StableId) REFERENCES Stables(Id),
            FOREIGN KEY (TurnoutScheduleId) REFERENCES TurnoutSchedules(Id)
        );
    """.trimIndent()
        db.execSQL(queryTurnoutExecutions)

        // Таблица "Время кормления" (FeedingTime)
        val queryFeedingTimes = """
        CREATE TABLE FeedingTimes (
            Id INTEGER PRIMARY KEY AUTOINCREMENT,
            Title TEXT NOT NULL,
            StableId INTEGER NOT NULL,
            FOREIGN KEY (StableId) REFERENCES Stables(Id)
        );
    """.trimIndent()
        db.execSQL(queryFeedingTimes)

        // Таблица "Расписание кормления" (FeedingSchedule)
        val queryFeedingSchedules = """
        CREATE TABLE FeedingSchedules (
            Id INTEGER PRIMARY KEY AUTOINCREMENT,
            FeedingTimeId INTEGER NOT NULL,
            FeedId INTEGER NOT NULL,
            Quantity REAL NOT NULL,
            HorseId INTEGER NOT NULL,
            Steam BOOLEAN NOT NULL,
            StableId INTEGER NOT NULL,
            FOREIGN KEY (StableId) REFERENCES Stables(Id),
            FOREIGN KEY (FeedingTimeId) REFERENCES FeedingTimes(Id),
            FOREIGN KEY (FeedId) REFERENCES Feeds(Id),
            FOREIGN KEY (HorseId) REFERENCES Horses(Id)
        );
    """.trimIndent()
        db.execSQL(queryFeedingSchedules)

        // Таблица "Выполнение кормления" (FeedingExecution)
        val queryFeedingExecutions = """
        CREATE TABLE FeedingExecutions (
            Id INTEGER PRIMARY KEY AUTOINCREMENT,
            FeedingScheduleId INTEGER NOT NULL,
            ExecutionDate DATE NOT NULL,
            StableId INTEGER NOT NULL,
            FOREIGN KEY (StableId) REFERENCES Stables(Id),
            FOREIGN KEY (FeedingScheduleId) REFERENCES FeedingSchedules(Id)
        );
    """.trimIndent()
        db.execSQL(queryFeedingExecutions)

        val queryNotifications = """
        CREATE TABLE Notifications (
            Id INTEGER PRIMARY KEY AUTOINCREMENT,
            Description TEXT NOT NULL,
            IsRead BOOLEAN DEFAULT 0,
            EmployeeId INTEGER,
            OwnerId INTEGER,
            CreationDate TEXT DEFAULT (datetime('now', 'localtime')),
            SenderEmployeeId INTEGER,
            SenderOwnerId INTEGER,
            FOREIGN KEY (EmployeeId) REFERENCES Employees(Id),
            FOREIGN KEY (OwnerId) REFERENCES Owners(Id),
            FOREIGN KEY (SenderEmployeeId) REFERENCES Employees(Id),
            FOREIGN KEY (SenderOwnerId) REFERENCES Owners(Id)
        );
    """.trimIndent()
        db.execSQL(queryNotifications)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS owners")
        db.execSQL("DROP TABLE IF EXISTS stables")
        db.execSQL("DROP TABLE IF EXISTS roles")
        db.execSQL("DROP TABLE IF EXISTS employees")
        db.execSQL("DROP TABLE IF EXISTS type_breeds")
        db.execSQL("DROP TABLE IF EXISTS breeds")
        db.execSQL("DROP TABLE IF EXISTS gender_horses")
        db.execSQL("DROP TABLE IF EXISTS horses")
        db.execSQL("DROP TABLE IF EXISTS feeds")
        db.execSQL("DROP TABLE IF EXISTS Veterinarians")
        db.execSQL("DROP TABLE IF EXISTS Farriers")
        db.execSQL("DROP TABLE IF EXISTS Services")
        db.execSQL("DROP TABLE IF EXISTS Appointments")
        db.execSQL("DROP TABLE IF EXISTS TrainingLevels")
        db.execSQL("DROP TABLE IF EXISTS EmployeeTrainingLevels")
        db.execSQL("DROP TABLE IF EXISTS HorseTrainingLevels")
        db.execSQL("DROP TABLE IF EXISTS Turnouts")
        db.execSQL("DROP TABLE IF EXISTS TurnoutSchedules")
        db.execSQL("DROP TABLE IF EXISTS TurnoutExecutions")
        db.execSQL("DROP TABLE IF EXISTS FeedingTimes")
        db.execSQL("DROP TABLE IF EXISTS FeedingSchedules")
        db.execSQL("DROP TABLE IF EXISTS FeedingExecutions")
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

    fun updateOwner(id: Int, surname: String, name: String, patronymic: String, email: String, login: String, password: String): Boolean{
        val db = this.readableDatabase
        try {
            val values = ContentValues().apply {
                put("surname", surname)
                put("name", name)
                put("patronymic", patronymic)
                put("email", email)
                put("login", login)
                put("password", password)
            }

            val rowsAffected = db.update(
                "owners",
                values,
                "id = ?",
                arrayOf(id.toString())
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

    fun putBanOwner(ownerId: Int, ban: Boolean): Boolean{
        val db = this.readableDatabase
        try {
            val values = ContentValues().apply {
                put("ban", ban)
            }

            val rowsAffected = db.update(
                "owners",
                values,
                "id = ?",
                arrayOf(ownerId.toString())
            )

            if (rowsAffected > 0) {
                Log.d("Database", "Бан успешно обновлен.")
                return true
            } else {
                Log.w("Database", "Владелец не найден для обновления.")
                return false
            }

        } catch (e: Exception) {
            Log.e("Database", "Ошибка при обновлении бана: ${e.message}")
            return false
        }
    }

    fun deleteOwnerAndAllRelatedData(ownerId: Int) {
        val db = this.writableDatabase
        db.beginTransaction()

        try {
            val stableIds = mutableListOf<Int>()
            var cursorStables: Cursor? = null

            try {
                cursorStables = db.rawQuery("SELECT id FROM stables WHERE ownerId = ?", arrayOf(ownerId.toString()))

                if (cursorStables.moveToFirst()) {
                    do {
                        val id = cursorStables.getInt(cursorStables.getColumnIndexOrThrow("id"))
                        stableIds.add(id)
                    } while (cursorStables.moveToNext())
                }
            } finally {
                cursorStables?.close()
            }

            val employeeIdsToDelete = mutableListOf<Int>()
            var cursorEmployees: Cursor? = null
            try {
                cursorEmployees = db.rawQuery("SELECT id FROM employees WHERE stableId IN (${stableIds.joinToString(",")})", null)
                if (cursorEmployees.moveToFirst()) {
                    do {
                        employeeIdsToDelete.add(cursorEmployees.getInt(cursorEmployees.getColumnIndexOrThrow("id")))
                    } while (cursorEmployees.moveToNext())
                }
            } finally {
                cursorEmployees?.close()
            }

            if (employeeIdsToDelete.isNotEmpty()) {
                db.delete("EmployeeTrainingLevels", "EmployeeId IN (${employeeIdsToDelete.joinToString(",")})", null)
                db.delete("employees", "id IN (${employeeIdsToDelete.joinToString(",")})", null)
            }

            val horseIdsToDelete = mutableListOf<Int>()
            var cursorHorses: Cursor? = null
            try {
                cursorHorses = db.rawQuery("SELECT id FROM horses WHERE BreedId IN (SELECT Id FROM Breeds WHERE StableId IN (${stableIds.joinToString(",")}))", null)
                if (cursorHorses.moveToFirst()) {
                    do {
                        horseIdsToDelete.add(cursorHorses.getInt(cursorHorses.getColumnIndexOrThrow("id")))
                    } while (cursorHorses.moveToNext())
                }
            } finally {
                cursorHorses?.close()
            }

            if (horseIdsToDelete.isNotEmpty()) {
                db.delete("HorseTrainingLevels", "HorseId IN (${horseIdsToDelete.joinToString(",")})", null)
                db.delete("horses", "id IN (${horseIdsToDelete.joinToString(",")})", null)
            }

            for (stableId in stableIds) {
                db.delete("Veterinarians", "StableId = ?", arrayOf(stableId.toString()))
                db.delete("Farriers", "StableId = ?", arrayOf(stableId.toString()))

                db.delete("Appointments", "StableId = ?", arrayOf(stableId.toString()))
                db.delete("Turnouts", "StableId = ?", arrayOf(stableId.toString()))

                db.delete("TurnoutSchedules", "TurnoutId IN (SELECT Id FROM Turnouts WHERE StableId = ?)", arrayOf(stableId.toString()))
                db.delete("TurnoutExecutions", "TurnoutScheduleId IN (SELECT Id FROM TurnoutSchedules WHERE TurnoutId IN (SELECT Id FROM Turnouts WHERE StableId = ?))", arrayOf(stableId.toString()))

                db.delete("FeedingSchedules", "HorseId IN (SELECT Id FROM Horses WHERE BreedId IN (SELECT Id FROM Breeds WHERE StableId IN (${stableIds.joinToString(",")})) )", arrayOf(stableId.toString()))
                db.delete("FeedingExecutions", "FeedingScheduleId IN (SELECT Id FROM FeedingSchedules WHERE HorseId IN (SELECT Id FROM Horses WHERE BreedId IN (SELECT Id FROM Breeds WHERE StableId IN (${stableIds.joinToString(",")})) ))", arrayOf(stableId.toString()))

            }

            db.delete("Feeds", "StableId IN (${stableIds.joinToString(",")})", null)

            db.delete("Services", "StableId IN (${stableIds.joinToString(",")})", null)

            db.delete("stables", "ownerId = ?", arrayOf(ownerId.toString()))

            db.delete("owners", "id = ?", arrayOf(ownerId.toString()))

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DBHelper", "Ошибка при удалении владельца и связанных данных: ${e.message}")
        } finally {
            db.endTransaction()
            db.close()
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

    fun getRolesNotAdmin(): List<Role> {
        val db = this.readableDatabase
        val roles = mutableListOf<Role>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT id, title FROM roles WHERE title != 'Администратор'", null)
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

    fun deleteRole(id: Int){
        val db = this.readableDatabase

        try {
            val selection = "id = ?"
            val selectionArgs = arrayOf(id.toString())

            val deletedRows = db.delete("roles", selection, selectionArgs)

            if (deletedRows > 0) {
                Log.d("DBHelper", "Роль с ID $id успешно удалена.")
            } else {
                Log.w("DBHelper", "Не удалось удалить роль с ID $id. Возможно, такой роли не существует.")
            }
        } catch (e: SQLException) {
            Log.e("DBHelper", "Ошибка при удалении роли:", e)
        } finally {
            db.close()
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

    fun deleteTypeBreed(id: Int){
        val db = this.readableDatabase

        try {
            val selection = "id = ?"
            val selectionArgs = arrayOf(id.toString())

            val deletedRows = db.delete("type_breeds", selection, selectionArgs)

            if (deletedRows > 0) {
                Log.d("DBHelper", "Тип породы с ID $id успешно удален.")
            } else {
                Log.w("DBHelper", "Не удалось удалить тип породы с ID $id. Возможно, такого типа породы не существует.")
            }
        } catch (e: SQLException) {
            Log.e("DBHelper", "Ошибка при удалении типа породы:", e)
        } finally {
            db.close()
        }
    }

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

    fun addBreed(breed: Breed){
        val values = ContentValues()
        values.put("title", breed.title)
        values.put("typeBreedId", breed.typeBreedId)

        val db = this.writableDatabase
        db.insert("breeds", null, values)

        db.close()
    }

    fun getBreedById(breedId: Int) : Breed?{
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT title, typeBreedId FROM breeds WHERE id = ?", arrayOf(breedId.toString()))
            if (cursor.moveToFirst()) {
                val titleColumnIndex = cursor.getColumnIndex("title")
                val typeIdColumnIndex = cursor.getColumnIndex("typeBreedId")

                if (titleColumnIndex == -1 || typeIdColumnIndex == -1) {
                    Log.e("Database", "Один или несколько столбцов не найдены в таблице breeds!")
                    return null
                }

                val title = cursor.getString(titleColumnIndex)
                val typeId = cursor.getInt(typeIdColumnIndex)


                return Breed(title, typeId)

            } else {
                Log.d("Database", "Порода с ID $breedId не найдена")
                return null
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при получении породы: ${e.message}")
            return null
        } finally {
            cursor?.close()
        }
    }

    fun updateBreed(breedId: Int, title: String, typeId: Int): Boolean{
        val db = this.readableDatabase
        try {
            val values = ContentValues().apply {
                put("title", title)
                put("typeBreedId", typeId)
            }

            val rowsAffected = db.update(
                "breeds",
                values,
                "id = ?",
                arrayOf(breedId.toString())
            )

            if (rowsAffected > 0) {
                Log.d("Database", "Порода успешно обновлена.")
                return true
            } else {
                Log.w("Database", "Порода не найдена для обновления.")
                return false
            }

        } catch (e: Exception) {
            Log.e("Database", "Ошибка при обновлении породы: ${e.message}")
            return false
        }
    }

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

    fun deleteBreed(id: Int){
        val db = this.readableDatabase

        try {
            val selection = "id = ?"
            val selectionArgs = arrayOf(id.toString())

            val deletedRows = db.delete("breeds", selection, selectionArgs)

            if (deletedRows > 0) {
                Log.d("DBHelper", "Порода с ID $id успешно удалена.")
            } else {
                Log.w("DBHelper", "Не удалось удалить породу с ID $id. Возможно, такой породы не существует.")
            }
        } catch (e: SQLException) {
            Log.e("DBHelper", "Ошибка при удалении породы:", e)
        } finally {
            db.close()
        }
    }

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

    fun deleteGenderHorse(id: Int){
        val db = this.readableDatabase

        try {
            val selection = "id = ?"
            val selectionArgs = arrayOf(id.toString())

            val deletedRows = db.delete("gender_horses", selection, selectionArgs)

            if (deletedRows > 0) {
                Log.d("DBHelper", "Пол лошадей с ID $id успешно удален.")
            } else {
                Log.w("DBHelper", "Не удалось удалить пол лошадей с ID $id. Возможно, такого пола лошадей не существует.")
            }
        } catch (e: SQLException) {
            Log.e("DBHelper", "Ошибка при удалении пола лошадей:", e)
        } finally {
            db.close()
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

    fun authEmployee(context: Context, login: String, passwordAttempt: String): Int? {
        val db = this.readableDatabase
        var result: Cursor? = null
        try {
            result = db.rawQuery("SELECT id, password FROM employees WHERE login = ?", arrayOf(login))
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

    fun getEmployeeById(employeeId: Int): Employee?{
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT surname, name, patronymic, email, login, password, roleId, dateOfBirth, salary, stableId, imageProfile FROM employees WHERE id = ?", arrayOf(employeeId.toString()))
            if (cursor.moveToFirst()) {
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
                val image = cursor.getBlob(imageProfileColumnIndex)


                return Employee(surname, name, patronymic, email, login, password, roleId, dateOfBirth, salary, stableId, image)

            } else {
                Log.d("Database", "Сотрудник с ID $employeeId не найден")
                return null
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при получении сотрудника: ${e.message}")
            return null
        } finally {
            cursor?.close()
        }
    }

    fun updateEmployeeProfile(id: Int, surname: String, name: String, patronymic: String, dateOfBirth: String, email: String, login: String, image: ByteArray): Boolean{
        val db = this.readableDatabase
        try {
            val values = ContentValues().apply {
                put("surname", surname)
                put("name", name)
                put("patronymic", patronymic)
                put("dateOfBirth", dateOfBirth)
                put("email", email)
                put("login", login)
                put("imageProfile", image)
            }

            val rowsAffected = db.update(
                "employees",
                values,
                "id = ?",
                arrayOf(id.toString())
            )

            if (rowsAffected > 0) {
                Log.d("Database", "Сотрудник $login успешно обновлен.")
                return true
            } else {
                Log.w("Database", "Сотрудник $login не найден для обновления.")
                return false
            }

        } catch (e: Exception) {
            Log.e("Database", "Ошибка при обновлении сотрудника: ${e.message}")
            return false
        }
    }

    fun updateEmployee(id: Int, surname: String, name: String, patronymic: String, dateOfBirth: String, email: String, login: String, password: String, image: ByteArray, roleId: Int, salary: Double, stableId: Int): Boolean{
        val db = this.readableDatabase
        try {
            val values = ContentValues().apply {
                put("surname", surname)
                put("name", name)
                put("patronymic", patronymic)
                put("dateOfBirth", dateOfBirth)
                put("email", email)
                put("login", login)
                put("password", password)
                put("roleId", roleId)
                put("salary", salary)
                put("stableId", stableId)
                put("imageProfile", image)
            }

            val rowsAffected = db.update(
                "employees",
                values,
                "id = ?",
                arrayOf(id.toString())
            )

            if (rowsAffected > 0) {
                Log.d("Database", "Сотрудник $login успешно обновлен.")
                return true
            } else {
                Log.w("Database", "Сотрудник $login не найден для обновления.")
                return false
            }

        } catch (e: Exception) {
            Log.e("Database", "Ошибка при обновлении сотрудника: ${e.message}")
            return false
        }
    }

    fun deleteEmployeeAndRelatedData(employeeIdToDelete: Int) {
        val db = this.writableDatabase
        db.beginTransaction()

        try {
            var cursorEmployee: Cursor? = null
            var stableId: Int? = null
            try {
                cursorEmployee = db.rawQuery("SELECT stableId FROM employees WHERE id = ?", arrayOf(employeeIdToDelete.toString()))
                if (cursorEmployee.moveToFirst()) {
                    stableId = cursorEmployee.getInt(cursorEmployee.getColumnIndexOrThrow("stableId"))
                }
            } finally {
                cursorEmployee?.close()
            }

            if (stableId == null) {
                Log.e("DBHelper", "Сотрудник не найден или не имеет конюшни.")
                return
            }

            val appointmentIdsToNotify = mutableListOf<Int>()
            var cursorAppointments: Cursor? = null
            try {
                cursorAppointments = db.rawQuery("SELECT id FROM Appointments WHERE EmployeeId = ?", arrayOf(employeeIdToDelete.toString()))
                if (cursorAppointments.moveToFirst()) {
                    do {
                        val appointmentId = cursorAppointments.getInt(cursorAppointments.getColumnIndexOrThrow("id"))
                        appointmentIdsToNotify.add(appointmentId)
                    } while (cursorAppointments.moveToNext())
                }
            } finally {
                cursorAppointments?.close()
            }

            val values = ContentValues().apply {
                putNull("EmployeeId")
            }
            val selection = "EmployeeId = ?"
            val selectionArgs = arrayOf(employeeIdToDelete.toString())
            val count = db.update("Appointments", values, selection, selectionArgs)

            Log.d("DBHelper", "Обновлено $count записей в Appointments")


            val registrarIds = mutableListOf<Int>()
            var cursorRegistrars: Cursor? = null
            try {
                cursorRegistrars = db.rawQuery("SELECT id FROM employees WHERE stableId = ? AND roleId = (SELECT id FROM roles WHERE title = 'Регистратор')", arrayOf(stableId.toString()))
                if (cursorRegistrars.moveToFirst()) {
                    do {
                        registrarIds.add(cursorRegistrars.getInt(cursorRegistrars.getColumnIndexOrThrow("id")))
                    } while (cursorRegistrars.moveToNext())
                }
            } finally {
                cursorRegistrars?.close()
            }

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            for (appointmentId in appointmentIdsToNotify) {

                var appointmentDate: String = ""
                var cursorAppointmentDate: Cursor? = null
                try {
                    cursorAppointmentDate = db.rawQuery("SELECT AppointmentDateTime FROM Appointments WHERE id = ?", arrayOf(appointmentId.toString()))
                    if (cursorAppointmentDate.moveToFirst()) {
                        appointmentDate = cursorAppointmentDate.getString(cursorAppointmentDate.getColumnIndexOrThrow("AppointmentDateTime"))
                    }
                } finally {
                    cursorAppointmentDate?.close()
                }

                for (registrarId in registrarIds) {
                    val description = "Запись номер $appointmentId от $appointmentDate не имеет тренера, оповестите об этом клиента или назначьте нового тренера"
                    val valuesNotification = ContentValues().apply {
                        put("Description", description)
                        put("EmployeeId", registrarId)
                    }
                    db.insert("Notifications", null, valuesNotification)
                    Log.d("DBHelper", "Создано уведомление для регистратора $registrarId")
                }
            }


            var ownerId: Int? = null
            var cursorOwner: Cursor? = null
            try {
                cursorOwner = db.rawQuery("SELECT ownerId FROM stables WHERE id = ?", arrayOf(stableId.toString()))
                if (cursorOwner.moveToFirst()) {
                    ownerId = cursorOwner.getInt(cursorOwner.getColumnIndexOrThrow("ownerId"))
                }
            } finally {
                cursorOwner?.close()
            }

            if (ownerId != null) {
                for (appointmentId in appointmentIdsToNotify) {
                    var appointmentDate: String = ""
                    var cursorAppointmentDate: Cursor? = null
                    try {
                        cursorAppointmentDate = db.rawQuery("SELECT AppointmentDateTime FROM Appointments WHERE id = ?", arrayOf(appointmentId.toString()))
                        if (cursorAppointmentDate.moveToFirst()) {
                            appointmentDate = cursorAppointmentDate.getString(cursorAppointmentDate.getColumnIndexOrThrow("AppointmentDateTime"))
                        }
                    } finally {
                        cursorAppointmentDate?.close()
                    }

                    val description = "Запись номер $appointmentId от $appointmentDate не имеет тренера, оповестите об этом клиента или назначьте нового тренера"
                    val valuesNotification = ContentValues().apply {
                        put("Description", description)
                        put("OwnerId", ownerId)
                    }
                    db.insert("Notifications", null, valuesNotification)
                    Log.d("DBHelper", "Создано уведомление для владельца $ownerId")
                }
            }

            db.delete("EmployeeTrainingLevels", "EmployeeId = ?", arrayOf(employeeIdToDelete.toString()))

            db.delete("employees", "id = ?", arrayOf(employeeIdToDelete.toString()))

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DBHelper", "Ошибка при удалении сотрудника и связанных данных: ${e.message}")
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun doesEmployeeExist(userId: Int, login: String, email: String): Boolean {
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            val query = "SELECT 1 FROM employees WHERE (login = ? OR email = ?) AND id != ?"
            cursor = db.rawQuery(query, arrayOf(login, email, userId.toString()))

            val exists = cursor.count > 0
            Log.d("Database", "Проверка существования сотрудника с логином '$login' или email '$email': $exists")
            return exists

        } catch (e: Exception) {
            Log.e("Database", "Ошибка при проверке существования сотрудника: ${e.message}")
            return true
        } finally {
            cursor?.close()
        }
    }

    fun getEmployees(stableId: Int): List<Employee>{
        val db = this.readableDatabase
        val employees = mutableListOf<Employee>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM employees WHERE stableId = ?", arrayOf(stableId.toString()))
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
                    val imageProfileColumnIndex = cursor.getColumnIndex("imageProfile")

                    if (surnameColumnIndex == -1 || nameColumnIndex == -1 || emailColumnIndex == -1 || patronymicColumnIndex == -1
                        || loginColumnIndex == -1 || passwordColumnIndex == -1 || roleIdColumnIndex == -1 || dateOfBirthColumnIndex == -1
                        || salaryColumnIndex == -1 || imageProfileColumnIndex == -1) {
                        Log.e("Database", "Один или несколько столбцов не найдены в таблице employees!")
                        return emptyList()
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

    fun updatePasswordEmployee(login: String, newPass: String): Boolean{
        val db = this.readableDatabase
        try {
            val values = ContentValues().apply {
                put("password", newPass)
            }

            val rowsAffected = db.update(
                "employees",
                values,
                "login = ?",
                arrayOf(login)
            )

            if (rowsAffected > 0) {
                Log.d("Database", "Пароль успешно обновлен.")
                return true
            } else {
                Log.w("Database", "Сотрудник не найден для обновления.")
                return false
            }

        } catch (e: Exception) {
            Log.e("Database", "Ошибка при обновлении пароля: ${e.message}")
            return false
        }
    }

    fun removeRoleFromEmployees(roleId: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.putNull("roleId")
        val selection = "roleId = ?"
        val selectionArgs = arrayOf(roleId.toString())
        val count = db.update(
            "employees",
            values,
            selection,
            selectionArgs
        )
        Log.d("DBHelper", "Обновлено $count записей в employees")
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

    fun removeBreedFromHorses(breedId: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.putNull("BreedId")
        val selection = "BreedId = ?"
        val selectionArgs = arrayOf(breedId.toString())
        val count = db.update(
            "horses",
            values,
            selection,
            selectionArgs
        )
        Log.d("DBHelper", "Обновлено $count записей в horses")
        db.close()
    }

    fun removeGenderFromHorses(genderId: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.putNull("GenderId")
        val selection = "GenderId = ?"
        val selectionArgs = arrayOf(genderId.toString())
        val count = db.update(
            "horses",
            values,
            selection,
            selectionArgs
        )
        Log.d("DBHelper", "Обновлено $count записей в horses")
        db.close()
    }

    fun getAllFeeds(): List<Feed>{
        val db = this.readableDatabase
        val feeds = mutableListOf<Feed>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM feeds", null)
            if (cursor.moveToFirst()) {
                do {
                    val titleColumnIndex = cursor.getColumnIndex("title")
                    val quantityColumnIndex = cursor.getColumnIndex("quantity")
                    val stableIdColumnIndex = cursor.getColumnIndex("stableId")

                    if (titleColumnIndex == -1 || quantityColumnIndex == -1 || stableIdColumnIndex == -1) {
                        Log.e("Database", "Один или несколько столбцов не найдены!")
                        return emptyList()
                    }

                    val title = cursor.getString(titleColumnIndex)
                    val quantity = cursor.getDouble(quantityColumnIndex)
                    val stableId = cursor.getInt(stableIdColumnIndex)

                    val feed = Feed(title, quantity, stableId)
                    feeds.add(feed)

                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("Database", "Ошибка при получении данных из feeds: ${e.message}")
        } finally {
            cursor?.close()
        }

        return feeds
    }

    fun addFeed(feed: Feed){
        val values = ContentValues()
        values.put("title", feed.title)
        values.put("quantity", feed.quantity)
        values.put("stableId", feed.stableId)

        val db = this.writableDatabase
        db.insert("feeds", null, values)

        db.close()
    }

}