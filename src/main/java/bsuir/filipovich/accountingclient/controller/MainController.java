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

    private IService service;

    @Autowired
    MainController(IService service) {
        this.service = service;
    }

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model) {
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
        model.addAttribute("userList", service.readAll("user"));
        if (operation != null) {
            switch (operation) {
                case "Добавить":
                    service.create("user", new String[]{id, surname, forename, patronymic, role, login, "4138cb2035b332f60280335e2a3e8a37", "f8feb0246fc4889a02a56291eee6f08a"});
                    break;
                case "Сохранить":
                    service.update("user", new String[]{id, surname, forename, patronymic, role, login, null, null});
                    break;
                case "Удалить":
                    service.remove("user", id);
                    break;
            }
        }
        return "users";
    }

    @RequestMapping(value = {"/stores"}, method = RequestMethod.GET)
    public String viewStoreList(Model model,
                                @RequestParam(required = false) String id,
                                @RequestParam(required = false) String region,
                                @RequestParam(required = false) String city,
                                @RequestParam(required = false) String street,
                                @RequestParam(required = false) String number,
                                @RequestParam(required = false) String building,
                                @RequestParam(required = false) String operation) {
        model.addAttribute("storeList", service.readAll("store"));
        if (operation != null) {
            switch (operation) {
                case "Добавить":
                    service.create("store", new String[]{id, region, city, street, number, building});
                    break;
                case "Сохранить":
                    service.update("store", new String[]{id, region, city, street, number, building});
                    break;
                case "Удалить":
                    service.remove("store", id);
                    break;
            }
        }
        return "stores";
    }

    @RequestMapping(value = {"/products"}, method = RequestMethod.GET)
    public String viewProductList(Model model,
                                @RequestParam(required = false) String id,
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false) String sellingPrice,
                                @RequestParam(required = false) String description,
                                @RequestParam(required = false) String operation) {
        model.addAttribute("productList", service.readAll("product"));
        if (operation != null) {
            switch (operation) {
                case "Добавить":
                    service.create("product", new String[]{id, name, sellingPrice, description});
                    break;
                case "Сохранить":
                    service.update("product", new String[]{id, name, sellingPrice, description});
                    break;
                case "Удалить":
                    service.remove("product", id);
                    break;
            }
        }
        return "products";
    }
}