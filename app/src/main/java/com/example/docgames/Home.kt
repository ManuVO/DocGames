package com.example.docgames

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val intent : Intent = intent
        val email = intent.getStringExtra("email")
        println(email)
    }
}