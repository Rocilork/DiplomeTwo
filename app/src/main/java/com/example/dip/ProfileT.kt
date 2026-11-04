package com.example.dip

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.dip.Class.ClassParserTeacher
import com.example.dip.Class.ProfileTeacher
import com.example.dip.Class.User
import io.github.jan.supabase.gotrue.SignOutScope
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class ProfileT : Fragment() {
    private lateinit var fio_t: TextView
    private lateinit var faculty_t: TextView
    private lateinit var department_t: TextView
    private lateinit var place_work_t: TextView
    private lateinit var button_exite_t: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_profile_t, container, false)
    }

    //Логика
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Находим View через переданный view
        fio_t = view.findViewById(R.id.FIO_T)
        faculty_t = view.findViewById(R.id.Faculty_T)
        department_t = view.findViewById(R.id.Department_T)
        place_work_t = view.findViewById(R.id.PlaceWork_T)
        button_exite_t = view.findViewById(R.id.buttonExiteT)

        lifecycleScope.launch {
            //Получаем авторизованную сессию
            val currentUserId = SB.getClient().auth.currentSessionOrNull()?.user?.id

            //Получаем все записи из таблицы ПрофильПреподавателя
            val teachers = SB.getClient().from("ПрофильПреподавателя").select{
                filter {
                    eq("ID_преподавателя", currentUserId.toString())
                }
            }.decodeList<ProfileTeacher>()
            Log.e("", teachers.toString())

            //Вытаскиваем уникальные ID пользователей
            val userIds = teachers.map{it.ID_преподавателя}.distinct()
            Log.e("", userIds.toString())

            //Запрашиваем пользователей, чьи ID попали в список
            val userMap = SB.getClient().postgrest["Пользователь"].select{
                filter { isIn("ID_пользователя", userIds) }
            }.decodeList<User>().associateBy { it.ID_пользователя }
            Log.e("", userMap.toString())

            //Объединяем данные
            val results = teachers.map { teacher ->
                val user = userMap[teacher.ID_преподавателя]
                ClassParserTeacher(
                    record = teacher,
                    userFIO = user?.ФИО?: ""
                )
            }

            // Вывод результата
            results.forEach {
                fio_t.text = it.userFIO
                faculty_t.text = ("Факультет: ${it.record.Факультет}")
                department_t.text = ("Кафедра: ${it.record.Кафедра}")
                place_work_t.text = ("Место работы: ${it.record.МестоРаботы}")
            }
        }

        //Деавторизация
        button_exite_t.setOnClickListener {
            try {
                lifecycleScope.launch {
                    try {
                        //Выход из всей системы
                        SB.getClient().auth.signOut(SignOutScope.GLOBAL)

                        startActivity(Intent(requireContext(), Avtorization::class.java))
                    }catch (ex: Exception){
                        Toast.makeText(requireContext(), "Ошибка0", Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (ex: Exception){
                Toast.makeText(requireContext(), "Ошибка1", Toast.LENGTH_SHORT).show()
            }
        }
    }
}