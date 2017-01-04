package ru.jbreak
/**
 * Created by ruslanmikhalev on 04/01/17.
 */
import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention (RetentionPolicy.SOURCE)
@Target([ElementType.TYPE])
@GroovyASTTransformationClass(["ru.jbreak.EntityTransformation"])
public @interface Entity {

}