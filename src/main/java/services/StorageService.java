package services;

import model.Book;
import model.Person;

import java.util.List;

/**
 * (c) Roman Gordeev
 * <p/>
 * 2014 июн 18
 */
public interface StorageService
{
    void add(String personName, String phone, String adress);

    List<Person> list();

    Book defaultBook();

    void delete(Long longId);

    void update(String personName, String phone, String adress, Long longId);

    void close();
}
