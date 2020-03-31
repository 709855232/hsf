package com.weidiango.spring.schema;

import com.weidiango.spring.util.HSFAnnotationUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 @author mht
 @date 2019/9/17 11:42 */
public class HSFAutoAnnotationBeanDefinitionParser implements BeanDefinitionParser {

    private static final String AUTO_HSF_ANNOTATION = "auto-hsf-annotation";

    private static final String AUTO_PROVIDER_HSF_ANNOTATION = "auto-provider-hsf-annotation";

    private static final String AUTO_CONSUMER_HSF_ANNOTATION = "auto-consumer-hsf-annotation";

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String localName = element.getLocalName();
        switch (localName){
            case AUTO_HSF_ANNOTATION:
                HSFAnnotationUtil.registProvderHSFAnnotationProcessor(parserContext.getRegistry());
                HSFAnnotationUtil.registConsumerHSFAnnotationProcessor(parserContext.getRegistry());
                break;
            case AUTO_PROVIDER_HSF_ANNOTATION:
                HSFAnnotationUtil.registProvderHSFAnnotationProcessor(parserContext.getRegistry());
                break;
            case AUTO_CONSUMER_HSF_ANNOTATION:
                HSFAnnotationUtil.registConsumerHSFAnnotationProcessor(parserContext.getRegistry());
                break;
            default:
        }
        return null;
    }
}
