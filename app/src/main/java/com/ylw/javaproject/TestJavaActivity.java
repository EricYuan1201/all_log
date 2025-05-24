package com.ylw.javaproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.ylw.javaproject.annotation.Bind;

/**
 * Java测试Activity
 * 用于测试字节码插桩在Java代码中的效果
 */
public class TestJavaActivity extends Activity {

    @Bind(id = "J001")
    Button javaButton1;
    
    @Bind(id = "J002") 
    Button javaButton2;
    
    @Bind(id = "J003")
    Button javaButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_java);
        
        initViews();
        setupClickListeners();
        
        // 调用生成的绑定器
        TestJavaActivityActionBinder.bind(this);
    }

    private void initViews() {
        javaButton1 = findViewById(R.id.java_button1);
        javaButton2 = findViewById(R.id.java_button2);
        javaButton3 = findViewById(R.id.java_button3);
    }

    /**
     * 测试不同类型的点击监听器设置方式
     */
    private void setupClickListeners() {
        // 测试1: 匿名内部类方式
        javaButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag(R.id.action_log_tag);
                Toast.makeText(TestJavaActivity.this, 
                    "Java匿名内部类点击 --> " + tag, Toast.LENGTH_SHORT).show();
                testMethod1();
            }
        });

        // 测试2: 方法引用方式
        javaButton2.setOnClickListener(this::onButton2Click);

        // 测试3: lambda表达式方式 (需要API 24+)
        javaButton3.setOnClickListener(v -> {
            Object tag = v.getTag(R.id.action_log_tag);
            Toast.makeText(this, "Java Lambda点击 --> " + tag, Toast.LENGTH_SHORT).show();
            testMethod3();
        });
    }

    /**
     * 按钮2的点击处理方法
     */
    private void onButton2Click(View v) {
        Object tag = v.getTag(R.id.action_log_tag);
        Toast.makeText(this, "Java方法引用点击 --> " + tag, Toast.LENGTH_SHORT).show();
        testMethod2();
    }

    /**
     * 测试方法1 - 用于验证插桩后的调用链
     */
    private void testMethod1() {
        System.out.println("TestJavaActivity: testMethod1 被调用");
    }

    /**
     * 测试方法2 - 用于验证插桩后的调用链
     */
    private void testMethod2() {
        System.out.println("TestJavaActivity: testMethod2 被调用");
    }

    /**
     * 测试方法3 - 用于验证插桩后的调用链
     */
    private void testMethod3() {
        System.out.println("TestJavaActivity: testMethod3 被调用");
    }

    /**
     * 测试动态设置点击监听器
     */
    public void testDynamicClickListener() {
        // 动态创建按钮并设置监听器
        Button dynamicButton = new Button(this);
        dynamicButton.setText("动态按钮");
        
        // 这个调用也应该被插桩拦截
        dynamicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestJavaActivity.this, 
                    "动态按钮点击", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 测试动态设置
        testDynamicClickListener();
    }
}
