package com.example.stablemanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StablesAdapter(var stables: List<Stable>, var context: Context) : RecyclerView.Adapter<StablesAdapter.MyViewHolder>() {
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.stableListTitle)
        val description: TextView = view.findViewById(R.id.stableListDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stable_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stables.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = stables[position].title
        holder.description.text = stables[position].description
    }
}