package com.example.dip

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.dip.Class.User
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import org.json.JSONException

class Avtorization : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_avtorization)

        val button: Button = findViewById(R.id.buttonEnter)
        val log: EditText = findViewById(R.id.login)
        val pass: EditText = findViewById(R.id.password)

        //Используем функцию проверки соединения
        if(isOnline()){
            Toast.makeText(applicationContext, "Есть соединение", Toast.LENGTH_LONG).show()
        } else{
            Toast.makeText(applicationContext, "Нет соединения", Toast.LENGTH_LONG).show()
            finish()
        }

        //Обработчик авторизации
        button.setOnClickListener {
            //Переход на другое окно
            val intentStudent = Intent(this, Student::class.java)
            val intentTeacher = Intent(this, Teacher::class.java)

            val logA = log.text.toString()
            val passA = pass.text.toString()

            //Обработка ошибки
            try {
                //Проверка
                if(logA == "" || passA == ""){
                    Toast.makeText(applicationContext, "Поля не все заполнены", Toast.LENGTH_SHORT).show()
                } else if(logA.toBoolean() == logA.isEmailValid()){
                    Toast.makeText(applicationContext, "Почта некорректна", Toast.LENGTH_SHORT).show()
                }
                else{
                    lifecycleScope.launch {
                        try {
                            //Авторизация пользователя по электронной почте и паролю
                            SB.getClient().auth.signInWith(Email) {
                                email = logA
                                password = passA
                            }
                            Toast.makeText(applicationContext, "Вы авторизовались!", Toast.LENGTH_LONG).show()
                            //Получаем пользователя по id
                            val session = SB.getClient().auth.retrieveUserForCurrentSession(updateSession = true).id
                            val user = SB.getClient().from("Пользователь").select() {
                                filter {
                                    eq("ID_пользователя", session)
                                }
                            }.decodeSingle<User>()
                            //В зависимости от id роли переходим на определённый экран
                            when(user.id_роли){
                                1 -> startActivity(intentStudent)
                                2 -> startActivity(intentTeacher)
                            }
                        }catch (ex: Exception){
                            Toast.makeText(applicationContext, "Такого пользователя нет!", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }catch (ex: JSONException){
                Toast.makeText(applicationContext, "Ошибка", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Функция для проверки электронной почты
    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    //Функция для проверки интернет соединения
    fun isOnline(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(connectivityManager != null){
            val capability = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if(capability != null){
                if(capability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    //Toast.makeText(applicationContext, "Есть мобильный", Toast.LENGTH_LONG).show()
                    return true
                } else if (capability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    //Toast.makeText(applicationContext, "Есть WIFI", Toast.LENGTH_LONG).show()
                    return true
                } else if (capability.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    //Toast.makeText(applicationContext, "Есть проводной", Toast.LENGTH_LONG).show()
                    return true
                }
            }
        }
        return false
    }
}