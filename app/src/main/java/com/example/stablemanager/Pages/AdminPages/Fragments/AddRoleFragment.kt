package com.example.stablemanager.Pages.AdminPages.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Role


class AddRoleFragment : Fragment() {
    companion object{
        val TAG: String = AddRoleFragment::class.java.simpleName
        fun newInstance() = AddRoleFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_role, container, false)

        val db = DBHelper(requireContext(), null)
        val titleRole: EditText = view.findViewById(R.id.addTitleRole)
        val addRoleBtn: Button = view.findViewById(R.id.SaveRolePage)

        addRoleBtn.setOnClickListener {
            val title = titleRole.text.toString().trim()
            if(title == ""){
                Toast.makeText(requireContext(), "Название должно быть заполнено", Toast.LENGTH_SHORT).show()
            }
            else{
                db.addRole(Role(0, title))
                Toast.makeText(requireContext(), "Роль сохранена", Toast.LENGTH_SHORT).show()
                val activity = activity as? StartAdminPageActivity

                if (activity != null) {
                    activity.replaceFragment(RoleAdminPageFragment.newInstance(), RoleAdminPageFragment.TAG)
                } else {
                    Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
                }
            }
        }

        return view
    }
}