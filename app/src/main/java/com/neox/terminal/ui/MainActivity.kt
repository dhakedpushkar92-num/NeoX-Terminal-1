package com.neox.terminal.ui
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.neox.terminal.databinding.ActivityMainBinding
import com.neox.terminal.ui.terminal.TerminalFragment
import com.neox.terminal.ui.dashboard.DashboardFragment
import com.neox.terminal.ui.ai.AiFragment
import com.neox.terminal.ui.packages.PackageFragment
import com.neox.terminal.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load dashboard by default
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, DashboardFragment())
                .commit()
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                com.neox.terminal.R.id.dashboardFragment -> DashboardFragment()
                com.neox.terminal.R.id.terminalFragment  -> TerminalFragment()
                com.neox.terminal.R.id.packageFragment   -> PackageFragment()
                com.neox.terminal.R.id.aiFragment        -> AiFragment()
                com.neox.terminal.R.id.settingsFragment  -> SettingsFragment()
                else -> DashboardFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit()
            true
        }
    }
}
