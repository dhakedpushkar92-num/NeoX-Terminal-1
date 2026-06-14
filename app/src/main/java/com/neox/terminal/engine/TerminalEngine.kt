package com.neox.terminal.engine
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.*

class TerminalEngine(private val scope: CoroutineScope) {
    private val _output = MutableStateFlow("")
    val output: StateFlow<String> = _output
    private val _running = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _running
    private val history = mutableListOf<String>()
    private var histIdx = -1
    private var process: Process? = null
    private var writer: PrintWriter? = null

    fun start() {
        scope.launch(Dispatchers.IO) {
            try {
                val pb = ProcessBuilder("/system/bin/sh").apply {
                    environment()["TERM"] = "xterm-256color"
                    environment()["HOME"] = "/data/data/com.neox.terminal/files"
                    redirectErrorStream(true)
                }
                process = pb.start()
                writer = PrintWriter(OutputStreamWriter(process!!.outputStream), true)
                _running.value = true
                emit(banner())
                val buf = CharArray(4096)
                val reader = BufferedReader(InputStreamReader(process!!.inputStream))
                while (_running.value) {
                    val n = reader.read(buf)
                    if (n == -1) break
                    emit(String(buf, 0, n))
                }
            } catch (e: Exception) {
                _running.value = false
                emit("\r\n[NeoX] Running in demo mode\r\n")
            }
        }
    }

    fun send(cmd: String) {
        if (cmd.isBlank()) return
        history.add(cmd); histIdx = history.size
        scope.launch(Dispatchers.IO) {
            writer?.println(cmd) ?: demo(cmd)
        }
    }

    fun sendRaw(s: String) { scope.launch(Dispatchers.IO) { writer?.print(s); writer?.flush() } }
    fun interrupt() { sendRaw("\u0003") }
    fun clear() { sendRaw("\u000C") }
    fun histUp() = history.getOrElse((--histIdx).coerceAtLeast(0)) { "" }
    fun histDown() = history.getOrElse((++histIdx).coerceAtMost(history.size)) { "" }
    fun stop() { writer?.close(); process?.destroy(); _running.value = false }

    private fun emit(t: String) { scope.launch(Dispatchers.Main) { _output.value = t } }

    private fun demo(cmd: String) {
        val r = when {
            cmd == "help" -> "Commands: ls, pwd, whoami, echo, clear, neofetch, pkg list"
            cmd == "whoami" -> "neox"
            cmd == "pwd" -> "/data/data/com.neox.terminal/files/home"
            cmd.startsWith("ls") -> "Documents  Downloads  projects  README.md"
            cmd.startsWith("echo") -> cmd.removePrefix("echo").trim()
            cmd == "neofetch" -> "NeoX Terminal v1.0 | Android | ARM64"
            cmd.startsWith("pkg") -> "python3  nodejs  git  vim  curl  wget  openssh"
            cmd == "clear" -> "\u001b[2J\u001b[H"
            else -> "neox: $cmd: command not found"
        }
        emit("\r\n$r\r\n")
    }

    private fun banner() = "\r\n" +
        "  _   _           __  __\r\n" +
        " | \\ | | ___  ___|  \\/  |\r\n" +
        " |  \\| |/ _ \\/ _ \\ |\\/| |\r\n" +
        " | |\\  |  __/ (_) | |  | |\r\n" +
        " |_| \\_|\\___|\\___/|_|  |_|\r\n" +
        "\r\n  NeoX Terminal v1.0 — Type 'help'\r\n\r\n"
}
