package spending_management_project.tools;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Component
public class DateUtil {
    public static String dateToString(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        return df.format(date);
    }

    public static Date stringToDate(String date, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.parse(date);
    }

    public static String formatStringDate(String date, String format, String newFormat) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        SimpleDateFormat newDateFormat = new SimpleDateFormat(newFormat, Locale.getDefault());
        return newDateFormat.format(dateFormat.parse(date));
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate getFirstDayOfYear() {
        return LocalDate.now().with(firstDayOfYear());
    }

    public static LocalDate getLastDayOfYear() {
        return LocalDate.now().with(lastDayOfYear());
    }

    public static LocalDate getDayAgo(int dayAgo) {
        return LocalDate.now().minusDays(dayAgo);
    }
    public static LocalDate getFirstDayMonth(){
        return LocalDate.now().with(firstDayOfMonth());
    }
    public static LocalDate getLastDayOfMonth(){
        return LocalDate.now().with(lastDayOfMonth());
    }
}
