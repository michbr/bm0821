import java.text.ParseException;

public class CheckoutManager {

    private final Catalog catalog;

    public CheckoutManager(Catalog catalog) {
        this.catalog = catalog;
    }

    public RentalAgreement checkout(String toolCode, int rentalDayCount, int discountPercent, String checkoutDate) throws RentalAgreement.InvalidFieldException, ParseException {
        return RentalAgreement.getBuilder(catalog.getCatalogEntry(toolCode))
                .setCheckoutDate(checkoutDate)
                .setRentalDays(rentalDayCount)
                .setDiscountPercent(discountPercent)
                .build();
    }
}
