package com.example.stablemanager.Pages.AdminPages.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Components.RoleAdapter
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper

class RoleAdminPageFragment : Fragment() {
    companion object{
        val TAG: String = RoleAdminPageFragment::class.java.simpleName
        fun newInstance() = RoleAdminPageFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_role_admin_page, container, false)

        val db = DBHelper(requireContext(), null)
        val roleList: RecyclerView = view.findViewById(R.id.roleAdminView)
        val addRoleBtn: Button = view.findViewById(R.id.addRoleButton)

        val roles = db.getRoles()
        roleList.layoutManager = LinearLayoutManager(requireContext())
        roleList.adapter = RoleAdapter(roles, requireContext())

        addRoleBtn.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(AddRoleFragment.newInstance(), AddRoleFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        return view
    }
}