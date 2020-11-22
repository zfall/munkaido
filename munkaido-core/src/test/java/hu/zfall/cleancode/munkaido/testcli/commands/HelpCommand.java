package hu.zfall.cleancode.munkaido.testcli.commands;

import java.util.stream.Collectors;
import hu.zfall.cleancode.munkaido.testcli.CliApp;
import hu.zfall.cleancode.munkaido.testcli.Command;

public class HelpCommand implements Command {

    @Override
    public void execute(String[] args) {
        System.out
                .println("Avaialble commands: " + CliApp.COMMANDS.stream().map(c -> c.getName()).collect(Collectors.joining(", ")));
    }

    @Override
    public String getName() {
        return "h";
    }
}
