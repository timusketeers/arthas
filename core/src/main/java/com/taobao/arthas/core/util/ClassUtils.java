package com.taobao.arthas.core.util;

import static com.taobao.text.ui.Element.label;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.arthas.deps.org.objectweb.asm.Type;
import com.taobao.arthas.core.command.model.ClassLoaderVO;
import com.taobao.arthas.core.command.model.ClassVO;
import com.taobao.arthas.core.command.model.MethodVO;
import com.taobao.text.Color;
import com.taobao.text.Decoration;
import com.taobao.text.ui.Element;
import com.taobao.text.ui.LabelElement;
import com.taobao.text.ui.TableElement;

import static com.taobao.text.Decoration.bold;

/**
 *
 * @author hengyunabc 2018-10-18
 *
 */
public class ClassUtils {

    public static String getCodeSource(final CodeSource cs) {
        if (null == cs || null == cs.getLocation() || null == cs.getLocation().getFile()) {
            return com.taobao.arthas.core.util.Constants.EMPTY_STRING;
        }

        return cs.getLocation().getFile();
    }

    public static boolean isLambdaClass(Class<?> clazz) {
        return clazz.getName().contains("$$Lambda$");
    }

    public static Element renderClassInfo(Class<?> clazz) {
        return renderClassInfo(clazz, false, null);
    }

    public static Element renderClassInfo(ClassVO clazz) {
        return renderClassInfo(clazz, false, null);
    }

    public static Element renderClassInfo(Class<?> clazz, boolean isPrintField, Integer expand) {
        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
        CodeSource cs = clazz.getProtectionDomain().getCodeSource();

        table.row(label("class-info").style(Decoration.bold.bold()), label(StringUtils.classname(clazz)))
                .row(label("code-source").style(Decoration.bold.bold()), label(getCodeSource(cs)))
                .row(label("name").style(Decoration.bold.bold()), label(StringUtils.classname(clazz)))
                .row(label("isInterface").style(Decoration.bold.bold()), label("" + clazz.isInterface()))
                .row(label("isAnnotation").style(Decoration.bold.bold()), label("" + clazz.isAnnotation()))
                .row(label("isEnum").style(Decoration.bold.bold()), label("" + clazz.isEnum()))
                .row(label("isAnonymousClass").style(Decoration.bold.bold()), label("" + clazz.isAnonymousClass()))
                .row(label("isArray").style(Decoration.bold.bold()), label("" + clazz.isArray()))
                .row(label("isLocalClass").style(Decoration.bold.bold()), label("" + clazz.isLocalClass()))
                .row(label("isMemberClass").style(Decoration.bold.bold()), label("" + clazz.isMemberClass()))
                .row(label("isPrimitive").style(Decoration.bold.bold()), label("" + clazz.isPrimitive()))
                .row(label("isSynthetic").style(Decoration.bold.bold()), label("" + clazz.isSynthetic()))
                .row(label("simple-name").style(Decoration.bold.bold()), label(clazz.getSimpleName()))
                .row(label("modifier").style(Decoration.bold.bold()), label(StringUtils.modifier(clazz.getModifiers(), ',')))
                .row(label("annotation").style(Decoration.bold.bold()), label(TypeRenderUtils.drawAnnotation(clazz)))
                .row(label("interfaces").style(Decoration.bold.bold()), label(TypeRenderUtils.drawInterface(clazz)))
                .row(label("super-class").style(Decoration.bold.bold()), TypeRenderUtils.drawSuperClass(clazz))
                .row(label("class-loader").style(Decoration.bold.bold()), TypeRenderUtils.drawClassLoader(clazz))
                .row(label("classLoaderHash").style(Decoration.bold.bold()), label(StringUtils.classLoaderHash(clazz)));

        if (isPrintField) {
            table.row(label("fields").style(Decoration.bold.bold()), TypeRenderUtils.drawField(clazz, expand));
        }
        return table;
    }

    public static Element renderClassInfo(ClassVO clazz, boolean isPrintField, Integer expand) {
        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);

        table.row(label("class-info").style(Decoration.bold.bold()), label(clazz.getClassInfo()))
                .row(label("code-source").style(Decoration.bold.bold()), label(clazz.getCodeSource()))
                .row(label("name").style(Decoration.bold.bold()), label(clazz.getName()))
                .row(label("isInterface").style(Decoration.bold.bold()), label("" + clazz.getInterface()))
                .row(label("isAnnotation").style(Decoration.bold.bold()), label("" + clazz.getAnnotation()))
                .row(label("isEnum").style(Decoration.bold.bold()), label("" + clazz.getEnum()))
                .row(label("isAnonymousClass").style(Decoration.bold.bold()), label("" + clazz.getAnonymousClass()))
                .row(label("isArray").style(Decoration.bold.bold()), label("" + clazz.getArray()))
                .row(label("isLocalClass").style(Decoration.bold.bold()), label("" + clazz.getLocalClass()))
                .row(label("isMemberClass").style(Decoration.bold.bold()), label("" + clazz.getMemberClass()))
                .row(label("isPrimitive").style(Decoration.bold.bold()), label("" + clazz.getPrimitive()))
                .row(label("isSynthetic").style(Decoration.bold.bold()), label("" + clazz.getSynthetic()))
                .row(label("simple-name").style(Decoration.bold.bold()), label(clazz.getSimpleName()))
                .row(label("modifier").style(Decoration.bold.bold()), label(clazz.getModifier()))
                .row(label("annotation").style(Decoration.bold.bold()), label(StringUtils.join(clazz.getAnnotations(), ",")))
                .row(label("interfaces").style(Decoration.bold.bold()), label(StringUtils.join(clazz.getInterfaces(), ",")))
                .row(label("super-class").style(Decoration.bold.bold()), TypeRenderUtils.drawSuperClass(clazz))
                .row(label("class-loader").style(Decoration.bold.bold()), TypeRenderUtils.drawClassLoader(clazz))
                .row(label("classLoaderHash").style(Decoration.bold.bold()), label(clazz.getClassLoaderHash()));

        if (isPrintField) {
            table.row(label("fields").style(Decoration.bold.bold()), TypeRenderUtils.drawField(clazz, expand));
        }
        return table;
    }

    public static ClassVO createClassInfo(Class clazz, boolean detail, boolean withFields) {
        CodeSource cs = clazz.getProtectionDomain().getCodeSource();
        ClassVO classInfo = new ClassVO();
        classInfo.setName(StringUtils.classname(clazz));
        if (detail) {
            classInfo.setClassInfo(StringUtils.classname(clazz));
            classInfo.setCodeSource(ClassUtils.getCodeSource(cs));
            classInfo.setInterface(clazz.isInterface());
            classInfo.setAnnotation(clazz.isAnnotation());
            classInfo.setEnum(clazz.isEnum());
            classInfo.setAnonymousClass(clazz.isAnonymousClass());
            classInfo.setArray(clazz.isArray());
            classInfo.setLocalClass(clazz.isLocalClass());
            classInfo.setMemberClass(clazz.isMemberClass());
            classInfo.setPrimitive(clazz.isPrimitive());
            classInfo.setSynthetic(clazz.isSynthetic());
            classInfo.setSimpleName(clazz.getSimpleName());
            classInfo.setModifier(StringUtils.modifier(clazz.getModifiers(), ','));
            classInfo.setAnnotations(TypeRenderUtils.getAnnotations(clazz));
            classInfo.setInterfaces(TypeRenderUtils.getInterfaces(clazz));
            classInfo.setSuperClass(TypeRenderUtils.getSuperClass(clazz));
            classInfo.setClassloader(TypeRenderUtils.getClassloader(clazz));
            classInfo.setClassLoaderHash(StringUtils.classLoaderHash(clazz));
        }
        if (withFields) {
            classInfo.setFields(TypeRenderUtils.getFields(clazz));
        }
        return classInfo;
    }

    public static ClassVO createSimpleClassInfo(Class clazz) {
        ClassVO classInfo = new ClassVO();
        classInfo.setName(StringUtils.classname(clazz));
        classInfo.setClassloader(TypeRenderUtils.getClassloader(clazz));
        classInfo.setClassLoaderHash(StringUtils.classLoaderHash(clazz));
        return classInfo;
    }

    public static MethodVO createMethodInfo(Method method, Class clazz, boolean detail) {
        MethodVO methodVO = new MethodVO();
        methodVO.setDeclaringClass(clazz.getName());
        methodVO.setMethodName(method.getName());
        methodVO.setDescriptor(Type.getMethodDescriptor(method));
        methodVO.setConstructor(false);
        if (detail) {
            methodVO.setModifier(StringUtils.modifier(method.getModifiers(), ','));
            methodVO.setAnnotations(TypeRenderUtils.getAnnotations(method.getDeclaredAnnotations()));
            methodVO.setParameters(getClassNameList(method.getParameterTypes()));
            methodVO.setReturnType(StringUtils.classname(method.getReturnType()));
            methodVO.setExceptions(getClassNameList(method.getExceptionTypes()));
            methodVO.setClassLoaderHash(StringUtils.classLoaderHash(clazz));
        }
        return methodVO;
    }

    public static MethodVO createMethodInfo(Constructor constructor, Class clazz, boolean detail) {
        MethodVO methodVO = new MethodVO();
        methodVO.setDeclaringClass(clazz.getName());
        methodVO.setDescriptor(Type.getConstructorDescriptor(constructor));
        methodVO.setMethodName("<init>");
        methodVO.setConstructor(true);
        if (detail) {
            methodVO.setModifier(StringUtils.modifier(constructor.getModifiers(), ','));
            methodVO.setAnnotations(TypeRenderUtils.getAnnotations(constructor.getDeclaredAnnotations()));
            methodVO.setParameters(getClassNameList(constructor.getParameterTypes()));
            methodVO.setExceptions(getClassNameList(constructor.getExceptionTypes()));
            methodVO.setClassLoaderHash(StringUtils.classLoaderHash(clazz));
        }
        return methodVO;
    }


    public static Element renderMethod(Method method, Class<?> clazz) {
        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
        table.row(label("declaring-class").style(bold.bold()), label(method.getDeclaringClass().getName()))
                .row(label("method-name").style(bold.bold()), label(method.getName()).style(bold.bold()))
                .row(label("modifier").style(bold.bold()), label(StringUtils.modifier(method.getModifiers(), ',')))
                .row(label("annotation").style(bold.bold()), label(TypeRenderUtils.drawAnnotation(method)))
                .row(label("parameters").style(bold.bold()), label(TypeRenderUtils.drawParameters(method)))
                .row(label("return").style(bold.bold()), label(TypeRenderUtils.drawReturn(method)))
                .row(label("exceptions").style(bold.bold()), label(TypeRenderUtils.drawExceptions(method)))
                .row(label("classLoaderHash").style(bold.bold()), label(StringUtils.classLoaderHash(clazz)));
        return table;
    }

    public static Element renderMethod(MethodVO method) {
        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
        table.row(label("declaring-class").style(bold.bold()), label(method.getDeclaringClass()))
                .row(label("method-name").style(bold.bold()), label(method.getMethodName()).style(bold.bold()))
                .row(label("modifier").style(bold.bold()), label(method.getModifier()))
                .row(label("annotation").style(bold.bold()), label(TypeRenderUtils.drawAnnotation(method.getAnnotations())))
                .row(label("parameters").style(bold.bold()), label(TypeRenderUtils.drawParameters(method.getParameters())))
                .row(label("return").style(bold.bold()), label(method.getReturnType()))
                .row(label("exceptions").style(bold.bold()), label(TypeRenderUtils.drawExceptions(method.getExceptions())))
                .row(label("classLoaderHash").style(bold.bold()), label(method.getClassLoaderHash()));
        return table;
    }

    public static Element renderConstructor(Constructor<?> constructor, Class<?> clazz) {
        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
        table.row(label("declaring-class").style(bold.bold()), label(constructor.getDeclaringClass().getName()))
                .row(label("constructor-name").style(bold.bold()), label("<init>").style(bold.bold()))
                .row(label("modifier").style(bold.bold()), label(StringUtils.modifier(constructor.getModifiers(), ',')))
                .row(label("annotation").style(bold.bold()), label(TypeRenderUtils.drawAnnotation(constructor.getDeclaredAnnotations())))
                .row(label("parameters").style(bold.bold()), label(TypeRenderUtils.drawParameters(constructor)))
                .row(label("exceptions").style(bold.bold()), label(TypeRenderUtils.drawExceptions(constructor)))
                .row(label("classLoaderHash").style(bold.bold()), label(StringUtils.classLoaderHash(clazz)));
        return table;
    }

    public static Element renderConstructor(MethodVO constructor) {
        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
        table.row(label("declaring-class").style(bold.bold()), label(constructor.getDeclaringClass()))
                .row(label("constructor-name").style(bold.bold()), label("<init>").style(bold.bold()))
                .row(label("modifier").style(bold.bold()), label(constructor.getModifier()))
                .row(label("annotation").style(bold.bold()), label(TypeRenderUtils.drawAnnotation(constructor.getAnnotations())))
                .row(label("parameters").style(bold.bold()), label(TypeRenderUtils.drawParameters(constructor.getParameters())))
                .row(label("exceptions").style(bold.bold()), label(TypeRenderUtils.drawExceptions(constructor.getExceptions())))
                .row(label("classLoaderHash").style(bold.bold()), label(constructor.getClassLoaderHash()));
        return table;
    }

    public static String[] getClassNameList(Class[] classes) {
        List<String> list = new ArrayList<String>();
        for (Class anInterface : classes) {
            list.add(StringUtils.classname(anInterface));
        }
        return list.toArray(new String[0]);
    }

    public static List<ClassVO> createClassVOList(Set<Class<?>> matchedClasses) {
        List<ClassVO> classVOs = new ArrayList<ClassVO>(matchedClasses.size());
        for (Class<?> aClass : matchedClasses) {
            ClassVO classVO = createSimpleClassInfo(aClass);
            classVOs.add(classVO);
        }
        return classVOs;
    }

    public static ClassLoaderVO createClassLoaderVO(ClassLoader classLoader) {
        ClassLoaderVO classLoaderVO = new ClassLoaderVO();
        classLoaderVO.setHash(classLoaderHash(classLoader));
        classLoaderVO.setName(classLoader==null?"BootstrapClassLoader":classLoader.toString());
        ClassLoader parent = classLoader == null ? null : classLoader.getParent();
        classLoaderVO.setParent(parent==null?null:parent.toString());
        return classLoaderVO;
    }

    public static String classLoaderHash(Class<?> clazz) {
        if (clazz == null || clazz.getClassLoader() == null) {
            return "null";
        }
        return Integer.toHexString(clazz.getClassLoader().hashCode());
    }

    public static String classLoaderHash(ClassLoader classLoader) {
        if (classLoader == null ) {
            return "null";
        }
        return Integer.toHexString(classLoader.hashCode());
    }

    public static Element renderMatchedClasses(Collection<ClassVO> matchedClasses) {
        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
        table.row(new LabelElement("NAME").style(Decoration.bold.bold()),
                new LabelElement("HASHCODE").style(Decoration.bold.bold()),
                new LabelElement("CLASSLOADER").style(Decoration.bold.bold()));

        for (ClassVO c : matchedClasses) {
            table.row(label(c.getName()),
                    label(c.getClassLoaderHash()).style(Decoration.bold.fg(Color.red)),
                    TypeRenderUtils.drawClassLoader(c));
        }
        return table;
    }

    /**
     * 转换增强字节码回调方法中的非标准类名
     * normalizeClassName:  a/b/c/MyClass -> a.b.c.MyClass
     * @param className  maybe path class name
     * @param normalizeClassNameMap
     */
    public static String normalizeClassName(String className, Map<String, String> normalizeClassNameMap) {
        //如果类名包含'/'，需要转换为标准类名
        String normalClassName = null;
        if (className.indexOf('/') != -1) {
            normalClassName = normalizeClassNameMap.get(className);
            if (normalClassName == null) {
                normalClassName = StringUtils.normalizeClassName(className);
                normalizeClassNameMap.put(className, normalClassName);
            }
        } else {
            normalClassName = className;
        }
        return normalClassName;
    }
}
