package com.example.dip

import android.content.Intent
import android.provider.Telephony
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//ТЕСТИРОВАНИЕ ИНТЕРФЕЙСА  UI-ТЕСТОВ
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    // Используем IntentsRule для перехвата интентов
    @get:Rule
    val intentsRule = IntentsRule()

    @get:Rule
    val activityRule = ActivityScenarioRule(Avtorization::class.java)

    @Test
    fun testSuccessfulLogin() {
        // Ввод логина и пароля
        onView(withId(R.id.login)).perform(typeText("test@gmail.com"))
        onView(withId(R.id.password)).perform(typeText("111222"))
        // Нажатие кнопки
        onView(withId(R.id.buttonEnter)).perform(click())
        //Проверяем, что был отправлен Intent на запуск Student Activity
        //intended(hasComponent(Student::class.java.name))
    }
}