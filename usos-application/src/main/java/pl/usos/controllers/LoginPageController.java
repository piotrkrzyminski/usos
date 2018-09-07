package pl.usos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.usos.constants.ControllerConstants;

@Controller
@RequestMapping(value = "/login")
public class LoginPageController {

    @RequestMapping(method = RequestMethod.GET)
    public String getLogin() {
        return ControllerConstants.Views.Pages.Login;
    }
}
