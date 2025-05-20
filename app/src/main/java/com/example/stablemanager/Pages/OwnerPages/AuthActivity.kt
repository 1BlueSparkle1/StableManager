package com.example.stablemanager.Pages.OwnerPages

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Owner

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonReg: Button = findViewById(R.id.registrationButton)
        val buttonEmpty: Button = findViewById(R.id.authButton)
        val userLogin: EditText = findViewById(R.id.user_login)
        val userPassword: EditText = findViewById(R.id.user_password)
        val saveSwitch: Switch = findViewById(R.id.saveSwitch)

        buttonReg.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        buttonEmpty.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val password = userPassword.text.toString().trim()

            if(login == "" || password == "")
                Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_LONG).show()
            else {
                val db = DBHelper(this, null)
                val isAuth = db.authOwner(this, login, password)

                if(isAuth != null){
                    Toast.makeText(this, "Пользователь $login авторизован", Toast.LENGTH_SHORT).show()
                    userLogin.text.clear()
                    userPassword.text.clear()
                    val owner = db.getOwnerById(isAuth)
                    if(owner != null && saveSwitch.isChecked){
                        saveUserData(owner)
                    }

                    val intent = Intent(this, ListStableActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "Пользователь $login не найден!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveUserData(user: Owner) {
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("surname", user.surname)
        editor.putString("fullname", user.fullname)
        editor.putString("patronymic", user.patronymic)
        editor.putString("email", user.email)
        editor.putString("login", user.login)
        editor.putString("pass", user.pass)
        editor.putBoolean("ban", user.ban)
        editor.apply()
    }

}