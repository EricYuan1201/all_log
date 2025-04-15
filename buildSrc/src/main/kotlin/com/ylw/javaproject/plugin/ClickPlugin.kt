package com.ylw.javaproject.plugin

import com.android.build.api.transform.Transform
import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 点击事件插桩插件
 * 用于拦截所有的setOnClickListener调用并替换为WrappedOnClickListener
 */
class ClickPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("ClickPlugin: 开始应用插件")
        
        // 注册Transform
        val appExtension = project.extensions.getByType(AppExtension::class.java)
        val transform = ClickTransform()
        appExtension.registerTransform(transform)
        
        println("ClickPlugin: 插件应用完成")
    }
}
