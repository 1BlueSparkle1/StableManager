package com.example.stablemanager.Pages.OwnerPages.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stablemanager.R

class NotifyOwnerFragment : Fragment() {
    companion object{
        val TAG: String = NotifyOwnerFragment::class.java.simpleName
        fun newInstance() = NotifyOwnerFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notify_owner, container, false)
        return view
    }
}