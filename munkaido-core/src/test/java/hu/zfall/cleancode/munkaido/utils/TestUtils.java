package hu.zfall.cleancode.munkaido.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import hu.zfall.cleancode.munkaido.domain.WorkTimeItem;
import hu.zfall.cleancode.munkaido.domain.WorkTimeItemSpecial;

public class TestUtils {

    public static List<WorkTimeItem> createWorkTimeItemListOne(final String startItem, final String endItem) {
        final WorkTimeItem item = new WorkTimeItem();
        item.startItem = UtilConv.str2OffsetDateTime(startItem);
        item.endItem = UtilConv.str2OffsetDateTime(endItem);
        final List<WorkTimeItem> items = new ArrayList<>();
        items.add(item);
        return items;
    }

    public static List<WorkTimeItem> createWorkTimeItemListTwo(final String startItem, final String endItem,
                                                               final String startItem2, final String endItem2,
                                                               final WorkTimeItemSpecial special) {
        final List<WorkTimeItem> items = new ArrayList<>();
        WorkTimeItem item = new WorkTimeItem();
        item.startItem = UtilConv.str2OffsetDateTime(startItem);
        item.endItem = UtilConv.str2OffsetDateTime(endItem);
        item.special = special;
        items.add(item);
        item = new WorkTimeItem();
        item.startItem = UtilConv.str2OffsetDateTime(startItem2);
        item.endItem = UtilConv.str2OffsetDateTime(endItem2);
        items.add(item);
        return items;
    }

    public static void setPrivateMember(Object object, String memberName, Object memberValue) {
        try {
            Field field = object.getClass().getDeclaredField(memberName);
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(object, memberValue);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
