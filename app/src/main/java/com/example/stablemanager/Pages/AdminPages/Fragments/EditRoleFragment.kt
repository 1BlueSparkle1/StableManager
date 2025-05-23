package com.example.stablemanager.Pages.AdminPages.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.stablemanager.R


class EditRoleFragment : Fragment() {
    companion object{
        val TAG: String = EditRoleFragment::class.java.simpleName
        fun newInstance() = EditRoleFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_role, container, false)
        return view
    }
}