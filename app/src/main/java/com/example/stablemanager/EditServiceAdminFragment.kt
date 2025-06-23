package com.example.stablemanager

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.stablemanager.Components.Managers.EmployeeManager
import com.example.stablemanager.Components.Managers.ServiceManager
import com.example.stablemanager.Components.byteArrayToBitmap
import com.example.stablemanager.Components.setEditable
import com.example.stablemanager.Pages.AdminPages.Fragments.EditEmployeeAdminFragment
import com.example.stablemanager.Pages.AdminPages.Fragments.EmployeeListAdminFragment
import com.example.stablemanager.Pages.AdminPages.Fragments.ListServicesAdminFragment
import com.example.stablemanager.Pages.AdminPages.Fragments.RoleAdminPageFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.Pages.OwnerPages.StartOwnerPageActivity
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Employee
import com.example.stablemanager.db.Service
import org.mindrot.jbcrypt.BCrypt
import java.io.ByteArrayOutputStream


class EditServiceAdminFragment : Fragment() {
    companion object{
        val TAG: String = EditServiceAdminFragment::class.java.simpleName
        fun newInstance() = EditServiceAdminFragment()
    }

    private lateinit var serviceManager: ServiceManager

    private lateinit var titleEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var stableTextView: EditText
    private lateinit var stableButton: Button
    private  lateinit var deleteButton: Button
    private lateinit var service: Service

    private var selectedStableId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        serviceManager = ServiceManager(requireContext())
    }

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_service_admin, container, false)

        titleEditText = view.findViewById(R.id.editTitleService)
        priceEditText = view.findViewById(R.id.editPriceServiceAdmin)
        descriptionEditText = view.findViewById(R.id.editDescriptionService)
        stableTextView = view.findViewById(R.id.addStableIdEmployee)
        stableButton = view.findViewById(R.id.addStableEmployeeButton)
        deleteButton = view.findViewById(R.id.deleteServiceButton)

        stableButton.setOnClickListener { showStableSelectionDialog() }

        val db = DBHelper(requireContext(), null)
        val serviceId = serviceManager.getServiceId()

        if (serviceId != -1) {
            service = db.getServiceById(serviceId)!!
            titleEditText.setText(service.title)
            priceEditText.setText(service.price.toString())
            descriptionEditText.setText(service.description)
            val stable = db.getStableById(service.stableId)
            if (stable != null){
                stableTextView.setText(stable.title)
            }

        } else {
            Toast.makeText(requireContext(), "Произошла ошибка загрузки услуги.", Toast.LENGTH_SHORT).show()
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(ListServicesAdminFragment.newInstance(), ListServicesAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        val editServiceButton: Button = view.findViewById(R.id.EditServiceButtonPage)
        val saveServiceButton: Button = view.findViewById(R.id.SaveServiceButtonPage)

        editServiceButton.setOnClickListener {
            titleEditText.setEditable(true)
            priceEditText.setEditable(true)
            descriptionEditText.setEditable(true)
            stableButton.isEnabled = true
            stableButton.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(),
                R.color.brown_text
            ))

            editServiceButton.visibility = View.GONE
            saveServiceButton.visibility = View.VISIBLE
        }

        saveServiceButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()
            var price = 0.0

            if(priceEditText.text != null || priceEditText.text.toString() != ""){
                price = priceEditText.text.toString().toDoubleOrNull() ?: 0.0

                if (price <= 0.0) {
                    Toast.makeText(requireContext(), "Цена услуги не может быть равна или меньше 0", Toast.LENGTH_SHORT).show()

                }
            }

            if(title == "" || selectedStableId == -1){
                Toast.makeText(requireContext(), "Поля названия и конюшни должны быть заполнены", Toast.LENGTH_SHORT).show()
            }
            else{
                titleEditText.setEditable(false)
                descriptionEditText.setEditable(false)
                stableButton.isEnabled = false
                stableButton.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(),
                    R.color.light_brown_text
                ))


                if (selectedStableId == -1){
                    selectedStableId = service.stableId
                }
                if (price == 0.0){
                    price = service.price
                }

                db.updateService(serviceId, title, price, description, selectedStableId)
                Toast.makeText(requireContext(), "Услуга изменена", Toast.LENGTH_SHORT).show()

                editServiceButton.visibility = View.VISIBLE
                saveServiceButton.visibility = View.GONE
            }
        }

        deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Удаление Услуги")
            builder.setMessage("Вы уверены, что хотите удалить эту услугу?\nВместе с ней будут удалены все записи на услуги,\nв которых она фигурирует.")
            builder.setPositiveButton("Да") { dialog, which ->
                db.deleteServiceAndRelatedAppointments(serviceId)
                val activity = activity as? StartAdminPageActivity

                if (activity != null) {
                    activity.replaceFragment(ListServicesAdminFragment.newInstance(), ListServicesAdminFragment.TAG)
                } else {
                    Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
                }
                Toast.makeText(requireContext(), "Услуга удалена", Toast.LENGTH_SHORT).show()
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