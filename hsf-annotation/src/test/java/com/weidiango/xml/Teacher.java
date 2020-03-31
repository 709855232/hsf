package com.weidiango.xml;

import com.weidiango.spring.annotation.HSFService;
import com.weidiango.spring.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 @author mht
 @date 2020/3/25 14:53 */
@Component
public class Teacher {

    //@Reference
    public Student student1;

    //@Reference(version = "1.2.3")
    public Student student2;

    public Student student;

   /* @Reference()
    public Student student2;*/



}
