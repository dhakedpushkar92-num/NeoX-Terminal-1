package com.neox.terminal.ui.settings
import android.os.Bundle; import android.view.*; import androidx.fragment.app.Fragment
import com.neox.terminal.databinding.FragmentSettingsFragmentBinding
class SettingsFragment : Fragment() {
    private var _b: FragmentSettingsFragmentBinding? = null; private val b get() = _b!!
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentSettingsFragmentBinding.inflate(i,c,false); return b.root }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
