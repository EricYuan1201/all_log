package com.ylw.javaproject.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.ylw.javaproject.annotation.Bind;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Bind注解处理器
 * 用于处理@Bind注解，生成绑定代码
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.ylw.javaproject.annotation.Bind")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BindProcessor extends AbstractProcessor {

    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }

        // 收集被@Bind注解标记的元素
        Map<TypeElement, Map<VariableElement, String>> elementMap = new HashMap<>();
        
        // 遍历所有被@Bind注解标记的元素
        for (Element element : roundEnv.getElementsAnnotatedWith(Bind.class)) {
            if (element instanceof VariableElement) {
                VariableElement variableElement = (VariableElement) element;
                TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
                
                // 获取注解中的id值
                String id = element.getAnnotation(Bind.class).id();
                
                // 将元素添加到映射中
                Map<VariableElement, String> variableMap = elementMap.computeIfAbsent(typeElement, k -> new HashMap<>());
                variableMap.put(variableElement, id);
            }
        }
        
        // 为每个类生成绑定代码
        for (Map.Entry<TypeElement, Map<VariableElement, String>> entry : elementMap.entrySet()) {
            TypeElement typeElement = entry.getKey();
            Map<VariableElement, String> variableMap = entry.getValue();
            
            // 生成类名，格式为：类名+ActionBinder
            String className = typeElement.getSimpleName() + "ActionBinder";
            
            // 创建bind方法
            MethodSpec.Builder bindMethodBuilder = MethodSpec.methodBuilder("bind")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(ClassName.get(typeElement), "target")
                    .returns(void.class);
            
            // 为每个被注解的变量生成绑定代码
            for (Map.Entry<VariableElement, String> varEntry : variableMap.entrySet()) {
                VariableElement variableElement = varEntry.getKey();
                String id = varEntry.getValue();
                
                String fieldName = variableElement.getSimpleName().toString();
                // 使用R.id.action_log_tag作为tag的key，如果没有这个资源ID，可以使用一个固定的整数
                bindMethodBuilder.addStatement("target.$L.setTag(R.id.action_log_tag, $S)", fieldName, id);
            }
            
            // 创建绑定类
            TypeSpec binderClass = TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(bindMethodBuilder.build())
                    .build();
            
            // 获取包名
            String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
            
            // 生成Java文件
            try {
                JavaFile.builder(packageName, binderClass)
                        .build()
                        .writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Bind.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
