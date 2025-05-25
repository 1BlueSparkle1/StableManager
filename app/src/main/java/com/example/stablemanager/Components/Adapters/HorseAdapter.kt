package com.example.stablemanager.Components.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Components.Managers.EmployeeManager
import com.example.stablemanager.Components.Managers.HorseManager
import com.example.stablemanager.Pages.AdminPages.Fragments.EditRoleFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Employee
import com.example.stablemanager.db.Horse

class HorseAdapter(private var horses: List<Horse>, private val activity: StartAdminPageActivity, private var context: Context) : RecyclerView.Adapter<HorseAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image: ImageView = itemView.findViewById(R.id.horseLogoImage)
        val moniker: TextView = itemView.findViewById(R.id.horseListMoniker)
        val stable: TextView = itemView.findViewById(R.id.horseListStable)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_horse_list_admin, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return horses.count()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val db = DBHelper(context, null)
        val horse = horses[position]
        holder.moniker.text = horse.moniker
        val stable = db.getStableById(horse.stableId)
        if(stable != null){
            holder.stable.text = "Расположение: ${stable.title}"
        }


        val horseManager = HorseManager(context)
        val idHorse = db.getIdHorse(horse)

        holder.itemView.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                if(idHorse != null){
                    horseManager.saveHorseId(idHorse)
                }
                activity.replaceFragment(EditRoleFragment.newInstance(), EditRoleFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }
    }
}