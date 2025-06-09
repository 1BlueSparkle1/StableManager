package com.example.stablemanager.Components.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stablemanager.Pages.AdminPages.Fragments.AddingFeedAdminFragment
import com.example.stablemanager.Components.Managers.FeedManager
import com.example.stablemanager.Pages.AdminPages.Fragments.DeductionFeedAdminFragment
import com.example.stablemanager.Pages.AdminPages.Fragments.EditRoleFragment
import com.example.stablemanager.Pages.AdminPages.StartAdminPageActivity
import com.example.stablemanager.R
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.db.Feed

class FeedAdapter(private var feeds: List<Feed>, private val activity: StartAdminPageActivity, private var context: Context) : RecyclerView.Adapter<FeedAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.feedsListTitle)
        val quantity: TextView = itemView.findViewById(R.id.feedsListQuantity)
        val plusQuantity: Button = itemView.findViewById(R.id.AddQuantityFeed)
        val minusQuantity: Button = itemView.findViewById(R.id.downQuantityFeed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.feeds_admin_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return feeds.count()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val feed = feeds[position]
        holder.title.text = feed.title
        holder.quantity.text = feed.quantity.toString()

        val feedManager = FeedManager(context)
        val db = DBHelper(context, null)
        val idFeed = db.getIdFeed(feed.title, feed.quantity, feed.stableId)

        holder.plusQuantity.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                if(idFeed != null){
                    feedManager.saveFeedId(idFeed)
                }
                activity.replaceFragment(AddingFeedAdminFragment.newInstance(), AddingFeedAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        holder.minusQuantity.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                if(idFeed != null){
                    feedManager.saveFeedId(idFeed)
                }
                activity.replaceFragment(DeductionFeedAdminFragment.newInstance(), DeductionFeedAdminFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }

        holder.itemView.setOnClickListener {
            val activity = activity as? StartAdminPageActivity

            if (activity != null) {
                if(idFeed != null){
                    feedManager.saveFeedId(idFeed)
                }
                activity.replaceFragment(EditRoleFragment.newInstance(), EditRoleFragment.TAG)
            } else {
                Log.e("OptionsFragment", "StartAdminPageActivity не найдена")
            }
        }
    }
}