package com.weidiango.spring.processor;

import com.taobao.hsf.app.spring.util.HSFSpringConsumerBean;
import com.weidiango.spring.annotation.Reference;
import com.weidiango.spring.exception.HSFAnnotationHandlerException;
import com.weidiango.spring.schema.HSFConsumerAnnotationBeanDefinitionParser;
import com.weidiango.spring.util.Consumer;
import com.weidiango.spring.util.ConsumerBeanDefinition;
import com.weidiango.spring.util.HSFAnnotationUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 @author mht
 @date 2019/9/12 15:51 */
public class HSFConsumerPostProcessor extends AbstractHSFPostProcessor implements InitializingBean{

    private Map<String,ConsumerBeanDefinition> consumerBeanDefinitions = new HashMap<>();

    public static final String BEAN_DEFINITION_SCANNER_CLASS = "org.springframework.context.annotation.ClassPathBeanDefinitionScanner";

    public static final String ANNOTATION_TYPE_FILTER_CLASS = "org.springframework.core.type.filter.AnnotationTypeFilter";

    public static final String ADD_INCLUDE_FILTER_METHOD = "addIncludeFilter";

    public static final String FIND_CANDIDATE_COMPONENTS_METHOD = "findCandidateComponents";

    @Override
    public void doCreateBeanDefinition(BeanDefinitionRegistry registry, Object o) {
        if(o == null){
            throw new HSFAnnotationHandlerException("consumer tag not allow empty !!!");
        }
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<?> scannerClass = ClassUtils.forName(BEAN_DEFINITION_SCANNER_CLASS,classLoader);
            Object scanner = scannerClass.getConstructor(new Class<?>[]{BeanDefinitionRegistry.class, boolean.class}).newInstance(registry, true);
            Class<?> filterClass = ClassUtils.forName(ANNOTATION_TYPE_FILTER_CLASS, classLoader);
            Object serviceFilter = filterClass.getConstructor(new Class[]{Class.class}).newInstance(Service.class);
            Object controllerFilter = filterClass.getConstructor(new Class[]{Class.class}).newInstance(Controller.class);
            Method filter = scannerClass.getMethod(ADD_INCLUDE_FILTER_METHOD, new Class<?>[]{TypeFilter.class});
            filter.invoke(scanner,serviceFilter);
            filter.invoke(scanner,controllerFilter);
            Method scan = scannerClass.getMethod(FIND_CANDIDATE_COMPONENTS_METHOD, new Class<?>[]{String.class});
            Set<BeanDefinition> result = (Set<BeanDefinition>) scan.invoke(scanner, ((Consumer) o).getPackageName());

            Iterator<BeanDefinition> i$ = result.iterator();
            while(i$.hasNext()){
                analyzeClass(null,i$.next().getBeanClassName(),registry,o);
            }

        }catch (Exception e){
            throw new HSFAnnotationHandlerException(e.toString(),e);
        }
    }

    @Override
    public void analyzeClass(String beanName, String clazzName, BeanDefinitionRegistry registry, Object consumer) throws Exception{
        Class<?> clazz = findClass(clazzName);
        Collection<Field> fields = obtainAllFileds(clazz, true);
        Iterator<Field> i$ = fields.iterator();
        while (i$.hasNext()){
            Field field = i$.next();
            if(!field.isAnnotationPresent(findAnnotation())){
                continue;
            }
            Reference reference = (Reference) field.getAnnotation(findAnnotation());
            RootBeanDefinition beanDef = new RootBeanDefinition();
            beanDef.setBeanClass(getSpringBeanClass());
            beanDef.setLazyInit(false);
            beanDef.getPropertyValues().addPropertyValue("interfaceName",field.getType().getName());
            batchResolveAnnotationAttributes(reference,beanDef,consumer,new String[]{"value"});
            String existBeanName = hasAdded(beanDef);
            if(existBeanName == null){
                String id = generateBeanName(generateReferenceBeanName(field.getType()),registry);
                if(reference.value() != null && !reference.value().trim().isEmpty()){
                    id = reference.value();
                }
                putConsumerBeanDefinitions(new ConsumerBeanDefinition(id,beanDef));
                BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDef, id);
                BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
                continue;
            }
        }
    }

    public void processAnnotation(Field field,String nameValue) throws Exception{
        if (field.isAnnotationPresent(Resource.class)){
            Resource resource = field.getAnnotation(Resource.class);
            reviseAnnotationValue(resource,"name",nameValue);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String[] cousumerBeanNames = beanFactory.getBeanNamesForType(getSpringBeanClass(),true,false);
        Iterator<String> i$ = Arrays.asList(cousumerBeanNames).iterator();
        while (i$.hasNext()){
            String cousumerBeanName = i$.next();
            if(cousumerBeanName.charAt(0) == '&'){
                cousumerBeanName = cousumerBeanName.substring(1);
            }
            BeanDefinition beanDef = beanFactory.getBeanDefinition(cousumerBeanName);
            putConsumerBeanDefinitions(new ConsumerBeanDefinition(cousumerBeanName,beanDef));
        }
    }

    public void putConsumerBeanDefinitions(ConsumerBeanDefinition consumerBeanDefinition){
        synchronized (consumerBeanDefinitions){
            String unique = consumerBeanDefinition.getUniqueIdentification();
            if(consumerBeanDefinitions.containsKey(unique)){
                throw new HSFAnnotationHandlerException(unique+" exist!");
            }
            consumerBeanDefinitions.put(unique,consumerBeanDefinition);
        }
    }

    @Override
    public String hasAdded(BeanDefinition beanDefinition){
        Iterator<String> i$ = Arrays.asList(eqaulsAttributes()).iterator();
        StringBuilder uniqueIdentification = new StringBuilder();
        while (i$.hasNext()){
            String attributes = i$.next();
            uniqueIdentification.append(beanDefinition.getPropertyValues().get(attributes)).append("&");
        }
        String id = uniqueIdentification.substring(0,uniqueIdentification.length()-1);
        synchronized (consumerBeanDefinitions){
            ConsumerBeanDefinition consumerBeanDefinition = consumerBeanDefinitions.get(id);
            if(consumerBeanDefinition != null){
                return consumerBeanDefinition.getBeanName();
            }
        }
        return null;
    }

    @Override
    public String generateBeanName(String beanName,BeanDefinitionRegistry registry){
        return HSFAnnotationUtil.generateBeanName(beanName,registry);
    }

    @Override
    public String getConstantBeanName() {
        return HSFConsumerAnnotationBeanDefinitionParser.CONSUMER_BEAN_NAME;
    }

    @Override
    public Class<?> getConstantBeanClass() {
        return Consumer.class;
    }

    @Override
    public Class<?> getSpringBeanClass() {
        return HSFSpringConsumerBean.class;
    }

    @Override
    public Class<? extends Annotation> findAnnotation() {
        return Reference.class;
    }

    @Override
    public String[] eqaulsAttributes() {
        return new String[]{"interfaceName","group","version"};
    }
}
