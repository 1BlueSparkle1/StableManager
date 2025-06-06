package com.example.stablemanager.Pages.AdminPages.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.Pages.MainActivity
import com.example.stablemanager.R

class OptionsFragment : Fragment() {
    companion object{
        val TAG: String = OptionsFragment::class.java.simpleName
        fun newInstance() = OptionsFragment()
    }

    private lateinit var exitAdminButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_options, container, false)

        exitAdminButton = view.findViewById(R.id.ExitAdminButton)
         val roleAdminButton : Button = view.findViewById(R.id.roleAdminButton)

        roleAdminButton.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                activity.replaceFragment(RoleAdminPageFragment.newInstance(), RoleAdminPageFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        exitAdminButton.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}