package applica.documentation.admin.facade;

import applica.documentation.domain.data.UsersRepository;
import applica.documentation.domain.model.Role;
import applica.documentation.domain.model.User;
import applica.framework.library.utils.ProgramException;
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
        user.setMail("admin@applicamobile.com");
        user.setPassword("applica");
        user.setUsername("administrator");
        user.setActive(true);

        Role role = new Role();
        role.setRole(Role.ADMIN);

        List<Role> roles = new ArrayList<>();
        roles.add(role);

        usersRepository.save(user);
    }

}
