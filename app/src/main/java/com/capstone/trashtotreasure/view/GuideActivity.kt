package com.capstone.trashtotreasure.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.trashtotreasure.databinding.ActivityGuideBinding
import com.capstone.trashtotreasure.view.ui.login.LoginActivity

class GuideActivity : AppCompatActivity() {

    private val binding: ActivityGuideBinding by lazy {
        ActivityGuideBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}