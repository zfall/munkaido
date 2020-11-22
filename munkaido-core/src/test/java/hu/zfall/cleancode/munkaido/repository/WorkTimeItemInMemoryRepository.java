package hu.zfall.cleancode.munkaido.repository;

import static hu.zfall.cleancode.munkaido.utils.UtilConv.str2LocalDate;
import static hu.zfall.cleancode.munkaido.utils.UtilConv.str2OffsetDateTime;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.sqlite.SQLiteDataSource;
import hu.zfall.cleancode.munkaido.domain.WorkTimeItem;
import hu.zfall.cleancode.munkaido.domain.WorkTimeItemSpecial;
import hu.zfall.cleancode.munkaido.service.TimeService;
import hu.zfall.cleancode.munkaido.utils.StatementConsumer;
import hu.zfall.cleancode.munkaido.utils.UtilConv;

public class WorkTimeItemInMemoryRepository implements WorkTimeItemRepository {

    private DataSource  dataSource;

    private TimeService timeService;

    public WorkTimeItemInMemoryRepository() {
        final SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl("jdbc:sqlite:sample.db");
        dataSource = ds;

        executeStatement(statement -> {
            statement.setQueryTimeout(30);

            statement.executeUpdate("drop table if exists work_time_item");
            statement.executeUpdate("CREATE TABLE work_time_item\r\n"
                    + "( username string NOT NULL\r\n"
                    + ", day string NOT NULL \r\n"
                    + ", startItem string NOT NULL\r\n"
                    + ", endItem string\r\n"
                    + ", special string\r\n"
                    + ")");

        });
    }

    @Override
    public List<WorkTimeItem> getAllItemsTodayForUsernameOrderedByStartItem(String username) {
        List<WorkTimeItem> itemList = new ArrayList<>();

        String currentDay = UtilConv.localDate2Str(timeService.getCurrentLocalDate());
        String sql = "select * from work_time_item where username = '" + username + "' and day = '" + currentDay
                + "' ORDER BY startItem";

        executeStatement(statement -> {
            try (ResultSet rs = statement.executeQuery(sql)) {
                mapWorkTimeItems(rs, itemList);
            }
        });

        return itemList;
    }

    @Override
    public WorkTimeItem loadTodayUnfinishedItemForUsername(String username) {
        List<WorkTimeItem> itemList = new ArrayList<>();

        String currentDay = UtilConv.localDate2Str(timeService.getCurrentLocalDate());
        String sql = "select * from work_time_item where username = '" + username + "' and day = '" + currentDay
                + "' and endItem is null";

        executeStatement(statement -> {
            try (ResultSet rs = statement.executeQuery(sql)) {
                mapWorkTimeItems(rs, itemList);
            }
        });

        return itemList.isEmpty() ? null : itemList.get(0);
    }

    @Override
    public void saveNewItem(WorkTimeItem item) {
        String endItemValue = UtilConv.offsetDateTime2Str(item.endItem);
        String sql = "insert into work_time_item values('" + item.username + "','"
                + UtilConv.localDate2Str(item.day) + "','"
                + UtilConv.offsetDateTime2Str(item.startItem) + "',"
                + (endItemValue == null ? "null," : "'" + endItemValue + "',")
                + (item.special == null ? "null" : "'" + item.special.getValue() + "'") + ")";

        executeStatement(statement -> statement.executeUpdate(sql));
    }

    @Override
    public void updateItem(WorkTimeItem item) {
        String sql = "update work_time_item set endItem = '" + UtilConv.offsetDateTime2Str(item.endItem) + "',"
                + " special = " + (item.special == null ? "null" : "'" + item.special.getValue() + "'")
                + " WHERE username = '" + item.username + "' AND day = '" + UtilConv.localDate2Str(item.day) + "'"
                + " AND startItem = '" + UtilConv.offsetDateTime2Str(item.startItem) + "'";

        executeStatement(statement -> statement.executeUpdate(sql));
    }

    public void executeStatement(StatementConsumer sc) {
        try {
            try (final Connection connection = dataSource.getConnection();
                    final Statement statement = connection.createStatement()) {
                sc.accept(statement);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void mapWorkTimeItems(ResultSet rs, List<WorkTimeItem> itemList) throws SQLException {
        while (rs.next()) {
            itemList.add(new WorkTimeItem(rs.getString("username"), str2LocalDate(rs.getString("day")),
                    str2OffsetDateTime(rs.getString("startItem")), str2OffsetDateTime(rs.getString("endItem")),
                    WorkTimeItemSpecial.getByValue(rs.getString("special"))));
        }
    }

}
