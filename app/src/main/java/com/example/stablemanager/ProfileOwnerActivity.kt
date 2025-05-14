package com.example.stablemanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager

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
        val buttonEditPass: Button = findViewById(R.id.editPasswordButton)
        val userSurname: EditText = findViewById(R.id.userSurnameProfile)
        val userName: EditText = findViewById(R.id.userNameProfile)
        val userPatronymic: EditText = findViewById(R.id.userPatronymicProfile)
        val userEmail: EditText = findViewById(R.id.userEmailProfile)
        val userLogin: EditText = findViewById(R.id.userLoginProfile)


        val db = DBHelper(this, null)
        val authManager = AuthManager(this)
        if (authManager.isLoggedIn()) {
            val userId = authManager.getUserId()
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
            authManager.clearAuthData()

            val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val logoProfileImage: ImageView = findViewById(R.id.logoProfileImage)

        logoProfileImage.setOnClickListener {
            val intent = Intent(this, ListStableActivity::class.java)
            startActivity(intent)
        }
    }
}