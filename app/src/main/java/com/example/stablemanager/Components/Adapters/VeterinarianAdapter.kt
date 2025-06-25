package com.example.stablemanager.Components.Adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Components.Managers.BreedManager
import com.example.stablemanager.Components.Managers.VeterinarianManager
import com.example.stablemanager.EditVeterinarianAdminFragment
import com.example.stablemanager.EditVeterinarianOwnerFragment
import com.example.stablemanager.Pages.AdminPages.Fragments.EditBreedAdminFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.Pages.OwnerPages.StartOwnerPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.Breed
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Veterinarian

class VeterinarianAdapter(private var veterinarians: List<Veterinarian>, private val activity: Activity, private var context: Context, private val admin: Boolean) : RecyclerView.Adapter<VeterinarianAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.veterinarianAdminListName)
        val phone: TextView = itemView.findViewById(R.id.veterinarianListPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.veterinarian_admin_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return veterinarians.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val veterinarian = veterinarians[position]
        holder.name.text = veterinarian.fullname
        holder.phone.text = veterinarian.phone

        val db = DBHelper(context, null)
        val veterinarianManager = VeterinarianManager(context)
        val idVeterinarian = if(veterinarian.stableId == 0){
            db.getIdVeterinarian(veterinarian.fullname, veterinarian.phone, null)
        } else{
            db.getIdVeterinarian(veterinarian.fullname, veterinarian.phone, veterinarian.stableId)
        }

        holder.itemView.setOnClickListener {
            if (admin) {
                val adminActivity = activity as? StartAdminPageActivity
                if (adminActivity != null) {
                    if (idVeterinarian != null) {
                        veterinarianManager.saveVeterinarianId(idVeterinarian)
                    } else {
                        Log.w("MyAdapter", "Veterinarian ID not found for admin. Proceeding to edit, but check data integrity.")
                        Toast.makeText(context, "Предупреждение: ID ветеринара не найден.", Toast.LENGTH_SHORT).show()
                    }
                    adminActivity.replaceFragment(EditVeterinarianAdminFragment.newInstance(), EditVeterinarianAdminFragment.TAG)
                } else {
                    Log.e("MyAdapter", "Ошибка: текущая Activity не является StartAdminPageActivity для администратора.")
                    Toast.makeText(context, "Ошибка приложения. Пожалуйста, перезапустите.", Toast.LENGTH_LONG).show()
                }
            } else {
                if (veterinarian.stableId == null) {
                    Toast.makeText(context, "Ветеринара системы редактировать нельзя!", Toast.LENGTH_SHORT).show()
                    Log.d("MyAdapter", "Попытка владельца отредактировать системного ветеринара (ID: $idVeterinarian). Заблокировано.")
                } else {
                    val ownerActivity = activity as? StartOwnerPageActivity
                    if (ownerActivity != null) {
                        if (idVeterinarian != null) {
                            veterinarianManager.saveVeterinarianId(idVeterinarian)
                        } else {
                            Log.e("MyAdapter", "Ошибка: ID ветеринара не найден для владельца, хотя StableId присутствует. Обновление не будет возможно.")
                            Toast.makeText(context, "Не удалось получить ID ветеринара для редактирования.", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                        ownerActivity.replaceFragment(EditVeterinarianOwnerFragment.newInstance(), EditVeterinarianOwnerFragment.TAG)
                    } else {
                        Log.e("MyAdapter", "Ошибка: текущая Activity не является StartOwnerPageActivity для владельца.")
                        Toast.makeText(context, "Ошибка приложения. Пожалуйста, перезапустите.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}