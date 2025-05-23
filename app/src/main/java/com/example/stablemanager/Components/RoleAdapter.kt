package com.example.stablemanager.Components

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Pages.OwnerPages.StartOwnerPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Role

class RoleAdapter(private var roles: List<Role>, private var context: Context) : RecyclerView.Adapter<RoleAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.roleListTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.role_admin_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roles.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val role = roles[position]
        holder.title.text = role.title

        val stableManager = StableManager(context)
        val db = DBHelper(context, null)
//        val idRole = db.getIdStable(stable.title, stable.description, stable.ownerId)
//
//
//        holder.btn.setOnClickListener {
//            val intent = Intent(holder.itemView.context, StartOwnerPageActivity::class.java)
//            if(idStable != null){
//                stableManager.saveStableId(idStable)
//            }
//            holder.itemView.context.startActivity(intent)
//        }
    }
}