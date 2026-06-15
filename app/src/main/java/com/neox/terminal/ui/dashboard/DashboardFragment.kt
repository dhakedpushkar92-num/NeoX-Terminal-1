package com.neox.terminal.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.neox.terminal.R
import com.neox.terminal.databinding.FragmentDashboardBinding
import com.neox.terminal.ui.ai.AiFragment
import com.neox.terminal.ui.packages.PackageFragment
import com.neox.terminal.ui.settings.SettingsFragment
import com.neox.terminal.ui.terminal.TerminalFragment
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {
    private var _b: FragmentDashboardBinding? = null
    private val b get() = _b!!
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentDashboardBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v, s)
        try {
            val h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            b.greetingText.text = when {
                h < 12 -> "Good morning, operator"
                h < 17 -> "Good afternoon, operator"
                else   -> "Good evening, operator"
            }
            b.dateText.text = SimpleDateFormat("EEE, MMM d yyyy", Locale.getDefault()).format(Date())

            val clock = object : Runnable {
                override fun run() {
                    if (_b != null) {
                        b.clockText.text = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                        handler.postDelayed(this, 1000)
                    }
                }
            }
            handler.post(clock)

            b.btnNewTerminal.setOnClickListener { navigate(TerminalFragment()) }
            b.btnPackages.setOnClickListener   { navigate(PackageFragment()) }
            b.btnAi.setOnClickListener         { navigate(AiFragment()) }
            b.btnSettings.setOnClickListener   { navigate(SettingsFragment()) }

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Dashboard error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun navigate(fragment: Fragment) {
        try {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Nav error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        _b = null
    }
}
