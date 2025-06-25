package com.example.stablemanager.Pages.OwnerPages.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.stablemanager.FeedListOwnerFragment
import com.example.stablemanager.ListServiceOwnerFragment
import com.example.stablemanager.Pages.OwnerPages.StartOwnerPageActivity
import com.example.stablemanager.R


class OptionsOwnerFragment : Fragment() {
    companion object{
        val TAG: String = OptionsOwnerFragment::class.java.simpleName
        fun newInstance() = OptionsOwnerFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_options_owner, container, false)

        val userAgreementOwnerButton: Button = view.findViewById(R.id.userAgreementOwnerButton)
        val veterinariansOwnerButton: Button = view.findViewById(R.id.veterinariansOwnerButton)
        val serviceOwnerButton: Button = view.findViewById(R.id.serviceOwnerButton)
        val feedOwnerButton: Button = view.findViewById(R.id.feedOwnerButton)


        userAgreementOwnerButton.setOnClickListener {
            val activity = activity as? StartOwnerPageActivity

            if (activity != null) {
                activity.replaceFragment(UserAgreementOwnerFragment.newInstance(), UserAgreementOwnerFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartOwnerPageActivity не найдена")
            }
        }

        veterinariansOwnerButton.setOnClickListener {
            val activity = activity as? StartOwnerPageActivity

            if (activity != null) {
                activity.replaceFragment(ListVeterinarianOwnerFragment.newInstance(), ListVeterinarianOwnerFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartOwnerPageActivity не найдена")
            }
        }

        serviceOwnerButton.setOnClickListener {
            val activity = activity as? StartOwnerPageActivity

            if (activity != null) {
                activity.replaceFragment(ListServiceOwnerFragment.newInstance(), ListServiceOwnerFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartOwnerPageActivity не найдена")
            }
        }

        feedOwnerButton.setOnClickListener {
            val activity = activity as? StartOwnerPageActivity

            if (activity != null) {
                activity.replaceFragment(FeedListOwnerFragment.newInstance(), FeedListOwnerFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartOwnerPageActivity не найдена")
            }
        }

        return view
    }
}