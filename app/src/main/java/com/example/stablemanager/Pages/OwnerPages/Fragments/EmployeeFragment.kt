package com.example.stablemanager.Pages.OwnerPages.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stablemanager.R


class EmployeeFragment : Fragment() {
    companion object{
        val TAG: String = EmployeeFragment::class.java.simpleName
        fun newInstance() = EmployeeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_employee, container, false)
        return view
    }
}