package com.neox.terminal
import android.app.Application
class NeoXApp : Application() {
    override fun onCreate() { super.onCreate(); instance = this }
    companion object { lateinit var instance: NeoXApp private set }
}
