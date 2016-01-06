package commands;

import controllers.ApplicationContext;
import services.StorageService;

/**
 * Created by Поль on 07.01.2016.
 */
public class CommandUpdate extends AbstractCommand
{
    public static final String NAME = "update";


    public CommandUpdate(String person, String phone, String adress, String strId, StorageService storage)
    {
        super(storage);
        this.person = person;
        this.phone = phone;
        this.adress = adress;
        // this.strId = strId;

        try {
            this.id = Long.valueOf(strId);
        }
        catch (Exception e)
        {
            System.out.println("Incorrectly entered id!");
            //e.printStackTrace();
        }
    }

    @Override
    public void execute(ApplicationContext ap)
    {
        getStorage().update(this.person, this.phone, this.adress, id);
        // System.out.println(getName() + ": person '" + this.person + "' was added to the book, phone is: '" + this.phone +
        //        "' adress is: '" + this.adress+"'");
    }

    @Override
    public String getName() {
        return NAME;
    }

    private String person;
    private String phone;
    private String adress;
    // private String strId;
    private Long id;
}

