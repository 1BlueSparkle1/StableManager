package com.example.stablemanager.Pages.AdminPages.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Components.Adapters.NotifyListAdapter
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper


class ListNotifyFragment : Fragment() {
    companion object{
        val TAG: String = ListNotifyFragment::class.java.simpleName
        fun newInstance() = ListNotifyFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list_notify, container, false)

        val db = DBHelper(requireContext(), null)
        val notifyList: RecyclerView = view.findViewById(R.id.NotifyListView)

        val notifies = db.getAllNotify()
        notifyList.layoutManager = LinearLayoutManager(requireContext())
        val activity = activity as StartAdminPageActivity
        notifyList.adapter = NotifyListAdapter(notifies, activity, requireContext())

        return view
    }
}