package applica.framework;

import applica.framework.data.Entity;

public interface CrudConstants {
    public static final Class<? extends Entity> DEFAULT_ENTITY_TYPE = Entity.class;

    public static final int REPOSITORY = 1;
    public static final int GRIDRENDERER = 2;
}
