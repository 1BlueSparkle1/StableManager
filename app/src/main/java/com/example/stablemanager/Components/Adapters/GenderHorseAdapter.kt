package com.example.stablemanager.Components.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Components.Managers.GenderHorseManager
import com.example.stablemanager.Pages.AdminPages.Fragments.EditGenderHorseFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.GenderHorse

class GenderHorseAdapter(private var genderHorses: List<GenderHorse>, private val activity: StartAdminPageActivity, private var context: Context) : RecyclerView.Adapter<GenderHorseAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.genderHorseListTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gender_horse_admin_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return genderHorses.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val genderHorse = genderHorses[position]
        holder.title.text = genderHorse.title

        val db = DBHelper(context, null)
        val genderHorseManager = GenderHorseManager(context)
        val idGenderHorse = db.getIdGenderHorse(genderHorse.title)

        holder.itemView.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                if(idGenderHorse != null){
                    genderHorseManager.saveGenderHorseId(idGenderHorse)
                }
                activity.replaceFragment(EditGenderHorseFragment.newInstance(), EditGenderHorseFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }
    }
}