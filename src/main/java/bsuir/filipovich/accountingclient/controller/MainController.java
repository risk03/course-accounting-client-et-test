package bsuir.filipovich.accountingclient.controller;

import bsuir.filipovich.accountingclient.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

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
    public String viewUserList(Model model) {
        model.addAttribute("userList", service.getUserList());
        return "users";
    }
}