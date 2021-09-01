package com.example.todomvc.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment<V : BaseView, P : BasePresenter<V>> : Fragment() {
    protected lateinit var mPresenter: P
    protected lateinit var mFragmentView: View

    protected abstract fun getViewContract(): V

    protected abstract fun getPresenter(): P

    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mPresenter = getPresenter()
        mPresenter.onAttach()
        mPresenter.setView(getViewContract())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutRes(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentView = view
        mPresenter.onViewCreated()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
    }

    override fun onPause() {
        mPresenter.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        mPresenter.onDestroyView()
        super.onDestroyView()
    }

    override fun onDetach() {
        mPresenter.onDetach()
        super.onDetach()
    }

    fun finishActivity() {
        activity?.finishAndRemoveTask()
    }
}