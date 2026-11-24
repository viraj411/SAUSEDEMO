package bdd.pages;

import com.microsoft.playwright.Page;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CheckoutPage extends BasePage {

    private static final double PRICE_TOLERANCE = 0.01;

    private static final String FIRST_NAME_FIELD = "#first-name";
    private static final String LAST_NAME_FIELD = "#last-name";
    private static final String ZIP_CODE_FIELD = "#postal-code";
    private static final String CONTINUE_BUTTON = "#continue";
    private static final String FINISH_BUTTON = "#finish";
    private static final String CANCEL_BUTTON = "#cancel";
    private static final String ERROR_MESSAGE = "h3[data-test='error']";
    private static final String ERROR_CLOSE_BUTTON = ".error-button";
    private static final String PAGE_TITLE = ".title";
    private static final String OVERVIEW_SUMMARY = ".summary_info";
    private static final String ITEM_PRICES = ".inventory_item_price";
    private static final String ITEM_NAMES = ".inventory_item_name";
    private static final String SUBTOTAL_LABEL = ".summary_subtotal_label";
    private static final String TAX_LABEL = ".summary_tax_label";
    private static final String TOTAL_LABEL = ".summary_total_label";
    private static final String SUMMARY_VALUES = ".summary_value_label";
    private static final String SUCCESS_MESSAGE = ".complete-header";
    private static final String DISPATCH_MESSAGE = ".complete-text";
    private static final String BACK_TO_HOME_BUTTON = "#back-to-products";

    public CheckoutPage(Page page) {
        super(page);
    }

    public void enterShippingDetails(String firstName, String lastName, String zip) {
        fill(FIRST_NAME_FIELD, firstName);
        fill(LAST_NAME_FIELD, lastName);
        fill(ZIP_CODE_FIELD, zip);
        click(CONTINUE_BUTTON);
        page.locator(ERROR_MESSAGE).or(page.locator(OVERVIEW_SUMMARY)).first().waitFor();
    }

    public String getPageTitle() {
        return textOf(PAGE_TITLE);
    }

    public boolean areShippingFieldsDisplayed() {
        return isVisible(FIRST_NAME_FIELD)
                && isVisible(LAST_NAME_FIELD)
                && isVisible(ZIP_CODE_FIELD)
                && isVisible(CONTINUE_BUTTON);
    }

    public String getErrorText() {
        page.locator(ERROR_MESSAGE).waitFor();
        return textOf(ERROR_MESSAGE);
    }

    public boolean isErrorDisplayed() {
        return isVisible(ERROR_MESSAGE);
    }

    public void dismissError() {
        click(ERROR_CLOSE_BUTTON);
    }

    public String getFirstNameValue() {
        return valueOf(FIRST_NAME_FIELD);
    }

    public String getLastNameValue() {
        return valueOf(LAST_NAME_FIELD);
    }

    public String getZipCodeValue() {
        return valueOf(ZIP_CODE_FIELD);
    }

    public void clickCancel() {
        click(CANCEL_BUTTON);
        assertThat(page).not().hasURL(Pattern.compile(".*checkout-step.*"));
    }

    public List<String> getOverviewItemNames() {
        waitForUrl("checkout-step-two");
        return textsOf(ITEM_NAMES);
    }

    public List<String> getSummaryValues() {
        return textsOf(SUMMARY_VALUES);
    }

    public double getItemsTotal() {
        double total = 0.0;
        for (String price : page.locator(ITEM_PRICES).allInnerTexts()) {
            total += parsePrice(price);
        }
        return total;
    }

    public double getSubtotal() {
        return parsePrice(textOf(SUBTOTAL_LABEL));
    }

    public double getTax() {
        return parsePrice(textOf(TAX_LABEL));
    }

    public double getDisplayedTotal() {
        return parsePrice(textOf(TOTAL_LABEL));
    }

    public boolean doesSubtotalMatchItems() {
        return Math.abs(getItemsTotal() - getSubtotal()) < PRICE_TOLERANCE;
    }

    public boolean isTotalConsistent() {
        return Math.abs((getSubtotal() + getTax()) - getDisplayedTotal()) < PRICE_TOLERANCE;
    }

    public boolean isFinishButtonDisplayed() {
        return isVisible(FINISH_BUTTON);
    }

    public void completeOrder() {
        click(FINISH_BUTTON);
        waitForUrl("checkout-complete");
    }

    public String getSuccessMessage() {
        return textOf(SUCCESS_MESSAGE);
    }

    public String getDispatchMessage() {
        return textOf(DISPATCH_MESSAGE);
    }

    public void clickBackToHome() {
        click(BACK_TO_HOME_BUTTON);
        waitForUrl("inventory.html");
    }

    private String valueOf(String selector) {
        page.locator(selector).waitFor();
        return page.locator(selector).inputValue();
    }

    private double parsePrice(String priceText) {
        String numeric = priceText.replaceAll("[^0-9.]", "");
        if (numeric.isEmpty()) {
            throw new IllegalArgumentException("No price found in '" + priceText + "'");
        }
        return Double.parseDouble(numeric);
    }
}
