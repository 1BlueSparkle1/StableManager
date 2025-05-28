package com.example.stablemanager.Pages.AdminPages.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.stablemanager.Components.Managers.AuthEmployeeManager
import com.example.stablemanager.Components.Managers.AuthManager
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.Pages.OwnerPages.Fragments.HorseFragment
import com.example.stablemanager.Pages.OwnerPages.ProfileOwnerActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import org.mindrot.jbcrypt.BCrypt


class EditPasswordEmployeeFragment : Fragment() {
    companion object{
        val TAG: String = EditPasswordEmployeeFragment::class.java.simpleName
        fun newInstance() = EditPasswordEmployeeFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_password_employee, container, false)

        val buttonSave: Button = view.findViewById(R.id.savePasswordEmployee)
        val oldPassword: EditText = view.findViewById(R.id.oldPasswordEmployee)
        val newPassword: EditText = view.findViewById(R.id.newPasswordEmployee)

        val authManager = AuthEmployeeManager(requireContext())
        val db = DBHelper(requireContext(), null)

        buttonSave.setOnClickListener {
            val passwordOld = oldPassword.text.toString().trim()
            val passwordNew = newPassword.text.toString().trim()
            if (authManager.isLoggedIn()) {
                val employeeId = authManager.getEmployeeId()
                val employee = db.getEmployeeById(employeeId)
                if(passwordOld == "" && passwordNew == ""){
                    Toast.makeText(requireContext(), "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
                }
                else{
                    if(employee != null){
                        val isAuth = db.authEmployee(requireContext(), employee.login, oldPassword.text.toString().trim())
                        if(isAuth != null){
                            val saltRounds = 12
                            val hashPassword = BCrypt.hashpw(passwordNew, BCrypt.gensalt(saltRounds))
                            if(db.updatePasswordEmployee(employee.login, hashPassword)){
                                Toast.makeText(requireContext(), "Пароль обновлен!", Toast.LENGTH_SHORT).show()
                                val activity = activity as? StartAdminPageActivity

                                if (activity != null) {
                                    activity.replaceFragment(ProfileAdminFragment.newInstance(), ProfileAdminFragment.TAG)
                                } else {
                                    Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
                                }
                            }
                            else{
                                Toast.makeText(requireContext(), "Ошибка сохранения пароля.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Toast.makeText(requireContext(), "Старый пароль неверен", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(requireContext(), "Ошибка пользователя. Перезойдите или повторите попытку позже.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        return view
    }
}