package com.example.todomvc.presentation

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity

abstract class BasePresenter<V : BaseView> {
    protected lateinit var mView: V

    fun setView(view: V) {
        mView = view
    }

    open fun onAttach() {
        // NOP
    }

    open fun onViewCreated() {
        // NOP
    }

    open fun onResume() {
        // NOP
    }

    open fun onPause() {
        // NOP
    }

    open fun onDestroyView() {
        // NOP
    }

    open fun onDetach() {
        // NOP
    }

    fun startActivity(intent: Intent) {
        mView.startActivity(intent)
    }

    fun startActivityForResult(intent: Intent, requestCode: Int) {
        mView.startActivityForResult(intent, requestCode)
    }

    fun finishActivity() {
        mView.finishActivity()
    }

    fun getContext(): Context {
        return mView.getContext()
    }

    fun getActivity(): FragmentActivity? {
        return mView.getActivity()
    }
}