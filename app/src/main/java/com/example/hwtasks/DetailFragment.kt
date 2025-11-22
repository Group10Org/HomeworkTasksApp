package com.example.hwtasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.hwtasks.data.AppDatabase
import com.example.hwtasks.data.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailFragment : Fragment() {

    private lateinit var repo: TaskRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.get(requireContext())
        repo = TaskRepository(db.taskDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvTitle = view.findViewById<TextView>(R.id.tvDetailTitle)
        val tvClass = view.findViewById<TextView>(R.id.tvDetailClass)
        val tvDue = view.findViewById<TextView>(R.id.tvDetailDue)
        val tvPriority = view.findViewById<TextView>(R.id.tvDetailPriority)
        val tvDesc = view.findViewById<TextView>(R.id.tvDetailDescription)
        val btnBack = view.findViewById<Button>(R.id.btnBack)

        val taskId = arguments?.getLong("task_id") ?: -1L
        if (taskId == -1L) {
            tvTitle.text = "Error: no task id"
            return
        }


        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val task = repo.getTask(taskId)

            if (task == null) {
                withContext(Dispatchers.Main) {
                    tvTitle.text = "Task not found"
                }
                return@launch
            }

            val dueString = task.dueAt?.let {
                val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                sdf.format(Date(it * 1000L))
            } ?: "No due date"

            val priorityText = when (task.priority) {
                1 -> "Priority: 🔴 High"
                2 -> "Priority: 🟡 Medium"
                else -> "Priority: 🟢 Low"
            }

            withContext(Dispatchers.Main) {
                tvTitle.text = task.title
                tvClass.text = "Class: ${task.className ?: "None"}"
                tvDue.text = "Due: $dueString"
                tvPriority.text = priorityText
                tvDesc.text = task.description ?: "No description"
            }
        }


        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()

        }
    }
}
