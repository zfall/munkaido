package hu.zfall.cleancode.munkaido.domain;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class WorkTimeItem {

    public String              username;

    public LocalDate           day;

    public OffsetDateTime      startItem;

    public OffsetDateTime      endItem;

    public WorkTimeItemSpecial special;

    public WorkTimeItem() {
    }

    public WorkTimeItem(String username, LocalDate day, OffsetDateTime startItem, OffsetDateTime endItem,
            WorkTimeItemSpecial special) {
        this.username = username;
        this.day = day;
        this.startItem = startItem;
        this.endItem = endItem;
        this.special = special;
    }

}
