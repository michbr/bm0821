import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static final String DATE_FORMAT = "MM/dd/yy";

    public static void validateDate(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        simpleDateFormat.parse(date);
    }

    public static LocalDate parseDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static String getDueDate(String startDate, int days) throws ParseException {
        LocalDate date = parseDate(startDate);
        return date.plusDays(days).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static int getChargedDays(String checkoutDate, int days, boolean weekdays, boolean weekends, boolean holidays) {
        LocalDate date = parseDate(checkoutDate);
        LocalDate next = date.plusDays(1);
        int chargedDays = 0;
        for (int i = 0; i < days; ++i) {
            if (isHoliday(next) && !holidays) {
                next = next.plusDays(1);
                continue;
            }

            if (weekdays && isWeekday(next)) {
                ++chargedDays;
            } else if(weekends && isWeekend(next)) {
                ++chargedDays;
            }
            next = next.plusDays(1);
        }
        return chargedDays;
    }

    private static boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private static boolean isWeekday(LocalDate date) {
        return !isWeekend(date);
    }

    private static boolean isHoliday(LocalDate date) {


        return false;
    }
}
