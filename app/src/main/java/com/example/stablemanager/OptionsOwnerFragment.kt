package com.example.stablemanager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stablemanager.Pages.AdminPages.Fragments.OptionsFragment


class OptionsOwnerFragment : Fragment() {
    companion object{
        val TAG: String = OptionsOwnerFragment::class.java.simpleName
        fun newInstance() = OptionsOwnerFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_options_owner, container, false)

        return view
    }
}