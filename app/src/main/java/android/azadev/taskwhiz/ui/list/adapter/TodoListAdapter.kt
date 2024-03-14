package android.azadev.taskwhiz.ui.list.adapter

import android.azadev.taskwhiz.R
import android.azadev.taskwhiz.databinding.ItemTodoBinding
import android.azadev.taskwhiz.model.Priority
import android.azadev.taskwhiz.model.TodoData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/14/2024
 */

class TodoListAdapter(
    private val onItemClickListener: (todo: TodoData) -> Unit
) :
    ListAdapter<TodoData, TodoListAdapter.TodoListViewHolder>(TodoListDiffUtil()) {

    inner class TodoListViewHolder(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(todo: TodoData) {
            binding.apply {
                tvTitle.text = todo.title
                tvDescription.text = todo.description
                binding.ivPriority.setBackgroundColor(
                    when (todo.priority) {
                        Priority.HIGH -> ContextCompat.getColor(binding.root.context, R.color.red)
                        Priority.MEDIUM -> ContextCompat.getColor(
                            binding.root.context,
                            R.color.yellow
                        )

                        Priority.LOW -> ContextCompat.getColor(binding.root.context, R.color.green)
                    }
                )
                binding.cardView.setOnClickListener {
                    onItemClickListener.invoke(todo)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        return TodoListViewHolder(
            ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class TodoListDiffUtil : DiffUtil.ItemCallback<TodoData>() {
        override fun areItemsTheSame(oldItem: TodoData, newItem: TodoData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoData, newItem: TodoData): Boolean {
            return oldItem == newItem
        }
    }
}