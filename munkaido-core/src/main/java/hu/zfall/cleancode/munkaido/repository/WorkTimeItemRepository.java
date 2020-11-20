package hu.zfall.cleancode.munkaido.repository;

import java.util.List;
import hu.zfall.cleancode.munkaido.domain.WorkTimeItem;

public interface WorkTimeItemRepository {

    WorkTimeItem loadTodayUnfinishedItemForUsername(String username);

    void saveNewItem(WorkTimeItem item);

    void updateItem(WorkTimeItem item);

    List<WorkTimeItem> getAllItemsTodayForUsernameOrderedByStartItem(String username);

}
