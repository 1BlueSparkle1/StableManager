package com.example.stablemanager.Pages.OwnerPages.Fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.stablemanager.R


open class HorseFragment : Fragment() {
    companion object{
        val TAG: String = HorseFragment::class.java.simpleName
        fun newInstance() = HorseFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_horse, container, false)
        return view
    }
}