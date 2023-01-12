package com.bobocode;

import com.bobocode.annotation.Trimmed;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * This is processor class implements {@link BeanPostProcessor}, looks for a beans where method parameters are marked with
 * {@link Trimmed} annotation, creates proxy of them, overrides methods and trims all {@link String} arguments marked with
 * {@link Trimmed}. For example if there is a string " Java   " as an input parameter it has to be automatically trimmed to "Java"
 * if parameter is marked with {@link Trimmed} annotation.
 * <p>
 * <p>
 * Note! This bean is not marked as a {@link Component} to avoid automatic scanning, instead it should be created in
 * {@link StringTrimmingConfiguration} class which can be imported to a {@link Configuration} class by annotation
 * {@link EnableStringTrimming}
 */
public class TrimmedAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final Map<String, Class<?>> beansToBeProxied = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (containsParametersAnnotatedWithTrimmed(bean)) {
            beansToBeProxied.put(beanName, bean.getClass());
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);

    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        var proxyToCreate = bean.getClass();
        if (proxyToCreate != null) {
            return createProxyObject(proxyToCreate);
        }
        return bean;
    }

    public Object createProxyObject(Class<?> beanType) {
        var enhancer = new Enhancer();
        enhancer.setSuperclass(beanType);

        MethodInterceptor methodInterceptor = (Object var1, Method method, Object[] args, MethodProxy proxy) -> {
            return proxy.invoke(var1, processParameters(method, args));

        };
        enhancer.setCallback(methodInterceptor);

        return beanType.cast(enhancer.create());
    }

    private Object[] processParameters(Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(Trimmed.class)) {
                args[i] = getTrimArg(args[i]);
            }
        }
        return args;

    }

    private static Object getTrimArg(Object arg) {
        if (arg instanceof String string) {
            return StringUtils.hasLength(string) ? string.trim() : string;
        }
        return arg;

    }

    private boolean containsParametersAnnotatedWithTrimmed(Object bean) {
        return Arrays.stream(bean.getClass().getDeclaredMethods())
                .flatMap(m -> Stream.of(m.getParameters()))
                .anyMatch(p -> p.isAnnotationPresent(Trimmed.class));
    }

}
