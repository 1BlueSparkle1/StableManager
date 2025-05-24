package com.example.stablemanager.Components.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Components.Managers.StableManager
import com.example.stablemanager.Pages.OwnerPages.StartOwnerPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Stable

class StablesAdapter(private var stables: List<Stable>, private var context: Context) : RecyclerView.Adapter<StablesAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.stableListTitle)
        val description: TextView = itemView.findViewById(R.id.stableListDescription)
        val btn: Button = itemView.findViewById(R.id.stableEmptyButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stable_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stables.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val stable = stables[position]
        holder.title.text = stable.title
        holder.description.text = stable.description

        val stableManager = StableManager(context)
        val db = DBHelper(context, null)
        val idStable = db.getIdStable(stable.title, stable.description, stable.ownerId)


        holder.btn.setOnClickListener {
            val intent = Intent(holder.itemView.context, StartOwnerPageActivity::class.java)
            if(idStable != null){
                stableManager.saveStableId(idStable)
            }
            holder.itemView.context.startActivity(intent)
        }
    }
}