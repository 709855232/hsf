package com.weidiango.spring.util;

import java.util.HashMap;
import java.util.Map;

/**
 @author mht
 @date 2019/9/17 21:19 */
public class AliasMappingRegistry {

    private static final Map<String,Object> map = new HashMap<>();

    public static final String ALIAS_REQUIRED_SUFFIX = "Required";

    public static final String ALIAS_PROVIDER_SUFFIX = "Provider";

    static {
        registryAlias("groupProvider","serviceGroup");
        registryAlias("versionProvider","serviceVersion");
        registryAlias("package","packageName");
        registryAlias("versionRequired",true);
        registryAlias("groupRequired",true);
    }

    public static void registryAlias(String key,Object value){
        synchronized (map){
            if(!map.containsKey(key)){
                map.put(key,value);
            }
        }
    }

    public static Object resolveAlias(String key){
        Object o = map.get(key);
        if(o == null){
            return key;
        }
        return o;
    }

    public static Object resolveRequiredAlias(String alias){
        return resolveAlias(alias + ALIAS_REQUIRED_SUFFIX);
    }

    public static Object resolveProviderAlias(String alias,Class<?> clazz){
        String newAlias = alias + ALIAS_PROVIDER_SUFFIX;
        if(clazz == Provider.class){
            Object o = resolveAlias(newAlias);
            if(o instanceof String && newAlias.equals(o)){
                return alias;
            }
            return o;
        }
        return resolveAlias(alias);
    }


}
