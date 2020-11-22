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

    private static final String            username   = "zoli";

    private StartEndWorkRegistration       workReg    = null;

    private WorkTimeItemInMemoryRepository repository = new WorkTimeItemInMemoryRepository();

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
    public void testStartWorkEnd() {
        workReg.startWork(username);
        workReg.endWork(username);
        List<WorkTimeItem> itemList = repository.getAllItemsTodayForUsernameOrderedByStartItem(username);

        Assertions.assertEquals(1, itemList.size());
        WorkTimeItem item = itemList.get(0);
        Assertions.assertNotNull(item.endItem);
        Assertions.assertNull(item.special);
    }

    @Test
    public void testStartWorkLunch() {
        workReg.startWork(username);
        workReg.startLanch(username);
        List<WorkTimeItem> itemList = repository.getAllItemsTodayForUsernameOrderedByStartItem(username);

        Assertions.assertEquals(1, itemList.size());
        WorkTimeItem item = itemList.get(0);
        Assertions.assertNotNull(item.endItem);
        Assertions.assertNotNull(item.special);
    }

    @Test
    public void testStartWorkLunchBack() {
        workReg.startWork(username);
        workReg.startLanch(username);
        sleep(1000);
        workReg.endLanch(username);
        List<WorkTimeItem> itemList = repository.getAllItemsTodayForUsernameOrderedByStartItem(username);

        Assertions.assertEquals(2, itemList.size());
        WorkTimeItem item1 = itemList.get(0);
        Assertions.assertNotNull(item1.endItem);
        Assertions.assertNotNull(item1.special);
        WorkTimeItem item2 = itemList.get(1);
        Assertions.assertNull(item2.endItem);
        Assertions.assertNull(item2.special);
    }

    @Test
    public void testStartWorkLunchBackEnd() {
        workReg.startWork(username);
        workReg.startLanch(username);
        sleep(1000);
        workReg.endLanch(username);
        workReg.endWork(username);
        List<WorkTimeItem> itemList = repository.getAllItemsTodayForUsernameOrderedByStartItem(username);

        Assertions.assertEquals(2, itemList.size());
        WorkTimeItem item1 = itemList.get(0);
        Assertions.assertNotNull(item1.endItem);
        Assertions.assertNotNull(item1.special);
        WorkTimeItem item2 = itemList.get(1);
        Assertions.assertNotNull(item2.endItem);
        Assertions.assertNull(item2.special);
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
