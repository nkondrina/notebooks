package commands;

import controllers.ApplicationContext;
import services.StorageService;

/**
 * Created by Поль on 07.01.2016.
 */
public class CommandDelete extends AbstractCommand
{
    public static final String NAME = "delete";

    public CommandDelete(String strnumber, StorageService storage)
    {
        super(storage);
        //this.strnumber=strnumber;

        try {
            this.number = Long.valueOf(strnumber);
        }
        catch (Exception e)
        {
            System.out.println("Incorrectly entered id!");
           // e.printStackTrace();
        }
    }

    @Override
    public void execute(ApplicationContext ap)
    {
        getStorage().delete(this.number);
    }

    @Override
    public String getName() {
        return NAME;
    }

    private Long number;
    // private String strnumber;
}
