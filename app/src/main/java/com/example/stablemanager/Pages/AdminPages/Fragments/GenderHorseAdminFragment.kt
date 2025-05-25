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
import com.example.stablemanager.Components.Adapters.GenderHorseAdapter
import com.example.stablemanager.Components.Adapters.TypeBreedAdapter
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper


class GenderHorseAdminFragment : Fragment() {
    companion object{
        val TAG: String = GenderHorseAdminFragment::class.java.simpleName
        fun newInstance() = GenderHorseAdminFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gender_horse_admin, container, false)

        val db = DBHelper(requireContext(), null)
        val genderHorseList: RecyclerView = view.findViewById(R.id.genderHorseListView)
        val addGenderHorseBtn: Button = view.findViewById(R.id.addGenderHorseAdminButton)

        val genderHorses = db.getGenderHorse()
        genderHorseList.layoutManager = LinearLayoutManager(requireContext())
        val activity = activity as StartAdminPageActivity
        genderHorseList.adapter = GenderHorseAdapter(genderHorses, activity, requireContext())

        addGenderHorseBtn.setOnClickListener {
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