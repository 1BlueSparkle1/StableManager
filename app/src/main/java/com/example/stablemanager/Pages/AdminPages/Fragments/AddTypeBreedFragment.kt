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
import com.example.stablemanager.db.TypeBreed

class AddTypeBreedFragment : Fragment() {
    companion object{
        val TAG: String = AddTypeBreedFragment::class.java.simpleName
        fun newInstance() = AddTypeBreedFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_type_breed, container, false)

        val db = DBHelper(requireContext(), null)
        val titleTypeBreed: EditText = view.findViewById(R.id.addTitleTypeBreed)
        val addTypeBreedBtn: Button = view.findViewById(R.id.SaveTypeBreedPage)

        addTypeBreedBtn.setOnClickListener {
            val title = titleTypeBreed.text.toString().trim()
            if(title == ""){
                Toast.makeText(requireContext(), "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            }
            else{
                db.addTypeBreed(TypeBreed(title))
                Toast.makeText(requireContext(), "Тип породы сохранен", Toast.LENGTH_SHORT).show()
                val activity = activity as? StartAdminPageActivity

                if (activity != null) {
                    activity.replaceFragment(TypeBreedAdminFragment.newInstance(), TypeBreedAdminFragment.TAG)
                } else {
                    Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
                }
            }
        }

        return view
    }
}