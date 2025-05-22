package com.example.stablemanager.Pages.OwnerPages.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import com.example.stablemanager.Components.StableManager
import com.example.stablemanager.Pages.OwnerPages.ListStableActivity
import com.example.stablemanager.R
import androidx.navigation.fragment.findNavController
import com.example.stablemanager.Components.setEditable
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Stable


class StartStableFragment : Fragment() {
    companion object{
        val TAG: String = StartStableFragment::class.java.simpleName
        fun newInstance() = StartStableFragment()
    }

    private lateinit var stableManager: StableManager
    private lateinit var titleTextView: EditText
    private lateinit var descriptionStableStartPage: EditText
    private lateinit var editStableStartPage: Button
    private lateinit var saveStableStartPage: Button
    private lateinit var exitStableButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stableManager = StableManager(requireContext())
    }

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_start_stable, container, false)

        titleTextView = view.findViewById(R.id.titleTextView)
        descriptionStableStartPage = view.findViewById(R.id.descriptionStableStartPage)
        editStableStartPage = view.findViewById(R.id.EditStableStartPage)
        saveStableStartPage = view.findViewById(R.id.SaveStableStartPage)
        exitStableButton = view.findViewById(R.id.ExitStableButton)

        val db = DBHelper(requireContext(), null)
        val stableId = stableManager.getStableId()
        var stable = Stable("", "", 0)

        if (stableId != -1) {
            stable = db.getStableById(stableId)!!
            titleTextView.setText("\"${stable.title}\"")
            descriptionStableStartPage.setText(stable.description)

        } else {
            Toast.makeText(requireContext(), "Произошла ошибка загрузки конюшни.", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, ListStableActivity::class.java)
            startActivity(intent)
        }

        exitStableButton.setOnClickListener {
            stableManager.clearStableData()
            val intent = Intent(context, ListStableActivity::class.java)
            startActivity(intent)
        }

        editStableStartPage.setOnClickListener {
            titleTextView.setText(stable.title)
            titleTextView.setEditable(true)
            descriptionStableStartPage.setEditable(true)

            editStableStartPage.visibility = View.GONE
            saveStableStartPage.visibility = View.VISIBLE
        }

        saveStableStartPage.setOnClickListener {
            val title = titleTextView.text.toString().trim()
            val description = descriptionStableStartPage.text.toString().trim()

            if(title == "" || description == ""){
                Toast.makeText(requireContext(), "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            }
            else{
                titleTextView.setText("\"${title}\"")
                titleTextView.setEditable(false)
                descriptionStableStartPage.setEditable(false)

                db.updateStable(stableId, title, description, stable.ownerId)

                editStableStartPage.visibility = View.VISIBLE
                saveStableStartPage.visibility = View.GONE
            }
        }

        return view
    }
}