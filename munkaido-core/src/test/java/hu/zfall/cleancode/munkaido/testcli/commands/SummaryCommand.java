package hu.zfall.cleancode.munkaido.testcli.commands;

import hu.zfall.cleancode.munkaido.boundaries.dto.WorkTimeSummary;
import hu.zfall.cleancode.munkaido.testcli.CliApp;

public class SummaryCommand extends BaseWorkTimeCommand {

    @Override
    public String getName() {
        return "summary";
    }

    @Override
    public String methodCall(String username) {
        WorkTimeSummary summary = CliApp.workTimeService.generateSummary(username);

        return "Worked time:    " + summary.getWorkedTime()
                + "\nRemaining time: " + summary.getRemainingTime();
    }

}
