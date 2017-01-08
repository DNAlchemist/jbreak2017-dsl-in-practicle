package ru.jbreak

import groovy.transform.CompileStatic
import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Created by ruslanmikhalev on 05/01/17.
 */
@Retention (RetentionPolicy.SOURCE)
@Target([ElementType.TYPE])
@GroovyASTTransformationClass(["ru.jbreak.DbTransformation"])
@CompileStatic
public @interface Db {
    String value()
}
