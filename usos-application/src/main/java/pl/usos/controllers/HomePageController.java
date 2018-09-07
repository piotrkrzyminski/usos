package pl.usos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.usos.constants.ControllerConstants;

/**
 * Controller for home page.
 *
 * @author Piotr Krzyminski
 */
@Controller
@RequestMapping(value = "/")
public class HomePageController {

    @RequestMapping(method = RequestMethod.GET)
    public String getHome() {
        return ControllerConstants.Views.Pages.home;
    }
}
