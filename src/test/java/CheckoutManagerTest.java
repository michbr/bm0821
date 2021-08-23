import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutManagerTest {

    @Test
    public void test1() throws IOException {

        CheckoutManager checkoutManager = new CheckoutManager(new Catalog("prices.json", "tools.json"));
        assertThrows(RentalAgreement.InvalidFieldException.class, () -> checkoutManager.checkout("JAKR", 0, 101, null));
    }

    @Test
    public void test2() throws IOException, ParseException, RentalAgreement.InvalidFieldException {
        CheckoutManager checkoutManager = new CheckoutManager(new Catalog("prices.json", "tools.json"));
        RentalAgreement rentalAgreement = checkoutManager.checkout("LADW", 3, 10, "07/02/20");
        assertNotNull(rentalAgreement);

        assertEquals("LADW", rentalAgreement.getToolCode());
        assertEquals("Ladder", rentalAgreement.getToolType());
        assertEquals("Werner", rentalAgreement.getToolBrand());
        assertEquals(3, rentalAgreement.getRentalDays());
        assertEquals("07/02/20", rentalAgreement.getCheckoutDate());
        assertEquals(1.99, rentalAgreement.getDailyRentalCharge());
        assertEquals(10, rentalAgreement.getDiscountPercent());
        assertEquals(2, rentalAgreement.getChargeDays());
        assertEquals(3.98, rentalAgreement.getPreDiscountCharge());
        assertEquals(0.4, rentalAgreement.getDiscountAmount());
        assertEquals(3.58, rentalAgreement.getFinalCharge());
        assertEquals("07/05/20", rentalAgreement.getDueDate());
    }

    @Test
    public void test3() throws ParseException, RentalAgreement.InvalidFieldException, IOException {
        CheckoutManager checkoutManager = new CheckoutManager(new Catalog("prices.json", "tools.json"));
        RentalAgreement rentalAgreement = checkoutManager.checkout("CHNS", 5, 25, "07/02/15");
        assertNotNull(rentalAgreement);

        assertEquals("CHNS", rentalAgreement.getToolCode());
        assertEquals("Chainsaw", rentalAgreement.getToolType());
        assertEquals("Stihl", rentalAgreement.getToolBrand());
        assertEquals(5, rentalAgreement.getRentalDays());
        assertEquals("07/02/15", rentalAgreement.getCheckoutDate());
        assertEquals(1.49, rentalAgreement.getDailyRentalCharge());
        assertEquals(25, rentalAgreement.getDiscountPercent());
        assertEquals(3, rentalAgreement.getChargeDays());
        assertEquals(4.47, rentalAgreement.getPreDiscountCharge());
        assertEquals(1.12, rentalAgreement.getDiscountAmount());
        assertTrue(Math.abs(3.35 - rentalAgreement.getFinalCharge()) < 0.0001);
        assertEquals("07/07/15", rentalAgreement.getDueDate());
    }

    @Test
    public void test4() throws ParseException, RentalAgreement.InvalidFieldException, IOException {
        CheckoutManager checkoutManager = new CheckoutManager(new Catalog("prices.json", "tools.json"));
        RentalAgreement rentalAgreement = checkoutManager.checkout("JAKD", 6, 0, "09/03/15");
        assertNotNull(rentalAgreement);

        assertEquals("JAKD", rentalAgreement.getToolCode());
        assertEquals("Jackhammer", rentalAgreement.getToolType());
        assertEquals("DeWalt", rentalAgreement.getToolBrand());
        assertEquals(6, rentalAgreement.getRentalDays());
        assertEquals("09/03/15", rentalAgreement.getCheckoutDate());
        assertEquals(2.99, rentalAgreement.getDailyRentalCharge());
        assertEquals(0, rentalAgreement.getDiscountPercent());
        assertEquals(3, rentalAgreement.getChargeDays());
        assertEquals(8.97, rentalAgreement.getPreDiscountCharge());
        assertEquals(0, rentalAgreement.getDiscountAmount());
        assertEquals(8.97, rentalAgreement.getFinalCharge());
        assertEquals("09/09/15", rentalAgreement.getDueDate());
    }

    @Test
    public void test5() throws IOException, ParseException, RentalAgreement.InvalidFieldException {
        CheckoutManager checkoutManager = new CheckoutManager(new Catalog("prices.json", "tools.json"));
        RentalAgreement rentalAgreement = checkoutManager.checkout("JAKR", 9, 0, "07/02/15");
        assertNotNull(rentalAgreement);

        assertEquals("JAKR", rentalAgreement.getToolCode());
        assertEquals("Jackhammer", rentalAgreement.getToolType());
        assertEquals("Ridgid", rentalAgreement.getToolBrand());
        assertEquals(9, rentalAgreement.getRentalDays());
        assertEquals("07/02/15", rentalAgreement.getCheckoutDate());
        assertEquals(2.99, rentalAgreement.getDailyRentalCharge());
        assertEquals(0, rentalAgreement.getDiscountPercent());
        assertEquals(5, rentalAgreement.getChargeDays());
        assertEquals(14.95, rentalAgreement.getPreDiscountCharge());
        assertEquals(0, rentalAgreement.getDiscountAmount());
        assertEquals(14.95, rentalAgreement.getFinalCharge());
        assertEquals("07/11/15", rentalAgreement.getDueDate());
    }

    @Test
    public void test6() throws IOException, ParseException, RentalAgreement.InvalidFieldException {
        CheckoutManager checkoutManager = new CheckoutManager(new Catalog("prices.json", "tools.json"));
        RentalAgreement rentalAgreement = checkoutManager.checkout("JAKR", 4, 50, "07/02/20");
        assertNotNull(rentalAgreement);

        assertEquals("JAKR", rentalAgreement.getToolCode());
        assertEquals("Jackhammer", rentalAgreement.getToolType());
        assertEquals("Ridgid", rentalAgreement.getToolBrand());
        assertEquals(4, rentalAgreement.getRentalDays());
        assertEquals("07/02/20", rentalAgreement.getCheckoutDate());
        assertEquals(2.99, rentalAgreement.getDailyRentalCharge());
        assertEquals(50, rentalAgreement.getDiscountPercent());
        assertEquals(1, rentalAgreement.getChargeDays());
        assertEquals(2.99, rentalAgreement.getPreDiscountCharge());
        assertEquals(1.5, rentalAgreement.getDiscountAmount());
        assertTrue(Math.abs(1.49 - rentalAgreement.getFinalCharge()) < 0.0001);
        assertEquals("07/06/20", rentalAgreement.getDueDate());
    }
}