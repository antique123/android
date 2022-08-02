package com.epoxy.gridlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class TestActivity : AppCompatActivity() {

    private val textView: TextView by lazy {
        findViewById(R.id.textView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        textView.text = intent.getStringExtra("key")
    }
}