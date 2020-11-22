package hu.zfall.cleancode.munkaido.testcli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.function.Function;
import hu.zfall.cleancode.munkaido.boundaries.dto.InfoResponse;
import hu.zfall.cleancode.munkaido.boundaries.dto.WorkTimeSummary;
import hu.zfall.cleancode.munkaido.repository.WorkTimeItemInMemoryRepository;
import hu.zfall.cleancode.munkaido.service.TimeService;
import hu.zfall.cleancode.munkaido.service.WorkTimeService;
import hu.zfall.cleancode.munkaido.utils.TestUtils;

public class CliApp {

    public static WorkTimeService workTimeService = null;

    public static void main(String[] args) {
        createApp();
        System.out.println("Munkaido core test command line interface");
        System.out.println("Exit: q <enter>");
        System.out.println("Help: h <enter>");
        System.out.println("Usage: <command> <arguments>");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String readLine = "";
            while (!"q".equalsIgnoreCase(readLine.trim())) {
                System.out.print("\n> ");
                readLine = br.readLine();
                String[] readWords = readLine.split(" ");

                if (readWords.length > 0) {
                    String command = readWords[0];
                    String[] params = Arrays.copyOfRange(readWords, 1, readWords.length);
                    switch (command) {
                        case "h":
                            System.out.println("Avaialble commands: startWork, endWork, startLunch, endLunch, summary");
                            break;
                        case "q":
                            System.out.println("Bye");
                            break;
                        case "startWork":
                            callRegistration(params, u -> workTimeService.startWork(u));
                            break;
                        case "endWork":
                            callRegistration(params, u -> workTimeService.endWork(u));
                            break;
                        case "startLunch":
                            callRegistration(params, u -> workTimeService.startLunch(u));
                            break;
                        case "endLunch":
                            callRegistration(params, u -> workTimeService.endLunch(u));
                            break;
                        case "summary":
                            callSummary(params);
                            break;
                        default:
                            System.out.println("Unknown command: " + command);
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    private static void callSummary(String[] params) {
        if (params.length != 1) {
            System.err.println("Exactly one parameter required!");
        } else {
            WorkTimeSummary summary = CliApp.workTimeService.generateSummary(params[0]);

            System.out.println("Worked time:    " + summary.getWorkedTime() + "\nRemaining time: " + summary.getRemainingTime());
        }
    }

    private static void callRegistration(String[] params, Function<String, InfoResponse> registrationFunction) {
        if (params.length != 1) {
            System.err.println("Exactly one parameter required!");
        } else {
            try {
                System.out.println(registrationFunction.apply(params[0]).getMessage());
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + (e.getMessage() == null ? "" : e.getMessage()));
            }

        }
    }

    private static void createApp() {
        workTimeService = new WorkTimeService();
        TimeService timeService = new TimeService();
        WorkTimeItemInMemoryRepository repository = new WorkTimeItemInMemoryRepository();
        TestUtils.setPrivateMember(workTimeService, "timeService", timeService);
        TestUtils.setPrivateMember(workTimeService, "repository", repository);
        TestUtils.setPrivateMember(repository, "timeService", timeService);
    }
}
