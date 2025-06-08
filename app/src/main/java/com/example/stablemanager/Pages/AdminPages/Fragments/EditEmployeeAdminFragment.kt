package com.example.stablemanager.Pages.AdminPages.Fragments

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
import com.example.stablemanager.Components.byteArrayToBitmap
import com.example.stablemanager.Components.setEditable
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.Pages.OwnerPages.StartOwnerPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Employee
import org.mindrot.jbcrypt.BCrypt
import java.io.ByteArrayOutputStream


class EditEmployeeAdminFragment : Fragment() {
    companion object{
        val TAG: String = EditEmployeeAdminFragment::class.java.simpleName
        fun newInstance() = EditEmployeeAdminFragment()
        private const val PICK_IMAGE_REQUEST = 1
    }

    private lateinit var employeeManager: EmployeeManager

    private lateinit var surnameEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var patronymicEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var roleTextView: EditText
    private lateinit var roleButton: Button
    private lateinit var stableTextView: EditText
    private lateinit var stableButton: Button
    private lateinit var imageView: ImageView
    private lateinit var imageButton: Button
    private lateinit var dateOfBirthEditText: EditText
    private lateinit var salaryEditText: EditText
    private lateinit var employee: Employee
    private  lateinit var removeButton: Button

    private var selectedRoleId: Int = -1
    private var selectedStableId: Int = -1
    private var imageByteArray: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        employeeManager = EmployeeManager(requireContext())
    }

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_employee_admin, container, false)

        surnameEditText = view.findViewById(R.id.addSurnameEmployee)
        nameEditText = view.findViewById(R.id.addNameEmployee)
        patronymicEditText = view.findViewById(R.id.addPatronymicEmployee)
        emailEditText = view.findViewById(R.id.addEmailEmployee)
        loginEditText = view.findViewById(R.id.addLoginEmployee)
        passwordEditText = view.findViewById(R.id.addPasswordEmployee)
        roleTextView = view.findViewById(R.id.addRoleIdEmployee)
        roleButton = view.findViewById(R.id.addRoleEmployeeButton)
        stableTextView = view.findViewById(R.id.addStableIdEmployee)
        stableButton = view.findViewById(R.id.addStableEmployeeButton)
        imageView = view.findViewById(R.id.AddEmployeeLogo)
        imageButton = view.findViewById(R.id.addImageEmployeeButton)
        dateOfBirthEditText = view.findViewById(R.id.addDateOfBirthEmployee)
        salaryEditText = view.findViewById(R.id.addSalaryEmployee)
        removeButton = view.findViewById(R.id.removeEmployeeButton)

        roleButton.setOnClickListener { showRoleSelectionDialog() }
        imageButton.setOnClickListener { chooseImageFromGallery() }
        stableButton.setOnClickListener { showStableSelectionDialog() }

        val db = DBHelper(requireContext(), null)
        val employeeId = employeeManager.getEmployeeId()

        if (employeeId != -1) {
            employee = db.getEmployeeById(employeeId)!!
            surnameEditText.setText(employee.surname)
            nameEditText.setText(employee.name)
            patronymicEditText.setText(employee.patronymic)
            emailEditText.setText(employee.email)
            loginEditText.setText(employee.login)
            passwordEditText.setText(employee.password)
            val role = db.getRolesById(employee.roleId)
            if (role != null){
                roleTextView.setText(role.title)
            }
            val stable = db.getStableById(employee.stableId)
            if (stable != null){
                stableTextView.setText(stable.title)
            }
            val byteArray: ByteArray = employee.imageProfile
            val bitmap = byteArrayToBitmap(byteArray)
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap)
            }
            dateOfBirthEditText.setText(employee.dateOfBirth)
            salaryEditText.setText(employee.salary.toString())

        } else {
            Toast.makeText(requireContext(), "Произошла ошибка загрузки роли.", Toast.LENGTH_SHORT).show()
            val activity = activity as? StartOwnerPageActivity

            if (activity != null) {
                activity.replaceFragment(RoleAdminPageFragment.newInstance(), RoleAdminPageFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartOwnerPageActivity не найдена")
            }
        }

        val editEmployeeButton: Button = view.findViewById(R.id.EditEmployeePage)
        val saveEmployeeButton: Button = view.findViewById(R.id.SaveEmployeePage)

        editEmployeeButton.setOnClickListener {
            surnameEditText.setEditable(true)
            nameEditText.setEditable(true)
            patronymicEditText.setEditable(true)
            emailEditText.setEditable(true)
            loginEditText.setEditable(true)
            passwordEditText.setEditable(true)
            dateOfBirthEditText.setEditable(true)
            salaryEditText.setEditable(true)
            roleButton.isEnabled = true
            roleButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),
                R.color.brown_text
            ))
            stableButton.isEnabled = true
            stableButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),
                R.color.brown_text
            ))
            imageButton.isEnabled = true
            imageButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),
                R.color.brown_text
            ))

            editEmployeeButton.visibility = View.GONE
            saveEmployeeButton.visibility = View.VISIBLE
            Toast.makeText(requireContext(), "Сотрудник изменен", Toast.LENGTH_SHORT).show()
        }

        saveEmployeeButton.setOnClickListener {
            val surname = surnameEditText.text.toString().trim()
            val name = nameEditText.text.toString().trim()
            val patronymic = patronymicEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val login = loginEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val birth = dateOfBirthEditText.text.toString().trim()
            var salary = 0.0

            if(salaryEditText.text != null || salaryEditText.text.toString() != ""){
                salary = salaryEditText.text.toString().toDoubleOrNull() ?: 0.0

                if (salary <= 0.0) {
                    Toast.makeText(requireContext(), "Зарплата не может быть равна или меньше 0", Toast.LENGTH_SHORT).show()

                }
            }

            if(surname == "" || name == "" || email == "" || login == "" || password == "" || selectedRoleId == -1 || selectedStableId == -1){
                Toast.makeText(requireContext(), "Поля фамилии, имени, почты, логина, пароля, роли и конюшни должны быть заполнены", Toast.LENGTH_SHORT).show()
            }
            else{
                surnameEditText.setEditable(false)
                nameEditText.setEditable(false)
                patronymicEditText.setEditable(false)
                emailEditText.setEditable(false)
                loginEditText.setEditable(false)
                passwordEditText.setEditable(false)
                dateOfBirthEditText.setEditable(false)
                salaryEditText.setEditable(false)
                roleButton.isEnabled = false
                roleButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),
                    R.color.light_brown_text
                ))
                stableButton.isEnabled = false
                stableButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),
                    R.color.light_brown_text
                ))
                imageButton.isEnabled = false
                imageButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),
                    R.color.light_brown_text
                ))

                if (selectedRoleId == -1){
                    selectedRoleId = employee.roleId
                }
                if (imageByteArray == null){
                    imageByteArray = employee.imageProfile
                }
                if (selectedStableId == -1){
                    selectedStableId = employee.stableId
                }
                if (salary == 0.0){
                    salary = employee.salary
                }
                var hashPassword = ""
                if(password != employee.password){
                    val saltRounds = 12
                    hashPassword = BCrypt.hashpw(password, BCrypt.gensalt(saltRounds))
                }
                else{
                    hashPassword = employee.password
                }

                db.updateEmployee(employeeId, surname, name, patronymic, birth, email, login, hashPassword, imageByteArray!!, selectedRoleId, salary, selectedStableId)
                Toast.makeText(requireContext(), "Сотрудник изменен", Toast.LENGTH_SHORT).show()

                editEmployeeButton.visibility = View.VISIBLE
                saveEmployeeButton.visibility = View.GONE
            }
        }

        removeButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Удаление сотрудника")
            builder.setMessage("Вы уверены, что хотите уволить этого сотрудника?\nВместе с ним будут удалены все записи, в которых он участвует.")
            builder.setPositiveButton("Да") { dialog, which ->
                db.deleteEmployeeAndRelatedData(employeeId)
                val activity = activity as? StartAdminPageActivity

                if (activity != null) {
                    activity.replaceFragment(EmployeeListAdminFragment.newInstance(), EmployeeListAdminFragment.TAG)
                } else {
                    Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
                }
                Toast.makeText(requireContext(), "Сотрудник уволен", Toast.LENGTH_SHORT).show()
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

    private fun showRoleSelectionDialog() {
        val db = DBHelper(requireContext(), null)
        val roles = db.getRoles()
        val roleTitles = roles.map { it.title }.toTypedArray()

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Выберите роль")
        builder.setItems(roleTitles) { _, which ->
            selectedRoleId = roles[which].id
            roleTextView.setText(roles[which].title)
        }
        builder.show()
    }

    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            try {
                val bitmap = android.provider.MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri!!)
                imageView.setImageBitmap(bitmap)
                imageByteArray = bitmapToByteArray(bitmap)
            } catch (e: Exception) {
            }
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            return stream.toByteArray()
        } catch (e: Exception) {
            Log.e("BitmapToByteArray", "Ошибка при преобразовании Bitmap в ByteArray: ${e.message}")
            return ByteArray(0)
        }
    }
}