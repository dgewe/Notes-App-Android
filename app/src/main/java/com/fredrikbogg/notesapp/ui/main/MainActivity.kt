package com.fredrikbogg.notesapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.fredrikbogg.notesapp.util.EventObserver
import com.fredrikbogg.notesapp.R
import com.fredrikbogg.notesapp.databinding.ActivityMainBinding
import com.fredrikbogg.notesapp.ui.edit_note.EditNoteFragment
import com.fredrikbogg.notesapp.ui.notes.NotesFragment
import com.fredrikbogg.notesapp.util.ThemesUtil
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val viewModel by viewModels<MainFoldersViewModel>()
    private lateinit var viewDataBinding: ActivityMainBinding
    private lateinit var listAdapter: MainFoldersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(ThemesUtil.getCurrentTheme(this))
        viewDataBinding =
            ActivityMainBinding.inflate(layoutInflater).apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this
        setContentView(viewDataBinding.root)


        navController = findNavController(R.id.nav_host_fragment)
        drawerLayout = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        navigation_view.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

        when (intent?.action) {
            Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    handleSendText(intent)
                }
            }
        }
        setupListAdapter()
        setupViewModelObservers()
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = MainFoldersAdapter(viewModel)
            viewDataBinding.foldersRecyclerView.adapter = listAdapter
        } else {
            throw Exception("The viewmodel is not initialized")
        }
    }

    private fun setupViewModelObservers() {
        viewModel.selectedFolderId.observe(this,
            EventObserver {
                val bundle = bundleOf(NotesFragment.ARGS_KEY_FOLDER_ID to it)
                if (navController.currentDestination!!.id == R.id.editNoteFragment) {
                    navController.navigate(R.id.action_editNoteFragment_to_notesFragment, bundle)
                } else {
                    navController.navigate(R.id.action_notesFragment_self, bundle)
                }
                drawerLayout.close()
            })
    }

    private fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            val bundle = bundleOf(EditNoteFragment.ARGS_KEY_TEXT to it)
            navController.navigate(
                R.id.action_notesFragment_to_editNoteFragment,
                bundle
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}