package com.ylw.javaproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.ylw.javaproject.annotation.Bind;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Java Lambda表达式测试Activity
 * 专门用于测试各种Java 8+ lambda表达式的字节码插桩效果
 * 需要API 24+ 或者启用desugaring
 */
public class TestLambdaActivity extends Activity {

    @Bind(id = "L001")
    Button lambdaButton1;
    
    @Bind(id = "L002")
    Button lambdaButton2;
    
    @Bind(id = "L003")
    Button lambdaButton3;
    
    @Bind(id = "L004")
    Button lambdaButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_lambda);
        
        initViews();
        setupLambdaClickListeners();
        
        // 调用生成的绑定器
        TestLambdaActivityActionBinder.bind(this);
    }

    private void initViews() {
        lambdaButton1 = findViewById(R.id.lambda_button1);
        lambdaButton2 = findViewById(R.id.lambda_button2);
        lambdaButton3 = findViewById(R.id.lambda_button3);
        lambdaButton4 = findViewById(R.id.lambda_button4);
    }

    /**
     * 测试各种Java lambda表达式的插桩效果
     */
    private void setupLambdaClickListeners() {
        // 测试1: 简单lambda表达式
        lambdaButton1.setOnClickListener(v -> {
            Object tag = v.getTag(R.id.action_log_tag);
            Toast.makeText(this, "简单Lambda点击 --> " + tag, Toast.LENGTH_SHORT).show();
            testLambdaMethod1();
        });

        // 测试2: 单行lambda表达式
        lambdaButton2.setOnClickListener(v -> handleSingleLineLambda(v));

        // 测试3: 复杂lambda表达式
        lambdaButton3.setOnClickListener(v -> {
            Object tag = v.getTag(R.id.action_log_tag);
            Toast.makeText(this, "复杂Lambda点击 --> " + tag, Toast.LENGTH_SHORT).show();
            
            // 嵌套lambda测试
            List<String> testList = Arrays.asList("item1", "item2", "item3");
            testList.forEach(item -> {
                System.out.println("处理项目: " + item);
            });
            
            testLambdaMethod3();
        });

        // 测试4: 方法引用
        lambdaButton4.setOnClickListener(this::handleMethodReference);
    }

    /**
     * 单行lambda处理方法
     */
    private void handleSingleLineLambda(View v) {
        Object tag = v.getTag(R.id.action_log_tag);
        Toast.makeText(this, "单行Lambda点击 --> " + tag, Toast.LENGTH_SHORT).show();
        testLambdaMethod2();
    }

    /**
     * 方法引用处理方法
     */
    private void handleMethodReference(View v) {
        Object tag = v.getTag(R.id.action_log_tag);
        Toast.makeText(this, "方法引用点击 --> " + tag, Toast.LENGTH_SHORT).show();
        testLambdaMethod4();
    }

    /**
     * 测试动态lambda设置
     */
    private void testDynamicLambdaSetup() {
        List<Button> buttons = Arrays.asList(lambdaButton1, lambdaButton2, lambdaButton3, lambdaButton4);
        
        // 使用forEach和lambda动态设置监听器
        buttons.forEach(button -> {
            // 这个setOnClickListener调用也应该被插桩
            button.setOnClickListener(view -> {
                Object tag = view.getTag(R.id.action_log_tag);
                Toast.makeText(this, "动态Lambda点击 --> " + tag, Toast.LENGTH_SHORT).show();
            });
        });
    }

    /**
     * 测试高阶函数中的lambda
     */
    private void testHighOrderLambda() {
        setupButtonWithLambda(lambdaButton1, v -> {
            Object tag = v.getTag(R.id.action_log_tag);
            Toast.makeText(this, "高阶Lambda点击 --> " + tag, Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * 接受lambda参数的高阶函数
     */
    private void setupButtonWithLambda(Button button, View.OnClickListener listener) {
        // 这个setOnClickListener调用也应该被插桩
        button.setOnClickListener(listener);
    }

    /**
     * 测试Consumer接口的lambda
     */
    private void testConsumerLambda() {
        Consumer<Button> buttonConsumer = button -> {
            // 这种间接的setOnClickListener调用
            button.setOnClickListener(v -> {
                Toast.makeText(this, "Consumer Lambda点击", Toast.LENGTH_SHORT).show();
            });
        };
        
        buttonConsumer.accept(lambdaButton4);
    }

    /**
     * 测试方法1
     */
    private void testLambdaMethod1() {
        System.out.println("TestLambdaActivity: testLambdaMethod1 被调用");
    }

    /**
     * 测试方法2
     */
    private void testLambdaMethod2() {
        System.out.println("TestLambdaActivity: testLambdaMethod2 被调用");
    }

    /**
     * 测试方法3
     */
    private void testLambdaMethod3() {
        System.out.println("TestLambdaActivity: testLambdaMethod3 被调用");
    }

    /**
     * 测试方法4
     */
    private void testLambdaMethod4() {
        System.out.println("TestLambdaActivity: testLambdaMethod4 被调用");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 测试各种复杂场景
        testDynamicLambdaSetup();
        testHighOrderLambda();
        testConsumerLambda();
    }
}
