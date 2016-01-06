package executors;

import commands.Command;
import commands.factories.CommandFactory;
import controllers.ApplicationContext;
import controllers.CommandLineController;
import model.Book;
import model.Params;

/**
 * (c) Roman Gordeev
 * <p/>
 * 2014 июн 10
 */
public class Executor
{
    public Executor(CommandLineController controller, CommandFactory commandFactory)
    {
        this.controller = controller;
        this.commandFactory = commandFactory;
    }

    public void execute(String commandLine)
    {
        Params params = getController().parseCommandLineString(commandLine);
        Command command = getCommandFactory().createCommand(params);
        command.execute(ap);
    }

    public void init(ApplicationContext ap)
    {
        this.ap = ap;
    }

    public CommandLineController getController()
    {
        return controller;
    }

    public CommandFactory getCommandFactory()
    {
        return commandFactory;
    }

    private CommandLineController controller;
    private CommandFactory commandFactory;
    private ApplicationContext ap;

}
