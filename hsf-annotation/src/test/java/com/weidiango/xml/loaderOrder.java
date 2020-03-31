package com.weidiango.xml;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.lang.reflect.Field;

/**
 @author mht
 @date 2020/3/26 10:58 */
public class loaderOrder {

    public static void main(String[] args) {
        System.out.println(123);
        Field[] declaredFields = A.class.getDeclaredFields();
        System.out.println(declaredFields);
        A.AA();


    }
}


class A{
    static final void AA(){
        System.out.println("AA");
    }

    static {
        System.out.println("类A被加载");
    }

}

class B{
    static {
        System.out.println("类b被加载");
    }

}

class C{
    static {
        System.out.println("类c被加载");
    }

}


