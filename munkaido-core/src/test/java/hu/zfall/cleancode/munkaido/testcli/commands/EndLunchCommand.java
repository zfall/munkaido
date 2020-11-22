package hu.zfall.cleancode.munkaido.testcli.commands;

import hu.zfall.cleancode.munkaido.testcli.CliApp;

public class EndLunchCommand extends BaseWorkTimeCommand {

    @Override
    public String methodCall(String username) {
        return CliApp.workTimeService.endLunch(username).getMessage();
    }

    @Override
    public String getName() {
        return "endLunch";
    }
}
