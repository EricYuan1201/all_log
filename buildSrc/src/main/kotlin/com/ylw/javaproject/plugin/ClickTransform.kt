package com.ylw.javaproject.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

/**
 * 点击事件转换器
 * 负责处理字节码转换逻辑
 */
class ClickTransform : Transform() {

    override fun getName(): String = "ClickTransform"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> = 
        TransformManager.CONTENT_CLASS

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> = 
        TransformManager.SCOPE_FULL_PROJECT

    override fun isIncremental(): Boolean = false

    override fun transform(transformInvocation: TransformInvocation) {
        println("ClickTransform: 开始转换")
        
        // 如果非增量构建，则清空输出目录
        if (!isIncremental) {
            transformInvocation.outputProvider.deleteAll()
        }

        // 遍历所有输入
        transformInvocation.inputs.forEach { input ->
            // 处理jar文件
            input.jarInputs.forEach { jarInput ->
                processJarInput(jarInput, transformInvocation.outputProvider)
            }
            
            // 处理目录文件
            input.directoryInputs.forEach { directoryInput ->
                processDirectoryInput(directoryInput, transformInvocation.outputProvider)
            }
        }
        
        println("ClickTransform: 转换完成")
    }
    
    /**
     * 处理Jar输入
     */
    private fun processJarInput(jarInput: JarInput, outputProvider: TransformOutputProvider) {
        val dest = outputProvider.getContentLocation(
            jarInput.name,
            jarInput.contentTypes,
            jarInput.scopes,
            Format.JAR
        )
        
        if (jarInput.file.absolutePath.endsWith(".jar")) {
            val modifiedJar = File(jarInput.file.parent, jarInput.file.name + ".temp")
            if (modifiedJar.exists()) modifiedJar.delete()
            
            val jarFile = JarFile(jarInput.file)
            val enumeration = jarFile.entries()
            val jarOutputStream = JarOutputStream(FileOutputStream(modifiedJar))
            
            while (enumeration.hasMoreElements()) {
                val jarEntry = enumeration.nextElement()
                val entryName = jarEntry.name
                val inputStream = jarFile.getInputStream(jarEntry)
                
                val jarOutEntry = JarEntry(entryName)
                jarOutputStream.putNextEntry(jarOutEntry)
                
                if (entryName.endsWith(".class") && !entryName.startsWith("android/") && !entryName.startsWith("androidx/")) {
                    val classBytes = IOUtils.toByteArray(inputStream)
                    val modifiedBytes = modifyClass(classBytes)
                    jarOutputStream.write(modifiedBytes)
                } else {
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                
                jarOutputStream.closeEntry()
                inputStream.close()
            }
            
            jarOutputStream.close()
            jarFile.close()
            
            FileUtils.copyFile(modifiedJar, dest)
            modifiedJar.delete()
        } else {
            FileUtils.copyFile(jarInput.file, dest)
        }
    }
    
    /**
     * 处理目录输入
     */
    private fun processDirectoryInput(directoryInput: DirectoryInput, outputProvider: TransformOutputProvider) {
        val dest = outputProvider.getContentLocation(
            directoryInput.name,
            directoryInput.contentTypes,
            directoryInput.scopes,
            Format.DIRECTORY
        )
        
        if (directoryInput.file.isDirectory) {
            directoryInput.file.walkTopDown().filter { it.isFile && it.name.endsWith(".class") }.forEach { file ->
                val classBytes = file.readBytes()
                val modifiedBytes = modifyClass(classBytes)
                
                // 获取相对路径
                val relativePath = file.relativeTo(directoryInput.file)
                val destFile = File(dest, relativePath.path)
                
                // 确保父目录存在
                if (!destFile.parentFile.exists()) {
                    destFile.parentFile.mkdirs()
                }
                
                destFile.writeBytes(modifiedBytes)
            }
        }
        
        // 复制其他文件
        if (directoryInput.file.isDirectory) {
            directoryInput.file.walkTopDown().filter { it.isFile && !it.name.endsWith(".class") }.forEach { file ->
                val relativePath = file.relativeTo(directoryInput.file)
                val destFile = File(dest, relativePath.path)
                
                if (!destFile.parentFile.exists()) {
                    destFile.parentFile.mkdirs()
                }
                
                FileUtils.copyFile(file, destFile)
            }
        }
    }
    
    /**
     * 修改类文件
     */
    private fun modifyClass(classBytes: ByteArray): ByteArray {
        val classReader = ClassReader(classBytes)
        val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
        val classVisitor = ClickClassVisitor(classWriter)
        
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
        return classWriter.toByteArray()
    }
}
