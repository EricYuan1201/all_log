package com.ylw.javaproject.plugin

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter

/**
 * 点击事件方法访问者
 * 用于拦截和修改setOnClickListener方法调用
 */
class ClickMethodVisitor(
    methodVisitor: MethodVisitor,
    access: Int,
    private val className: String,
    private val methodName: String,
    descriptor: String?
) : AdviceAdapter(Opcodes.ASM9, methodVisitor, access, methodName, descriptor) {

    // 要拦截的方法名和描述符
    private val TARGET_METHOD = "setOnClickListener"
    private val TARGET_DESC = "(Landroid/view/View\$OnClickListener;)V"
    
    override fun visitMethodInsn(
        opcode: Int,
        owner: String,
        name: String,
        descriptor: String,
        isInterface: Boolean
    ) {
        // 检查是否是调用setOnClickListener方法
        // 只处理com/ylw/javaproject包内的类
        if (className.startsWith("com/ylw/javaproject") &&
            opcode == Opcodes.INVOKEVIRTUAL && 
            name == TARGET_METHOD && 
            descriptor == TARGET_DESC &&
            (owner.startsWith("android/widget/") || owner.startsWith("android/view/"))) {
            
            println("ClickMethodVisitor: 拦截到setOnClickListener调用，位置: $className.$methodName")
            
            // 插入代码，将原始的OnClickListener包装为WrappedOnClickListener
            // 原始代码: view.setOnClickListener(listener)
            // 修改后: view.setOnClickListener(new WrappedOnClickListener(listener))
            
            // 创建WrappedOnClickListener实例
            mv.visitTypeInsn(Opcodes.NEW, "com/ylw/javaproject/WrappedOnClickListener")
            mv.visitInsn(Opcodes.DUP_X1)
            mv.visitInsn(Opcodes.SWAP)
            mv.visitMethodInsn(
                Opcodes.INVOKESPECIAL,
                "com/ylw/javaproject/WrappedOnClickListener",
                "<init>",
                "(Landroid/view/View\$OnClickListener;)V",
                false
            )
            
            // 调用原始方法
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
        } else {
            // 不是目标方法，正常调用
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
        }
    }
}
