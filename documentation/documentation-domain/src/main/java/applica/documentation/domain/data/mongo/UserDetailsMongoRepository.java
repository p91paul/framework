package applica.documentation.domain.data.mongo;

import applica.documentation.domain.data.UsersRepository;
import applica.documentation.domain.model.Filters;
import applica.documentation.domain.model.User;
import applica.documentation.domain.model.UsersDetails;
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
        User user = usersRepository.find(LoadRequestBuilder.build().eq(Filters.USER_MAIL, mail)).getOne(User.class);
        if(user == null) {
            return null;
        }

        return new UsersDetails(user);
    }
}
