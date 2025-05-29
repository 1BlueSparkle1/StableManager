package com.example.stablemanager.Pages.AdminPages.Fragments

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
import com.example.stablemanager.Components.Managers.OwnerManager
import com.example.stablemanager.Components.isValidEmail
import com.example.stablemanager.Components.setEditable
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Owner


class EditOwnerFragment : Fragment() {
    companion object{
        val TAG: String = EditOwnerFragment::class.java.simpleName
        fun newInstance() = EditOwnerFragment()
    }

    private lateinit var ownerManager: OwnerManager
    private lateinit var owner: Owner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ownerManager = OwnerManager(requireContext())
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_owner, container, false)

        val surnameOwner: EditText = view.findViewById(R.id.editSurnameOwner)
        val nameOwner: EditText = view.findViewById(R.id.editNameOwner)
        val patronymicOwner: EditText = view.findViewById(R.id.editPatronymicOwner)
        val emailOwner: EditText = view.findViewById(R.id.editEmailOwner)
        val loginOwner: EditText = view.findViewById(R.id.editLoginOwner)
        val deleteButton: Button = view.findViewById(R.id.deleteOwnerButton)
        val db = DBHelper(requireContext(), null)
        val ownerId = ownerManager.getOwnerId()

        if (ownerId != -1) {
            owner = db.getOwnerById(ownerId)!!
            surnameOwner.setText(owner.surname)
            nameOwner.setText(owner.fullname)
            patronymicOwner.setText(owner.patronymic)
            emailOwner.setText(owner.email)
            loginOwner.setText(owner.login)

        } else {
            Toast.makeText(requireContext(), "Произошла ошибка загрузки роли.", Toast.LENGTH_SHORT).show()
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(OwnerListAdminFragment.newInstance(), OwnerListAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        val editOwnerButton: Button = view.findViewById(R.id.EditOwnerButton)
        val saveOwnerButton: Button = view.findViewById(R.id.SaveOwnerButton)

        editOwnerButton.setOnClickListener {
            surnameOwner.setEditable(true)
            nameOwner.setEditable(true)
            patronymicOwner.setEditable(true)
            emailOwner.setEditable(true)
            loginOwner.setEditable(true)

            editOwnerButton.visibility = View.GONE
            saveOwnerButton.visibility = View.VISIBLE
        }

        saveOwnerButton.setOnClickListener {
            val surname = surnameOwner.text.toString().trim()
            val name = nameOwner.text.toString().trim()
            val patronymic = patronymicOwner.text.toString().trim()
            val email = emailOwner.text.toString().trim()
            val login = loginOwner.text.toString().trim()

            if(surname == "" || name == "" || email == "" || login == ""){
                Toast.makeText(requireContext(), "Все основные поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            }
            else{
                if(isValidEmail(email)){
                    surnameOwner.setEditable(false)
                    nameOwner.setEditable(false)
                    patronymicOwner.setEditable(false)
                    emailOwner.setEditable(false)
                    loginOwner.setEditable(false)

                    db.updateOwner(ownerId, surname, name, patronymic, email, login)

                    editOwnerButton.visibility = View.VISIBLE
                    saveOwnerButton.visibility = View.GONE
                    Toast.makeText(requireContext(), "Владелец изменен", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(requireContext(), "Поле почты заполнено некорректно. Заполните в формате mail@mail.ru", Toast.LENGTH_SHORT).show()
                }

            }
        }

        deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Удаление Владельца")
            builder.setMessage("Вы уверены, что хотите удалить этого владельца?\nВместе с ним будут удалены все его данные.")
            builder.setPositiveButton("Да") { dialog, which ->
                db.deleteOwner(ownerId)
                val activity = activity as? StartAdminPageActivity

                if (activity != null) {
                    activity.replaceFragment(OwnerListAdminFragment.newInstance(), OwnerListAdminFragment.TAG)
                } else {
                    Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
                }
                Toast.makeText(requireContext(), "Владелец удален", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Отмена") { dialog, which ->
            }

            builder.show()
        }

        return view
    }
}