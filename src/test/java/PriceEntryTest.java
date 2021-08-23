import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriceEntryTest {

    @Test
    public void testCreate() {
        PriceEntry priceEntry = new PriceEntry("Ladder", 1.99, true, true, true);
        assertEquals("Ladder", priceEntry.type());
        assertEquals(1.99, priceEntry.dailyCharge());
        assertTrue(priceEntry.holidayCharge());
        assertTrue(priceEntry.weekdayCharge());
        assertTrue(priceEntry.weekendCharge());
    }

}