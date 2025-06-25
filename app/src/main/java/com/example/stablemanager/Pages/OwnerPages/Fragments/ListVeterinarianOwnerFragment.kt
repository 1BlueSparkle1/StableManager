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
import com.example.stablemanager.Components.Adapters.VeterinarianAdapter
import com.example.stablemanager.Components.Managers.StableManager
import com.example.stablemanager.Pages.OwnerPages.StartOwnerPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper


class ListVeterinarianOwnerFragment : Fragment() {
    companion object{
        val TAG: String = ListVeterinarianOwnerFragment::class.java.simpleName
        fun newInstance() = ListVeterinarianOwnerFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list_veterinarian_owner, container, false)

        val db = DBHelper(requireContext(), null)
        val veterinarianList: RecyclerView = view.findViewById(R.id.veterinariansListView)
        val addVeterinarianBtn: Button = view.findViewById(R.id.addVeterinarianAdminButton)

        val stableManager = StableManager(requireContext())

        val veterinarians = db.getAllVeterinariansInStable(stableManager.getStableId())
        veterinarianList.layoutManager = LinearLayoutManager(requireContext())
        val activity = activity as StartOwnerPageActivity
        veterinarianList.adapter = VeterinarianAdapter(veterinarians, activity, requireContext(), false)

        addVeterinarianBtn.setOnClickListener {
            val activity = activity as? StartOwnerPageActivity

            if (activity != null) {
                activity.replaceFragment(AddVeterinarianOwnerFragment.newInstance(), AddVeterinarianOwnerFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartOwnerPageActivity не найдена")
            }
        }

        return view
    }
}