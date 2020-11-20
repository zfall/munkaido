package hu.zfall.cleancode.munkaido.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.stereotype.Service;
import hu.zfall.cleancode.munkaido.exception.MunkaidoException;

@Service
public class TimeService {
    public static final String TIMESTAMP_DAO_PATTERN = "yyyyMMddHHmmss";

    public String getCurrentDay() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date());
    }

    public java.sql.Date getCurrentDaySQL() {
        return new java.sql.Date(new Date().getTime());
    }

    public String getCurrentTimestamp() {
        final SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_DAO_PATTERN);
        return sdf.format(new Date());
    }

    public Date getCurrentUtilDate() {
        return new Date();
    }

    public Date convertDateFromTimestampString(final String timestamp) {
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_DAO_PATTERN);
            return sdf.parse(timestamp);
        } catch (final ParseException e) {
            throw new MunkaidoException("Exception in convertDateFromTimestampString", e);
        }
    }

    public String printHoursMinutesSeconds(final long milliseconds) {
        final StringBuilder b = new StringBuilder();
        b.append(milliseconds / 3600000);
        b.append(":");
        int restMillis = (int)milliseconds % 3600000;
        final int minutes = restMillis / 60000;
        if (minutes < 10) {
            b.append("0");
        }
        b.append(minutes);
        b.append(":");
        restMillis = restMillis % 60000;
        final int seconds = restMillis / 1000;
        if (seconds < 10) {
            b.append("0");
        }
        b.append(seconds);
        return b.toString();

    }
}
