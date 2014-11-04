package applica._APPNAME_.admin.facade;

import applica._APPNAME_.domain.data.UsersRepository;
import applica._APPNAME_.domain.model.Role;
import applica._APPNAME_.domain.model.User;
import applica.framework.library.utils.ProgramException;
import applica.framework.security.authorization.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 03/02/14
 * Time: 17:29
 */
@Component
public class SetupFacade {

    @Autowired
    private UsersRepository usersRepository;

    public void createAdmin(boolean check) {

        if(check) {
            if(usersRepository.find(null).getTotalRows() > 0) {
                throw new ProgramException("Admin user already created");
            }
        }

        User user = new User();
        user.setMail("admin@applicadoit.com");
        user.setPassword("applica");
        user.setUsername("administrator");
        user.setActive(true);

        Role role = new Role();
        role.setRole(Role.ADMIN);
        role.setPermissions(Permissions.instance().allPermissions());

        List<Role> roles = new ArrayList<>();
        roles.add(role);

        usersRepository.save(user);
    }

}
