package com.weidiango.acm.day02;

import com.sun.xml.internal.ws.org.objectweb.asm.*;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.Vector;

/**
 @author mht
 @date 2020/3/25 16:45 */
public class Person {

    static {
        System.out.println("类 person 被加载");
    }

    public String name = "123";

    public String adress = "456";


    public static void main(String[] args) {

    }
}


class Transform extends ClassAdapter {

    public Transform(ClassVisitor cv) {
        super(cv);
    }

    @Override
    public void visitEnd() {
        cv.visitField(Opcodes.ACC_PUBLIC, "age", Type.getDescriptor(int.class),
                null, null);
    }

}


class test{
    static {

    }

    public static void main(String[] args) throws Exception{
        /*Class<?> aClass = Class.forName("com.weidiango.acm.day02.Person");
        Object o = aClass.newInstance();*/

        Person person1 = new Person();

        ClassReader classReader = new ClassReader(
                "com.weidiango.acm.day02.Person");
        print(Thread.currentThread().getContextClassLoader());

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassAdapter classAdapter = new Transform(classWriter);

        classReader.accept(classAdapter, ClassReader.SKIP_DEBUG);

        byte[] classFile = classWriter.toByteArray();
        System.out.println(classAdapter);
        write(classFile,Person.class);

        GeneratorClassLoader classLoader = new GeneratorClassLoader();
        Class clazz = classLoader.defineClassFromClassFile(
                "com.weidiango.acm.day02.Person", classFile);


        Object obj = clazz.newInstance();
        clazz.getDeclaredFields();
        print(Thread.currentThread().getContextClassLoader());

        System.out.println(clazz.getDeclaredField("name").get(obj));//----(1)
        System.out.println(clazz.getDeclaredField("age").get(obj));//----(2)

        /*Person person = new Person();
        System.out.println(person);
        Field[] declaredFields = person.getClass().getDeclaredFields();*/
        

        Person person = new Person();
        Object age = person.getClass().getDeclaredField("age").get(person);
        Class<?> aClass1 = Class.forName("com.weidiango.acm.day02.Person");
        Object o1 = aClass1.newInstance();
        System.out.println(o1);
    }

    static void print(ClassLoader classLoader) throws Exception{
        Field classes = ClassLoader.class.getDeclaredField("classes");
        classes.setAccessible(true);
        Vector<Class<?>> v = (Vector<Class<?>>) classes.get(classLoader);
        for (Class<?> aClass : v) {
            System.out.println("加载:"+aClass.getName());
        }
        System.out.println(classLoader);
    }


    static  void write(byte[] classFile,Class clazz) throws Exception{
        String classFilePath = clazz.getResource("/").getPath();
        String s = clazz.getName().replaceAll("\\.", "/");
        File file = new File(classFilePath,s+".class");
        System.out.println(file.getAbsolutePath());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(classFile);
        fileOutputStream.close();
    }



}

 class GeneratorClassLoader extends ClassLoader {

    @SuppressWarnings("rawtypes")
    public Class defineClassFromClassFile(String className, byte[] classFile)
            throws ClassFormatError {
        return defineClass(className, classFile, 0, classFile.length);
    }
}
