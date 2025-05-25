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
import com.example.stablemanager.Components.Managers.StableAdminAdapter
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper

class StableListAdminFragment : Fragment() {
    companion object{
        val TAG: String = StableListAdminFragment::class.java.simpleName
        fun newInstance() = StableListAdminFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stable_list_admin, container, false)

        val db = DBHelper(requireContext(), null)
        val stableList: RecyclerView = view.findViewById(R.id.stableListView)
        val addStableBtn: Button = view.findViewById(R.id.addStableAdminButton)

        val stables = db.getAllStables()
        stableList.layoutManager = LinearLayoutManager(requireContext())
        val activity = activity as StartAdminPageActivity
        stableList.adapter = StableAdminAdapter(stables, activity, requireContext())

        addStableBtn.setOnClickListener {
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