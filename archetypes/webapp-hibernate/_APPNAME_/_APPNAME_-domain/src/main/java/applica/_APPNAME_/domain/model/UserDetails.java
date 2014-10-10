package applica._APPNAME_.domain.model;

import applica.framework.data.SEntity;
import applica.framework.data.StringIdEntity;
import applica.framework.data.hibernate.annotations.IgnoreMapping;
import applica.framework.security.*;
import org.apache.http.entity.StringEntity;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 31/10/13
 * Time: 13:31
 */
@IgnoreMapping
public class UserDetails extends UserDetailsImpl implements StringIdEntity {

    private String id;

    public UserDetails(applica.framework.security.User user) {
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
