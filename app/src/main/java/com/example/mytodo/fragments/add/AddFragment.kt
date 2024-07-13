package com.example.mytodo.fragments.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mytodo.R
import com.example.mytodo.data.models.ToDoData
import com.example.mytodo.data.models.Priority
import com.example.mytodo.data.viewmodel.ToDoViewModel

class AddFragment : Fragment() {

    private val mToDoViewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        // 打开选项菜单
        setHasOptionsMenu(true)

        return view
    }

    // 打开选项菜单时将其与add_fragment_menu绑定
    // inflate方法用于将XML布局文件转换成对应的视图对象View object
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val title_et = view?.findViewById<EditText>(R.id.title_et)
        val priorities_spinner = view?.findViewById<Spinner>(R.id.priorities_spinner)
        val description_et = view?.findViewById<EditText>(R.id.description_et)
        val mTitle = title_et?.text.toString()
        val mPriority = priorities_spinner?.selectedItem.toString()
        val mDescription = description_et?.text.toString()

        val validation = verifyDataFromUser(mTitle, mDescription)
        if (validation) {
            val newData = ToDoData(
                0,
                mTitle,
                parsePriority(mPriority),
                mDescription
            )
            mToDoViewModel.insertData(newData)
            // 显示添加成功的提醒
            Toast.makeText(requireContext(), "添加成功！", Toast.LENGTH_SHORT).show()

            // 导航回到todo表格页面
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "请填写数据", Toast.LENGTH_SHORT).show()
        }

    }

    private fun verifyDataFromUser(title: String, description: String): Boolean {
        return if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            false
        } else !(title.isEmpty() || description.isEmpty())
    }

    private fun parsePriority(priority: String): Priority {
        return when(priority){
            "优先级 高" -> { Priority.HIGH }
            "优先级 中" -> { Priority.MEDIUM }
            "优先级 低" -> { Priority.LOW }
            else -> Priority.LOW
        }
    }

}