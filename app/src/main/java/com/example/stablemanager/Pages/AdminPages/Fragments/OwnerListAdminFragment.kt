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
import com.example.stablemanager.Components.Adapters.OwnerAdapter
import com.example.stablemanager.Components.Adapters.RoleAdapter
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper


class OwnerListAdminFragment : Fragment() {
    companion object{
        val TAG: String = OwnerListAdminFragment::class.java.simpleName
        fun newInstance() = OwnerListAdminFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_owner_list_admin, container, false)

        val db = DBHelper(requireContext(), null)
        val ownerList: RecyclerView = view.findViewById(R.id.ownerListView)
        val addOwnerBtn: Button = view.findViewById(R.id.addOwnerButton)

        val owners = db.getOwners()
        ownerList.layoutManager = LinearLayoutManager(requireContext())
        val activity = activity as StartAdminPageActivity
        if(owners != null){
            ownerList.adapter = OwnerAdapter(owners, activity, requireContext())
        }

        addOwnerBtn.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(AddOwnerAdminFragment.newInstance(), AddOwnerAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        return view
    }
}