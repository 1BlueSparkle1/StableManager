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
import com.example.stablemanager.Components.Managers.GenderHorseManager
import com.example.stablemanager.Components.Managers.TypeBreedManager
import com.example.stablemanager.Components.setEditable
import com.example.stablemanager.Pages.AdminPages.Fragments.GenderHorseAdminFragment
import com.example.stablemanager.Pages.AdminPages.Fragments.TypeBreedAdminFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.GenderHorse
import com.example.stablemanager.db.TypeBreed


class EditGenderHorseFragment : Fragment() {
    companion object{
        val TAG: String = EditGenderHorseFragment::class.java.simpleName
        fun newInstance() = EditGenderHorseFragment()
    }

    private lateinit var genderHorseManager: GenderHorseManager
    private lateinit var genderHorse: GenderHorse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        genderHorseManager = GenderHorseManager(requireContext())
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_gender_horse, container, false)

        val titleGenderHorse: EditText = view.findViewById(R.id.editTitleGenderHorse)
        val deleteButton: Button = view.findViewById(R.id.deleteGenderHorseButton)
        val db = DBHelper(requireContext(), null)
        val genderHorseId = genderHorseManager.getGenderHorseId()

        if (genderHorseId != -1) {
            genderHorse = db.getGenderHorseById(genderHorseId)!!
            titleGenderHorse.setText(genderHorse.title)

        } else {
            Toast.makeText(requireContext(), "Произошла ошибка загрузки пола лошадей.", Toast.LENGTH_SHORT).show()
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(GenderHorseAdminFragment.newInstance(), GenderHorseAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        val editGenderHorseButton: Button = view.findViewById(R.id.editGenderHorseButton)
        val saveGenderHorseButton: Button = view.findViewById(R.id.SaveGenderHorseButton)

        editGenderHorseButton.setOnClickListener {
            titleGenderHorse.setEditable(true)

            editGenderHorseButton.visibility = View.GONE
            saveGenderHorseButton.visibility = View.VISIBLE
        }

        saveGenderHorseButton.setOnClickListener {
            val title = titleGenderHorse.text.toString().trim()

            if(title == ""){
                Toast.makeText(requireContext(), "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            }
            else{
                titleGenderHorse.setEditable(false)

                db.updateGenderHorse(genderHorseId, title)

                editGenderHorseButton.visibility = View.VISIBLE
                saveGenderHorseButton.visibility = View.GONE
                Toast.makeText(requireContext(), "пол лошадей изменен", Toast.LENGTH_SHORT).show()
            }
        }

        deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Удаление пола лошадей")
            builder.setMessage("Вы уверены, что хотите удалить этот пол лошадей?\nОн будет убран у всех лошадей.")
            builder.setPositiveButton("Да") { dialog, which ->
                db.deleteGenderHorse(genderHorseId)
                val activity = activity as? StartAdminPageActivity

                if (activity != null) {
                    activity.replaceFragment(GenderHorseAdminFragment.newInstance(), GenderHorseAdminFragment.TAG)
                } else {
                    Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
                }
                Toast.makeText(requireContext(), "Пол лошадей удален", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Отмена") { dialog, which ->
            }

            builder.show()
        }

        return view
    }
}