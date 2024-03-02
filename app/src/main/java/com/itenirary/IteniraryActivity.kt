package com.itenirary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.tripnila.R
import com.itenirary.fragments.IteniraryFragment

class IteniraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_itenirary)

        val touristId = intent.getStringExtra("touristId")

        val iteniraryFragment = IteniraryFragment().apply {
            arguments = Bundle().apply {
                putString("touristId", touristId)
            }
        }

        loadIteniraryFragment(iteniraryFragment)
    }

    private fun loadIteniraryFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.frItenirary, fragment)
            .commit()
    }
}