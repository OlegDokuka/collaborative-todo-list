package com.example.todomvc.presentation

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseActivity : AppCompatActivity() {
    fun replaceFragment(fragment: Fragment, @IdRes containerId: Int) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(containerId, fragment, fragment.javaClass.simpleName)
        ft.commit()
    }
}