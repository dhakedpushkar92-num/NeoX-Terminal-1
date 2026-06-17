package com.neox.terminal.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.PrintWriter
import java.io.StringWriter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            buildUI()
        } catch (e: Throwable) {
            showCrash(e)
        }
    }

    private fun showCrash(e: Throwable) {
        val sw = StringWriter()
        e.printStackTrace(PrintWriter(sw))
        val tv = TextView(this).apply {
            text = "CRASH DETECTED:\n\n${e.message}\n\n${sw}"
            setTextColor(Color.RED)
            textSize = 11f
            setPadding(24, 24, 24, 24)
            typeface = Typeface.MONOSPACE
        }
        setContentView(ScrollView(this).apply { addView(tv) })
    }

    private fun buildUI() {
        val outputBuilder = StringBuilder()

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#0A0A0F"))
        }

        val output = TextView(this).apply {
            setTextColor(Color.parseColor("#00FF88"))
            textSize = 13f
            typeface = Typeface.MONOSPACE
            setPadding(16, 16, 16, 16)
        }

        val scroll = ScrollView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)
            addView(output)
        }

        val inputRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setBackgroundColor(Color.parseColor("#111118"))
            setPadding(8, 8, 8, 8)
        }

        val prompt = TextView(this).apply {
            text = "$ "
            setTextColor(Color.parseColor("#FF00FF"))
            textSize = 14f
            typeface = Typeface.MONOSPACE
        }

        val input = EditText(this).apply {
            layoutParams = LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            setTextColor(Color.WHITE)
            setHintTextColor(Color.GRAY)
            hint = "Enter command..."
            background = null
            textSize = 13f
            typeface = Typeface.MONOSPACE
            imeOptions = EditorInfo.IME_ACTION_SEND
        }

        val btn = Button(this).apply {
            text = "RUN"
            setTextColor(Color.parseColor("#00FF88"))
            setBackgroundColor(Color.TRANSPARENT)
        }

        fun appendOutput(text: String) {
            outputBuilder.append(text)
            output.text = outputBuilder.toString()
        }

        fun processCommand(cmd: String): String = when {
            cmd == "help" -> "Commands: help, ls, pwd, whoami, clear, neofetch, echo"
            cmd == "whoami" -> "neox"
            cmd == "pwd" -> "/data/data/com.neox.terminal"
            cmd.startsWith("ls") -> "Documents  Downloads  projects"
            cmd.startsWith("echo") -> cmd.removePrefix("echo").trim()
            cmd == "neofetch" -> "NeoX Terminal v1.0 | Android"
            cmd == "clear" -> { outputBuilder.clear(); "" }
            else -> "neox: $cmd: command not found"
        }

        val runCmd = {
            val cmd = input.text.toString().trim()
            if (cmd.isNotBlank()) {
                appendOutput("$ $cmd\n")
                appendOutput(processCommand(cmd) + "\n\n")
                input.text.clear()
                scroll.post { scroll.fullScroll(ScrollView.FOCUS_DOWN) }
            }
        }

        btn.setOnClickListener { runCmd() }
        input.setOnEditorActionListener { _, _, _ -> runCmd(); true }

        inputRow.addView(prompt)
        inputRow.addView(input)
        inputRow.addView(btn)
        layout.addView(scroll)
        layout.addView(inputRow)
        setContentView(layout)

        appendOutput("NeoX Terminal v1.0\nType 'help' for commands\n\n")
    }
}
