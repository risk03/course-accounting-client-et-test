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
                               @RequestParam(required = false) Integer id,
                               @RequestParam(required = false) String surname,
                               @RequestParam(required = false) String add,
                               @RequestParam(required = false) String save,
                               @RequestParam(required = false) String delete,
                               @RequestParam(required = false) String forename,
                               @RequestParam(required = false) String patronymic,
                               @RequestParam(required = false) String role,
                               @RequestParam(required = false) String login) {
        model.addAttribute("userList", service.getUserList());
        if (add != null){
            System.out.println("Надо записать");
        }
        else if (save != null){
            System.out.println("Надо перезаписать");
        }
        else if (delete != null){
            System.out.println("Надо удалить");
        }
        else{
            System.out.println("Just Check-in");
        }
        return "users";
    }
}