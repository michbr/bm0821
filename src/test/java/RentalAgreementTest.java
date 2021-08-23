import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class RentalAgreementTest {

    @Test
    public void testCreate() throws Exception {
        Tool tool = new Tool("Ladder", "Werner", "LADW");
        PriceEntry priceEntry = new PriceEntry("Ladder", 0.99, true, true, true);
        Catalog.CatalogEntry item = new Catalog.CatalogEntry(priceEntry, tool);

        RentalAgreement.RentalAgreementBuilder builder = RentalAgreement.getBuilder(item);
        builder.setCheckoutDate("03/16/16")
                .setRentalDays(3)
                .setDiscountPercent(20);
        RentalAgreement agreement = builder.build();


        assertEquals("LADW", agreement.getToolCode());
        assertEquals("Ladder", agreement.getToolType());
        assertEquals("Werner", agreement.getToolBrand());
        assertEquals(3, agreement.getRentalDays());
        assertEquals("03/16/16", agreement.getCheckoutDate().toString());
        assertEquals(0.99, agreement.getDailyRentalCharge());
        assertEquals(20, agreement.getDiscountPercent());
        assertEquals(3, agreement.getChargeDays());
        assertEquals(2.97, agreement.getPreDiscountCharge());
        assertEquals(0.59, agreement.getDiscountAmount());
        assertTrue(Math.abs(2.38 - agreement.getFinalCharge()) < 0.0001);
        assertEquals("03/19/16", agreement.getDueDate());
    }

    @Test
    public void testCreate_InvalidCheckoutDate() {
        Tool tool = new Tool("Ladder", "Werner", "LADW");
        PriceEntry priceEntry = new PriceEntry("Ladder", 0.99, true, true, true);
        Catalog.CatalogEntry item = new Catalog.CatalogEntry(priceEntry, tool);

        RentalAgreement.RentalAgreementBuilder builder = RentalAgreement.getBuilder(item);

        assertThrows(RentalAgreement.InvalidFieldException.class, () -> builder.setCheckoutDate("03-16-16"));
    }

    @Test
    public void testCreate_NoCheckoutDate() throws RentalAgreement.InvalidFieldException {
        Tool tool = new Tool("Ladder", "Werner", "LADW");
        PriceEntry priceEntry = new PriceEntry("Ladder", 0.99, true, true, true);
        Catalog.CatalogEntry item = new Catalog.CatalogEntry(priceEntry, tool);

        RentalAgreement.RentalAgreementBuilder builder = RentalAgreement.getBuilder(item);
        builder.setRentalDays(3)
                .setDiscountPercent(20);
        assertThrows(RentalAgreement.InvalidFieldException.class, () -> builder.build());
    }

    @Test
    public void testCreate_NoRentalDays() throws RentalAgreement.InvalidFieldException {
        Tool tool = new Tool("Ladder", "Werner", "LADW");
        PriceEntry priceEntry = new PriceEntry("Ladder", 0.99, true, true, true);
        Catalog.CatalogEntry item = new Catalog.CatalogEntry(priceEntry, tool);

        RentalAgreement.RentalAgreementBuilder builder = RentalAgreement.getBuilder(item);
        builder.setCheckoutDate("03/16/16")
                .setDiscountPercent(20);
        assertThrows(RentalAgreement.InvalidFieldException.class, () -> builder.build());
    }

    @Test
    public void testCreate_InvalidDiscount() throws RentalAgreement.InvalidFieldException {
        Tool tool = new Tool("Ladder", "Werner", "LADW");
        PriceEntry priceEntry = new PriceEntry("Ladder", 0.99, true, true, true);
        Catalog.CatalogEntry item = new Catalog.CatalogEntry(priceEntry, tool);

        RentalAgreement.RentalAgreementBuilder builder = RentalAgreement.getBuilder(item);
        builder.setCheckoutDate("03/16/16")
                .setRentalDays(4);
        assertThrows(RentalAgreement.InvalidFieldException.class, () -> builder.setDiscountPercent(500));
        assertThrows(RentalAgreement.InvalidFieldException.class, () -> builder.setDiscountPercent(-5));
    }

    @Test
    public void testToString() throws ParseException, RentalAgreement.InvalidFieldException {
        Tool tool = new Tool("Ladder", "Werner", "LADW");
        PriceEntry priceEntry = new PriceEntry("Ladder", 0.99, true, true, true);
        Catalog.CatalogEntry item = new Catalog.CatalogEntry(priceEntry, tool);

        RentalAgreement.RentalAgreementBuilder builder = RentalAgreement.getBuilder(item);
        builder.setCheckoutDate("03/16/16")
                .setRentalDays(3)
                .setDiscountPercent(20);
        RentalAgreement agreement = builder.build();
        assertEquals("Tool code: LADW\n" +
                "Tool type: Ladder\n" +
                "Tool brand: Werner\n" +
                "Rental days: 3\n" +
                "Check out date: 03/16/16\n" +
                "Due date: 03/19/16\n" +
                "Daily rental charge: $0.99\n" +
                "Charge days: 3\n" +
                "Pre-discount charge: $2.97\n" +
                "Discount percent: 20%\n" +
                "Discount amount: $0.59\n" +
                "Final charge: $2.38\n", agreement.toString());

    }

}