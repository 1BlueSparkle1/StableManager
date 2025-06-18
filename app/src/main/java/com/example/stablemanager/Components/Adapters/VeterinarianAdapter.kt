package com.example.stablemanager.Components.Adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Components.Managers.BreedManager
import com.example.stablemanager.Pages.AdminPages.Fragments.EditBreedAdminFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.Breed
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Veterinarian

class VeterinarianAdapter(private var veterinarians: List<Veterinarian>, private val activity: Activity, private var context: Context) : RecyclerView.Adapter<VeterinarianAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.veterinarianAdminListName)
        val phone: TextView = itemView.findViewById(R.id.veterinarianListPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.veterinarian_admin_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return veterinarians.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val veterinarian = veterinarians[position]
        holder.name.text = veterinarian.fullname
        holder.phone.text = veterinarian.phone

//        val db = DBHelper(context, null)
//        val breedManager = BreedManager(context)
//        val idBreed = db.getIdBreed(breed.title, breed.typeBreedId)

        holder.itemView.setOnClickListener {
//            val activity = activity as? StartAdminPageActivity
//
//            if (activity != null) {
//                if(idBreed != null){
//                    breedManager.saveBreedId(idBreed)
//                }
//                activity.replaceFragment(EditBreedAdminFragment.newInstance(), EditBreedAdminFragment.TAG)
//            } else {
//                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
//            }
        }
    }
}