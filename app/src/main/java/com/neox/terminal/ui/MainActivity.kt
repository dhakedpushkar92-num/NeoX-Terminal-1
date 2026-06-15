package com.neox.terminal.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.neox.terminal.R
import com.neox.terminal.ui.terminal.TerminalFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TerminalFragment())
                .commit()
        }
    }
}
