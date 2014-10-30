package applica.framework.data.mongodb.Constraints;

import applica.framework.data.*;
import applica.framework.utils.TypeUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 30/10/14
 * Time: 11:49
 */
public abstract class ForeignKeyConstraint<T1 extends Entity, T2 extends Entity> implements ReferencedConstraint<T1, T2> {

    public enum Cascade {
        DELETE,
        CHECK
    }

    @Autowired
    private RepositoriesFactory repositoriesFactory;

    public Cascade getCascade() {
        return Cascade.CHECK;
    }

    /**
     * Check if primary key entity is used by some foreign key.
     * This check is commonly used in deletions
     * @param entity
     * @throws ConstraintException
     */
    @Override
    public void checkPrimary(Entity entity) throws ConstraintException {
        Objects.requireNonNull(entity, "Entity cannot be null");
        Objects.requireNonNull(getForeignProperty(), "Foreign property cannot be null");
        Objects.requireNonNull(getForeignType(), "Foreign type cannot be null");
        Repository foreignRepository = repositoriesFactory.createForEntity(getForeignType());
        Object id = entity.getId();
        if (id != null) {
            LoadResponse response = foreignRepository.find(LoadRequest.build().id(getForeignProperty(), id));

            if (getCascade() == Cascade.CHECK) {
                if (response.getTotalRows() > 0) {
                    throw new ConstraintException(
                            String.format("ForeignKey check failure: %s.%s [%s] was found in %s.%s",
                                    entity.getClass().getName(),
                                    getForeignProperty(),
                                    id,
                                    getForeignType().getName(),
                                    getForeignProperty()
                            )
                    );
                }
            } else if (getCascade() == Cascade.DELETE) {
                response.getRows().forEach(foreignRepository::delete);
            }
        }
    }

    /**
     * Check if primary entity id stored in foreign property exists
     * @param foreignEntity
     */
    @Override
    public void checkForeign(Entity foreignEntity) {
        Objects.requireNonNull(foreignEntity, "Entity cannot be null");
        Objects.requireNonNull(getForeignProperty(), "Foreign property cannot be null");
        Objects.requireNonNull(getForeignType(), "Foreign type cannot be null");
        Object foreignValue = null;

        try {
            Class<?> foreignValueType = TypeUtils.getField(getForeignType(), getForeignProperty()).getType();
            foreignValue = PropertyUtils.getProperty(foreignEntity, getForeignProperty());
            if (foreignValue != null) {
                if (TypeUtils.isRelatedField(foreignEntity.getClass(), getForeignProperty())) {
                    if (TypeUtils.isList(foreignValueType)) {
                        if (TypeUtils.isEntity(TypeUtils.getFirstGenericArgumentType(foreignValueType))) {

                        }
                    }
                } else {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
