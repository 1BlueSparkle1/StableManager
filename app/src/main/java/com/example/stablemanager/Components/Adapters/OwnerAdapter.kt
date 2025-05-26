package com.example.stablemanager.Components.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Components.Managers.OwnerManager
import com.example.stablemanager.Components.Managers.RoleManagers
import com.example.stablemanager.Pages.AdminPages.Fragments.EditRoleFragment
import com.example.stablemanager.Pages.AdminPages.Fragments.OwnerListAdminFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Owner
import com.example.stablemanager.db.Role

class OwnerAdapter(private var owners: List<Owner>, private val activity: StartAdminPageActivity, private var context: Context) : RecyclerView.Adapter<OwnerAdapter.MyViewHolder>()  {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.ownerListTitle)
        val email: TextView = itemView.findViewById(R.id.ownerListEmail)
        val ban: TextView = itemView.findViewById(R.id.ownerListBan)
        val banButton: Button = itemView.findViewById(R.id.banButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.owner_admin_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return owners.count()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var owner = owners[position]
        holder.title.text = "${owner.surname} ${owner.fullname} ${owner.patronymic}"
        holder.email.text = owner.email
        if(owner.ban){
            holder.ban.setTextColor(ContextCompat.getColor(context, R.color.exit_red))
            holder.ban.text = "Заблокирован"
            holder.banButton.text = "Разбанить"
            holder.banButton.setBackgroundColor(ContextCompat.getColor(context, R.color.brown_text))
        }
        else{
            holder.ban.setTextColor(ContextCompat.getColor(context, R.color.brown_text))
            holder.ban.text = "Доступен"
            holder.banButton.text = "Забанить"
            holder.banButton.setBackgroundColor(ContextCompat.getColor(context, R.color.exit_red))
        }

        val ownerManager = OwnerManager(context)
        val db = DBHelper(context, null)
        val idOwner = db.getIdOwner(owner.email, owner.login)

        holder.itemView.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                if(idOwner != null){
                    ownerManager.saveOwnerId(idOwner)
                }
                activity.replaceFragment(EditRoleFragment.newInstance(), EditRoleFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        holder.banButton.setOnClickListener {
            if(owner.ban){
                holder.ban.setTextColor(ContextCompat.getColor(context, R.color.brown_text))
                holder.ban.text = "Доступен"
                holder.banButton.text = "Забанить"
                holder.banButton.setBackgroundColor(ContextCompat.getColor(context, R.color.exit_red))
            }
            else{
                holder.ban.setTextColor(ContextCompat.getColor(context, R.color.exit_red))
                holder.ban.text = "Заблокирован"
                holder.banButton.text = "Разбанить"
                holder.banButton.setBackgroundColor(ContextCompat.getColor(context, R.color.brown_text))
            }
            db.putBanOwner(idOwner!!, !owner.ban)
            activity.replaceFragment(OwnerListAdminFragment.newInstance(), OwnerListAdminFragment.TAG)
        }
    }
}