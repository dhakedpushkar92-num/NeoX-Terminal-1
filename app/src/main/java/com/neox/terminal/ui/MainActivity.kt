package com.neox.terminal.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.neox.terminal.R
import com.neox.terminal.databinding.ActivityMainBinding
import com.neox.terminal.ui.ai.AiFragment
import com.neox.terminal.ui.dashboard.DashboardFragment
import com.neox.terminal.ui.packages.PackageFragment
import com.neox.terminal.ui.settings.SettingsFragment
import com.neox.terminal.ui.terminal.TerminalFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            if (savedInstanceState == null) {
                loadFragment(DashboardFragment())
            }

            binding.bottomNav.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.dashboardFragment -> loadFragment(DashboardFragment())
                    R.id.terminalFragment  -> loadFragment(TerminalFragment())
                    R.id.packageFragment   -> loadFragment(PackageFragment())
                    R.id.aiFragment        -> loadFragment(AiFragment())
                    R.id.settingsFragment  -> loadFragment(SettingsFragment())
                }
                true
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        try {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        } catch (e: Exception) {
            Toast.makeText(this, "Fragment error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
