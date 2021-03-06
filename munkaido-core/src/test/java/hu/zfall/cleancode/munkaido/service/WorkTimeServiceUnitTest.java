package hu.zfall.cleancode.munkaido.service;

import static hu.zfall.cleancode.munkaido.domain.WorkTimeItemSpecial.LUNCH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import hu.zfall.cleancode.munkaido.boundaries.dto.InfoResponse;
import hu.zfall.cleancode.munkaido.boundaries.dto.WorkTimeSummary;
import hu.zfall.cleancode.munkaido.domain.WorkTimeItem;
import hu.zfall.cleancode.munkaido.domain.WorkTimeItemSpecial;
import hu.zfall.cleancode.munkaido.exception.AlreadyStartedWorkException;
import hu.zfall.cleancode.munkaido.exception.NotYetStartedWorkException;
import hu.zfall.cleancode.munkaido.repository.WorkTimeItemRepository;
import hu.zfall.cleancode.munkaido.utils.TestUtils;
import hu.zfall.cleancode.munkaido.utils.UtilConv;

@ExtendWith(MockitoExtension.class)
public class WorkTimeServiceUnitTest {

    @InjectMocks
    private WorkTimeService        target;

    @Mock
    private WorkTimeItemRepository repository;

    @Spy
    private TimeService            timeService;

    private final String           expectedUsername   = "username";
    private final OffsetDateTime   currentTimeStamp   = UtilConv.str2OffsetDateTime("20201018183100");
    private final OffsetDateTime   startItemTimeStamp = UtilConv.str2OffsetDateTime("20201018184400");
    private final OffsetDateTime   endItemTimeStamp   = UtilConv.str2OffsetDateTime("20201018184500");
    private final LocalDate        currentDay         = UtilConv.str2LocalDate("20201018");

    @Test
    public void testStartWorkWithoutUnfinishedItem() {
        //given
        when(repository.loadTodayUnfinishedItemForUsername(expectedUsername)).thenReturn(null);
        when(timeService.getCurrentOffsetDateTime()).thenReturn(currentTimeStamp);
        when(timeService.getCurrentLocalDate()).thenReturn(currentDay);

        //when
        final InfoResponse response = target.startWork(expectedUsername);

        //then
        assertNotNull(response);
        assertEquals("OK", response.getMessage());
        verify(repository, Mockito.times(1)).saveNewItem(argThat(item -> item.startItem.equals(currentTimeStamp)
                && item.day.equals(currentDay)
                && item.username.equals(expectedUsername) && item.endItem == null && item.special == null));

    }

    @Test
    public void testStartWorkWithUnfinishedItem() {
        //given
        final WorkTimeItem unfinishedItem = new WorkTimeItem();
        unfinishedItem.startItem = startItemTimeStamp;
        when(repository.loadTodayUnfinishedItemForUsername(expectedUsername)).thenReturn(unfinishedItem);

        //when
        final AlreadyStartedWorkException exception = Assertions.assertThrows(AlreadyStartedWorkException.class, () -> {
            target.startWork(expectedUsername);
        });

        //then
        assertEquals("Work already started at: " + startItemTimeStamp, exception.getMessage());
    }

    @Test
    public void testEndWorkWithoutUnfinishedItem() {
        //given
        when(repository.loadTodayUnfinishedItemForUsername(expectedUsername)).thenReturn(null);

        //when
        Assertions.assertThrows(NotYetStartedWorkException.class, () -> {
            target.endWork(expectedUsername);
        });
    }

    @Test
    public void testEndWorkWithUnfinishedItem() {
        //given
        final WorkTimeItem item = new WorkTimeItem();
        when(repository.loadTodayUnfinishedItemForUsername(expectedUsername)).thenReturn(item);
        when(timeService.getCurrentOffsetDateTime()).thenReturn(endItemTimeStamp);

        //when
        final InfoResponse response = target.endWork(expectedUsername);

        //then
        assertNotNull(response);
        assertEquals("OK", response.getMessage());
        verify(repository, Mockito.times(1)).updateItem(argThat(i -> i == item && i.endItem.equals(endItemTimeStamp)));

    }

    @Test
    public void testStartLanch() {
        //given
        final WorkTimeItem item = new WorkTimeItem();
        when(repository.loadTodayUnfinishedItemForUsername(expectedUsername)).thenReturn(item);
        when(timeService.getCurrentOffsetDateTime()).thenReturn(endItemTimeStamp);

        //when
        final InfoResponse response = target.startLunch(expectedUsername);

        //then
        assertNotNull(response);
        assertEquals("OK", response.getMessage());
        verify(repository, Mockito.times(1))
                .updateItem(argThat(i -> i == item && i.endItem.equals(endItemTimeStamp) && i.special.equals(LUNCH)));
    }

    @Test
    public void testSummary_zeroRecord() {
        //when
        final WorkTimeSummary response = target.generateSummary(expectedUsername);

        //then
        assertNotNull(response);
        assertEquals("8:20:00", response.getRemainingTime());
        assertEquals("0:00:00", response.getWorkedTime());
    }

    @Test
    public void testSummary_oneRecord() {
        final List<WorkTimeItem> items = TestUtils.createWorkTimeItemListOne("20201015090000", "20201015160000");
        when(repository.getAllItemsTodayForUsernameOrderedByStartItem(expectedUsername)).thenReturn(items);
        //when
        final WorkTimeSummary response = target.generateSummary(expectedUsername);

        //then
        assertNotNull(response);
        assertEquals("1:20:00", response.getRemainingTime());
        assertEquals("7:00:00", response.getWorkedTime());
    }

    @Test
    public void testSummary_twoRecords() {
        final List<WorkTimeItem> items = TestUtils.createWorkTimeItemListTwo("20201015090000", "20201015100000",
                "20201015110000", "20201015120000", null);
        when(repository.getAllItemsTodayForUsernameOrderedByStartItem(expectedUsername)).thenReturn(items);
        //when
        final WorkTimeSummary response = target.generateSummary(expectedUsername);

        //then
        assertNotNull(response);
        assertEquals("6:20:00", response.getRemainingTime());
        assertEquals("2:00:00", response.getWorkedTime());
    }

    @Test
    public void testSummary_twoRecordsWithLanch() {
        final List<WorkTimeItem> items = TestUtils.createWorkTimeItemListTwo("20201015090000", "20201015100000",
                "20201015110000", "20201015120000", WorkTimeItemSpecial.LUNCH);
        when(repository.getAllItemsTodayForUsernameOrderedByStartItem(expectedUsername)).thenReturn(items);
        //when
        final WorkTimeSummary response = target.generateSummary(expectedUsername);

        //then
        assertNotNull(response);
        assertEquals("6:00:00", response.getRemainingTime());
        assertEquals("2:00:00", response.getWorkedTime());
    }

    @Test
    public void testSummary_oneRecordNotClosed() {
        //given
        final List<WorkTimeItem> items = TestUtils.createWorkTimeItemListOne("20201015090000", null);
        when(repository.getAllItemsTodayForUsernameOrderedByStartItem(expectedUsername)).thenReturn(items);
        when(timeService.getCurrentOffsetDateTime()).thenReturn(UtilConv.str2OffsetDateTime("20201015103000"));

        //when
        final WorkTimeSummary response = target.generateSummary(expectedUsername);

        //then
        assertNotNull(response);
        assertEquals("6:50:00", response.getRemainingTime());
        assertEquals("1:30:00", response.getWorkedTime());
    }

    @Test
    public void testSummary_oneRecordNotClosedWithEmptyString() {
        //given
        final List<WorkTimeItem> items = TestUtils.createWorkTimeItemListOne("20201015090000", "");
        when(repository.getAllItemsTodayForUsernameOrderedByStartItem(expectedUsername)).thenReturn(items);
        when(timeService.getCurrentOffsetDateTime()).thenReturn(UtilConv.str2OffsetDateTime("20201015103000"));

        //when
        final WorkTimeSummary response = target.generateSummary(expectedUsername);

        //then
        assertNotNull(response);
        assertEquals("6:50:00", response.getRemainingTime());
        assertEquals("1:30:00", response.getWorkedTime());
    }
}
