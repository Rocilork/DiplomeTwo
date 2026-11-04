package com.example.dip

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.dip.Class.ClassParserStudent
import com.example.dip.Class.ClassParserTeacher
import com.example.dip.Class.Groups
import com.example.dip.Class.ProfileStudent
import com.example.dip.Class.ProfileTeacher
import com.example.dip.Class.User
import io.github.jan.supabase.gotrue.SignOutScope
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import org.json.JSONException


class ProfileS : Fragment() {
    private lateinit var fio_s: TextView
    private lateinit var group_s: TextView
    private lateinit var course_s: TextView
    private lateinit var faculty_s: TextView
    private lateinit var button_exite_s: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_profile_s, container, false)
    }

    //Логика
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Находим View через переданный view
        fio_s = view.findViewById(R.id.FIO_S)
        group_s = view.findViewById(R.id.Group_S)
        course_s = view.findViewById(R.id.Course_S)
        faculty_s = view.findViewById(R.id.Faculty_S)
        button_exite_s = view.findViewById(R.id.buttonExiteS)

        lifecycleScope.launch {
            //Получаем авторизованную сессию
            val currentUserId = SB.getClient().auth.currentSessionOrNull()?.user?.id

            //Получаем все записи из таблицы ПрофильСтудента
            val students = SB.getClient().from("ПрофильСтудента").select {
                filter {
                    eq("ID_студента", currentUserId.toString())
                }
            }.decodeList<ProfileStudent>()
            Log.e("", students.toString())

            //Вытаскиваем уникальные ID пользователей
            val userIds = students.map{it.ID_студента}.distinct()
            Log.e("", userIds.toString())

            //Вытаскиваем уникальные ID групп
            val groupsIds = students.map {it.id_группы}.distinct()
            Log.e("", groupsIds.toString())

            //Запрашиваем пользователей, чьи ID попали в список
            val userMap = SB.getClient().postgrest["Пользователь"].select{
                filter { isIn("ID_пользователя", userIds) }
            }.decodeList<User>().associateBy { it.ID_пользователя }
            Log.e("", userMap.toString())

            //Запрашиваем группы, чьи ID попали в список
            val groupMap = SB.getClient().postgrest["Группы"].select{
                filter {isIn(column = "ID_группы", groupsIds)}
            }.decodeList<Groups>().associateBy { it.ID_группы }
            Log.e("", groupMap.toString())

            //Объединяем данные
            val results = students.map { student ->
                val user = userMap[student.ID_студента]
                val group = groupMap[student.id_группы]
                ClassParserStudent(
                    record = student,
                    userFIO = user?.ФИО?: "",
                    groupName = group?.Название?: ""
                )
            }

            // Вывод результата
            results.forEach {
                fio_s.text = it.userFIO
                group_s.text = ("Группа: ${it.groupName}")
                course_s.text = ("Курс: ${it.record.Курс}")
                faculty_s.text = ("Факультет: ${it.record.Факультет}")
            }
        }

        //Деавторизация
        button_exite_s.setOnClickListener {
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