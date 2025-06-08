package com.example.stablemanager.Pages.AdminPages.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Feed


class AddFeedAdminFragment : Fragment() {
    companion object{
        val TAG: String = AddFeedAdminFragment::class.java.simpleName
        fun newInstance() = AddFeedAdminFragment()
    }

    private lateinit var addStableFeed: EditText
    private var selectedStableId: Int = -1

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_feed_admin, container, false)

        val db = DBHelper(requireContext(), null)
        val titleFeed: EditText = view.findViewById(R.id.addTitleFeed)
        val quantity: EditText = view.findViewById(R.id.addQuantityFeedAdmin)
        val addFeedBtn: Button = view.findViewById(R.id.SaveFeedAdminPage)
        addStableFeed = view.findViewById(R.id.addStableIdFeed)
        val addStableFeedButton: Button = view.findViewById(R.id.addStableFeedButton)

        addStableFeedButton.setOnClickListener {
            showStableSelectionDialog()
        }

        addFeedBtn.setOnClickListener {
            val title = titleFeed.text.toString().trim()
            if(title == "" || selectedStableId == -1){
                Toast.makeText(requireContext(), "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            }
            else{
                if(quantity.text.toString().trim() == ""){
                    db.addFeed(Feed(title, 0.0, selectedStableId))
                }
                else{
                    db.addFeed(Feed(title, quantity.text.toString().trim().toDouble(), selectedStableId))
                }
                Toast.makeText(requireContext(), "Корм сохранен", Toast.LENGTH_SHORT).show()
                val activity = activity as? StartAdminPageActivity

                if (activity != null) {
                    activity.replaceFragment(
                        FeedListAdminFragment.newInstance(),
                        FeedListAdminFragment.TAG
                    )
                } else {
                    Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
                }
            }
        }

        return view
    }

    private fun showStableSelectionDialog() {
        val db = DBHelper(requireContext(), null)
        val stables = db.getAllStables()
        val stableTitles = stables.map { it.title }.toTypedArray()

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Выберите конюшню")
        builder.setItems(stableTitles) { _, which ->
            selectedStableId = db.getIdStable(stables[which].title, stables[which].description, stables[which].ownerId)!!
            addStableFeed.setText(stables[which].title)
        }
        builder.show()
    }
}