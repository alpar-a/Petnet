package com.example.petnet

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NextScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = TextView(this).apply {
            text = "This is the next screen"
            textSize = 20f
        }

        setContentView(textView)
    }
}
