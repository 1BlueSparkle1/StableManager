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
import com.example.stablemanager.Pages.AdminPages.Fragments.ListServicesAdminFragment
import com.example.stablemanager.Pages.AdminPages.Fragments.ListVeterinarianAdminFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Service
import com.example.stablemanager.db.Veterinarian


class AddServiceAdminFragment : Fragment() {
    companion object{
        val TAG: String = AddServiceAdminFragment::class.java.simpleName
        fun newInstance() = AddServiceAdminFragment()
    }

    private lateinit var addStableService: EditText
    private var selectedStableId: Int = -1

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_service_admin, container, false)

        val db = DBHelper(requireContext(), null)
        val titleService: EditText = view.findViewById(R.id.addTitleService)
        val priceService: EditText = view.findViewById(R.id.addPriceServiceAdmin)
        val descriptionService: EditText = view.findViewById(R.id.addDescriptionService)
        val addServiceBtn: Button = view.findViewById(R.id.SaveServiceAdminPage)

        addStableService = view.findViewById(R.id.addStableIdService)
        val addStableServiceButton: Button = view.findViewById(R.id.addStableServiceButton)

        addStableServiceButton.setOnClickListener { showStableSelectionDialog() }

        addServiceBtn.setOnClickListener {
            val title = titleService.text.toString().trim()
            val price = priceService.text.toString().trim()
            val description = descriptionService.text.toString().trim()
            if(title == "" || price == "" || selectedStableId == -1){
                Toast.makeText(requireContext(), "Поля названия и цены должны быть заполнены", Toast.LENGTH_SHORT).show()
            }
            else{
                db.addService(Service(title, price.toDouble(), description, selectedStableId))
                Toast.makeText(requireContext(), "Услуга сохранена", Toast.LENGTH_SHORT).show()
                val activity = activity as? StartAdminPageActivity

                if (activity != null) {
                    activity.replaceFragment(
                        ListServicesAdminFragment.newInstance(),
                        ListServicesAdminFragment.TAG
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
            addStableService.setText(stables[which].title)
        }
        builder.show()
    }
}