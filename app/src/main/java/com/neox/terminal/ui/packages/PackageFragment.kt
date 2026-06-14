package com.neox.terminal.ui.packages
import android.os.Bundle; import android.view.*; import androidx.fragment.app.Fragment
import com.neox.terminal.databinding.FragmentPackageFragmentBinding
class PackageFragment : Fragment() {
    private var _b: FragmentPackageFragmentBinding? = null; private val b get() = _b!!
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentPackageFragmentBinding.inflate(i,c,false); return b.root }
    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
