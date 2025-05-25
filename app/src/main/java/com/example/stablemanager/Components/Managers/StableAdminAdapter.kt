package com.example.stablemanager.Components.Managers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Components.Adapters.OwnerAdapter
import com.example.stablemanager.Pages.AdminPages.Fragments.EditRoleFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Owner
import com.example.stablemanager.db.Stable

class StableAdminAdapter(private var stables: List<Stable>, private val activity: StartAdminPageActivity, private var context: Context) : RecyclerView.Adapter<StableAdminAdapter.MyViewHolder>(){
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.stableAdminListTitle)
        val owner: TextView = itemView.findViewById(R.id.stableListOwner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stable_admin_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stables.count()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val db = DBHelper(context, null)
        val stable = stables[position]
        holder.title.text = stable.title
        val owner = db.getOwnerById(stable.ownerId)
        holder.owner.text = "${owner!!.surname} ${owner.fullname} ${owner.patronymic}"

        val stableManager = StableManager(context)
        val idStable = db.getIdStable(stable.title, stable.description, stable.ownerId)

        holder.itemView.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                if(idStable != null){
                    stableManager.saveStableId(idStable)
                }
                activity.replaceFragment(EditRoleFragment.newInstance(), EditRoleFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }
    }
}