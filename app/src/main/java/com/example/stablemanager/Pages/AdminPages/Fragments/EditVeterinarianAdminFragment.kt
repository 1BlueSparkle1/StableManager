package com.example.stablemanager.Pages.AdminPages.Fragments

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
import com.example.stablemanager.Components.Managers.VeterinarianManager
import com.example.stablemanager.Components.setEditable
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Veterinarian


class EditVeterinarianAdminFragment : Fragment() {
    companion object{
        val TAG: String = EditVeterinarianAdminFragment::class.java.simpleName
        fun newInstance() = EditVeterinarianAdminFragment()
    }

    private lateinit var veterinarianManager: VeterinarianManager

    private lateinit var fullNameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var stableTextView: EditText
    private lateinit var stableButton: Button
    private  lateinit var deleteButton: Button
    private lateinit var veterinarian: Veterinarian

    private var selectedStableId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        veterinarianManager = VeterinarianManager(requireContext())
    }

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_veterinarian_admin, container, false)

        fullNameEditText = view.findViewById(R.id.EditFioVeterinarian)
        phoneEditText = view.findViewById(R.id.phoneVeterinarianAdmin)
        stableTextView = view.findViewById(R.id.addStableIdVeterinarian)
        stableButton = view.findViewById(R.id.addStableVeterinarianButton)
        deleteButton = view.findViewById(R.id.deleteVeterinarianButton)

        stableButton.setOnClickListener { showStableSelectionDialog() }

        val db = DBHelper(requireContext(), null)
        val veterinarianId = veterinarianManager.getVeterinarianId()

        if (veterinarianId != -1) {
            veterinarian = db.getVeterinarianById(veterinarianId)!!
            fullNameEditText.setText(veterinarian.fullname)
            phoneEditText.setText(veterinarian.phone)
            if(veterinarian.stableId != null){
                val stable = db.getStableById(veterinarian.stableId!!)
                if (stable != null){
                    stableTextView.setText(stable.title)
                }
            }

        } else {
            Toast.makeText(requireContext(), "Произошла ошибка загрузки ветеринара.", Toast.LENGTH_SHORT).show()
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(ListVeterinarianAdminFragment.newInstance(), ListVeterinarianAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        val editVeterinarianButton: Button = view.findViewById(R.id.EditVeterinarianButtonPage)
        val saveVeterinarianButton: Button = view.findViewById(R.id.SaveVeterinarianButtonPage)

        editVeterinarianButton.setOnClickListener {
            fullNameEditText.setEditable(true)
            phoneEditText.setEditable(true)
            stableButton.isEnabled = true
            stableButton.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(),
                    R.color.brown_text
                ))

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
                stableButton.isEnabled = false
                stableButton.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(),
                        R.color.light_brown_text
                    ))


                if (selectedStableId == -1){
                    if(veterinarian.stableId != null){
                        selectedStableId = veterinarian.stableId!!
                    }
                }

                if(selectedStableId == -1){
                    db.updateVeterinarian(veterinarianId, fullName, phone, null)
                }
                else{
                    db.updateVeterinarian(veterinarianId, fullName, phone, selectedStableId)
                }
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
                val activity = activity as? StartAdminPageActivity

                if (activity != null) {
                    activity.replaceFragment(ListVeterinarianAdminFragment.newInstance(), ListVeterinarianAdminFragment.TAG)
                } else {
                    Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
                }
                Toast.makeText(requireContext(), "Ветеринар удален", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Отмена") { dialog, which ->
            }

            builder.show()
        }

        return view
    }

    private fun showStableSelectionDialog() {
        val db = DBHelper(requireContext(), null)
        val stables = db.getAllStables()
        val stableTitles = stables.map { it.title }.toTypedArray()

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Выберите конюшню")
        builder.setItems(stableTitles) { _, which ->
            selectedStableId = db.getIdStable(stables[which].title, stables[which].description, stables[which].ownerId)!!
            stableTextView.setText(stables[which].title)
        }
        builder.show()
    }
}