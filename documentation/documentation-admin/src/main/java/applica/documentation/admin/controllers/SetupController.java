package applica.documentation.admin.controllers;

import applica.documentation.admin.facade.SetupFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 03/02/14
 * Time: 17:32
 */
@Controller
@RequestMapping("/setup")
public class SetupController {

    @Autowired
    private SetupFacade setupFacade;

    @RequestMapping("/admin")
    public @ResponseBody String admin() {
        setupFacade.createAdmin(false);
        return "OK";
    }

}
