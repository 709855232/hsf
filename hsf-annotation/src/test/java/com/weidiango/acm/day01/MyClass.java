package com.weidiango.acm.day01;

import com.sun.xml.internal.ws.org.objectweb.asm.*;

import java.lang.reflect.Method;

/**
 asm动态写一个类
 @author mht
 @date 2020/3/25 15:58 */
public class MyClass {

    private String name;

    public MyClass(){
        this.name = "zhangzhou";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class GenerateClass{

    public static void main(String[] args) {
        GenerateClass generateClass = new GenerateClass();
        generateClass.generateClass();
    }

    public void generateClass(){
        //方法的栈长度和本地变量表长度用户自己计算
        ClassWriter classWriter = new ClassWriter(0);
        //Opcodes.V1_6指定类的版本
        //Opcodes.ACC_PUBLIC表示这个类是public，
        //org/victorzhzh/core/classes/MyClass”类的全限定名称
        //第一个null位置变量定义的是泛型签名，
        //java/lang/Object”这个类的父类
        //第二个null位子的变量定义的是这个类实现的接口
        classWriter.visit(Opcodes.V1_6,Opcodes.ACC_PUBLIC,"org/victorzhzh/core/classes/MyClass",null,"java/lang/Object",null);


        ClassAdapter classAdapter = new MyClassAdapter(classWriter);

        classAdapter.visitField(Opcodes.ACC_PRIVATE , "name",
                Type.getDescriptor(String.class), null, null);//定义name属性

        classAdapter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null,
                null).visitCode();//定义构造方法

        String setMethodDesc = "(" + Type.getDescriptor(String.class) + ")V";
        classAdapter.visitMethod(Opcodes.ACC_PUBLIC, "setName", setMethodDesc,
                null, null).visitCode();//定义setName方法

        String getMethodDesc = "()" + Type.getDescriptor(String.class);
        classAdapter.visitMethod(Opcodes.ACC_PUBLIC, "getName", getMethodDesc,
                null, null).visitCode();//定义getName方法

        byte[] classFile = classWriter.toByteArray();//生成字节码

        MyClassLoader classLoader = new MyClassLoader();//定义一个类加载器
        Class clazz = classLoader.defineClassFromClassFile(
                "org.victorzhzh.core.classes.MyClass", classFile);
        try {//利用反射方式，访问getName
            Object obj = clazz.newInstance();
            Method method = clazz.getMethod("getName");
            System.out.println(obj.toString());
            System.out.println(method.invoke(obj, null));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class MyClassLoader extends ClassLoader {
        public Class defineClassFromClassFile(String className, byte[] classFile)
                throws ClassFormatError {
            return defineClass(className, classFile, 0, classFile.length);
        }
    }


}

class MyClassAdapter extends ClassAdapter {

    public MyClassAdapter(ClassVisitor cv) {
        super(cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals("<init>")) {
            return new InitMethodAdapter(methodVisitor);
        } else if (name.equals("setName")) {
            return new SetMethodAdapter(methodVisitor);
        } else if (name.equals("getName")) {
            return new GetMethodAdapter(methodVisitor);
        } else {
            return super.visitMethod(access, name, desc, signature, exceptions);
        }
    }


    //这个类生成具体的构造方法字节码
    class InitMethodAdapter extends MethodAdapter{

        public InitMethodAdapter(MethodVisitor mv) {
            super(mv);
        }

        @Override
        public void visitCode(){
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object",
                    "<init>", "()V");//调用父类的构造方法
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitLdcInsn("zhangzhuo");//将常量池中的字符串常量加载刀栈顶
            mv.visitFieldInsn(Opcodes.PUTFIELD,
                    "org/victorzhzh/core/classes/MyClass", "name",
                    Type.getDescriptor(String.class));//对name属性赋值
            mv.visitInsn(Opcodes.RETURN);//设置返回值
            mv.visitMaxs(2, 1);//设置方法的栈和本地变量表的大小
        }
    }

    //这个类生成具体的setName方法字节码
    class SetMethodAdapter extends MethodAdapter {
        public SetMethodAdapter(MethodVisitor mv) {
            super(mv);
        }

        @Override
        public void visitCode() {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitFieldInsn(Opcodes.PUTFIELD,
                    "org/victorzhzh/core/classes/MyClass", "name",
                    Type.getDescriptor(String.class));
            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(2, 2);
        }

    }

    ////这个类生成具体的getName方法字节
    class GetMethodAdapter extends MethodAdapter {

        public GetMethodAdapter(MethodVisitor mv) {
            super(mv);
        }

        @Override
        public void visitCode() {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD,
                    "org/victorzhzh/core/classes/MyClass", "name",
                    Type.getDescriptor(String.class));//获取name属性的值
            mv.visitInsn(Opcodes.ARETURN);//返回一个引用，这里是String的引用即name
            mv.visitMaxs(1, 1);
        }
    }

}
