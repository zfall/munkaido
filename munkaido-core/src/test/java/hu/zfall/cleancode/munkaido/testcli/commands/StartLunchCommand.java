package hu.zfall.cleancode.munkaido.testcli.commands;

import hu.zfall.cleancode.munkaido.testcli.CliApp;

public class StartLunchCommand extends BaseWorkTimeCommand {

    @Override
    public String methodCall(String username) {
        return CliApp.workTimeService.startLunch(username).getMessage();
    }

    @Override
    public String getName() {
        return "startLunch";
    }
}
