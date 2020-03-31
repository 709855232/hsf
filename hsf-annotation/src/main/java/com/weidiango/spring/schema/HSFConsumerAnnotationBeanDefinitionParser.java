package com.weidiango.spring.schema;

import com.weidiango.spring.util.Consumer;
import com.weidiango.spring.util.HSFAnnotationUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 @author mht
 @date 2019/9/17 17:29 */
public class HSFConsumerAnnotationBeanDefinitionParser extends AbstractHSFAnnotationBeanDefinitionParser {

    public static final String CONSUMER_BEAN_NAME = "com.weidian.spring.processor.Consumer";

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        HSFAnnotationUtil.registConsumerHSFAnnotationProcessor(registry);
        if(parserContext.getRegistry().containsBeanDefinition(CONSUMER_BEAN_NAME)){
            return null;
        }
        BeanDefinition beanDefinition = copyElementAttributesForBeanDefinition(element,Consumer.class);
        registerBeanDefinition(beanDefinition,CONSUMER_BEAN_NAME,registry);
        return null;
    }
}
