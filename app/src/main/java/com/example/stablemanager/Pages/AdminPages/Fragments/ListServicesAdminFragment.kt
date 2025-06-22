package com.example.stablemanager.Pages.AdminPages.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.AddServiceAdminFragment
import com.example.stablemanager.Components.Adapters.RoleAdapter
import com.example.stablemanager.Components.Adapters.ServiceAdapter
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper


class ListServicesAdminFragment : Fragment() {
    companion object{
        val TAG: String = ListServicesAdminFragment::class.java.simpleName
        fun newInstance() = ListServicesAdminFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list_services_admin, container, false)

        val db = DBHelper(requireContext(), null)
        val serviceList: RecyclerView = view.findViewById(R.id.serviceAdminView)
        val addServiceBtn: Button = view.findViewById(R.id.addServiceButton)

        val services = db.getServices()
        serviceList.layoutManager = LinearLayoutManager(requireContext())
        val activity = activity as StartAdminPageActivity
        serviceList.adapter = ServiceAdapter(services, activity, requireContext())

        addServiceBtn.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(AddServiceAdminFragment.newInstance(), AddServiceAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        return view
    }
}