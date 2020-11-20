package hu.zfall.cleancode.munkaido.utils;

import java.util.ArrayList;
import java.util.List;
import hu.zfall.cleancode.munkaido.domain.WorkTimeItem;
import hu.zfall.cleancode.munkaido.domain.WorkTimeItemSpecial;

public class TestUtils {

    public static List<WorkTimeItem> createWorkTimeItemListOne(final String startItem, final String endItem) {
        final WorkTimeItem item = new WorkTimeItem();
        item.startItem = startItem;
        item.endItem = endItem;
        final List<WorkTimeItem> items = new ArrayList<>();
        items.add(item);
        return items;
    }

    public static List<WorkTimeItem> createWorkTimeItemListTwo(final String startItem, final String endItem,
                                                               final String startItem2, final String endItem2,
                                                               final WorkTimeItemSpecial special) {
        final List<WorkTimeItem> items = new ArrayList<>();
        WorkTimeItem item = new WorkTimeItem();
        item.startItem = startItem;
        item.endItem = endItem;
        item.special = special;
        items.add(item);
        item = new WorkTimeItem();
        item.startItem = startItem2;
        item.endItem = endItem2;
        items.add(item);
        return items;
    }

}
