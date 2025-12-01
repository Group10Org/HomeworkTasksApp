package com.example.hwtasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hwtasks.data.AppDatabase
import com.example.hwtasks.data.TaskEntity
import com.example.hwtasks.data.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var repo: TaskRepository
    private lateinit var incompleteAdapter: TaskAdapter
    private lateinit var incompleteRecyclerView: RecyclerView
    private lateinit var completedRecyclerView: RecyclerView
    private lateinit var completedAdapter: TaskAdapter
    private var incompleteTasksJob: Job? = null
    private var completedTasksJob: Job? = null
    private lateinit var database: AppDatabase
    private lateinit var settingsManager: SettingsManager
    private var trueCount: Long = 0

    private var curr : Long? = System.currentTimeMillis()


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

        // Setup Incomplete Tasks RecyclerView
        incompleteRecyclerView = view.findViewById(R.id.taskRecyclerView)
        incompleteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val incomplete: TextView = view.findViewById(R.id.incompleteTaskCountView)
        val complete: TextView = view.findViewById(R.id.completeTaskCountView)
        val total: TextView = view.findViewById(R.id.totalTaskCountView)
        val sorted: TextView = view.findViewById(R.id.pastDueView)
        incompleteAdapter = TaskAdapter(
            onClick = { task -> openDetail(task) },
            onDelete = { task -> deleteTask(task) },
            onToggleComplete = { task -> toggleTask(task) }
        )
        incompleteRecyclerView.adapter = incompleteAdapter

        // Setup Completed Tasks RecyclerView
        completedRecyclerView = view.findViewById(R.id.completedTaskView)
        completedRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        completedAdapter = TaskAdapter(
            onClick = { task -> openDetail(task) },
            onDelete = { task -> deleteTask(task) },
            onToggleComplete = { task -> toggleTask(task) }
        )
        completedRecyclerView.adapter = completedAdapter


        // Load tasks initially
        loadTasks()
        countIncompleted(incomplete)
        countCompleted(complete)
        countTotal(total)
        countPastDue(sorted)
    }

    override fun onResume() {
        super.onResume()
        // Reload tasks when returning to this fragment (e.g., after changing settings)
        loadTasks()
    }

    private fun loadTasks() {
        // Cancel previous collections if any
        incompleteTasksJob?.cancel()
        completedTasksJob?.cancel()

        // Load incomplete tasks
        incompleteTasksJob = viewLifecycleOwner.lifecycleScope.launch {
            repo.getIncompleteTasks(sortByPriority = settingsManager.sortByPriority)
                .collectLatest { tasks ->
                    incompleteAdapter.submitList(tasks)
                }
        }

        // Load completed tasks
        completedTasksJob = viewLifecycleOwner.lifecycleScope.launch {
            repo.getCompletedTasks(sortByPriority = settingsManager.sortByPriority)
                .collectLatest { tasks ->
                    completedAdapter.submitList(tasks)
                }

        }

    }

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
    //Statistics
    //Incomplete Tasks
    private fun countIncompleted(textView: TextView) {
        viewLifecycleOwner.lifecycleScope.launch {
            repo.incompleteCount.collectLatest { count ->
                textView.text = "Incomplete Tasks: $count"
                trueCount = count
            }
      /*  if(trueCount > 0)
        {

        } */
        }
    }
    private fun countTotal(textView: TextView)
    {
        viewLifecycleOwner.lifecycleScope.launch {
            repo.totalCount.collectLatest { count ->
                textView.text = "Total Tasks: $count"
            }
        }
    }

    private fun countPastDue(textView: TextView) {
        viewLifecycleOwner.lifecycleScope.launch {
            repo.pastDueCount.collectLatest { count ->
                textView.text = "Assignments Past Due: $count"
            }

        }
    }

    private fun countCompleted(textView: TextView) {
        viewLifecycleOwner.lifecycleScope.launch {
            repo.completedCount.collectLatest { count ->
                textView.text = "Completed Tasks: $count"
            }
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