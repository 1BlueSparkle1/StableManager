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
import androidx.appcompat.app.AlertDialog
import com.example.stablemanager.Components.Managers.TypeBreedManager
import com.example.stablemanager.Components.setEditable
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.TypeBreed

class EditTypeBreedFragment : Fragment() {
    companion object{
        val TAG: String = EditTypeBreedFragment::class.java.simpleName
        fun newInstance() = EditTypeBreedFragment()
    }

    private lateinit var typeBreedManager: TypeBreedManager
    private lateinit var typeBreed: TypeBreed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        typeBreedManager = TypeBreedManager(requireContext())
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_type_breed, container, false)

        val titleTypeBreed: EditText = view.findViewById(R.id.editTitleTypeBreed)
        val deleteButton: Button = view.findViewById(R.id.deleteTypeBreedButton)
        val db = DBHelper(requireContext(), null)
        val typeBreedId = typeBreedManager.getTypeBreedId()

        if (typeBreedId != -1) {
            typeBreed = db.getTypeBreedById(typeBreedId)!!
            titleTypeBreed.setText(typeBreed.title)

        } else {
            Toast.makeText(requireContext(), "Произошла ошибка загрузки типа породы.", Toast.LENGTH_SHORT).show()
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(TypeBreedAdminFragment.newInstance(), TypeBreedAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        val editTypeBreedButton: Button = view.findViewById(R.id.editTypeBreedButton)
        val saveTypeBreedButton: Button = view.findViewById(R.id.SaveTypeBreedButton)

        editTypeBreedButton.setOnClickListener {
            titleTypeBreed.setEditable(true)

            editTypeBreedButton.visibility = View.GONE
            saveTypeBreedButton.visibility = View.VISIBLE
        }

        saveTypeBreedButton.setOnClickListener {
            val title = titleTypeBreed.text.toString().trim()

            if(title == ""){
                Toast.makeText(requireContext(), "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            }
            else{
                titleTypeBreed.setEditable(false)

                db.updateTypeBreed(typeBreedId, title)

                editTypeBreedButton.visibility = View.VISIBLE
                saveTypeBreedButton.visibility = View.GONE
                Toast.makeText(requireContext(), "Тип породы изменен", Toast.LENGTH_SHORT).show()
            }
        }

        deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Удаление типа породы")
            builder.setMessage("Вы уверены, что хотите удалить этот тип породы?\nОн будет убран у всех пород.")
            builder.setPositiveButton("Да") { dialog, which ->
                db.deleteTypeBreed(typeBreedId)
                val activity = activity as? StartAdminPageActivity

                if (activity != null) {
                    activity.replaceFragment(TypeBreedAdminFragment.newInstance(), TypeBreedAdminFragment.TAG)
                } else {
                    Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
                }
                Toast.makeText(requireContext(), "тип породы удален", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Отмена") { dialog, which ->
            }

            builder.show()
        }

        return view
    }
}