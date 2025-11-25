package com.example.hwtasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hwtasks.data.AppDatabase
import com.example.hwtasks.data.TaskEntity
import com.example.hwtasks.data.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var repo: TaskRepository
    private lateinit var adapter: TaskAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: AppDatabase
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        database = AppDatabase.get(requireContext())  // Initialize database
        settingsManager = SettingsManager(requireContext())
        repo = TaskRepository(database.taskDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.taskRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = TaskAdapter(
            onClick = { task -> openDetail(task) },
            onDelete = { task -> deleteTask(task) },
            onToggleComplete = { task -> toggleTask(task) }
        )

        recyclerView.adapter = adapter

        // Load tasks initially
        loadTasks()
    }

    override fun onResume() {
        super.onResume()
        // Reload tasks when returning to this fragment (e.g., after changing settings)
        loadTasks()
    }

    private fun loadTasks() {
        viewLifecycleOwner.lifecycleScope.launch {
            // Choose the appropriate query based on settings
            val tasksFlow = if (settingsManager.sortByPriority) {
                database.taskDao().tasksByPriority()
            } else {
                database.taskDao().tasksByDueDate()
            }

            tasksFlow.collectLatest { tasks ->
                adapter.submitList(tasks)
            }
        }
    }  // <-- This closing brace was missing

    // 🔹 Open DetailFragment manually using FragmentManager
    private fun openDetail(task: TaskEntity) {
        val fragment = DetailFragment().apply {
            arguments = Bundle().apply {
                putLong("task_id", task.id)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun deleteTask(task: TaskEntity) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            repo.remove(task)
        }
    }

    private fun toggleTask(task: TaskEntity) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            repo.toggle(task)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}