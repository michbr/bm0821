import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
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

    public static String getDueDate(String startDate, int days) {
        LocalDate date = parseDate(startDate);
        return date.plusDays(days).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static int getChargedDays(String checkoutDate, int days, boolean weekdays, boolean weekends, boolean holidays) {
        LocalDate date = parseDate(checkoutDate);
        LocalDate next = date.plusDays(1);
        int chargedDays = 0;
        // Walk through the days in the agreement and count the days that should be charged
        for (int i = 0; i < days; ++i) {
            // if it is a holiday and we don't charge for holidays, go to the next day without counting it
            if (isHoliday(next) && !holidays) {
                next = next.plusDays(1);
                continue;
            }

            // Add weekdays if we charge or weekends if we charge.
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
        // if we're in September, and the day is the 7th or earlier and it's a monday, it's the first monday
        // therefore labor day
        if (date.getMonth() == Month.SEPTEMBER && date.getDayOfMonth() <= 7 && date.getDayOfWeek() == DayOfWeek.MONDAY) {
            return true;
        }

        // If we're in July, check for Independence Day
        if (date.getMonth() == Month.JULY) {
            // If it's Friday the 3rd, the 4th is on saturday, so the holiday is observed on the 3rd
            if (date.getDayOfWeek() == DayOfWeek.FRIDAY && date.getDayOfMonth() == 3) {
                return true;
            }

            // similarly, Monday the 5th is a holiday
            if (date.getDayOfWeek() == DayOfWeek.MONDAY && date.getDayOfMonth() == 5) {
                return true;
            }

            // if it's the 4th and a weekday, then holiday
            if (isWeekday(date) && date.getDayOfMonth() == 4) {
                return true;
            }
        }

        return false;
    }
}
