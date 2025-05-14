package com.example.stablemanager

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonOwner: Button = findViewById(R.id.ownerButton)
        val buttonEmployee: Button = findViewById(R.id.employeeButton)

        buttonOwner.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        buttonEmployee.setOnClickListener {
            Toast.makeText(this, "You navigated employee", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkSavedUserData() {
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val login = sharedPreferences.getString("login", null)
        val pass = sharedPreferences.getString("pass", null)
        val db = DBHelper(this, null)

        if (login != null && pass != null) {
            val userId = db.authOwner(this, login, pass)
            if (userId != null) {
                val intent = Intent(this, ListStableActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}