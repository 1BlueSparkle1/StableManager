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
import com.example.stablemanager.Components.Adapters.EmployeeAdapter
import com.example.stablemanager.Components.Adapters.HorseAdapter
import com.example.stablemanager.Pages.AdminPages.Fragments.AddRoleFragment
import com.example.stablemanager.Pages.AdminPages.Fragments.EmployeeListAdminFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.db.DBHelper


class HorseListAdminFragment : Fragment() {
    companion object{
        val TAG: String = HorseListAdminFragment::class.java.simpleName
        fun newInstance() = HorseListAdminFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_horse_list_admin, container, false)

        val db = DBHelper(requireContext(), null)
        val horseList: RecyclerView = view.findViewById(R.id.horseListView)
        val addHorseBtn: Button = view.findViewById(R.id.addHorseAdminButton)

        val horses = db.getAllHorses()
        horseList.layoutManager = LinearLayoutManager(requireContext())
        val activity = activity as StartAdminPageActivity
        horseList.adapter = HorseAdapter(horses, activity, requireContext())

        addHorseBtn.setOnClickListener {
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