package com.example.hwtasks

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.hwtasks.data.AppDatabase
import com.example.hwtasks.data.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class CreationFragment : Fragment() {

    private lateinit var repo: TaskRepository
    private var selectedDueAt: Long? = null  // epoch seconds

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
        return inflater.inflate(R.layout.fragment_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleField = view.findViewById<EditText>(R.id.editTextText)      // Title
        val classField = view.findViewById<EditText>(R.id.editTextText2)     // Class
        val descField = view.findViewById<EditText>(R.id.editTextText3)      // Description
        val priorityField = view.findViewById<EditText>(R.id.editTextText4)  // Priority
        val dateField = view.findViewById<EditText>(R.id.editTextDate)       // Due date
        val createButton = view.findViewById<Button>(R.id.button)

        // Date picker
        dateField.setOnClickListener {
            showDatePicker(dateField)
        }

        createButton.setOnClickListener {
            val titleText = titleField.text.toString().trim()
            val classText = classField.text.toString().trim()
            val descText = descField.text.toString().trim()

            if (titleText.isBlank()) {
                Toast.makeText(requireContext(), "Title is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val priority = priorityField.text.toString()
                .trim()
                .toIntOrNull()
                ?.coerceIn(1, 3) ?: 3

            val dueAt = selectedDueAt


            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                repo.add(
                    title = titleText,
                    desc = descText.ifBlank { null },
                    className = classText.ifBlank { null },
                    priority = priority,
                    dueAt = dueAt
                )

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Task created", Toast.LENGTH_SHORT).show()

                    // Clear fields
                    titleField.text?.clear()
                    classField.text?.clear()
                    descField.text?.clear()
                    priorityField.text?.clear()
                    dateField.text?.clear()
                    selectedDueAt = null
                }
            }
        }
    }

    private fun showDatePicker(targetField: EditText) {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(
            requireContext(),
            { _, y, m, d ->
                val display = "${m + 1}/$d/$y"
                targetField.setText(display)

                val chosen = Calendar.getInstance().apply {
                    set(Calendar.YEAR, y)
                    set(Calendar.MONTH, m)
                    set(Calendar.DAY_OF_MONTH, d)
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 59)
                    set(Calendar.MILLISECOND, 0)
                }
                selectedDueAt = chosen.timeInMillis / 1000L
            },
            year,
            month,
            day
        )

        dialog.show()
    }
}
