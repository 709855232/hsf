package com.weidiango.spring.processor;

import com.taobao.hsf.app.spring.util.HSFSpringProviderBean;
import com.weidiango.spring.annotation.HSFService;
import com.weidiango.spring.exception.HSFAnnotationHandlerException;
import com.weidiango.spring.schema.HSFProviderAnnotationBeanDefinitionParser;
import com.weidiango.spring.util.Provider;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Iterator;

/**
 @author mht
 @date 2019/9/12 15:51 */
public class HSFProviderPostProcessor extends AbstractHSFPostProcessor{

    @Override
    public void doCreateBeanDefinition(BeanDefinitionRegistry registry, Object o) {
        try {
            String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            Iterator<String> i$ = Arrays.asList(beanDefinitionNames).iterator();
            while (i$.hasNext()){
                String beanDefinitionName = i$.next();
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
                String beanClassName = beanDefinition.getBeanClassName();
                analyzeClass(beanDefinitionName,beanClassName,registry,o);
            }
        }catch (Exception e){
            throw new HSFAnnotationHandlerException(e.toString(),e);
        }
    }

    @Override
    public void analyzeClass(String beanName,String clazzName,BeanDefinitionRegistry beanDefinitionRegistry,Object provider) throws Exception{
        Class<?> clazz = findClass(clazzName);
        if(!clazz.isAnnotationPresent(findAnnotation())){
            return;
        }
        HSFService annotation = (HSFService)clazz.getAnnotation(findAnnotation());
        String id = annotation.value();
        id = id == null || id.trim().isEmpty() ? generateBeanName(generateServiceBeanName(beanName),beanDefinitionRegistry) : id;
        RootBeanDefinition beanDef = new RootBeanDefinition();
        beanDef.setBeanClass(getSpringBeanClass());
        beanDef.setLazyInit(false);
        beanDef.getPropertyValues().addPropertyValue("target", new RuntimeBeanReference(beanName));
        beanDef.getPropertyValues().addPropertyValue("serviceInterface", resolveInterFaceName(clazz));
        batchResolveAnnotationAttributes(annotation,beanDef,provider,new String[]{"value"});
        if(hasAdded(beanDef) == null){
            BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDef, id);
            BeanDefinitionReaderUtils.registerBeanDefinition(holder, beanDefinitionRegistry);
        }
    }

    @Override
    public Class<?> getSpringBeanClass() {
        return HSFSpringProviderBean.class;
    }

    @Override
    public Class<? extends Annotation> findAnnotation() {
        return HSFService.class;
    }

    @Override
    public String[] eqaulsAttributes() {
        return new String[]{"serviceInterface","serviceGroup","serviceVersion"};
    }

    @Override
    public String  getConstantBeanName() {
        return HSFProviderAnnotationBeanDefinitionParser.PROVIDER_BEAN_NAME;
    }

    @Override
    public Class<?> getConstantBeanClass() {
        return Provider.class;
    }
}
