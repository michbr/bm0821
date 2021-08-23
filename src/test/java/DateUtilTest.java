import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {

    @Test
    public void testParseDate() throws ParseException {
        LocalDate date = DateUtil.parseDate("09/21/20");
        assertEquals("09/21/20", date.format(DateTimeFormatter.ofPattern("MM/dd/yy")));
    }

    @Test
    public void testValidateDate() throws ParseException {
        DateUtil.validateDate("09/21/20");
    }

    @Test
    public void testValidateDate_incorrectFormat() throws ParseException {
        assertThrows(ParseException.class, () -> DateUtil.validateDate("09-21-20"));
    }

    @Test
    public void testGetDueDate() throws ParseException {
        assertEquals("09/25/20", DateUtil.getDueDate("09/21/20", 4));
    }

    @Test
    public void testGetDueDate_monthChange() throws ParseException {
        assertEquals("10/03/20", DateUtil.getDueDate("09/29/20", 4));
    }

    @Test
    public void testGetChargedDays() {
        assertEquals(2, DateUtil.getChargedDays("09/18/20", 4, true, false, false));
    }

}