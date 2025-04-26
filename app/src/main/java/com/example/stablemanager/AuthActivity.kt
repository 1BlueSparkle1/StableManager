package com.example.stablemanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
                val isAuth = db.getOwner(login, password)

                if(isAuth){
                    Toast.makeText(this, "Пользователь $login авторизован", Toast.LENGTH_SHORT).show()
                    userLogin.text.clear()
                    userPassword.text.clear()
                }
                else{
                    Toast.makeText(this, "Пользователь $login не найден!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}