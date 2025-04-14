package com.ylw.javaproject

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity

/**
 *  author : liwen15
 *  date : 2022/6/16
 *  description :
 */
class MainActivity : Activity() {

    var name : String? = "123"

    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)
        button.setOnClickListener {
            Toast.makeText(this, "点击了button", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        test()
    }

    private fun test() {
        name?.let {
            print("ylw, 123")
        }?.let {
            print("ylw, 456")
        }
    }
}