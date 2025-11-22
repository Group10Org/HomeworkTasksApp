package com.example.hwtasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hwtasks.data.TaskEntity
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(
    private val onClick: (TaskEntity) -> Unit,
    private val onDelete: (TaskEntity) -> Unit,
    private val onToggleComplete: (TaskEntity) -> Unit
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private var tasks: List<TaskEntity> = emptyList()

    fun submitList(newList: List<TaskEntity>) {
        tasks = newList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvInfo: TextView = itemView.findViewById(R.id.tvInfo)
        val tvPriorityEmoji: TextView = itemView.findViewById(R.id.tvPriorityEmoji)
        val btnComplete: TextView = itemView.findViewById(R.id.btnComplete)
        val btnDelete: TextView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]

        val className = task.className ?: "No class"

        val dueString = task.dueAt?.let {
            val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            sdf.format(Date(it * 1000L))
        } ?: "No due date"

        holder.tvInfo.text = "$className - ${task.title} - $dueString"

        val emoji = when (task.priority) {
            1 -> "🔴"
            2 -> "🟡"
            else -> "🟢"
        }
        holder.tvPriorityEmoji.text = emoji

        holder.tvInfo.paint.isStrikeThruText = task.completionStatus


        holder.itemView.setOnClickListener { onClick(task) }


        holder.btnComplete.setOnClickListener { onToggleComplete(task) }
        holder.btnDelete.setOnClickListener { onDelete(task) }
    }

    override fun getItemCount(): Int = tasks.size
}
