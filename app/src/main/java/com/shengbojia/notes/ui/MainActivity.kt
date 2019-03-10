package com.shengbojia.notes.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.shengbojia.notes.R
import com.shengbojia.notes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.nav_host_frag)
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        // Set up ActionBar
        setSupportActionBar(binding.toolbarMain)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    /*

    private fun dialogConfirmation() {
        val builder = AlertDialog.Builder(this, R.style.ConfirmationDialogStyle)
        builder.apply {
            setTitle(getString(R.string.dialog_confirmationTitle))
            setPositiveButton(getString(R.string.dialog_positive)) { _, _ ->
                noteViewModel.deleteAllNotes()
            }
            setNegativeButton(getString(R.string.dialog_cancel)) { _, _ ->
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun onFabClick(view: View) {
        val addIntent = Intent(this, AddEditNoteActivity::class.java)
        startActivity(addIntent)
    }

    */

    companion object {
        private const val TAG = "ActMain"
    }
}
