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
import com.example.stablemanager.Components.Managers.StableManager
import com.example.stablemanager.Pages.AdminPages.Fragments.ListServicesAdminFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.Pages.OwnerPages.StartOwnerPageActivity
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Service


class AddServiceOwnerFragment : Fragment() {
    companion object{
        val TAG: String = AddServiceOwnerFragment::class.java.simpleName
        fun newInstance() = AddServiceOwnerFragment()
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_service_owner, container, false)

        val db = DBHelper(requireContext(), null)
        val titleService: EditText = view.findViewById(R.id.addTitleService)
        val priceService: EditText = view.findViewById(R.id.addPriceServiceAdmin)
        val descriptionService: EditText = view.findViewById(R.id.addDescriptionService)
        val addServiceBtn: Button = view.findViewById(R.id.SaveServiceAdminPage)


        addServiceBtn.setOnClickListener {
            val title = titleService.text.toString().trim()
            val price = priceService.text.toString().trim()
            val description = descriptionService.text.toString().trim()
            if(title == "" || price == ""){
                Toast.makeText(requireContext(), "Поля названия и цены должны быть заполнены", Toast.LENGTH_SHORT).show()
            }
            else{
                val stableManager = StableManager(requireContext())

                db.addService(Service(title, price.toDouble(), description, stableManager.getStableId()))

                Toast.makeText(requireContext(), "Услуга сохранена", Toast.LENGTH_SHORT).show()
                val activity = activity as? StartOwnerPageActivity

                if (activity != null) {
                    activity.replaceFragment(
                        ListServiceOwnerFragment.newInstance(),
                        ListServiceOwnerFragment.TAG
                    )
                } else {
                    Log.e("OptionsFragment", "StartOwnerPageActivity не найдена")
                }
            }
        }

        return view
    }
}