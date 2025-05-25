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
import com.example.stablemanager.Components.Adapters.TypeBreedAdapter
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper

class TypeBreedAdminFragment : Fragment() {
    companion object{
        val TAG: String = TypeBreedAdminFragment::class.java.simpleName
        fun newInstance() = TypeBreedAdminFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_type_breed_admin, container, false)

        val db = DBHelper(requireContext(), null)
        val typeBreedList: RecyclerView = view.findViewById(R.id.typeBreedListView)
        val addTypeBreedBtn: Button = view.findViewById(R.id.addTypeBreedAdminButton)

        val typeBreeds = db.getTypeBreed()
        typeBreedList.layoutManager = LinearLayoutManager(requireContext())
        val activity = activity as StartAdminPageActivity
        typeBreedList.adapter = TypeBreedAdapter(typeBreeds, activity, requireContext())

        addTypeBreedBtn.setOnClickListener {
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