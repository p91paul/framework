package applica.documentation.admin.controllers;

import applica.documentation.domain.data.RolesRepository;
import applica.documentation.domain.model.Role;
import applica.framework.builders.LoadRequestBuilder;
import applica.framework.library.SimpleItem;
import applica.framework.library.responses.ValueResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 3/3/13
 * Time: 11:11 PM
 */
@Controller
@RequestMapping("/values")
public class ValuesController {

    @Autowired
    RolesRepository rolesRepository;

    @RequestMapping("/roles")
    public @ResponseBody ValueResponse roles(String keyword) {
        List<Role> roles = rolesRepository.find(
                LoadRequestBuilder.build()
                        .like("role", keyword)
        ).getRows(Role.class);

        return new ValueResponse(SimpleItem.createList(roles, "role", "role"));
    }

}
