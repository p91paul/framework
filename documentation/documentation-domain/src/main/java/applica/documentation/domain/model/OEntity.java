package applica.documentation.domain.model;

import applica.framework.data.Entity;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 11/09/14
 * Time: 18:57
 */
public class OEntity implements Entity {

    private Object id;

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public void setId(Object id) {
        this.id = id;
    }
}
