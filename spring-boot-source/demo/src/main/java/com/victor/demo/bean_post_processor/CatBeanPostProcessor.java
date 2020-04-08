package com.victor.demo.bean_post_processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CatBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof Cat) {
            System.out.println("Cat postProcessBeforeInitialization run...");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof Cat) {
            System.out.println("Cat posProcessAfterInitialization run..");
            Cat cat = (Cat)bean;
            cat.name = "dog";
        }
        return bean;
    }
}
