package com.example.stablemanager.Pages.OwnerPages.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Components.Adapters.FeedOwnerAdapter
import com.example.stablemanager.Components.Managers.StableManager
import com.example.stablemanager.Pages.AdminPages.Fragments.AddFeedAdminFragment
import com.example.stablemanager.Pages.OwnerPages.StartOwnerPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper


class FeedListOwnerFragment : Fragment() {
    companion object{
        val TAG: String = FeedListOwnerFragment::class.java.simpleName
        fun newInstance() = FeedListOwnerFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_feed_list_owner, container, false)

        val db = DBHelper(requireContext(), null)
        val feedList: RecyclerView = view.findViewById(R.id.feedListView)
        val addFeedBtn: Button = view.findViewById(R.id.addFeedAdminButton)
        val stableManager = StableManager(requireContext())

        val feeds = db.getFeedsInStable(stableManager.getStableId())
        feedList.layoutManager = LinearLayoutManager(requireContext())
        val activity = activity as StartOwnerPageActivity
        feedList.adapter = FeedOwnerAdapter(feeds, activity, requireContext())

        addFeedBtn.setOnClickListener {
            val activity = activity as? StartOwnerPageActivity

            if (activity != null) {
                activity.replaceFragment(AddFeedAdminFragment.newInstance(), AddFeedAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        return view
    }
}