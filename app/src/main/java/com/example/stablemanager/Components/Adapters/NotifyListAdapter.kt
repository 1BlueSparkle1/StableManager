package com.example.stablemanager.Components.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.Pages.OwnerPages.Fragments.NotifyOwnerFragment
import com.example.stablemanager.Pages.OwnerPages.StartOwnerPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Notification

class NotifyListAdapter(private var notifies: List<Notification>, private val activity: StartAdminPageActivity, private var context: Context) : RecyclerView.Adapter<NotifyListAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val description: TextView = itemView.findViewById(R.id.notifyListDescription)
        val date: TextView = itemView.findViewById(R.id.notifyListDate)
        val sender: TextView = itemView.findViewById(R.id.NotifyListSender)
        val recipient: TextView = itemView.findViewById(R.id.NotifyListRecipient)
        val read: TextView = itemView.findViewById(R.id.NotifyListRead)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notify_in_list, parent, false)
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
            holder.sender.text = "От администратор: ${employee!!.surname} ${employee.name.first().uppercaseChar()}. ${employee.patronymic.first().uppercaseChar()}."
        }
        else if(notify.senderOwnerId != null){
            val owner = db.getOwnerById(notify.senderOwnerId)
            holder.sender.text = "От пользователь: ${owner!!.surname} ${owner.fullname.first().uppercaseChar()}. ${owner.patronymic.first().uppercaseChar()}."
        }
        else{
            holder.sender.text = "От система"
        }
        if(notify.ownerId != null){
            val owner = db.getOwnerById(notify.ownerId)
            if(owner!!.patronymic != ""){
                holder.recipient.text = "Кому - пользователь: ${owner.surname} ${owner.fullname.first().uppercaseChar()}. ${owner.patronymic.first().uppercaseChar()}."
            }
            else{
                holder.recipient.text = "Кому - пользователь: ${owner.surname} ${owner.fullname.first().uppercaseChar()}."
            }
        }
        else if(notify.employeeId != null){
            val employee = db.getEmployeeById(notify.employeeId)
            if(employee!!.patronymic != ""){
                holder.recipient.text = "Кому - администратор: ${employee.surname} ${employee.name.first().uppercaseChar()}. ${employee.patronymic.first().uppercaseChar()}."
            }
            else{
                holder.recipient.text = "Кому - администратор: ${employee.surname} ${employee.name.first().uppercaseChar()}."
            }
        }
        else{
            holder.recipient.text = "Кому - администраторам"
        }
        if(notify.isRead){
            holder.read.text = "Прочитано"
        }
        else{
            holder.read.text = "Не прочитано"
        }

    }
}