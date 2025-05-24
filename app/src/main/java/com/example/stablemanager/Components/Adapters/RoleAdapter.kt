package com.example.stablemanager.Components.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Components.Managers.RoleManagers
import com.example.stablemanager.Components.Managers.StableManager
import com.example.stablemanager.Pages.AdminPages.Fragments.EditRoleFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Role

class RoleAdapter(private var roles: List<Role>, private val activity: StartAdminPageActivity, private var context: Context) : RecyclerView.Adapter<RoleAdapter.MyViewHolder>() {

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

        val roleManager = RoleManagers(context)
        val db = DBHelper(context, null)
        val idRole = db.getIdRole(role.title)

        holder.itemView.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                if(idRole != null){
                    roleManager.saveRoleId(idRole)
                }
                activity.replaceFragment(EditRoleFragment.newInstance(), EditRoleFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }
    }
}