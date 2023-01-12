package com.bobocode.config;

import com.bobocode.TrimmedAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;

public class StringTrimmingConfiguration {

    @Bean
    public TrimmedAnnotationBeanPostProcessor postProcessorBeanMethod(){
        return new TrimmedAnnotationBeanPostProcessor();
    }
}
