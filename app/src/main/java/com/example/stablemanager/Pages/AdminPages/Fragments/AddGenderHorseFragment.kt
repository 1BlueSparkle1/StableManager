package com.example.stablemanager.Pages.AdminPages.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.GenderHorse

class AddGenderHorseFragment : Fragment() {
    companion object{
        val TAG: String = AddGenderHorseFragment::class.java.simpleName
        fun newInstance() = AddGenderHorseFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_gender_horse, container, false)

        val db = DBHelper(requireContext(), null)
        val titleGenderHorse: EditText = view.findViewById(R.id.addTitleGenderHorse)
        val addGenderHorseBtn: Button = view.findViewById(R.id.SaveGenderHorsePage)

        addGenderHorseBtn.setOnClickListener {
            val title = titleGenderHorse.text.toString().trim()
            if(title == ""){
                Toast.makeText(requireContext(), "Название должно быть заполнено", Toast.LENGTH_SHORT).show()
            }
            else{
                db.addGenderHorse(GenderHorse(title))
                Toast.makeText(requireContext(), "Пол лошадей сохранен", Toast.LENGTH_SHORT).show()
                val activity = activity as? StartAdminPageActivity

                if (activity != null) {
                    activity.replaceFragment(GenderHorseAdminFragment.newInstance(), GenderHorseAdminFragment.TAG)
                } else {
                    Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
                }
            }
        }

        return view
    }
}