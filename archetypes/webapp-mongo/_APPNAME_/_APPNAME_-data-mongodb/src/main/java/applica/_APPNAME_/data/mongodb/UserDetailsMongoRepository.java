package applica._APPNAME_.data.mongodb;

import applica._APPNAME_.domain.data.UsersRepository;
import applica._APPNAME_.domain.model.Filters;
import applica._APPNAME_.domain.model.User;
import applica._APPNAME_.domain.model.UsersDetails;
import applica.framework.builders.LoadRequestBuilder;
import applica.framework.security.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 2/26/13
 * Time: 6:18 PM
 */
@Repository
public class UserDetailsMongoRepository implements UserDetailsRepository {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails getByMail(String mail) {
        try {
            return usersRepository
                    .find(LoadRequestBuilder.build().eq(Filters.USER_MAIL, mail))
                    .findFirst()
                    .map(UsersDetails::new)
                    .orElse(null);
        } catch(Throwable t) {
            t.printStackTrace();
        }

        return null;
    }
}