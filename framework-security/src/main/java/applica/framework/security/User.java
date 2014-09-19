package applica.framework.security;

import applica.framework.data.Entity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface User {

    String getUsername();
    String getPassword();
    boolean isActive();
    Date getRegistrationDate();
    List<? extends Role> getRoles();


}
