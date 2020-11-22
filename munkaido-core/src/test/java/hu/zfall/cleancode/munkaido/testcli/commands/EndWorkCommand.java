package hu.zfall.cleancode.munkaido.testcli.commands;

import hu.zfall.cleancode.munkaido.testcli.CliApp;

public class EndWorkCommand extends BaseWorkTimeCommand {

    @Override
    public String methodCall(String username) {
        return CliApp.workTimeService.endWork(username).getMessage();
    }

    @Override
    public String getName() {
        return "endWork";
    }
}
