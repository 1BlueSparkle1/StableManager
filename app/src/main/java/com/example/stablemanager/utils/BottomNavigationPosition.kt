package com.example.stablemanager.utils

import androidx.fragment.app.Fragment
import com.example.stablemanager.Pages.OwnerPages.Fragments.EmployeeFragment
import com.example.stablemanager.Pages.OwnerPages.Fragments.HorseFragment
import com.example.stablemanager.R
import com.example.stablemanager.RecordFragment
import com.example.stablemanager.WarehouseFragment

enum class BottomNavigationPosition(val position: Int, val id: Int){
    FIRST(0, R.id.horseMenu),
    SECOND(1, R.id.employeeMenu),
    THIRD(2, R.id.recordMenu),
    FOURTH(3, R.id.warehouseMenu)
}

fun findNavigationPositionById(id: Int): BottomNavigationPosition = when (id) {
    BottomNavigationPosition.FIRST.id -> BottomNavigationPosition.FIRST
    BottomNavigationPosition.SECOND.id -> BottomNavigationPosition.SECOND
    BottomNavigationPosition.THIRD.id -> BottomNavigationPosition.THIRD
    BottomNavigationPosition.FOURTH.id -> BottomNavigationPosition.FOURTH
    else -> BottomNavigationPosition.FIRST
}

fun BottomNavigationPosition.createFragment(): Fragment = when (this){
    BottomNavigationPosition.FIRST -> HorseFragment.newInstance()
    BottomNavigationPosition.SECOND -> EmployeeFragment.newInstance()
    BottomNavigationPosition.THIRD -> RecordFragment.newInstance()
    BottomNavigationPosition.FOURTH -> WarehouseFragment.newInstance()
}

fun BottomNavigationPosition.getTag(): String = when (this){
    BottomNavigationPosition.FIRST -> HorseFragment.TAG
    BottomNavigationPosition.SECOND -> EmployeeFragment.TAG
    BottomNavigationPosition.THIRD -> RecordFragment.TAG
    BottomNavigationPosition.FOURTH -> WarehouseFragment.TAG
}