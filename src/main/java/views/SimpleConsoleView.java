package views;

import java.util.Scanner;

/**
 * (c) Roman Gordeev
 * <p/>
 * 2014 июн 10
 */
public class SimpleConsoleView implements ConsoleView
{
    private static final ConsoleView instance = new SimpleConsoleView();

    public static ConsoleView getInstance()
    {
        return instance;
    }

    private SimpleConsoleView()
    {
        in = new Scanner(System.in);
    }

    @Override
    public String getNextCommandLine()
    {
        String str="***************************\n" +
                "ENTER THE FOLLOWING COMMAND:\n" +
                "1) add:name/phone/adress <- to add a new person \n" +
                "2) list <- to view existing records \n" +
                "3) delete:id <- to remove a person by number \n" +
                "4) update:name/phone/adress/id <- to update the data about the person \n" +
                "5) exit <- for application exit \n" +
                "***************************";

        System.out.println(str);
        System.out.print("-> ");
        return in.nextLine();
    }

    private Scanner in;
}
