package com.weidiango.spring.util;

import com.weidiango.spring.processor.HSFConsumerPostProcessor;
import com.weidiango.spring.processor.HSFProviderPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 @author mht
 @date 2019/9/17 15:48 */
public class HSFAnnotationUtil{

    private static final String BEAN_NAME_SUFFIX = "#";

    private static final String SERVICE_BEAN_NAME_SUFFIX = "Impl";

    private static final String HSF_PROVIDER_POST_PROCESSOR_BEAN_NAME = "com.weidian.spring.processor.HSFProviderPostProcessor";

    private static final String HSF_COUSUMER_POST_PROCESSOR_BEAN_NAME = "com.weidian.spring.processor.HSFConsumerPostProcessor";

    public static BeanDefinition registProvderHSFAnnotationProcessor(BeanDefinitionRegistry registry){
        if(!registry.containsBeanDefinition(HSF_PROVIDER_POST_PROCESSOR_BEAN_NAME)){
            RootBeanDefinition beanDefinition = new RootBeanDefinition(HSFProviderPostProcessor.class);
            BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, HSF_PROVIDER_POST_PROCESSOR_BEAN_NAME);
            BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
            return beanDefinition;
        }
        return null;
    }

    public static BeanDefinition registConsumerHSFAnnotationProcessor(BeanDefinitionRegistry registry){
        if(!registry.containsBeanDefinition(HSF_COUSUMER_POST_PROCESSOR_BEAN_NAME)){
            RootBeanDefinition beanDefinition = new RootBeanDefinition(HSFConsumerPostProcessor.class);
            BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, HSF_COUSUMER_POST_PROCESSOR_BEAN_NAME);
            BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
            return beanDefinition;
        }
        return null;
    }

    public static String generateBeanName(String name,BeanDefinitionRegistry registry){
        String[] beanNames = registry.getBeanDefinitionNames();
        List<String> list = Arrays.asList(beanNames);
        if(list.contains(name)){
            if(name.contains(BEAN_NAME_SUFFIX)){
                String[] i$ = name.split(BEAN_NAME_SUFFIX);
                if(matches(i$[i$.length-1])){
                    StringBuilder newName = new StringBuilder();
                    for (int i = 0 ; i < i$.length-1 ; i++) {
                        newName.append(i$[i]);
                        newName.append(BEAN_NAME_SUFFIX);
                    }
                    newName.append(Integer.parseInt(i$[i$.length-1])+1);
                    return generateBeanName(newName.toString(),registry);
                }
            }
            return generateBeanName(name+BEAN_NAME_SUFFIX+"1",registry);
        }
        return name;
    }

    public static String generateServiceBeanName(String beanName){
        if(SERVICE_BEAN_NAME_SUFFIX.equalsIgnoreCase(beanName)){
            return InitialCapitalization(beanName);
        }
        if(beanName.endsWith(SERVICE_BEAN_NAME_SUFFIX)){
            beanName = beanName.replaceAll(SERVICE_BEAN_NAME_SUFFIX,"");
        }
        return InitialCapitalization(beanName);
    }

    public static String generateReferenceBeanName(Class<?> clazz){
        String simpleName = clazz.getSimpleName();
        char chars[] = simpleName.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    public static String InitialCapitalization(String character){
        if(character == null || character.trim().isEmpty()){
            return character;
        }
        return new StringBuilder(String.valueOf(Character.toUpperCase(character.charAt(0)))).append(character.substring(1)).toString();
    }

    public static boolean matches(String expr){
        if(expr.matches("\\d")){
            return true;
        }
        return false;
    }

    public static Class<?> resolveInterFaceName(Class<?> clazz){
        Class<?>[] interfaces = clazz.getInterfaces();
        if(interfaces == null || interfaces.length == 0){
            return clazz;
        }
        return interfaces[0];
    }

    public static Collection<Field> obtainAllFileds(Class<?> clazz,boolean containParents){
        Set<Field> fields = new HashSet<>();
        if(containParents){
            fields.addAll(Arrays.asList(clazz.getFields()));
        }
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return fields;
    }

    public static Collection<Method> obtainAllMethods(Class<?> clazz,boolean containParents){
        Set<Method> methods = new HashSet<>();
        if(containParents){
            methods.addAll(Arrays.asList(clazz.getMethods()));
        }
        methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        return methods;
    }

     public static Class<?> findClass(String clazzName) throws Exception{
        return ClassUtils.forName(clazzName, Thread.currentThread().getContextClassLoader());
    }

    public static String decapitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) &&
                Character.isUpperCase(name.charAt(0))){
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }
}
