package com.example.stablemanager.Pages.AdminPages.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.stablemanager.Pages.MainActivity
import com.example.stablemanager.Pages.OwnerPages.ListStableActivity
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

        exitAdminButton.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}