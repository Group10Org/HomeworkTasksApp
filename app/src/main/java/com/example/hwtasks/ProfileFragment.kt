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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        val db = AppDatabase.get(requireContext())
        repo = TaskRepository(db.taskDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.taskRecyclerView)

        adapter = TaskAdapter(
            onClick = { task -> openDetail(task) },
            onDelete = { task -> deleteTask(task) },
            onToggleComplete = { task -> toggleTask(task) }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repo.tasks.collectLatest { list ->
                adapter.submitList(list)
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
            // TODO: replace `fragment_container` with the actual id
            // of the container in your activity_main.xml that hosts fragments
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

