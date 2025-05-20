package com.example.stablemanager.Pages.OwnerPages

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.stablemanager.Components.isValidEmail
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Owner
import org.mindrot.jbcrypt.BCrypt

class RegistrationActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userSurname: EditText = findViewById(R.id.user_surname)
        val userName: EditText = findViewById(R.id.user_name)
        val userPatronymic: EditText = findViewById(R.id.user_patronymic)
        val userEmail: EditText = findViewById(R.id.user_email)
        val userLogin: EditText = findViewById(R.id.user_login)
        val userPassword: EditText = findViewById(R.id.user_password)


        val emptyButton: Button = findViewById(R.id.emptyButton)
        val regButton: Button = findViewById(R.id.regButton)

        emptyButton.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        regButton.setOnClickListener {
            val surname = userSurname.text.toString().trim()
            val name = userName.text.toString().trim()
            val patronymic = userPatronymic.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val login = userLogin.text.toString().trim()
            val password = userPassword.text.toString().trim()

            if(surname == "" || name == "" || patronymic == "" || email == "" || login == "" || password == "")
                Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            else {
                if(isValidEmail(email)){
                    val saltRounds = 12
                    val hashPassword = BCrypt.hashpw(password, BCrypt.gensalt(saltRounds))
                    val owner = Owner(surname, name, patronymic, email, login, hashPassword, false)

                    val db = DBHelper(this, null)
                    db.addOwner(owner)
                    Toast.makeText(this, "Пользователь $login добавлен", Toast.LENGTH_SHORT).show()
                    userSurname.text.clear()
                    userName.text.clear()
                    userPatronymic.text.clear()
                    userEmail.text.clear()
                    userLogin.text.clear()
                    userPassword.text.clear()
                }
                else{
                    Toast.makeText(this, "Поле почты заполнено некорректно. Заполните в формате mail@mail.ru", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}