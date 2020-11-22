package hu.zfall.cleancode.munkaido.utils;

import java.sql.SQLException;
import java.sql.Statement;

public interface StatementConsumer {

    void accept(Statement s) throws SQLException;
}
