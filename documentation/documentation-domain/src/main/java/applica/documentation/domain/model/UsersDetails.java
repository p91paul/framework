package applica.documentation.domain.model;

import applica.framework.data.StringIdEntity;
import applica.framework.security.UserDetailsImpl;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 31/10/13
 * Time: 13:31
 */
public class UsersDetails extends UserDetailsImpl implements StringIdEntity {

    private String id;

    public UsersDetails(applica.framework.security.User user) {
        super(user);
    }

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
        this.id = sid;
    }

    @Override
    public String getSid() {
        return this.id;
    }
}
