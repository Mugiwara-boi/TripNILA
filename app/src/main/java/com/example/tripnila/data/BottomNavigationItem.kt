package com.example.tripnila.data

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val hasNews: Boolean,
    val badgeCount: Int? = null,
)

