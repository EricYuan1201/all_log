package com.ylw.javaproject

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.ylw.javaproject.annotation.Bind

/**
 *  author : liwen15
 *  date : 2022/6/16
 *  description :
 */
class MainActivity : Activity() {

    var name : String? = "123"

    @Bind(id = "O123")
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)
        button.setOnClickListener {
            val tag = button.getTag(R.id.action_log_tag)
            Toast.makeText(this, "点击了button --> $tag", Toast.LENGTH_SHORT).show()
        }
        // 调用生成的绑定器
        MainActivityActionBinder.bind(this)
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