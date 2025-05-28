package com.example.stablemanager.Components.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Components.Managers.EmployeeManager
import com.example.stablemanager.Components.byteArrayToBitmap
import com.example.stablemanager.Pages.AdminPages.Fragments.EditRoleFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.Pages.OwnerPages.Fragments.EditEmployeeFragment
import com.example.stablemanager.Pages.OwnerPages.StartOwnerPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Employee

class EmployeeAdapter(private var employees: List<Employee>, private val activity: Activity, private var context: Context, private val admin: Boolean) : RecyclerView.Adapter<EmployeeAdapter.MyViewHolder>(){
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image: ImageView = itemView.findViewById(R.id.employeeLogoImage)
        val fullname: TextView = itemView.findViewById(R.id.employeeListFullname)
        val role: TextView = itemView.findViewById(R.id.employeeListRole)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.employee_admin_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return employees.count()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val db = DBHelper(context, null)
        val employee = employees[position]
        holder.fullname.text = "${employee.surname} ${employee.name} ${employee.patronymic}"
        val role = db.getRolesById(employee.roleId)
        if(role != null){
            holder.role.text = role.title
        }
        val byteArray: ByteArray = employee.imageProfile
        val bitmap = byteArrayToBitmap(byteArray)

        if (bitmap != null) {
            holder.image.setImageBitmap(bitmap)
        }


        val employeeManager = EmployeeManager(context)
        val idEmployee = db.getIdEmployee(employee.email, employee.login)

        holder.itemView.setOnClickListener {
            if(idEmployee != null){
                employeeManager.saveEmployeeId(idEmployee)
            }
            if(admin){
                val activity = activity as? StartAdminPageActivity
                if (activity != null) {
                    activity.replaceFragment(EditRoleFragment.newInstance(), EditRoleFragment.TAG)
                } else {
                    Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
                }
            }
            else{
                val activity = activity as? StartOwnerPageActivity
                if (activity != null) {
                    activity.replaceFragment(EditEmployeeFragment.newInstance(), EditEmployeeFragment.TAG)
                } else {
                    Log.e("OptionsFragment", "StartOwnerPageActivity не найдена")
                }
            }
        }
    }
}