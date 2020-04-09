package bsuir.filipovich.accountingclient.controller;

import bsuir.filipovich.accountingclient.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    IService service;

    @Autowired
    MainController(IService service) {
        this.service = service;
    }

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("message", "Hello Spring Boot + JSP");
        return "index";
    }

    @RequestMapping(value = {"/users"}, method = RequestMethod.GET)
    public String viewUserList(Model model,
                               @RequestParam(required = false) String id,
                               @RequestParam(required = false) String surname,
                               @RequestParam(required = false) String operation,
                               @RequestParam(required = false) String forename,
                               @RequestParam(required = false) String patronymic,
                               @RequestParam(required = false) String role,
                               @RequestParam(required = false) String login) {
        model.addAttribute("userList", service.getUserList());
        if (operation == null) {
            System.out.println("Just Check-in");
        } else if (operation.equals("Добавить")) {
            service.create("user", new String[]{id, surname, forename, patronymic, role, login, "4138cb2035b332f60280335e2a3e8a37", "f8feb0246fc4889a02a56291eee6f08a"});
        } else if (operation.equals("Сохранить")) {
            service.update("user", new String[]{id, surname, forename, patronymic, role, login, null, null});
        } else if (operation.equals("Удалить")) {
            service.remove("user", id);
        }
        return "users";
    }
}