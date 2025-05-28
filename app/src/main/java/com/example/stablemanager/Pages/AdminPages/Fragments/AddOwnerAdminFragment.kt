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
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Owner
import com.example.stablemanager.db.Role
import org.mindrot.jbcrypt.BCrypt

class AddOwnerAdminFragment : Fragment() {
    companion object{
        val TAG: String = AddOwnerAdminFragment::class.java.simpleName
        fun newInstance() = AddOwnerAdminFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_owner_admin, container, false)

        val db = DBHelper(requireContext(), null)
        val surnameOwner: EditText = view.findViewById(R.id.addSurnameOwner)
        val nameOwner: EditText = view.findViewById(R.id.addNameOwner)
        val patronymicOwner: EditText = view.findViewById(R.id.addPatronymicOwner)
        val emailOwner: EditText = view.findViewById(R.id.addEmailOwner)
        val loginOwner: EditText = view.findViewById(R.id.addLoginOwner)
        val passwordOwner: EditText = view.findViewById(R.id.addPasswordOwner)
        val addOwnerBtn: Button = view.findViewById(R.id.SaveOwnerAdminPage)

        addOwnerBtn.setOnClickListener {
            val surname = surnameOwner.text.toString().trim()
            val name = nameOwner.text.toString().trim()
            val patronymic = patronymicOwner.text.toString().trim()
            val email = emailOwner.text.toString().trim()
            val login = loginOwner.text.toString().trim()
            val password = passwordOwner.text.toString().trim()
            if(surname == "" || name == "" || email == "" || login == "" || password == ""){
                Toast.makeText(requireContext(), "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            }
            else{
                val saltRounds = 12
                val hashPassword = BCrypt.hashpw(password, BCrypt.gensalt(saltRounds))
                db.addOwner(Owner(surname, name, patronymic, email, login, hashPassword, false))
                Toast.makeText(requireContext(), "Владелец сохранен", Toast.LENGTH_SHORT).show()
                val activity = activity as? StartAdminPageActivity

                if (activity != null) {
                    activity.replaceFragment(OwnerListAdminFragment.newInstance(), OwnerListAdminFragment.TAG)
                } else {
                    Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
                }
            }
        }

        return view
    }
}