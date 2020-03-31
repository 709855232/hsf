package com.weidiango.test;

import com.weidiango.spring.annotation.HSFService;
import com.weidiango.spring.annotation.Reference;

import javax.annotation.Resource;

/**
 @author mht
 @date 2019/9/17 20:00 */
@HSFService(group = "xixix",clientTimeout="9999")
public class testb {

    public String name;

    @Reference(group = "weidian-shop",version = "4.5.6")
    public testb id;

    @Reference(group = "weidian-shop",version = "1.2.3")
    public testb id1;


    public static void main(String[] args) {

    }

}
