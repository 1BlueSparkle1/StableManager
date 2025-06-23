package com.example.stablemanager.Components.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Components.Managers.StableManager
import com.example.stablemanager.Pages.OwnerPages.Fragments.EditEmployeeFragment
import com.example.stablemanager.Pages.OwnerPages.Fragments.NotifyOwnerFragment
import com.example.stablemanager.Pages.OwnerPages.StartOwnerPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Notification
import com.example.stablemanager.db.Stable

class NotifyAdapter(private var notifies: List<Notification>, private val activity: StartOwnerPageActivity, private var context: Context) : RecyclerView.Adapter<NotifyAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val description: TextView = itemView.findViewById(R.id.notifyListDescription)
        val date: TextView = itemView.findViewById(R.id.notifyListDate)
        val sender: TextView = itemView.findViewById(R.id.NotifyListSender)
        val btn: Button = itemView.findViewById(R.id.ReadButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notify_in_owner_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notifies.count()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val notify = notifies[position]
        holder.description.text = notify.description
        holder.date.text = notify.creationDate

        val db = DBHelper(context, null)
        if(notify.senderEmployeeId != null){
            val employee = db.getEmployeeById(notify.senderEmployeeId)
            holder.sender.text = "Администратор: ${employee!!.surname} ${employee.name.first().uppercaseChar()}. ${employee.patronymic.first().uppercaseChar()}."
        }
        else{
            holder.sender.text = "Система"
        }

        holder.btn.setOnClickListener {
            db.notifyIsRead(notify.id)
            val activity = activity as? StartOwnerPageActivity
            if (activity != null) {
                activity.replaceFragment(NotifyOwnerFragment.newInstance(), NotifyOwnerFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartOwnerPageActivity не найдена")
            }
        }
    }
}