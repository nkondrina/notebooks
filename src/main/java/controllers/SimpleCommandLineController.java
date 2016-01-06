package controllers;

import model.Params;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * (c) Roman Gordeev
 * <p/>
 * 2014 июн 10
 */
public class SimpleCommandLineController implements CommandLineController
{
    private static final CommandLineController instance = new SimpleCommandLineController();

    public static CommandLineController getInstance()
    {
        return instance;
    }

    private SimpleCommandLineController(){ }

    @Override
    public Params parseCommandLineString(String commandLine)
    {
        String cmd_name = null;
        String cmd_args = null;
        String[] row_data;

        if ((commandLine == null) || (commandLine.length() == 0))
            return new Params("", "");


        row_data = StringUtils.split(deleteSpace(commandLine));

        if (row_data == null || row_data.length == 0)
            return new Params("", "");


        row_data = StringUtils.split(StringUtils.join(row_data," "), ":");
        if (row_data == null || row_data.length == 0)
            return new Params("", "");
        cmd_name = row_data[0];
        String[] row_args = Arrays.copyOfRange(row_data, 1, row_data.length);
        cmd_args = StringUtils.join(row_args, "/");


        return new Params(cmd_name, cmd_args);

    }

    private StringBuilder delSpace(StringBuilder sb, String symbol)
    {
        int i;

        do
        {
            i = sb.indexOf(symbol);

            if (i>=0)
                sb.deleteCharAt(i+symbol.indexOf(' '));
        }
        while (i>=0);

        return sb;
    }

    private String deleteSpace(String str)
    {
        StringBuilder sb1 = new StringBuilder(str);
        sb1=delSpace(sb1, " :");
        sb1=delSpace(sb1, ": ");
        sb1=delSpace(sb1, " /");
        sb1=delSpace(sb1, "/ ");

        return sb1.toString();
    }

}
