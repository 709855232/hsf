package com.weidiango.spring.schema;

import com.weidiango.spring.util.HSFAnnotationUtil;
import com.weidiango.spring.util.Provider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 @author mht
 @date 2019/9/17 17:29 */
public class HSFProviderAnnotationBeanDefinitionParser extends AbstractHSFAnnotationBeanDefinitionParser {

    public static final String PROVIDER_BEAN_NAME = "com.weidian.spring.processor.Provider";
    
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext){
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        HSFAnnotationUtil.registProvderHSFAnnotationProcessor(registry);
        if(parserContext.getRegistry().containsBeanDefinition(PROVIDER_BEAN_NAME)){
           return null; 
        }
        BeanDefinition beanDefinition = copyElementAttributesForBeanDefinition(element,Provider.class);
        registerBeanDefinition(beanDefinition,PROVIDER_BEAN_NAME,registry);
        return null;
    }
}
