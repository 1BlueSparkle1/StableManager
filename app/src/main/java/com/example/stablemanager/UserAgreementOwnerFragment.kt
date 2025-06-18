package com.example.stablemanager

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Components.Adapters.VeterinarianAdapter
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.db.DBHelper


class UserAgreementOwnerFragment : Fragment() {
    companion object{
        val TAG: String = UserAgreementOwnerFragment::class.java.simpleName
        fun newInstance() = UserAgreementOwnerFragment()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_agreement_owner, container, false)

        val db = DBHelper(requireContext(), null)
        val agreementText: TextView = view.findViewById(R.id.textUserAgreement)
        val text = db.getLatestUserAgreement()
        agreementText.text = text



        return view
    }
}