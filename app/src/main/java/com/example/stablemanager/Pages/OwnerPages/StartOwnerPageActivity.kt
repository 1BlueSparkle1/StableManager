package com.example.stablemanager.Pages.OwnerPages

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.stablemanager.Components.Managers.AuthManager
import com.example.stablemanager.Components.Managers.OwnerManager
import com.example.stablemanager.Pages.OwnerPages.Fragments.NotifyOwnerFragment
import com.example.stablemanager.R
import com.example.stablemanager.Pages.OwnerPages.Fragments.StartStableFragment
import com.example.stablemanager.db.DBHelper
import com.example.stablemanager.utils.BottomNavigationPosition
import com.example.stablemanager.utils.NotificationViewModel
import com.example.stablemanager.utils.NotificationViewModelFactory
import com.example.stablemanager.utils.createFragment
import com.example.stablemanager.utils.findNavigationPositionById
import com.example.stablemanager.utils.getTag
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StartOwnerPageActivity : AppCompatActivity() , BottomNavigationView.OnNavigationItemSelectedListener {
    private var navPosition: BottomNavigationPosition = BottomNavigationPosition.FIRST
    private lateinit var bottomNavigationOwner: BottomNavigationView
    private lateinit var myImageView: ImageView
    private lateinit var notifyButton: ImageView
    private lateinit var notificationViewModel: NotificationViewModel

    private val notificationRefreshReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val userId = intent?.getIntExtra("userId", -1) ?: -1
            val isOwner = intent?.getBooleanExtra("isOwner", false) ?: false
            val authManager = AuthManager(context!!)
            if (userId == authManager.getUserId() && isOwner) {
                notificationViewModel.refreshNotificationsCount()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_owner_page)
        bottomNavigationOwner = findViewById(R.id.bottomNavigationOwner)
        myImageView = findViewById(R.id.logoStartPageImage)
        notifyButton = findViewById(R.id.notifyBell)
        initBottomNavigation()
        setupImageViewClick()
        val context: Context = this
        val ownerManager = OwnerManager(this)

        val intent = Intent("com.example.stablemanager.NEW_NOTIFICATION")
        intent.putExtra("userId", ownerManager.getOwnerId())
        intent.putExtra("isOwner", true)
        context.sendBroadcast(intent)

        if (savedInstanceState == null) {
            replaceFragment(StartStableFragment.newInstance(), StartStableFragment.TAG)
        }

        notifyButton.setOnClickListener {
            replaceFragment(NotifyOwnerFragment.newInstance(), NotifyOwnerFragment.TAG)
        }

        Toast.makeText(this, "Для возврата на эту страницу, нажмите на логотип", Toast.LENGTH_SHORT).show()

        val db = DBHelper(this, null)
        val authManager = AuthManager(this)

        notificationViewModel = ViewModelProvider(this, NotificationViewModelFactory(db, authManager.getUserId(), true))
            .get(NotificationViewModel::class.java)

        lifecycleScope.launch {
            notificationViewModel.unreadNotificationsCount.collectLatest { count ->
                if (count > 0) {
                    notifyButton.setImageResource(R.drawable.ic_bell_ring)
                } else {
                    notifyButton.setImageResource(R.drawable.ic_bell)
                }
            }
        }

        val filter = IntentFilter("com.example.stablemanager.REFRESH_NOTIFICATION_COUNT")
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationRefreshReceiver, filter)

    }

    private fun setupImageViewClick() {
        myImageView.setOnClickListener {
            replaceFragment(StartStableFragment.newInstance(), StartStableFragment.TAG)
        }
    }

    fun replaceFragment(fragment: Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment, tag)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navPosition = findNavigationPositionById(item.itemId)
        return switchFragment (navPosition)
    }

    private fun initBottomNavigation() {
        bottomNavigationOwner.setOnNavigationItemSelectedListener(this)
        bottomNavigationOwner.selectedItemId = R.id.horseMenu
    }

    private fun switchFragment (navPosition: BottomNavigationPosition): Boolean {
        val fragment : Fragment = supportFragmentManager.findFragment(navPosition)
        if (fragment.isAdded) return false
        detachFragment()
        attachFragment(fragment, navPosition.getTag())
        supportFragmentManager.executePendingTransactions()
        return true
    }

    private fun FragmentManager.findFragment(position: BottomNavigationPosition): Fragment {
        return findFragmentByTag(position.getTag()) ?: position.createFragment()
    }

    private fun detachFragment() {
        supportFragmentManager.findFragmentById(R.id.container)?.also {
            supportFragmentManager.beginTransaction().detach(it).commit()
        }
    }

    private fun attachFragment(fragment: Fragment, tag: String) {
        if (fragment.isDetached) {
            supportFragmentManager.beginTransaction().attach(fragment).commit()
        } else {
            supportFragmentManager.beginTransaction().add(R.id.container, fragment, tag).commit()
        }

        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}