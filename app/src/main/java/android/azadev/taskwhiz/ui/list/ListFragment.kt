package android.azadev.taskwhiz.ui.list

import android.app.AlertDialog
import android.azadev.taskwhiz.R
import android.azadev.taskwhiz.databinding.FragmentListBinding
import android.azadev.taskwhiz.model.TodoData
import android.azadev.taskwhiz.ui.list.adapter.TodoListAdapter
import android.azadev.taskwhiz.ui.list.viewmodel.TodoListViewModel
import android.azadev.taskwhiz.utils.UiExtension.visibleIf
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/13/2024
 */

class ListFragment : Fragment(R.layout.fragment_list), SearchView.OnQueryTextListener {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TodoListViewModel by viewModel()
    private lateinit var todoListAdapter: TodoListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentListBinding.bind(view)

        configureMenu()

        configureAdapter()

        viewModel.getAllTodo.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                viewModel.checkIsEmptyDatabase()
                todoListAdapter.submitList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)


        viewModel.isEmptyDatabase.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .distinctUntilChanged()
            .onEach {
                showEmptyViews(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.searchResult.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                todoListAdapter.submitList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)


        binding.fbAdd.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
    }

    private fun swipeDelete() {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = todoListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteTodoData(deletedItem)
                restoreDeletedData(viewHolder.itemView, deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvUser)
    }

    private fun configureAdapter() {
        todoListAdapter = TodoListAdapter { data ->
            val actionListFragmentToUpdateFragment =
                ListFragmentDirections.actionListFragmentToUpdateFragment(data)
            findNavController().navigate(actionListFragmentToUpdateFragment)
        }
        binding.rvUser.adapter = todoListAdapter
        binding.rvUser.itemAnimator = LandingAnimator().apply {
            addDuration = 300
        }
        swipeDelete()
    }

    private fun restoreDeletedData(view: View, deletedItem: TodoData) {
        Snackbar.make(
            view,
            getString(R.string.message_removed, deletedItem.title),
            Snackbar.LENGTH_LONG
        ).setAction(getString(R.string.text_undo)) {
            viewModel.upsertTodoData(deletedItem)
        }.show()
    }

    private fun showEmptyViews(boolean: Boolean) {
        binding.ivNoData.visibleIf(boolean)
        binding.tvNoData.visibleIf(boolean)
    }

    private fun configureMenu() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_list_fragment, menu)
                    val search = menu.findItem(R.id.menu_search)
                    val searchView = search.actionView as SearchView
                    searchView.isSubmitButtonEnabled = true
                    searchView.setOnQueryTextListener(this@ListFragment)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.menu_delete_all -> {
                            confirmAllTodoRemoval()
                            true
                        }

                        R.id.menu_priority_high -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.sortByHighPriority.collect {
                                    todoListAdapter.submitList(it)
                                }
                            }
                            true
                        }

                        R.id.menu_priority_low -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.sortByLowPriority.collect {
                                    todoListAdapter.submitList(it)
                                }
                            }
                            true
                        }

                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED
        )
    }

    private fun confirmAllTodoRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.message_are_you_sure_remove_everything))
        builder.setTitle(getString(R.string.message_delete_everything))
        builder.setPositiveButton(getString(R.string.text_yes)) { dialog, _ ->
            viewModel.deleteAll()
            dialog.dismiss()
            Toast.makeText(
                requireContext(),
                getString(R.string.message_removed_everything), Toast.LENGTH_SHORT
            ).show()
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchThroughDatabase(newText)
        }
        return true
    }


    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"
        viewModel.searchDatabase(searchQuery)
    }

}