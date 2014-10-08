package applica.documentation.admin.acl;

import applica.framework.library.crud.acl.CrudAuthorizationException;
import applica.framework.library.crud.acl.CrudGuard;
import applica.framework.library.crud.acl.CrudSecurityConfigurer;
import applica.framework.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 03/02/14
 * Time: 15:41
 */
@Component
public class ExpressionsCrudGuard implements CrudGuard {

    @Autowired
    private Security security;

    @Override
    public void check(String crudPermission, String entity) throws CrudAuthorizationException {
        String expression = CrudSecurityConfigurer.instance().getExpression(entity, crudPermission);
        if(StringUtils.hasLength(expression)) {
            ExpressionParser parser = new SpelExpressionParser();
            Expression expr = parser.parseExpression(expression);
            EvaluationContext context = new StandardEvaluationContext(security);
            boolean value = (boolean) expr.getValue(context);
            if(!value) {
                throw new CrudAuthorizationException();
            }
        }
    }
}
