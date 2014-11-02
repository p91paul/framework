package applica.framework.data;

import applica.framework.data.Entity;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 25/10/13
 * Time: 18:06
 */
public class SEntity implements StringIdEntity, Entity {

    protected String id;

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public void setId(Object id) {
        this.id = (String)id;
    }


    @Override
    public void setSid(String sid) {
        setId((String)sid);
    }

    @Override
    public String getSid() {
        return (String)getId();
    }
}
