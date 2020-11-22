package hu.zfall.cleancode.munkaido.boundaries;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import hu.zfall.cleancode.munkaido.domain.WorkTimeItem;
import hu.zfall.cleancode.munkaido.repository.WorkTimeItemInMemoryRepository;
import hu.zfall.cleancode.munkaido.service.TimeService;
import hu.zfall.cleancode.munkaido.service.WorkTimeService;
import hu.zfall.cleancode.munkaido.utils.TestUtils;

public class StartEndWorkRegistrationPackageTest {

    private StartEndWorkRegistration workReg    = null;

    WorkTimeItemInMemoryRepository   repository = new WorkTimeItemInMemoryRepository();

    private static final String      username   = "zoli";

    @BeforeEach
    private void init() {
        WorkTimeService service = new WorkTimeService();
        TimeService timeService = new TimeService();
        TestUtils.setPrivateMember(service, "timeService", timeService);
        TestUtils.setPrivateMember(service, "repository", repository);
        TestUtils.setPrivateMember(repository, "timeService", timeService);
        workReg = service;
    }

    @Test
    public void testStartWork() {
        workReg.startWork(username);
        List<WorkTimeItem> itemList = repository.getAllItemsTodayForUsernameOrderedByStartItem(username);

        Assertions.assertEquals(1, itemList.size());
        WorkTimeItem item = itemList.get(0);
        Assertions.assertNull(item.endItem);
        Assertions.assertNull(item.special);
    }

    @Test
    public void testStartEndWork() {
        workReg.startWork(username);
        List<WorkTimeItem> itemList = repository.getAllItemsTodayForUsernameOrderedByStartItem(username);
        workReg.endWork(username);
        itemList = repository.getAllItemsTodayForUsernameOrderedByStartItem(username);

        Assertions.assertEquals(1, itemList.size());
        WorkTimeItem item = itemList.get(0);
        Assertions.assertNotNull(item.endItem);
        Assertions.assertNull(item.special);
    }
}
