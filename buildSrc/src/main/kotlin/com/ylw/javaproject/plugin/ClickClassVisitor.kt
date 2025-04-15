package com.ylw.javaproject.plugin

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * 点击事件类访问者
 * 用于访问和修改类文件中的方法
 */
class ClickClassVisitor(classVisitor: ClassVisitor) : ClassVisitor(Opcodes.ASM9, classVisitor) {

    private var className: String = ""
    
    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        this.className = name
        super.visit(version, access, name, signature, superName, interfaces)
    }
    
    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        
        // 跳过接口和抽象方法
        if (access and Opcodes.ACC_ABSTRACT != 0 || access and Opcodes.ACC_INTERFACE != 0) {
            return methodVisitor
        }
        
        // 创建方法访问者来修改setOnClickListener调用
        return ClickMethodVisitor(methodVisitor, access, className, name, descriptor)
    }
}
