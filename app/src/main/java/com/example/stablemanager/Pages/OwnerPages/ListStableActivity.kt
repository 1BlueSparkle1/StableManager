package com.example.stablemanager.Pages.OwnerPages

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Components.Managers.AuthManager
import com.example.stablemanager.Components.Adapters.StablesAdapter
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper

class ListStableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_stable)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = DBHelper(this, null)
        val stableList: RecyclerView = findViewById(R.id.stableView)
        val authManager = AuthManager(this)
        stableList.layoutManager = LinearLayoutManager(this)
        if (authManager.isLoggedIn()) {
            val userId = authManager.getUserId()
            val stable = db.getStables(userId)

            stableList.adapter = StablesAdapter(stable, this)
        }

        val addStableButton: Button = findViewById(R.id.addStableButton)

        addStableButton.setOnClickListener {
            val intent = Intent(this, AddStableActivity::class.java)
            startActivity(intent)
        }

        val profileButton: Button = findViewById(R.id.profileButton)

        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileOwnerActivity::class.java)
            startActivity(intent)
        }

    }
}