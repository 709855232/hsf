package com.weidiango.spring.schema;

import com.weidiango.spring.util.AliasMappingRegistry;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 @author mht
 @date 2019/9/17 17:31 */
public abstract class AbstractHSFAnnotationBeanDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        return null;
    }

    public void registerBeanDefinition(BeanDefinition beanDefinition, String beanName, BeanDefinitionRegistry registry){
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, beanName);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    public <T> T copyElementAttributesForClass(Element element,Class<T> clazz) throws Exception{
        Map<String,Field> map = new HashMap<>();
        for (Field declaredField : clazz.getDeclaredFields()) {
            map.put(declaredField.getName(),declaredField);
        }
        for (Field field : clazz.getFields()) {
            map.put(field.getName(),field);
        }
        Iterator<Map.Entry<String, Field>> i$ = map.entrySet().iterator();
        T t = clazz.newInstance();
        while(i$.hasNext()){
            Map.Entry<String, Field> entry = i$.next();
            String attributeValue = element.getAttribute(entry.getKey());
            if(attributeValue == null || attributeValue.trim().length() == 0){
                continue;
            }
            entry.getValue().setAccessible(true);
            entry.getValue().set(t,attributeValue);
        }
        return t;
    }

    public BeanDefinition copyElementAttributesForBeanDefinition(Element element,Class<?> clazz){
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(clazz);
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0 ; i < attributes.getLength() ; i++) {
            Node item = attributes.item(i);
            String nodeValue = item.getNodeValue();
            if(nodeValue == null || nodeValue.trim().isEmpty()){
                continue;
            }
            rootBeanDefinition.getPropertyValues().addPropertyValue((String)resolveProviderAlias(item.getNodeName(),clazz),nodeValue);
        }
        return rootBeanDefinition;
    }

    public Object resolveAlias(String alias){
       return AliasMappingRegistry.resolveAlias(alias);
    }

    public static Object resolveProviderAlias(String alias,Class<?> clazz){
        return AliasMappingRegistry.resolveProviderAlias(alias,clazz);
    }
}
