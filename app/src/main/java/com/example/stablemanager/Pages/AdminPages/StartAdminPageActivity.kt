package com.example.stablemanager.Pages.AdminPages

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.stablemanager.Pages.AdminPages.Fragments.HomeAdminFragment
import com.example.stablemanager.Pages.AdminPages.Fragments.NotifyAdminFragment
import com.example.stablemanager.R
import com.example.stablemanager.utils.AdminBottomNavigationPosition
import com.example.stablemanager.utils.createFragment
import com.example.stablemanager.utils.findAdminNavigationPositionById
import com.example.stablemanager.utils.getTag
import com.google.android.material.bottomnavigation.BottomNavigationView

class StartAdminPageActivity : AppCompatActivity() , BottomNavigationView.OnNavigationItemSelectedListener {
    private var navPosition: AdminBottomNavigationPosition = AdminBottomNavigationPosition.FIRST
    private lateinit var bottomNavigationAdmin: BottomNavigationView
    private lateinit var bellImageView: ImageView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_admin_page)
        bottomNavigationAdmin = findViewById(R.id.bottomNavigationAdmin)
        bellImageView = findViewById(R.id.notifyBell)
        initBottomNavigation()
        setupImageViewClick()

        if (savedInstanceState == null) {
            replaceFragment(HomeAdminFragment.newInstance(), HomeAdminFragment.TAG)
        }
    }

    private fun setupImageViewClick() {
        bellImageView.setOnClickListener {
            replaceFragment(NotifyAdminFragment.newInstance(), NotifyAdminFragment.TAG)
        }
    }

    fun replaceFragment(fragment: Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.containerAdmin, fragment, tag)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navPosition = findAdminNavigationPositionById(item.itemId)
        return switchFragment (navPosition)
    }

    private fun initBottomNavigation() {
        bottomNavigationAdmin.setOnNavigationItemSelectedListener(this)
        bottomNavigationAdmin.selectedItemId = R.id.homeMenu
    }

    private fun switchFragment (navPosition: AdminBottomNavigationPosition): Boolean {
        val fragment : Fragment = supportFragmentManager.findFragment(navPosition)
        if (fragment.isAdded) return false
        detachFragment()
        attachFragment(fragment, navPosition.getTag())
        supportFragmentManager.executePendingTransactions()
        return true
    }

    private fun FragmentManager.findFragment(position: AdminBottomNavigationPosition): Fragment {
        return findFragmentByTag(position.getTag()) ?: position.createFragment()
    }

    private fun detachFragment() {
        supportFragmentManager.findFragmentById(R.id.containerAdmin)?.also {
            supportFragmentManager.beginTransaction().detach(it).commit()
        }
    }

    private fun attachFragment(fragment: Fragment, tag: String) {
        if (fragment.isDetached) {
            supportFragmentManager.beginTransaction().attach(fragment).commit()
        } else {
            supportFragmentManager.beginTransaction().add(R.id.containerAdmin, fragment, tag).commit()
        }

        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}