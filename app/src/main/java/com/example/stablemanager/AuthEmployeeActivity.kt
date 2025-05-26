package com.example.stablemanager

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
import com.example.stablemanager.Components.Managers.AuthEmployeeManager
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.Pages.OwnerPages.ListStableActivity
import com.example.stablemanager.Pages.OwnerPages.RegistrationActivity
import com.example.stablemanager.db.DBHelper

class AuthEmployeeActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth_employee)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonEmpty: Button = findViewById(R.id.authEmployeeButton)
        val userLogin: EditText = findViewById(R.id.employee_login)
        val userPassword: EditText = findViewById(R.id.employee_password)

        buttonEmpty.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val password = userPassword.text.toString().trim()

            if(login == "" || password == "")
                Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            else {
                val db = DBHelper(this, null)
                val isAuth = db.authEmployee(this, login, password)
                val authEmployeeManager = AuthEmployeeManager(this)

                if(isAuth != null){
                    Toast.makeText(this, "Пользователь $login авторизован", Toast.LENGTH_SHORT).show()
                    authEmployeeManager.saveEmployeeId(isAuth)
                    userLogin.text.clear()
                    userPassword.text.clear()

                    val employee = db.getEmployeeById(isAuth)
                    val role = db.getRolesById(employee!!.roleId)
                    if(role!!.title == "Администратор"){
                        val intent = Intent(this, StartAdminPageActivity::class.java)
                        startActivity(intent)
                    }
                }
                else{
                    Toast.makeText(this, "Пользователь $login не найден!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}