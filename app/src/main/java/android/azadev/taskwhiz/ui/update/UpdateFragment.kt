package android.azadev.taskwhiz.ui.update

import android.app.AlertDialog
import android.azadev.taskwhiz.R
import android.azadev.taskwhiz.databinding.FragmentUpdateBinding
import android.azadev.taskwhiz.model.TodoData
import android.azadev.taskwhiz.utils.Constants
import android.azadev.taskwhiz.utils.UiExtension.selected
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/13/2024
 */

class UpdateFragment : Fragment(R.layout.fragment_update) {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UpdateFragmentArgs>()

    private val viewModel: UpdateViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUpdateBinding.bind(view)

        configureUi()

        configureMenu()

        changeSpinnerTextColor()

    }

    private fun configureUi() {
        val todoData = args.currentTodo
        binding.apply {
            edtCurrentTitle.setText(todoData.title)
            edtCurrentDescription.setText(todoData.description)
            spinnerCurrentPriority.setSelection(todoData.priority.ordinal)
        }
    }

    private fun updateTodoData() {
        binding.apply {
            val title = edtCurrentTitle.text.toString()
            val description = edtCurrentDescription.text.toString()
            val priority = spinnerCurrentPriority.selectedItem.toString()

            val validation = viewModel.verifyDataFromUser(title, description)
            if (validation) {
                val updateTodoData = TodoData(
                    id = args.currentTodo.id,
                    title = title,
                    description = description,
                    priority = Constants.parsePriorityWhenSaveToDatabase(priority),
                )
                viewModel.updateTodoData(updateTodoData)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.message_successfully_updated), Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.message_fill_all_fields), Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private fun changeSpinnerTextColor() {
        binding.spinnerCurrentPriority.selected { position, textView ->
            when (position) {
                0 -> textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                1 -> textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.yellow))
                2 -> textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
        }
    }

    private fun configureMenu() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_update_fragment, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.menu_save -> {
                            updateTodoData()
                            true
                        }

                        R.id.menu_delete -> {
                            confirmTodoDataRemoval()
                            true
                        }

                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED
        )
    }

    private fun confirmTodoDataRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.message_delete, args.currentTodo.title))
        builder.setMessage(
            getString(
                R.string.message_are_you_sure_remove,
                args.currentTodo.title
            )
        )
        builder.setPositiveButton(getString(R.string.text_yes)) { dialog, _ ->
            viewModel.deleteTodoData(args.currentTodo)
            Toast.makeText(
                requireContext(),
                getString(R.string.message_removed, args.currentTodo.title),
                Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton(getString(R.string.text_no)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}