package com.example.stablemanager

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
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
import com.example.stablemanager.Components.Managers.StableManager
import com.example.stablemanager.Components.isValidEmail
import com.example.stablemanager.Pages.AdminPages.Fragments.AddEmployeeAdminFragment
import com.example.stablemanager.Pages.AdminPages.Fragments.EmployeeListAdminFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Employee
import org.mindrot.jbcrypt.BCrypt
import java.io.ByteArrayOutputStream


class AddEmployeeFragment : Fragment() {
    companion object{
        val TAG: String = AddEmployeeFragment::class.java.simpleName
        fun newInstance() = AddEmployeeFragment()
        private const val PICK_IMAGE_REQUEST = 1
    }

    private lateinit var surnameEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var patronymicEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var roleTextView: EditText
    private lateinit var roleButton: Button
    private lateinit var imageView: ImageView
    private lateinit var imageButton: Button
    private lateinit var dateOfBirthEditText: EditText
    private lateinit var salaryEditText: EditText
    private lateinit var addButton: Button

    private var selectedRoleId: Int = -1
    private var imageByteArray: ByteArray? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_employee, container, false)

        surnameEditText = view.findViewById(R.id.addSurnameEmployee)
        nameEditText = view.findViewById(R.id.addNameEmployee)
        patronymicEditText = view.findViewById(R.id.addPatronymicEmployee)
        emailEditText = view.findViewById(R.id.addEmailEmployee)
        loginEditText = view.findViewById(R.id.addLoginEmployee)
        passwordEditText = view.findViewById(R.id.addPasswordEmployee)
        roleTextView = view.findViewById(R.id.addRoleIdEmployee)
        roleButton = view.findViewById(R.id.addRoleEmployeeButton)
        imageView = view.findViewById(R.id.AddEmployeeLogo)
        imageButton = view.findViewById(R.id.addImageEmployeeButton)
        dateOfBirthEditText = view.findViewById(R.id.addDateOfBirthEmployee)
        salaryEditText = view.findViewById(R.id.addSalaryEmployee)
        addButton = view.findViewById(R.id.SaveEmployeeAdminPage)

        roleButton.setOnClickListener { showRoleSelectionDialog() }
        imageButton.setOnClickListener { chooseImageFromGallery() }
        addButton.setOnClickListener { addEmployee() }

        return view
    }


    private fun showRoleSelectionDialog() {
        // TODO: Реализовать диалоговое окно для выбора роли
        // Заменить на реальные данные из базы данных
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


    private fun addEmployee() {
        val db = DBHelper(requireContext(), null)
        val surname = surnameEditText.text.toString()
        val name = nameEditText.text.toString()
        val patronymic = patronymicEditText.text.toString()
        val email = emailEditText.text.toString()
        val login = loginEditText.text.toString()
        val password = passwordEditText.text.toString()
        val dateOfBirth = dateOfBirthEditText.text.toString()
        val salary = salaryEditText.text.toString().toDoubleOrNull() ?: 0.0

        if (salary <= 0.0) {
            Toast.makeText(requireContext(), "Зарплата не может быть равна или меньше 0", Toast.LENGTH_SHORT).show()
            return
        }

        if (db.doesEmployeeExist(-1, login, email)){
            Toast.makeText(requireContext(), "Пользователь с таким логином или почтой уже существует", Toast.LENGTH_SHORT).show()
        }
        else{
            if(isValidEmail(email)){
                val saltRounds = 12
                val hashPassword = BCrypt.hashpw(password, BCrypt.gensalt(saltRounds))
                val stableManager = StableManager(requireContext())

                val employee = Employee(surname, name, patronymic, email, login, hashPassword, selectedRoleId, dateOfBirth, salary, stableManager.getStableId(), imageByteArray ?: ByteArray(0))
                db.addEmployee(employee)
                Toast.makeText(requireContext(), "Сотрудник добавлен", Toast.LENGTH_SHORT).show()
                surnameEditText.text.clear()
                nameEditText.text.clear()
                patronymicEditText.text.clear()
                emailEditText.text.clear()
                loginEditText.text.clear()
                passwordEditText.text.clear()
                dateOfBirthEditText.text.clear()
                salaryEditText.text.clear()

                val activity = activity as? StartAdminPageActivity

                if (activity != null) {
                    activity.replaceFragment(EmployeeListAdminFragment.newInstance(), EmployeeListAdminFragment.TAG)
                } else {
                    Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
                }
            }
            else{
                Toast.makeText(context, "Поле почты заполнено некорректно. Заполните в формате mail@mail.ru", Toast.LENGTH_SHORT).show()
            }
        }
    }
}