import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;

public class RentalAgreement {

    private final String toolCode;
    private final String toolType;
    private final String toolBrand;
    private final double dailyRentalCharge;
    private boolean weekendsCharged;
    private boolean weekdaysCharged;
    private boolean holidaysCharged;
    private int rentalDays;
    private String checkoutDate;
    private int discountPercent;
    private int chargeDays;
    private double preDiscountCharge;
    private double discountAmount;
    private double finalCharge;
    private String dueDate;

    public static RentalAgreementBuilder getBuilder(Catalog.CatalogEntry catalogEntry) {
        return new RentalAgreementBuilder(catalogEntry);
    }

    private RentalAgreement(Catalog.CatalogEntry catalogEntry) {
        this.toolCode = catalogEntry.getTool().id();
        this.toolType = catalogEntry.getTool().type();
        this.toolBrand = catalogEntry.getTool().brand();
        this.dailyRentalCharge = catalogEntry.getPricing().dailyCharge();
        weekdaysCharged = catalogEntry.getPricing().weekdayCharge();
        weekendsCharged = catalogEntry.getPricing().weekendCharge();
        holidaysCharged = catalogEntry.getPricing().holidayCharge();
    }

    public String getToolCode() {
        return toolCode;
    }

    public String getToolType() {
        return toolType;
    }

    public String getToolBrand() {
        return toolBrand;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public double getDailyRentalCharge() {
        return dailyRentalCharge;
    }

    public int getChargeDays() {
        return chargeDays;
    }

    public double getPreDiscountCharge() {
        return preDiscountCharge;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public double getFinalCharge() {
        return finalCharge;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    @Override
    public String toString() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        return "Tool code: " + getToolCode() + "\n" +
                "Tool type: " + getToolType() + "\n" +
                "Tool brand: " + getToolBrand() + "\n" +
                "Rental days: " + getRentalDays() + "\n" +
                "Check out date: " + getCheckoutDate() + "\n" +
                "Due date: " + getDueDate() + "\n" +
                "Daily rental charge: " + formatter.format(getDailyRentalCharge()) + "\n" +
                "Charge days: " + getChargeDays() + "\n" +
                "Pre-discount charge: " + formatter.format(getPreDiscountCharge()) + "\n" +
                "Discount percent: " + getDiscountPercent() + "%\n" +
                "Discount amount: " + formatter.format(getDiscountAmount()) + "\n" +
                "Final charge: " + formatter.format(getFinalCharge()) + "\n";
    }

    public void printRentalAgreement() {
        System.out.println(this);
    }

    public static class RentalAgreementBuilder {

        public static final String FIELD_DISCOUNT_PERCENT = "discount_percent";
        public static final String FIELD_RENTAL_DAYS = "rental_days";
        public static final String FIELD_CHECKOUT_DATE = "checkout_date";
        private final RentalAgreement rentalAgreement;

        private RentalAgreementBuilder(Catalog.CatalogEntry entry) {
            rentalAgreement = new RentalAgreement(entry);
        }


        public RentalAgreementBuilder setCheckoutDate(String checkoutDate) throws InvalidFieldException {
            if (checkoutDate == null) {
                throw new InvalidFieldException(FIELD_CHECKOUT_DATE, "Checkout date cannot be empty.");
            }
            try {
                rentalAgreement.checkoutDate = checkoutDate;
                DateUtil.validateDate(rentalAgreement.checkoutDate);
            } catch (ParseException e) {
                InvalidFieldException invalidFieldException = new InvalidFieldException("checkout_date", "Date format must be mm/dd/yy");
                invalidFieldException.initCause(e);
                throw invalidFieldException;
            }
            return this;
        }

        public RentalAgreementBuilder setRentalDays(int rentalDays) throws InvalidFieldException {
            rentalAgreement.rentalDays = rentalDays;
            return this;
        }

        public RentalAgreementBuilder setDiscountPercent(int discountPercent) throws InvalidFieldException {
            if (discountPercent < 0 || discountPercent > 100) {
                throw new InvalidFieldException(FIELD_DISCOUNT_PERCENT, "Must be in the range 0-100.");
            }
            rentalAgreement.discountPercent = discountPercent;
            return this;
        }

        public RentalAgreement build() throws InvalidFieldException, ParseException {
            // Validate user set fields before we generate derived fields
            validate();

            // Set charge days, pre-discount charge, discount amount, final charge, and due date
            rentalAgreement.chargeDays = DateUtil.getChargedDays(rentalAgreement.checkoutDate, rentalAgreement.rentalDays,
                    rentalAgreement.weekdaysCharged, rentalAgreement.weekendsCharged, rentalAgreement.holidaysCharged);
            rentalAgreement.preDiscountCharge =
                    BigDecimal.valueOf(rentalAgreement.getChargeDays() * rentalAgreement.getDailyRentalCharge()).setScale(2, RoundingMode.HALF_UP).doubleValue();
            if (rentalAgreement.discountPercent > 0) {
                rentalAgreement.discountAmount =
                        BigDecimal.valueOf(rentalAgreement.preDiscountCharge * (rentalAgreement.discountPercent / 100.0)).setScale(2, RoundingMode.HALF_UP).doubleValue();
            }
            rentalAgreement.finalCharge = rentalAgreement.preDiscountCharge - rentalAgreement.discountAmount;
            rentalAgreement.dueDate = DateUtil.getDueDate(rentalAgreement.checkoutDate, rentalAgreement.rentalDays);

            return rentalAgreement;
        }

        private void validate() throws InvalidFieldException {
            if (rentalAgreement.getCheckoutDate() == null) {
                throw new InvalidFieldException(FIELD_CHECKOUT_DATE, "Required field cannot be empty.");
            }

            if (rentalAgreement.getRentalDays() <= 0) {
                throw new InvalidFieldException(FIELD_RENTAL_DAYS, "Must be greater than 0.");
            }
        }
    }

    public static class InvalidFieldException extends Exception {
        public InvalidFieldException(String field, String cause) {
            super("Invalid agreement. Field: " + field + " Cause: " + cause);
        }
    }
}
