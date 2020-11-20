package hu.zfall.cleancode.munkaido.service;

import static hu.zfall.cleancode.munkaido.domain.WorkTimeItemSpecial.LUNCH;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hu.zfall.cleancode.munkaido.domain.WorkTimeItem;
import hu.zfall.cleancode.munkaido.domain.WorkTimeItemSpecial;
import hu.zfall.cleancode.munkaido.dto.InfoResponse;
import hu.zfall.cleancode.munkaido.dto.WorkTimeSummary;
import hu.zfall.cleancode.munkaido.exception.AlreadyStartedWorkException;
import hu.zfall.cleancode.munkaido.exception.NotYetStartedWorkException;
import hu.zfall.cleancode.munkaido.repository.WorkTimeItemRepository;

@Service
public class WorkTimeService {

    @Autowired
    private WorkTimeItemRepository repository;

    @Autowired
    private TimeService            timeService;

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

    public InfoResponse endWork(final String username) {
        return endWork(username, null);
    }

    public InfoResponse startLanch(final String username) {
        return endWork(username, WorkTimeItemSpecial.LUNCH);
    }

    private InfoResponse endWork(final String username, final WorkTimeItemSpecial special) {
        final WorkTimeItem item = repository.loadTodayUnfinishedItemForUsername(username);

        if (item == null) {
            throw new NotYetStartedWorkException();
        }

        item.endItem = timeService.getCurrentTimestamp();
        item.special = special;

        repository.updateItem(item);

        return new InfoResponse("OK");
    }

    private WorkTimeItem createNewWorkTimeItem(final String username) {
        final WorkTimeItem item = new WorkTimeItem();
        item.day = timeService.getCurrentDay();
        item.startItem = timeService.getCurrentTimestamp();
        item.username = username;
        return item;
    }

    public WorkTimeSummary getSummary(final String username) {
        final List<WorkTimeItem> items = repository.getAllItemsTodayForUsernameOrderedByStartItem(username);
        int millisecondsWorked = 0;
        boolean wasLunch = false;

        for (final WorkTimeItem item : items) {
            if (LUNCH.equals(item.special)) {
                wasLunch = true;
            }
            final long start = timeService.convertDateFromTimestampString(item.startItem).getTime();
            final long end = (StringUtils.isBlank(item.endItem) ? timeService.getCurrentUtilDate()
                    : timeService.convertDateFromTimestampString(item.endItem)).getTime();
            millisecondsWorked = millisecondsWorked + (int)(end - start);
        }
        int millisecondsToWork = wasLunch ? 8 * 60 * 60 * 1000 : (8 * 60 + 20) * 60 * 1000;
        millisecondsToWork = millisecondsToWork - millisecondsWorked;
        return new WorkTimeSummary(timeService.printHoursMinutesSeconds(millisecondsWorked),
                timeService.printHoursMinutesSeconds(millisecondsToWork));
    }

}
