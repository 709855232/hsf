package com.weidiango.spring.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 @author mht
 @date 2019/9/17 10:59 */
public class HSFAnnotationNameSpaceHandler extends NamespaceHandlerSupport{

    @Override
    public void init() {
        this.registerBeanDefinitionParser("auto-hsf-annotation", new HSFAutoAnnotationBeanDefinitionParser());
        this.registerBeanDefinitionParser("auto-provider-hsf-annotation", new HSFAutoAnnotationBeanDefinitionParser());
        this.registerBeanDefinitionParser("auto-consumer-hsf-annotation", new HSFAutoAnnotationBeanDefinitionParser());
        this.registerBeanDefinitionParser("provider", new HSFProviderAnnotationBeanDefinitionParser());
        this.registerBeanDefinitionParser("consumer", new HSFConsumerAnnotationBeanDefinitionParser());
    }
}