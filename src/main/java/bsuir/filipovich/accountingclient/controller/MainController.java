package bsuir.filipovich.accountingclient.controller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"SameReturnValue", "ConstantConditions"})
@Controller
public class MainController {
    private String send(String url) {
        HttpClient client = new HttpClient();
        HttpMethod method = null;
        try {
            String part1 = url.substring(0, url.indexOf('?') + 1);
            String part2 = URIUtil.encodePath(url.substring(url.indexOf('?') + 1), "UTF-8");
            part2 = part2.replace("%2500", "%00");
            String fullURL = "http://localhost:9966/accounting-server" + part1 + part2;
            method = new GetMethod(fullURL);
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                return content.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (method != null)
                method.releaseConnection();
        }
        return null;
    }

    private String getOne(String value) {
        if (value.equals(""))
            return null;
        return value;
    }

    private String[] getArr(String value) {
        if (value == null || value.equals(""))
            return new String[0];
        String debracketed = value.replace("[", "").replace("]", "").replace("\\\"", "\"");
        ArrayList<String> list = new ArrayList<>(Arrays.asList(debracketed.split("\",\"")));
        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            if (i == 0)
                list.set(i, item.substring(1));
            if (i == list.size() - 1)
                list.set(i, item.substring(0, item.length() - 1));
        }
        return list.toArray(new String[0]);
    }

    private String[][] getDArr(String values) {
        if (values == null || values.equals("[]") || values.equals(""))
            return new String[][]{};
        ArrayList<String[]> list = new ArrayList<>();
        for (String value : values.split("],\\[")) {
            list.add(getArr(value));
        }
        return list.toArray(new String[0][]);
    }

    private String toArr(String[] what) {
        StringBuilder strbldr = new StringBuilder();
        for (int i = 0; i < what.length; i++) {
            strbldr.append("strings");
            strbldr.append('=');
            strbldr.append(what[i] != null ? what[i] : "%00");
            if (i < what.length - 1)
                strbldr.append('&');
        }
        return strbldr.toString();
    }

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public ModelAndView index(Model model,
                              @RequestParam(required = false) String loginLogin,
                              @RequestParam(required = false) String loginPassword,
                              @RequestParam(required = false) String loginOperation) {
        if (loginLogin != null && loginPassword != null && loginOperation != null && loginOperation.equals("Вход")) {
            send("/login?login=" + loginLogin + "&password=" + loginPassword);
        }
        if (loginOperation != null && loginOperation.equals("Выход")) {
            send("/logout");
        }
        if (send("/getLoggedUser").equals(""))
            model.addAttribute("loginStatus", "notLogged");
        else {
            model.addAttribute("loginStatus", getArr(send("/getLoggedUser"))[4]);
            String s = getArr(send("/getLoggedUser"))[0];
            model.addAttribute("loggedUser", new ArrayList<>(Arrays.asList(getArr(send("/readOne?type=user&id=" + getArr(send("/getLoggedUser"))[0])))));
        }
        return new ModelAndView("index");
    }

    @RequestMapping(value = {"/users"}, method = RequestMethod.GET)
    public ModelAndView viewUserList(Model model,
                                     @RequestParam(required = false) String id,
                                     @RequestParam(required = false) String surname,
                                     @RequestParam(required = false) String operation,
                                     @RequestParam(required = false) String forename,
                                     @RequestParam(required = false) String patronymic,
                                     @RequestParam(required = false) String role,
                                     @RequestParam(required = false) String login) {
        if (getArr(send("/getLoggedUser")) == null || !getArr(send("/getLoggedUser"))[4].equals("Заведующий"))
            return new ModelAndView(new RedirectView("/index", true));
        if (operation != null) {
            switch (operation) {
                case "Добавить":
                    send("/create?type=user&" + toArr(new String[]{id, surname, forename, patronymic, role, login, "password"}));
                    break;
                case "Сохранить":
                    send("/update?type=user&" + toArr(new String[]{id, surname, forename, patronymic, role, login, "password"}));
                    break;
                case "Удалить":
                    send("/remove?type=user&id=" + id);
                    break;
            }
        }
        String[][] userList = getDArr(send("/readAll?type=user"));
        model.addAttribute("userList", new ArrayList<String[]>(Arrays.asList(userList)));
        return new ModelAndView("users");
    }

    @RequestMapping(value = {"/stores"}, method = RequestMethod.GET)
    public ModelAndView viewStoreList(Model model,
                                      @RequestParam(required = false) String id,
                                      @RequestParam(required = false) String region,
                                      @RequestParam(required = false) String city,
                                      @RequestParam(required = false) String street,
                                      @RequestParam(required = false) String number,
                                      @RequestParam(required = false) String building,
                                      @RequestParam(required = false) String operation) {
        if (getArr(send("/getLoggedUser")) == null)
            return new ModelAndView(new RedirectView("/index", true));
        if (getArr(send("/getLoggedUser"))[4].equals("Заведующий")) {
            model.addAttribute("role", "Заведующий");
        } else {
            model.addAttribute("role", "Кассир");
        }
        if (operation != null) {
            switch (operation) {
                case "Добавить":
                    send("/create?type=store&" + toArr(new String[]{id, region, city, street, number, building}));
                    break;
                case "Сохранить":
                    send("/update?type=store&" + toArr(new String[]{id, region, city, street, number, building}));
                    break;
                case "Удалить":
                    send("/remove?type=store&id=" + id);
                    break;
            }
        }
        model.addAttribute("storeList", new ArrayList<>(Arrays.asList(getDArr(send("/readAll?type=store")))));
        return new ModelAndView("stores");
    }

    @RequestMapping(value = {"/store"}, method = RequestMethod.GET)
    public ModelAndView viewStore(Model model,
                                  @RequestParam(required = false) String id,
                                  @RequestParam(required = false) String operation,
                                  @RequestParam(required = false) String product,
                                  @RequestParam(required = false) String quantity) {
        model.addAttribute("storeId", id);
        if (operation != null && operation.equals("Установить")) {
            send("/setManyToMany?type=assortment&root=" + id + "&product=" + product.substring(0, product.indexOf('-') - 1) + "&quantity=" + quantity.replace(",", "."));
        }
        String[][] s = getDArr(send("/readAllById?type=assortment&id=" + id));
        ArrayList<String[]> rs = new ArrayList<>();
        for (String[] item : s) {
            String[] pr = getArr(send("/readOne?type=product&id=" + item[2]));
            rs.add(new String[]{pr[0] + " - " + pr[1], item[3]});
        }
        model.addAttribute("assortmentList", rs);

        ArrayList<String> productArr = new ArrayList<>();
        for (String[] row : getDArr(send("/readAll?type=product"))) {
            productArr.add(row[0] + " - " + row[1]);
        }
        model.addAttribute("productList", productArr);

        List<String> storeInfo = Arrays.asList(getArr(send("/readOne?type=store&id=" + id)));
        model.addAttribute("storeInfo", storeInfo);
        return new ModelAndView("store");
    }

    @RequestMapping(value = {"/transactions"}, method = RequestMethod.GET)
    public ModelAndView viewTransactionList(Model model,
                                            @RequestParam(required = false) String id,
                                            @RequestParam(required = false) String store,
                                            @RequestParam(required = false) String user,
                                            @RequestParam(required = false) String date,
                                            @RequestParam(required = false) String operation) {
        if (getArr(send("/getLoggedUser")) == null)
            return new ModelAndView(new RedirectView("/index", true));
        if (operation != null) {
            switch (operation) {
                case "Добавить":
                    send("/create?type=transaction&" + toArr(new String[]{id, store.substring(0, store.indexOf('-') - 1), user.substring(0, user.indexOf('-') - 1), date.replace('T', ' ')}));
                    break;
                case "Сохранить":
                    send("/update?type=transaction&" + toArr(new String[]{id, store.substring(0, store.indexOf('-') - 1), user.substring(0, user.indexOf('-') - 1), date.replace('T', ' ')}));
                    break;
                case "Удалить":
                    send("/remove?type=transaction&id=" + id);
                    break;
            }
        }
        String[][] t = getDArr(send("/readAll?type=transaction"));
        ArrayList<String[]> rs = new ArrayList<>();
        for (String[] strings : t) {
            String[] s = getArr(send("/readOne?type=store&id=" + strings[1]));
            String[] u = getArr(send("/readOne?type=user&id=" + strings[2]));
            StringBuilder strbldr = new StringBuilder();
            strbldr.append(s[0]);
            strbldr.append(" - ");
            strbldr.append(s[2]);
            strbldr.append(' ');
            strbldr.append(s[3]);
            strbldr.append(' ');
            strbldr.append(s[4]);
            if (!s[5].equals("")) {
                strbldr.append('/');
                strbldr.append(s[5]);
            }
            String sstring = strbldr.toString();
            strbldr.setLength(0);
            strbldr.append(u[0]);
            strbldr.append(" - ");
            strbldr.append(u[1]);
            strbldr.append(' ');
            strbldr.append(u[2].charAt(0));
            strbldr.append('.');
            if (!u[3].equals("")) {
                strbldr.append(' ');
                strbldr.append(u[3].charAt(0));
                strbldr.append('.');
            }
            rs.add(new String[]{strings[0], sstring, strbldr.toString(), strings[3]});
        }
        model.addAttribute("transactionList", rs);

        ArrayList<String> stores = new ArrayList<>();
        for (String[] row : getDArr(send("/readAll?type=store"))) {
            stores.add(row[0] + " - " + row[2] + ' ' + row[3] + ' ' + row[4] + (row[5].equals("") ? "" : '/' + row[5]));
        }
        model.addAttribute("storeList", stores);

        ArrayList<String> users = new ArrayList<>();
        for (String[] row : getDArr(send("/readAll?type=user"))) {
            users.add(row[0] + " - " + row[1] + " " + row[2].charAt(0) + (row[2].length() > 1 ? ". " : ' ') + row[3].charAt(0) + (row[3].length() > 1 ? ". " : ""));
        }
        model.addAttribute("userList", users);
        return new ModelAndView("transactions");
    }

    @RequestMapping(value = {"/transaction"}, method = RequestMethod.GET)
    public ModelAndView viewTransaction(Model model,
                                        @RequestParam(required = false) String id,
                                        @RequestParam(required = false) String operation,
                                        @RequestParam(required = false) String product,
                                        @RequestParam(required = false) String quantity) {
        model.addAttribute("transactionId", id);
        if (operation != null && operation.equals("Установить")) {
            send("/setManyToMany?type=entry&root=" + id + "&product=" + product.substring(0, product.indexOf('-') - 1) + "&quantity=" + quantity.replace(",", "."));
        }
        String[][] s = getDArr(send("/readAllById?type=entry&id=" + id));
        ArrayList<String[]> rs = new ArrayList<>();
        for (String[] item : s) {
            String[] pr = getArr(send("/readOne?type=product&id=" + item[2]));
            rs.add(new String[]{pr[0] + " - " + pr[1], item[3]});
        }
        model.addAttribute("entryList", rs);
        ArrayList<String> productArr = new ArrayList<>();
        for (String[] row : getDArr(send("/readAll?type=product"))) {
            productArr.add(row[0] + " - " + row[1]);
        }
        model.addAttribute("productList", productArr);
        String[] transaction = getArr(send("/readOne?type=transaction&id=" + id));
        List<String> transactionInfo = new ArrayList<>();
        transactionInfo.add(transaction[0]);
        String[] store = getArr(send("/readOne?type=store&id=" + transaction[1]));
        transactionInfo.add("Магазин №" + store[0] + " по адресу " + (!store[1].equals("") ? (store[1] + ' ') : "" + store[2] + ' ' + store[3] + ' ' + store[4]));
        String[] user = getArr(send("/readOne?type=user&id=" + transaction[1]));
        transactionInfo.add(user[4] + ' ' + user[1] + ' ' + user[2] + (!user[3].equals("") ? ' ' + user[3] : ""));
        transactionInfo.add(transaction[3]);
        model.addAttribute("transactionInfo", transactionInfo);
        return new ModelAndView("transaction");
    }

    @RequestMapping(value = {"/products"}, method = RequestMethod.GET)
    public ModelAndView viewProductList(Model model,
                                        @RequestParam(required = false) String id,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) String sellingPrice,
                                        @RequestParam(required = false) String description,
                                        @RequestParam(required = false) String operation) {
        if (operation != null) {
            switch (operation) {
                case "Добавить":
                    send("/create?type=product&" + toArr(new String[]{id, name, sellingPrice, description}));
                    break;
                case "Сохранить":
                    send("/update?type=product&" + toArr(new String[]{id, name, sellingPrice, description}));
                    break;
                case "Удалить":
                    send("/remove?type=product&id=" + id);
                    break;
            }
        }
        model.addAttribute("productList", new ArrayList<>(Arrays.asList(getDArr(send("/readAll?type=product")))));
        model.addAttribute("loginStatus", getArr(send("/getLoggedUser"))[4]);
        return new ModelAndView("products");
    }

    @RequestMapping(value = {"/product"}, method = RequestMethod.GET)
    public ModelAndView viewProductList(Model model,
                                        @RequestParam(required = true) String id) {
        model.addAttribute("productInfo", new ArrayList<>(Arrays.asList(getArr("/readOne?type=product&id=" + id))));
        String[] exist = getArr(send("/showWhere?id=" + id));
        ArrayList<String[]> arr = new ArrayList<>();
        for (String storeId: exist){
            String[] row = getArr(send("/readOne?type=store&id=" + storeId));
            arr.add(new String[]{row[0] + " - " + row[2] + ' ' + row[3] + ' ' + row[4] + (row[5].equals("") ? "" : '/' + row[5])});
        }
        model.addAttribute("storeList", arr);
        return new ModelAndView("product");
    }


    @RequestMapping(value = {"reports"}, method = RequestMethod.GET)
    public ModelAndView getReport(Model model,
                                  @RequestParam(required = false) String operation,
                                  @RequestParam(required = false) String dateFrom,
                                  @RequestParam(required = false) String dateTo,
                                  @RequestParam(required = false) String reportType,
                                  @RequestParam(required = false) String currency) {
        if (getArr(send("/getLoggedUser")) == null || !getArr(send("/getLoggedUser"))[4].equals("Заведующий"))
            return new ModelAndView(new RedirectView("/index", true));
        if (operation != null && dateFrom != null && dateTo != null && reportType != null) {
            String[][] report = null;
            StringBuilder strbldr = new StringBuilder();
            switch (reportType) {
                case "О магазинах":
                    strbldr.append("/getSalesByStoreReport");
                    model.addAttribute("headers", new ArrayList<String>() {
                        {
                            add("Магазин");
                            add("Выручка");
                            add("Доля от всей выручки");
                        }
                    });
                    break;
                case "О товарах":
                    strbldr.append("/getSalesByProductReport");
                    model.addAttribute("headers", new ArrayList<String>() {
                        {
                            add("Товар");
                            add("Количество");
                            add("Выручка");
                            add("Доля от всей выручки");
                        }
                    });
                    break;
                case "О сотрудниках":
                    strbldr.append("/getSalesByCashierReport");
                    model.addAttribute("headers", new ArrayList<String>() {
                        {
                            add("Кассир");
                            add("Транзакции");
                            add("Выручка");
                        }
                    });
                    break;
            }
            strbldr.append("?from=");
            strbldr.append(dateFrom.replace('T', ' '));
            strbldr.append("&to=");
            strbldr.append(dateTo.replace('T', ' '));
            String[][] darr = getDArr(send(strbldr.toString()));
            ArrayList<ArrayList<String>> rep = new ArrayList<>();
            for (String[] row : darr) {
                ArrayList<String> d = new ArrayList<>();
                switch (reportType) {
                    case "О магазинах":
                        String[] store = getArr(send("/readOne?type=store&id=" + row[0]));
                        d.add("Магазин №" + store[0] + " - " + (!store[1].equals("") ? (store[1] + ' ') : "" + store[2] + ' ' + store[3] + ' ' + store[4]));
                        break;
                    case "О товарах":
                        d.add(getArr(send("/readOne?type=product&id=" + row[0]))[1]);
                        break;
                    case "О сотрудниках":
                        String[] u = getArr(send("/readOne?type=user&id=" + row[0]));
                        String sstring = strbldr.toString();
                        strbldr.setLength(0);
                        strbldr.append(u[0]);
                        strbldr.append(" - ");
                        strbldr.append(u[1]);
                        strbldr.append(' ');
                        strbldr.append(u[2].charAt(0));
                        strbldr.append('.');
                        if (!u[3].equals("")) {
                            strbldr.append(' ');
                            strbldr.append(u[3].charAt(0));
                            strbldr.append('.');
                        }
                        d.add(strbldr.toString());
                        break;
                }
                switch (reportType) {
                    case "О магазинах":
                        if (!currency.equals("BYN"))
                            d.add(send("/toCur?value=" + row[1] + "&currency=" + currency));
                        else
                            d.add(row[1]);
                        d.add(row[2]);
                        break;
                    case "О товарах":
                        d.add(row[1]);
                        if (!currency.equals("BYN"))
                            d.add(send("/toCur?value=" + row[1] + "&currency=" + currency));
                        else
                            d.add(row[1]);
                        if (!currency.equals("BYN"))
                            d.add(send("/toCur?value=" + row[3] + "&currency=" + currency));
                        else
                            d.add(row[3]);
                        break;
                    case "О сотрудниках":
                        d.add(row[1]);
                        if (!currency.equals("BYN"))
                            d.add(send("/toCur?value=" + row[1] + "&currency=" + currency));
                        else
                            d.add(row[2]);
                        break;
                }
                rep.add(d);
            }
            model.addAttribute("reportrow", rep);
        } else {
            model.addAttribute("headers", new ArrayList<String>() {
            });
            model.addAttribute("reportrow", new ArrayList<String[]>());
        }
        return new ModelAndView("report");
    }

    @RequestMapping(value = {"test"}, method = RequestMethod.GET)
    public ModelAndView test(Model model) {
        return new ModelAndView("test");
    }
}