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

class AddStableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_stable)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val titleStable: EditText = findViewById(R.id.titleStable)
        val descriptionAddStable: EditText = findViewById(R.id.descriptionAddStable)
        val addStablePageButton: Button = findViewById(R.id.addStablePageButton)

        addStablePageButton.setOnClickListener {
            val title = titleStable.text.toString().trim()
            val description = descriptionAddStable.text.toString().trim()

            val authManager = AuthManager(this)
            if (authManager.isLoggedIn()) {
                val userId = authManager.getUserId()
                val stable = Stable(title, description, userId)

                val db = DBHelper(this, null)
                db.addStable(stable)

                Toast.makeText(this, "Конюшня добавлена", Toast.LENGTH_SHORT).show()
                titleStable.text.clear()
                descriptionAddStable.text.clear()

                val intent = Intent(this, ListStableActivity::class.java)
                startActivity(intent)
            }

        }



    }
}