package com.fredrikbogg.notesapp.ui.notes

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fredrikbogg.notesapp.util.EventObserver
import com.fredrikbogg.notesapp.R
import com.fredrikbogg.notesapp.databinding.FragmentNotesBinding
import com.fredrikbogg.notesapp.ui.edit_note.EditNoteFragment
import com.fredrikbogg.notesapp.data.db.entity.Note
import com.fredrikbogg.notesapp.util.ThemesUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class NotesFragment : Fragment() {

    companion object {
        const val ARGS_KEY_FOLDER_ID = "bundle_folder_id"
    }

    private val viewModel: FolderNotesViewModel by viewModels {
        FolderNotesViewModelFactory(
            requireActivity().application, arguments?.getString(ARGS_KEY_FOLDER_ID) ?: ""
        )
    }

    private lateinit var viewDataBinding: FragmentNotesBinding
    private lateinit var listAdapter: NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            FragmentNotesBinding.inflate(inflater, container, false).apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all_notes -> {
                viewModel.deleteAllNotes()
                return true
            }
            R.id.menu_theme_default -> {
                activity?.let { ThemesUtil.changeTheme(R.style.DefaultTheme, it) }
                return true
            }

            R.id.menu_theme_dark_blue -> {
                activity?.let { ThemesUtil.changeTheme(R.style.DarkBlueTheme, it) }
                return true
            }

            R.id.menu_theme_dark_red -> {
                activity?.let { ThemesUtil.changeTheme(R.style.DarkRedTheme, it) }
                return true
            }

            R.id.menu_delete_folder -> {
                viewModel.deleteFolder()
                return true
            }

            R.id.menu_rename_folder -> {
                val input = EditText(requireActivity() as Context)
                AlertDialog.Builder(requireActivity()).apply {
                    setTitle("New name:")
                    setView(input)
                    setPositiveButton("Ok") { _, _ ->
                        val textInput = input.text.toString()
                        if (textInput.isNotEmpty()) {
                            viewModel.changeFolderTitle(textInput)
                        }
                    }
                    setNegativeButton("Cancel") { _, _ -> }
                    show()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_notes_menu, menu)
        val argsFolderId: String? = arguments?.getString(ARGS_KEY_FOLDER_ID)
        if (argsFolderId != null && argsFolderId != "") {
            inflater.inflate(R.menu.fragment_notes_with_folder_menu, menu)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListAdapter()
        setupViewModelObservers()
        setupAddNoteFab()
    }

    private fun setupViewModelObservers() {
        viewModel.selectedNote.observe(
            viewLifecycleOwner,
            EventObserver { navigateToEditNote(it) })

        viewModel.notesDeletedEvent.observe(viewLifecycleOwner,
            EventObserver {
                view?.let {
                    Snackbar.make(it, "All notes deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo") { viewModel.undoDeleteNotes() }.show()
                }
            })
        viewModel.folderDeletedEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                findNavController().navigate(
                    R.id.action_notesFragment_self
                )
            })
    }

    private fun setupAddNoteFab() {
        viewDataBinding.root.findViewById<FloatingActionButton>(R.id.addNoteFab)?.let {
            it.setOnClickListener { navigateToEditNote(null) }
        }
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = NoteListAdapter(viewModel)
            viewDataBinding.notesRecyclerView.adapter = listAdapter
        } else {
            throw Exception("The viewmodel is not initialized")
        }
    }

    private fun navigateToEditNote(note: Note?) {
        val bundle = bundleOf(
            EditNoteFragment.ARGS_KEY_NOTE to note,
            EditNoteFragment.ARGS_KEY_FOLDER_ID to arguments?.getString(ARGS_KEY_FOLDER_ID)
        )
        findNavController().navigate(R.id.action_notesFragment_to_editNoteFragment, bundle)
    }
}

