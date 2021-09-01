package com.example.todomvc.presentation.presentor.todo

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.todomvc.androidApp.R
import com.example.todomvc.model.Todo
import com.example.todomvc.presentation.BaseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class TodoListFragment : BaseFragment<TodoListContract.View, TodoListPresenter>(), TodoListContract.View {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_all -> {
                mPresenter.showAll()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_active -> {
                mPresenter.showActive()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_check -> {
                mPresenter.showCompleted()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        requireActivity().findViewById<Button>(R.id.addButton).setOnClickListener {
            val textView = requireActivity().findViewById<EditText>(R.id.editText)
            if (textView.text.isNotEmpty()) {
                mPresenter.add(requireActivity().findViewById<EditText>(R.id.editText).text.toString())
                textView.text.clear()
            }
        }
        val listView = requireActivity().findViewById<ListView>(R.id.todo_list)
        listView.setOnItemClickListener { asasd, view, i, id ->

            val item = asasd.adapter.getItem(i) as Todo
            val editText = view.findViewById<EditText>(R.id.todoText)
            if (id == R.id.delete_button.toLong()) {
                mPresenter.delete(item)
            }
            if (id == R.id.checkFrameLayout.toLong()) {

                val check = view.findViewById<ImageView>(R.id.image_check)
                val done = check.visibility == View.VISIBLE

                mPresenter.update(item.copy(completed = !done, title = editText.text.toString()))
            }

            if(id == R.id.todoText.toLong()){
                mPresenter.update(item.copy(title = editText.text.toString()))
            }
        }

    }

    override fun showChangeItem(list: MutableList<Todo>) {
        val listView = requireActivity().findViewById<ListView>(R.id.todo_list)
        val adapter = TodoListAdapter(requireActivity(), R.layout.todo_list_item, list)
        listView.adapter = adapter
    }

    override fun getViewContract(): TodoListContract.View {
        return this
    }

    override fun getPresenter(): TodoListPresenter {
        return TodoListPresenter()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun getContext(): Context {
        return requireActivity()
    }
}