package com.example.dip

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            val mainActivity = Intent(this@MainActivity, Avtorization::class.java)
            startActivity(mainActivity)
        }, 5000)


//        try {
//            lifecycleScope.launch {
//                val city = SB.getClient().from("Пользователь").select()
//                Log.e("", city.data.toString())
//            }
//        } catch (ex: Exception){
//            Log.e("Ошибка", ex.message.toString())
//        }
    }
}