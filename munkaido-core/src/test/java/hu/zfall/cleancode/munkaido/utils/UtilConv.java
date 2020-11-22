package hu.zfall.cleancode.munkaido.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.zone.ZoneRules;
import org.apache.commons.lang3.StringUtils;

public class UtilConv {

    private static final ZoneRules         DEFAULT_ZONE_RULES        = ZoneId.systemDefault().getRules();
    private static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter LOCAL_DATE_FORMATTER      = DateTimeFormatter.ofPattern("yyyyMMdd");

    private UtilConv() {
        // only static methods
    }

    public static OffsetDateTime localDateTime2OffsetDateTime(final LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atOffset(DEFAULT_ZONE_RULES.getOffset(localDateTime));
    }

    public static LocalDateTime offsetDateTime2LocalDateTime(final OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }
        return offsetDateTime.toLocalDateTime();
    }

    public static String offsetDateTime2Str(final OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }
        return offsetDateTime.format(LOCAL_DATE_TIME_FORMATTER);
    }

    public static OffsetDateTime str2OffsetDateTime(final String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        return localDateTime2OffsetDateTime(str2LocalDateTime(s));
    }

    public static String localDateTime2Str(final LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }
        return localDateTime.format(LOCAL_DATE_TIME_FORMATTER);
    }

    public static LocalDateTime str2LocalDateTime(final String s) {
        try {
            return LocalDateTime.parse(s, LOCAL_DATE_TIME_FORMATTER);
        } catch (final DateTimeParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static LocalDate str2LocalDate(final String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        try {
            return LocalDate.parse(s.length() > 8 ? s.substring(0, 8) : s, LOCAL_DATE_FORMATTER);
        } catch (final DateTimeParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String localDate2Str(final LocalDate localDate) {
        if (localDate == null) {
            return "";
        }
        return localDate.format(LOCAL_DATE_FORMATTER);
    }
}
