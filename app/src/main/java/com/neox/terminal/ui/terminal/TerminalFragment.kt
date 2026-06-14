package com.neox.terminal.ui.terminal
import android.os.Bundle; import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment; import androidx.lifecycle.lifecycleScope
import com.neox.terminal.databinding.FragmentTerminalBinding
import com.neox.terminal.engine.TerminalEngine
import kotlinx.coroutines.launch
class TerminalFragment : Fragment() {
    private var _b: FragmentTerminalBinding? = null
    private val b get() = _b!!
    private lateinit var engine: TerminalEngine
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentTerminalBinding.inflate(i, c, false); return b.root }
    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v, s)
        engine = TerminalEngine(lifecycleScope)
        engine.start()
        lifecycleScope.launch { engine.output.collect { t ->
            b.terminalOutput.append(t)
            b.terminalScroll.post { b.terminalScroll.fullScroll(View.FOCUS_DOWN) } } }
        b.btnSend.setOnClickListener { sendCmd() }
        b.commandInput.setOnEditorActionListener { _, a, _ ->
            if(a == EditorInfo.IME_ACTION_SEND) { sendCmd(); true } else false }
        b.btnCtrlC.setOnClickListener { engine.interrupt() }
        b.btnTab.setOnClickListener { engine.sendRaw("\t") }
        b.btnClear.setOnClickListener { b.terminalOutput.text = ""; engine.clear() }
        b.btnHistoryUp.setOnClickListener { b.commandInput.setText(engine.histUp()) }
        b.btnHistoryDown.setOnClickListener { b.commandInput.setText(engine.histDown()) }
    }
    private fun sendCmd() {
        val cmd = b.commandInput.text.toString().trim()
        if(cmd.isNotBlank()) { engine.send(cmd); b.commandInput.text?.clear() } }
    override fun onDestroyView() { super.onDestroyView(); engine.stop(); _b = null }
}
