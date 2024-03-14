package android.azadev.taskwhiz.ui.add

import android.azadev.taskwhiz.R
import android.azadev.taskwhiz.databinding.FragmentAddBinding
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
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/13/2024
 */

class AddFragment : Fragment(R.layout.fragment_add) {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddBinding.bind(view)

        configureMenu()

        changeSpinnerTextColor()

    }

    private fun insertDataToDatabase() {
        binding.apply {
            val mTitle = edtTitle.text.toString()
            val mDescription = edtDescription.text.toString()
            val priority = spinnerPriority.selectedItem.toString()

            val validation = viewModel.verifyDataFromUser(mTitle, mDescription)
            if (validation) {
                val data = TodoData(
                    priority = Constants.parsePriorityWhenSaveToDatabase(priority),
                    title = mTitle,
                    description = mDescription
                )
                viewModel.upsertData(data)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.message_successfully_added), Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_addFragment_to_listFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.message_fill_all_fields),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun configureMenu() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_add_fragment, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return if (menuItem.itemId == R.id.menu_add) {
                        insertDataToDatabase()
                        true
                    } else false
                }
            },
            viewLifecycleOwner, Lifecycle.State.RESUMED
        )
    }

    private fun changeSpinnerTextColor() {
        binding.spinnerPriority.selected { position, textView ->
            when (position) {
                0 -> textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                1 -> textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.yellow))
                2 -> textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}