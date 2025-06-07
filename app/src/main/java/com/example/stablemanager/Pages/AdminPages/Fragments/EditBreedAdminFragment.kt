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
import com.example.stablemanager.Components.Managers.BreedManager
import com.example.stablemanager.Components.setEditable
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.Breed
import com.example.stablemanager.db.DBHelper


class EditBreedAdminFragment : Fragment() {
    companion object{
        val TAG: String = EditBreedAdminFragment::class.java.simpleName
        fun newInstance() = EditBreedAdminFragment()
    }

    private lateinit var breedManager: BreedManager
    private lateinit var breed: Breed
    private lateinit var typeBreedTextView: EditText
    private lateinit var typeBreedButton: Button
    private var selectedTypeBreedId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        breedManager = BreedManager(requireContext())
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_breed_admin, container, false)

        val titleBreed: EditText = view.findViewById(R.id.editTitleBreed)
        typeBreedButton = view.findViewById(R.id.addTypeBreedButton)
        typeBreedTextView = view.findViewById(R.id.editTypeIdBreed)
        val deleteButton: Button = view.findViewById(R.id.deleteBreedButton)
        val db = DBHelper(requireContext(), null)
        val breedId = breedManager.getBreedId()

        if (breedId != -1) {
            breed = db.getBreedById(breedId)!!
            titleBreed.setText(breed.title)
            val typeBreed = db.getTypeBreedById(breed.typeBreedId)
            if(typeBreed != null){
                typeBreedTextView.setText(typeBreed.title)
            }

        } else {
            Toast.makeText(requireContext(), "Произошла ошибка загрузки породы.", Toast.LENGTH_SHORT).show()
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(BreedListAdminFragment.newInstance(), BreedListAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        typeBreedButton.setOnClickListener { showTypeBreedSelectionDialog() }

        val editBreedButton: Button = view.findViewById(R.id.editBreedButton)
        val saveBreedButton: Button = view.findViewById(R.id.SaveBreedButton)

        editBreedButton.setOnClickListener {
            titleBreed.setEditable(true)
            typeBreedButton.isEnabled = true
            typeBreedButton.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(),
                R.color.brown_text
            ))

            editBreedButton.visibility = View.GONE
            saveBreedButton.visibility = View.VISIBLE
        }

        saveBreedButton.setOnClickListener {
            val title = titleBreed.text.toString().trim()

            if(title == ""){
                Toast.makeText(requireContext(), "Название должно быть заполнено", Toast.LENGTH_SHORT).show()
            }
            else{
                titleBreed.setEditable(false)
                typeBreedButton.isEnabled = false
                typeBreedButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),
                    R.color.light_brown_text
                ))

                if (selectedTypeBreedId == -1){
                    selectedTypeBreedId = breed.typeBreedId
                }

                db.updateBreed(breedId, title, selectedTypeBreedId)

                editBreedButton.visibility = View.VISIBLE
                saveBreedButton.visibility = View.GONE
                Toast.makeText(requireContext(), "Порода изменена", Toast.LENGTH_SHORT).show()
            }
        }

        deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Удаление породы")
            builder.setMessage("Вы уверены, что хотите удалить эту породу?\nОна будет убрана у всех лошадей.")
            builder.setPositiveButton("Да") { dialog, which ->
                db.deleteBreed(breedId)
                val activity = activity as? StartAdminPageActivity

                if (activity != null) {
                    activity.replaceFragment(BreedListAdminFragment.newInstance(), BreedListAdminFragment.TAG)
                } else {
                    Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
                }
                Toast.makeText(requireContext(), "Порода удалена", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Отмена") { dialog, which ->
            }

            builder.show()
        }

        return view
    }

    private fun showTypeBreedSelectionDialog() {
        val db = DBHelper(requireContext(), null)
        val typeBreeds = db.getTypeBreed()
        val typeBreedTitles = typeBreeds.map { it.title }.toTypedArray()

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Выберите тип породы")
        builder.setItems(typeBreedTitles) { _, which ->
            selectedTypeBreedId = db.getIdTypeBreed(typeBreeds[which].title)!!
            typeBreedTextView.setText(typeBreeds[which].title)
        }
        builder.show()
    }
}