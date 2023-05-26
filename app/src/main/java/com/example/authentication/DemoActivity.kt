package com.example.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.authentication.databinding.ActivityDemoBinding

class DemoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        delayTimer()
    }
    private fun delayTimer() {
        Handler(Looper.getMainLooper()).postDelayed({
            goToNext()
        },400)
    }

    private fun goToNext() {
        val code = intent.getStringExtra("code")
        if(code == "n"){
            binding.netConnection.visibility = View.VISIBLE
            binding.demoLayout.visibility = View.GONE
            binding.reloadButton.setOnClickListener{
                startActivity(Intent(this, SplashScreen::class.java))
                finish()
            }
        }
        else{
            binding.netConnection.visibility = View.GONE
            binding.demoLayout.visibility = View.VISIBLE
            startActivity(Intent(this, AuthenticaitonActivity::class.java))
            finish()
        }

    }
}