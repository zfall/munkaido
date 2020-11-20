package hu.zfall.cleancode.munkaido.utils;

import java.util.ArrayList;
import java.util.List;
import hu.zfall.cleancode.munkaido.domain.WorkTimeItem;
import hu.zfall.cleancode.munkaido.domain.WorkTimeItemSpecial;

public class TestUtils {

    public static List<WorkTimeItem> createWorkTimeItemListOne(final String startitem, final String enditem) {
        final WorkTimeItem item = new WorkTimeItem();
        item.setStartitem(startitem);
        item.setEnditem(enditem);
        final List<WorkTimeItem> items = new ArrayList<>();
        items.add(item);
        return items;
    }

    public static List<WorkTimeItem> createWorkTimeItemListTwo(final String startitem, final String enditem,
                                                               final String startitem2, final String enditem2,
                                                               final WorkTimeItemSpecial special) {
        final List<WorkTimeItem> items = new ArrayList<>();
        WorkTimeItem item = new WorkTimeItem();
        item.setStartitem(startitem);
        item.setEnditem(enditem);
        item.setSpecial(special);
        items.add(item);
        item = new WorkTimeItem();
        item.setStartitem(startitem2);
        item.setEnditem(enditem2);
        items.add(item);
        return items;
    }

}
