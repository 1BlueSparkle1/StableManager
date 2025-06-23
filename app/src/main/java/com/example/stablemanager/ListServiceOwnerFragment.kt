package com.example.stablemanager

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
import com.example.stablemanager.Components.Adapters.ServiceAdapter
import com.example.stablemanager.Components.Managers.StableManager
import com.example.stablemanager.Pages.AdminPages.Fragments.ListServicesAdminFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.Pages.OwnerPages.StartOwnerPageActivity
import com.example.stablemanager.db.DBHelper


class ListServiceOwnerFragment : Fragment() {
    companion object{
        val TAG: String = ListServiceOwnerFragment::class.java.simpleName
        fun newInstance() = ListServiceOwnerFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list_service_owner, container, false)

        val db = DBHelper(requireContext(), null)
        val serviceList: RecyclerView = view.findViewById(R.id.serviceAdminView)
        val addServiceBtn: Button = view.findViewById(R.id.addServiceButton)

        val stableManager = StableManager(requireContext())

        val services = db.getServicesInStable(stableManager.getStableId())
        serviceList.layoutManager = LinearLayoutManager(requireContext())
        val activity = activity as StartOwnerPageActivity
        serviceList.adapter = ServiceAdapter(services, activity, requireContext(), false)

        addServiceBtn.setOnClickListener {
            val activity = activity as? StartOwnerPageActivity

            if (activity != null) {
                activity.replaceFragment(AddServiceAdminFragment.newInstance(), AddServiceAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartOwnerPageActivity не найдена")
            }
        }

        return view
    }
}