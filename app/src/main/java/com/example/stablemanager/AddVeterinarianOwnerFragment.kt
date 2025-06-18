package com.example.stablemanager

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
import androidx.appcompat.app.AlertDialog
import com.example.stablemanager.Components.Managers.OwnerManager
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Veterinarian


class AddVeterinarianOwnerFragment : Fragment() {
    companion object{
        val TAG: String = AddVeterinarianOwnerFragment::class.java.simpleName
        fun newInstance() = AddVeterinarianOwnerFragment()
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_veterinarian_owner, container, false)

        val db = DBHelper(requireContext(), null)
        val nameVeterinarian: EditText = view.findViewById(R.id.addFioVeterinarian)
        val phoneVeterinarian: EditText = view.findViewById(R.id.phoneVeterinarianAdmin)
        val addVeterinarianBtn: Button = view.findViewById(R.id.SaveVeterinarianAdminPage)


        addVeterinarianBtn.setOnClickListener {
            val name = nameVeterinarian.text.toString().trim()
            val phone = phoneVeterinarian.text.toString().trim()
            if(name == "" || phone == ""){
                Toast.makeText(requireContext(), "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            }
            else{
                val ownerManager = OwnerManager(requireContext())
                val ownerId = ownerManager.getOwnerId()
                val owner = db.getOwnerById(ownerId)



                db.addVeterinarian(Veterinarian(name, phone, null))
                Toast.makeText(requireContext(), "Ветеринар сохранен", Toast.LENGTH_SHORT).show()
                val activity = activity as? StartAdminPageActivity

                if (activity != null) {
                    activity.replaceFragment(
                        ListVeterinarianAdminFragment.newInstance(),
                        ListVeterinarianAdminFragment.TAG
                    )
                } else {
                    Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
                }
            }
        }

        return view
    }

}