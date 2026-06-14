package com.neox.terminal.ui.splash
import android.content.Intent; import android.os.Bundle; import android.os.Handler; import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.neox.terminal.databinding.ActivitySplashBinding
import com.neox.terminal.ui.MainActivity
class SplashActivity : AppCompatActivity() {
    private lateinit var b: ActivitySplashBinding
    private val lines = listOf("[BOOT] NeoX v1.0 loading...","[BOOT] Terminal ready...","[BOOT] AI loaded...","[BOOT] Welcome, operator!")
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        b = ActivitySplashBinding.inflate(layoutInflater); setContentView(b.root)
        lines.forEachIndexed { i, l ->
            Handler(Looper.getMainLooper()).postDelayed({
                b.bootLog.append(if(i==0) l else "\n$l")
                b.progressBar.progress = (i+1)*100/lines.size
            }, i*400L)
        }
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java)); finish()
        }, lines.size*400L+500L)
    }
}
