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
import com.example.stablemanager.Pages.AdminPages.Fragments.FeedListAdminFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Veterinarian


class AddVeterinarianAdminFragment : Fragment() {
    companion object{
        val TAG: String = AddVeterinarianAdminFragment::class.java.simpleName
        fun newInstance() = AddVeterinarianAdminFragment()
    }

    private lateinit var addStableVeterinarian: EditText
    private var selectedStableId: Int = -1

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_veterinarian_admin, container, false)

        val db = DBHelper(requireContext(), null)
        val nameVeterinarian: EditText = view.findViewById(R.id.addFioVeterinarian)
        val phoneVeterinarian: EditText = view.findViewById(R.id.phoneVeterinarianAdmin)
        val addVeterinarianBtn: Button = view.findViewById(R.id.SaveVeterinarianAdminPage)
        addStableVeterinarian = view.findViewById(R.id.addStableIdVeterinarian)
        val addStableVeterinarianButton: Button = view.findViewById(R.id.addStableVeterinarianButton)

        addStableVeterinarianButton.setOnClickListener {
            showStableSelectionDialog()
        }

        addVeterinarianBtn.setOnClickListener {
            val name = nameVeterinarian.text.toString().trim()
            val phone = phoneVeterinarian.text.toString().trim()
            if(name == "" || phone == ""){
                Toast.makeText(requireContext(), "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            }
            else{
                if(selectedStableId == -1){
                    db.addVeterinarian(Veterinarian(name, phone, null))
                }
                else{
                    db.addVeterinarian(Veterinarian(name, phone, selectedStableId))
                }
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

    private fun showStableSelectionDialog() {
        val db = DBHelper(requireContext(), null)
        val stables = db.getAllStables()
        val stableTitles = stables.map { it.title }.toTypedArray()

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Выберите конюшню")
        builder.setItems(stableTitles) { _, which ->
            selectedStableId = db.getIdStable(stables[which].title, stables[which].description, stables[which].ownerId)!!
            addStableVeterinarian.setText(stables[which].title)
        }
        builder.show()
    }
}