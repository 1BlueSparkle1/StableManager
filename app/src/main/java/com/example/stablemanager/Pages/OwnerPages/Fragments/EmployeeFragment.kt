package com.example.stablemanager.Pages.OwnerPages.Fragments

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
import com.example.stablemanager.AddEmployeeFragment
import com.example.stablemanager.Components.Adapters.EmployeeAdapter
import com.example.stablemanager.Components.Managers.StableManager
import com.example.stablemanager.Pages.AdminPages.Fragments.AddEmployeeAdminFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.Pages.OwnerPages.StartOwnerPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper


class EmployeeFragment : Fragment() {
    companion object{
        val TAG: String = EmployeeFragment::class.java.simpleName
        fun newInstance() = EmployeeFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_employee, container, false)

        val db = DBHelper(requireContext(), null)
        val employeeList: RecyclerView = view.findViewById(R.id.employeeListView)
        val addEmployeeBtn: Button = view.findViewById(R.id.addEmployeeButton)
        val stableManager = StableManager(requireContext())

        val employees = db.getEmployees(stableManager.getStableId())
        employeeList.layoutManager = LinearLayoutManager(requireContext())
        val activity = activity as StartOwnerPageActivity
        employeeList.adapter = EmployeeAdapter(employees, activity, requireContext(), false)

        addEmployeeBtn.setOnClickListener {
            val activity = activity as? StartOwnerPageActivity

            if (activity != null) {
                activity.replaceFragment(AddEmployeeFragment.newInstance(), AddEmployeeFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        return view
    }
}