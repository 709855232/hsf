package com.weidiango.spring.util;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 @author mht
 @date 2019/9/17 16:22 */
public class ConsumerBeanDefinition {
    private String beanName;

    private Class<?> reference;

    private BeanDefinition beanDefinition;

    private MutablePropertyValues propertyValues;

    private String group;

    private String version;

    private String uniqueIdentification;

    public ConsumerBeanDefinition() {

    }

    public ConsumerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception{
        this.beanName = beanName;
        this.beanDefinition = beanDefinition;
        this.propertyValues = beanDefinition.getPropertyValues();
        this.reference = HSFAnnotationUtil.findClass((String)propertyValues.get("interfaceName"));
        this.group = (String) propertyValues.get("group");
        this.version = (String) propertyValues.get("version");
        uniqueIdentification = new StringBuilder(reference.getName()).append("&").append(group).append("&").append(version).toString();
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Class<?> getReference() {
        return reference;
    }

    public void setReference(Class<?> reference) {
        this.reference = reference;
    }

    public BeanDefinition getBeanDefinition() {
        return beanDefinition;
    }

    public void setBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinition = beanDefinition;
    }

    public String getUniqueIdentification() {
        return uniqueIdentification;
    }
}
