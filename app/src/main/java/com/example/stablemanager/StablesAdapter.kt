package com.example.stablemanager

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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

        holder.btn.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddStableActivity::class.java)

            holder.itemView.context.startActivity(intent)
        }
    }
}