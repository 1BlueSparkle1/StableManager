package com.example.stablemanager.Pages.AdminPages.Fragments

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
import com.example.stablemanager.Components.Managers.AuthEmployeeManager
import com.example.stablemanager.Components.byteArrayToBitmap
import com.example.stablemanager.Components.isValidEmail
import com.example.stablemanager.Components.setEditable
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Employee
import java.io.ByteArrayOutputStream


class ProfileAdminFragment : Fragment() {
    companion object{
        val TAG: String = ProfileAdminFragment::class.java.simpleName
        fun newInstance() = ProfileAdminFragment()
        private const val PICK_IMAGE_REQUEST = 1
    }

    private lateinit var user: Employee
    private lateinit var userImage: ImageView
    private var imageByteArray: ByteArray? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_admin, container, false)

        val buttonEdit: Button = view.findViewById(R.id.editProfileEmployeeButton)
        val buttonSave: Button = view.findViewById(R.id.saveProfileEmployeeButton)
        val buttonEditPass: Button = view.findViewById(R.id.editPasswordEmployeeButton)
        val userSurname: EditText = view.findViewById(R.id.userSurnameProfileEmployee)
        val userName: EditText = view.findViewById(R.id.userNameProfileEmployee)
        val userPatronymic: EditText = view.findViewById(R.id.userPatronymicProfileEmployee)
        val userEmail: EditText = view.findViewById(R.id.userEmailProfileEmployee)
        val userLogin: EditText = view.findViewById(R.id.userLoginProfileEmployee)
        val userBirth: EditText = view.findViewById(R.id.dateOfBirthProfileEmployee)
        val buttonAddImage: Button = view.findViewById(R.id.employeeAddImageProfile)
        userImage = view.findViewById(R.id.employeeLogoProfile)


        val db = DBHelper(requireContext(), null)
        val authEmployeeManager = AuthEmployeeManager(requireContext())
        var userId = -1
        if (authEmployeeManager.isLoggedIn()) {
            userId = authEmployeeManager.getEmployeeId()

            if (db.getEmployeeById(userId) != null) {
                user = db.getEmployeeById(userId)!!
                userSurname.setText(user.surname)
                userName.setText(user.name)
                userPatronymic.setText(user.patronymic)
                userBirth.setText(user.dateOfBirth)
                userEmail.setText(user.email)
                userLogin.setText(user.login)

                val byteArray: ByteArray = user.imageProfile
                val bitmap = byteArrayToBitmap(byteArray)

                if (bitmap != null) {
                    userImage.setImageBitmap(bitmap)
                }
            } else {
                Log.e("Profile", "Пользователь не найден в базе данных.")
            }
        }

        buttonAddImage.setOnClickListener { chooseImageFromGallery() }

        buttonEdit.setOnClickListener {
            userSurname.setEditable(true)
            userName.setEditable(true)
            userPatronymic.setEditable(true)
            userBirth.setEditable(true)
            userEmail.setEditable(true)
            userLogin.setEditable(true)

            buttonEdit.visibility = View.GONE
            buttonSave.visibility = View.VISIBLE
        }

        buttonSave.setOnClickListener {
            val surname = userSurname.text.toString().trim()
            val name = userName.text.toString().trim()
            val patronymic = userPatronymic.text.toString().trim()
            val dateOfBirth = userBirth.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val login = userLogin.text.toString().trim()

            if(surname == "" || name == "" || patronymic == "" || email == "" || login == "" || dateOfBirth == "")
                Toast.makeText(requireContext(), "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            else {
                if (db.doesEmployeeExist(userId, login, email)){
                    Toast.makeText(requireContext(), "Пользователь с таким логином или почтой уже существует", Toast.LENGTH_SHORT).show()
                }
                else {
                    if (isValidEmail(email)){
                        userSurname.setEditable(false)
                        userName.setEditable(false)
                        userPatronymic.setEditable(false)
                        userBirth.setEditable(false)
                        userEmail.setEditable(false)
                        userLogin.setEditable(false)

                        if(imageByteArray != null){
                            db.updateEmployeeProfile(userId, surname, name, patronymic, dateOfBirth, email, login, imageByteArray!!)
                        }
                        else{
                            db.updateEmployeeProfile(userId, surname, name, patronymic, dateOfBirth, email, login, user.imageProfile)
                        }

                        buttonEdit.visibility = View.VISIBLE
                        buttonSave.visibility = View.GONE
                        Toast.makeText(requireContext(), "Изменения сохранены", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireContext(), "Поле почты заполнено некорректно. Заполните в формате mail@mail.ru", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        buttonEditPass.setOnClickListener {
//            val activity = activity as? StartAdminPageActivity
//
//            if (activity != null) {
//                activity.replaceFragment(EmployeeListAdminFragment.newInstance(), EmployeeListAdminFragment.TAG)
//            } else {
//                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
//            }
        }

        return view
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
                userImage.setImageBitmap(bitmap)
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