package services;

import com.google.inject.Singleton;
import configs.DBConnection;

import model.Adress;
import model.Book;
import model.Person;
import model.Phone;
import org.apache.commons.lang3.StringUtils;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * (c) Roman Gordeev
 * <p/>
 * 2014 июн 18
 */
@Singleton
public class JDBCStorageService implements StorageService
{
    @Override
    public void add(String personName, String phone, String adress)
    {
        TransactionScript.getInstance().addPerson(personName, phone, adress, defaultBook());
    }

    @Override
    public void update(String personName, String phone, String adress, Long longId)
    {
        TransactionScript.getInstance().updatePerson(personName, phone, adress, longId, defaultBook());
    }

    @Override
    public void delete(Long longId)
    {
        TransactionScript.getInstance().delPerson(longId, defaultBook());
    }

    @Override
    public List<Person> list()
    {
        return TransactionScript.getInstance().listPersons();
    }

    @Override
    public Book defaultBook()
    {
        return TransactionScript.getInstance().defaultBook();
    }

    @Override
    public void close()
    {
        try
        {
            TransactionScript.getInstance().close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static final class TransactionScript
    {
        private static final TransactionScript instance = new TransactionScript();

        public static TransactionScript getInstance() {
            return instance;
        }

        public TransactionScript()
        {
            String url      = DBConnection.JDBC.url();
            String login    = DBConnection.JDBC.username();
            String password = DBConnection.JDBC.password();

            try
            {
                connection = DriverManager.getConnection(url, login,
                        password);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public List<Person> listPersons()
        {
            List<Person> result = new ArrayList<>(10);

            try
            {
                PreparedStatement statement = connection.prepareStatement(
                        "select name, phone, adress, p.id from book b \n" +
                                "inner join person p on b.id = p.book_id \n" +
                                "inner join phone ph on p.id = ph.person_id \n" +
                                "inner join adress adr on p.id = adr.person_id \n");

                ResultSet r_set = statement.executeQuery();

                while (r_set.next())
                {
                    Person p = new Person(r_set.getString("name"));
                    Phone ph = new Phone(p, r_set.getString("phone"));
                    Adress adr =new Adress(p,r_set.getString("adress"));

                    p.getPhones().add(ph);
                    p.getAdresses().add(adr);
                    p.setId(r_set.getLong("id"));

                    result.add(p);
                }

            } catch (Exception e)
            {
                e.printStackTrace();
            }

            return result;
        }

        public void addPerson(String person, String phone, String adress, Book book)
        {
            try
            {
                if (book.getId() == null)
                {
                    PreparedStatement addBook = connection.prepareStatement("insert into book (id) values (DEFAULT)", Statement.RETURN_GENERATED_KEYS);
                    addBook.execute();
                    ResultSet generated_book_id = addBook.getGeneratedKeys();

                    if (generated_book_id.next())
                        book.setId(generated_book_id.getLong("id"));
                }

                PreparedStatement addPerson = connection.prepareStatement("insert into person (book_id, name) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
                PreparedStatement addPhone  = connection.prepareStatement("insert into phone (person_id, phone) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
                PreparedStatement addAdress = connection.prepareStatement("insert into adress (person_id, adress) values (?, ?)", Statement.RETURN_GENERATED_KEYS);

                addPerson.setLong(1, book.getId());
                addPerson.setString (2, person);

                addPerson.execute();

                ResultSet auto_pk = addPerson.getGeneratedKeys();
                while (auto_pk.next())
                {
                    int id = auto_pk.getInt("id");
                    addPhone.setInt(1, id);
                    addPhone.setString(2, phone);
                    addPhone.execute();

                    addAdress.setInt(1, id);
                    addAdress.setString(2, adress);
                    addAdress.execute();
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public  void updatePerson(String person, String phone, String adress, Long longId, Book book)
        {
            try
            {
                if (book.getId() == null)
                {
                    PreparedStatement addBook = connection.prepareStatement("insert into book (id) values (DEFAULT)", Statement.RETURN_GENERATED_KEYS);
                    addBook.execute();
                    ResultSet generated_book_id = addBook.getGeneratedKeys();

                    if (generated_book_id.next())
                        book.setId(generated_book_id.getLong("id"));
                }

                PreparedStatement statement = connection.prepareStatement("select name from person where book_id=? and id=?");

                statement.setLong(1, book.getId());
                statement.setLong(2, longId);

                ResultSet r_set = statement.executeQuery();


                if (r_set.next()) {

                    PreparedStatement updPhone = connection.prepareStatement("update phone set phone =? where person_id =?");
                    PreparedStatement updAdress = connection.prepareStatement("update adress set adress =? where person_id =?");
                    PreparedStatement updPerson = connection.prepareStatement("update person set name =? where book_id=? and id=?");

                    updPhone.setString(1, phone);
                    updPhone.setLong(2, longId);
                    updPhone.executeUpdate();

                    updAdress.setString(1, adress);
                    updAdress.setLong(2, longId);
                    updAdress.executeUpdate();

                    updPerson.setString(1, person);
                    updPerson.setLong(2, book.getId());
                    updPerson.setLong(3, longId);
                    updPerson.executeUpdate();

                    System.out.println("Updating record id: " + longId + " was successful! \n" +
                            "Updated on: person is: '" + person + "' , phone is: '" + phone + "' adress is: '" + adress + "'");
                }
                else
                    System.out.println("Id " + longId.toString() + " is NOT found!");

            }
            catch (Exception e)
            {
                System.out.println("The record is NOT updated!");
                e.printStackTrace();
            }
        }

        public void delPerson(Long longId, Book book)
        {
            try
            {
                if (book.getId() == null)
                {
                    PreparedStatement addBook = connection.prepareStatement("insert into book (id) values (DEFAULT)", Statement.RETURN_GENERATED_KEYS);
                    addBook.execute();
                    ResultSet generated_book_id = addBook.getGeneratedKeys();

                    if (generated_book_id.next())
                        book.setId(generated_book_id.getLong("id"));
                }

                PreparedStatement statement = connection.prepareStatement("select name from person where book_id=? and id=?");

                statement.setLong(1, book.getId());
                statement.setLong(2, longId);

                ResultSet r_set = statement.executeQuery();


                if (r_set.next()) {

                    PreparedStatement delPhone = connection.prepareStatement("delete from phone where person_id =?");
                    PreparedStatement delAdress = connection.prepareStatement("delete from adress where person_id =?");
                    PreparedStatement delbook_person = connection.prepareStatement("delete from book_person where book_id=? and persons_id =?");
                    PreparedStatement delPerson = connection.prepareStatement("delete from person where book_id=? and id=?");

                    delPhone.setLong(1, longId);
                    delPhone.executeUpdate();

                    delAdress.setLong(1, longId);
                    delAdress.executeUpdate();

                    delbook_person.setLong(1, book.getId());
                    delbook_person.setLong(2, longId);
                    delbook_person.executeUpdate();

                    delPerson.setLong(1, book.getId());
                    delPerson.setLong(2, longId);
                    delPerson.executeUpdate();

                    r_set=statement.executeQuery();

                    if (r_set.next())
                        System.out.println("Id " + longId.toString() + " is NOT deleted!");
                    else
                        System.out.println("Removal completed successfully!");
                }
                else
                    System.out.println("Id " + longId.toString() + " is NOT found!");
            }
            catch (Exception e)
            {
                System.out.println("The record is NOT deleted!");
                e.printStackTrace();
            }
        }

        public Book defaultBook()
        {
            // создаем новый экземпляр, который в дальнейшем и сохраним,
            // если не найдем для него записи в БД
            Book book = new Book();

            try
            {
                Statement statement = connection.createStatement();
                // выбираем из таблицы book единственную запись
                ResultSet books = statement.executeQuery("select id from book limit 1");
                // если хоть одна зепись в таблице нашлась, инициализируем наш объект полученными значениями
                if (books.next())
                {
                    book.setId(books.getLong("id"));
                }

            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

            // возвращаем проинициализированный или пустой объект книги
            return book;
        }

        public void close() throws SQLException
        {
            if (connection != null && !connection.isClosed())
                connection.close();
        }

        private Connection connection;
    }
}
