package com.fredrikbogg.notesapp.ui.edit_note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fredrikbogg.notesapp.util.EventObserver
import com.fredrikbogg.notesapp.R
import com.fredrikbogg.notesapp.databinding.FragmentEditNoteBinding
import com.google.android.material.snackbar.Snackbar

class EditNoteFragment : Fragment() {

    private val viewModel by viewModels<EditNoteViewModel>()
    private lateinit var viewDataBinding: FragmentEditNoteBinding

    companion object {
        const val ARGS_KEY_NOTE = "bundle_note"
        const val ARGS_KEY_TEXT = "bundle_text"
        const val ARGS_KEY_FOLDER_ID = "bundle_folder_id"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentEditNoteBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_share -> {
                shareNote()
                return true
            }
            R.id.menu_delete -> {
                viewModel.deleteNote()
                return true
            }
            android.R.id.home -> {
                viewModel.saveNote()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_edit_note_menu, menu)
    }

    private fun shareNote() {
        viewModel.note.value?.let {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, it.title + "\n" + it.description)
            }
            startActivity(Intent.createChooser(intent, null))
        }
    }

    private fun hideKeyboard() {
        val inputManager: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputManager.hideSoftInputFromWindow(
            requireActivity().currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun setupViewModel() {
        viewModel.start(
            arguments?.getParcelable(ARGS_KEY_NOTE),
            arguments?.getString(ARGS_KEY_TEXT),
            arguments?.getString(ARGS_KEY_FOLDER_ID)
        )
        viewModel.noteUpdatedEvent.observe(viewLifecycleOwner,
            EventObserver { findNavController().popBackStack() })
        viewModel.noteEmptyEvent.observe(viewLifecycleOwner,
            EventObserver {
                view?.let { Snackbar.make(it, "Invalid title", Snackbar.LENGTH_SHORT).show() }
            })
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }
}