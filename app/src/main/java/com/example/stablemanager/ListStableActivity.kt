package com.example.stablemanager

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

        val stableList: RecyclerView = findViewById(R.id.stableView)
        val stable = arrayListOf<Stable>()

        stable.add(Stable(1, "Конюшня 1", "Первая конюшня", 1))
        stable.add(Stable(2, "Конюшня 2", "Представьте себе место, где время замедляется, где суета современной жизни уступает место умиротворяющему шепоту лошадей, где воздух напоен ароматом свежего сена, кожи и теплого дерева. Это не просто конюшня, это убежище, где вековые традиции коневодства переплетаются с современными удобствами, создавая идеальную гармонию для всадников и их благородных спутников. Добро пожаловать в [Название Конюшни], место, где рождаются чемпионы, а простые прогулки верхом превращаются в незабываемые приключения.", 1))

        stableList.layoutManager = LinearLayoutManager(this)
        stableList.adapter = StablesAdapter(stable, this)
    }
}