package hu.zfall.cleancode.munkaido.service;

import static hu.zfall.cleancode.munkaido.domain.WorkTimeItemSpecial.LUNCH;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hu.zfall.cleancode.munkaido.boundaries.StartEndWorkRegistration;
import hu.zfall.cleancode.munkaido.boundaries.WorkReport;
import hu.zfall.cleancode.munkaido.boundaries.dto.InfoResponse;
import hu.zfall.cleancode.munkaido.boundaries.dto.WorkTimeSummary;
import hu.zfall.cleancode.munkaido.domain.WorkTimeItem;
import hu.zfall.cleancode.munkaido.domain.WorkTimeItemSpecial;
import hu.zfall.cleancode.munkaido.exception.AlreadyStartedWorkException;
import hu.zfall.cleancode.munkaido.exception.NotYetStartedWorkException;
import hu.zfall.cleancode.munkaido.repository.WorkTimeItemRepository;

@Service
public class WorkTimeService implements WorkReport, StartEndWorkRegistration {

    private static final long      DAILY_WORKTIME_SECONDS = 8 * 60 * 60;
    private static final long      LUNCHBREAK_SECONDS     = 20 * 60;

    @Autowired
    private WorkTimeItemRepository repository;

    @Autowired
    private TimeService            timeService;

    @Override
    @Transactional
    public InfoResponse startWork(final String username) {
        WorkTimeItem item = repository.loadTodayUnfinishedItemForUsername(username);

        if (item != null) {
            throw new AlreadyStartedWorkException("Work already started at: " + item.startItem);
        }

        item = createNewWorkTimeItem(username);

        repository.saveNewItem(item);

        return new InfoResponse("OK");
    }

    @Override
    public InfoResponse endLunch(String username) {
        return startWork(username);
    }

    @Override
    public InfoResponse endWork(final String username) {
        return endWork(username, null);
    }

    @Override
    public InfoResponse startLunch(final String username) {
        return endWork(username, WorkTimeItemSpecial.LUNCH);
    }

    private InfoResponse endWork(final String username, final WorkTimeItemSpecial special) {
        final WorkTimeItem item = repository.loadTodayUnfinishedItemForUsername(username);

        if (item == null) {
            throw new NotYetStartedWorkException();
        }

        item.endItem = timeService.getCurrentOffsetDateTime();
        item.special = special;

        repository.updateItem(item);

        return new InfoResponse("OK");
    }

    private WorkTimeItem createNewWorkTimeItem(final String username) {
        final WorkTimeItem item = new WorkTimeItem();
        item.day = timeService.getCurrentLocalDate();
        item.startItem = timeService.getCurrentOffsetDateTime();
        item.username = username;
        return item;
    }

    @Override
    public WorkTimeSummary generateSummary(final String username) {
        final List<WorkTimeItem> items = repository.getAllItemsTodayForUsernameOrderedByStartItem(username);
        long secondsWorked = 0;
        boolean wasLunch = false;

        OffsetDateTime now = timeService.getCurrentOffsetDateTime();
        for (final WorkTimeItem item : items) {
            if (LUNCH.equals(item.special)) {
                wasLunch = true;
            }
            Duration d = Duration.between(item.startItem, (item.endItem == null ? now : item.endItem));
            secondsWorked = secondsWorked + d.getSeconds();
        }
        long secondsRemaining = wasLunch ? DAILY_WORKTIME_SECONDS : DAILY_WORKTIME_SECONDS + LUNCHBREAK_SECONDS;
        secondsRemaining = secondsRemaining - secondsWorked;
        return new WorkTimeSummary(timeService.prettyPrintTime(secondsWorked), timeService.prettyPrintTime(secondsRemaining));
    }

}
