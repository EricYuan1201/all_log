package com.ylw.javaproject

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.ylw.javaproject.annotation.Bind

/**
 * Kotlin测试Activity
 * 用于测试字节码插桩在Kotlin代码中的效果，包括lambda表达式
 */
class TestKotlinActivity : Activity() {

    @Bind(id = "K001")
    lateinit var kotlinButton1: Button
    
    @Bind(id = "K002")
    lateinit var kotlinButton2: Button
    
    @Bind(id = "K003")
    lateinit var kotlinButton3: Button
    
    @Bind(id = "K004")
    lateinit var kotlinButton4: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_kotlin)
        
        initViews()
        setupClickListeners()
        
        // 调用生成的绑定器
        TestKotlinActivityActionBinder.bind(this)
    }

    private fun initViews() {
        kotlinButton1 = findViewById(R.id.kotlin_button1)
        kotlinButton2 = findViewById(R.id.kotlin_button2)
        kotlinButton3 = findViewById(R.id.kotlin_button3)
        kotlinButton4 = findViewById(R.id.kotlin_button4)
    }

    /**
     * 测试不同类型的Kotlin点击监听器设置方式
     */
    private fun setupClickListeners() {
        // 测试1: Kotlin lambda表达式
        kotlinButton1.setOnClickListener { v ->
            val tag = v.getTag(R.id.action_log_tag)
            Toast.makeText(this, "Kotlin Lambda点击 --> $tag", Toast.LENGTH_SHORT).show()
            testMethod1()
        }

        // 测试2: 简化的lambda表达式
        kotlinButton2.setOnClickListener {
            val tag = it.getTag(R.id.action_log_tag)
            Toast.makeText(this, "Kotlin 简化Lambda点击 --> $tag", Toast.LENGTH_SHORT).show()
            testMethod2()
        }

        // 测试3: 方法引用
        kotlinButton3.setOnClickListener(::onButton3Click)

        // 测试4: 匿名对象方式
        kotlinButton4.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val tag = v.getTag(R.id.action_log_tag)
                Toast.makeText(this@TestKotlinActivity, 
                    "Kotlin 匿名对象点击 --> $tag", Toast.LENGTH_SHORT).show()
                testMethod4()
            }
        })
    }

    /**
     * 按钮3的点击处理方法
     */
    private fun onButton3Click(v: View) {
        val tag = v.getTag(R.id.action_log_tag)
        Toast.makeText(this, "Kotlin 方法引用点击 --> $tag", Toast.LENGTH_SHORT).show()
        testMethod3()
    }

    /**
     * 测试方法1 - 用于验证插桩后的调用链
     */
    private fun testMethod1() {
        println("TestKotlinActivity: testMethod1 被调用")
    }

    /**
     * 测试方法2 - 用于验证插桩后的调用链
     */
    private fun testMethod2() {
        println("TestKotlinActivity: testMethod2 被调用")
    }

    /**
     * 测试方法3 - 用于验证插桩后的调用链
     */
    private fun testMethod3() {
        println("TestKotlinActivity: testMethod3 被调用")
    }

    /**
     * 测试方法4 - 用于验证插桩后的调用链
     */
    private fun testMethod4() {
        println("TestKotlinActivity: testMethod4 被调用")
    }

    /**
     * 测试链式调用和复杂lambda表达式
     */
    private fun testComplexLambda() {
        val buttons = listOf(kotlinButton1, kotlinButton2, kotlinButton3, kotlinButton4)
        
        buttons.forEach { button ->
            // 这种动态设置也应该被插桩拦截
            button.setOnClickListener { view ->
                val tag = view.getTag(R.id.action_log_tag)
                Toast.makeText(this, "复杂Lambda点击 --> $tag", Toast.LENGTH_SHORT).show()
                
                // 嵌套lambda测试
                listOf("test1", "test2").forEach { testStr ->
                    println("嵌套lambda: $testStr")
                }
            }
        }
    }

    /**
     * 测试高阶函数中的setOnClickListener调用
     */
    private fun testHighOrderFunction() {
        setupButtonWithCallback(kotlinButton1) { view ->
            val tag = view.getTag(R.id.action_log_tag)
            Toast.makeText(this, "高阶函数Lambda点击 --> $tag", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 高阶函数，接受lambda作为参数
     */
    private fun setupButtonWithCallback(button: Button, callback: (View) -> Unit) {
        // 这个setOnClickListener调用也应该被插桩
        button.setOnClickListener(callback)
    }

    override fun onResume() {
        super.onResume()
        // 测试复杂场景
        testComplexLambda()
        testHighOrderFunction()
    }
}
