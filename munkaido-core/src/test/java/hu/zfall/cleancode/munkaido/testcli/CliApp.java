package hu.zfall.cleancode.munkaido.testcli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import hu.zfall.cleancode.munkaido.repository.WorkTimeItemInMemoryRepository;
import hu.zfall.cleancode.munkaido.service.TimeService;
import hu.zfall.cleancode.munkaido.service.WorkTimeService;
import hu.zfall.cleancode.munkaido.testcli.commands.*;
import hu.zfall.cleancode.munkaido.utils.TestUtils;

public class CliApp {

    public static List<Command>   COMMANDS        = Arrays.asList(new HelpCommand(), new QuitCommand(), new StartWorkCommand(),
            new EndWorkCommand(), new StartLunchCommand(), new EndLunchCommand(), new SummaryCommand());

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
                    Optional<Command> commandOption = COMMANDS.stream().filter(c -> c.getName().equals(readWords[0])).findFirst();
                    if (commandOption.isPresent()) {
                        String[] params = Arrays.copyOfRange(readWords, 1, readWords.length);
                        commandOption.get().execute(params);
                    } else {
                        System.out.println("Unknown command: " + readWords[0]);
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
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
