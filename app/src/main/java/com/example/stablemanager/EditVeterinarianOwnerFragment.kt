package com.example.stablemanager

import android.annotation.SuppressLint
import android.content.res.ColorStateList
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
import androidx.core.content.ContextCompat
import com.example.stablemanager.Components.Managers.ServiceManager
import com.example.stablemanager.Components.Managers.VeterinarianManager
import com.example.stablemanager.Components.setEditable
import com.example.stablemanager.Pages.AdminPages.Fragments.ListServicesAdminFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.Pages.OwnerPages.Fragments.ListVeterinarianOwnerFragment
import com.example.stablemanager.Pages.OwnerPages.StartOwnerPageActivity
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Service
import com.example.stablemanager.db.Veterinarian


class EditVeterinarianOwnerFragment : Fragment() {
    companion object{
        val TAG: String = EditVeterinarianOwnerFragment::class.java.simpleName
        fun newInstance() = EditVeterinarianOwnerFragment()
    }

    private lateinit var veterinarianManager: VeterinarianManager

    private lateinit var fullNameEditText: EditText
    private lateinit var phoneEditText: EditText
    private  lateinit var deleteButton: Button
    private lateinit var veterinarian: Veterinarian

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        veterinarianManager = VeterinarianManager(requireContext())
    }

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_veterinarian_owner, container, false)

        fullNameEditText = view.findViewById(R.id.editFioVeterinarian)
        phoneEditText = view.findViewById(R.id.phoneVeterinarianAdmin)
        deleteButton = view.findViewById(R.id.deleteVeterinarianButton)

        val db = DBHelper(requireContext(), null)
        val veterinarianId = veterinarianManager.getVeterinarianId()

        if (veterinarianId != -1) {
            veterinarian = db.getVeterinarianById(veterinarianId)!!
            fullNameEditText.setText(veterinarian.fullname)
            phoneEditText.setText(veterinarian.phone)

        } else {
            Toast.makeText(requireContext(), "Произошла ошибка загрузки ветеринара.", Toast.LENGTH_SHORT).show()
            val activity = activity as? StartOwnerPageActivity

            if (activity != null) {
                activity.replaceFragment(ListVeterinarianOwnerFragment.newInstance(), ListVeterinarianOwnerFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartOwnerPageActivity не найдена")
            }
        }

        val editVeterinarianButton: Button = view.findViewById(R.id.EditVeterinarianButtonPage)
        val saveVeterinarianButton: Button = view.findViewById(R.id.SaveVeterinarianButtonPage)

        editVeterinarianButton.setOnClickListener {
            fullNameEditText.setEditable(true)
            phoneEditText.setEditable(true)

            editVeterinarianButton.visibility = View.GONE
            saveVeterinarianButton.visibility = View.VISIBLE
        }

        saveVeterinarianButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()

            if(fullName == "" || phone == ""){
                Toast.makeText(requireContext(), "Поля ФИО и телефона должны быть заполнены", Toast.LENGTH_SHORT).show()
            }
            else{
                fullNameEditText.setEditable(false)
                phoneEditText.setEditable(false)

                db.updateVeterinarian(veterinarianId, fullName, phone, veterinarian.stableId)
                Toast.makeText(requireContext(), "Ветеринар изменен", Toast.LENGTH_SHORT).show()

                editVeterinarianButton.visibility = View.VISIBLE
                saveVeterinarianButton.visibility = View.GONE
            }
        }

        deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Удаление Услуги")
            builder.setMessage("Вы уверены, что хотите удалить этого ветеринара?\nУ лошадей он будет заменен системным ветеринаром, если такой существует.")
            builder.setPositiveButton("Да") { dialog, which ->
                db.deleteVeterinarianAndReassignHorses(veterinarianId)
                val activity = activity as? StartOwnerPageActivity

                if (activity != null) {
                    activity.replaceFragment(ListVeterinarianOwnerFragment.newInstance(), ListVeterinarianOwnerFragment.TAG)
                } else {
                    Log.e("OptionsFragment", "StartOwnerPageActivity не найдена")
                }
                Toast.makeText(requireContext(), "Ветеринар удален", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Отмена") { dialog, which ->
            }

            builder.show()
        }

        return view
    }
}