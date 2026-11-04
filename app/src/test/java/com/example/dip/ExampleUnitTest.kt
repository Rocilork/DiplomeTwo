package com.example.dip

import android.app.Notification
import com.example.dip.Tests.AuthValidator
import com.example.dip.Tests.NotificationsProfile
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//ТЕСТИРОВАНИЕ ЛОГИКИ ЮНИТ-ТЕСТОВ
class AuthValidatorTest {
    //Проверка логики, что почта совпадает в системе
    @Test
    fun emailValidator_CorrectEmailSimple() {
        val email = AuthValidator()
        val result = email.isValidEmail("test@gmail.com")
        assertEquals("test@gmail.com", result)
    }
    //Проверка логики, что пароль совпадает в системе
    @Test
    fun passwordValidator_CorrectPasswordSimple() {
        val password = AuthValidator()
        val result = password.isPasswordValid("111222")
        assertEquals("111222", result)
    }
    //Проверка логики, что уведомления включены
    @Test
    fun notificationValidator_OnNotificationSimple() {
        val notification = NotificationsProfile()
        val result = notification.isNotificationsValid(true)
        assertTrue(result)
    }
    //Проверка логики, что уведомления выключены
    @Test
    fun notificationValidator_OffNotificationSimple() {
        val notification = NotificationsProfile()
        val result = notification.isNotificationsValid(false)
        assertFalse(result)
    }
}