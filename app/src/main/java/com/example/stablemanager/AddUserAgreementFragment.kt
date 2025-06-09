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
import com.example.stablemanager.Pages.AdminPages.Fragments.AddRoleFragment
import com.example.stablemanager.Pages.AdminPages.Fragments.RoleAdminPageFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Role

class AddUserAgreementFragment : Fragment() {
    companion object{
        val TAG: String = AddUserAgreementFragment::class.java.simpleName
        fun newInstance() = AddUserAgreementFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_user_agreement, container, false)

        val editTextAgreementContent: EditText = view.findViewById(R.id.editTextAgreementContent)
        val buttonSaveAgreement: Button = view.findViewById(R.id.buttonSaveAgreement)

        buttonSaveAgreement.setOnClickListener {
            val agreementContent = editTextAgreementContent.text.toString().trim()

            if (agreementContent.isEmpty()) {
                Toast.makeText(requireContext(), "Пожалуйста, введите текст соглашения.", Toast.LENGTH_SHORT).show()
            } else {
                val dbHelper = DBHelper(requireContext(), null)
                dbHelper.insertUserAgreement(agreementContent)
                Toast.makeText(requireContext(), "Соглашение сохранено.", Toast.LENGTH_SHORT).show()
                editTextAgreementContent.text.clear()
            }
        }

        return view
    }
}