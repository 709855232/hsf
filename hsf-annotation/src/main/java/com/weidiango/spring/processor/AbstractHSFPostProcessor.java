package com.weidiango.spring.processor;

import com.weidiango.spring.exception.HSFAnnotationHandlerException;
import com.weidiango.spring.util.AliasMappingRegistry;
import com.weidiango.spring.util.HSFAnnotationUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 @author mht
 @date 2019/9/12 15:51 */
public abstract class AbstractHSFPostProcessor implements BeanDefinitionRegistryPostProcessor,BeanFactoryAware{

    public ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Object object = null;
        try {
            object = beanFactory.getBean(getConstantBeanName(),getConstantBeanClass());
        }catch (Exception e){
            ;
        }
        doCreateBeanDefinition(registry,object);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if(!(beanFactory instanceof ConfigurableListableBeanFactory)){
            throw new HSFAnnotationHandlerException("beanFactory must be instanceof ConfigurableListableBeanFactory");
        }
        this.beanFactory = (ConfigurableListableBeanFactory)beanFactory;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    public Object processGlobalConstant(String fieldName,Object value,Object object) throws Exception{
        if(value != null && ( (value instanceof String && !((String) value).trim().isEmpty()) || !(value instanceof String))){
            return value;
        }
        try {
            Collection<Field> fields = obtainAllFileds(object.getClass(),true);
            Iterator<Field> i$ = fields.iterator();
            while (i$.hasNext()){
                Field field = i$.next();
                if(field.getName().equals(resolveProviderAlias(fieldName))){
                    field.setAccessible(true);
                    return isRequired(fieldName,field.get(object));
                }
            }
        }catch (Exception e){
            return isRequired(fieldName,value);
        }
        return null;
    }

    Object isRequired(String key,Object value){
        if(value != null && ( (value instanceof String && !((String) value).trim().isEmpty()) || !(value instanceof String))){
            return value;
        }
        Object required = resolveRequiredAlias(key);
        if(required instanceof Boolean && ((Boolean) required) == true){
            throw new HSFAnnotationHandlerException(key + "  is required");
        }
        return null;
    }

    public <T extends Annotation> void batchResolveAnnotationAttributes(T t,BeanDefinition beanDefinition,
                                                                    Object object,String [] ingoreAttribute) throws Exception{
        Collection<Method> methods = obtainAllMethods(t.annotationType(),false);
        for (Method method : methods) {
            if(Arrays.asList(ingoreAttribute).contains(method.getName())){
                continue;
            }
            Object value = method.invoke(t);
            value = processGlobalConstant(method.getName(), value, object);
            if(value != null){
                beanDefinition.getPropertyValues().addPropertyValue((String) resolveProviderAlias(method.getName()),value);
            }
        }
    }

    public String hasAdded(BeanDefinition beanDefinition){
        String[] beanNames = beanFactory.getBeanNamesForType(getSpringBeanClass(),true,false);
        for (String beanName : beanNames) {
            BeanDefinition existBeanDefinition = beanFactory.getBeanDefinition(beanName);
            if(eqaulsAttributes(existBeanDefinition, beanDefinition)){
                return beanName;
            }
        }
        return null;
    }

    public boolean eqaulsAttributes(BeanDefinition existBeanDefinition,BeanDefinition newBeanDefinition){
        for (String attribute : eqaulsAttributes()) {
            if(!existBeanDefinition.getPropertyValues().get(attribute).equals(newBeanDefinition.getPropertyValues().get(attribute))){
                return false;
            }
        }
        return true;
    }

    public void reviseAnnotationValue(Annotation annotation,String key,String value)throws Exception{
        InvocationHandler handler = Proxy.getInvocationHandler(annotation);
        Field resourceField = handler.getClass().getDeclaredField("memberValues");
        resourceField.setAccessible(true);
        Map<String,Object> map = (Map<String,Object>)resourceField.get(handler);
        map.put(key,value);
    }

    public Class<?> findClass(String clazzName) throws Exception{
        return HSFAnnotationUtil.findClass(clazzName);
    }

    public Object resolveAlias(String alias){
        return AliasMappingRegistry.resolveAlias(alias);
    }

    public String generateBeanName(String beanName,BeanDefinitionRegistry registry){
        return HSFAnnotationUtil.generateBeanName(beanName,registry);
    }

    public String generateServiceBeanName(String beanName){
        return HSFAnnotationUtil.generateServiceBeanName(beanName);
    }

    public String generateReferenceBeanName(Class<?> clazz){
        return HSFAnnotationUtil.generateReferenceBeanName(clazz);
    }

    public String resolveInterFaceName(Class<?> clazz){
        return HSFAnnotationUtil.resolveInterFaceName(clazz).getName();
    }

    public Object resolveRequiredAlias(String alias){
        return AliasMappingRegistry.resolveRequiredAlias(alias);
    }

    public Object resolveProviderAlias(String alias){
        return AliasMappingRegistry.resolveProviderAlias(alias,getConstantBeanClass());
    }

    public Collection<Field> obtainAllFileds(Class<?> clazz,boolean containParents){
        return HSFAnnotationUtil.obtainAllFileds(clazz,containParents);
    }

    public Collection<Method> obtainAllMethods(Class<?> clazz,boolean containParents){
        return HSFAnnotationUtil.obtainAllMethods(clazz,containParents);
    }

    public abstract String [] eqaulsAttributes();

    public abstract String getConstantBeanName();

    public abstract Class<?> getConstantBeanClass();

    public abstract void analyzeClass(String beanName,String clazzName,BeanDefinitionRegistry beanDefinitionRegistry,Object o) throws Exception;

    public abstract void doCreateBeanDefinition(BeanDefinitionRegistry registry,Object o);

    public abstract Class<?> getSpringBeanClass();

    public abstract Class<? extends Annotation> findAnnotation();

}
