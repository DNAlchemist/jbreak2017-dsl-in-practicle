package ru.jbreak;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.Expression;
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
        return Mock(macroContext, classExpression, null);
    }

    @Macro
    public static Expression Mock(MacroContext macroContext, ClassExpression classExpression, ClosureExpression closureExpression) {
        StaticMethodCallExpression mock = callX(new ClassNode(Mockito.class), "mock", args(classExpression));

//        Service serv = Mockito.mock(Service.class);
//        Mockito.when(serv.getId()).thenReturn(1);

        for (Statement statement : ((BlockStatement) closureExpression.getCode()).getStatements()) {
            String label = statement.getStatementLabel();

            StaticMethodCallExpression when = callX(new ClassNode(Mockito.class), "when", args(callX(mock, label)));

            BlockStatement closureBlockStatement = new BlockStatement();
            closureBlockStatement.addStatement(statement);
            ClosureExpression result = closureX(closureBlockStatement);
            result.setVariableScope(closureExpression.getVariableScope().copy());

            callX(when, "thenReturn", callX(result, "call"));
        }
        return mock;
    }

}
