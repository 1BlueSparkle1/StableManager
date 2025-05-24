package com.example.stablemanager.Pages.OwnerPages

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.stablemanager.Components.Managers.AuthManager
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import org.mindrot.jbcrypt.BCrypt

class EditPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonSave: Button = findViewById(R.id.SavePasswordButton)
        val oldPassword: EditText = findViewById(R.id.oldPasswordText)
        val newPassword: EditText = findViewById(R.id.newPasswordText)

        val authManager = AuthManager(this)
        val db = DBHelper(this, null)

        buttonSave.setOnClickListener {
            val passwordOld = oldPassword.text.toString().trim()
            val passwordNew = newPassword.text.toString().trim()
            if (authManager.isLoggedIn()) {
                val userId = authManager.getUserId()
                val user = db.getOwnerById(userId)
                if(passwordOld == "" && passwordNew == ""){
                    Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
                }
                else{
                    if(user != null){
                        val isAuth = db.authOwner(this, user.login, oldPassword.text.toString().trim())
                        if(isAuth != null){
                            val saltRounds = 12
                            val hashPassword = BCrypt.hashpw(passwordNew, BCrypt.gensalt(saltRounds))
                            if(db.updatePassword(user.login, hashPassword)){
                                Toast.makeText(this, "Пароль обновлен!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, ProfileOwnerActivity::class.java)
                                startActivity(intent)
                            }
                            else{
                                Toast.makeText(this, "Ошибка сохранения пароля.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Toast.makeText(this, "Старый пароль неверен", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(this, "Ошибка пользователя. Перезойдите или повторите попытку позже.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }
}