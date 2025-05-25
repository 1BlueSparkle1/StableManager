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
import com.example.stablemanager.Components.Managers.HorseManager
import com.example.stablemanager.Components.Managers.TypeBreedManager
import com.example.stablemanager.Pages.AdminPages.Fragments.EditRoleFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Horse
import com.example.stablemanager.db.TypeBreed

class TypeBreedAdapter(private var typeBreeds: List<TypeBreed>, private val activity: StartAdminPageActivity, private var context: Context) : RecyclerView.Adapter<TypeBreedAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.typeBreedListTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.type_breed_admin_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return typeBreeds.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val typeBreed = typeBreeds[position]
        holder.title.text = typeBreed.title

        val db = DBHelper(context, null)
        val typeBreedManager = TypeBreedManager(context)
        val idTypeBreed = db.getIdTypeBreed(typeBreed.title)

        holder.itemView.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                if(idTypeBreed != null){
                    typeBreedManager.saveTypeBreedId(idTypeBreed)
                }
                activity.replaceFragment(EditRoleFragment.newInstance(), EditRoleFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }
    }
}