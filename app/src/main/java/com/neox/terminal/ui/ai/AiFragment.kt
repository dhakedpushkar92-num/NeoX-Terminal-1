package com.neox.terminal.ui.ai
import android.os.Bundle; import android.view.*; import android.widget.*
import androidx.fragment.app.Fragment; import androidx.lifecycle.lifecycleScope
import com.neox.terminal.databinding.FragmentAiBinding
import kotlinx.coroutines.*
import org.json.JSONArray; import org.json.JSONObject
import java.net.HttpURLConnection; import java.net.URL
class AiFragment : Fragment() {
    private var _b: FragmentAiBinding? = null
    private val b get() = _b!!
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentAiBinding.inflate(i, c, false); return b.root }
    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v, s)
        addMsg("🤖 Hi! I'm NeoX AI. Ask me anything about terminal commands!", false)
        b.btnSend.setOnClickListener { send() }
    }
    private fun send() {
        val txt = b.inputField.text.toString().trim()
        if(txt.isBlank()) return
        b.inputField.text?.clear()
        addMsg(txt, true)
        b.loadingIndicator.visibility = View.VISIBLE
        lifecycleScope.launch {
            val r = withContext(Dispatchers.IO) { callApi(txt) }
            b.loadingIndicator.visibility = View.GONE
            addMsg(r, false)
        }
    }
    private fun addMsg(msg: String, isUser: Boolean) {
        val tv = TextView(requireContext()).apply {
            text = if(isUser) "You: $msg" else "AI: $msg"
            setTextColor(if(isUser) 0xFF00CFFF.toInt() else 0xFF00FF88.toInt())
            textSize = 13f; setPadding(8,8,8,8)
        }
        b.chatContainer.addView(tv)
        b.chatScroll.post { b.chatScroll.fullScroll(View.FOCUS_DOWN) }
    }
    private fun callApi(q: String): String = try {
        val url = URL("https://api.anthropic.com/v1/messages")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type","application/json")
        conn.setRequestProperty("anthropic-version","2023-06-01")
        conn.doOutput = true; conn.connectTimeout = 15000
        val body = JSONObject().put("model","claude-sonnet-4-6").put("max_tokens",500)
            .put("system","You are NeoX AI terminal assistant. Be concise.")
            .put("messages", JSONArray().put(JSONObject().put("role","user").put("content",q))).toString()
        conn.outputStream.write(body.toByteArray())
        if(conn.responseCode == 200)
            JSONObject(conn.inputStream.bufferedReader().readText()).getJSONArray("content").getJSONObject(0).getString("text")
        else "⚠️ Add API key in Settings to use AI."
    } catch(e: Exception) { "⚠️ ${e.message}" }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
