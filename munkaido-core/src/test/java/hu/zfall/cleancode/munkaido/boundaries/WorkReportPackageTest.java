package hu.zfall.cleancode.munkaido.boundaries;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import hu.zfall.cleancode.munkaido.boundaries.dto.WorkTimeSummary;
import hu.zfall.cleancode.munkaido.repository.WorkTimeItemInMemoryRepository;
import hu.zfall.cleancode.munkaido.service.TimeService;
import hu.zfall.cleancode.munkaido.service.WorkTimeService;
import hu.zfall.cleancode.munkaido.utils.TestUtils;
import hu.zfall.cleancode.munkaido.utils.UtilConv;

@ExtendWith(MockitoExtension.class)
public class WorkReportPackageTest {

    private static final String            username   = "zoli";

    private WorkReport                     workReport = null;

    @Spy
    private TimeService                    timeService;

    private WorkTimeItemInMemoryRepository repository = new WorkTimeItemInMemoryRepository();

    @BeforeEach
    private void init() {
        WorkTimeService service = new WorkTimeService();
        TestUtils.setPrivateMember(service, "timeService", timeService);
        TestUtils.setPrivateMember(service, "repository", repository);
        TestUtils.setPrivateMember(repository, "timeService", timeService);
        workReport = service;
    }

    @Test
    public void testNotWorkedAtAll() {
        WorkTimeSummary summary = workReport.generateSummary(username);

        Assertions.assertEquals("0:00:00", summary.getWorkedTime());
        Assertions.assertEquals("8:20:00", summary.getRemainingTime());
    }

    @Test
    public void testStartWork() {
        repository.executeStatement(s -> {
            s.executeUpdate("insert into work_time_item values('" + username + "','20201122','20201122080000',null,null)");
        });
        Mockito.when(timeService.getCurrentOffsetDateTime()).thenReturn(UtilConv.str2OffsetDateTime("20201122091500"));
        Mockito.when(timeService.getCurrentLocalDate()).thenReturn(UtilConv.str2LocalDate("20201122"));

        WorkTimeSummary summary = workReport.generateSummary(username);

        Assertions.assertEquals("1:15:00", summary.getWorkedTime());
        Assertions.assertEquals("7:05:00", summary.getRemainingTime());
    }

    @Test
    public void testStartWorkEnd() {
        repository.executeStatement(s -> {
            s.executeUpdate(
                    "insert into work_time_item values('" + username + "','20201122','20201122080000', '20201122160000',null)");
        });

        WorkTimeSummary summary = workReport.generateSummary(username);

        Assertions.assertEquals("8:00:00", summary.getWorkedTime());
        Assertions.assertEquals("0:20:00", summary.getRemainingTime());
    }

    @Test
    public void testStartWorkEndOverdue() {
        repository.executeStatement(s -> {
            s.executeUpdate(
                    "insert into work_time_item values('" + username + "','20201122','20201122080000', '20201122170000',null)");
        });

        WorkTimeSummary summary = workReport.generateSummary(username);

        Assertions.assertEquals("9:00:00", summary.getWorkedTime());
        Assertions.assertEquals("-0:40:00", summary.getRemainingTime());
    }

    @Test
    public void testStartWorkLunch() {
        repository.executeStatement(s -> {
            s.executeUpdate(
                    "insert into work_time_item values('" + username + "','20201122','20201122080000', '20201122113000','L')");
        });

        WorkTimeSummary summary = workReport.generateSummary(username);

        Assertions.assertEquals("3:30:00", summary.getWorkedTime());
        Assertions.assertEquals("4:30:00", summary.getRemainingTime());
    }

    @Test
    public void testStartWorkLunchBack() {
        repository.executeStatement(s -> {
            s.executeUpdate(
                    "insert into work_time_item values('" + username + "','20201122','20201122080000', '20201122113000','L')");
            s.executeUpdate(
                    "insert into work_time_item values('" + username + "','20201122','20201122120000',null,null)");
        });

        Mockito.when(timeService.getCurrentOffsetDateTime()).thenReturn(UtilConv.str2OffsetDateTime("20201122151500"));
        Mockito.when(timeService.getCurrentLocalDate()).thenReturn(UtilConv.str2LocalDate("20201122"));

        WorkTimeSummary summary = workReport.generateSummary(username);

        Assertions.assertEquals("6:45:00", summary.getWorkedTime());
        Assertions.assertEquals("1:15:00", summary.getRemainingTime());
    }

    @Test
    public void testStartWorkLunchBackEnd() {
        repository.executeStatement(s -> {
            s.executeUpdate(
                    "insert into work_time_item values('" + username + "','20201122','20201122080000', '20201122113000','L')");
            s.executeUpdate(
                    "insert into work_time_item values('" + username + "','20201122','20201122120000', '20201122163000',null)");
        });

        WorkTimeSummary summary = workReport.generateSummary(username);

        Assertions.assertEquals("8:00:00", summary.getWorkedTime());
        Assertions.assertEquals("0:00:00", summary.getRemainingTime());
    }
}
