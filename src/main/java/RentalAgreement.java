import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        StringBuilder sb = new StringBuilder();
        sb.append("Tool code: ").append(getToolCode()).append("\n");
        sb.append("Tool type: ").append(getToolType()).append("\n");
        sb.append("Tool brand: ").append(getToolBrand()).append("\n");
        sb.append("Rental days: ").append(getRentalDays()).append("\n");
        sb.append("Check out date: ").append(getCheckoutDate()).append("\n");
        sb.append("Due date: ").append(getDueDate()).append("\n");
        sb.append("Daily rental charge: ").append(formatter.format(getDailyRentalCharge())).append("\n");
        sb.append("Charge days: ").append(getChargeDays()).append("\n");
        sb.append("Pre-discount charge: ").append(formatter.format(getPreDiscountCharge())).append("\n");
        sb.append("Discount percent: ").append(getDiscountPercent()).append("%\n");
        sb.append("Discount amount: ").append(formatter.format(getDiscountAmount())).append("\n");
        sb.append("Final charge: ").append(formatter.format(getFinalCharge())).append("\n");
        return sb.toString();
    }

    public static class RentalAgreementBuilder {

        private final RentalAgreement rentalAgreement;

        private RentalAgreementBuilder(Catalog.CatalogEntry entry) {
            rentalAgreement = new RentalAgreement(entry);
        }


        public RentalAgreementBuilder setCheckoutDate(String checkoutDate) throws InvalidFieldException {
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

        public RentalAgreementBuilder setDiscountPercent(int discountPercent) {
            rentalAgreement.discountPercent = discountPercent;
            return this;
        }

        public RentalAgreement build() throws InvalidFieldException, ParseException {
            // Validate user set fields before we generate derived fields
            validate();

            rentalAgreement.chargeDays = DateUtil.getChargedDays(rentalAgreement.checkoutDate, rentalAgreement.rentalDays,
                    rentalAgreement.weekdaysCharged, rentalAgreement.weekendsCharged, rentalAgreement.holidaysCharged);
            rentalAgreement.preDiscountCharge = rentalAgreement.getChargeDays() * rentalAgreement.getDailyRentalCharge();
            if (rentalAgreement.discountPercent > 0) {
                rentalAgreement.discountAmount = rentalAgreement.preDiscountCharge * (rentalAgreement.discountPercent / 100.0);
            }
            rentalAgreement.finalCharge = rentalAgreement.preDiscountCharge - rentalAgreement.discountAmount;
            rentalAgreement.dueDate = DateUtil.getDueDate(rentalAgreement.checkoutDate, rentalAgreement.rentalDays);

            return rentalAgreement;
        }

        private void validate() throws InvalidFieldException {
            if (rentalAgreement.getCheckoutDate() == null) {
                throw new InvalidFieldException("checkout_date", "Required field cannot be empty.");
            }

            if (rentalAgreement.getRentalDays() <= 0) {
                throw new InvalidFieldException("rental_days", "Must be greater than 0.");
            }

            if (rentalAgreement.getDiscountPercent() < 0 || rentalAgreement.getDiscountPercent() > 100) {
                throw new InvalidFieldException("discount_percent", "Must be in the range 0-100.");
            }

        }
    }

    public static class InvalidFieldException extends Exception {
        public InvalidFieldException (String field, String cause) {
            super("Invalid agreement. Field: " + field + " Cause: " + cause);
        }
    }
}
