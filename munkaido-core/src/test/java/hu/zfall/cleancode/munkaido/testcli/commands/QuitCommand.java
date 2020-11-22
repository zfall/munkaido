package hu.zfall.cleancode.munkaido.testcli.commands;

import hu.zfall.cleancode.munkaido.testcli.Command;

public class QuitCommand implements Command {

    @Override
    public void execute(String[] args) {
        System.out.println("Bye!");
    }

    @Override
    public String getName() {
        return "q";
    }
}
