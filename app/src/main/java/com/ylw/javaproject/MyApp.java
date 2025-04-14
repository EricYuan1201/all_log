package com.ylw.javaproject;

import android.app.Application;
import android.os.Looper;

/**
 * author : liwen15
 * date : 2025/3/9
 * description :
 */
public class MyApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    private void dumpStackTrace() {
        StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
        StringBuilder stackInfo = new StringBuilder();
        for (StackTraceElement element : stackTrace) {
            stackInfo.append(element.toString()).append("\n"); // 堆栈信息拼接
        }
        // 存入本地文件，核心实现是写入日志文件
        // writeToFile(stackInfo.toString());
    }

}
