package ru.jbreak

import groovy.transform.CompileStatic
import groovyjarjarasm.asm.Opcodes
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

/**
 * Created by ruslanmikhalev on 04/01/17.
 */
@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class EntityTransformation implements ASTTransformation {

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        source.AST.classes[1].addField("executor", Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC, ClassHelper.make(QueryExecutor), null)
    }

}
