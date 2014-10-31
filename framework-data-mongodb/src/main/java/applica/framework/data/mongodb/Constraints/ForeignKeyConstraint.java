package applica.framework.data.mongodb.Constraints;

import applica.framework.data.*;
import applica.framework.utils.TypeUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;
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

    public RepositoriesFactory getRepositoriesFactory() {
        return repositoriesFactory;
    }

    public void setRepositoriesFactory(RepositoriesFactory repositoriesFactory) {
        this.repositoriesFactory = repositoriesFactory;
    }

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
    public void checkPrimary(T1 entity) throws ConstraintException {
        Objects.requireNonNull(entity, "Entity cannot be null");
        Objects.requireNonNull(getForeignProperty(), "Foreign property cannot be null");
        Objects.requireNonNull(getForeignType(), "Foreign type cannot be null");
        Object id = entity.getId();

        try {
            if (id != null) {
                Repository<? extends Entity> foreignRepository = repositoriesFactory.createForEntity(getForeignType());
                for (Entity foreignEntity : foreignRepository.find(LoadRequest.build()).getRows()) {
                    Class<?> foreignValueType = TypeUtils.getField(getForeignType(), getForeignProperty()).getType();
                    Object foreignValue = PropertyUtils.getProperty(foreignEntity, getForeignProperty());
                    if (foreignValue != null) {
                        if (TypeUtils.isList(foreignValueType)) {
                            Type listGenericType = TypeUtils.getFirstGenericArgumentType(foreignValueType);
                            if (getPrimaryType().equals(listGenericType)) {
                                List<? extends Entity> list = (List) foreignValue;
                                for (Entity el : list) {
                                    if (id.equals(el.getId())) {
                                        throw new ConstraintException(
                                                String.format("ForeignKey check failure: %s [%s] was found in %s.%s",
                                                        getPrimaryType().getName(),
                                                        el.getId(),
                                                        getForeignType().getName(),
                                                        getForeignProperty()
                                                )
                                        );
                                    }
                                }
                            } else if (String.class.equals(listGenericType) ||
                                    Integer.class.equals(listGenericType) ||
                                    Long.class.equals(listGenericType) ||
                                    Key.class.equals(listGenericType)) {
                                Object foreignId = Key.class.equals(foreignValueType) ? ((Key) foreignValue).getValue() : foreignValue;
                                if (id.equals(foreignId)) {
                                    throw new ConstraintException(
                                            String.format("ForeignKey check failure: %s [%s] was found in %s.%s",
                                                    getPrimaryType().getName(),
                                                    id,
                                                    getForeignType().getName(),
                                                    getForeignProperty()
                                            )
                                    );
                                }
                            }
                        }  else if (getPrimaryType().equals(foreignValueType)) {
                            Entity value = ((Entity) foreignValue);
                            if (value != null) {
                                if (id.equals(value.getId())) {
                                    throw new ConstraintException(
                                            String.format("ForeignKey check failure: %s [%s] was found in %s.%s",
                                                    getPrimaryType().getName(),
                                                    id,
                                                    getForeignType().getName(),
                                                    getForeignProperty()
                                            )
                                    );
                                }
                            }

                        } else if (String.class.equals(foreignValueType) ||
                                Integer.class.equals(foreignValueType) ||
                                Long.class.equals(foreignValueType) ||
                                Key.class.equals(foreignValueType)) {
                            Object foreignId = Key.class.equals(foreignValueType) ? ((Key) foreignValue).getValue() : foreignValue;
                            if (id.equals(foreignId)) {
                                throw new ConstraintException(
                                        String.format("ForeignKey check failure: %s [%s] was found in %s.%s",
                                                getPrimaryType().getName(),
                                                id,
                                                getForeignType().getName(),
                                                getForeignProperty()
                                        )
                                );
                            }
                        }
                    }
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if primary entity id stored in foreign property exists
     * @param foreignEntity
     */
    @Override
    public void checkForeign(T2 foreignEntity) throws ConstraintException {
        Objects.requireNonNull(foreignEntity, "Entity cannot be null");
        Objects.requireNonNull(getPrimaryType(), "Primary type cannot be null");
        Objects.requireNonNull(getForeignProperty(), "Foreign property cannot be null");
        Objects.requireNonNull(getForeignType(), "Foreign type cannot be null");

        try {
            Class<?> foreignValueType = TypeUtils.getField(getForeignType(), getForeignProperty()).getType();
            Object foreignValue = PropertyUtils.getProperty(foreignEntity, getForeignProperty());
            if (foreignValue != null) {
                if (TypeUtils.isList(foreignValueType)) {
                    Type listGenericType = TypeUtils.getFirstGenericArgumentType(foreignValueType);
                    if (getPrimaryType().equals(listGenericType)) {
                        Repository<? extends Entity> repository = repositoriesFactory.createForEntity(getPrimaryType());
                        List<? extends Entity> list = (List) foreignValue;
                        for (Entity el : list) {
                            repository.get(el.getId()).orElseThrow(() -> new ConstraintException(
                                    String.format("ForeignKey check failure: %s.%s [%s] was found in %s",
                                            getForeignType().getName(),
                                            getForeignProperty(),
                                            el.getId(),
                                            getPrimaryType().getName()
                                    )
                            ));
                        }
                    } else if (String.class.equals(listGenericType) ||
                            Integer.class.equals(listGenericType) ||
                            Long.class.equals(listGenericType) ||
                            Key.class.equals(listGenericType)) {
                        Repository<? extends Entity> repository = repositoriesFactory.createForEntity(getPrimaryType());
                        List<?> list = (List) foreignValue;
                        for (Object possibleId : list) {
                            Object id = Key.class.equals(listGenericType) ? ((Key) possibleId).getValue() : possibleId;
                            repository.get(id).orElseThrow(() -> new ConstraintException(
                                    String.format("ForeignKey check failure: %s.%s [%s] was found in %s",
                                            getForeignType().getName(),
                                            getForeignProperty(),
                                            id,
                                            getPrimaryType().getName()
                                    )
                            ));
                        }
                    }
                } else if (getPrimaryType().equals(foreignValueType)) {
                    Repository<? extends Entity> repository = repositoriesFactory.createForEntity(getPrimaryType());
                    Entity value = ((Entity) foreignValue);
                    if (value != null) {
                        repository.get(value.getId()).orElseThrow(() -> new ConstraintException(
                                String.format("ForeignKey check failure: %s.%s [%s] was found in %s",
                                        getForeignType().getName(),
                                        getForeignProperty(),
                                        value.getId(),
                                        getPrimaryType().getName()
                                )
                        ));
                    }
                } else if (String.class.equals(foreignValueType) ||
                        Integer.class.equals(foreignValueType) ||
                        Long.class.equals(foreignValueType) ||
                        Key.class.equals(foreignValueType)) {
                    Object id = Key.class.equals(foreignValueType) ? ((Key) foreignValue).getValue() : foreignValue;
                    Repository<? extends Entity> repository = repositoriesFactory.createForEntity(getPrimaryType());
                    repository.get(id).orElseThrow(() -> new ConstraintException(
                            String.format("ForeignKey check failure: %s.%s [%s] was found in %s",
                                    getForeignType().getName(),
                                    getForeignProperty(),
                                    id,
                                    getPrimaryType().getName()
                            )
                    ));
                }
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
