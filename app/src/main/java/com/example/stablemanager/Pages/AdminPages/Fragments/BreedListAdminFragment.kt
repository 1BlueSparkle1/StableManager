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
import com.example.stablemanager.Components.Adapters.BreedAdapter
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper


class BreedListAdminFragment : Fragment() {
    companion object{
        val TAG: String = BreedListAdminFragment::class.java.simpleName
        fun newInstance() = BreedListAdminFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_breed_list_admin, container, false)

        val db = DBHelper(requireContext(), null)
        val breedList: RecyclerView = view.findViewById(R.id.breedListView)
        val addBreedBtn: Button = view.findViewById(R.id.addBreedAdminButton)

        val breeds = db.getBreeds()
        breedList.layoutManager = LinearLayoutManager(requireContext())
        val activity = activity as StartAdminPageActivity
        breedList.adapter = BreedAdapter(breeds, activity, requireContext())

        addBreedBtn.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(AddBreedAdminFragment.newInstance(), AddBreedAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        return view
    }
}