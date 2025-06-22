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
import com.example.stablemanager.Components.Adapters.VeterinarianAdapter
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper


class ListVeterinarianAdminFragment : Fragment() {
    companion object{
        val TAG: String = ListVeterinarianAdminFragment::class.java.simpleName
        fun newInstance() = ListVeterinarianAdminFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list_veterinarian_admin, container, false)

        val db = DBHelper(requireContext(), null)
        val veterinarianList: RecyclerView = view.findViewById(R.id.veterinariansListView)
        val addVeterinarianBtn: Button = view.findViewById(R.id.addVeterinarianAdminButton)

        val veterinarians = db.getAllVeterinarians()
        veterinarianList.layoutManager = LinearLayoutManager(requireContext())
        val activity = activity as StartAdminPageActivity
        veterinarianList.adapter = VeterinarianAdapter(veterinarians, activity, requireContext())

        addVeterinarianBtn.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(AddVeterinarianAdminFragment.newInstance(), AddVeterinarianAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        return view
    }
}