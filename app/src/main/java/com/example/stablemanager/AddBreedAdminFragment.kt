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
import com.example.stablemanager.Pages.AdminPages.Fragments.AddRoleFragment
import com.example.stablemanager.Pages.AdminPages.Fragments.BreedListAdminFragment
import com.example.stablemanager.Pages.AdminPages.Fragments.RoleAdminPageFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.db.Breed
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Role

class AddBreedAdminFragment : Fragment() {
    companion object{
        val TAG: String = AddBreedAdminFragment::class.java.simpleName
        fun newInstance() = AddBreedAdminFragment()
    }

    private lateinit var typeBreedTextView: EditText
    private lateinit var typeBreedButton: Button
    private var selectedTypeBreedId: Int = -1

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_breed_admin, container, false)

        val db = DBHelper(requireContext(), null)
        val titleBreed: EditText = view.findViewById(R.id.addTitleBreed)
        val addBreedBtn: Button = view.findViewById(R.id.SaveBreedPage)
        typeBreedTextView = view.findViewById(R.id.addTypeIdBreed)
        typeBreedButton = view.findViewById(R.id.addTypeBreedButton)

        typeBreedButton.setOnClickListener { showTypeBreedSelectionDialog() }

        addBreedBtn.setOnClickListener {
            val title = titleBreed.text.toString().trim()
            if(title == "" || selectedTypeBreedId == -1){
                Toast.makeText(requireContext(), "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            }
            else{
                db.addBreed(Breed(title, selectedTypeBreedId))
                Toast.makeText(requireContext(), "Порода сохранена", Toast.LENGTH_SHORT).show()
                val activity = activity as? StartAdminPageActivity

                if (activity != null) {
                    activity.replaceFragment(BreedListAdminFragment.newInstance(), BreedListAdminFragment.TAG)
                } else {
                    Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
                }
            }
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