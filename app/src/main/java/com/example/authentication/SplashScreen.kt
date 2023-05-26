package com.example.authentication

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.authentication.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding : ActivitySplashScreenBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this).load(R.drawable.bird).into(binding.birdGif)
        Glide.with(this).load(R.drawable.loadinggif).into(binding.loadingGif)
        delayTimer()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun delayTimer() {
        Handler(Looper.getMainLooper()).postDelayed({
            goToNext()
        },3000)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun goToNext() {
        val result = Helper().isOnline(this)
        if (result) {
            startActivity(Intent(this, AuthenticaitonActivity::class.java))
            finish()
        }
        else{
            val intent = Intent(this, DemoActivity::class.java)
            intent.putExtra("code", "n")
            startActivity(intent)
            finish()
        }
    }
}