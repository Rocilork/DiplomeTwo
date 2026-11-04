package com.example.dip

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Student : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.homeFragment -> RecordBookS()
                R.id.statisticFragment -> StatisticS()
                R.id.messageFragment -> MessageS()
                R.id.profileFragment -> ProfileS()
                else -> return@setOnItemSelectedListener false
            }
            switchFragment(fragment)
            true
        }

        // Открываем стартовый фрагмент при первом запуске
        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.homeFragment
        }


    }
    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}