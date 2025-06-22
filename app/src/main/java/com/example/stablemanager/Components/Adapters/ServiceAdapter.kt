package com.example.stablemanager.Components.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Components.Managers.RoleManagers
import com.example.stablemanager.Pages.AdminPages.Fragments.EditRoleFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Role
import com.example.stablemanager.db.Service

class ServiceAdapter(private var services: List<Service>, private val activity: Activity, private var context: Context) : RecyclerView.Adapter<ServiceAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.serviceAdminListTitle)
        val price: TextView = itemView.findViewById(R.id.serviceListPrice)
        val stableId: TextView = itemView.findViewById(R.id.serviceListStable)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.service_admin_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return services.count()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val service = services[position]
        val db = DBHelper(context, null)

        holder.title.text = service.title
        holder.price.text = service.price.toString()
        val stable = service.stableId?.let { db.getStableById(it) }
        if (stable != null) {
            holder.stableId.text = "Расположение: " + stable.title
        }

//        val serviceManager = RoleManagers(context)
//
//        val idRole = db.getIdRole(role.title)
//
//        holder.itemView.setOnClickListener {
//            val activity = activity as? StartAdminPageActivity
//
//            if (activity != null) {
//                if(idRole != null){
//                    roleManager.saveRoleId(idRole)
//                }
//                activity.replaceFragment(EditRoleFragment.newInstance(), EditRoleFragment.TAG)
//            } else {
//                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
//            }
//        }
    }
}