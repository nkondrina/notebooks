package commands;

import controllers.ApplicationContext;
import model.Adress;
import model.Person;
import model.Phone;
import services.StorageService;

import java.util.List;

/**
* User: rgordeev
* Date: 25.06.14
* Time: 17:21
*/
public class CommandList extends AbstractCommand
{
    public static final String NAME = "list";


    public CommandList(StorageService storage)
    {
        super(storage);
    }

    @Override
    public void execute(ApplicationContext ap)
    {
        List<Person> persons = getStorage().list();
        System.out.println("***************************\n");
        for (Person p : persons) {
            // if (persons.size() != 0)
            printPerson(p);
        }
    }

    private void printPerson(Person person)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("Person: ").append(person.getName()).append("\n\t").append("phones: ");//append("\n").append("adresses: ");

        for (Phone phone : person.getPhones())
            sb.append(phone.getPhone()).append("\n\t");

        sb.append("adresses: ");
        for (Adress adress : person.getAdresses())
            sb.append(adress.getAdress()).append("\n\t");

        sb.append("id: ").append(person.getId()).append("\n\t");

        System.out.println(sb.toString());
    }

    @Override
    public String getName() {
        return NAME;
    }


}
