package hu.zfall.cleancode.munkaido.testcli.commands;

import hu.zfall.cleancode.munkaido.testcli.Command;

public abstract class BaseWorkTimeCommand implements Command {

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            System.err.println("Exactly one parameter required!");
        } else {
            System.out.println(methodCall(args[0]));
        }
    }

    public abstract String methodCall(String username);
}
