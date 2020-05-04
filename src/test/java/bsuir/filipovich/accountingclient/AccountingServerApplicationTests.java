package bsuir.filipovich.accountingclient;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings({"unused", "ConstantConditions"})
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class AccountingServerApplicationTests {
    private String send(String url) {
        HttpClient client = new HttpClient();
        HttpMethod method = null;
        try {
            String part1 = url.substring(0, url.indexOf('?') + 1);
            String part2 = URIUtil.encodePath(url.substring(url.indexOf('?') + 1), "UTF-8");
            part2 = part2.replace("%2500", "%00");
            String fullURL = "http://acc-server:9967/accounting-server" + part1 + part2;
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

    String[][] getDArr(String values) {
        if (values == null || values.equals("[]") || values.equals(""))
            return new String[][]{};
        ArrayList<String[]> list = new ArrayList<>();
        for (String value : values.split("],\\[")) {
            list.add(getArr(value));
        }
        return list.toArray(new String[0][]);
    }

    String toArr(String[] what) {
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

    private final String[] DEFAULT_ADMIN = new String[]{"1", "Фамилия", "Имя", "Отчество", "Заведующий", "admin", "password"};
    private final String ADMIN_LOGIN = "risk03";
    private final String ADMIN_PASSWORD = "wasd";
    @SuppressWarnings("FieldCanBeLocal")
    private final String ADMIN_PATRONYMIC = "Викторович";
    @SuppressWarnings("FieldCanBeLocal")
    private final String ADMIN_FORENAME = "Виктор";
    @SuppressWarnings("FieldCanBeLocal")
    private final String ADMIN_SURNAME = "Филиппович";
    private final String[][] STORE = new String[][]{
            {"1", "", "г. Минск", "ул. Платонова", "39", ""},
            {"2", "", "г. Минск", "ул. Берута", "9", "3"},
            {"3", "Могилёвская обл.", "г. Бобруск", "ул. Чайковского", "2", "1"}};
    private final String[][] PRODUCT = new String[][]{
            {"1", "Ручка шариковая синяя \"Classic Stick\" (1,0 мм)", "1.30", "Шариковая ручка с прозрачным корпусом и колпачком. Цвет стержня: синий."},
            {"2", "Ручка шариковая чёрная \"Classic Stick\" (1,0 мм)", "1.35", "Шариковая ручка с прозрачным корпусом и колпачком. Цвет стержня: чёрный."},
            {"3", "Диск с учебной программой \"Правила дорожного движения 2020\"", "15.50", "Учебная программа предназначена для изучения Правил дорожного движения Республики Беларусь и подготовки будущих водителей транспортных средств категорий «B» и «C» к теоретическому экзамену ГАИ."},
            {"4", "Ластик \"Elephant 300/80\"", "0.75", "Ластик Elephant 300/80 изготовлен из натурального материала. Применяется для стирания карандашных записей и рисунков. Эластичный и мягкий ластик не пачкает бумагу и не оставляет следов."},
            {"5", "Тетрадь полуобщая в клетку \"Паттерн\" (48 листов)", "1.80", "Товар представлен в ассортименте, отгрузка осуществляется произвольно."}};
    private final String[] CASHIER = new String[]{"2", "Омаров", "Леонид", "Сергеевич", "Кассир", "Oules", "wasd"};
    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            System.exit(-1);
        }
    };
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    @Order(1)
    void loginTest() {
        send("/logout");
        Assert.assertNull(getOne(send("/getLoggedUser")));
        Assert.assertEquals("true", getOne(send("/login?login=" + DEFAULT_ADMIN[5] + "&password=" + DEFAULT_ADMIN[6])));
        Assert.assertEquals(DEFAULT_ADMIN[5], getArr(send("/getLoggedUser"))[5]);
        send("/logout");
    }

    @Test
    @Order(2)
    void changeAdmin() {
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + DEFAULT_ADMIN[5] + "&password=" + DEFAULT_ADMIN[6])));
        String[] user = getArr(send("/getLoggedUser"));
        Assert.assertNotNull(user);
        String newData = toArr(new String[]{user[0], ADMIN_SURNAME, ADMIN_FORENAME, ADMIN_PATRONYMIC, user[4], ADMIN_LOGIN, ADMIN_PASSWORD});
        send("/update?type=user&" + newData);
        user = getArr(send("/getLoggedUser"));
        Assert.assertEquals(ADMIN_SURNAME, user[1]);
        Assert.assertEquals(ADMIN_FORENAME, user[2]);
        Assert.assertEquals(ADMIN_PATRONYMIC, user[3]);
        Assert.assertEquals(ADMIN_LOGIN, user[5]);
        send("/logout");
    }

    @Test
    @Order(3)
    void createStore() {
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + ADMIN_LOGIN + "&password=" + ADMIN_PASSWORD)));
        for (String[] store : STORE) {
            send("/create?type=store&" + toArr(store));
        }
        ArrayList<String[]> stores = new ArrayList<>();
        for (int i = 0; i < STORE.length; ++i) {
            stores.add(getArr(send("/readOne?type=store&id=" + (i + 1))));
        }
        for (int i = 0; i < STORE.length; ++i) {
            for (int j = 0; j < STORE[0].length; ++j) {
                Assert.assertEquals(STORE[i][j], stores.get(i)[j]);
            }
        }
        send("/logout");
    }

    @Test
    @Order(4)
    void showStores() {
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + ADMIN_LOGIN + "&password=" + ADMIN_PASSWORD)));
        String[][] rs = getDArr(send("/readAll?type=store"));
        Assert.assertNotNull(rs);
        for (String[] tr : rs) {
            for (String td : tr) {
                System.out.print(td + ' ');
            }
            System.out.println();
        }
        send("/logout");
    }

    @Test
    @Order(5)
    void createProduct() {
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + ADMIN_LOGIN + "&password=" + ADMIN_PASSWORD)));
        for (String[] product : PRODUCT) {
            send("/create?type=product&" + toArr(product));
        }
        ArrayList<String[]> products = new ArrayList<>();
        for (int i = 0; i < PRODUCT.length; ++i) {
            products.add(getArr(send("/readOne?type=product&id=" + (i + 1))));
        }
        for (int i = 0; i < PRODUCT.length; ++i) {
            for (int j = 0; j < PRODUCT[0].length; ++j) {
                Assert.assertEquals(PRODUCT[i][j], products.get(i)[j]);
            }
        }
        send("/logout");
    }

    @Test
    @Order(6)
    void showProducts() {
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + ADMIN_LOGIN + "&password=" + ADMIN_PASSWORD)));
        String[][] rs = getDArr(send("/readAll?type=product"));
        Assert.assertNotNull(rs);
        for (String[] tr : rs) {
            for (String td : tr) {
                System.out.print(td + ' ');
            }
            System.out.println();
        }
        send("/logout");
    }

    @Test
    @Order(7)
    void fillStore() {
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + ADMIN_LOGIN + "&password=" + ADMIN_PASSWORD)));
        for (int store = 0; store < STORE.length; ++store) {
            for (int product = 0; product < PRODUCT.length; ++product) {
                send("/setManyToMany?type=assortment&root=" + (store + 1) + "&product=" + (product + 1) + "&quantity=" + (2 * store + product + 1));
            }
        }
        send("/logout");
    }

    @Test
    @Order(8)
    void createCashier() {
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + ADMIN_LOGIN + "&password=" + ADMIN_PASSWORD)));
        send("/create?type=user&" + toArr(CASHIER));
        String[] user = getArr(send("/readOne?type=user&id=" + CASHIER[0]));
        for (int i = 0; i < 6; ++i) {
            Assert.assertEquals(CASHIER[i], user[i]);
        }
        send("/logout");
    }

    @Test
    @Order(9)
    void showUsers() {
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + ADMIN_LOGIN + "&password=" + ADMIN_PASSWORD)));
        String[][] rs = getDArr(send("/readAll?type=user"));
        Assert.assertNotNull(rs);
        for (String[] tr : rs) {
            for (String td : tr) {
                System.out.print(td + ' ');
            }
            System.out.println();
        }
        send("/logout");
    }

    @Test
    @Order(10)
    void logInLogOut() {
        send("/logout");
        Assert.assertNull(getOne(send("/getLoggedUser")));
        Assert.assertEquals("true", getOne(send("/login?login=" + ADMIN_LOGIN + "&password=" + ADMIN_PASSWORD)));
        Assert.assertEquals("false", getOne(send("/login?login=" + CASHIER[5] + "&password=" + CASHIER[6])));
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + CASHIER[5] + "&password=" + CASHIER[6])));
        send("/logout");
    }

    @Test
    @Order(11)
    void addTransactions() {
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + CASHIER[5] + "&password=" + CASHIER[6])));
        int j = 1;
        int NUM_OF_TRANSACTIONS = 9;
        for (int i = 0; i < NUM_OF_TRANSACTIONS; i++) {
            if (j > STORE.length) {
                j = 1;
            }
            Date date = new Date();
            Assert.assertEquals("true", send("/create?type=transaction&" + toArr(new String[]{String.valueOf(i + 1), String.valueOf(j), getArr(send("/getLoggedUser"))[0], dateFormat.format(date)})));
            j++;
        }
        send("/logout");
    }

    @Test
    @Order(12)
    void sellProducts() {
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + CASHIER[5] + "&password=" + CASHIER[6])));
        for (int i = 0; i < STORE.length; i++) {
            for (int k = 0; k < PRODUCT.length; k++) {
                send("/setManyToMany?type=entry&root=" + (i + 1) + "&product=" + (k + 1) + "&quantity=" + (i + k/2));
            }
        }
        send("/logout");
    }

    @Test
    @Order(13)
    void showWhere() {
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + ADMIN_LOGIN + "&password=" + ADMIN_PASSWORD)));
        for (String[] strings : PRODUCT) {
            for (String id : getArr(send("/showWhere?id=" + strings[0]))) {
                String[] store = getArr(send("/readOne?type=store&id=" + id));
                System.out.println(store[2] + ' ' + store[3] + ' ' + store[4]);
            }
        }
        send("/logout");
    }

    @Test
    @Order(14)
    void getSalesByStoreReport() {
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + ADMIN_LOGIN + "&password=" + ADMIN_PASSWORD)));
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = c.getTime();
        String[][] rs = getDArr(send("/getSalesByStoreReport?from=" + dateFormat.format(yesterday) + "&to=" + dateFormat.format(today)));
        Assert.assertNotNull(rs);
        for (String[] row : rs) {
            for (String td : row) {
                System.out.print(td);
                System.out.print(' ');
            }
            System.out.println();
        }
        send("/logout");
    }

    @Test
    @Order(15)
    void getSalesByProductReport() {
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + ADMIN_LOGIN + "&password=" + ADMIN_PASSWORD)));
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = c.getTime();
        String[][] rs = getDArr(send("/getSalesByProductReport?from=" + dateFormat.format(yesterday) + "&to=" + dateFormat.format(today)));
        Assert.assertNotNull(rs);
        for (String[] row : rs) {
            for (String td : row) {
                System.out.print(td);
                System.out.print(' ');
            }
            System.out.println();
        }
        send("/logout");
    }

    @Test
    @Order(16)
    void getSalesByCashierReport() {
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + ADMIN_LOGIN + "&password=" + ADMIN_PASSWORD)));
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = c.getTime();
        String[][] rs = getDArr(send("/getSalesByCashierReport?from=" + dateFormat.format(yesterday) + "&to=" + dateFormat.format(today)));
        Assert.assertNotNull(rs);
        for (String[] row : rs) {
            for (String td : row) {
                System.out.print(td);
                System.out.print(' ');
            }
            System.out.println();
        }
        send("/logout");
    }

    @Test
    @Order(17)
    void safelyDelete() {
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + ADMIN_LOGIN + "&password=" + ADMIN_PASSWORD)));
        Assert.assertEquals("false", send("/remove?type=user&id=" + getArr(send("/getLoggedUser"))[0]));
        send("/logout");
    }

    @Test
    @Order(18)
    void rest() {
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + ADMIN_LOGIN + "&password=" + ADMIN_PASSWORD)));
        String[] curriencesCodes = new String[]{"USD", "EUR"};
        boolean br = false;
        for (String[] product : PRODUCT) {
            for (String currency : curriencesCodes) {
                String result = send("/toCur?value=" + product[2] + "&currency=" + currency);
                if (result == null) {
                    System.out.println("Сервис недоступен");
                    br = true;
                    break;
                } else
                    System.out.println(product[1] + " - " + result + currency);
            }
            if (br)
                break;
        }
        send("/logout");
    }

    @Test
    @Order(19)
    void changePrice(){
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + ADMIN_LOGIN + "&password=" + ADMIN_PASSWORD)));
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = c.getTime();
        String[][] oldO = getDArr(send("/getSalesByProductReport?from=" + dateFormat.format(yesterday) + "&to=" + dateFormat.format(today)));
        Assert.assertNotNull(oldO);
        for (String[] product : PRODUCT){
            send("/update?type=product&" + toArr(new String[]{product[0], product[1], String.valueOf(Double.parseDouble(product[2]) * 1.15), product[3]}));
        }
        String[][] newO = getDArr(send("/getSalesByProductReport?from=" + dateFormat.format(yesterday) + "&to=" + dateFormat.format(today)));
        Assert.assertNotNull(newO);
        for (int i = 0; i < oldO.length; i++){
            for (int j = 0; j < oldO[i].length; j++){
                System.out.print(oldO[i][j] + ' ');
            }
            System.out.println();
            for (int j = 0; j < newO[i].length; j++){
                System.out.print(newO[i][j] + ' ');
            }
            System.out.println();
        }
        send("/logout");
    }

    @Test
    @Order(20)
    void clearAll() {
        send("/logout");
        Assert.assertEquals("true", getOne(send("/login?login=" + ADMIN_LOGIN + "&password=" + ADMIN_PASSWORD)));
        String[] sequence = {"entry", "transaction", "assortment", "product", "store"};
        for (String type : sequence) {
            String[][] arr = getDArr(send("/readAll?type=" + type));
            for (String[] row : arr) {
                send("/remove?type=" + type + "&id=" + row[0]);
            }
        }
        String[][] arr = getDArr(send("/readAll?type=user"));
        for (int i = 1; i < arr.length; i++) {
            send("/remove?type=user&id=" + arr[i][0]);
        }
        send("/update?type=user&" + toArr(DEFAULT_ADMIN));
        arr = getDArr(send("/readAll?type=user"));
        Assert.assertEquals(1, arr.length);
        send("/logout");
    }
}
