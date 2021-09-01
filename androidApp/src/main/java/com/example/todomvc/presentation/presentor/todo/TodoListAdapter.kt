package com.example.todomvc.presentation.presentor.todo

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.todomvc.androidApp.R
import com.example.todomvc.model.Todo


class TodoListAdapter(context: Context, resource: Int, list: MutableList<Todo>) :
    ArrayAdapter<Todo>(context, resource, list) {

    private val mResource = resource
    private val mContext = context
    private val mList = list
    private var mInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (convertView == null) {
            view = mInflater.inflate(mResource, null)
        } else {
            view = convertView
        }

        val item = mList[position]
        val editText = view.findViewById<EditText>(R.id.todoText)
        val paint = editText.paint


        if (editText.text.isEmpty()) {
            editText.setText(item.title, TextView.BufferType.NORMAL)
        }

        editText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val edit = v as EditText
                mList[position] = item.copy(title = edit.text.toString())
            }
        }

        editText.setOnClickListener {
            val list = parent as ListView
            list.performItemClick(view, position, R.id.todoText.toLong())
        }

        view.findViewById<FrameLayout>(R.id.checkFrameLayout).setOnClickListener {
            val list = parent as ListView
            list.performItemClick(view, position, R.id.checkFrameLayout.toLong())
        }


        val check = view.findViewById<ImageView>(R.id.image_check)

        val delete = view.findViewById<ImageView>(R.id.delete_button)
        delete.setOnClickListener {
            val list = parent as ListView
            list.performItemClick(view, position, R.id.delete_button.toLong())
        }

        if (!item.completed) {
            check.visibility = View.GONE
            editText.setTextColor(Color.BLACK)
            paint.flags = editText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        } else {
            check.visibility = View.VISIBLE
            editText.setTextColor(Color.LTGRAY)
            paint.flags = editText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            paint.isAntiAlias = true
        }

        return view
    }
}