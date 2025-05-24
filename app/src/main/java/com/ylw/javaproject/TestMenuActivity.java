package com.ylw.javaproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 测试菜单Activity
 * 提供各种测试页面的入口
 */
public class TestMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 动态创建布局
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);
        
        // 标题
        TextView title = new TextView(this);
        title.setText("字节码插桩测试菜单");
        title.setTextSize(20);
        title.setPadding(0, 0, 0, 50);
        layout.addView(title);
        
        // 原始MainActivity按钮
        Button mainActivityBtn = new Button(this);
        mainActivityBtn.setText("原始MainActivity (Kotlin)");
        mainActivityBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });
        layout.addView(mainActivityBtn);
        
        // Java测试按钮
        Button javaTestBtn = new Button(this);
        javaTestBtn.setText("Java测试页面");
        javaTestBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, TestJavaActivity.class));
        });
        layout.addView(javaTestBtn);
        
        // Kotlin测试按钮
        Button kotlinTestBtn = new Button(this);
        kotlinTestBtn.setText("Kotlin测试页面");
        kotlinTestBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, TestKotlinActivity.class));
        });
        layout.addView(kotlinTestBtn);
        
        // Lambda测试按钮
        Button lambdaTestBtn = new Button(this);
        lambdaTestBtn.setText("Java Lambda测试页面");
        lambdaTestBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, TestLambdaActivity.class));
        });
        layout.addView(lambdaTestBtn);
        
        // 说明文本
        TextView description = new TextView(this);
        description.setText("\n测试说明：\n" +
                "1. 每个测试页面都包含不同类型的点击监听器\n" +
                "2. 点击按钮后观察Toast消息和Logcat输出\n" +
                "3. 查看ActionLog是否正确输出，验证插桩效果\n" +
                "4. 注意观察不同语法形式的hook效果");
        description.setTextSize(14);
        description.setPadding(0, 30, 0, 0);
        layout.addView(description);
        
        setContentView(layout);
    }
}
