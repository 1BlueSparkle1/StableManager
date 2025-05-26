package com.example.stablemanager.Pages.AdminPages.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.AddEmployeeAdminFragment
import com.example.stablemanager.Components.Adapters.EmployeeAdapter
import com.example.stablemanager.Components.Managers.StableAdminAdapter
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper

class EmployeeListAdminFragment : Fragment() {
    companion object{
        val TAG: String = EmployeeListAdminFragment::class.java.simpleName
        fun newInstance() = EmployeeListAdminFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_employee_list_admin, container, false)

        val db = DBHelper(requireContext(), null)
        val employeeList: RecyclerView = view.findViewById(R.id.employeeListView)
        val addEmployeeBtn: Button = view.findViewById(R.id.addEmployeeButton)

        val employees = db.getAllEmployees()
        employeeList.layoutManager = LinearLayoutManager(requireContext())
        val activity = activity as StartAdminPageActivity
        if(employees != null){
            employeeList.adapter = EmployeeAdapter(employees, activity, requireContext())
        }

        addEmployeeBtn.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(AddEmployeeAdminFragment.newInstance(), AddEmployeeAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        return view
    }
}