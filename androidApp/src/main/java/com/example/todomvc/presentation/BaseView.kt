
package com.example.todomvc.presentation

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity


interface BaseView {
    fun startActivity(intent: Intent)

    fun startActivityForResult(intent: Intent, requestCode: Int)

    fun finishActivity()

    fun getContext(): Context

    fun getActivity(): FragmentActivity?
}