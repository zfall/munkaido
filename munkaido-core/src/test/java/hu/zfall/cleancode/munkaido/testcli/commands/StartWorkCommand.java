package hu.zfall.cleancode.munkaido.testcli.commands;

import hu.zfall.cleancode.munkaido.testcli.CliApp;

public class StartWorkCommand extends BaseWorkTimeCommand {

    @Override
    public String methodCall(String username) {
        return CliApp.workTimeService.startWork(username).getMessage();
    }

    @Override
    public String getName() {
        return "startWork";
    }
}
