package hu.zfall.cleancode.munkaido.testcli;

public interface Command {

    String getName();

    void execute(String[] args);
}
