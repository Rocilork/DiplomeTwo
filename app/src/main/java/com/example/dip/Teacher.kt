package com.example.dip

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Teacher : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_teacher)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav2)

        bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.homeFragment -> ListT()
                R.id.statisticFragment -> StatisticT()
                R.id.messageFragment -> MessageT()
                R.id.profileFragment -> ProfileT()
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
            .replace(R.id.fragment_container2, fragment)
            .commit()
    }
}