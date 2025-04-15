package com.ylw.javaproject;

import android.util.Log;

/**
 * 行为日志工具类
 * 用于记录用户行为日志
 */
public class ActionLog {
    private static final String TAG = "ActionLog";

    /**
     * 发送行为日志
     * @param tag 行为标签
     * @param action 行为类型
     */
    public static void send(Object tag, String action) {
        String tagStr = tag != null ? tag.toString() : "unknown";
        Log.d(TAG, "行为日志: " + tagStr + ", 行为: " + action);
        
    }
}
