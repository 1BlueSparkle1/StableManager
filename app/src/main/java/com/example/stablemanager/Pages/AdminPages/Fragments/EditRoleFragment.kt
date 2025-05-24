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
import com.example.stablemanager.Components.Managers.RoleManagers
import com.example.stablemanager.Components.setEditable
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.Pages.OwnerPages.ListStableActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Role


class EditRoleFragment : Fragment() {
    companion object{
        val TAG: String = EditRoleFragment::class.java.simpleName
        fun newInstance() = EditRoleFragment()
    }

    private lateinit var roleManager: RoleManagers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roleManager = RoleManagers(requireContext())
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_role, container, false)

        val titleRole: EditText = view.findViewById(R.id.editTitleRole)
        val db = DBHelper(requireContext(), null)
        val roleId = roleManager.getRoleId()
        var role = Role(0, "")

        if (roleId != -1) {
            role = db.getRolesById(roleId)!!
            titleRole.setText(role.title)

        } else {
            Toast.makeText(requireContext(), "Произошла ошибка загрузки роли.", Toast.LENGTH_SHORT).show()
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(RoleAdminPageFragment.newInstance(), RoleAdminPageFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        val editRoleButton: Button = view.findViewById(R.id.editRoleButton)
        val saveRoleButton: Button = view.findViewById(R.id.SaveRoleButton)

        editRoleButton.setOnClickListener {
            titleRole.setEditable(true)

            editRoleButton.visibility = View.GONE
            saveRoleButton.visibility = View.VISIBLE
        }

        saveRoleButton.setOnClickListener {
            val title = titleRole.text.toString().trim()

            if(title == ""){
                Toast.makeText(requireContext(), "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            }
            else{
                titleRole.setEditable(false)

                db.updateRole(roleId, title, )

                editRoleButton.visibility = View.VISIBLE
                saveRoleButton.visibility = View.GONE
            }
        }

        return view
    }
}