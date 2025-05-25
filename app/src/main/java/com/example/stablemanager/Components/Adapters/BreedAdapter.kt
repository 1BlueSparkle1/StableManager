package com.example.stablemanager.Components.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Components.Managers.BreedManager
import com.example.stablemanager.Components.Managers.GenderHorseManager
import com.example.stablemanager.Pages.AdminPages.Fragments.EditRoleFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.Breed
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.GenderHorse

class BreedAdapter(private var breeds: List<Breed>, private val activity: StartAdminPageActivity, private var context: Context) : RecyclerView.Adapter<BreedAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.breedsListTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.breeds_admin_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return breeds.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val breed = breeds[position]
        holder.title.text = breed.title

        val db = DBHelper(context, null)
        val breedManager = BreedManager(context)
        val idBreed = db.getIdBreed(breed.title, breed.typeBreedId)

        holder.itemView.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                if(idBreed != null){
                    breedManager.saveBreedId(idBreed)
                }
                activity.replaceFragment(EditRoleFragment.newInstance(), EditRoleFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }
    }
}