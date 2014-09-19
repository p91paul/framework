package applica.framework.security;

import applica.framework.data.Repository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsRepository {
    UserDetails getByMail(String mail);
}
