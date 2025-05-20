package com.example.stablemanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class StartStableFragment : Fragment() {
    companion object{
        val TAG: String = StartStableFragment::class.java.simpleName
        fun newInstance() = StartStableFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_start_stable, container, false)
        return view
    }
}