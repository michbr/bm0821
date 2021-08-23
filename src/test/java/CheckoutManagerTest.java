import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutManagerTest {

    @Test
    public void test2() throws IOException, ParseException, RentalAgreement.InvalidFieldException {
        CheckoutManager checkoutManager = new CheckoutManager(new Catalog("prices.json", "tools.json"));
        RentalAgreement rentalAgreement = checkoutManager.checkout("LADW", 3, 10, "07/02/20");
        assertNotNull(rentalAgreement);

        assertEquals("LADW", rentalAgreement.getToolCode());
        assertEquals("Ladder", rentalAgreement.getToolType());
        assertEquals("Werner", rentalAgreement.getToolBrand());
        assertEquals(3, rentalAgreement.getRentalDays());
        assertEquals("07/02/20", rentalAgreement.getCheckoutDate().toString());
        assertEquals(1.99, rentalAgreement.getDailyRentalCharge());
        assertEquals(10, rentalAgreement.getDiscountPercent());
        assertEquals(2, rentalAgreement.getChargeDays());
        assertEquals(3.0*0.99, rentalAgreement.getPreDiscountCharge());
        assertEquals((3.0*0.99)*.2, rentalAgreement.getDiscountAmount());
        assertEquals(3.0*0.99 - (3.0*0.99)*.2, rentalAgreement.getFinalCharge());
        assertEquals("03/19/16", rentalAgreement.getDueDate());
    }

}