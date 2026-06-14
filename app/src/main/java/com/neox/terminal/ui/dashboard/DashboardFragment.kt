package com.neox.terminal.ui.dashboard
import android.os.Bundle; import android.os.Handler; import android.os.Looper
import android.view.*; import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.neox.terminal.R
import com.neox.terminal.databinding.FragmentDashboardBinding
import java.text.SimpleDateFormat; import java.util.*
class DashboardFragment : Fragment() {
    private var _b: FragmentDashboardBinding? = null
    private val b get() = _b!!
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentDashboardBinding.inflate(i, c, false); return b.root }
    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v, s)
        val h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        b.greetingText.text = "${if(h<12)"Good morning" else if(h<17)"Good afternoon" else "Good evening"}, operator"
        b.dateText.text = SimpleDateFormat("EEE, MMM d yyyy", Locale.getDefault()).format(Date())
        val handler = Handler(Looper.getMainLooper())
        val clock = object: Runnable { override fun run() {
            b.clockText.text = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            handler.postDelayed(this, 1000) } }
        handler.post(clock)
        b.btnNewTerminal.setOnClickListener { findNavController().navigate(R.id.terminalFragment) }
        b.btnPackages.setOnClickListener { findNavController().navigate(R.id.packageFragment) }
        b.btnAi.setOnClickListener { findNavController().navigate(R.id.aiFragment) }
        b.btnSettings.setOnClickListener { findNavController().navigate(R.id.settingsFragment) }
    }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
