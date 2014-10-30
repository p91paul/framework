package applica.framework.data.mongodb.Constraints;

import applica.framework.data.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 30/10/14
 * Time: 11:42
 */
public class SimpleConstraintsChecker implements ConstraintsChecker {

    @Autowired
    private ApplicationContext applicationContext;

    private List<Constraint> constraints;

    private List<Constraint> loadConstraints(Class<? extends Entity> type) {
        if (constraints == null) {
            constraints = new ArrayList<Constraint>(applicationContext.getBeansOfType(Constraint.class).values());
        }

        return constraints.stream().filter(c -> c.getType().equals(type)).collect(Collectors.toList());
    }

    @Override
    public void check(Entity entity) throws ConstraintException {
        Objects.requireNonNull(entity, "Entity cannot be null");
        List<Constraint> entityConstraints = loadConstraints(entity.getClass());
        for (Constraint constraint : entityConstraints) {
            constraint.check(entity);
        }
    }
}
