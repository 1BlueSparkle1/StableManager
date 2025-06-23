package com.example.stablemanager.Pages.OwnerPages.Fragments

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.AddServiceAdminFragment
import com.example.stablemanager.Components.Adapters.NotifyAdapter
import com.example.stablemanager.Components.Adapters.ServiceAdapter
import com.example.stablemanager.Components.Managers.AuthManager
import com.example.stablemanager.Components.Managers.OwnerManager
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.Pages.OwnerPages.StartOwnerPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.utils.NotificationViewModel
import com.example.stablemanager.utils.NotificationViewModelFactory

class NotifyOwnerFragment : Fragment() {
    companion object{
        val TAG: String = NotifyOwnerFragment::class.java.simpleName
        fun newInstance() = NotifyOwnerFragment()
    }

    private lateinit var notificationViewModel: NotificationViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notify_owner, container, false)

        val db = DBHelper(requireContext(), null)
        val notifyList: RecyclerView = view.findViewById(R.id.NotifyAdminView)
        val allReadNotifyBtn: Button = view.findViewById(R.id.ReadAllNotifyButton)

        val authManager = AuthManager(requireContext())

        val notifies = db.getUnreadNotifications(authManager.getUserId(), true)
        notifyList.layoutManager = LinearLayoutManager(requireContext())
        val activity = activity as StartOwnerPageActivity
        notifyList.adapter = NotifyAdapter(notifies, activity, requireContext())

        allReadNotifyBtn.setOnClickListener {

        }

        val factory = NotificationViewModelFactory(db, authManager.getUserId(), true)
        notificationViewModel = ViewModelProvider(requireActivity(), factory)
            .get(NotificationViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationViewModel.refreshNotificationsCount()
    }

    override fun onResume() {
        super.onResume()
        notificationViewModel.refreshNotificationsCount()
    }
}