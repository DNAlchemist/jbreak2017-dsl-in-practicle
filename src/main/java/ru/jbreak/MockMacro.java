package ru.jbreak;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.mockito.Mockito;
import ru.trylogic.groovy.macro.runtime.Macro;
import ru.trylogic.groovy.macro.runtime.MacroContext;

import static org.codehaus.groovy.ast.tools.GeneralUtils.args;
import static org.codehaus.groovy.ast.tools.GeneralUtils.callX;
import static org.codehaus.groovy.ast.tools.GeneralUtils.closureX;

/**
 * @author Aleksey Dobrynin
 */
public class MockMacro {
    @Macro
    public static Expression Mock(MacroContext macroContext, ClassExpression classExpression) {
        return callX(new ClassNode(Mockito.class), "mock", args(classExpression));
    }

    @Macro
    public static Expression Mock(MacroContext macroContext, ClassExpression classExpression, ClosureExpression closureExpression) {
        StaticMethodCallExpression mock = (StaticMethodCallExpression) Mock(macroContext, classExpression);

        for (Statement statement : ((BlockStatement) closureExpression.getCode()).getStatements()) {
            String label = statement.getStatementLabel();

            StaticMethodCallExpression when = callX(new ClassNode(Mockito.class), "when", args(callX(mock, label)));

            ClosureExpression then = closureX(statement);
            MethodCallExpression result = callX(then, "call");

            callX(when, "thenReturn", result);
        }
        return mock;
    }
}
