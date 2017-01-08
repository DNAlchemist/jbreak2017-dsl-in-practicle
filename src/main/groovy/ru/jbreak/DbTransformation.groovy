package ru.jbreak

import groovyjarjarasm.asm.Opcodes
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

/**
 * Created by ruslanmikhalev on 05/01/17.
 */
@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class DbTransformation implements ASTTransformation {

    @Override
    void visit(ASTNode[] astNodes, SourceUnit source) {
        if (!astNodes) return

        AnnotationNode annotation = (AnnotationNode)astNodes[0];
        ClassNode classNode = (ClassNode)astNodes[1]

        final String url = annotation.getMember("value").text
        def urlExpr = new ConstantExpression(url);
        def queryExecutorExpr = new ConstructorCallExpression(ClassHelper.make(JdbcQueryExecutor.class), urlExpr)
        def dbExpr = new ConstructorCallExpression(ClassHelper.make(Database.class), queryExecutorExpr)
        classNode.addField("db", Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL, ClassHelper.make(Database.class), dbExpr)
    }

}
