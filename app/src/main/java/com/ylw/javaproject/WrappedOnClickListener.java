package com.ylw.javaproject;

import android.view.View;

/**
 * 点击监听器包装类
 * 用于在原始点击监听器执行前添加日志上报逻辑
 */
public class WrappedOnClickListener implements View.OnClickListener {
    private final View.OnClickListener original;

    public WrappedOnClickListener(View.OnClickListener original) {
        this.original = original;
    }

    @Override
    public void onClick(View v) {
        // 在点击时先上报日志
        ActionLog.send(v.getTag(R.id.action_log_tag), "click");
        // 再调用原有的 onClick 逻辑
        if (original != null) {
            original.onClick(v);
        }
    }
}
