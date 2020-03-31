package com.weidiango.test;

import com.weidiango.spring.annotation.HSFService;
import com.weidiango.spring.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 @author mht
 @date 2019/9/17 20:00 */
@HSFService(group = "xixix",clientTimeout="9999")
@Component
public class testa {

    public String name;

    @Reference(group = "weidian-shop")
    public com.weidiango.test.testb testb;


    public static void main(String[] args) throws Exception{
        Field field = testa.class.getField("testb");
        System.out.println(field.getAnnotation(Autowired.class));
    }

    @Override
    public String toString() {
        return "testa{" +
                "name='" + name + '\'' +
                ", testb=" + testb +
                '}';
    }
}
