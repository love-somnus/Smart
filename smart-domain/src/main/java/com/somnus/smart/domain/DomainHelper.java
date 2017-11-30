package com.somnus.smart.domain;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public class DomainHelper {
    private static Logger logger = LoggerFactory.getLogger(DomainHelper.class);

    public static Object getDomainInstance(Class<?> clazz) {
        DomainProxy proxy = new DomainProxy();
        return proxy.getProxy(clazz);
    }

    public static void setModelData(Object model, Object domain) {
    	BeanUtils.copyProperties(domain, model);
    }

    public static void setDomainData(Object domain, Object model) {
    	BeanUtils.copyProperties(model, domain);
    }

    public static void setDomainData(Object domain, Object model, HashMap<String, String> propertyMap) {
        try {
            BeanUtils.copyProperties(domain, model);

            for (String key : propertyMap.keySet()) {
                Object value = PropertyUtils.getProperty(model, propertyMap.get(key));
                PropertyUtils.setProperty(domain, key, value);
            }
        } catch (NoSuchMethodException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (InvocationTargetException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public static Object addProxy(final Object domain) {

        Class<?> clazz = domain.getClass();
        
        Object proxy = getDomainInstance(clazz);
        
        BeanUtils.copyProperties(domain, proxy);

        return proxy;
    }

    public static Object removeProxy(final Object proxy) {
        Object domain;
        
        try {
            Class<?> clazz = proxy.getClass().getSuperclass();
            domain = clazz.newInstance();
            BeanUtils.copyProperties(proxy, domain);
        } catch (InstantiationException ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        } catch (IllegalAccessException ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }

        return domain;
    }
}