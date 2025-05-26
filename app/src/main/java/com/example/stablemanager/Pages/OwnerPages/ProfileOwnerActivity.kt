package com.example.stablemanager.Pages.OwnerPages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.stablemanager.Components.Managers.AuthManager
import com.example.stablemanager.Components.Managers.StableManager
import com.example.stablemanager.Components.isValidEmail
import com.example.stablemanager.Components.setEditable
import com.example.stablemanager.Pages.MainActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper

class ProfileOwnerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_owner)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Toast.makeText(this, "Для возврата к списку нажмите на логотип", Toast.LENGTH_LONG).show()

        val buttonExit: Button = findViewById(R.id.exitAccountButton)
        val buttonEdit: Button = findViewById(R.id.editProfileButton)
        val buttonSave: Button = findViewById(R.id.saveProfileButton)
        val buttonEditPass: Button = findViewById(R.id.editPasswordButton)
        val userSurname: EditText = findViewById(R.id.userSurnameProfile)
        val userName: EditText = findViewById(R.id.userNameProfile)
        val userPatronymic: EditText = findViewById(R.id.userPatronymicProfile)
        val userEmail: EditText = findViewById(R.id.userEmailProfile)
        val userLogin: EditText = findViewById(R.id.userLoginProfile)


        val db = DBHelper(this, null)
        val authManager = AuthManager(this)
        var userId = -1
        if (authManager.isLoggedIn()) {
            userId = authManager.getUserId()
            val user = db.getOwnerById(userId)

            if (user != null) {
                userSurname.setText(user.surname)
                userName.setText(user.fullname)
                userPatronymic.setText(user.patronymic)
                userEmail.setText(user.email)
                userLogin.setText(user.login)
            } else {
                Log.e("Profile", "Пользователь не найден в базе данных.")
            }
        }

        buttonExit.setOnClickListener {
            val stableManager = StableManager(this)
            stableManager.clearStableData()
            authManager.clearAuthData()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val logoProfileImage: ImageView = findViewById(R.id.logoProfileImage)

        logoProfileImage.setOnClickListener {
            val intent = Intent(this, ListStableActivity::class.java)
            startActivity(intent)
        }

        buttonEdit.setOnClickListener {
            userSurname.setEditable(true)
            userName.setEditable(true)
            userPatronymic.setEditable(true)
            userEmail.setEditable(true)
            userLogin.setEditable(true)

            buttonEdit.visibility = View.GONE
            buttonSave.visibility = View.VISIBLE
        }

        buttonSave.setOnClickListener {
            val surname = userSurname.text.toString().trim()
            val name = userName.text.toString().trim()
            val patronymic = userPatronymic.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val login = userLogin.text.toString().trim()

            if(surname == "" || name == "" || patronymic == "" || email == "" || login == "")
                Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            else {
                if (db.doesOwnerExist(userId, login, email)){
                    Toast.makeText(this, "Пользователь с таким логином или почтой уже существует", Toast.LENGTH_SHORT).show()
                }
                else {
                    if (isValidEmail(email)){
                        userSurname.setEditable(false)
                        userName.setEditable(false)
                        userPatronymic.setEditable(false)
                        userEmail.setEditable(false)
                        userLogin.setEditable(false)

                        db.updateOwner(userId, surname, name, patronymic, email, login)

                        buttonEdit.visibility = View.VISIBLE
                        buttonSave.visibility = View.GONE
                    }
                    else{
                        Toast.makeText(this, "Поле почты заполнено некорректно. Заполните в формате mail@mail.ru", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        buttonEditPass.setOnClickListener {
            val intent = Intent(this, EditPasswordActivity::class.java)
            startActivity(intent)
        }

    }
}