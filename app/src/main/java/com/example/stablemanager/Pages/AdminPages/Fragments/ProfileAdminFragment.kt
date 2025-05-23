package com.example.stablemanager.Pages.AdminPages.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stablemanager.R


class ProfileAdminFragment : Fragment() {
    companion object{
        val TAG: String = ProfileAdminFragment::class.java.simpleName
        fun newInstance() = ProfileAdminFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_admin, container, false)
        return view
    }
}