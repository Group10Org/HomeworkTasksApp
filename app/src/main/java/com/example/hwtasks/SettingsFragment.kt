package com.example.hwtasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var switchDueDate: Switch
    private lateinit var switchPriority: Switch
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ INITIALIZE settingsManager HERE
        settingsManager = SettingsManager(requireContext())

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switchDueDate = view.findViewById(R.id.switchDueDate)
        switchPriority = view.findViewById(R.id.switchPriority)

        // Load saved preferences
        switchDueDate.isChecked = settingsManager.sortByDueDate
        switchPriority.isChecked = settingsManager.sortByPriority

        // Due Date Switch Listener
        switchDueDate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switchPriority.isChecked = false
                settingsManager.sortByDueDate = true
                settingsManager.sortByPriority = false
            } else if (!switchPriority.isChecked) {
                // Don't allow both to be off - default to due date
                switchDueDate.isChecked = true
            }
        }

        // Priority Switch Listener
        switchPriority.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // ✅ FIXED: When priority is checked, turn off due date
                switchDueDate.isChecked = false
                settingsManager.sortByPriority = true
                settingsManager.sortByDueDate = false
            } else if (!switchDueDate.isChecked) {
                // Don't allow both to be off - default to due date
                switchDueDate.isChecked = true
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}