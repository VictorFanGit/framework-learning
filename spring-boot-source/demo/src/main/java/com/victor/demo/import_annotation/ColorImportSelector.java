package com.victor.demo.import_annotation;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 该类（ColorImportSelector）不会被注册进IOC容器
 */
public class ColorImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{Blue.class.getName(), Green.class.getName()};
    }
}
