package com.example.stablemanager.Pages.AdminPages.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.Pages.MainActivity
import com.example.stablemanager.R

class OptionsFragment : Fragment() {
    companion object{
        val TAG: String = OptionsFragment::class.java.simpleName
        fun newInstance() = OptionsFragment()
    }

    private lateinit var exitAdminButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_options, container, false)

        exitAdminButton = view.findViewById(R.id.ExitAdminButton)
        val roleAdminButton : Button = view.findViewById(R.id.roleAdminButton)
        val ownerAdminButton : Button = view.findViewById(R.id.ownerAdminButton)
        val stableAdminButton: Button = view.findViewById(R.id.stableAdminButton)
        val employeeAdminButton : Button = view.findViewById(R.id.employeeAdminButton)
        val horseAdminButton : Button = view.findViewById(R.id.horseAdminButton)
        val typeBreedButton : Button = view.findViewById(R.id.typeBreedAdminButton)
        val breedAdminButton : Button = view.findViewById(R.id.breedAdminButton)

        val genderHorseButton: Button = view.findViewById(R.id.genderHorseAdminButton)

        roleAdminButton.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(RoleAdminPageFragment.newInstance(), RoleAdminPageFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        ownerAdminButton.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(OwnerListAdminFragment.newInstance(), OwnerListAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        stableAdminButton.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(StableListAdminFragment.newInstance(), StableListAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        employeeAdminButton.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(EmployeeListAdminFragment.newInstance(), EmployeeListAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        horseAdminButton.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(HorseListAdminFragment.newInstance(), HorseListAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        typeBreedButton.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(TypeBreedAdminFragment.newInstance(), TypeBreedAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        genderHorseButton.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(GenderHorseAdminFragment.newInstance(), GenderHorseAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        breedAdminButton.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(BreedListAdminFragment.newInstance(), BreedListAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        exitAdminButton.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}