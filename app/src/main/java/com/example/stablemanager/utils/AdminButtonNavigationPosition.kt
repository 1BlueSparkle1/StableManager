package com.example.stablemanager.utils

import androidx.fragment.app.Fragment
import com.example.stablemanager.Pages.AdminPages.Fragments.HomeAdminFragment
import com.example.stablemanager.Pages.AdminPages.Fragments.OptionsFragment
import com.example.stablemanager.Pages.AdminPages.Fragments.ProfileAdminFragment
import com.example.stablemanager.R

enum class AdminBottomNavigationPosition(val position: Int, val id: Int){
    FIRST(0, R.id.profileMenu),
    SECOND(1, R.id.homeMenu),
    THIRD(2, R.id.optionsMenu)
}

fun findAdminNavigationPositionById(id: Int): AdminBottomNavigationPosition = when (id) {
    AdminBottomNavigationPosition.FIRST.id -> AdminBottomNavigationPosition.FIRST
    AdminBottomNavigationPosition.SECOND.id -> AdminBottomNavigationPosition.SECOND
    AdminBottomNavigationPosition.THIRD.id -> AdminBottomNavigationPosition.THIRD
    else -> AdminBottomNavigationPosition.FIRST
}

fun AdminBottomNavigationPosition.createFragment(): Fragment = when (this){
    AdminBottomNavigationPosition.FIRST -> ProfileAdminFragment.newInstance()
    AdminBottomNavigationPosition.SECOND -> HomeAdminFragment.newInstance()
    AdminBottomNavigationPosition.THIRD -> OptionsFragment.newInstance()
}

fun AdminBottomNavigationPosition.getTag(): String = when (this){
    AdminBottomNavigationPosition.FIRST -> ProfileAdminFragment.TAG
    AdminBottomNavigationPosition.SECOND -> HomeAdminFragment.TAG
    AdminBottomNavigationPosition.THIRD -> OptionsFragment.TAG
}