package com.example.stablemanager.Pages.AdminPages.Fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.stablemanager.R


class NotifyAdminFragment : Fragment() {
    companion object{
        val TAG: String = NotifyAdminFragment::class.java.simpleName
        fun newInstance() = NotifyAdminFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notify_admin, container, false)
        return view
    }
}