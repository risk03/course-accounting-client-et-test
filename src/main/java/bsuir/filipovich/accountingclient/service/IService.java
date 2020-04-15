package bsuir.filipovich.accountingclient.service;

import java.util.ArrayList;

public interface IService {
    String getMessage();

    void create(String type, String[] strings);

    String[] readOne(String type, int id);

    ArrayList<String[]> readAll(String type);

    ArrayList<String[]> readAll(String type, int id);

    void update(String type, String[] strings);

    void remove(String type, String id);
}