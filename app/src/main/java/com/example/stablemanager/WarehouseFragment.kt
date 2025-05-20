package com.example.stablemanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class WarehouseFragment : Fragment() {
    companion object{
        val TAG: String = WarehouseFragment::class.java.simpleName
        fun newInstance() = WarehouseFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_warehouse, container, false)
        return view
    }
}