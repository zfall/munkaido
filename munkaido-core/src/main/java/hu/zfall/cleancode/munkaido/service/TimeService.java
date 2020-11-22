package hu.zfall.cleancode.munkaido.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;

@Service
public class TimeService {
    public static final String TIMESTAMP_DAO_PATTERN = "yyyyMMddHHmmss";

    public LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }

    public OffsetDateTime getCurrentOffsetDateTime() {
        return OffsetDateTime.now();
    }

    public String prettyPrintTime(final long timeSec) {
        long timeSeconds = timeSec;
        final StringBuilder b = new StringBuilder();
        if (timeSeconds < 0) {
            b.append("-");
            timeSeconds = timeSeconds * (-1);
        }
        b.append(timeSeconds / 3600);
        b.append(":");
        int restSeconds = (int)timeSeconds % 3600;
        final int minutes = restSeconds / 60;
        if (minutes < 10) {
            b.append("0");
        }
        b.append(minutes);
        b.append(":");
        restSeconds = restSeconds % 60;
        if (restSeconds < 10) {
            b.append("0");
        }
        b.append(restSeconds);
        return b.toString();

    }
}
